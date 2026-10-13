package app.muhimel.daily_accounts_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class MonthlyExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberOfMonth;
    private Integer year;

    @OneToMany(mappedBy = "monthlyExpense")
    private List<DailyExpense> dailyExpenseList;

    private BigDecimal runningCost;
}
