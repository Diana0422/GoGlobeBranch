package logic.control;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.bean.TripBean;
import logic.model.Trip;
import logic.model.adapters.FlightFinderAdapter;
import logic.model.apis.SkyscannerAPI;
import logic.model.exceptions.APIException;
import logic.model.exceptions.FlightNotFoundException;
import logic.model.exceptions.IPNotFoundException;
import logic.model.exceptions.LocationNotFoundException;
import logic.model.utils.GeolocationPicker;
import logic.persistence.dao.DayDao;
import logic.persistence.dao.TripDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class FlightController {

	private static FlightController instance = null;
	private static String userLocation;
	private static final String ND = "N/D"; 
	
	private FlightController() {
		//empty
	}
	
	public static FlightController getInstance() {
		if (instance == null) {
			instance = new FlightController();
			String userIP = null;
			try {
				userIP = GeolocationPicker.getInstance().forwardIPRequestToAPI();
			} catch (IPNotFoundException e) {
				Logger.getGlobal().log(Level.WARNING, e.getMessage());
				userIP = ND;
			}
			try {
				userLocation = GeolocationPicker.getInstance().forwardLocationRequestToAPI(userIP);
			} catch (LocationNotFoundException e) {
				Logger.getGlobal().log(Level.WARNING, e.getMessage());
				userLocation = ND;
			}
		}
		
		return instance;
	}
	
	public int retrieveFlightPrice(TripBean bean) throws DatabaseException {
		// Adding variable flight ticket price
		Trip trip = null;
		int ticketPrice = 0;
		//get trip
		try {
			trip = TripDao.getInstance().getTripByTitle(bean.getTitle());
			trip.setDays(DayDao.getInstance().getTripDays(trip.getTitle()));
			String destination = trip.getDays().get(0).getLocation().getCity();
			
			ticketPrice = new FlightFinderAdapter(new SkyscannerAPI()).getFlightPrice(userLocation, destination, trip.getDepartureDate());
			trip.setTicketPrice(ticketPrice);
		} catch (DBConnectionException | SQLException e1) {
			throw new DatabaseException(e1.getMessage(), e1.getCause());
		} catch (FlightNotFoundException | APIException e) {
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
			e.printStackTrace();
			ticketPrice = 0;
		}

		return ticketPrice;
	}
	
	public String retrieveFlightOrigin(TripBean bean) throws DatabaseException {
		// Retrieve flight origin airport name
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(bean.getTitle()); 
			trip.setDays(DayDao.getInstance().getTripDays(trip.getTitle()));
			String destination = trip.getDays().get(0).getLocation().getCity();
			return new FlightFinderAdapter(new SkyscannerAPI()).getFlightOrigin(userLocation, destination, trip.getDepartureDate());
		} catch (FlightNotFoundException | APIException e) {
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
			e.printStackTrace();
			return ND;
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public String retrieveFlightDestination(TripBean bean) throws DatabaseException {
		// Retrieve flight destination airport name
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(bean.getTitle());
			trip.setDays(DayDao.getInstance().getTripDays(trip.getTitle()));
			String destination = trip.getDays().get(0).getLocation().getCity();
			return new FlightFinderAdapter(new SkyscannerAPI()).getFlightDestination(userLocation, destination, trip.getDepartureDate());
		} catch (FlightNotFoundException | APIException e) {
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
			e.printStackTrace();
			return ND;
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public String retrieveFlightCarrier(TripBean bean) throws DatabaseException {
		// Retrieve flight carrier name
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(bean.getTitle());
			trip.setDays(DayDao.getInstance().getTripDays(trip.getTitle()));
			String destination = trip.getDays().get(0).getLocation().getCity();
			return new FlightFinderAdapter(new SkyscannerAPI()).getFlightCarrier(userLocation, destination, trip.getDepartureDate());
		} catch (FlightNotFoundException | APIException e) {
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
			e.printStackTrace();
			return ND;
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
}
