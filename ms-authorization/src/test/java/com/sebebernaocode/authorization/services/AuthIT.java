package com.sebebernaocode.authorization.services;

import com.sebebernaocode.authorization.controllers.dto.LoginRequestDto;
import com.sebebernaocode.authorization.controllers.dto.TokenResponseDto;
import com.sebebernaocode.authorization.controllers.exceptions.StandardError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/user-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/user-role-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void auth_WithValidData_Returns200() {
        TokenResponseDto sut = testClient
                .post()
                .uri("/api/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequestDto("jose@mail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
    }

    @Test
    public void auth_WithInValidData_Returns200() {
        StandardError sut = testClient
                .post()
                .uri("/api/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequestDto("jose@mail.com", "1234567"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(400);
    }

    @Test
    public void auth_WithInvalidFields_Returns422() {
        StandardError sut = testClient
                .post()
                .uri("/api/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequestDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);

        sut = testClient
                .post()
                .uri("/api/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequestDto("jose@mail.c", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);

        sut = testClient
                .post()
                .uri("/api/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequestDto("jose@mail.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);
    }
}
