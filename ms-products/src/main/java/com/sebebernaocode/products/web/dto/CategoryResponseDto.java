package com.sebebernaocode.products.web.dto;

public class CategoryResponseDto {
    private Long id;
    private String name;

    public CategoryResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }   
}