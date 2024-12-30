package com.sebebernaocode.products.service;

import static org.assertj.core.api.Assertions.*;
import static com.sebebernaocode.products.common.CategoryConstants.CATEGORY;
import static com.sebebernaocode.products.common.CategoryConstants.INVALID_CATEGORY_NAME;
import com.sebebernaocode.products.exception.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.repository.CategoryRepository;
import com.sebebernaocode.products.web.dto.CategoryCreateDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void createCategory_WithValidData_ReturnsCategory() {
        when(categoryRepository.save(CATEGORY)).thenReturn(CATEGORY);

        Category sut = categoryService.save(CATEGORY);

        assertThat(sut).isEqualTo(CATEGORY);
    }

    @Test
    public void createCategory_WithInvalidNameNull_ThrowsException() {
        when(categoryRepository.save(INVALID_CATEGORY_NAME)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> categoryService.save(INVALID_CATEGORY_NAME)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findCategoryById_WithExistingId_ReturnsCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(CATEGORY));

        Category sut = categoryService.findById(1L);

        assertThat(sut).isEqualTo(CATEGORY);
    }

    @Test
    public void findCategoryById_WithUnexistingId_ReturnsErrorStatus404() {
        doThrow(new EntityNotFoundException("Category not found")).when(categoryRepository).findById(99L);

        assertThatThrownBy(() -> categoryService.findById(99L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void updateCategory_WithExistingId_ReturnsCategory() {
        when(categoryRepository.save(CATEGORY)).thenReturn(CATEGORY);

        Category updatedCategory = new Category(1L, "updated name");
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(CATEGORY));
        when(categoryRepository.save(CATEGORY)).thenReturn(updatedCategory);

        Category sut = categoryService.updateCategory(1L, new CategoryCreateDto("updated name"));

        assertThat(sut).isEqualTo(CATEGORY);
    }

    @Test
    public void updateCategory_WithUnexistingId_ReturnsErrorStatus404() {
        doThrow(new EntityNotFoundException("Category not found")).when(categoryRepository).findById(99L);

        assertThatThrownBy(() -> categoryService.updateCategory(99L, new CategoryCreateDto("updated name")))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void deleteCategory_WithExistingId_doesNotThrowAnyException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(CATEGORY));

        assertThatCode(() -> categoryService.deleteCategory(1L)).doesNotThrowAnyException();
    }

}