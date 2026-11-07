package app.muhimel.daily_accounts_api.repository;

import app.muhimel.daily_accounts_api.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(@NotEmpty(message = "Name is Required") String name);
}
