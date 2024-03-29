package org.example.lab4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class ServerMockTests {
    private static final String baseUrl = "https://d3c09a90-ff6f-43c1-a70f-5e083b450ca2.mock.pstmn.io/";

    private static final String OWNER_NAME_URL = baseUrl + "owner-name/";
    private static final String CREATE_POST_URL = baseUrl + "create-post/";
    private static final String UPD_POST_URL = baseUrl + "post/{id}";


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void getOwnerNameSuccess() {
        given().get(OWNER_NAME_URL)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("name", equalTo("Braha Vladyslav"));
    }

    @Test(dependsOnMethods = "getOwnerNameSuccess")
    public void getOwnerNameUnsuccess() {
        given().get(OWNER_NAME_URL+"unsuccess/")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("exception", equalTo("I won't say my name"));
    }

    @Test(dependsOnMethods = "getOwnerNameUnsuccess")
    public void postCreatePost() {
        Map<String, ?> body = Map.of(
                "id", 1,
                "title", "music",
                "content", "I like listen to music so much."
        );

        given()
                .body(body)
                .queryParam("permission", "true")
                .post(CREATE_POST_URL)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test(dependsOnMethods = "postCreatePost")
    public void postCreatePostUnsuccess() {
        given()
                .post(CREATE_POST_URL)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(dependsOnMethods = "postCreatePostUnsuccess")
    public void putPost() {
        Map<String, ?> body = Map.of(
                "title", "music 2",
                "content", "I like listen to music so much."
        );

        given()
                .body(body)
                .pathParams("id", "1")
                .put(UPD_POST_URL)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "putPost")
    public void deletePost() {
        given()
                .pathParams("id", "1")
                .delete(UPD_POST_URL)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}
