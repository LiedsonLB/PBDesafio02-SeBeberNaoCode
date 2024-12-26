package com.sebebernaocode.authorization.controllers;

import com.sebebernaocode.authorization.entities.user.User;
import com.sebebernaocode.authorization.entities.user.dto.*;
import com.sebebernaocode.authorization.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserCreateDto dto) {
        User user = UserMapper.toUser(dto);
        user = userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> get(@PathVariable("id") Long id) {
        User user = userService.get(id);
        return ResponseEntity.ok().body(UserMapper.toDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody @Valid UserUpdateDto dto) {
        userService.update(id, dto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("id") Long id, @RequestBody @Valid UserUpdatePasswordDto dto){
        userService.updatePassword(id, dto);

        return ResponseEntity.noContent().build();
    }
}
