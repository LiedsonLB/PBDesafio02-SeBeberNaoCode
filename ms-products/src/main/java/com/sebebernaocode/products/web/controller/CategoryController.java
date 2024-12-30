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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Categorias", description = "Contém os recursos de gerenciamento de categorias.")
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
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody  @Valid CategoryCreateDto dto) {
        Category SavedCategory = categoryService.save(CategoryMapper.toCategory(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryMapper.toDto(SavedCategory));
    }

    @Operation(
            summary = "Buscar uma categoria.",
            description = "Recurso para buscar uma categoria por id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não foi possivel localizar o recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getByID(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok().body(CategoryMapper.toDto(category));
    }

    @Operation(
            summary = "Altera uma categoria.",
            description = "Recurso para atualizar uma categoria por id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não foi possivel localizar o recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoryCreateDto dto) {
        Category updatedCategory = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok().body(CategoryMapper.toDto(updatedCategory));
    }

    @Operation(
            summary = "Deleta uma categoria.",
            description = "Recurso para deletar uma categoria por id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso deletado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não foi possivel localizar o recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.deleteCategory(id);
        return ResponseEntity.ok().body(category);
    }
}
