package logic.model.apis;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import logic.model.exceptions.APIException;
import logic.model.exceptions.FlightNotFoundException;

public class SkyscannerAPI {
	
	private JSONArray carriers;
	private JSONArray places;
	private JSONObject placeInfo;
	private static final String FLIGHT_OBJ = "OutboundLeg";

	public String getCityId(String cityName, String locale, String country, String currency) throws APIException {
		String query = cityName;
		
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/"
							+country+"/"
							+currency+"/"
							+locale+"/"
							+"?query="+query))
					.header("x-rapidapi-key", "dcbd2cb61amsh9bca9dccbe16d5bp13175ajsnba4d6a777e3c")
					.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
					.method("GET", HttpRequest.BodyPublishers.noBody())
					.build();
			HttpResponse<String> response;
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			
			JSONObject json = new JSONObject(response.body());
			this.placeInfo = json.getJSONArray("Places").getJSONObject(0);
			return this.placeInfo.getString("CityId");
			
		} catch (IOException | JSONException  e) {
			throw new APIException(e.getCause(), "Error parsing JSON.");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new APIException(e.getCause(), "The request to the API was interrupted.");
		}
	}
	
	public String getCountry(String cityName, String locale, String localeCountry, String currency) throws APIException {
		getCityId(cityName,locale, localeCountry, currency);
		return this.placeInfo.getString("CountryName");
	}


	public JSONObject getCheapestFlight(String originId, String destId, String outboundpartial, String locale, String country,
			String currency) throws FlightNotFoundException, APIException {
		
		try
        {      	
        	HttpRequest request = HttpRequest.newBuilder()
        			.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/"
        					+country+"/"
        					+currency+"/"
        					+locale+"/"
        					+originId+"/"
        					+destId+"/"
        					+outboundpartial))
        			.header("x-rapidapi-key", "dcbd2cb61amsh9bca9dccbe16d5bp13175ajsnba4d6a777e3c")
        			.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
        			.method("GET", HttpRequest.BodyPublishers.noBody())
        			.build();
        	HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            this.carriers = json.getJSONArray("Carriers");
            this.places = json.getJSONArray("Places");
            JSONArray quotes = json.getJSONArray("Quotes");
            
            // FlightNotFoundException handling
            if (!quotes.isEmpty()) {
            	return quotes.getJSONObject(0);
            } else {
            	throw new FlightNotFoundException("No flight was found for the origin: "+originId+"and destination: "+destId);
            }
            
        } catch (IOException | JSONException e) {
            throw new APIException(e.getCause(), "Error parsing JSON.");
        } catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new APIException(e.getCause(), "The request to the API was interrupted.");
		}
	}


	public String getOrigin(JSONObject flight)  {
		
		JSONObject details = flight.getJSONObject(FLIGHT_OBJ);
    	int originId = details.getInt("OriginId");
		
    	for (int i=0; i<this.places.length(); i++) {
    		JSONObject place = this.places.getJSONObject(i);
    		if (place.getInt("PlaceId") == originId) return place.getString("Name");
    	}
		return null;	
	}
	
	
	public String getDestination(JSONObject flight) {
		
		JSONObject details = flight.getJSONObject(FLIGHT_OBJ);
		int destId = details.getInt("DestinationId");
		
		for (int i=0; i<this.places.length(); i++) {
			JSONObject place = this.places.getJSONObject(i);
			if (place.getInt("PlaceId") == destId) return place.getString("Name");
		}
		
		return null;
	}
	


	public String getCarrierName(JSONObject flight) {
		JSONObject details = flight.getJSONObject(FLIGHT_OBJ);
		int carrierId = details.getJSONArray("CarrierIds").getInt(0);
		
		for (int i=0; i<this.carriers.length(); i++) {
			JSONObject carrier = this.carriers.getJSONObject(i);
			if (carrier.getInt("CarrierId") == carrierId) return carrier.getString("Name");
		}
		return null;
	}


	public int getPrice(JSONObject flight) {
		if (flight != null) return flight.getInt("MinPrice");
		return 0;
	}

}
