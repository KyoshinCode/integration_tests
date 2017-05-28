package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class CreateBlogPostTest extends FunctionalTests {

    @Test
    public void confirmedUserCanAddPost() {
        JSONObject request = new JSONObject()
                .put("entry", "testEntry");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(request.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED)
                .when().post("/blog/user/1/post");
    }
}
