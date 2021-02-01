package logic.model.apis;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class HereImplementation {
	
	
	private static final String API_KEY = "oDbN2CPeuk1ebsCIJhhcaxEKnKuGCJiUVQB9KYkjAYY";
	
	public JSONObject getNearbyPlaces(String coordinates, String category) throws IOException, InterruptedException {
		try
        {
        	HttpRequest request = HttpRequest.newBuilder()
        			.uri(URI.create("https://places.ls.hereapi.com/places/v1/discover/explore"
        					+ "?apiKey="+ API_KEY
        					+ "&in=" + coordinates + ";r=80000"
        					+ category
        					+ "&size=50"
        					+ "&pretty"))
        			.method("GET", HttpRequest.BodyPublishers.noBody())
        			.header("Accepted-Language", "en-en")
        			.build();
 
        
        	HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        	
        	return new JSONObject(response.body());
        } catch (IOException | JSONException e) {
        	throw e;
        } catch (InterruptedException e) {
        	Thread.currentThread().interrupt();
        	throw e;
		}	
    }

	
	//GET LOCATION'S GEOCODE INFORMATION	
	public JSONObject getLocationInfo(String locationName) throws IOException, InterruptedException {				
           return getGeocodeResponse(locationName);            	
    }
	
	//SEND GEOCODE REQUEST FOR locationName
	public JSONObject getGeocodeResponse(String locationName) throws IOException, InterruptedException {
		try {
			//Generating request
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://geocode.search.hereapi.com/v1/"
							+"geocode" 
							+"?q=" + locationName
							+"&apiKey=" + API_KEY))
					.method("GET", HttpRequest.BodyPublishers.noBody())
					.build();       
        	HttpResponse<String> response = HttpClient.newHttpClient()
        			.send(request, HttpResponse.BodyHandlers.ofString());
        	
            return new JSONObject(response.body());
		}catch (IOException | JSONException e){
	        throw e;
	    } catch (InterruptedException e) {
	    	Thread.currentThread().interrupt();
	    	throw e;
		}		
	}
}

