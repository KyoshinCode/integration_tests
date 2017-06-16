package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

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
}
