package com.alz2019.integration;

import com.alz2019.dto.RequestDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MarsIT {
    @LocalServerPort
    private int port;

    @Test
    @Order(1)
    void downloadPhotosTest() {
        given()
                .port(port)
                .with().body(new RequestDto(15)).contentType("application/json")
                .when()
                .post("/api/v1/pictures")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void getSmallestPhotoTest() {
        given()
                .port(port)
                .when()
                .get("/api/v1/pictures/smallest")
                .then()
                .statusCode(200)
                .and()
                .contentType("image/png");
    }

    @Test
    @Order(3)
    void getLargestPhotoTest() {
        given()
                .port(port)
                .when()
                .get("/api/v1/pictures/largest")
                .then()
                .statusCode(200)
                .and()
                .contentType("image/png");
    }
}
