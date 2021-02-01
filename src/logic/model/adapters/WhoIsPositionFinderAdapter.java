package logic.model.adapters;

import logic.model.apis.WhoIsPositionFinderAPI;
import logic.model.exceptions.APIException;
import logic.model.exceptions.LocationNotFoundException;
import logic.model.interfaces.PositionFinder;

public class WhoIsPositionFinderAdapter implements PositionFinder {
	
	private WhoIsPositionFinderAPI api;
	
	public WhoIsPositionFinderAdapter() {
		this.api = new WhoIsPositionFinderAPI();
	}

	@Override
	public String getUserPosition(String ipAddress) throws LocationNotFoundException {
		try {
			return api.geolocateIP(ipAddress);
		} catch (APIException e) {
			throw new LocationNotFoundException(e, e.getMessage());
		}
	}

}
