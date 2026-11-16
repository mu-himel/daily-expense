package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.dto.MonthlyExpenseResponse;
import app.muhimel.daily_accounts_api.entity.MonthlyExpense;
import app.muhimel.daily_accounts_api.repository.MonthlyExpenseRepository;
import app.muhimel.daily_accounts_api.request.MonthlyExpenseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MonthlyExpenseService {

    private final MonthlyExpenseRepository monthlyExpenseRepository;

    Optional<MonthlyExpense> fetchMonthlyExpenseByMonthAndYear(Integer month, Integer year){
        if(Objects.isNull(month)){
            throw new RuntimeException("Month is required");
        }
        if(Objects.isNull(year)){
            throw new RuntimeException("Year is required");
        }
        if(month<=0 || month>12){
            throw new RuntimeException("Month should be within 1-12");
        }
        return Optional.ofNullable(monthlyExpenseRepository.findByNumberOfMonthAndYear(month,year));
    }

    @Transactional
    public MonthlyExpense createMonthlyExpense(MonthlyExpenseRequest request) {

        if(Objects.isNull(request.getBudgetAmount())){
            throw new RuntimeException("Budget amount is required");
        }
        Optional<MonthlyExpense> op = fetchMonthlyExpenseByMonthAndYear(request.getMonth(), request.getYear());
        if(op.isPresent()){
            throw new RuntimeException("Monthly Expense Already Exist");

        }
        MonthlyExpense monthlyExpense = new MonthlyExpense(request.getMonth(),request.getYear(),request.getBudgetAmount());
        return monthlyExpenseRepository.save(monthlyExpense);
    }

    @Transactional(readOnly = true)
    public List<MonthlyExpenseResponse> getAllMonthlyExpensesByYear(Integer year) {
        return monthlyExpenseRepository.findByYear(year).stream()
                .map(monthlyExpense -> new MonthlyExpenseResponse(
                        monthlyExpense.getNumberOfMonth(),
                        monthlyExpense.getYear(),
                        monthlyExpense.getBudgetAmount()))
                .toList();
    }
}
