package com.sebebernaocode.products.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.web.dto.CategoryCreateDto;
import com.sebebernaocode.products.web.dto.CategoryResponseDto;

public class CategoryMapper {
    public static Category toCategory(CategoryCreateDto createDto) {
        return new ModelMapper().map(createDto, Category.class);
    }

    public static CategoryResponseDto toDto(Category category) {
        return new ModelMapper().map(category, CategoryResponseDto.class);
    }
}
