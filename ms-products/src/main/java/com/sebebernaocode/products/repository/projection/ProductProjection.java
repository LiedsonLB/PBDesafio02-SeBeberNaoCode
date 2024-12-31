package com.sebebernaocode.products.repository.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebebernaocode.products.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"date", "description", "name", "imgUrl", "price", "categories" })
public interface ProductProjection {

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDate();

    String getDescription();

    String getName();

    String getImgUrl();

    BigDecimal getPrice();

    Set<Category> getCategories();

}
