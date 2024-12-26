package com.sebebernaocode.products.service;

import org.springframework.stereotype.Service;

import com.sebebernaocode.products.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
}
