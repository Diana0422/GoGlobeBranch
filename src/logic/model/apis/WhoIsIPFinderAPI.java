package logic.model.apis;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import logic.model.exceptions.APIException;

public class WhoIsIPFinderAPI {
	
	public String getUserIP() throws APIException {
			
		HttpRequest request = HttpRequest.newBuilder()
	    		.uri(URI.create("http://ipwhois.app/json/"))
	    		.method("GET", HttpRequest.BodyPublishers.noBody())
	    		.build();
			
		HttpResponse<String> response;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
					
			String logStr = response.body();
			Logger.getGlobal().info(logStr);
			        
			JSONObject json = new JSONObject(response.body());
			    
			logStr = "IP Address: "+json.getString("ip");
			Logger.getGlobal().info(logStr);
			return json.getString("ip");
		} catch (IOException | JSONException e) {
			throw new APIException(e.getCause(), "Error sending request to API");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new APIException(e.getCause(), "Interrupted IP location routine.");
		}
	}
}
