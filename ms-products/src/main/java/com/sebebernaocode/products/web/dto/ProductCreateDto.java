package com.sebebernaocode.products.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductCreateDto {

    @NotBlank
    @Size(min = 2, max = 60, message = "Name should be between 2 and 60 characters")
    private String name;

    @NotBlank
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters")
    private String description;

    private String imgUrl;

    @NotBlank
    @Min(value = 0, message = "The price of the product must be greater than 0")
    private Double price;
}