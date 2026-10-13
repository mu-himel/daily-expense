package app.muhimel.daily_accounts_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Data
public class DailyExpense extends BaseEntity{


    private final LocalDate currentDate;

    @OneToMany(mappedBy = "dailyExpense")
    private List<ItemCost> itemCostList = new ArrayList<>();

    private BigDecimal totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    private MonthlyExpense monthlyExpense;

    DailyExpense(LocalDate d, BigDecimal totalCost){
        this.currentDate = d;
        this.totalCost = totalCost;
    }

    DailyExpense addItemCost(ItemCost itemCost){
        itemCostList.add(itemCost);
        this.totalCost = totalCost.add(itemCost.getUnitPrice());
        return this;
    }
}
