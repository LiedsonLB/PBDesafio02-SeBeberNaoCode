package com.sebebernaocode.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebebernaocode.products.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    boolean existsByName(String name);
}