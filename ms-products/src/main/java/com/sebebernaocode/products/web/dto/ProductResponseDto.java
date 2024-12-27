package com.sebebernaocode.products.web.dto;

import com.sebebernaocode.products.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductResponseDto {

    private LocalDateTime date;
    private String name;
    private String description;
    private String imgUrl;
    private BigDecimal price;
    private List<Category> categories;
}