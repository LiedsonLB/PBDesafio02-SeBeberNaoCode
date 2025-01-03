package com.sebebernaocode.products.service;

import com.sebebernaocode.products.web.dto.CategoryCreateDto;
import com.sebebernaocode.products.web.dto.CategoryResponseDto;
import com.sebebernaocode.products.web.exception.ErrorMessage;
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
@Sql(scripts = "/sql/category-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/category-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createCategory_WithValidData_Returns201() {
        CategoryCreateDto productCreateDto = new CategoryCreateDto("test category");

        CategoryResponseDto sut = testClient
                .post()
                .uri("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CategoryResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getName()).isEqualTo("test category");
    }

    @Test
    public void createCategory_WithInvalidData_Returns422() {
        CategoryCreateDto productCreateDto = new CategoryCreateDto("");

        ErrorMessage sut = testClient
                .post()
                .uri("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);
    }

    @Test
    public void createCategory_WithInvalidAuthorization_Returns403() {
        CategoryCreateDto productCreateDto = new CategoryCreateDto("valid data");

        ErrorMessage sut = testClient
                .post()
                .uri("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(403);
    }

    @Test
    public void getById_WithValidData_Returns200() {
        CategoryResponseDto sut = testClient
                .get()
                .uri("/api/categories/{id}", 100)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getName()).isEqualTo("ANY CATEGORY");
    }

    @Test
    public void getById_WithUnexistingId_Returns404() {
        ErrorMessage sut = testClient
                .get()
                .uri("/api/categories/{id}", 999)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(404);
    }

    @Test
    public void updateCategory_WithValidData_Returns200() {
        CategoryCreateDto productCreateDto = new CategoryCreateDto("altered category");

        CategoryResponseDto sut = testClient
                .put()
                .uri("/api/categories/{id}", 100)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getName()).isEqualTo("altered category");
    }

    @Test
    public void updateCategory_WithUnexistingId_Returns404() {
        CategoryCreateDto productCreateDto = new CategoryCreateDto("altered category");

        ErrorMessage sut = testClient
                .put()
                .uri("/api/categories/{id}", 999)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(404);
    }

    @Test
    public void updateCategory_WithInvalidAuthorization_Returns404() {
        CategoryCreateDto productCreateDto = new CategoryCreateDto("altered category");

        ErrorMessage sut = testClient
                .put()
                .uri("/api/categories/{id}", 999)
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(403);
    }

    @Test
    public void deleteCategory_WithExistingId_Returns204() {
        Void sut = testClient
                .delete()
                .uri("/api/categories/{id}", "100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .returnResult().getResponseBody();

        ErrorMessage verifyCategory = testClient
                .get()
                .uri("/api/categories/{id}", "100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(verifyCategory).isNotNull();
        Assertions.assertThat(verifyCategory.getStatus()).isEqualTo(404);
    }

    @Test
    public void deleteCategory_WithInvalidAuthToken_Returns403() {
        ErrorMessage sut = testClient
                .delete()
                .uri("/api/categories/102")
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(403);
    }

    @Test
    public void deleteCategory_WithUnexistingId_Returns404() {
        ErrorMessage sut = testClient
                .delete()
                .uri("/api/categories/{id}", "999")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(404);
    }
}
