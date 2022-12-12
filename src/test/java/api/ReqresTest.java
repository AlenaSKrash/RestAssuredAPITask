package api;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTest {
    private final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarANdIdTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        List<DataItem> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", DataItem.class);
        users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(String.valueOf(x.getId()))));
        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));
//
        List <String> avatars = users.stream().map(DataItem::getAvatar).collect(Collectors.toList());
        List <String> ids = users.stream().map(x->String.valueOf(x.getId())).collect(Collectors.toList());
        for(int i = 0; i<avatars.size(); i++){
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }

    }
}
