package com.sebebernaocode.products.web.controller;

import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.service.ProductService;
import com.sebebernaocode.products.web.dto.ProductCreateDto;
import com.sebebernaocode.products.web.dto.ProductResponseDto;
import com.sebebernaocode.products.web.dto.mapper.ProductMapper;
import com.sebebernaocode.products.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Produtos", description = "Contém os recursos de gerenciamento de produtos.")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/products")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Criar um novo produto.",
            description = "Recurso para criar um novo produto.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "422", description = "Não foi possível criar o recurso, pois os dados de entrada são inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductCreateDto dto) {
        Product product = ProductMapper.toProduct(dto);
        product = productService.create(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toDto(product));
    }

    @Operation(
            summary = "Buscar um produto.",
            description = "Recurso para buscar um produto por id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não foi possivel localizar o recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        Product product = productService.findById(id);
        ProductResponseDto dto = ProductMapper.toDto(product);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
