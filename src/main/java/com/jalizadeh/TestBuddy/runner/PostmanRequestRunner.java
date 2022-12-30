package com.jalizadeh.testbuddy.runner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.net.ssl.SSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpRequest;

import com.jalizadeh.testbuddy.model.PostmanItem;
import com.jalizadeh.testbuddy.model.PostmanVariables;


public class PostmanRequestRunner {
	private static final Logger logger = LoggerFactory.getLogger(PostmanRequestRunner.class);
	private PostmanVariables var;
	private boolean haltOnError = false;
	private List<Observer> observers;

	public static interface Observer {
		void preTransport(PostmanItem item, HttpRequest httpRequest);

		void postTransport(PostmanItem item, HttpRange httpResponse);
	}

	public PostmanRequestRunner(PostmanVariables pv, boolean haltOnError, List<Observer> observers) {
		this.var = pv;
		this.haltOnError = haltOnError;
		this.observers = observers;
	}

	
	/*
	protected CloseableHttpClient createHttpClient() {
		try {
			SSLContext sslContext = SSLContexts.custom().useProtocol("TLSv1.2").build();
			RequestConfig config = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(5000)
					.setConnectionRequestTimeout(60000).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

			CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext)
					.setDefaultRequestConfig(config).build();
			return httpClient;
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			logger.error("Failed to create http client using TLSv1.2");
			throw new RuntimeException("Failed to create http client using TLSv1.2.", e);
		}
	}

	public boolean run(PostmanItem item, PostmanRunResult runResult) {

		runPrerequestScript(item, runResult);
		PostmanRequest request = item.request;
		Map<String, String> headers = request.getHeaders(var);
		StringEntity entity;
		if (request.body != null && request.body.mode != null && request.body.mode.equals("urlencoded")) {
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			entity = new StringEntity(request.getData(var), ContentType.APPLICATION_FORM_URLENCODED);
		} else {
			entity = new StringEntity(request.getData(var), ContentType.APPLICATION_JSON);
		}
		String requestId = headers.get(PoyntHttpHeaders.REQUEST_ID_HEADER);
		if (requestId == null) {
			requestId = UUID.randomUUID().toString();
			headers.put(PoyntHttpHeaders.REQUEST_ID_HEADER, requestId);
		}
		logger.info("===============> requestId:" + requestId);
		String url = request.getUrl(var);
		URI uri;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			if (haltOnError)
				throw new HaltTestFolderException();
			else
				return false;
		}

		HttpRequestBase httpMethod;
		switch (request.method) {
		case "GET":
			httpMethod = new HttpGet(uri);
			break;
		case "POST":
			HttpPost post = new HttpPost(uri);
			post.setEntity(entity);
			httpMethod = post;
			break;
		case "PUT":
			HttpPut put = new HttpPut(uri);
			put.setEntity(entity);
			httpMethod = put;
			break;
		case "PATCH":
			HttpPatch patch = new HttpPatch(uri);
			patch.setEntity(entity);
			httpMethod = patch;
			break;
		case "DELETE":
			httpMethod = new HttpDelete(uri);
			break;
		default:
			logger.error("Invalid http method: {}", request.method);
			if (haltOnError)
				throw new HaltTestFolderException();
			else
				return false;
		}
		for (Entry<String, String> entry : headers.entrySet()) {
			httpMethod.setHeader(entry.getKey(), entry.getValue());
		}

		if (observers != null) {
			for (Observer ob : observers) {
				ob.preTransport(item, httpMethod);
			}
		}
		long startMillis = System.currentTimeMillis();
		PostmanHttpResponse response;
		try (CloseableHttpClient httpClient = createHttpClient()) {
			HttpResponse httpResponse = httpClient.execute(httpMethod);
			if (observers != null) {
				for (Observer ob : observers) {
					ob.postTransport(item, httpResponse);
				}
			}

			response = new PostmanHttpResponse(httpResponse);
		} catch (IOException e) {
			logger.error("Failed to execute http request.");
			if (haltOnError)
				throw new HaltTestFolderException(e);
			else
				return false;
		}
		logger.info(" [" + (System.currentTimeMillis() - startMillis) + "ms]");

		// NOTE: there are certain negative test cases that expect 5xx series
		// response code.
		return this.evaluateTests(item, response, runResult);
	}
	
	*/

