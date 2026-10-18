package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.entity.*;
import app.muhimel.daily_accounts_api.repository.DailyExpenseRepository;
import app.muhimel.daily_accounts_api.repository.MonthlyExpenseRepository;
import app.muhimel.daily_accounts_api.request.DailyExpenseRequest;
import app.muhimel.daily_accounts_api.request.ItemCostRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DailyExpenseServiceTest {

    @Mock
    private DailyExpenseRepository dailyExpenseRepository;

    @InjectMocks
    private DailyExpenseService dailyExpenseService;

    @Mock
    private MonthlyExpenseService monthlyExpenseService;

    @Test
    void test_ThrowExceptionWhenMonthlyExpenseRecordNotFound() {
        // extract date to get month and year
        ItemCostRequest itr1 = new ItemCostRequest(1L, 1d, new BigDecimal(220));
        ItemCostRequest itr2 = new ItemCostRequest(2L, 5d, new BigDecimal(60));
        DailyExpenseRequest der = new DailyExpenseRequest("2026-09-01", List.of(itr1, itr2));
        Integer month = der.getCurrentDate().getMonthValue();
        Integer year = der.getCurrentDate().getYear();
        when(monthlyExpenseService.fetchMonthlyExpenseByMonthAndYear(month,year)).thenReturn(Optional.empty());
        assertThatThrownBy(()->dailyExpenseService.createNewDailyExpense(der))
                .isInstanceOf(RuntimeException.class)
                .extracting("message")
                .isEqualTo("Sorry Monthly Expense not setup yet for the month and year");;
        verify(dailyExpenseRepository,never()).save(any(DailyExpense.class));
    }

    // create daily expense with item cost for a date
    @Test
    void test_CreateNewDailyExpenseWithItemCostForADate(){
        // extract date to get month and year
        ItemCostRequest itr1 = new ItemCostRequest(1L,1d,new BigDecimal(220));
        ItemCostRequest itr2 = new ItemCostRequest(2L,5d,new BigDecimal(60));
        DailyExpenseRequest der = new DailyExpenseRequest("2026-09-01", List.of(itr1,itr2));
        Integer month = der.getCurrentDate().getMonthValue();
        Integer year = der.getCurrentDate().getYear();
        // find monthly expense record
        when(monthlyExpenseService.fetchMonthlyExpenseByMonthAndYear(month,year)).thenReturn(Optional.of(new MonthlyExpense(1L,month,year)));
        when(dailyExpenseRepository.save(any(DailyExpense.class))).thenReturn(new DailyExpense(der.getCurrentDate(),new BigDecimal(520)));
        MonthlyExpense monthlyExpense = monthlyExpenseService.fetchMonthlyExpenseByMonthAndYear(month,year).orElse(null);
        DailyExpense deSaved = dailyExpenseService.createNewDailyExpense(der);
        assertThat(deSaved).isNotNull();
        assertThat(deSaved.getTotalCost()).isEqualTo(new BigDecimal(520));
        verify(dailyExpenseRepository).save(any(DailyExpense.class));

    }

    // add new item cost entry to an existing daily expense
    void test_AddNewItemCostToAnExistingDailyExpense(){

    }
}
