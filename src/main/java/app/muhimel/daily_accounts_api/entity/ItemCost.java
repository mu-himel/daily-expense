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

    @ManyToOne(fetch = FetchType.LAZY)
    private DailyExpense dailyExpense;
}
