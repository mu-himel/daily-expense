package app.muhimel.daily_accounts_api.service;

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
        assertThatThrownBy(()->monthlyExpenseService.createMonthlyExpense(new MonthlyExpenseRequest(null,2026)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void test_ThrowExceptionIfMonthlyExpenseAlreadyExist(){
        Integer month = 9;
        Integer year = 2026;
        MonthlyExpense monthlyExpense = existMonthlyExpense(month,year);
        when(monthlyExpenseRepository.findByNumberOfMonthAndYear(month,year)).thenReturn(monthlyExpense);
        assertThatThrownBy(()->monthlyExpenseService.createMonthlyExpense(new MonthlyExpenseRequest(month,year)))
                .isInstanceOf(RuntimeException.class);
        verify(monthlyExpenseRepository, never()).save(any());
    }

    @Test
    void test_CreateNewMonthlyExpenseIfNotExist(){
            Integer month = 9;
            Integer year = 2026;

            when(monthlyExpenseRepository.findByNumberOfMonthAndYear(month,year)).thenReturn(null);
            when(monthlyExpenseRepository.save(any(MonthlyExpense.class))).thenAnswer((inv)->inv.getArgument(0));
            MonthlyExpenseRequest request = new MonthlyExpenseRequest(month, year);
            monthlyExpenseService.createMonthlyExpense(request);
            ArgumentCaptor<MonthlyExpense> captor = ArgumentCaptor.forClass(MonthlyExpense.class);
            verify(monthlyExpenseRepository).save(captor.capture());
            assertThat(captor.getValue().getNumberOfMonth()).isEqualTo(month);
            assertThat(captor.getValue().getYear()).isEqualTo(year);
    }
}
