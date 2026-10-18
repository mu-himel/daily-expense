package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.entity.DailyExpense;
import app.muhimel.daily_accounts_api.entity.ItemCost;
import app.muhimel.daily_accounts_api.entity.Product;
import app.muhimel.daily_accounts_api.repository.DailyExpenseRepository;
import app.muhimel.daily_accounts_api.repository.ItemCostRepository;
import app.muhimel.daily_accounts_api.repository.ProductRepository;
import app.muhimel.daily_accounts_api.request.ItemCostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemCostService {

    private final ItemCostRepository itemCostRepository;
    private final ProductRepository productRepository;
    private final DailyExpenseRepository dailyExpenseRepository;

    public ItemCost addNewItemCost(ItemCostRequest itemCostRequest){

        Optional<DailyExpense> dailyExpenseOp = dailyExpenseRepository.findById(itemCostRequest.getDailyExpenseId());
        if(dailyExpenseOp.isEmpty()){
            throw new RuntimeException("Sorry! Daily Expense Required");
        }

        Optional<Product> productOp = productRepository.findById(itemCostRequest.getItemId());
        if(productOp.isEmpty()){
            throw new RuntimeException("Sorry! Product not found");
        }

        DailyExpense dailyExpense = dailyExpenseOp.get();
        Product product = productOp.get();

        ItemCost itemCost = new ItemCost(product,itemCostRequest.getQty(), itemCostRequest.getUnitPrice(),dailyExpense);
        return itemCostRepository.save(itemCost);
    }
}
