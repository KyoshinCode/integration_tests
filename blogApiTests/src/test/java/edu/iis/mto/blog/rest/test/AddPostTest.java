package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class AddPostTest extends FunctionalTests {
	
	@Test
	public void confirmedUserCanAddPost() {
    	JSONObject jsonObj = new JSONObject().put("entry", "tralala");
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
        .post("/blog/user/1/post");
    }
}