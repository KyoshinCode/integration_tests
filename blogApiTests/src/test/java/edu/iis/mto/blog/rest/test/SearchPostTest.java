package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Piotrek on 23.05.2017.
 */
public class SearchPostTest {

    @Test
    public void findPostWithCorrectAmountOfLikes(){
        String receive = "";
        JSONObject jsonObj = new JSONObject().put("entry", "message");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");

        jsonObj = new JSONObject().put("entry", "message");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/1/like/1");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .post("/blog/user/2/like/2");

        receive = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(receive).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .get("/blog/user/1/post").print();

        Assert.assertThat((receive.contains('"' + "likesCount" + '"' + ":0") && receive.contains('"' + "likesCount" + '"' + ":1")), is(equalTo(true)));
    }
}
