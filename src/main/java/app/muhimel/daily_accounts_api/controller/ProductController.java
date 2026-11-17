package app.muhimel.daily_accounts_api.controller;

import app.muhimel.daily_accounts_api.dto.ProductPage;
import app.muhimel.daily_accounts_api.request.ProductRequest;
import app.muhimel.daily_accounts_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> addNewProduct(@RequestBody @Validated(ProductRequest.Create.class) ProductRequest req){
        productService.createNewProduct(req);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Void> updateProduct(@RequestBody @Validated(ProductRequest.Update.class) ProductRequest req){
        productService.updateProduct(req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ProductPage> getProducts(@RequestParam("name") Optional<String> name,
                                                   @RequestParam("page") Integer page,
                                                   @RequestParam("size") Integer size){

        return new ResponseEntity<>(productService.getProductPage(name.orElse(""),page,size),
                HttpStatus.OK);
    }
}
