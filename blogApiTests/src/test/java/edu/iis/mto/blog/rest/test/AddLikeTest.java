package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class AddLikeTest {
	
	@Ignore
	public void confirmedUserCanLikeOtherUsersPost() {
		
		JSONObject jsonObj = new JSONObject().put("entry", "tralala");
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
        .post("/blog/user/1/post");
    	
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
        .post("/blog/user/3/like/1");
	}
	
	@Ignore
	public void confirmedUserCannotLikeHisOwnPost() {
    	
		JSONObject jsonObj = new JSONObject();
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
        .post("/blog/user/1/like/1");
	}
	
	@Ignore
	public void multipleLikesFromTheSameUserDontChangePostLikeCount() {

		JSONObject jsonObj = new JSONObject();
		String received = "";
		
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
        .post("/blog/user/3/like/1");
    	
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(received).expect().log().all().statusCode(HttpStatus.SC_OK).when()
        .get("/blog/user/1/post").print().contains("likesCount:" + " 1");
	}
	
	@Ignore
	public void newUserCannotLikePost() {
		JSONObject jsonObj = new JSONObject();
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
        .post("/blog/user/2/like/1");
	}
	
	@Test
	public void orderedTestFixture() {
		confirmedUserCanLikeOtherUsersPost();
		confirmedUserCannotLikeHisOwnPost();
		multipleLikesFromTheSameUserDontChangePostLikeCount();
	}
}
