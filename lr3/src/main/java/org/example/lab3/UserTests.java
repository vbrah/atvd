package org.example.lab3;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserTests {
    private static final String baseUrl = "https://petstore.swagger.io/v2/";

    private static final String USER = "/user",
     USER_USERNAME = USER + "/{username}",
    USER_LOGIN = USER + "/login",
    USER_LOGOUT = USER + "/logout";

    private static final String PET = "/pet", PET_PETID = PET + "/{petId}";

    private String petId;
    private String petName;

    private Map<String, Object> bodyPets;

    private String username;
    private String firstName;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyLoginAction() {
        Map<String, ?> body = Map.of(
                "username", "BrahaVladyslav",
                "password", "121-21ck-1"
        );
        Response response = given().
                body(body)
                .get(USER_LOGIN);

        response.then()
                .statusCode(HttpStatus.SC_OK);

        String responseBody = response.getBody().asString();
        String sessionId = JsonPath.from(responseBody).getString("message").replaceAll("[^0-9]", "");

        RestAssured.requestSpecification.sessionId(sessionId);
    }

    @Test(dependsOnMethods = "verifyLoginAction")
    public void verifyCreateAction() {
        username = Faker.instance().name().username();
        firstName = Faker.instance().harryPotter().character();

        Map<String, ?> body = Map.of(
                "username", username,
                "firstName", firstName,
                "lastName", Faker.instance().gameOfThrones().character(),
                "email", Faker.instance().internet().emailAddress(),
                "password", Faker.instance().internet().password(),
                "phone", Faker.instance().phoneNumber().phoneNumber(),
                "userStatus", Integer.valueOf("1")
        );

        given().body(body)
                .post(USER)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyCreateAction")
    public void verifyGetAction() {
        given().pathParams("username", username)
                .get(USER_USERNAME)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("firstName", equalTo(firstName));
    }

//    @Test(dependsOnMethods = "verifyGetAction")
//    public void verifyDeleteAction() {
//        given().pathParams("username", username)
//                .delete(USER_USERNAME)
//                .then()
//                .statusCode(HttpStatus.SC_OK);
//    }
//
//    @Test(dependsOnMethods = "verifyLoginAction", priority = 1)
//    public void verifyLogoutAction() {
//        given().get(USER_LOGOUT)
//                .then()
//                .statusCode(HttpStatus.SC_OK);
//    }

    @Test(dependsOnMethods = "verifyGetAction")
    public void verifyAddPets() {
      String imageUrl = "https://th.bing.com/th?id=OSK.HERO2fPxnqkreBmznkEzogr96euNnZp5ja7yjeqNXV_Yoq8&w=472&h=280&c=1&rs=2&o=6&dpr=1.5&pid=SANGAM";

        petId = "9223372036854607001";
        petName = "Kitty";

        Map<String, Object> petCategory = new HashMap<>();
        petCategory.put("id", 0);
        petCategory.put("name", "cat");

        Map<String, Object> petTags = new HashMap<>();
        petTags.put("id", 0);
        petTags.put("name", petName);

        bodyPets = new HashMap<>();
        bodyPets.put("id", petId);
        bodyPets.put("category", petCategory);
        bodyPets.put("name", petName);
        bodyPets.put("photoUrls", new String[]{imageUrl});
        bodyPets.put("tags", new Map[]{petTags});
        bodyPets.put("status", "available");

      given().body(bodyPets)
              .post(PET)
              .then()
              .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyAddPets")
    public void verifyGetPets() {
        given()
                .pathParam("petId", petId)
                .when()
                .get(PET_PETID)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyGetPets")
    public void verifyUpdatePets() {
        String updatedPetName = "NewKitty";

        bodyPets.put("name", updatedPetName);

        given()
                .contentType("application/json")
                .body(bodyPets)
                .when()
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
