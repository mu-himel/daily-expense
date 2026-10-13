package app.muhimel.daily_accounts_api.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ItemCostTest {

	@Test
	void test_ItemCostClassExist() {
        Product p = new Product(1L,"Fish","Pcs");
        ItemCost itemCost = new ItemCost(p,1d,new BigDecimal(100));
        assertThat(itemCost).hasFieldOrPropertyWithValue("unitPrice",new BigDecimal(100));
        assertThat(itemCost).extracting(ItemCost::getItem).extracting(Product::getId).isEqualTo(1L);
    }

}
