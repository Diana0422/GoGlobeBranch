package logic.model.adapters;

import logic.model.apis.IPFindPositionFinderAPI;
import logic.model.exceptions.APIException;
import logic.model.exceptions.LocationNotFoundException;
import logic.model.interfaces.PositionFinder;

public class IPFindPositionFinderAdapter implements PositionFinder {
	
	private IPFindPositionFinderAPI api;
	
	public IPFindPositionFinderAdapter() {
		this.api = new IPFindPositionFinderAPI();
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
