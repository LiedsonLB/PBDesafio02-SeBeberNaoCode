package com.sebebernaocode.authorization.entities.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreateDto {
    @NotNull
    @Size(min = 2, max = 100)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 100)
    private String lastName;

    @NotBlank
    @Email(message = "formato de email inválido.", regexp = "^[a-z0-9.+_-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;

    @NotBlank
    @Size(min = 6, max = 30)
    private String password;
}
