package com.sebebernaocode.products.service;

import static org.assertj.core.api.Assertions.*;
import static com.sebebernaocode.products.common.ProductConstants.PRODUCT;
import static com.sebebernaocode.products.common.ProductConstants.INVALID_PRODUCT;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void createProduct_WithValidData_ReturnsProduct() {
        when(productRepository.save(PRODUCT)).thenReturn(PRODUCT);

        Product sut = productService.create(PRODUCT);

        assertThat(sut).isEqualTo(PRODUCT);
    }

    @Test
    public void createProduct_WithInvalidData_ThrowsException() {
        when(productRepository.save(INVALID_PRODUCT)).thenThrow(RuntimeException.class);

        assertThatThrownBy( () -> productService.create(INVALID_PRODUCT) ).isInstanceOf(RuntimeException.class);
    }
}
