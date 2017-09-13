package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import org.junit.Test;

public class AddLikeTest {
	
	@Test
    public void addingLikeShouldCheckCONFIRMEDStatusOfAccount() {
		
		JSONObject first = new JSONObject().put("entry", "First test for like");
		
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(first.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/1/like/1");
	}
	
	@Test
    public void authorOfPostCannotLikeOwnPost() {
		
		JSONObject second = new JSONObject().put("entry", "Second test for like");
		
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(second.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/2/like/1");
	}

}
