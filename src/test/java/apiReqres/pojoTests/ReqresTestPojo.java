package apiReqres.pojoTests;

import apiReqres.users.UserData;
import apiReqres.users.UserTime;
import apiReqres.users.UserTimeResponse;
import apiReqres.colors.ColorsData;
import apiReqres.registration.Register;
import apiReqres.registration.SuccessReg;
import apiReqres.registration.UnSuccessReg;
import apiReqres.specifications.Specifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTestPojo {
    private final static String URL = "https://reqres.in/";

    //GET1
    @Test
    @DisplayName("Names of avatar files contain correct ids of users," +
            "Emails of users end with @reqres.in")
    public void checkAvatarANdIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        //Names of avatar files contain correct ids of users
        users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(String.valueOf(x.getId()))));

        //Emails of users end with "@reqres.in"
        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        //Another way of checking if names of avatar files contain correct ids of users
        List<String> avatars = users.stream().map(UserData::getAvatar).toList();
        List<String> ids = users.stream().map(x -> String.valueOf(x.getId())).toList();
        for (int i = 0; i < avatars.size(); i++) {
            Assertions.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    //GET2
    @Test
    @DisplayName("Users id is 2, email is janet.weaver@reqres.in, name is Janet, last name is Weaver")
    public void correctUserDetailsTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        Response response = given()
                .when()
                .get("api/users/2")
                .then().log().all()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Integer id = jsonPath.get("data.id");
        String email = jsonPath.get("data.email");
        String firstName = jsonPath.get("data.first_name");
        String lastName = jsonPath.get("data.last_name");
        Assertions.assertEquals(2,id);
        Assertions.assertEquals("janet.weaver@reqres.in", email);
        Assertions.assertEquals("Janet", firstName);
        Assertions.assertEquals("Weaver", lastName);
    }

    //GET3
    @Test
    @DisplayName("User not found response is 404")
    public void userNotFound(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUniq(404));
        given().when().get("/api/users/23").then().log().all();
    }

    //GET4
    @Test
    @DisplayName("Returns sorted years in the ascending order")
    public void sortedYearsTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        List<ColorsData> colors = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorsData.class);
        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        //LIST<RESOURCE> returns sorted years in the ascending order
        Assertions.assertEquals(sortedYears, years);
    }
    
    //GET5
    @Test
    @DisplayName("Resource's id is 2, name is 'fuchsia rose', year is 2001, color is '#C74375'")
    public void dataOfTheColorSourceTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        Response response = given()
                .when()
                .get("api/unknown/2")
                .then().log().all()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        System.out.println(response);
        Integer id = jsonPath.get("data.id");
        String name = jsonPath.get("data.name");
        String color = jsonPath.get("data.color");
        Integer year = jsonPath.get("data.year");
        Assertions.assertEquals(2,id);
        Assertions.assertEquals("fuchsia rose", name);
        Assertions.assertEquals("#C74375", color);
        Assertions.assertEquals(2001, year);
    }

    //GET6
    @Test
    @DisplayName("Resource with id 23 is not found and returns 404 error")
    public void resourceNotFound(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUniq(404));
        given().when().get("api/unknown/23")
                .then().log().all();
    }

    //POST2
    @Test
    @DisplayName("Successful registration")
    public void successRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        Register user = new Register("pistol", "eve.holt@reqres.in");
        SuccessReg successReg = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessReg.class);

        //Successful registration case
        Assertions.assertEquals(4, successReg.getId());
        Assertions.assertEquals("QpwL5tke4Pnpja7X4", successReg.getToken());
    }

    //POST3
    @Test
    @DisplayName("Registration is not successful with empty password and returns an error message")
    public void unSuccessfullReg() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec400());
        Register user = new Register("", "sydney@fife");
        UnSuccessReg unSuccessReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);

        //Unsuccessful registration case
        Assertions.assertEquals("Missing password", unSuccessReg.getError());
    }



    @Test
    @DisplayName("Delete User")
    public void deleteUserTest(){

        //Checking if status is 204 with delete method
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUniq(204));
        given()
                .when().delete("api/users/2")
                .then().log().all();
    }

    @Test
    @DisplayName("Patch/update the user class by adding updatedAt")
    public void timeTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        UserTime user = new UserTime("morpheus", "zion resident");
        UserTimeResponse response = given()
                .body(user)
                .when()
                .put("api/users/2")
                .then().log().all()
                .extract().as(UserTimeResponse.class);
        String regex = "(.{5})$";

        // REGEX DOESNT WORK FOR CURRENT TIME - NO IDEA WHY
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");
        System.out.println(currentTime);

        Assertions.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regex, ""));
        System.out.println(response.getUpdatedAt().replaceAll(regex, ""));
    }
}

