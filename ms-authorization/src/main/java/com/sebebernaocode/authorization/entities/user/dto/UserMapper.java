package com.sebebernaocode.authorization.entities.user.dto;

import com.sebebernaocode.authorization.entities.role.Role;
import com.sebebernaocode.authorization.entities.user.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toUser(UserCreateDto dto) {
        return new ModelMapper().map(dto, User.class);
    }

    public static UserResponseDto toDto(User user) {
        Set<Long> rolesId = user.getRoles().stream().map(Role::getId).collect(Collectors.toSet());

        PropertyMap<User, UserResponseDto> props = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setRoles(rolesId);
            }
        };
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(props);
        return modelMapper.map(user, UserResponseDto.class);
    }
}
