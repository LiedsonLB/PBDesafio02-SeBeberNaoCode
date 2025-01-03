package com.sebebernaocode.authorization.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LoginRequestDto {
    @NotBlank
    @Email(message = "formato de email inválido.", regexp = "^[a-z0-9.+_-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;

    @NotBlank
    @Size(min = 6, max = 30)
    private String password;
}
