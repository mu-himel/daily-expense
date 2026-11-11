package app.muhimel.daily_accounts_api.controller;

import app.muhimel.daily_accounts_api.dto.ProductPage;
import app.muhimel.daily_accounts_api.entity.Product;
import app.muhimel.daily_accounts_api.request.ProductRequest;
import app.muhimel.daily_accounts_api.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void test_AddNewProductReturnsCreatedAndDelegatesToService() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Egg","unit":"Pcs"}
                        """)
        ).andExpect(status().isCreated());

        ArgumentCaptor<ProductRequest> captor = ArgumentCaptor.forClass(ProductRequest.class);
        verify(productService).createNewProduct(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("Egg");
        assertThat(captor.getValue().getUnit()).isEqualTo("Pcs");
        verifyNoMoreInteractions(productService);
    }

    @Test
    void test_AddNewProductReturnsBadRequestWhenNameIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"unit":"Pcs"}
                        """)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void test_AddNewProductReturnsBadRequestWhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"","unit":"Pcs"}
                        """)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void test_AddNewProductReturnsBadRequestWhenUnitIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Egg"}
                        """)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void test_AddNewProductReturnsBadRequestWhenUnitIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Egg","unit":""}
                        """)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void test_GetProductsReturnsPageWhenNameIsProvided() throws Exception {
        List<Product> products = List.of(
                new Product(1L, "Egg", "Pcs"),
                new Product(2L, "Egg", "Box")
        );
        ProductPage productPage = new ProductPage(
                new PageImpl<>(products, PageRequest.of(1, 2), 7));
        when(productService.getProductPage("Egg", 1, 2)).thenReturn(productPage);

        mockMvc.perform(get("/api/v1/products")
                .param("name", "Egg")
                .param("page", "1")
                .param("size", "2")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").value(1))
        .andExpect(jsonPath("$.pageSize").value(2))
        .andExpect(jsonPath("$.total").value(7))
        .andExpect(jsonPath("$.totalPages").value(4))
        .andExpect(jsonPath("$.items[0].id").value(1))
        .andExpect(jsonPath("$.items[0].name").value("Egg"))
        .andExpect(jsonPath("$.items[0].unit").value("Pcs"))
        .andExpect(jsonPath("$.items[1].id").value(2))
        .andExpect(jsonPath("$.items[1].unit").value("Box"));

        verify(productService).getProductPage("Egg", 1, 2);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void test_GetProductsUsesEmptyNameWhenNameIsNotProvided() throws Exception {
        List<Product> products = List.of(
                new Product(2L, "Rice", "Kg"),
                new Product(1L, "Egg", "Pcs")
        );
        ProductPage productPage = new ProductPage(
                new PageImpl<>(products, PageRequest.of(0, 5), 2));
        when(productService.getProductPage("", 0, 5)).thenReturn(productPage);

        mockMvc.perform(get("/api/v1/products")
                .param("page", "0")
                .param("size", "5")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.pageSize").value(5))
        .andExpect(jsonPath("$.total").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.items[0].id").value(2))
        .andExpect(jsonPath("$.items[0].name").value("Rice"))
        .andExpect(jsonPath("$.items[1].id").value(1))
        .andExpect(jsonPath("$.items[1].name").value("Egg"));

        verify(productService).getProductPage("", 0, 5);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void test_GetProductsReturnsBadRequestWhenPageIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                .param("size", "5")
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void test_GetProductsReturnsBadRequestWhenSizeIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                .param("page", "0")
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }
}
