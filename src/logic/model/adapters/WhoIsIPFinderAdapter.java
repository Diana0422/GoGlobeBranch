package logic.model.adapters;

import logic.model.apis.WhoIsIPFinderAPI;
import logic.model.exceptions.APIException;
import logic.model.interfaces.IPFinder;

public class WhoIsIPFinderAdapter implements IPFinder {
	
	private WhoIsIPFinderAPI api;
	
	public WhoIsIPFinderAdapter() {
		api = new WhoIsIPFinderAPI();
	}

	@Override
	public String getCurrentIP() throws APIException {
		return api.getUserIP();
	}

}
