package logic.model.interfaces;

import logic.model.exceptions.LocationNotFoundException;

public interface PositionFinder {
	
	public String getUserPosition(String ipAddress) throws LocationNotFoundException;
	
}
