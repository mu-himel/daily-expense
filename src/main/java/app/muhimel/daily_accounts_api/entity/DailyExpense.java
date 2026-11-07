package app.muhimel.daily_accounts_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class DailyExpense extends BaseEntity{



    @Column(name = "expense_date")
    private LocalDate currentDate;

    @OneToMany(mappedBy = "dailyExpense",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCost> itemCostList = new ArrayList<>();

    private BigDecimal totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    private MonthlyExpense monthlyExpense;

    public DailyExpense(LocalDate currentDate){
        this.currentDate = currentDate;
    }

    public DailyExpense(Long id, LocalDate d, BigDecimal totalCost){
        this.setId(id);
        this.currentDate = d;
        this.totalCost = totalCost;
    }

    public DailyExpense(LocalDate d, BigDecimal totalCost){
        this.currentDate = d;
        this.totalCost = totalCost;
    }

    DailyExpense addItemCost(ItemCost itemCost){
        itemCostList.add(itemCost);
        this.totalCost = totalCost.add(itemCost.getUnitPrice());
        return this;
    }

    public void setItemCostList(List<ItemCost> itemCostList) {
        this.itemCostList = itemCostList;
        this.totalCost = this.itemCostList.stream().map(
                itemCost ->itemCost.getUnitPrice().multiply(new BigDecimal(itemCost.getQty()))
        ).reduce(new BigDecimal(0),BigDecimal::add);
    }
}
