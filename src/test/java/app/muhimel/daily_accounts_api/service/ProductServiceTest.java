package app.muhimel.daily_accounts_api.service;

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
}
