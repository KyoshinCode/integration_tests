package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreateUserTest extends FunctionalTests {
	
	@Ignore
    public void postFormWithMalformedRequestDataReturnsBadRequest() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user");
    }
    
    @Ignore
    public void userShouldHaveUniqueEmail() {
    	JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com").put("accountStatus", "NEW").put("firstName", "John").put("lastName", "Cena");
    	RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CONFLICT).when()
        .post("/blog/user");
    }
    
    @Test
    public void orderedTestSuite() {
    	postFormWithMalformedRequestDataReturnsBadRequest();
    	userShouldHaveUniqueEmail();
    }
}
