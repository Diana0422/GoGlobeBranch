package logic.model.adapters;

import logic.model.apis.IPFindIPFinderAPI;
import logic.model.exceptions.APIException;
import logic.model.interfaces.IPFinder;

public class IPFindIPFinderAdapter implements IPFinder {
	
	private IPFindIPFinderAPI ipFinderApi;
	
	public IPFindIPFinderAdapter() {
		this.ipFinderApi = new IPFindIPFinderAPI();
	}

	@Override
	public String getCurrentIP() throws APIException {
		return ipFinderApi.getUserIP();
	}

}
