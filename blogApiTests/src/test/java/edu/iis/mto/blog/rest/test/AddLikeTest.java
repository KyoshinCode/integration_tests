package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

/**
 * Created by Wojciech Szczepaniak on 16.06.2017.
 */
public class AddLikeTest extends FunctionalTests {

    @Test
    public void confirmedUserCanLikeOtherUserPost() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).body(is("true"))
                .when().post("/blog/user/4/like/1");
    }
}
