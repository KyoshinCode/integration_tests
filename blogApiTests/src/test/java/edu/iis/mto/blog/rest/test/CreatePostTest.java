package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;
import org.hamcrest.Matchers;
import org.junit.Assert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreatePostTest extends FunctionalTests {

	@Test
    public void createPostShouldRequireCONFIRMEDAccountStatus() {
		JSONObject jsonObj = new JSONObject().put("entry", "Post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");
		
        JSONObject jsonObj2 = new JSONObject().put("entry", "Post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj2.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/2/post");
    }
	
	@Test
    public void addLikeShouldRequireCONFIRMEDAccountStatus() {
		JSONObject jsonObj = new JSONObject().put("entry", "Post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
	            .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
	            .post("/blog/user/2/like/1");
 	}
	
	@Test
	public void addLikeTwiceShouldNotChangePostStatus() {
		JSONObject jsonObj = new JSONObject().put("entry", "Post");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
				.post("/blog/user/1/post");
	     
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
				.post("/blog/user/3/like/1");
	     
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
		        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
		        .post("/blog/user/3/like/1");
	     
		int likesCount = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
				.get("/blog/user/1/post").then().extract().jsonPath().getInt("likesCount[0]");
	     
		Assert.assertThat(likesCount, Matchers.equalTo(1));
	}
}
