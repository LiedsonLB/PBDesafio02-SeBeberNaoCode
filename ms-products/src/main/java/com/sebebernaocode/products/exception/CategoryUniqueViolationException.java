package com.sebebernaocode.products.exception;

public class CategoryUniqueViolationException extends RuntimeException {
    public CategoryUniqueViolationException(String msg) {
        super(msg);
    }
}