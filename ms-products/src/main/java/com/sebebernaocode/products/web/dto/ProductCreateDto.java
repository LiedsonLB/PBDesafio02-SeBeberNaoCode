package com.sebebernaocode.products.web.dto;

import com.sebebernaocode.products.entity.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductCreateDto {

    @NotBlank
    @Size(min = 2, max = 60, message = "Name should be between 2 and 60 characters")
    private String name;

    @NotBlank
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters")
    private String description;

    private String imgUrl;

    @DecimalMin("0.0")
    private BigDecimal price;

    @NotEmpty
    private Set<Long> categories;
}