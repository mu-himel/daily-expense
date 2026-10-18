package app.muhimel.daily_accounts_api.repository;

import app.muhimel.daily_accounts_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
}
