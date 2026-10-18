package app.muhimel.daily_accounts_api.repository;

import app.muhimel.daily_accounts_api.entity.DailyExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyExpenseRepository extends JpaRepository<DailyExpense,Long> {
}
