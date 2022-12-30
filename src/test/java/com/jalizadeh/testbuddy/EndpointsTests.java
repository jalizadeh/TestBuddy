package com.jalizadeh.testbuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalizadeh.testbuddy.model.InputRequest;
import com.jalizadeh.testbuddy.types.Filters;

@DisplayName("Check test endpoints")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class EndpointsTests {

	private final String baseUrl = "http://localhost";
    private static final Logger logger = LogManager.getLogger(EndpointsTests.class);
    private final ObjectMapper objectMapper = new ObjectMapper();


	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	Environment env;
	
	@BeforeAll
	public static void setErrorLogging() {
	   LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(java.lang.System.Logger.Level.DEBUG.getName(), LogLevel.DEBUG);
	}
	
	
	@Test
	@Order(1)
	@DisplayName("Application is started correctly on random port")
	void applicationIsRunning() {
		assertEquals(this.baseUrl + ":" + port, getUrl());
	}

	@Disabled("it is no more an API, but a webpage")
	@Test
	@Order(2)
	@DisplayName("Test endpoint \"Home [GET]\" is fine")
	void testEndpointHome() {
		ResponseEntity<String> response = restTemplate.exchange(getUrl(), HttpMethod.GET, getSimpleEntity(),
				String.class);
		assertEquals("OK from home", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	@Order(3)
	@DisplayName("Test endpoint \"Simple [GET]\" is fine")
	void testEndpointSimple() {
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl() + "/simple", String.class);
		assertEquals("OK", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	// Only age=40 is tested, while the endpoint checks different values
	@DisplayName("Test endpoint \"Age [POST]\" is fine")
	@Order(4)
	@ParameterizedTest(name = "id: {0} -> response: {1}")
	@CsvSource({ "-1, Age is not valid", "20, age is 20-30", "35, age is 30-40", "45, 40-50 + custom header",
			"10, 10" })
	void testEndpointAge(int id, String resp) {
		ResponseEntity<String> response = restTemplate.postForEntity(getUrl() + "/age?age=" + id, getSimpleEntity(),
				String.class);
		assertEquals(resp, response.getBody());
		// assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	@Order(5)
	@DisplayName("Test endpoint \"rawBody [POST]\" is fine")
	void testEndpointRawBody() {
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
	@Order(6)
	@DisplayName("Test endpoint \"Profile id\" is fine")
	void testEndpointProfileId() {
		int id = 40;
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl() + "/protected/profile?id=" + id,
				String.class);
		assertEquals("Your id is: " + id, response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	
	@DisplayName("Test endpoint \"Authorized Basic\" is fine")
	@Order(7)
	@ParameterizedTest(name = "{0}:{1} -> status: {3}")
	@CsvSource({"username, password, ,200", "invalid_user, invalid_pass, ,403"})
	void testEndpointAuthorized(String user, String pass, String msg, String status) {
		ResponseEntity<String> response = restTemplate.withBasicAuth(user,pass)
			.getForEntity(getUrl()+"/authorized/basic", String.class);
		assertEquals(msg, response.getBody());
		assertEquals(status, response.getStatusCodeValue()+"");
	}
	
	
	@Test
	@Order(8)
	@DisplayName("Test endpoint \"Update Profile\" is fine")
	void testEndpointUpdate() {
		ResponseEntity<String> response = restTemplate.exchange(getUrl()+"/update", HttpMethod.PUT, getSimpleEntity(), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Profile updated", response.getBody());
	}
	
	@Test
	@Order(9)
	@DisplayName("Test endpoint \"Update Profile\" is fine")
	void testEndpointDelete() {
		ResponseEntity<String> response = restTemplate.exchange(getUrl()+"/delete", HttpMethod.DELETE, getSimpleEntity(), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Profile deleted", response.getBody());
	}
	
	
	@Disabled("This test needs the server to be up and running, disabled for now")
	@Test
	@Order(10)
	@DisplayName("Endpoint \"/json\" is fine")
	void testJsonInput() throws JsonMappingException, JsonProcessingException {
		InputRequest reqBody = new InputRequest();
		
		//empty list of filters
		//reqBody.setFilters(new ArrayList<>());
		
		//4 unique filters (duplicates are ignored)
		reqBody.setFilters(Arrays.asList(Filters.EMPTY, Filters.EMPTY, Filters.INVALID, Filters.MISSING, Filters.RANDOM));
		
		ResponseEntity<String> response = 
				restTemplate.exchange(getUrl()+"/json?delay=0", HttpMethod.POST, getFullEntity(reqBody), String.class);
		JsonNode root = objectMapper.readTree(response.getBody());

		assertNotNull(response);
		assertNotNull(root);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertTrue(root.path("totalTimeMs").asInt() < 1000, "Parsing JSON took more than 1 second");
	    assertEquals(8, root.path("totalRequests").asInt());
	    assertEquals(8, root.path("requests").size());
	    assertEquals(72, root.path("totalCalls").asInt());
	    root.path("requests")
	    	.forEach(r -> assertTrue(r.path("url").asText().contains(baseUrl),"url wrong"));
	    assertEquals(root.path("totalCalls").asInt(), StreamSupport.stream(root.path("requests").spliterator(), false)
	    	.map(r -> {return r.path("positive").asInt() + r.path("negative").asInt();})
	    	.mapToInt(x -> x).sum());
	}
	

	private String getUrl() {
		return this.baseUrl + ":" + port;
	}

	private HttpEntity<String> getSimpleEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<String>(headers);
	}
	
	private <T> HttpEntity<T> getFullEntity(T body){
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<T>(body, headers);
	}
	
}
