package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class AddPostTest {

	@Test
    public void addingPostShouldCheckCONFIRMEDStatusOfAccount() {
		
		JSONObject first = new JSONObject().put("entry", "First test");
		
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(first.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");
        
        JSONObject second = new JSONObject().put("entry", "Second test");
		
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(second.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/2/post");
		
    }

}