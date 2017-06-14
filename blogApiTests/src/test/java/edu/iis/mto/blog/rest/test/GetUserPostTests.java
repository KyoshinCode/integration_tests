package edu.iis.mto.blog.rest.test;

import static org.junit.Assert.*;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class GetUserPostTests extends FunctionalTests {

	@Test
    public void getREMOVEDUserPostsShouldFail() {
		JSONObject jsonObj = new JSONObject().put("entry", "Post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/4/post");
	}
	
	@Test
	 public void getUserPostsShouldReturnCorrectLikesCount() {
			JSONObject jsonObj = new JSONObject().put("entry", "Post");
		 RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
			 .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
			 .post("/blog/user/1/post");
			 
		 RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
			 .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
			 .post("/blog/user/3/like/1"); // 3 is id of CONFIRMED USER
		 
		 RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
			.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
			.post("/blog/user/5/like/1"); // 5 is id of CONFIRMED USER
			 
		 int likesCount = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
			.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
			.get("/blog/user/1/post").then().extract().jsonPath().getInt("likesCount[0]");
		 
		 Assert.assertThat(likesCount, Matchers.equalTo(2));
	}
}
