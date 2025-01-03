package com.sebebernaocode.products.service;

import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.exception.CategoryUniqueViolationException;
import com.sebebernaocode.products.exception.EntityNotFoundException;
import com.sebebernaocode.products.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.repository.CategoryRepository;
import com.sebebernaocode.products.web.dto.CategoryCreateDto;
import com.sebebernaocode.products.web.exception.CategoryNotFoundException;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    @Transactional
    public Category save(Category category) {
        boolean nameExists = categoryRepository.existsByName(category.getName());
        if (nameExists) {
            System.out.println("Category name already exists: " + category.getName());
            throw new CategoryUniqueViolationException("Category name already exists");
        }
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category not found with id " + id));
    }

    @Transactional
    public Category updateCategory(long id, CategoryCreateDto dto) {
        Category oldCategory = findById(id);
        oldCategory.updateFrom(dto);
        return categoryRepository.save(oldCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            List<Product> products = productRepository.findByCategoriesId(id);
            for (Product product : products) {
                product.getCategories().removeIf(category -> category.getId().equals(id));
                productRepository.save(product);
            }
            categoryRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(String.format("category id: %s not found in the database", id));
        }

    }

    @Transactional
    public Set<Category> idToCategory(Set<Long> ids){
        return ids.stream()
                .map(id -> findById(id)).collect(Collectors.toSet());
    }
}
