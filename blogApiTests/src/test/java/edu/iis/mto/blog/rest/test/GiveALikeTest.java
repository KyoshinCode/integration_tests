package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by Konrad Gos on 22.05.2017.
 */
public class GiveALikeTest extends FunctionalTests  {
    @Test
    public void confirmedUserCanLikeOtherUsersPost() {
        JSONObject jsonObj = new JSONObject().put("entry", "entry text");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .post("/blog/user/2/like/1");
    }

    @Test
    public void confirmedUserIsNotAbleToLikeHisOwnPost() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/1/like/1");
    }

    @Test
    public void multipleLikesFromTheSameUserDoNotChangePostLikeCount() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .post("/blog/user/2/like/1");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .post("/blog/user/2/like/1");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .get("/blog/user/1/post").print().contains("likesCount:" + "1");
    }
}
