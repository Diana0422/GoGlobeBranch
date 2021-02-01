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
	
	private String originCityId;
	private String destCityId;
	private JSONObject flight;
	private JSONArray carriers;
	private JSONArray places;
	private static final String FLIGHT_OBJ = "OutboundLeg";

	public String getOriginCityId() {
		return originCityId;
	}

	public void setOriginCityId(String originCityId) {
		this.originCityId = originCityId;
	}

	public String getDestCityId() {
		return destCityId;
	}

	public void setDestCityId(String destCityId) {
		this.destCityId = destCityId;
	}

	public JSONObject getFlight() {
		return flight;
	}

	public void setFlight(JSONObject flight) {
		this.flight = flight;
	}
	
	public JSONArray getCarriers() {
		return carriers;
	}

	public void setCarriers(JSONArray carriers) {
		this.carriers = carriers;
	}
	
	public JSONArray getPlaces() {
		return places;
	}

	public void setPlaces(JSONArray places) {
		this.places = places;
	}

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
			return json.getJSONArray("Places").getJSONObject(0).getString("CityId");
			
		} catch (IOException | JSONException  e) {
			throw new APIException(e.getCause(), "Error parsing JSON.");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new APIException(e.getCause(), "The request to the API was interrupted.");
		}
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
            setCarriers(json.getJSONArray("Carriers"));
            setPlaces(json.getJSONArray("Places"));
            JSONArray quotes = json.getJSONArray("Quotes");
            
            // FlightNotFoundException handling
            if (!quotes.isEmpty()) {
            	setFlight(quotes.getJSONObject(0));
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
		
    	for (int i=0; i<getPlaces().length(); i++) {
    		JSONObject place = getPlaces().getJSONObject(i);
    		if (place.getInt("PlaceId") == originId) return place.getString("Name");
    	}
		return null;	
	}
	
	
	public String getDestination(JSONObject flight) {
		
		JSONObject details = flight.getJSONObject(FLIGHT_OBJ);
		int destId = details.getInt("DestinationId");
		
		for (int i=0; i<getPlaces().length(); i++) {
			JSONObject place = getPlaces().getJSONObject(i);
			if (place.getInt("PlaceId") == destId) return place.getString("Name");
		}
		
		return null;
	}
	


	public String getCarrierName(JSONObject flight) {
		JSONObject details = flight.getJSONObject(FLIGHT_OBJ);
		int carrierId = details.getJSONArray("CarrierIds").getInt(0);
		
		for (int i=0; i<getCarriers().length(); i++) {
			JSONObject carrier = getCarriers().getJSONObject(i);
			if (carrier.getInt("CarrierId") == carrierId) return carrier.getString("Name");
		}
		return null;
	}


	public int getPrice(JSONObject flight) {
		if (flight != null) return flight.getInt("MinPrice");
		return 0;
	}

}
