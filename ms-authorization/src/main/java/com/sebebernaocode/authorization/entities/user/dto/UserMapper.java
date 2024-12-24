package com.sebebernaocode.authorization.entities.user.dto;

import com.sebebernaocode.authorization.entities.user.User;
import org.modelmapper.ModelMapper;

public class UserMapper {
    public static User toUser(UserCreateDto dto){
        return new ModelMapper().map(dto, User.class);
    }

    public static UserResponseDto toDto(User user){
        return new ModelMapper().map(user, UserResponseDto.class);
    }
}
