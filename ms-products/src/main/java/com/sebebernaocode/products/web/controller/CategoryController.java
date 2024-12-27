package com.sebebernaocode.products.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.service.CategoryService;
import com.sebebernaocode.products.web.dto.CategoryCreateDto;
import com.sebebernaocode.products.web.dto.CategoryResponseDto;
import com.sebebernaocode.products.web.dto.ProductResponseDto;
import com.sebebernaocode.products.web.dto.mapper.CategoryMapper;
import com.sebebernaocode.products.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Criar uma nova categoria.",
            description = "Recurso para criar uma nova categoria.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))),
                    @ApiResponse(responseCode = "422", description = "Não foi possível criar o recurso, pois os dados de entrada são inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryCreateDto categoryCreateDto) {
        Category SavedCategory = categoryService.save(CategoryMapper.toCategory(categoryCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(SavedCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getByID(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.deleteCategory(id);
        return null;
    }
}
