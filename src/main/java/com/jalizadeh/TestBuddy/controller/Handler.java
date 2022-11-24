package com.jalizadeh.TestBuddy.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalizadeh.TestBuddy.central.FilterFactory;
import com.jalizadeh.TestBuddy.central.FiltersManager;
import com.jalizadeh.TestBuddy.central.StatisticsManager;
import com.jalizadeh.TestBuddy.model.InputRequest;
import com.jalizadeh.TestBuddy.model.PostmanCollection;
import com.jalizadeh.TestBuddy.runner.PostmanCollectionRunner;
import com.jalizadeh.TestBuddy.service.RestService;
import com.jalizadeh.TestBuddy.statistics.StatReport;
import com.jalizadeh.TestBuddy.types.Filters;

@RestController
public class Handler {
	
	@Autowired
	private Environment env;

	@Autowired
	private RestService restService;
	
	@Autowired
	private FilterFactory filterFactory;
	
	@Value("${htmlReportFile.path}") 
	private Resource reportTemplateLocation;

	@PostMapping("/json")
	public StatReport parseJson(
			@RequestParam(required = false) Optional<Integer> delay, 
			@RequestBody InputRequest input ) throws Exception {
		
		extractFilters(input);
		
		String jsonPath = env.getProperty("pm.file");

		PostmanCollection collection = new PostmanCollection();
		ObjectMapper mapper = new ObjectMapper();
		PostmanCollection parsedCollection = null;
		
		try {
			collection = new PostmanCollectionRunner().parseCollection(jsonPath, null, "Test", false, false);
			parsedCollection = restService.parseCollection(collection, delay);
			mapper.writerWithDefaultPrettyPrinter().writeValue(getFileName(collection), parsedCollection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//return parsedCollection;
		StatReport statReport = StatisticsManager.getInstance().getReport(collection.info.name);
		generateHtmlReport(statReport);
		return statReport;
	}


	private void generateHtmlReport(StatReport statReport) throws IOException {
		File htmlTemplateFile = reportTemplateLocation.getFile();
		String htmlString = FileUtils.readFileToString(htmlTemplateFile);
		String title = "TestBuddy | " + statReport.getCollectionName();
		
		htmlString = htmlString.replace("$title", title);
		htmlString = htmlString.replace("$collectionName", statReport.getCollectionName());
		htmlString = htmlString.replace("$totalRequests", statReport.getTotalRequests()+"");
		htmlString = htmlString.replace("$totalCalls", statReport.getTotalCalls()+"");
		htmlString = htmlString.replace("$totalPositive", statReport.getTotalPositive()+"");
		htmlString = htmlString.replace("$totalNegative", statReport.getTotalNegative()+"");
		
		StringBuilder sbReq = new StringBuilder();
		statReport.getRequests().stream()
			.forEach(r -> {
				sbReq
					.append("<div>")
					.append("<h2>")
					.append(r.getName())
					.append(" <span class=\"badge rounded-pill text-bg-secondary\">" + r.getMethod() + "</span>")
					.append(" <span class=\"badge rounded-pill text-bg-success\">" + r.getPositive() + "</span>")
					.append(" <span class=\"badge rounded-pill text-bg-danger\">" + r.getNegative() +"</span>")
					.append("</h2>")
					//.append("<p>" + r.getDescription() + "</p>")
					.append("<ul class=\"icon-list ps-0 text-secondary\">");
				
				r.getStatus().forEach(s -> sbReq.append("<li class=\"d-flex align-items-start mb-1\">" + s + "</li>"));
				
				sbReq
					.append("</ul>")
					.append("</div>");
			});
		htmlString = htmlString.replace("$requests", sbReq);
		
		File newHtmlFile = new File(env.getProperty("generatedFile.path") + "report.html");
		FileUtils.writeStringToFile(newHtmlFile, htmlString);
	}


	private File getFileName(PostmanCollection collection) {
		return new File(env.getProperty("generatedFile.path") + collection.info.name + ".json");
	}


	private void extractFilters(InputRequest input) {
		FiltersManager instance = FiltersManager.getInstance();
		if(validateInput(input)) {
			instance.clearFilters();
			
			for(Filters f : input.getFilters()) {
				try {
					instance.addFilter(filterFactory.create(f));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			instance = FiltersManager.getInstance();
			instance.clearFilters();
		}
	}


	private boolean validateInput(InputRequest input) {
		return (input == null || input.getFilters() == null ||  input.getFilters().size() == 0) ? false : true; 
	}
	

}
