package com.sebebernaocode.authorization.controllers;

import com.sebebernaocode.authorization.config.security.AuthService;
import com.sebebernaocode.authorization.controllers.dto.LoginRequestDto;
import com.sebebernaocode.authorization.controllers.dto.TokenResponseDto;
import com.sebebernaocode.authorization.controllers.exceptions.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Autenticação", description = "Operações referentes a autenticação do usuário")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/oauth/token")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authManager;

    @Operation(
            summary = "Autenticação de usuário",
            description = "Recurso para autenticar usuário",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário autenticado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Credenciais inválidas",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "422", description = "Erro na validação dos campos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto login, HttpServletRequest request) {
        try {
            Authentication authToken = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
            authManager.authenticate(authToken);
            String token = authService.getTokenAuthenticated(login.getEmail());
            return ResponseEntity.ok().body(new TokenResponseDto(token));
        } catch (AuthenticationException e) {
            log.warn("Invalid credentials for {}", login.getEmail());
        }

        return ResponseEntity
                .badRequest()
                .body(new StandardError(request, HttpStatus.BAD_REQUEST, "Invalid credentials"));
    }
}
