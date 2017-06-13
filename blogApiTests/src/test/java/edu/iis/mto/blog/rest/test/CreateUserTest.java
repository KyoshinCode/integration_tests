package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreateUserTest extends FunctionalTests {

//    @Test
//    public void postFormWithMalformedRequestDataReturnsBadRequest() {
//        JSONObject jsonObj = new JSONObject().put("email", "trace@domain.com").put("firstName", "Trace");
//        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
//                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
//                .post("/blog/user");
//    }
    
    @Test
    public void verifyIfUserHaveUniqueEmail() throws Exception {
        JSONObject jsonObj = new JSONObject().put("accountStatus", "NEW").put("email", "kapibarson@awesome.com").put("firstName", "John").put("lastName", "Kapibara");
//        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
//                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
//                .post("/blog/user");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
        		.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CONFLICT).when()
        		.post("/blog/user");
        
    }
    
}
