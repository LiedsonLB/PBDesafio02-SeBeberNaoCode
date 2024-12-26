package com.sebebernaocode.products.web.dto.mapper;

import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.web.dto.ProductCreateDto;
import com.sebebernaocode.products.web.dto.ProductResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static Product toProduct(ProductCreateDto dto) {
        return new ModelMapper().map(dto, Product.class);
    }

    public static ProductResponseDto toDto(Product product) {
        return new ModelMapper().map(product, ProductResponseDto.class);
    }
}