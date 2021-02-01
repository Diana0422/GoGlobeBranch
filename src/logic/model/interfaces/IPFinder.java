package logic.model.interfaces;

import logic.model.exceptions.APIException;

public interface IPFinder {
	
	public String getCurrentIP() throws APIException;

}
