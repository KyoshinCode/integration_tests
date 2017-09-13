package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Piotrek on 23.05.2017.
 */
public class SearchUserTest {

    @Test
    public void findCorrectUser(){

        String receive1 = "", receive2 = "";

        receive1 = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(receive1).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .get("/blog/user/1/").print();

        receive2 = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(receive2).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .get("/blog/user/2/").print();

        Assert.assertThat((receive1.contains("john@domain.com") &&
                receive2.contains("brian@domain.com")), is(equalTo(true)));
    }
}
