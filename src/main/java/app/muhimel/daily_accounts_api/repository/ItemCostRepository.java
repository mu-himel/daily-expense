package app.muhimel.daily_accounts_api.repository;

import app.muhimel.daily_accounts_api.entity.ItemCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCostRepository extends JpaRepository<ItemCost,Long> {
}
