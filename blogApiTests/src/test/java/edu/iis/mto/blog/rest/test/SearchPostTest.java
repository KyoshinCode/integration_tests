package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

/**
 * Created by Sasho on 2017-05-29.
 */
public class SearchPostTest extends FunctionalTests {
    @Test
    public void searchRemovedUserPosts() throws Exception {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_NOT_FOUND).when()
                .get("/blog/user/3/post");
    }

    @Test
    public void correctLikeNumber() throws Exception {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .post("/blog/user/4/like/3");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .post("/blog/user/5/like/3");
        int likes = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .get("/blog/user/1/post").then().extract().jsonPath().getInt("likesCount[1]");
        Assert.assertThat(likes, Matchers.equalTo(2));
    }
}
