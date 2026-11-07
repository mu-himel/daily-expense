package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.dto.ProductPage;
import app.muhimel.daily_accounts_api.entity.MonthlyExpense;
import app.muhimel.daily_accounts_api.entity.Product;
import app.muhimel.daily_accounts_api.repository.ProductRepository;
import app.muhimel.daily_accounts_api.request.ProductRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void test_DoNotCreateNewProductIfExist(){
        ProductRequest pr =  new ProductRequest("Egg", "pcs");
        Product p = new Product(1L, "Egg","pcs");
        when(productRepository.findByName(pr.getName())).thenReturn(Optional.ofNullable(p));
        assertThatThrownBy(()->productService.createNewProduct(pr)).isInstanceOf(RuntimeException.class);
        verify(productRepository, never()).save(any());
    }

    @Test
    void test_CreateNewProductSuccessfully(){
        ProductRequest pr =  new ProductRequest("Egg", "pcs");
        Product p = new Product(1L, "Egg","pcs");
        when(productRepository.findByName(pr.getName())).thenReturn(Optional.ofNullable(null));
        when(productRepository.save(any())).thenAnswer((inv)->inv.getArgument(0));
        productService.createNewProduct(pr);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Egg");
    }

    @Test
    void test_GetProductPageWithoutNameFilter() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> products = List.of(
                new Product(1L, "Egg", "pcs"),
                new Product(2L, "Rice", "kg")
        );
        Page<Product> productPage = new PageImpl<>(products, pageable, 12);
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        ProductPage result = productService.getProductPage("", 0, 5);

        assertThat(result.getItems()).isEqualTo(products);
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(5);
        assertThat(result.getTotal()).isEqualTo(12L);
        assertThat(result.getTotalPages()).isEqualTo(3);
        verify(productRepository).findAll(pageable);
        verify(productRepository, never()).findAllByName(anyString(), any(Pageable.class));
    }

    @Test
    void test_GetProductPageWithNameFilter() {
        Pageable pageable = PageRequest.of(1, 2);
        List<Product> products = List.of(new Product(3L, "Egg", "pcs"));
        Page<Product> productPage = new PageImpl<>(products, pageable, 7);
        when(productRepository.findAllByName("Egg", pageable)).thenReturn(productPage);

        ProductPage result = productService.getProductPage("Egg", 1, 2);

        assertThat(result.getItems()).isEqualTo(products);
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getPageSize()).isEqualTo(2);
        assertThat(result.getTotal()).isEqualTo(7L);
        assertThat(result.getTotalPages()).isEqualTo(4);
        verify(productRepository).findAllByName("Egg", pageable);
        verify(productRepository, never()).findAll(any(Pageable.class));
    }
}
