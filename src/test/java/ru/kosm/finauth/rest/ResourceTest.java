package ru.kosm.finauth.rest;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.kosm.finauth.FinauthApp;

/**
 * Unit test for REST resource. The test covers all REST API methods
 * and tests their accuracy.
 * 
 * @author kosm
 */
public class ResourceTest extends Assert {
	
	private FinauthApp app;
	
	@Before
	public void setUpResourceTest() throws IOException {
		app = new FinauthApp();
		app.start();
		RestAssured.port = FinauthApp.defaultPort;
		RestAssured.baseURI = "http://" + FinauthApp.defaultHost;
		RestAssured.basePath = "/finauth";
	}
	
	@After
	public void tearDownResourceTest() {
		app.stop();
	}
	
	String addUserBody() {
		return " { \"Add user\" : " +
				"{ \"firstName\" : \"Test\", \"lastName\" : \"Tester\", \"login\" : \"test\" }" +
			"}"; 
	}
	
	String getUserBody(String userId) {
		return String.format(" { \"Get user\" : " +
				"{ \"userId\" : \"%s\" }" +
			"}", userId);
	}
	
	Response getUser(String userId) {
		final String requestBody = " { \"Get user\" : " +
				"{ \"userId\" : \"%s\" }" +
			"}";
		
		return given().log().all().contentType(ContentType.JSON)
			.body(String.format(requestBody, userId))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get user\".status", equalTo("OK"))
			.body("\"Get user\".user.firstName", equalTo("Test"))
			.body("\"Get user\".user.lastName", equalTo("Tester"))
			.body("\"Get user\".user.login", equalTo("test"))
			.extract().response();
	}
	
	/**
	 * Test "Add user" operation - positive case
	 */
	@Test
	public void addUserPositive() {
		given().log().all().contentType(ContentType.JSON)
			.body(addUserBody())
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Add user\".status", equalTo("OK"))
			.body("\"Add user\".operationId", not(empty()))
			.body("\"Add user\".userId", not(empty()));
	}
	
	/**
	 * Test "Get user" operation - positive case
	 */
	@Test
	public void getUserPositive() {
		Response response = given().log().all().contentType(ContentType.JSON)
			.body(addUserBody())
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Add user\".status", equalTo("OK"))
			.body("\"Add user\".operationId", not(empty()))
			.body("\"Add user\".userId", not(empty()))
			.extract().response();
		
		given().log().all().contentType(ContentType.JSON)
			.body(getUserBody(response.path("\"Add user\".userId")))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get user\".status", equalTo("OK"))
			.body("\"Get user\".user.firstName", equalTo("Test"))
			.body("\"Get user\".user.lastName", equalTo("Tester"))
			.body("\"Get user\".user.login", equalTo("test"));
	}
	
	/**
	 * Test "Get user" operation - negative case: invalid userId
	 */
	@Test
	public void getUserNegative() {
		given().log().all().contentType(ContentType.JSON)
			.body(getUserBody("Invalid ID"))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get user\".status", equalTo("ERROR"));
	}

}
