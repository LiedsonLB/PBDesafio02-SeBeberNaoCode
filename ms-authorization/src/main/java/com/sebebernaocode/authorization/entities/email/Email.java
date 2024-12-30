package com.sebebernaocode.authorization.entities.email;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Email {
    private String from;
    @NotBlank
    private String to;
    @NotBlank
    private String subject;
    @NotBlank
    private String body;
}
