package app.muhimel.daily_accounts_api.controller;

import app.muhimel.daily_accounts_api.dto.MonthlyExpenseResponse;
import app.muhimel.daily_accounts_api.request.MonthlyExpenseRequest;
import app.muhimel.daily_accounts_api.service.MonthlyExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/monthly-expenses")
@RequiredArgsConstructor
public class MonthlyExpenseController {

    private final MonthlyExpenseService monthlyExpenseService;

    @PostMapping
    public ResponseEntity<Void> addMonthlyExpense(@RequestBody @Valid MonthlyExpenseRequest req){
        monthlyExpenseService.createMonthlyExpense(req);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MonthlyExpenseResponse>> getMonthlyExpenses(@RequestParam("year") Integer year){
        return new ResponseEntity<>(monthlyExpenseService.getAllMonthlyExpensesByYear(year),
                HttpStatus.OK);
    }
}
