package com.sebebernaocode.authorization.services;

import com.sebebernaocode.authorization.controllers.exceptions.StandardError;
import com.sebebernaocode.authorization.entities.user.dto.UserCreateDto;
import com.sebebernaocode.authorization.entities.user.dto.UserResponseDto;
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
public class UserIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUser_WithValidData_Returns201() {
        UserCreateDto user = new UserCreateDto("Francisco", "Carvalho", "francisco@mail.com", "123456");

        UserResponseDto sut = testClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isNotNull();
        Assertions.assertThat(sut.getFirstName()).isEqualTo(user.getFirstName());
        Assertions.assertThat(sut.getLastName()).isEqualTo(user.getLastName());
        Assertions.assertThat(sut.getRoles()).size().isEqualTo(1);
    }

    @Test
    public void createUser_WithDuplicateEmail_Returns409() {
        UserCreateDto user = new UserCreateDto("José", "Carvalho", "jose@mail.com", "123456");

        StandardError sut = testClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(409);
    }

    @Test
    public void createUser_WithInvalidFields_Returns422() {
        UserCreateDto user = new UserCreateDto("", "", "francisco@mail.com", "123456");

        StandardError sut = testClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);


        user.setFirstName("Francisco");
        user.setLastName("Carvalho");
        user.setEmail("francisco@mail.c");
        sut = testClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);

        user.setEmail("francisco@mail.com");
        user.setPassword("1234");
        sut = testClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);
    }
}
