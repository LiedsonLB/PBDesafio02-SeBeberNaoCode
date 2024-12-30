package com.sebebernaocode.products.web.dto.mapper;

import com.sebebernaocode.products.entity.Category;
import com.sebebernaocode.products.entity.Product;
import com.sebebernaocode.products.web.dto.ProductCreateDto;
import com.sebebernaocode.products.web.dto.ProductResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static Product toProduct(ProductCreateDto dto, Set<Category> categories) {
        PropertyMap<ProductCreateDto, Product> props = new PropertyMap<ProductCreateDto, Product>() {
            @Override
            protected void configure() {
                map().setCategories(categories);
            }
        };
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(props);
        return modelMapper.map(dto, Product.class);
    }

    public static ProductResponseDto toDto(Product product) {
        return new ModelMapper().map(product, ProductResponseDto.class);
    }
}