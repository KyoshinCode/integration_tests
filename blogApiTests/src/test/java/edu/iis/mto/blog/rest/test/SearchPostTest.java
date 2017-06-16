package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Wojciech Szczepaniak on 16.06.2017.
 */
public class SearchPostTest extends FunctionalTests {

    @Test
    public void searchRemovedUserPostsShouldNotFindUser() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_NOT_FOUND)
                .when().get("/blog/user/3/post");
    }

    @Test
    public void searchUserPostsReturnPostsWithCorrectLikesNumber() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK)
                .when().post("/blog/user/4/like/3");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK)
                .when().post("/blog/user/5/like/3");

        JsonPath request = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK)
                .when().get("/blog/user/1/post")
                .then().extract().jsonPath();

        int postNumber = request.getInt("$.size()");
        assertThat(postNumber, equalTo(2));

        int likesNumber = request.getInt("likesCount[1]");
        assertThat(likesNumber, equalTo(2));
    }
}
