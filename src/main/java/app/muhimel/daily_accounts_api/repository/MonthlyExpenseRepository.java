package app.muhimel.daily_accounts_api.repository;

import app.muhimel.daily_accounts_api.entity.MonthlyExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyExpenseRepository extends JpaRepository<MonthlyExpense,Long> {


    MonthlyExpense findByNumberOfMonthAndYear(Integer month, Integer year);

    List<MonthlyExpense> findByYear(Integer year);
}
