package com.sebebernaocode.products.service;

import static org.assertj.core.api.Assertions.*;
import static com.sebebernaocode.products.common.ProductConstants.PRODUCT;
import static com.sebebernaocode.products.common.ProductConstants.INVALID_PRODUCT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.exception.EntityNotFoundException;
import com.sebebernaocode.products.exception.InvalidQueryParameterException;
import com.sebebernaocode.products.repository.ProductRepository;
import com.sebebernaocode.products.repository.projection.ProductProjection;
import com.sebebernaocode.products.web.dto.ProductCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
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
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

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

    @Test
    public void getAllProducts_WithValidParams_ReturnsAllProducts() {

            Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());

            ProductProjection productProjection = mock(ProductProjection.class);

            when(productProjection.getName()).thenReturn(PRODUCT.getName());
            when(productProjection.getDescription()).thenReturn(PRODUCT.getDescription());
            when(productProjection.getPrice()).thenReturn(PRODUCT.getPrice());

            Page<ProductProjection> page = new PageImpl<>(Collections.singletonList(productProjection), pageable, 1);
            when(productRepository.findAllPageable(pageable)).thenReturn(page);

            Page<ProductProjection> sut = productService.getAll(pageable);

            assertThat(sut.getTotalElements()).isEqualTo(1);
            assertThat(sut.getContent().get(0).getName()).isEqualTo(PRODUCT.getName());
            assertThat(sut.getContent().get(0).getDescription()).isEqualTo(PRODUCT.getDescription());
            assertThat(sut.getContent().get(0).getPrice()).isEqualTo(PRODUCT.getPrice());
            verify(productRepository, times(1)).findAllPageable(pageable);
    }

    @Test
    public void getAllProducts_WithInvalidSortParam_ThrowsException() {
        Pageable pageableWithUnsortedSort = PageRequest.of(0, 5, Sort.unsorted());

        InvalidQueryParameterException exception;
        exception = assertThrows(InvalidQueryParameterException.class, () -> productService.getAll(pageableWithUnsortedSort));

        assertThat(exception.getMessage()).isEqualTo("This direction parameter is not supported. The direction can be either ascending or descending: ('asc' or 'desc', either lowercase or uppercase).");
        assertThatThrownBy( () -> productService.getAll(pageableWithUnsortedSort) ).isInstanceOf(InvalidQueryParameterException.class);
    }

    @Test
    public void createPageable_WithValidParams_ReturnsPageable() {
        int page = 0;
        int linesPerPage = 5;
        Sort sort = Sort.by("name").ascending();

        Pageable pageable = productService.createPageable(page, linesPerPage, sort);

        assertThat(pageable).isNotNull();
        assertThat(pageable.getPageNumber()).isEqualTo(page);
        assertThat(pageable.getPageSize()).isEqualTo(linesPerPage);
        assertThat(pageable.getSort()).isEqualTo(sort);
    }

    @Test
    public void createPageable_WithInvalidPageSize_ThrowsException() {
        int page = 0;
        int linesPerPage = -5;
        Sort sort = Sort.by("name").ascending();

        InvalidQueryParameterException exception;
        exception = assertThrows(InvalidQueryParameterException.class, () -> productService.createPageable(page, linesPerPage, sort));

        assertThat(exception.getMessage()).isEqualTo("Page size must not be less than one");
    }

    @Test
    public void createPageable_WithInvalidPageIndex_ThrowsException() {
        int page = -1;
        int linesPerPage = 5;
        Sort sort = Sort.by("name").ascending();

        InvalidQueryParameterException exception;
        exception = assertThrows(InvalidQueryParameterException.class, () -> productService.createPageable(page, linesPerPage, sort));

        assertThat(exception.getMessage()).isEqualTo("Page index must not be less than zero");
    }

    @Test
    public void updateProduct_WithValidData_ReturnsUpdatedProduct() {
        Product existingProduct = new Product();
        existingProduct.setId(PRODUCT.getId());
        existingProduct.setName(PRODUCT.getName());
        existingProduct.setDescription(PRODUCT.getDescription());
        existingProduct.setPrice(PRODUCT.getPrice());
        existingProduct.setDescription(PRODUCT.getDescription());
        existingProduct.setCategories(PRODUCT.getCategories());

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        ProductCreateDto dto = new ProductCreateDto();
        dto.setName("updated name");
        dto.setDescription("updated description");
        dto.setImgUrl("updated imgUrl");
        dto.setPrice(new BigDecimal(10000));
        dto.setCategories(new HashSet<>());

        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product updatedProduct = productService.updateProduct(1L, dto);

        assertThat(updatedProduct.getName()).isEqualTo(dto.getName());
        assertThat(updatedProduct.getDescription()).isEqualTo(dto.getDescription());
        assertThat(updatedProduct.getImgUrl()).isEqualTo(dto.getImgUrl());
        assertThat(updatedProduct.getPrice()).isEqualTo(new BigDecimal(10000));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void updateProduct_WithUnexistingId_ThrowsException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ProductCreateDto dto = new ProductCreateDto();

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(1L, dto));

        verify(productRepository, never()).save(any(Product.class));
    }
}
