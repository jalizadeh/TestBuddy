package com.jalizadeh.TestBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalizadeh.TestBuddy.runner.PostmanCollectionRunner;
import com.jalizadeh.TestBuddy.runner.PostmanRunResult;
import com.jalizadeh.TestBuddy.service.RestService;

@RestController
public class Handler {

	@Autowired
	private RestService restService;
	
	
	
	
	@GetMapping("/parse")
	public StringBuffer parse() {
		restService = new RestService(new RestTemplateBuilder());
		return restService.sendRequest();
	}
	
	
	@GetMapping("/json")
	public String parseJson() {
		 
	    String json = "{\r\n" + 
	    		"	\"info\": {\r\n" + 
	    		"		\"_postman_id\": \"ebcb7357-60d8-4923-8cbf-1704bd65ddac\",\r\n" + 
	    		"		\"name\": \"TestBuddy 2\",\r\n" + 
	    		"		\"schema\": \"https://schema.getpostman.com/json/collection/v2.1.0/collection.json\"\r\n" + 
	    		"	},\r\n" + 
	    		"	\"item\": [\r\n" + 
	    		"		{\r\n" + 
	    		"			\"name\": \"Simple 1\",\r\n" + 
	    		"			\"event\": [\r\n" + 
	    		"				{\r\n" + 
	    		"					\"listen\": \"test\",\r\n" + 
	    		"					\"script\": {\r\n" + 
	    		"						\"exec\": [\r\n" + 
	    		"							\"var jsonData = JSON.parse(responseBody);\",\r\n" + 
	    		"							\"postman.setEnvironmentVariable(\\\"access_token\\\", jsonData.access_token);\",\r\n" + 
	    		"							\"postman.setEnvironmentVariable(\\\"refresh_token\\\", jsonData.refresh_token);\",\r\n" + 
	    		"							\"\",\r\n" + 
	    		"							\"\"\r\n" + 
	    		"						],\r\n" + 
	    		"						\"type\": \"text/javascript\"\r\n" + 
	    		"					}\r\n" + 
	    		"				}\r\n" + 
	    		"			],\r\n" + 
	    		"			\"request\": {\r\n" + 
	    		"				\"auth\": {\r\n" + 
	    		"					\"type\": \"noauth\"\r\n" + 
	    		"				},\r\n" + 
	    		"				\"method\": \"POST\",\r\n" + 
	    		"				\"header\": [],\r\n" + 
	    		"				\"body\": {\r\n" + 
	    		"					\"mode\": \"raw\",\r\n" + 
	    		"					\"raw\": \"username=mn_1631547449@byom.de&password=secret123&grant_type=password\"\r\n" + 
	    		"				},\r\n" + 
	    		"				\"url\": {\r\n" + 
	    		"					\"raw\": \"https://customer-i.bmwgroup.com/gcdm/oauth/token\",\r\n" + 
	    		"					\"protocol\": \"https\",\r\n" + 
	    		"					\"host\": [\r\n" + 
	    		"						\"customer-i\",\r\n" + 
	    		"						\"bmwgroup\",\r\n" + 
	    		"						\"com\"\r\n" + 
	    		"					],\r\n" + 
	    		"					\"path\": [\r\n" + 
	    		"						\"gcdm\",\r\n" + 
	    		"						\"oauth\",\r\n" + 
	    		"						\"token\"\r\n" + 
	    		"					]\r\n" + 
	    		"				}\r\n" + 
	    		"			},\r\n" + 
	    		"			\"response\": [\r\n" + 
	    		"				{\r\n" + 
	    		"					\"name\": \"400 - Empty body\",\r\n" + 
	    		"					\"originalRequest\": {\r\n" + 
	    		"						\"method\": \"POST\",\r\n" + 
	    		"						\"header\": [],\r\n" + 
	    		"						\"url\": {\r\n" + 
	    		"							\"raw\": \"https://customer-i.bmwgroup.com/gcdm/oauth/token\",\r\n" + 
	    		"							\"protocol\": \"https\",\r\n" + 
	    		"							\"host\": [\r\n" + 
	    		"								\"customer-i\",\r\n" + 
	    		"								\"bmwgroup\",\r\n" + 
	    		"								\"com\"\r\n" + 
	    		"							],\r\n" + 
	    		"							\"path\": [\r\n" + 
	    		"								\"gcdm\",\r\n" + 
	    		"								\"oauth\",\r\n" + 
	    		"								\"token\"\r\n" + 
	    		"							]\r\n" + 
	    		"						}\r\n" + 
	    		"					},\r\n" + 
	    		"					\"status\": \"Bad Request\",\r\n" + 
	    		"					\"code\": 400,\r\n" + 
	    		"					\"_postman_previewlanguage\": \"json\",\r\n" + 
	    		"					\"header\": [\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Date\",\r\n" + 
	    		"							\"value\": \"Sun, 18 Sep 2022 07:31:55 GMT\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Content-Type\",\r\n" + 
	    		"							\"value\": \"application/json\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Content-Length\",\r\n" + 
	    		"							\"value\": \"87\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Connection\",\r\n" + 
	    		"							\"value\": \"keep-alive\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Origin\",\r\n" + 
	    		"							\"value\": \"\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Headers\",\r\n" + 
	    		"							\"value\": \"Authorization, Origin, X-c2b-Authorization, X-c2b-mTAN, X-Requested-With, X-c2b-Sender-Id, X-c2b-External-Id, Content-Type, Accept, Cache-Control, KeyId, x-dtc\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Max-Age\",\r\n" + 
	    		"							\"value\": \"3628800\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Credentials\",\r\n" + 
	    		"							\"value\": \"true\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Methods\",\r\n" + 
	    		"							\"value\": \"POST, GET, OPTIONS, PUT, DELETE, HEAD\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Referrer-Policy\",\r\n" + 
	    		"							\"value\": \"strict-origin\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"x-c2b-request-id\",\r\n" + 
	    		"							\"value\": \"rrt-1374166732152246650-d-geu1-19272-335589859-3\"\r\n" + 
	    		"						}\r\n" + 
	    		"					],\r\n" + 
	    		"					\"cookie\": [],\r\n" + 
	    		"					\"body\": \"{\\n    \\\"error\\\": \\\"invalid_request\\\",\\n    \\\"error_description\\\": \\\"Parameter response_type is missing\\\"\\n}\"\r\n" + 
	    		"				},\r\n" + 
	    		"				{\r\n" + 
	    		"					\"name\": \"401 - Invalid Auth\",\r\n" + 
	    		"					\"originalRequest\": {\r\n" + 
	    		"						\"method\": \"POST\",\r\n" + 
	    		"						\"header\": [],\r\n" + 
	    		"						\"body\": {\r\n" + 
	    		"							\"mode\": \"raw\",\r\n" + 
	    		"							\"raw\": \"username=mn_1631547449@byom.de&password=secret123&grant_type=password\"\r\n" + 
	    		"						},\r\n" + 
	    		"						\"url\": {\r\n" + 
	    		"							\"raw\": \"https://customer-i.bmwgroup.com/gcdm/oauth/token\",\r\n" + 
	    		"							\"protocol\": \"https\",\r\n" + 
	    		"							\"host\": [\r\n" + 
	    		"								\"customer-i\",\r\n" + 
	    		"								\"bmwgroup\",\r\n" + 
	    		"								\"com\"\r\n" + 
	    		"							],\r\n" + 
	    		"							\"path\": [\r\n" + 
	    		"								\"gcdm\",\r\n" + 
	    		"								\"oauth\",\r\n" + 
	    		"								\"token\"\r\n" + 
	    		"							]\r\n" + 
	    		"						}\r\n" + 
	    		"					},\r\n" + 
	    		"					\"status\": \"Unauthorized\",\r\n" + 
	    		"					\"code\": 401,\r\n" + 
	    		"					\"_postman_previewlanguage\": \"json\",\r\n" + 
	    		"					\"header\": [\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Date\",\r\n" + 
	    		"							\"value\": \"Sun, 18 Sep 2022 07:32:15 GMT\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Content-Type\",\r\n" + 
	    		"							\"value\": \"application/json\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Transfer-Encoding\",\r\n" + 
	    		"							\"value\": \"chunked\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Connection\",\r\n" + 
	    		"							\"value\": \"keep-alive\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"X-CorrelationID\",\r\n" + 
	    		"							\"value\": \"Id-7fc92663b2d61758f3c3ac8e 0\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Accept\",\r\n" + 
	    		"							\"value\": \"*/*\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Authorization\",\r\n" + 
	    		"							\"value\": \"Basic cXFjZGkzOmo1dG1hanBj\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Host\",\r\n" + 
	    		"							\"value\": \"int-api-emea.bmwgroup.net\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Postman-Token\",\r\n" + 
	    		"							\"value\": \"c2465b18-00db-4177-af04-10158e7c08e8\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"traceparent\",\r\n" + 
	    		"							\"value\": \"00-5088c37986e92d3f298576dcdbf8d3ba-2fcf7f306c2bf45e-01\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"tracestate\",\r\n" + 
	    		"							\"value\": \"ec57fc0c-3d6bc570@dt=fw4;4;73a212fd;36ea;4;0;0;1e4;a4e8;2h01;3h73a212fd;4h36ea;5h01;7h2fcf7f306c2bf45e\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"X-dynaTrace\",\r\n" + 
	    		"							\"value\": \"FW4;1030473072;4;1940001533;14058;4;-329778164;484;f5bf;2h01;3h73a212fd;4h36ea;5h01;6h5088c37986e92d3f298576dcdbf8d3ba;7h2fcf7f306c2bf45e\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"X-dynaTrace-Application\",\r\n" + 
	    		"							\"value\": \"v=2;appId=ea7c4b59f27d43eb;cookieDomain=bmwgroup.com;rid=553468539;rpid=256069962;en=l00w9ezo\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"X-dynaTrace-RequestState\",\r\n" + 
	    		"							\"value\": \"agentId=0x03960a2c73a212fd&pathDepth=1\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"X-ruxit-Apache-ServerNamePorts\",\r\n" + 
	    		"							\"value\": \"customer-i.bmwgroup.com\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Origin\",\r\n" + 
	    		"							\"value\": \"\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Headers\",\r\n" + 
	    		"							\"value\": \"Authorization, Origin, X-c2b-Authorization, X-c2b-mTAN, X-Requested-With, X-c2b-Sender-Id, X-c2b-External-Id, Content-Type, Accept, Cache-Control, KeyId, x-dtc\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Max-Age\",\r\n" + 
	    		"							\"value\": \"3628800\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Credentials\",\r\n" + 
	    		"							\"value\": \"true\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Methods\",\r\n" + 
	    		"							\"value\": \"POST, GET, OPTIONS, PUT, DELETE, HEAD\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Referrer-Policy\",\r\n" + 
	    		"							\"value\": \"strict-origin\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"x-c2b-request-id\",\r\n" + 
	    		"							\"value\": \"rrt-1374166732152246650-d-geu1-19272-335589859-5\"\r\n" + 
	    		"						}\r\n" + 
	    		"					],\r\n" + 
	    		"					\"cookie\": [],\r\n" + 
	    		"					\"body\": \"{\\n    \\\"error\\\": \\\"invalid_client\\\",\\n    \\\"error_description\\\": \\\"Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).\\\"\\n}\"\r\n" + 
	    		"				},\r\n" + 
	    		"				{\r\n" + 
	    		"					\"name\": \"manual addition\",\r\n" + 
	    		"					\"originalRequest\": {\r\n" + 
	    		"						\"method\": \"POST\",\r\n" + 
	    		"						\"header\": [],\r\n" + 
	    		"						\"url\": {\r\n" + 
	    		"							\"raw\": \"https://customer-i.bmwgroup.com/gcdm/oauth/token\",\r\n" + 
	    		"							\"protocol\": \"https\",\r\n" + 
	    		"							\"host\": [\r\n" + 
	    		"								\"customer-i\",\r\n" + 
	    		"								\"bmwgroup\",\r\n" + 
	    		"								\"com\"\r\n" + 
	    		"							],\r\n" + 
	    		"							\"path\": [\r\n" + 
	    		"								\"gcdm\",\r\n" + 
	    		"								\"oauth\",\r\n" + 
	    		"								\"token\"\r\n" + 
	    		"							]\r\n" + 
	    		"						}\r\n" + 
	    		"					},\r\n" + 
	    		"					\"status\": \"Bad Request\",\r\n" + 
	    		"					\"code\": 400,\r\n" + 
	    		"					\"_postman_previewlanguage\": \"json\",\r\n" + 
	    		"					\"header\": [\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Date\",\r\n" + 
	    		"							\"value\": \"Sun, 18 Sep 2022 07:31:55 GMT\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Content-Type\",\r\n" + 
	    		"							\"value\": \"application/json\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Content-Length\",\r\n" + 
	    		"							\"value\": \"87\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Connection\",\r\n" + 
	    		"							\"value\": \"keep-alive\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Origin\",\r\n" + 
	    		"							\"value\": \"\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Headers\",\r\n" + 
	    		"							\"value\": \"Authorization, Origin, X-c2b-Authorization, X-c2b-mTAN, X-Requested-With, X-c2b-Sender-Id, X-c2b-External-Id, Content-Type, Accept, Cache-Control, KeyId, x-dtc\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Max-Age\",\r\n" + 
	    		"							\"value\": \"3628800\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Credentials\",\r\n" + 
	    		"							\"value\": \"true\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Access-Control-Allow-Methods\",\r\n" + 
	    		"							\"value\": \"POST, GET, OPTIONS, PUT, DELETE, HEAD\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"Referrer-Policy\",\r\n" + 
	    		"							\"value\": \"strict-origin\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"x-c2b-request-id\",\r\n" + 
	    		"							\"value\": \"rrt-1374166732152246650-d-geu1-19272-335589859-3\"\r\n" + 
	    		"						}\r\n" + 
	    		"					],\r\n" + 
	    		"					\"cookie\": [],\r\n" + 
	    		"					\"body\": \"{\\n    \\\"error\\\": \\\"invalid_request\\\",\\n    \\\"error_description\\\": \\\"Parameter response_type is missing\\\"\\n}\"\r\n" + 
	    		"				}\r\n" + 
	    		"			]\r\n" + 
	    		"		},\r\n" + 
	    		"		{\r\n" + 
	    		"			\"name\": \"ACF1\",\r\n" + 
	    		"			\"event\": [\r\n" + 
	    		"				{\r\n" + 
	    		"					\"listen\": \"test\",\r\n" + 
	    		"					\"script\": {\r\n" + 
	    		"						\"exec\": [\r\n" + 
	    		"							\"var jsonData = JSON.parse(responseBody);\",\r\n" + 
	    		"							\"postman.setEnvironmentVariable(\\\"access_token\\\", jsonData.access_token);\",\r\n" + 
	    		"							\"postman.setEnvironmentVariable(\\\"refresh_token\\\", jsonData.refresh_token);\",\r\n" + 
	    		"							\"\",\r\n" + 
	    		"							\"\"\r\n" + 
	    		"						],\r\n" + 
	    		"						\"type\": \"text/javascript\"\r\n" + 
	    		"					}\r\n" + 
	    		"				}\r\n" + 
	    		"			],\r\n" + 
	    		"			\"request\": {\r\n" + 
	    		"				\"auth\": {\r\n" + 
	    		"					\"type\": \"basic\",\r\n" + 
	    		"					\"basic\": [\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"password\",\r\n" + 
	    		"							\"value\": \"{{sso_client_secret}}\",\r\n" + 
	    		"							\"type\": \"string\"\r\n" + 
	    		"						},\r\n" + 
	    		"						{\r\n" + 
	    		"							\"key\": \"username\",\r\n" + 
	    		"							\"value\": \"{{sso_client_id}}\",\r\n" + 
	    		"							\"type\": \"string\"\r\n" + 
	    		"						}\r\n" + 
	    		"					]\r\n" + 
	    		"				},\r\n" + 
	    		"				\"method\": \"POST\",\r\n" + 
	    		"				\"header\": [\r\n" + 
	    		"					{\r\n" + 
	    		"						\"key\": \"Accept\",\r\n" + 
	    		"						\"value\": \"application/json\"\r\n" + 
	    		"					},\r\n" + 
	    		"					{\r\n" + 
	    		"						\"key\": \"Content-Type\",\r\n" + 
	    		"						\"value\": \"application/x-www-form-urlencoded\"\r\n" + 
	    		"					}\r\n" + 
	    		"				],\r\n" + 
	    		"				\"body\": {\r\n" + 
	    		"					\"mode\": \"raw\",\r\n" + 
	    		"					\"raw\": \"username={{loginId}}&password=secret123&grant_type=password\"\r\n" + 
	    		"				},\r\n" + 
	    		"				\"url\": {\r\n" + 
	    		"					\"raw\": \"https://customer-i.bmwgroup.com/gcdm/oauth/token\",\r\n" + 
	    		"					\"protocol\": \"https\",\r\n" + 
	    		"					\"host\": [\r\n" + 
	    		"						\"customer-i\",\r\n" + 
	    		"						\"bmwgroup\",\r\n" + 
	    		"						\"com\"\r\n" + 
	    		"					],\r\n" + 
	    		"					\"path\": [\r\n" + 
	    		"						\"gcdm\",\r\n" + 
	    		"						\"oauth\",\r\n" + 
	    		"						\"token\"\r\n" + 
	    		"					]\r\n" + 
	    		"				}\r\n" + 
	    		"			},\r\n" + 
	    		"			\"response\": []\r\n" + 
	    		"		}\r\n" + 
	    		"	]\r\n" + 
	    		"}";

	    
	    String jsonPath = "C:/sample.json";
	    
	    PostmanRunResult runCollection = new PostmanRunResult();
	    
	    try {
			runCollection = new PostmanCollectionRunner().runCollection(jsonPath, null, null, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		return runCollection.toString();
	}
}
