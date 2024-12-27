package com.sebebernaocode.products.service;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.exception.EntityNotFoundException;
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

    private final CategoryService categoryService;

    @Transactional
    public Product create(Product product) {
        try {
            product.setDate(LocalDateTime.now());

            for (Category category : product.getCategories()) {
                category.setName(categoryService.findById(category.getId()).getName());
            }
            return productRepository.save(product);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Error trying to create product, error: %s", e.getMessage()));
        }
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("product id: %s not found in the database", id))
        );
    }

    @Transactional
    public void delete(Long id) {
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
            } else {
                throw new EntityNotFoundException(String.format("product id: %s not found in the database", id));
            }
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(String.format("Error trying to delete product, error: %s", e.getMessage()));
        }
    }
}
