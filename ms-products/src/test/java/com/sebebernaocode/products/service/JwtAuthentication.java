package com.sebebernaocode.products.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {
    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient webClient, String loginEmail, String LoginPassword) {
        webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();

        Object request = new Object(){
            public String email = loginEmail;
            public String password = LoginPassword;
        };

        String token = webClient
                .post()
                .uri("/api/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token.substring("{'token':'".length(), token.length() - 2));
    }
}
