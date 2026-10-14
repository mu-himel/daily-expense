package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.entity.MonthlyExpense;
import app.muhimel.daily_accounts_api.repository.MonthlyExpenseRepository;
import app.muhimel.daily_accounts_api.request.MonthlyExpenseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return Optional.ofNullable(monthlyExpenseRepository.findByNumberOfMonthAndYear(month,year));
    }

    @Transactional
    public MonthlyExpense createMonthlyExpense(MonthlyExpenseRequest request) {

        Optional<MonthlyExpense> op = fetchMonthlyExpenseByMonthAndYear(request.getMonth(), request.getYear());
        MonthlyExpense monthlyExpense = new MonthlyExpense(request.getMonth(),request.getYear());
        if(op.isPresent()){
            throw new RuntimeException("Monthly Expense Already Exist");

        }
        return monthlyExpenseRepository.save(monthlyExpense);
    }
}
