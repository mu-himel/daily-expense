package app.muhimel.daily_accounts_api.service;

import app.muhimel.daily_accounts_api.entity.Product;
import app.muhimel.daily_accounts_api.repository.ProductRepository;
import app.muhimel.daily_accounts_api.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
}
