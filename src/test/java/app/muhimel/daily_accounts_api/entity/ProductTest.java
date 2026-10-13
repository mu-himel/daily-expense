package app.muhimel.daily_accounts_api.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductTest {

    @Test
    public void test_ProductClassExist(){
        Product product = new Product(1L,"Fish","Pcs");
        assertThat(product).hasFieldOrPropertyWithValue("id",1L);
    }
}
