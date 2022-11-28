package com.jalizadeh.TestBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TestBuddyApplicationTests {

	
	private final String baseUrl = "http://localhost";
	
	@LocalServerPort
	private int port;
	
	@Autowired
	TestRestTemplate restTemplate;
	
	
	@Test
	@DisplayName("Application is started correctly on random port")
	public void applicationIsRunning() {
		assertEquals(this.baseUrl + ":" + port, getUrl());
	}
	
	@Test
	@DisplayName("Test endpoint \"Home [GET]\" is fine")
	public void testEndpointHome() {
		ResponseEntity<String> response = restTemplate.exchange(getUrl(), HttpMethod.GET, getSimpleEntity(), String.class);
		assertEquals("OK from home", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	
	@Test
	@DisplayName("Test endpoint \"Simple [GET]\" is fine")
	public void testEndpointSimple() {
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl()+"/simple", String.class);
		assertEquals("OK", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	
	
	//Only age=40 is tested, while the endpoint checks different values
	@DisplayName("Test endpoint \"Age [POST]\" is fine")
	@ParameterizedTest(name = "id: {0} -> response: {1}")
	@CsvSource({ "-1, Age is not valid", "20, age is 20-30", "35, age is 30-40", "45, 40-50 + custom header", "10, 10" })
	public void testEndpointAge(int id, String resp) {
		ResponseEntity<String> response = restTemplate.postForEntity(getUrl()+"/age?age=" + id, getSimpleEntity(), String.class);
		assertEquals(resp, response.getBody());
		//assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	
	@Test
	@DisplayName("Test endpoint \"rawBody [POST]\" is fine")
	public void testEndpointRawBody() {
		//negative case
		ResponseEntity<String> response = restTemplate.exchange(getUrl()+"/rawBody", HttpMethod.POST, getSimpleEntity(), String.class);
		assertEquals("Invalid body", response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		
		//positive case
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
		String body = "username=user@name.com&password=123456&grant_type=password";
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		response = restTemplate.exchange(getUrl()+"/rawBody", HttpMethod.POST, entity, String.class);
		assertEquals("OK", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		//negative - invalid username
		body = "username=INVALID@mail.com&password=123456&grant_type=password";
		entity = new HttpEntity<>(body, headers);
		response = restTemplate.exchange(getUrl()+"/rawBody", HttpMethod.POST, entity, String.class);
		assertEquals("Username or password is invalid", response.getBody());
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	
	
	@Test
	@DisplayName("Test endpoint \"Profile id\" is fine")
	public void testEndpointProfileId() {
		int id = 40;
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl()+"/protected/profile?id=" + id, String.class);
		assertEquals("Your id is: " + id, response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	
	private String getUrl() {
		return this.baseUrl + ":" + port;
	}
	
	private HttpEntity<String> getSimpleEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<String>("", headers);
	}
}
