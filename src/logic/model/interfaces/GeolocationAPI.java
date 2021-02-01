package logic.model.interfaces;

import java.util.List;

import logic.model.Location;

public interface GeolocationAPI {
	
	
	public Location getLocationByName(String location);
	
	public Double getLocationLatitude(String locationName);
	
	public Double getLocationLongitude(String locationName);
	
	public String getLocationCountry(String locationName);
	
	
	
	public List<Location> getPlacesByNameAndCategory(String location, String coordinates, String category);
	
	
	
}
