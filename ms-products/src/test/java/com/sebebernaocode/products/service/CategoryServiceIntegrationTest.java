package com.sebebernaocode.products.service;

import static com.sebebernaocode.products.common.CategoryConstants.CATEGORY_NAME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.repository.CategoryRepository;

@DataJpaTest
public class CategoryServiceIntegrationTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testExistsByName() {
        Category category = new Category(null, "this is a valid name");
        categoryRepository.save(category);
        categoryRepository.flush();

        boolean exists = categoryRepository.existsByName("this is a valid name");
        assertTrue(exists);
    }

    @Test
    public void createCategory_ThrowsException_InvalidNameExists() {
        Category category = new Category(null, "this is a valid name");
        testEntityManager.persistAndFlush(category);

        testEntityManager.flush();

        Category duplicateCategory = new Category(null, "this is a valid name");

        System.out.println("\nCategory: " + category + "\n");
        System.out.println("\nDuplicate Category: " + duplicateCategory + "\n");

        assertThatThrownBy(() -> categoryService.save(duplicateCategory))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Category name already exists");
    }

}
