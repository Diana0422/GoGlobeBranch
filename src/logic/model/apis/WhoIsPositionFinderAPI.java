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

public class WhoIsPositionFinderAPI {
	

	public String geolocateIP(String ip) throws APIException {
		HttpRequest request2 = HttpRequest.newBuilder()
    			.uri(URI.create("http://ipwhois.app/json/"+ip))
    			.method("GET", HttpRequest.BodyPublishers.noBody())
    			.build();
        
        HttpResponse<String> response;
		try {
			response = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
			
			String logStr = "\n"+response.body();
			Logger.getGlobal().info(logStr);
		    	
		    JSONObject ipInfo = new JSONObject(response.body());
		    return ipInfo.getString("country_capital");
		} catch (IOException | JSONException e) {
			throw new APIException(e.getCause(), "Error sending request to API");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new APIException(e.getCause(), "Interrupted IP location routine.");
		}
	}
}
