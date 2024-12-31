package com.sebebernaocode.authorization.entities.email;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Email {
    private String fromEmail;
    @NotBlank
    private String toEmail;
    @NotBlank
    private String subject;
    @NotBlank
    private String body;
}

