package app.muhimel.daily_accounts_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class MonthlyExpense extends BaseEntity{


    private Integer numberOfMonth;
    private Integer year;

    @OneToMany(mappedBy = "monthlyExpense")
    private List<DailyExpense> dailyExpenseList;

    private BigDecimal runningCost;

    public void setDailyExpenseList(List<DailyExpense> dailyExpenseList) {
        this.dailyExpenseList = dailyExpenseList;
        this.runningCost = this.dailyExpenseList.stream().reduce(new BigDecimal(0),
                (sum,dailyExpense) -> sum.add(dailyExpense.getTotalCost().setScale(1,RoundingMode.UP)),BigDecimal::add);
    }
}
