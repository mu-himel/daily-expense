package app.muhimel.daily_accounts_api.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MonthlyExpenseTest {

    private MonthlyExpense monthlyExpense;

    @BeforeEach
    void setUp(){
        monthlyExpense = initiateMonthlyExpense();
    }

    private MonthlyExpense initiateMonthlyExpense(){
        Product p1 = new Product(1L,"Fish","kg");
        ItemCost itemCost1 = new ItemCost(p1,1d,new BigDecimal(220));
        Product p2 = new Product(1L,"Rice","kg");
        ItemCost itemCost2 = new ItemCost(p1,5d,BigDecimal.valueOf(64));

        DailyExpense dailyExpense1 = new DailyExpense(LocalDate.of(2026,9,1));
        dailyExpense1.setItemCostList(List.of(itemCost1,itemCost2));

        Product p3 = new Product(1L,"Egg","Pcs");
        ItemCost itemCost3 = new ItemCost(p1,12d,new BigDecimal(13.33));
        Product p4 = new Product(1L,"Milk","ltr");
        ItemCost itemCost4 = new ItemCost(p1,.5d,new BigDecimal(110));
        DailyExpense dailyExpense2 = new DailyExpense(LocalDate.of(2026,9,2));
        dailyExpense2.setItemCostList(List.of(itemCost3,itemCost4));

        MonthlyExpense monthlyExpense = new MonthlyExpense();
        monthlyExpense.setYear(LocalDate.now().getYear());
        monthlyExpense.setNumberOfMonth(LocalDate.now().getMonthValue());
        monthlyExpense.setDailyExpenseList(List.of(dailyExpense1,dailyExpense2));
        return monthlyExpense;
    }

    @Test
    void test_CalculateRunningCost(){
        assertThat(monthlyExpense).extracting(MonthlyExpense::getRunningCost).isEqualTo(new BigDecimal("755.0"));

    }
}
