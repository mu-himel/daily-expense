package app.muhimel.daily_accounts_api.repository;

import app.muhimel.daily_accounts_api.entity.MonthlyExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyExpenseRepository extends JpaRepository<MonthlyExpense,Long> {


    MonthlyExpense findByNumberOfMonthAndYear(Integer month, Integer year);
}
