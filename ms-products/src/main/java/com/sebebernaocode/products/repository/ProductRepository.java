package com.sebebernaocode.products.repository;

import com.sebebernaocode.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