	/**
	 * @param item - item to be evaluated against
	 * @param httpResponse - the httpResponse
	 * @param runResult - the run result
	 * @return true if all tests pass, false otherwise
	 */
	
	
	/*
	public boolean evaluateTests(PostmanItem item, PostmanHttpResponse httpResponse, PostmanRunResult runResult) {
		List<String> tests = new ArrayList<>();
		if (item.event == null || item.event.size() == 0) {
			return true;
		} else {
			for (PostmanEvent event : item.event) {
				if (event.listen.equals("test")) {
					tests = event.script.exec;
				}
			}
		}
		if (tests.isEmpty()) {
			return true;
		}
		String testsAsString = stringListToString(tests);
		testsAsString = var.replace(testsAsString);
		Context cx = Context.enter();
		String testName = "---------------------> POSTMAN test";
		boolean isSuccessful = false;
		try {
			Scriptable scope = cx.initStandardObjects();
			PostmanJsVariables jsVar = new PostmanJsVariables(cx, scope, this.var.getEnv());
			jsVar.prepare(httpResponse);

			// Evaluate the test script
			cx.evaluateString(scope, testsAsString, testName, 1, null);
			// The results are in the jsVar.tests variable

			// Extract any generated environment variables during the js run.
			jsVar.extractEnvironmentVariables();
			isSuccessful = true;
			boolean hasFailure = false;
			for (Entry e : jsVar.tests.entrySet()) {
				runResult.totalTest++;

				String strVal = e.getValue().toString();
				if ("false".equalsIgnoreCase(strVal)) {
					hasFailure = true;
					runResult.failedTest++;
					runResult.failedTestName.add(item.name + "." + e.getKey().toString());
					isSuccessful = false;
				}

				logger.info(testName + ": " + e.getKey() + " - " + e.getValue());
			}
			if (hasFailure) {
				logger.info("=====THERE ARE TEST FAILURES=====");
				logger.info("========TEST========");
				logger.info(testsAsString);
				logger.info("========TEST========");
				logger.info("========RESPONSE========");
				logger.info(String.valueOf(httpResponse.code));
				logger.info(httpResponse.body);
				logger.info("========RESPONSE========");
				logger.info("=====THERE ARE TEST FAILURES=====");
			}
		} catch (Throwable t) {
			isSuccessful = false;
			logger.info("=====FAILED TO EVALUATE TEST AGAINST SERVER RESPONSE======");
			logger.info("========TEST========");
			logger.info(testsAsString);
			logger.info("========TEST========");
			logger.info("========RESPONSE========");
			logger.info(String.valueOf(httpResponse.code));
			logger.info(httpResponse.body);
			logger.info("========RESPONSE========");
			logger.info("=====FAILED TO EVALUATE TEST AGAINST SERVER RESPONSE======");
		} finally {
			Context.exit();
		}
		return isSuccessful;
	}

	public String stringListToString(List<String> tests) {
		StringBuilder testsBuilder = new StringBuilder();
		for (String s : tests) {
			testsBuilder.append(s);
			testsBuilder.append("\n");
		}
		return testsBuilder.toString().trim();
	}

	public boolean runPrerequestScript(PostmanItem item, PostmanRunResult runResult) {
		List<String> prerequest = new ArrayList<>();
		if (item.event == null || item.event.isEmpty()) {
			return true;
		} else {
			for (PostmanEvent event : item.event) {
				if (event.listen.equals("prerequest")) {
					prerequest = event.script.exec;
				}
			}
		}
		String preRequestString = stringListToString(prerequest);
		Context cx = Context.enter();
		String testName = "---------------------> POSTMAN test: ";
		boolean isSuccessful = false;
		try {
			Scriptable scope = cx.initStandardObjects();
			PostmanJsVariables jsVar = new PostmanJsVariables(cx, scope, this.var.getEnv());
			// jsVar.prepare(httpResponse);
			jsVar.prepare(null);

			// Evaluate the test script
			cx.evaluateString(scope, preRequestString, testName, 1, null);
			// The results are in the jsVar.tests ???? variable

			// Extract any generated environment variables during the js run.
			jsVar.extractEnvironmentVariables();
			isSuccessful = true;
		} finally {
			Context.exit();
		}
		return isSuccessful;
	}
	
	*/
}
