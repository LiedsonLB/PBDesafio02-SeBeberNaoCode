package com.sebebernaocode.authorization.entities.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
