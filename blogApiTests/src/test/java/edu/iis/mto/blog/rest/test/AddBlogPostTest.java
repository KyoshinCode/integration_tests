package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by Sasho on 2017-05-29.
 */
public class AddBlogPostTest extends FunctionalTests {
    @Test
    public void ConfirmedUsedCanAddPost() throws Exception {
        JSONObject jsonObject = new JSONObject().put("entry", "post testowy");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");
    }

    @Test
    public void NewUserCannotAddPost() throws Exception {
        JSONObject jsonObject = new JSONObject().put("entry", "post testowy 2");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/2/post");
    }

    @Test
    public void RemovedUserCannotAddPost() throws Exception {
        JSONObject jsonObject = new JSONObject().put("entry", "post testowy 3");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/3/post");
    }
}
