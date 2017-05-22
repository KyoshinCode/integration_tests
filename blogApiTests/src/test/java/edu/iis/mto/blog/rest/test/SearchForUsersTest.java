package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Konrad Gos on 22.05.2017.
 */
public class SearchForUsersTest extends FunctionalTests {
    @Test
    public void searchForRemovedUsersShouldBeEmpty() {
    JSONObject jsonObj = new JSONObject();
    String result = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
            .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
            .get("/blog/user/find?searchString=Torreto").then().extract().asString();

    Assert.assertThat(result, Matchers.equalTo("[]"));
    }

}
