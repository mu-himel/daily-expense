package app.muhimel.daily_accounts_api.request;

import app.muhimel.daily_accounts_api.entity.ItemCost;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DailyExpenseRequest {
    private LocalDate currentDate;
    private List<ItemCostRequest> itemCostList = new ArrayList<>();

    public DailyExpenseRequest(String currentDate, List<ItemCostRequest> itemCostList){
        this.currentDate = LocalDate.parse(currentDate);
        this.itemCostList = itemCostList;
    }
}
