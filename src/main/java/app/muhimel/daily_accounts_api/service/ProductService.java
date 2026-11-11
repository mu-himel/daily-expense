package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.dto.ProductPage;
import app.muhimel.daily_accounts_api.entity.Product;
import app.muhimel.daily_accounts_api.repository.ProductRepository;
import app.muhimel.daily_accounts_api.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createNewProduct(ProductRequest pr) {
        Optional<Product> productOp = productRepository.findByName(pr.getName());
        if(productOp.isPresent()){
            throw new RuntimeException("Sorry! Product already exist");
        }

        Product product = new Product(pr.getName(), pr.getUnit());
        return productRepository.save(product);

    }

    public ProductPage getProductPage(String name, Integer page, Integer size){
        Page<Product> pages = null;
        if(name.isEmpty()){
            pages = productRepository.findAll(PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"id")));
            return new ProductPage(pages);
        }
        pages = productRepository.findAllByName(name,PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id")));
        return new ProductPage(pages);
    }
}
