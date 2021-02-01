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


public class IPFindPositionFinderAPI {
	
	private static final String APIKEY = "534948a4-7cc6-4cd6-b9ed-be250ff572f4";

	public String geolocateIP(String ip) throws APIException {
		HttpRequest request2 = HttpRequest.newBuilder()
    			.uri(URI.create("https://api.ipfind.com?ip="+ip+"&auth="+APIKEY))
    			.method("GET", HttpRequest.BodyPublishers.noBody())
    			.build();
        
        HttpResponse<String> response2;
		try {
			response2 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
			
			String logStr = "\n"+response2.body();
			Logger.getGlobal().info(logStr);
		    	
		    JSONObject ipInfo = new JSONObject(response2.body());
		    return ipInfo.getString("city");
		} catch (IOException | JSONException e) {
			throw new APIException(e.getCause(), "Error sending request to API");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new APIException(e.getCause(), "Interrupted IP location routine.");
		}
	}

}
