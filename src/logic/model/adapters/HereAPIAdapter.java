package logic.model.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import logic.control.FormatManager;
import logic.model.Location;
import logic.model.PlaceBean;
import logic.model.apis.HereImplementation;
import logic.model.exceptions.APIException;
import logic.model.interfaces.LocationFinder;

public class HereAPIAdapter implements LocationFinder{
	
	HashMap<String, String> categoryHash = new HashMap<>();
		
	private HereImplementation hereAPI;

	
	public HereAPIAdapter(HereImplementation api){
		this.hereAPI = api;
		
		categoryHash.put("ADVENTURE" ,  "&cat=natural-geographical");
		categoryHash.put("CULTURE"   , 	"&cat=sights-museums");
		categoryHash.put("FUN"       , 	"&cat=going-out");
		categoryHash.put("RELAX"     , 	"&cat=leisure-outdoor");
	}
	
	

	@Override
	public List<PlaceBean> getNearbyPlaces(String coordinates, String category) throws APIException {	
		category = categoryHash.get(category.toUpperCase());		
		JSONObject response;
		String placeName;
		String placeAddress;
		String iconRef;
		String openingHours;
		try {
			response = hereAPI.getNearbyPlaces(coordinates, category);
			List<PlaceBean> nearbyPlaces = new ArrayList<>();
			JSONArray items = response.getJSONObject("results").getJSONArray("items");
			//parse every item in response to Place objects
			for (int i = 0; i < items.length(); i++) {
				
				JSONObject place = items.getJSONObject(i);			
				placeName = place.getString("title");
				placeAddress = place.getString("vicinity");
				iconRef = place.getString("icon");
				if (place.has("openingHours")) {
					openingHours = place.getJSONObject("openingHours").getString("text");
				} else {
					openingHours = "";
				}
				
				PlaceBean newPlace = new PlaceBean(placeName, placeAddress, openingHours, iconRef);
				nearbyPlaces.add(newPlace);	
			}
			return nearbyPlaces;
		} catch (IOException e) {
			throw new APIException(e, "No nearby places found.");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new APIException(e, "Thread interrupted.");
		}		
	}
	
	//Get location info
	@Override
	public Location getLocationInfo(String locationName) throws APIException {
		JSONObject json;
		try {
			json = hereAPI.getLocationInfo(FormatManager.prepareToURL(locationName), FormatManager.formatLocale());
			JSONArray items = json.getJSONArray("items");           
	     	String countryName = items.getJSONObject(0).getJSONObject("address").getString("countryName");
	     	String cityName = items.getJSONObject(0).getJSONObject("address").getString("city");
	     	String lat = Double.toString(items.getJSONObject(0).getJSONObject("position").getDouble("lat"));
	     	String lng = Double.toString(items.getJSONObject(0).getJSONObject("position").getDouble("lng"));
	     	String cityCoordinates = lat + "," + lng;
			return new Location(cityName, countryName, cityCoordinates);
		} catch (IOException e) {
			throw new APIException(e, e.getMessage());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new APIException(e.getCause(), "The request to the API was interrupted.");
		}
	}

}
