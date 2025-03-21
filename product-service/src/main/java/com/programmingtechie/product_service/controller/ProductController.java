package com.programmingtechie.product_service.controller;

import com.programmingtechie.product_service.dto.ProductRequest;
import com.programmingtechie.product_service.dto.ProductResponse;
import com.programmingtechie.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> allProducts = productService.getAllProducts();
        return allProducts;
    }
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<ProductResponse> getById(String id) {
//        List<ProductResponse> allProducts = productService.getAllProducts();
//        return allProducts;
//    }

}
