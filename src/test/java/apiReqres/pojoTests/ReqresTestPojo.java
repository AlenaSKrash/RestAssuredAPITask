package apiReqres.pojoTests;

import apiReqres.colors.ColorsData;
import apiReqres.registration.Register;
import apiReqres.registration.SuccessReg;
import apiReqres.registration.UnSuccessReg;
import apiReqres.specifications.Specifications;
import apiReqres.users.UserData;
import apiReqres.users.UserTime;
import apiReqres.users.UserTimeResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTestPojo {
    private final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarANdIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        List<UserData> users = given()
                .when()
                .get("apiReqres/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        //Names of avatar files contain correct ids of users
        users.forEach(x -> Assert.assertTrue(x.getAvatar().contains(String.valueOf(x.getId()))));

        //Emails of users end with "@reqres.in"
        Assert.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        //Another way of checking if names of avatar files contain correct ids of users
        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x -> String.valueOf(x.getId())).collect(Collectors.toList());
        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test
    public void successRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        Register user = new Register("pistol", "eve.holt@reqres.in");
        SuccessReg successReg = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("apiReqres/register")
                .then().log().all()
                .extract().as(SuccessReg.class);

        //Successful registration case
        Assert.assertEquals(4, successReg.getId());
        Assert.assertEquals("QpwL5tke4Pnpja7X4", successReg.getToken());
    }

    @Test
    public void unSuccessfullReg() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec400());
        Register user = new Register("", "sydney@fife");
        UnSuccessReg unSuccessReg = given()
                .body(user)
                .when()
                .post("apiReqres/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);

        //Unsuccessful registration case
        Assert.assertEquals("Missing password", unSuccessReg.getError());
    }

    @Test
    public void sortedYearsTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        List<ColorsData> colors = given()
                .when()
                .get("apiReqres/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorsData.class);
        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        //LIST<RESOURCE> returns sorted years in the ascending order
        Assert.assertEquals(sortedYears, years);
    }

    @Test
    public void deleteUserTest(){

        //Checking if status is 204 with delete method
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUniq(204));
        given()
                .when().delete("apiReqres/users/2")
                .then().log().all();
    }

    @Test
    public void timeTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        UserTime user = new UserTime("morpheus", "zion resident");
        UserTimeResponse response = given()
                .body(user)
                .when()
                .put("apiReqres/users/2")
                .then().log().all()
                .extract().as(UserTimeResponse.class);
        String regex = "(.{5})$";
        // REGEX DOESNT WORK FOR CURRENT TIME - NO IDEA WHY
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");
        System.out.println(currentTime);
        Assert.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regex, ""));
        System.out.println(response.getUpdatedAt().replaceAll(regex, ""));
    }
}

