package edu.iis.mto.blog.rest.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchUserTest {

	@Test
	public void userSearchReturnsCorrectUser() {
		
		String received = "", received2 = "";
		
		received = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
		        .body(received).expect().log().all().statusCode(HttpStatus.SC_OK).when()
		        .get("/blog/user/1/").print();
		
		received2 = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
		        .body(received2).expect().log().all().statusCode(HttpStatus.SC_OK).when()
		        .get("/blog/user/2/").print();
		    	
		Assert.assertThat((received.contains("john@domain.com") &&
				received2.contains("brian@domain.com")), is(equalTo(true)));
	}

}
