package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchPostTest {
	
	@Test
	public void foundPostsHaveCorrectAmountOfLikes() {

		String received = "";
		
		JSONObject jsonObj = new JSONObject().put("entry", "trala");
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
        .post("/blog/user/1/post");
    	
    	jsonObj = new JSONObject().put("entry", "tralala");
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
        .post("/blog/user/1/post");
		
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
        .post("/blog/user/1/like/1");
		
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
        .post("/blog/user/2/like/2");
    	
    	received = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(received).expect().log().all().statusCode(HttpStatus.SC_OK).when()
        .get("/blog/user/1/post").print();
    	
    	Assert.assertThat((received.contains('"' + "likesCount" + '"' + ":0") && received.contains('"' + "likesCount" + '"' + ":1")), is(equalTo(true)));
	}
}
