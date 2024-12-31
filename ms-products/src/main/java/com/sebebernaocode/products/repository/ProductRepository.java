package com.sebebernaocode.products.repository;

import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.repository.projection.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p")
    Page<ProductProjection> findAllPageable(Pageable pageable);
}
