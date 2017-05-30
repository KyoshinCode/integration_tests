package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreateUserTest extends FunctionalTests {

	String HEADER_1 = "Content-Type";
	String HEADER_2 = "application/json;charset=UTF-8";
	
	
    @Test
    public void postFormWithMalformedRequestDataReturnsBadRequest() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user");
    }
    
    
    //@Ignore
    @Test
    public void createUserRequireUniqueEmail() {
    	
    	JSONObject first = new JSONObject().put("email", "kamil@gmail.com");
    	JSONObject second = new JSONObject().put("email", "kamil@gmail.com");
    	
    	RestAssured.given().accept(ContentType.JSON)
    	.header(HEADER_1, HEADER_2)
    	.body(first.toString()).expect().log().all()
    	.statusCode(HttpStatus.SC_CREATED).when().post("/blog/user");
    	
    	RestAssured.given().accept(ContentType.JSON)
    	.header(HEADER_1, HEADER_2)
    	.body(second.toString()).expect().log().all().statusCode(HttpStatus.SC_CONFLICT).when()
    	.post("/blog/user");
    	
    	
    }
    
    
}
