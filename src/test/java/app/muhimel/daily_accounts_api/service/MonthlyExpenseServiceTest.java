package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.dto.MonthlyExpenseResponse;
import app.muhimel.daily_accounts_api.entity.DailyExpense;
import app.muhimel.daily_accounts_api.entity.ItemCost;
import app.muhimel.daily_accounts_api.entity.MonthlyExpense;
import app.muhimel.daily_accounts_api.entity.Product;
import app.muhimel.daily_accounts_api.repository.MonthlyExpenseRepository;
import app.muhimel.daily_accounts_api.request.MonthlyExpenseRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MonthlyExpenseServiceTest {

    @Mock
    MonthlyExpenseRepository monthlyExpenseRepository;

    @InjectMocks
    MonthlyExpenseService monthlyExpenseService;

    private MonthlyExpense existMonthlyExpense(Integer m, Integer y){
        Product p1 = new Product(1L,"Fish","kg");
        ItemCost itemCost1 = new ItemCost(p1,1d,new BigDecimal(220));
        Product p2 = new Product(1L,"Rice","kg");
        ItemCost itemCost2 = new ItemCost(p1,5d,BigDecimal.valueOf(64));

        DailyExpense dailyExpense1 = new DailyExpense(LocalDate.of(y,m,1));
        dailyExpense1.setItemCostList(List.of(itemCost1,itemCost2));
        MonthlyExpense monthlyExpense = new MonthlyExpense(m,y);
        monthlyExpense.setId(1L);
        monthlyExpense.setDailyExpenseList(List.of(dailyExpense1));
        return monthlyExpense;
    }

    @Test
    void test_ThrowExceptionIfMonthIsNull(){
        assertThatThrownBy(()->monthlyExpenseService.createMonthlyExpense(new MonthlyExpenseRequest(null,2026,new BigDecimal("1000"))))
                .isInstanceOf(RuntimeException.class).extracting("message").isEqualTo("Month is required");
    }

    @Test
    void test_ThrowExceptionIfYearIsNull(){
        assertThatThrownBy(()->monthlyExpenseService.createMonthlyExpense(new MonthlyExpenseRequest(9,null,new BigDecimal("1000"))))
                .isInstanceOf(RuntimeException.class).extracting("message").isEqualTo("Year is required");
    }

    @Test
    void test_ThrowExceptionIfBudgetAmountIsNull(){
        assertThatThrownBy(()->monthlyExpenseService.createMonthlyExpense(new MonthlyExpenseRequest(9,2026,null)))
                .isInstanceOf(RuntimeException.class).extracting("message").isEqualTo("Budget amount is required");
        verifyNoInteractions(monthlyExpenseRepository);
    }

    @Test
    void test_ThrowExceptionIfMonthIsOutOfBoundaryValues(){
        assertThatThrownBy(()->monthlyExpenseService.createMonthlyExpense(new MonthlyExpenseRequest(0,2026,new BigDecimal("1000"))))
                .isInstanceOf(RuntimeException.class).extracting("message").isEqualTo("Month should be within 1-12");
    }

    @Test
    void test_ThrowExceptionIfMonthlyExpenseAlreadyExist(){
        Integer month = 9;
        Integer year = 2026;
        MonthlyExpense monthlyExpense = existMonthlyExpense(month,year);
        when(monthlyExpenseRepository.findByNumberOfMonthAndYear(month,year)).thenReturn(monthlyExpense);
        assertThatThrownBy(()->monthlyExpenseService.createMonthlyExpense(new MonthlyExpenseRequest(month,year,new BigDecimal("1000"))))
                .isInstanceOf(RuntimeException.class);
        verify(monthlyExpenseRepository, never()).save(any());
    }

    @Test
    void test_CreateNewMonthlyExpenseIfNotExist(){
            Integer month = 9;
            Integer year = 2026;

            when(monthlyExpenseRepository.findByNumberOfMonthAndYear(month,year)).thenReturn(null);
            when(monthlyExpenseRepository.save(any(MonthlyExpense.class))).thenAnswer((inv)->inv.getArgument(0));
            MonthlyExpenseRequest request = new MonthlyExpenseRequest(month, year, new BigDecimal("5000.50"));
            monthlyExpenseService.createMonthlyExpense(request);
            ArgumentCaptor<MonthlyExpense> captor = ArgumentCaptor.forClass(MonthlyExpense.class);
            verify(monthlyExpenseRepository).save(captor.capture());
            assertThat(captor.getValue().getNumberOfMonth()).isEqualTo(month);
            assertThat(captor.getValue().getYear()).isEqualTo(year);
            assertThat(captor.getValue().getBudgetAmount()).isEqualTo(new BigDecimal("5000.50"));
    }

    @Test
    void test_GetAllMonthlyExpensesByYear(){
        int requestedYear = 2026;
        MonthlyExpense january = new MonthlyExpense(1, requestedYear, new BigDecimal("15000.00"));
        MonthlyExpense february = new MonthlyExpense(2, requestedYear, new BigDecimal("12000.00"));
        when(monthlyExpenseRepository.findByYear(requestedYear)).thenReturn(List.of(january, february));

        List<MonthlyExpenseResponse> result = monthlyExpenseService.getAllMonthlyExpensesByYear(requestedYear);

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0).getNumberOfMonth()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getYear()).isEqualTo(requestedYear);
        Assertions.assertThat(result.get(0).getBudgetAmount()).isEqualByComparingTo("15000.00");
        Assertions.assertThat(result.get(1).getNumberOfMonth()).isEqualTo(2);
        Assertions.assertThat(result.get(1).getYear()).isEqualTo(requestedYear);
        Assertions.assertThat(result.get(1).getBudgetAmount()).isEqualByComparingTo("12000.00");
        verify(monthlyExpenseRepository).findByYear(requestedYear);
        verifyNoMoreInteractions(monthlyExpenseRepository);
    }
}
