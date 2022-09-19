package com.jalizadeh.TestBuddy.runner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalizadeh.TestBuddy.model.PostmanCollection;
import com.jalizadeh.TestBuddy.model.PostmanFolder;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanReader;
import com.jalizadeh.TestBuddy.model.PostmanVariables;



public class PostmanCollectionRunner implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(PostmanCollectionRunner.class);
	private String colFilename;
	private String envFilename;
	private String folderName;
	private boolean haltOnError;

	//private PostmanVariables sharedPostmanEnvVars;

	@Override
	public void run() {
		try {
			runCollection(colFilename, envFilename, folderName, haltOnError, false, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public PostmanRunResult runCollection(String colFilename, String envFilename, String folderName,
			boolean haltOnError) throws Exception {
		return runCollection(colFilename, envFilename, folderName, haltOnError, false, null);
	}

	/**
	 *
	 * @param colFilename          - collection file
	 * @param envFilename          - environment file
	 * @param folderName           - folder the files are in
	 * @param haltOnError          - stop on error
	 * @param useSharedPostmanVars Use a single set of postman variable(s) across
	 *                             all your tests. This allows for running tests
	 *                             between a select few postman folders while
	 *                             retaining environment variables between each run
	 * @param observers            - observer hooks for request runners
	 * @return The run result object has statistics of the execution.
	 * @throws IOException - if failed to read collection
	 */
	public PostmanRunResult runCollection(String colFilename, String envFilename, String folderName,
			boolean haltOnError, boolean useSharedPostmanVars, List<PostmanRequestRunner.Observer> observers)
			throws IOException {
		logger.info("@@@@@ POSTMAN Runner start: {}", colFilename);
		PostmanRunResult runResult = new PostmanRunResult();

		PostmanReader reader = new PostmanReader();
		PostmanCollection collection = reader.readCollectionFile(colFilename);
		collection.init();
		//PostmanEnvironment e = reader.readEnvironmentFile(envFilename);
		//e.init();
		PostmanFolder folder = null;
		if (folderName != null && !folderName.isEmpty()) {
			folder = collection.folderLookup.get(folderName);
		}
		
		for(PostmanFolder f : collection.item ) {
			System.out.println("Reuqest: " + f.name + " /  " + f.item.size());
			
			for(PostmanItem item : f.item) {
				System.out.println("item: " + item.name);
			}
		}
		
		
		//writing test to new file
		ObjectMapper mapper = new ObjectMapper();
	    try {  
	        mapper.writeValue(new File("C:/Users/Javad Alizadeh/Desktop/result.json"), collection );
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } 
	    

		/*
		PostmanVariables var;
		if (useSharedPostmanVars) {
			if (sharedPostmanEnvVars == null) {
				sharedPostmanEnvVars = new PostmanVariables(e);
			}
			var = sharedPostmanEnvVars;
		} else {
			var = new PostmanVariables(e);
		}

		PostmanRequestRunner runner = new PostmanRequestRunner(var, haltOnError, observers);
		boolean isSuccessful = true;
		if (folder != null) {
			isSuccessful = runFolder(haltOnError, runner, var, folder, runResult);
		} else {
			// Execute all folder all requests
			for (PostmanFolder pf : c.item) {
				isSuccessful = runFolder(haltOnError, runner, var, pf, runResult) && isSuccessful;
				if (haltOnError && !isSuccessful) {
					return runResult;
				}
			}
		}
		*/

		logger.info("---- Collection loaded succesfully ----");
		return runResult;
	}
	
	
	
	
	public PostmanCollection parseCollection(String colFilename, String envFilename, String folderName,
			boolean haltOnError, boolean useSharedPostmanVars) throws IOException {
		logger.info("@@@@@ POSTMAN Runner start: {}", colFilename);
		PostmanRunResult runResult = new PostmanRunResult();

		PostmanReader reader = new PostmanReader();
		PostmanCollection collection = reader.readCollectionFile(colFilename);
		collection.init();
		//PostmanEnvironment e = reader.readEnvironmentFile(envFilename);
		//e.init();
		PostmanFolder folder = null;
		if (folderName != null && !folderName.isEmpty()) {
			folder = collection.folderLookup.get(folderName);
		}
		
		for(PostmanFolder f : collection.item ) {
			System.out.println("Reuqest: " + f.name + " /  " + f.item.size());
			
			for(PostmanItem item : f.item) {
				System.out.println("item: " + item.name);
			}
		}
		
		//writing test to new file
		ObjectMapper mapper = new ObjectMapper();
	    try {  
	        mapper.writeValue(new File("C:/Users/Javad Alizadeh/Desktop/result.json"), collection );
	    } catch (IOException e) {  
	        e.printStackTrace();
	    } 
	    
		logger.info("---- Collection loaded succesfully ----");
		return collection;
	}
	

	/*
	private boolean runFolder(boolean haltOnError, PostmanRequestRunner runner, PostmanVariables var,
			PostmanFolder folder, PostmanRunResult runResult) {
		logger.info("==> POSTMAN Folder: " + folder.name);
		boolean isSuccessful = true;
		for (PostmanItem fItem : folder.item) {
			runResult.totalRequest++;
			logger.info("======> POSTMAN request: " + fItem.name);
			try {
				boolean runSuccess = runner.run(fItem, runResult);
				if (!runSuccess) {
					runResult.failedRequest++;
					runResult.failedRequestName.add(folder.name + "." + fItem.name);
				}
				isSuccessful = runSuccess && isSuccessful;
				if (haltOnError && !isSuccessful) {
					return isSuccessful;
				}
			} catch (Throwable e) {
				e.printStackTrace();
				runResult.failedRequest++;
				runResult.failedRequestName.add(folder.name + "." + fItem.name);
				return false;
			}
		}
		return isSuccessful;
	}
	*/
}
