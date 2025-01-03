package com.sebebernaocode.authorization.entities.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserUpdateDto {
    @Size(min = 2, max = 100)
    private String firstName;

    @Size(min = 2, max = 100)
    private String lastName;

    @Email(message = "formato de email inválido.", regexp = "^[a-z0-9.+_-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;
}
