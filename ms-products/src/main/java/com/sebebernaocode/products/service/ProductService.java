package com.sebebernaocode.products.service;

import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.exception.EntityNotFoundException;
import com.sebebernaocode.products.exception.InvalidQueryParameterException;
import com.sebebernaocode.products.repository.ProductRepository;
import com.sebebernaocode.products.repository.projection.ProductProjection;
import com.sebebernaocode.products.web.dto.ProductCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            product.setDate(LocalDateTime.now());
            return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("product id: %s not found in the database", id))
        );
    }

    @Transactional
    public void delete(Long id) {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
            } else {
                throw new EntityNotFoundException(String.format("product id: %s not found in the database", id));
            }
    }

    @Transactional(readOnly = true)
    public Page<ProductProjection> getAll(Pageable pageable) {
        if(pageable.getSort().isUnsorted()) {
            throw new InvalidQueryParameterException("This direction parameter is not supported. The direction can be either ascending or descending: ('asc' or 'desc', either lowercase or uppercase).");
        }
        return productRepository.findAllPageable(pageable);
    }

    public Pageable createPageable(int page, int linesPerPage, Sort sort) {
        try {
            return PageRequest.of(page, linesPerPage, sort);
        }catch (IllegalArgumentException e) {
            throw new InvalidQueryParameterException(e.getMessage());
        }
    }

    public Product updateProduct(Long id, ProductCreateDto dto) {
        Product product = findById(id);
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImgUrl(dto.getImgUrl());
        product.setPrice(dto.getPrice());
        product.setCategories(categoryService.idToCategory(dto.getCategories()));
        return productRepository.save(product);
    }
}
