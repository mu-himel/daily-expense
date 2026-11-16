package app.muhimel.daily_accounts_api.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyExpenseRequest {
    @NotNull(message = "Month is Required")
    private Integer month;

    @NotNull(message = "Year is Required")
    private Integer year;

    @NotNull(message = "Budget amount is Required")
    private BigDecimal budgetAmount;
}
