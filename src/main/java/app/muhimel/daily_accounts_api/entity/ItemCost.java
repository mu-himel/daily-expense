package app.muhimel.daily_accounts_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class ItemCost extends BaseEntity{



    private Product item;
    private Double qty;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public ItemCost(Product item, Double qty, BigDecimal unitPrice){
        this.item = item;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(this.qty));
    }

    public ItemCost(Product item, Double qty, BigDecimal unitPrice, DailyExpense de){
        this.item = item;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.dailyExpense = de;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private DailyExpense dailyExpense;
}
