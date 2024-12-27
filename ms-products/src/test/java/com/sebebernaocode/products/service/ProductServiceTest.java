package com.sebebernaocode.products.service;

import static org.assertj.core.api.Assertions.*;
import static com.sebebernaocode.products.common.ProductConstants.PRODUCT;
import static com.sebebernaocode.products.common.ProductConstants.INVALID_PRODUCT;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.exception.EntityNotFoundException;
import com.sebebernaocode.products.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


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

    @Test
    public void findProductById_WithExistingId_ReturnsProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(PRODUCT));

        Product sut = productService.findById(1L);

        assertThat(sut).isEqualTo(PRODUCT);
    }

    @Test
    public void findProductById_WithUnexistingId_ReturnsErrorStatus404() {
        doThrow(new EntityNotFoundException("Product not found")).when(productRepository).findById(99L);

        assertThatThrownBy( () -> productService.findById(99L) ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void deleteProduct_WithExistingId_doesNotThrowAnyException() {
        when(productRepository.existsById(anyLong())).thenReturn(true);

        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteProduct_WithUnexistingId_ThrowsException() {
        when(productRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy( () -> productService.delete(1L) ).isInstanceOf(EntityNotFoundException.class);
    }
}
