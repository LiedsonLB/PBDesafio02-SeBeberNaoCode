package com.sebebernaocode.products.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.repository.CategoryRepository;
import com.sebebernaocode.products.web.dto.CategoryCreateDto;
import com.sebebernaocode.products.web.exception.CategoryNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category save(Category category) {
        boolean nameExists = categoryRepository.existsByName(category.getName());
        if (nameExists) {
            System.out.println("Category name already exists: " + category.getName());
            throw new RuntimeException("Category name already exists");
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
    public Category deleteCategory(Long id) {
        return null;
    }
}
