package com.jalizadeh.testbuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import com.jalizadeh.testbuddy.types.FilterType;
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
import org.junit.jupiter.params.provider.EnumSource;
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
import com.jalizadeh.testbuddy.central.FiltersManager;
import com.jalizadeh.testbuddy.model.InputRequest;

@DisplayName("Test /json endpoints")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class JSONEndpointTests {

	private final String baseUrl = "http://localhost";
    private static final Logger logger = LogManager.getLogger(JSONEndpointTests.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private InputRequest requestBody = new InputRequest();

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
	
	
	@Disabled("FiltersManager is a part of the service, not test application")
	@ParameterizedTest
	@EnumSource(FilterType.class)
	@Order(1)
	@DisplayName("FilterManager has correct number of filters")
	void testJSONEndpoint_givenFilterList_createsCorrectlyTheFilterManager(FilterType filter) {
		//empty list of filters => only positive cases
		requestBody.setFilters(Arrays.asList(filter));
		restTemplate.postForEntity(getUrl(), getEntity(requestBody), String.class);
		assertEquals(1, FiltersManager.getInstance().getFilters().size());
	}
	
	
	
	@Disabled("This test needs the server to be up and running, disabled for now")
	@Test
	@Order(2)
	@DisplayName("Endpoint \"/json\" is fine")
	void testJsonInput() throws JsonMappingException, JsonProcessingException {
		//empty list of filters
		//reqBody.setFilters(new ArrayList<>());
		
		//4 unique filters (duplicates are ignored)
		requestBody.setFilters(Arrays.asList(FilterType.EMPTY, FilterType.EMPTY, FilterType.INVALID, FilterType.MISSING, FilterType.RANDOM));
		
		ResponseEntity<String> r = restTemplate.exchange(getUrl(), HttpMethod.POST, 
						getEntity(requestBody), String.class);
		JsonNode root = objectMapper.readTree(r.getBody());

		assertNotNull(r);
		assertNotNull(root);
		assertEquals(HttpStatus.OK, r.getStatusCode());
	    //assertTrue(root.path("totalTimeMs").asInt() < 1000, "Parsing JSON took more than 1 second");
	    assertEquals(8, root.path("totalRequests").asInt());
	    assertEquals(8, root.path("requests").size());
	    assertEquals(72, root.path("totalCalls").asInt());
	    root.path("requests")
	    	.forEach(x -> assertTrue(x.path("url").asText().contains(baseUrl),"url wrong"));
	    assertEquals(root.path("totalCalls").asInt(), 
	    		StreamSupport.stream(root.path("requests").spliterator(), false)
	    			.map(x -> { return x.path("positive").asInt() + x.path("negative").asInt(); })
	    			.mapToInt(x -> x).sum());
	}
	
	
	private String getUrl() {
		return this.baseUrl + ":" + "8080" + "/json?delay=0";
	}

	private <T> HttpEntity<T> getEntity(T body){
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return (body != null ? new HttpEntity<T>(body, headers) : new HttpEntity<>(headers));
	}	
}
