package apiReqres.NoPojoTests;

import apiReqres.specifications.Specifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqresNoPojo {
    private final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarsNoPojoTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        Response response = given()
                .when()
                .get("apiReqres/users?page=2")
                .then().log().all()
                .body("page", equalTo(2))
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        List<String> emails = jsonPath.get("data.email");
        List<String> ids = jsonPath.get("data.id");
        List<String> avatars = jsonPath.get("data.avatar");

        for(int i = 0; i<avatars.size(); i++){
            Assert.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }

        Assert.assertTrue(emails.stream().allMatch(x->x.endsWith("@reqres.in")));
    }

    @Test
    public void successUserRegTestNoPojo(){

    }
}
