package com.sebebernaocode.products.service;


import com.sebebernaocode.products.web.dto.PageableDto;
import com.sebebernaocode.products.web.dto.ProductCreateDto;
import com.sebebernaocode.products.web.dto.ProductResponseDto;
import com.sebebernaocode.products.web.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/product-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/product-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createProduct_WithValidData_Returns201() {

        ProductCreateDto productCreateDto = new ProductCreateDto("test product", "test description", "url", new BigDecimal("35.00"), new HashSet<>());
        productCreateDto.getCategories().add(1L);
        productCreateDto.getCategories().add(2L);

        ProductResponseDto sut = testClient
                .post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getName()).isNotNull();
        Assertions.assertThat(sut.getDescription()).isNotNull();
        Assertions.assertThat(sut.getPrice()).isNotNull();
        Assertions.assertThat(sut.getCategories().size()).isEqualTo(2);
    }

    @Test
    public void createProduct_WithInvalidAuthToken_Returns403() {

        ProductCreateDto productCreateDto = new ProductCreateDto("test product", "test description", "url", new BigDecimal("35.00"), new HashSet<>());
        productCreateDto.getCategories().add(1L);
        productCreateDto.getCategories().add(2L);

        ErrorMessage sut = testClient
                .post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(403);
    }

    @Test
    public void createProduct_WithInvalidValidData_Returns422() {
        Set<Long> validCategories = new HashSet<>();
        validCategories.add(1L);
        validCategories.add(2L);

        ErrorMessage sut = testClient
                .post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(new ProductCreateDto("", "valid description", "alsoValid", new BigDecimal("1"), validCategories))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);

        sut = testClient
                .post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(new ProductCreateDto("valid name", "", "alsoValid", new BigDecimal("1"), validCategories))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);

        sut = testClient
                .post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(new ProductCreateDto("valid name", "valid description", "alsoValid", new BigDecimal("-1"), validCategories))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);

        sut = testClient
                .post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(new ProductCreateDto("valid name", "valid description", "alsoValid", new BigDecimal("20"), new HashSet<>()))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(422);
    }

    @Test
    public void getProductById_WithExistingId_Returns200() {
        ProductResponseDto sut = testClient
                .get()
                .uri("/api/products/{id}", "101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getName()).isEqualTo("PS4");
        Assertions.assertThat(sut.getDescription()).isEqualTo("A PLAYSTATION 4");
        Assertions.assertThat(sut.getPrice()).isEqualTo(new BigDecimal("30.00"));
    }

    @Test
    public void getProductById_WithInvalidAuthToken_Returns403() {
        ErrorMessage sut = testClient
                .get()
                .uri("/api/products/101")
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(403);
    }

    @Test
    public void getProductById_WithUnexistingId_Returns404() {
        ErrorMessage sut = testClient
                .get()
                .uri("/api/products/{id}", "999")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(404);
    }

    @Test
    public void deleteProduct_WithExistingId_Returns204() {
        Void sut = testClient
                .delete()
                .uri("/api/products/{id}", "102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .returnResult().getResponseBody();

        ErrorMessage verifyProduct = testClient
                .get()
                .uri("/api/products/{id}", "102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(verifyProduct).isNotNull();
        Assertions.assertThat(verifyProduct.getStatus()).isEqualTo(404);
    }

    @Test
    public void deleteProduct_WithInvalidAuthToken_Returns403() {
        ErrorMessage sut = testClient
                .delete()
                .uri("/api/products/102")
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(403);
    }

    @Test
    public void deleteProduct_WithUnexistingId_Returns404() {
        ErrorMessage sut = testClient
                .delete()
                .uri("/api/products/{id}", "999")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(404);
    }

    @Test
    public void getAllProducts_WithValidData_Return200(){
        PageableDto sut = testClient
                .get()
                .uri("api/products?page=1&linesPerPage=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(sut).isNotNull();
        org.assertj.core.api.Assertions.assertThat(sut.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(sut.getTotalPages()).isEqualTo(3);
        org.assertj.core.api.Assertions.assertThat(sut.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(sut.getSize()).isEqualTo(1);

        sut = testClient
                .get()
                .uri("api/products?page=0&linesPerPage=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(sut).isNotNull();
        org.assertj.core.api.Assertions.assertThat(sut.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(sut.getTotalPages()).isEqualTo(3);
        org.assertj.core.api.Assertions.assertThat(sut.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(sut.getSize()).isEqualTo(1);
    }

    @Test
    public void getAllProducts_WithInvalidAuthToken_Returns403() {
        ErrorMessage sut = testClient
                .get()
                .uri("/api/products?page=0&linesPerPage=1")
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(403);
    }

    @Test
    public void getAllProducts_WithInvalidQueryParams_Return422(){
        ErrorMessage sut = testClient
                .get()
                .uri("api/products?page=1&linesPerPage=1&orderBy=invalidparam")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(sut).isNotNull();
        org.assertj.core.api.Assertions.assertThat(sut.getStatus()).isEqualTo(422);

        sut = testClient
                .get()
                .uri("api/products?page=1&linesPerPage=1&orderBy=name&direction=invalidDirection")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(sut).isNotNull();
        org.assertj.core.api.Assertions.assertThat(sut.getStatus()).isEqualTo(422);

        sut = testClient
                .get()
                .uri("api/products?page=1&linesPerPage=-1&orderBy=name&direction=invalidDirection")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(sut).isNotNull();
        org.assertj.core.api.Assertions.assertThat(sut.getStatus()).isEqualTo(422);
    }


    @Test
    public void updateProduct_WithValidData_Returns200() {

        ProductCreateDto productCreateDto = new ProductCreateDto("altered product", "altered description", "altered_url", new BigDecimal("100.00"), new HashSet<>());
        productCreateDto.getCategories().add(1L);
        productCreateDto.getCategories().add(2L);

        ProductResponseDto sut = testClient
                .put()
                .uri("/api/products/{id}", "101")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getName()).isEqualTo("altered product");
        Assertions.assertThat(sut.getDescription()).isEqualTo("altered description");
        Assertions.assertThat(sut.getPrice()).isEqualTo(new BigDecimal("100.00"));
        Assertions.assertThat(sut.getCategories().size()).isEqualTo(2);
    }

    @Test
    public void updateProduct_WithInvalidAuthToken_Returns403() {
        ProductCreateDto productCreateDto = new ProductCreateDto("altered product", "altered description", "altered_url", new BigDecimal("100.00"), new HashSet<>());
        productCreateDto.getCategories().add(1L);
        productCreateDto.getCategories().add(2L);

        ErrorMessage sut = testClient
                .put()
                .uri("/api/products/{id}", "101")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(403);
    }

    @Test
    public void updateProduct_WithUnexistingId_Returns404() {

        ProductCreateDto productCreateDto = new ProductCreateDto("altered product", "altered description", "altered_url", new BigDecimal("100.00"), new HashSet<>());
        productCreateDto.getCategories().add(1L);
        productCreateDto.getCategories().add(2L);

        ErrorMessage sut = testClient
                .put()
                .uri("/api/products/{id}", "999")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@mail.com", "admin1"))
                .bodyValue(productCreateDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getStatus()).isEqualTo(404);
    }

}
