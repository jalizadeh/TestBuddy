package com.jalizadeh.TestBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "logging-test" })
class TestBuddyApplicationTests {

	private final String baseUrl = "http://localhost";
    private static final Logger LOGGER = LogManager.getLogger(TestBuddyApplicationTests.class);


	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate restTemplate;
	
	@BeforeAll
	public static void setErrorLogging() {
	   LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(java.lang.System.Logger.Level.DEBUG.getName(), LogLevel.DEBUG);
	}
	
	
	@Test
	@DisplayName("Application is started correctly on random port")
	public void applicationIsRunning() {
		assertEquals(this.baseUrl + ":" + port, getUrl());
	}

	@Test
	@DisplayName("Test endpoint \"Home [GET]\" is fine")
	public void testEndpointHome() {
		ResponseEntity<String> response = restTemplate.exchange(getUrl(), HttpMethod.GET, getSimpleEntity(),
				String.class);
		assertEquals("OK from home", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	@DisplayName("Test endpoint \"Simple [GET]\" is fine")
	public void testEndpointSimple() {
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl() + "/simple", String.class);
		assertEquals("OK", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	// Only age=40 is tested, while the endpoint checks different values
	@DisplayName("Test endpoint \"Age [POST]\" is fine")
	@ParameterizedTest(name = "id: {0} -> response: {1}")
	@CsvSource({ "-1, Age is not valid", "20, age is 20-30", "35, age is 30-40", "45, 40-50 + custom header",
			"10, 10" })
	public void testEndpointAge(int id, String resp) {
		ResponseEntity<String> response = restTemplate.postForEntity(getUrl() + "/age?age=" + id, getSimpleEntity(),
				String.class);
		assertEquals(resp, response.getBody());
		// assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	@DisplayName("Test endpoint \"rawBody [POST]\" is fine")
	public void testEndpointRawBody() {
		// negative case
		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/rawBody", HttpMethod.POST,
				getSimpleEntity(), String.class);
		assertEquals("Invalid body", response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		// positive case
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
		String body = "username=user@name.com&password=123456&grant_type=password";
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		response = restTemplate.exchange(getUrl() + "/rawBody", HttpMethod.POST, entity, String.class);
		assertEquals("OK", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// negative - invalid username
		body = "username=INVALID@mail.com&password=123456&grant_type=password";
		entity = new HttpEntity<>(body, headers);
		response = restTemplate.exchange(getUrl() + "/rawBody", HttpMethod.POST, entity, String.class);
		assertEquals("Username or password is invalid", response.getBody());
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	@DisplayName("Test endpoint \"Profile id\" is fine")
	public void testEndpointProfileId() {
		int id = 40;
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl() + "/protected/profile?id=" + id,
				String.class);
		assertEquals("Your id is: " + id, response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	
	@DisplayName("Test endpoint \"Authorized Basic\" is fine")
	@ParameterizedTest(name = "{0}:{1} -> status: {3}")
	@CsvSource({"username, password, ,200", "invalid_user, invalid_pass, ,403"})
	public void testEndpointAuthorized(String user, String pass, String msg, String status) {
		ResponseEntity<String> response = restTemplate.withBasicAuth(user,pass)
			.getForEntity(getUrl()+"/authorized/basic", String.class);
		assertEquals(msg, response.getBody());
		assertEquals(status, response.getStatusCodeValue()+"");
	}
	
	
	@Test
	@DisplayName("Test endpoint \"Update Profile\" is fine")
	public void testEndpointUpdate() {
		ResponseEntity<String> response = restTemplate.exchange(getUrl()+"/update", HttpMethod.PUT, getSimpleEntity(), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Profile updated", response.getBody());
	}
	
	@Test
	@DisplayName("Test endpoint \"Update Profile\" is fine")
	public void testEndpointDelete() {
		ResponseEntity<String> response = restTemplate.exchange(getUrl()+"/delete", HttpMethod.DELETE, getSimpleEntity(), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Profile deleted", response.getBody());
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
