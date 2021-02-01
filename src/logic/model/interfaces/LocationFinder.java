package logic.model.interfaces;

import java.util.List;

import logic.model.PlaceBean;
import logic.model.exceptions.APIException;
import logic.model.Location;

public interface LocationFinder {
	
	
	public Location getLocationInfo(String locationName) throws APIException;
	
	
	public List<PlaceBean> getNearbyPlaces(String coordinates, String category) throws APIException;
}
