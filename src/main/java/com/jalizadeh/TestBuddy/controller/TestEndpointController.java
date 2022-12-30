package com.jalizadeh.testbuddy.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEndpointController {
	
	private static final String GRANT_TYPE = "grant_type";
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	
	@Autowired
	private Environment env;

	
	@GetMapping("/simple")
	public ResponseEntity<String> simpleApi() {
		return new ResponseEntity<>("OK",HttpStatus.OK);
	}
	
	
	@PostMapping("/age")
	public ResponseEntity<String> age(@RequestParam("age") int age){
		if(age < 0 || age > 100) {
			return new ResponseEntity<>("Age is not valid", HttpStatus.BAD_REQUEST);
		}
		
		if(age >= 20 && age < 30) {
			return ResponseEntity.badRequest().body("age is 20-30");
		}
		
		if(age >= 30 && age < 40) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("age is 30-40");
		}
		
		if(age >= 40 && age < 50) {
			return ResponseEntity.ok()
					.header("Custom-header", "40-50")
					.body("40-50 + custom header");
		}
		
		return new ResponseEntity<>(String.valueOf(age), HttpStatus.OK);
	}
	
	
	@GetMapping("/customHeader")
	public ResponseEntity<String> customHeader(){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Custom-Header", "foo");
		httpHeaders.add("x-transaction-log", UUID.randomUUID().toString());
		
		return new ResponseEntity<>("response with custome header",httpHeaders, HttpStatus.OK);
	}
	
	
	
	@PostMapping("/rawBody")
	public ResponseEntity<String> rawBody(HttpEntity<String> httpEntity) {
		String body = httpEntity.getBody();
		
		if( body == null || body.isEmpty() || !body.contains("&") || !body.contains("=")) {
			return new ResponseEntity<>("Invalid body", HttpStatus.BAD_REQUEST);
		}
		
		//by default it is bad request
		ResponseEntity<String> response = new ResponseEntity<>("Unknown error", HttpStatus.BAD_REQUEST);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		Stream.of(body.split("&"))
			.forEach(param -> {
				String[] pSplit = param.split("=");
				paramMap.put(pSplit[0], pSplit.length == 2 ? pSplit[1] : "");
			});
		
		if(paramMap.size() != 3 
				|| !paramMap.containsKey(USERNAME) 
				|| !paramMap.containsKey(PASSWORD)
				|| !paramMap.containsKey(GRANT_TYPE)) {
			return new ResponseEntity<>("Missing parameter",HttpStatus.BAD_REQUEST);
		}
		
		if(!paramMap.get(GRANT_TYPE).equals(PASSWORD)) {
			response = new ResponseEntity<>("Invalid grant_type", HttpStatus.BAD_REQUEST);
		}
		
		if(paramMap.get(USERNAME).equals("user@name.com") && paramMap.get(PASSWORD).equals("123456")) {
			response = new ResponseEntity<>("OK", HttpStatus.OK);
		} else {
			response = new ResponseEntity<>("Username or password is invalid",HttpStatus.UNAUTHORIZED);
		}
		
		
		return response;
	}
	
	
	@PostMapping( value = "/xform", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> parseXForm(@RequestParam Map<String, String> paramMap) {
		
		if( paramMap == null || paramMap.isEmpty() ) {
			return new ResponseEntity<>("Invalid body", HttpStatus.BAD_REQUEST);
		}
		
		if(paramMap.size() != 3 
				|| !paramMap.containsKey(USERNAME) 
				|| !paramMap.containsKey(PASSWORD)
				|| !paramMap.containsKey(GRANT_TYPE)) {
			return new ResponseEntity<>("Missing parameter",HttpStatus.BAD_REQUEST);
		}
		
		//by default it is bad request
		ResponseEntity<String> response = new ResponseEntity<>("Unknown error", HttpStatus.BAD_REQUEST);
		
		if(URLDecoder.decode(paramMap.get(USERNAME)).equals("user@name.com") && URLDecoder.decode(paramMap.get(PASSWORD)).equals("123456")) {
			response = new ResponseEntity<>("OK", HttpStatus.OK);
		} else {
			response = new ResponseEntity<>("Username or password is invalid",HttpStatus.UNAUTHORIZED);
		}
		
		if(!paramMap.get(GRANT_TYPE).equals(PASSWORD)) {
			response = new ResponseEntity<>("Invalid grant_type", HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	//learn more about all header processing types
	//https://www.baeldung.com/spring-rest-http-headers
	@GetMapping("/authorized/basic")
	public ResponseEntity<String> basicAuthorizedEndpoint(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String auth) {
		String[] values = new String[2];
		boolean wasOK = false;
		if (auth.length() > 0 && auth.toLowerCase().startsWith("basic")) {
		    String base64Credentials = auth.substring("Basic".length()).trim();
		    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
		    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
		    values = credentials.split(":", 2);
		    wasOK = true;
		}
		
		if(wasOK && values[0].equals(env.getProperty("basicAuth.username")) 
				&&  values[1].equals(env.getProperty("basicAuth.password"))) {
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
	}
	
	@GetMapping("/protected/profile")
	public ResponseEntity<String> protectedProfile(@RequestParam("id") String id) {
		return new ResponseEntity<>("Your id is: " + id, HttpStatus.OK);
	}
	
	
	@PutMapping("/update")
	public ResponseEntity<String> update(){
		return new ResponseEntity<>("Profile updated", HttpStatus.OK);
	}
	
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(){
		return new ResponseEntity<>("Profile deleted", HttpStatus.OK);
	}
}
