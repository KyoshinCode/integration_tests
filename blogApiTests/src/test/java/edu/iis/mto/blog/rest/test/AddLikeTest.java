package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

/**
 * Created by Sasho on 2017-05-29.
 */
public class AddLikeTest extends FunctionalTests {
    @Test
    public void confirmedUserCanAddLike() throws Exception {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).body(is("true")).when()
                .post("/blog/user/4/like/1");
    }
    @Test
    public void NewUserCannotAddPost() throws Exception {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/2/like/1");
    }

    @Test
    public void RemovedUserCannotAddPost() throws Exception {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/3/like/1");
    }

    @Test
    public void cannotGiveLikeToOwnPost() throws Exception {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/1/like/1");
    }

    @Test
    public void noChangesWhenLikeTwice() throws Exception {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).body(is("false")).when()
                .post("/blog/user/5/like/1");
    }
}
