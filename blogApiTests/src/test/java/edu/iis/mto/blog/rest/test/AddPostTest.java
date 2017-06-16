package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by Wojciech Szczepaniak on 07.06.2017.
 */
public class AddPostTest extends FunctionalTests {

    @Test
    public void confirmedUserCanAddPost() {
        JSONObject jsonObject = new JSONObject().put("entry", "Test");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED)
                .when().post("/blog/user/1/post");
    }

    @Test
    public void newUserCannotAddPost() {
        JSONObject jsonObject = new JSONObject().put("entry", "Test 2");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post("/blog/user/2/post");
    }

    @Test
    public void removedUserCannotAddPost() {
        JSONObject jsonObject = new JSONObject().put("entry", "Test 3");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post("/blog/user/3/post");
    }
}
