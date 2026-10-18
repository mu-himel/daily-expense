package app.muhimel.daily_accounts_api.request;

import app.muhimel.daily_accounts_api.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data

public class ItemCostRequest {
    private Long itemId;
    private Double qty;
    private BigDecimal unitPrice;
    private Long dailyExpenseId;

    public ItemCostRequest(Long itemId, Double qty, BigDecimal unitPrice){
        this.itemId = itemId;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public ItemCostRequest(Long itemId, Double qty, BigDecimal unitPrice, Long dailyExpenseId){
        this.itemId = itemId;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.dailyExpenseId = dailyExpenseId;
    }
}
