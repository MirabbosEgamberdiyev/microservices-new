package com.programmingtechie.product_service.service;

import com.programmingtechie.product_service.dto.ProductRequest;
import com.programmingtechie.product_service.dto.ProductResponse;
import com.programmingtechie.product_service.model.Product;
import com.programmingtechie.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;


    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder().
                name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice()).build();

        productRepository.save(product);
        log.info("Product created: {}", product.getName());
    }

    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();
        Stream<ProductResponse> productResponseStream = products.stream().map(product -> mapToProductResponse(product));
        return productResponseStream.toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                        .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice()).build();
    }
}
