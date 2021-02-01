package logic.model.interfaces;

import java.util.Date;

import logic.model.exceptions.APIException;
import logic.model.exceptions.FlightNotFoundException;

public interface FlightFinder {

	public String getFlightOrigin(String userLocation, String destination, Date depDate) throws FlightNotFoundException, APIException;
	public String getFlightDestination(String userLocation, String destination, Date depDate) throws FlightNotFoundException, APIException;
	public String getFlightCarrier(String userLocation, String destination, Date depDate) throws FlightNotFoundException, APIException;
	public int getFlightPrice(String userLocation, String destination, Date depDate) throws FlightNotFoundException, APIException;
	
}
