package app.muhimel.daily_accounts_api.repository;

import app.muhimel.daily_accounts_api.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ProductRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:latest");

    @Autowired
    private ProductRepository productRepository;

    @Test
    void test_SaveAndFindByNameReturnsSavedProduct() {
        Product saved = productRepository.save(new Product("Egg", "pcs"));

        Optional<Product> found = productRepository.findByName("Egg");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getName()).isEqualTo("Egg");
        assertThat(found.get().getUnit()).isEqualTo("pcs");
    }

    @Test
    void test_FindByNameReturnsEmptyWhenProductDoesNotExist() {
        productRepository.save(new Product("Egg", "pcs"));

        assertThat(productRepository.findByName("Rice")).isEmpty();
    }

    @Test
    void test_FindAllByNameReturnsOnlyMatchingProducts() {
        productRepository.save(new Product("Egg", "pcs"));
        productRepository.save(new Product("Egg", "box"));
        productRepository.save(new Product("Rice", "kg"));

        Page<Product> page = productRepository.findAllByName("Egg", PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent()).allMatch(product -> "Egg".equals(product.getName()));
    }

    @Test
    void test_FindAllByNameAppliesPaging() {
        productRepository.save(new Product("Egg", "pcs"));
        productRepository.save(new Product("Egg", "box"));
        productRepository.save(new Product("Rice", "kg"));

        Page<Product> firstPage = productRepository.findAllByName("Egg", PageRequest.of(0, 1));
        Page<Product> secondPage = productRepository.findAllByName("Egg", PageRequest.of(1, 1));

        assertThat(firstPage.getTotalElements()).isEqualTo(2);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);
        assertThat(firstPage.getNumber()).isZero();
        assertThat(firstPage.getContent()).hasSize(1);
        assertThat(firstPage.getContent().get(0).getName()).isEqualTo("Egg");

        assertThat(secondPage.getNumber()).isEqualTo(1);
        assertThat(secondPage.getContent()).hasSize(1);
        assertThat(secondPage.getContent().get(0).getName()).isEqualTo("Egg");
    }

    @Test
    void test_FindAllByNameReturnsEmptyPageWhenNoMatch() {
        productRepository.save(new Product("Rice", "kg"));

        Page<Product> page = productRepository.findAllByName("Egg", PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getContent()).isEmpty();
    }

    @Test
    void test_FindAllWithPagingReturnsAllProducts() {
        productRepository.save(new Product("Egg", "pcs"));
        productRepository.save(new Product("Rice", "kg"));
        productRepository.save(new Product("Fish", "kg"));

        Page<Product> page = productRepository.findAll(PageRequest.of(0, 2));

        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isZero();
        assertThat(page.getContent()).hasSize(2);
    }
}
