package com.sebebernaocode.products.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sebebernaocode.products.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductResponseDto {

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime date;
    private String name;
    private String description;
    private String imgUrl;
    private BigDecimal price;
    private Set<Category> categories;
}