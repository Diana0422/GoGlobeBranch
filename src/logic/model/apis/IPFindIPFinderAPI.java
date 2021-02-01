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

public class IPFindIPFinderAPI {
	
	private static final String APIKEY = "534948a4-7cc6-4cd6-b9ed-be250ff572f4";
	
	public String getUserIP() throws APIException {
		
		HttpRequest request = HttpRequest.newBuilder()
	    		.uri(URI.create("https://api.ipfind.com/me?auth="+APIKEY))
	    		.method("GET", HttpRequest.BodyPublishers.noBody())
	    		.build();
			
		HttpResponse<String> resp;
		try {
			resp = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
					
			String logStr = resp.body();
			Logger.getGlobal().info(logStr);
			        
			JSONObject json = new JSONObject(resp.body());
			    
			logStr = "IP Address: "+json.getString("ip_address");
			Logger.getGlobal().info(logStr);
			return json.getString("ip_address");
		} catch (IOException | JSONException e) {
			throw new APIException(e.getCause(), "Error sending request to API");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new APIException(e.getCause(), "Interrupted IP location routine.");
		}
	}
}
