package ru.kosm.finauth.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.kosm.finauth.FinauthApp;
import ru.kosm.finauth.core.activity.ApplyFeeActivity;

/**
 * Unit test for REST resource. The test covers all REST API methods
 * and tests their accuracy.
 * 
 * @author kosm
 */
public class ResourceTest extends Assert {
	
	private static FinauthApp app;
	
	@BeforeClass
	public static void setUpResourceTest() throws IOException {
		app = new FinauthApp();
		app.start();
		RestAssured.port = FinauthApp.defaultPort;
		RestAssured.baseURI = "http://" + FinauthApp.defaultHost;
		RestAssured.basePath = "/finauth";
	}
	
	@AfterClass
	public static void tearDownResourceTest() {
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
	
	String adjustBody(String accountId, int amount) {
		return String.format(" { \"Adjust\" : " + 
				"{ \"accountId\" : \"%s\", \"amount\" : \"%d\" }" +
			"}", accountId, amount);
	}
	
	String transferBody(String sourceAccountId, String targetAccountId, int amount) {
		return String.format(" { \"Transfer\" : " + 
				"{ \"sourceAccountId\" : \"%s\", \"targetAccountId\" : \"%s\", \"amount\" : \"%d\" }" +
			"}", sourceAccountId, targetAccountId, amount);
	}
	
	String getAccountBody(String accountId) {
		return String.format(" { \"Get account\" : " +
				"{ \"accountId\" : \"%s\" }" +
			"}", accountId);
	}
	
	String getOperationBody(String operationId) {
		return String.format(" { \"Get operation\" : " +
				"{ \"operationId\" : \"%s\" }" +
			"}", operationId);
	}
	
	/**
	 * Test "Add user" operation - positive case
	 */
	@Test
	public void addUserPositive() {
		// Add user
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
		// Add user
		Response response = given().log().all().contentType(ContentType.JSON)
			.body(addUserBody())
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Add user\".status", equalTo("OK"))
			.body("\"Add user\".operationId", not(empty()))
			.body("\"Add user\".userId", not(empty()))
			.extract().response();
		
		// Get added user
		given().log().all().contentType(ContentType.JSON)
			.body(getUserBody(response.path("\"Add user\".userId")))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get user\".status", equalTo("OK"))
			.body("\"Get user\".operationId", not(empty()))
			.body("\"Get user\".user.firstName", equalTo("Test"))
			.body("\"Get user\".user.lastName", equalTo("Tester"))
			.body("\"Get user\".user.login", equalTo("test"));
	}
	
	/**
	 * Test "Get user" operation - negative case: invalid userId
	 */
	@Test
	public void getUserNegativeInvalidUserId() {
		// Get user with invalid ID
		given().log().all().contentType(ContentType.JSON)
			.body(getUserBody("Invalid ID"))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get user\".status", equalTo("ERROR"))
			.body("\"Get user\".operationId", not(empty()))
			.body("\"Get user\".errorDescription", containsString("No such user"));
	}
	
	/**
	 * Test "Adjust" operation - positive case
	 */
	@Test
	public void adjustPositive() {
		final int amount = 10000;
		
		// Add user
		Response response = given().log().all().contentType(ContentType.JSON)
			.body(addUserBody())
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Add user\".status", equalTo("OK"))
			.body("\"Add user\".operationId", not(empty()))
			.body("\"Add user\".userId", not(empty()))
			.extract().response();
		
		// Put funds on user's balance
		given().log().all().contentType(ContentType.JSON)
		.body(adjustBody(response.path("\"Add user\".accountId"), amount))
		.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
		.body("\"Adjust\".status", equalTo("OK"))
		.body("\"Adjust\".operationId", not(empty()));
	}
	
	/**
	 * Test "Adjust" operation - negative case: attempt to fall the user into the overdraft
	 */
	@Test
	public void adjustNegativeOverdraft() {
		final int amount = 10000;
		
		// Add user
		Response response = given().log().all().contentType(ContentType.JSON)
			.body(addUserBody())
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Add user\".status", equalTo("OK"))
			.body("\"Add user\".operationId", not(empty()))
			.body("\"Add user\".userId", not(empty()))
			.extract().response();
		
		// Take unexisting funds
		given().log().all().contentType(ContentType.JSON)
		.body(adjustBody(response.path("\"Add user\".accountId"), -amount))
		.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
		.body("\"Adjust\".status", equalTo("ERROR"))
		.body("\"Adjust\".operationId", not(empty()))
		.body("\"Adjust\".errorDescription", containsString("Not enough money"));
	}
	
	/**
	 * Test "Adjust" operation - negative case: invalid accountId
	 */
	@Test
	public void adjustNegativeInvalidAccountId() {
		final int amount = 10000;
		// Adjust account with invalid ID
		given().log().all().contentType(ContentType.JSON)
		.body(adjustBody("Invalid account ID", amount))
		.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
		.body("\"Adjust\".status", equalTo("ERROR"))
		.body("\"Adjust\".operationId", not(empty()))
		.body("\"Adjust\".errorDescription", containsString("No such account"));
	}
	
	/**
	 * Test "transfer" operation - positive case
	 */
	@Test
	public void transferPositive() {
		final int amount = 10000;
		
		// Add user 1
		Response sourceResponse = given().log().all().contentType(ContentType.JSON)
				.body(addUserBody())
				.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
				.body("\"Add user\".status", equalTo("OK"))
				.body("\"Add user\".operationId", not(empty()))
				.body("\"Add user\".userId", not(empty()))
				.extract().response();
			
		// Put initial amount to account 1
		given().log().all().contentType(ContentType.JSON)
			.body(adjustBody(sourceResponse.path("\"Add user\".accountId"), amount))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Adjust\".status", equalTo("OK"))
			.body("\"Adjust\".operationId", not(empty()));
		
		// Add user 2, his account should have zero balance
		Response targetResponse = given().log().all().contentType(ContentType.JSON)
				.body(addUserBody())
				.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
				.body("\"Add user\".status", equalTo("OK"))
				.body("\"Add user\".operationId", not(empty()))
				.body("\"Add user\".userId", not(empty()))
				.extract().response();
		
		// Doing transfer for (amount - fee)
		given().log().all().contentType(ContentType.JSON)
			.body(transferBody(
							sourceResponse.path("\"Add user\".accountId"), 
							targetResponse.path("\"Add user\".accountId"),
							amount - (int)ApplyFeeActivity.fee))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Transfer\".status", equalTo("OK"))
			.body("\"Transfer\".operationId", not(empty()));
		
		// Checking source amount, must be 0
		given().log().all().contentType(ContentType.JSON)
			.body(getAccountBody(sourceResponse.path("\"Add user\".accountId")))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get account\".status", equalTo("OK"))
			.body("\"Get account\".operationId", not(empty()))
			.body("\"Get account\".account.balance", equalTo(0));
		
		// Checking target amount, must be (amount - fee)
		given().log().all().contentType(ContentType.JSON)
			.body(getAccountBody(targetResponse.path("\"Add user\".accountId")))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get account\".status", equalTo("OK"))
			.body("\"Get account\".operationId", not(empty()))
			.body("\"Get account\".account.balance", equalTo(amount - (int)ApplyFeeActivity.fee));
	}
	
	/**
	 * Test "transfer" operation - negative case: invalid source and target account IDs
	 */
	@Test
	public void transferNegativeInvalidAccounts() {
		final int amount = 10000;
		
		// Doing transfer for (amount - fee)
		given().log().all().contentType(ContentType.JSON)
			.body(transferBody(
							"Invalid source account ID", 
							"Invalid target account ID",
							amount))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Transfer\".status", equalTo("ERROR"))
			.body("\"Transfer\".operationId", not(empty()))
			.body("\"Transfer\".errorDescription", containsString("No such account"));
	}

	/**
	 * Test "transfer" operation - negative case: not enough funds on the source account
	 */
	@Test
	public void transferNegativeNoFunds() {
		final int amount = 10000;
		
		// Add user 1
		Response sourceResponse = given().log().all().contentType(ContentType.JSON)
				.body(addUserBody())
				.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
				.body("\"Add user\".status", equalTo("OK"))
				.body("\"Add user\".operationId", not(empty()))
				.body("\"Add user\".userId", not(empty()))
				.extract().response();
			
		// Put initial amount to account 1
		given().log().all().contentType(ContentType.JSON)
			.body(adjustBody(sourceResponse.path("\"Add user\".accountId"), amount))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Adjust\".status", equalTo("OK"))
			.body("\"Adjust\".operationId", not(empty()));
				
		// Add user 2, his account should have zero balance
		Response targetResponse = given().log().all().contentType(ContentType.JSON)
				.body(addUserBody())
				.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
				.body("\"Add user\".status", equalTo("OK"))
				.body("\"Add user\".operationId", not(empty()))
				.body("\"Add user\".userId", not(empty()))
				.extract().response();
		
		// Doing transfer for (amount x2)
		given().log().all().contentType(ContentType.JSON)
			.body(transferBody(
							sourceResponse.path("\"Add user\".accountId"), 
							targetResponse.path("\"Add user\".accountId"),
							amount * 2))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Transfer\".status", equalTo("ERROR"))
			.body("\"Transfer\".operationId", not(empty()))
			.body("\"Transfer\".errorDescription", containsString("Not enough money"));
		
		// Checking source amount, must be unchanged
		given().log().all().contentType(ContentType.JSON)
			.body(getAccountBody(sourceResponse.path("\"Add user\".accountId")))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get account\".status", equalTo("OK"))
			.body("\"Get account\".operationId", not(empty()))
			.body("\"Get account\".account.balance", equalTo(amount));
		
		// Checking target amount, must be zero
		given().log().all().contentType(ContentType.JSON)
			.body(getAccountBody(targetResponse.path("\"Add user\".accountId")))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get account\".status", equalTo("OK"))
			.body("\"Get account\".operationId", not(empty()))
			.body("\"Get account\".account.balance", equalTo(0));
	}
	
	/**
	 * Test "Get account" operation - positive case
	 */
	@Test
	public void getAccountPositive() {
		Response response = given().log().all().contentType(ContentType.JSON)
			.body(addUserBody())
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Add user\".status", equalTo("OK"))
			.body("\"Add user\".operationId", not(empty()))
			.body("\"Add user\".userId", not(empty()))
			.extract().response();
		
		given().log().all().contentType(ContentType.JSON)
			.body(getAccountBody(response.path("\"Add user\".accountId")))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get account\".status", equalTo("OK"))
			.body("\"Get account\".operationId", not(empty()));
	}
	
	/**
	 * Test "Get account" operation - negative case: invalid account ID
	 */
	@Test
	public void getAccountNegative() {
		// Get account with invalid ID
		given().log().all().contentType(ContentType.JSON)
			.body(getAccountBody("Invalid account ID"))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get account\".status", equalTo("ERROR"))
			.body("\"Get account\".operationId", not(empty()))
			.body("\"Get account\".errorDescription", containsString("No such account"));
	}
	
	/**
	 * Test "Get operation" operation - positive case
	 */
	@Test
	public void getOperationPositive() {
		// Create new "add user" operation 
		Response response = given().log().all().contentType(ContentType.JSON)
			.body(addUserBody())
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Add user\".status", equalTo("OK"))
			.body("\"Add user\".operationId", not(empty()))
			.body("\"Add user\".userId", not(empty()))
			.extract().response();
		
		// Get the operation
		given().log().all().contentType(ContentType.JSON)
			.body(getOperationBody(response.path("\"Add user\".operationId")))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get operation\".status", equalTo("OK"))
			.body("\"Get operation\".operationId", not(empty()))
			.body("\"Get operation\".operation", not(empty()));
	}
	
	/**
	 * Test "Get operation" operation - negative case: invalid operation ID
	 */
	@Test
	public void getOperationNegativeInvalidOperationId() {
		// Get unexisting operation 
		given().log().all().contentType(ContentType.JSON)
			.body(getOperationBody("Invalid operation ID"))
			.post().then().log().all().statusCode(200).contentType(ContentType.JSON)
			.body("\"Get operation\".status", equalTo("ERROR"))
			.body("\"Get operation\".operationId", not(empty()))
			.body("\"Get operation\".errorDescription", containsString("No such operation"));
	}
}
