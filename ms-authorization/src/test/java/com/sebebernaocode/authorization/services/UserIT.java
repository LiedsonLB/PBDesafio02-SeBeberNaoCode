package com.sebebernaocode.authorization.services;

import com.sebebernaocode.authorization.controllers.exceptions.StandardError;
import com.sebebernaocode.authorization.entities.user.dto.UserCreateDto;
import com.sebebernaocode.authorization.entities.user.dto.UserResponseDto;
import com.sebebernaocode.authorization.entities.user.dto.UserUpdateDto;
import com.sebebernaocode.authorization.entities.user.dto.UserUpdatePasswordDto;
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

    @Test
    public void getUser_WithAdminRole_Returns200() {
        UserResponseDto sut = testClient
                .get()
                .uri("/api/users/102")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(102);
        Assertions.assertThat(sut.getFirstName()).isEqualTo("Maria");
        Assertions.assertThat(sut.getLastName()).isEqualTo("da Silva");
        Assertions.assertThat(sut.getEmail()).isEqualTo("maria@mail.com");
    }

    @Test
    public void getOtherUser_WithOperatorRole_Returns200() {
        UserResponseDto sut = testClient
                .get()
                .uri("/api/users/102")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "maria@mail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(102);
        Assertions.assertThat(sut.getFirstName()).isEqualTo("Maria");
        Assertions.assertThat(sut.getLastName()).isEqualTo("da Silva");
        Assertions.assertThat(sut.getEmail()).isEqualTo("maria@mail.com");
    }

    @Test
    public void getOtherUser_WithOperatorRole_Returns403() {
        StandardError sut = testClient
                .get()
                .uri("/api/users/104")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(403);
    }

    @Test
    public void getUserNotFound_WithAdminRole_Returns404() {
        StandardError sut = testClient
                .get()
                .uri("/api/users/0")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(404);
    }

    @Test
    public void updateData_WithAllDataAndPassword_Returns204(){
        testClient
                .put()
                .uri("/api/users/101")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .bodyValue(new UserUpdateDto("Jose", "Carvalho", "jose@creu.com"))
                .exchange()
                .expectStatus().isNoContent();

        testClient
                .patch()
                .uri("/api/users/101/password")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@creu.com", "123456"))
                .bodyValue(new UserUpdatePasswordDto("123456", "jose10", "jose10"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void updateDataOtherUser_WithAdminRole_Returns204(){
        testClient
                .put()
                .uri("/api/users/102")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(new UserUpdateDto("Maria Clara", "Carvalho da Silva", "maria@creu.com"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void updateDataOtherUser_WithAllData_Returns403(){
        testClient
                .put()
                .uri("/api/users/102")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .bodyValue(new UserUpdateDto("Maria Clara", "Carvalho da Silva", "maria@creu.com"))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void updateData_WithDuplicateEmail_Returns409(){
        testClient
                .put()
                .uri("/api/users/101")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .bodyValue(new UserUpdateDto("Jose Maria", "da Silva", "josemaria@mail.com"))
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    public void updateData_WithInvalidFields_Returns422(){
        testClient
                .put()
                .uri("/api/users/101")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .bodyValue(new UserUpdateDto("", "", "jose@creu.com"))
                .exchange()
                .expectStatus().isEqualTo(422);

        testClient
                .put()
                .uri("/api/users/101")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .bodyValue(new UserUpdateDto("Jose", "Carvalho", "jose@mail.c"))
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    @Test
    public void updatePassword_WithAdminRole_Returns204() {
        testClient
                .patch()
                .uri("/api/users/101/password")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(new UserUpdatePasswordDto("123456", "jose10", "jose10"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void updatePassword_WithInvalidData_Returns400(){
        testClient
                .patch()
                .uri("/api/users/101/password")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .bodyValue(new UserUpdatePasswordDto("123456", "1234567", "1234568"))
                .exchange()
                .expectStatus().isBadRequest();

        testClient
                .patch()
                .uri("/api/users/101/password")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .bodyValue(new UserUpdatePasswordDto("123457", "1234567", "1234567"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void updatePassword_WithOperatorRoleAndOtherUser_Returns403(){
        testClient
                .patch()
                .uri("/api/users/102/password")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .bodyValue(new UserUpdatePasswordDto("123456", "1234567", "1234567"))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void updatePassword_WithInvalidField_Returns422(){
        testClient
                .patch()
                .uri("/api/users/101/password")
                .headers(JwtAuthentication.getHeaderAutorization(testClient, "jose@mail.com", "123456"))
                .bodyValue(new UserUpdatePasswordDto("123456", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422);
    }
}
