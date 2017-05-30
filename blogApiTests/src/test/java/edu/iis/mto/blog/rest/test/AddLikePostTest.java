package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class AddLikePostTest extends FunctionalTests {

    @Test
    public void confirmedUserCanAddLike() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK)
                .when().post("/blog/user/3/like/1");
    }

    @Test
    public void newUserCanNotAddLike() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post("/blog/user/2/like/1");
    }

    @Test
    public void cannotLikeOwnPost() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post("/blog/user/1/like/1");
    }

    @Test
    public void repeatedLikingShouldNotChangeAnything() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK)
                .body(is("false"))
                .when().post("/blog/user/5/like/1");
    }
}
