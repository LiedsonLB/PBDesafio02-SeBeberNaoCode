package com.sebebernaocode.products.web.dto;

import com.sebebernaocode.products.entity.Category;

import java.util.Date;
import java.util.List;

public class ProductResponseDto {

    private Date date;
    private String description;
    private String imgUrl;
    private Double price;
    private List<Category> categories;
}