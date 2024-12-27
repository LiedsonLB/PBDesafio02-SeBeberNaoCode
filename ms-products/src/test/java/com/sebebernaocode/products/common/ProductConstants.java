package com.sebebernaocode.products.common;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

public class ProductConstants {
    public static final Product PRODUCT = new Product(1L, "this is a valid name", "this is a valid description", LocalDateTime.now(), "", new BigDecimal(10.00), new HashSet<Category>());
    public static final Product INVALID_PRODUCT = new Product(2L, "", "", LocalDateTime.now(), "", new BigDecimal(0.00), new HashSet<Category>());
}
