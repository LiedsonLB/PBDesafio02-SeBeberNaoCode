package com.sebebernaocode.products.web.controller;

import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.repository.projection.ProductProjection;
import com.sebebernaocode.products.service.ProductService;
import com.sebebernaocode.products.web.dto.PageableDto;
import com.sebebernaocode.products.web.dto.ProductCreateDto;
import com.sebebernaocode.products.web.dto.ProductResponseDto;
import com.sebebernaocode.products.web.dto.mapper.PageableMapper;
import com.sebebernaocode.products.web.dto.mapper.ProductMapper;
import com.sebebernaocode.products.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

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
                    @ApiResponse(responseCode = "404", description = "Não foi possível localizar o recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        Product product = productService.findById(id);
        ProductResponseDto dto = ProductMapper.toDto(product);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
            summary = "Deletar um produto.",
            description = "Recurso para deletar um produto por id.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Recurso deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Não foi possível localizar o recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Localizar os registros de produtos",
            description = "Recurso para buscar todos os registros de produtos",
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"
                    ),
                    @Parameter(in = QUERY, name = "linesPerPage",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Representa o total de elementos por página"
                    ),
                    @Parameter(in = QUERY, name = "orderBy",
                            content = @Content(schema = @Schema(type = "string", defaultValue = "id")),
                            description = "Campo pelo qual o conteúdo está sendo ordenado. Ex: id, name, description, price"
                    ),
                    @Parameter(in = QUERY, name = "direction",
                            content = @Content(schema = @Schema(type = "string", defaultValue = "asc")),
                            description = "Direção da ordenação")

            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(responseCode = "422", description = "Parâmetros de pesquisa inválidos. Possiveis causas: <br>" +
                            "- Campo de ordenação inválido no parâmetro de pesquisa 'orderBy'; <br/>" +
                            "- Valor do parâmetro de pesquisa 'direction' inválido; <br/>" +
                            "- Valor do parâmetro de pesquisa 'linesPerPage' (tamanho da página) inválido;",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping
    public ResponseEntity<PageableDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int linesPerPage,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equals("asc")  || direction.equals("ASC") ? Sort.by(orderBy).ascending() :
                    direction.equals("desc") || direction.equals("DESC") ? Sort.by(orderBy).descending() : Sort.unsorted();
        Pageable pageable = productService.createPageable(page, linesPerPage, sort);
        Page<ProductProjection> projection = productService.getAll(pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }
}
