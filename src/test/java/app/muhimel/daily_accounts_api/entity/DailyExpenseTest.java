package app.muhimel.daily_accounts_api.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DailyExpenseTest {

    @Test
    void test_createDailyExpenseObject(){

        ItemCost itemCost1 = new ItemCost(new Product(1L,"Fish","Kg"),1d,new BigDecimal(220));
        ItemCost itemCost2 = new ItemCost(new Product(2L,"Potato","Kg"),1d,new BigDecimal(30));
        DailyExpense dailyExpense = new DailyExpense(LocalDate.now(),new BigDecimal(0));
        dailyExpense.addItemCost(itemCost1).addItemCost(itemCost2);
        assertThat(dailyExpense).hasFieldOrPropertyWithValue("totalCost",new BigDecimal(250));

    }
}
