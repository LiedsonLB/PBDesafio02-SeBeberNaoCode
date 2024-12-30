package com.sebebernaocode.products.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static com.sebebernaocode.products.common.CategoryConstants.CATEGORY_NAME;
import static com.sebebernaocode.products.common.CategoryConstants.INVALID_CATEGORY_NAME;
import static com.sebebernaocode.products.common.CategoryConstants.INVALID_CATEGORY_NAME_SIZE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceUnitTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void createCategory_ReturnsCategory() {
        when(categoryRepository.save(CATEGORY_NAME)).thenReturn(CATEGORY_NAME);

        Category sut = categoryService.save(CATEGORY_NAME);

        assertThat(sut).isEqualTo(CATEGORY_NAME);
    }

    @Test
    public void createCategory_ReturnsNull() {
        when(categoryRepository.save(CATEGORY_NAME)).thenReturn(null);

        Category sut = categoryService.save(CATEGORY_NAME);

        assertThat(sut).isNull();
    }

    @Test
    public void createCategory_ReturnsNull_InvalidName() {
        when(categoryRepository.save(INVALID_CATEGORY_NAME)).thenThrow(RuntimeException.class);

        assertThatThrownBy( () -> categoryService.save(INVALID_CATEGORY_NAME) ).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createCategory_ReturnsNull_InvalidNameSize() {
        when(categoryRepository.save(INVALID_CATEGORY_NAME_SIZE)).thenReturn(INVALID_CATEGORY_NAME_SIZE);

        Category sut = categoryService.save(INVALID_CATEGORY_NAME_SIZE);

        assertThat(sut).isEqualTo(INVALID_CATEGORY_NAME_SIZE);
    }
}