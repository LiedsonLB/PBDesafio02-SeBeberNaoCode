package com.sebebernaocode.products.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
            () -> new RuntimeException("user not found")
        );
    }

    @Transactional
    public Category updateCategory(long id, Category category) {
        Category oldCategory = findById(id); 
        oldCategory.updateFrom(category);
        return categoryRepository.save(oldCategory);
    }
}
