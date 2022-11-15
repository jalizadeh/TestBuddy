package com.jalizadeh.TestBuddy.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEndpoint {
	
	@GetMapping("/")
	public ResponseEntity<String> rootApi() {
		return new ResponseEntity<>("OK from home",HttpStatus.OK);
	}

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
		
		return new ResponseEntity<String>("response with custome header",httpHeaders, HttpStatus.OK);
	}
	
	
	
	@PostMapping("/rawBody")
	public ResponseEntity<String> rawBody(HttpEntity<String> httpEntity) {
		String body = httpEntity.getBody();
		
		if( body == null || body.isEmpty() || !body.contains("&") || !body.contains("=")) {
			return new ResponseEntity<String>("Invalid body", HttpStatus.BAD_REQUEST);
		}
		
		//by default it is bad request
		ResponseEntity<String> response = new ResponseEntity<String>("Unknown error", HttpStatus.BAD_REQUEST);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		Stream.of(body.split("&"))
			.forEach(param -> {
				String[] pSplit = param.split("=");
				paramMap.put(pSplit[0], pSplit.length == 2 ? pSplit[1] : "");
			});
		
		/*
		for (Entry<String, String> p : paramMap.entrySet()) {
			System.out.println(p.getKey() + "\t" + p.getValue().toString());
		}
		*/
		
		if(paramMap.size() != 3 
				|| !paramMap.containsKey("username") 
				|| !paramMap.containsKey("password")
				|| !paramMap.containsKey("grant_type")) {
			return new ResponseEntity<String>("Missing parameter",HttpStatus.BAD_REQUEST);
		}
		
		if(paramMap.get("username").equals("user@name.com") && paramMap.get("password").equals("123456")) {
			response = new ResponseEntity<String>("OK", HttpStatus.OK);
		} else {
			response = new ResponseEntity<String>("Username or password is invalid",HttpStatus.UNAUTHORIZED);
		}
		
		if(!paramMap.get("grant_type").equals("password")) {
			response = new ResponseEntity<String>("Invalid grant_type", HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	
	@PostMapping( value = "/xform", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> parseXForm(@RequestParam Map<String, String> paramMap) throws Exception {
		
		if( paramMap == null || paramMap.isEmpty() ) {
			return new ResponseEntity<String>("Invalid body", HttpStatus.BAD_REQUEST);
		}
		
		//by default it is bad request
		ResponseEntity<String> response = new ResponseEntity<String>("Unknown error", HttpStatus.BAD_REQUEST);
		
		/*
		for (Entry<String, String> p : paramMap.entrySet()) {
			System.out.println(p.getKey() + "\t" + p.getValue().toString());
		}
		*/
		
		if(paramMap.size() != 3 
				|| !paramMap.containsKey("username") 
				|| !paramMap.containsKey("password")
				|| !paramMap.containsKey("grant_type")) {
			return new ResponseEntity<String>("Missing parameter",HttpStatus.BAD_REQUEST);
		}
		
		if(URLDecoder.decode(paramMap.get("username")).equals("user@name.com") && URLDecoder.decode(paramMap.get("password")).equals("123456")) {
			response = new ResponseEntity<String>("OK", HttpStatus.OK);
		} else {
			response = new ResponseEntity<String>("Username or password is invalid",HttpStatus.UNAUTHORIZED);
		}
		
		if(!paramMap.get("grant_type").equals("password")) {
			response = new ResponseEntity<String>("Invalid grant_type", HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	
	
	@GetMapping("/protected/profile")
	public String protectedProfile(@RequestParam("id") String id) {
		return "Your id is: " + id;
	}
	
	
	@PutMapping("/update")
	public ResponseEntity<String> update(){
		return new ResponseEntity<String>("Profile updated", HttpStatus.OK);
	}
	
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(){
		return new ResponseEntity<String>("Profile deleted", HttpStatus.OK);
	}
}
