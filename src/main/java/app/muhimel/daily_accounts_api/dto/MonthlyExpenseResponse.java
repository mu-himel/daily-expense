package app.muhimel.daily_accounts_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyExpenseResponse {
    private Integer numberOfMonth;
    private Integer year;
    private BigDecimal budgetAmount;
}
