package com.jalizadeh.testbuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

@DisplayName("Check test endpoints")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class JSONEndpointTests {

	private final String baseUrl = "http://localhost";
    private static final Logger logger = LogManager.getLogger(JSONEndpointTests.class);
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
	
	//input filters match the system filters

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
