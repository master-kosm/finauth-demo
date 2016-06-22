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
		given().log().all();
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
	
	/**
	 * Test "Add user" operation
	 */
	@Test
	public void addNewUser() {
		given().log().all().contentType(ContentType.JSON)
			.body(addUserBody())
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("Add user.status", equalTo("OK"))
			.body("Add user.userId", not(empty()));
	}

}
