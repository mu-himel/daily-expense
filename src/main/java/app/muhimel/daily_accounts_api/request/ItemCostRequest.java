package app.muhimel.daily_accounts_api.request;

import app.muhimel.daily_accounts_api.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ItemCostRequest {
    private Long itemId;
    private Double qty;
    private BigDecimal unitPrice;
}
