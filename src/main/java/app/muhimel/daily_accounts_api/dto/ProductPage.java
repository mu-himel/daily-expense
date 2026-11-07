package app.muhimel.daily_accounts_api.dto;

import app.muhimel.daily_accounts_api.entity.Product;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductPage {

    List<Product> items = new ArrayList<>();
    Integer page;
    Integer pageSize;
    Long total;
    Integer totalPages;

    public ProductPage(Page<Product> page){
        this.items = page.getContent();
        this.page = page.getNumber();
        this.pageSize = page.getSize();
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
