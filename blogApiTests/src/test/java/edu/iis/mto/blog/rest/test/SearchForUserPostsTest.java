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
public class SearchForUserPostsTest extends FunctionalTests {
    @Test
    public void searchForPostsOfRemovedUsersShouldFail() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .get("/blog/user/3/post");
    }

    @Test
    public void searchForPostsShouldReturnCorrectLikesCount() {
        JSONObject jsonObj = new JSONObject().put("entry", "entry text");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .post("/blog/user/4/like/1");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .post("/blog/user/5/like/1");

        int likesCount = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .get("/blog/user/1/post").then().extract().jsonPath().getInt("likesCount[0]");

        Assert.assertThat(likesCount, Matchers.equalTo(2));
    }

}
