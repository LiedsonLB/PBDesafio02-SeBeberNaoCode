package com.sebebernaocode.authorization.controllers;

import com.sebebernaocode.authorization.controllers.exceptions.StandardError;
import com.sebebernaocode.authorization.entities.user.User;
import com.sebebernaocode.authorization.entities.user.dto.*;
import com.sebebernaocode.authorization.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuários", description = "Operações referentes a manipulação do usuário")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Criação de usuário",
            description = "Recurso para criar um novo usuário",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "E-mail informado já existe",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Erro na validação dos campos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserCreateDto dto) {
        User user = UserMapper.toUser(dto);
        user = userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
    }

    @Operation(
            summary = "Localização de usuário por id",
            description = "Exige um Bearer Token. Acesso restrito a roles ADMIN(TODOS) e OPERATOR(APENAS O SEU)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário recuperado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário sem permissão",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('OPERATOR') AND #id == authentication.principal.id)")
    public ResponseEntity<UserResponseDto> get(@PathVariable("id") Long id) {
        User user = userService.get(id);
        return ResponseEntity.ok().body(UserMapper.toDto(user));
    }

    @Operation(
            summary = "Atualização de dados do usuário",
            description = "Exige um Bearer Token. Acesso restrito a roles ADMIN(TODOS) e OPERATOR(APENAS O SEU)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Usuário atualizada"),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário sem permissão",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "E-mail informado já existe",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Erro na validação dos campos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('OPERATOR') AND #id == authentication.principal.id)")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody @Valid UserUpdateDto dto) {
        userService.update(id, dto);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualização de senha",
            description = "Exige um Bearer Token. Acesso restrito a roles ADMIN(TODOS) e OPERATOR(APENAS O SEU)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Senha atualizada"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Senha inválida ou nova senha diferente da confirmação",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário sem permissão",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Erro na validação dos campos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('OPERATOR') AND #id == authentication.principal.id)")
    public ResponseEntity<Void> updatePassword(@PathVariable("id") Long id, @RequestBody @Valid UserUpdatePasswordDto dto){
        userService.updatePassword(id, dto);

        return ResponseEntity.noContent().build();
    }
}
