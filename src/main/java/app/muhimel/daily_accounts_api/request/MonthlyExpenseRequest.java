package app.muhimel.daily_accounts_api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyExpenseRequest {
    private Integer month;
    private Integer year;
}
