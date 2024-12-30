package com.sebebernaocode.products.common;

import com.sebebernaocode.products.entity.Category;

public class CategoryConstants {
    public static final Category CATEGORY= new Category (1L, "this is a valid name");
    public static final Category CATEGORY_NAME = new Category (null, "this is a valid name");
    public static final Category INVALID_CATEGORY_NAME = new Category (1L, "");
    public static final Category INVALID_CATEGORY_NAME_SIZE = new Category (1L, "this is a very long name that is not valid");
}
