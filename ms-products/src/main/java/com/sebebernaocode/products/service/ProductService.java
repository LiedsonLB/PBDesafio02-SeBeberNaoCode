package com.sebebernaocode.products.service;

import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product create(Product product) {
        try {
            product.setDate(LocalDateTime.now());
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Error trying to create product, error: %s", e.getMessage()));
        }
    }
}
