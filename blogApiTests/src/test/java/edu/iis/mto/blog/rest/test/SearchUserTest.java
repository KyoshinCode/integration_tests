package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Sasho on 2017-05-29.
 */
public class SearchUserTest extends FunctionalTests {
    @Test
    public void noRemovedUserFound() throws Exception {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).body("id", not(hasItem(3))).when()
                .get("/blog/user/find?searchString=");
    }
}
