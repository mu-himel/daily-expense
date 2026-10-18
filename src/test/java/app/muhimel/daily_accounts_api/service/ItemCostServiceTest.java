package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.entity.DailyExpense;
import app.muhimel.daily_accounts_api.entity.ItemCost;
import app.muhimel.daily_accounts_api.entity.Product;
import app.muhimel.daily_accounts_api.repository.DailyExpenseRepository;
import app.muhimel.daily_accounts_api.repository.ItemCostRepository;
import app.muhimel.daily_accounts_api.repository.ProductRepository;
import app.muhimel.daily_accounts_api.request.ItemCostRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemCostServiceTest {

    @Mock
    private ItemCostRepository itemCostRepository;

    @Mock
    private DailyExpenseRepository dailyExpenseRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ItemCostService itemCostService;


    // add new item cost entry to an existing daily expense
    @Test
    void test_AddNewItemCostToAnExistingDailyExpense(){
        ItemCostRequest itr1 = new ItemCostRequest(1L,1d,new BigDecimal(220), 1L);
        DailyExpense dailyExpense = new DailyExpense(1L, LocalDate.parse("2026-09-01"),new BigDecimal(520));
        ItemCost itemSaved = new ItemCost(new Product(3L),itr1.getQty(), itr1.getUnitPrice(),dailyExpense);
        when(dailyExpenseRepository.findById(itr1.getDailyExpenseId())).thenReturn(
                Optional.of(dailyExpense));
        when(productRepository.findById(itr1.getItemId())).thenReturn(Optional.of(new Product(3L)));
        when(itemCostRepository.save(any(ItemCost.class))).thenReturn(itemSaved);
        ItemCost itemCost = itemCostService.addNewItemCost(itr1);
        assertThat(itemCost.getItem().getId()).isEqualTo(3L);
        verify(itemCostRepository).save(any(ItemCost.class));
    }

}
