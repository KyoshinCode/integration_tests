package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class FindUsersTests extends FunctionalTests {

	@Test
    public void REMOVEDUsersShouldNotBeFound() {
		JSONObject jsonObj = new JSONObject().put("entry", "Post");
		String result = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
                .get("/blog/user/find?searchString=cba").then().extract().asString();
		
		Assert.assertThat(result, Matchers.equalTo("[]"));
	}

}
