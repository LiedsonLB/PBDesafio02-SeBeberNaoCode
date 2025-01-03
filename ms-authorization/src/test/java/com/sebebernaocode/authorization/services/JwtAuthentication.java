package com.sebebernaocode.authorization.services;

import com.sebebernaocode.authorization.controllers.dto.LoginRequestDto;
import com.sebebernaocode.authorization.controllers.dto.TokenResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {
    public static Consumer<HttpHeaders> getHeaderAutorization(WebTestClient client, String email, String password) {
        String token = client
                .post()
                .uri("/api/oauth/token")
                .bodyValue(new LoginRequestDto(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponseDto.class)
                .returnResult().getResponseBody().getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
