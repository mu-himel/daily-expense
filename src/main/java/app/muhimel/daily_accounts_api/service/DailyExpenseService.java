package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.entity.DailyExpense;
import app.muhimel.daily_accounts_api.entity.ItemCost;
import app.muhimel.daily_accounts_api.entity.MonthlyExpense;
import app.muhimel.daily_accounts_api.entity.Product;
import app.muhimel.daily_accounts_api.repository.DailyExpenseRepository;
import app.muhimel.daily_accounts_api.request.DailyExpenseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyExpenseService {

    private final MonthlyExpenseService monthlyExpenseService;
    private final DailyExpenseRepository dailyExpenseRepository;

    public DailyExpense createNewDailyExpense(DailyExpenseRequest der) {

        Integer month = der.getCurrentDate().getMonthValue();
        Integer year = der.getCurrentDate().getYear();
        Optional<MonthlyExpense> monthlyExpenseOp = monthlyExpenseService.fetchMonthlyExpenseByMonthAndYear(month,year);
        if(monthlyExpenseOp.isEmpty()){
            throw new RuntimeException("Sorry Monthly Expense not setup yet for the month and year");
        }
        // find and map product from db by product id
        Map<Long, Product> productMap = new HashMap<>(); // bring products from db and map also check performance  with this strategy
        productMap.put(1L, new Product(1L,"Fish","Kg"));
        productMap.put(2L, new Product(2L, "Rice", "Kg"));

        // create daily expense entity from daily expense request
        DailyExpense de = new DailyExpense(der.getCurrentDate());
        List<ItemCost> itemCostList =der.getItemCostList().stream().map(itr->
                new ItemCost(
                        productMap.get(itr.getItemId()),
                        itr.getQty(),
                        itr.getUnitPrice(),
                        de
                )).toList();
        de.setItemCostList(itemCostList);

        // attach to monthly expense
        de.setMonthlyExpense(monthlyExpenseOp.get());

        return  dailyExpenseRepository.save(de);
    }
}
