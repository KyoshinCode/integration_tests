package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by Konrad Gos on 22.05.2017.
 */
public class SearchForUserPostsTest extends FunctionalTests {
    @Test
    public void searchForPostsOfRemovedUsersShouldFail() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .get("/blog/user/3/post");
    }

}
