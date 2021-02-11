package logic.control;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.bean.FlightBean;
import logic.bean.TripBean;
import logic.model.Trip;
import logic.model.adapters.FlightFinderAdapter;
import logic.model.apis.SkyscannerAPI;
import logic.model.exceptions.APIException;
import logic.model.exceptions.FlightNotFoundException;
import logic.model.exceptions.IPNotFoundException;
import logic.model.exceptions.LocationNotFoundException;
import logic.model.utils.GeolocationPicker;
import logic.persistence.exceptions.DatabaseException;

public class FlightController {

	private String userLocation;
	private static final String ND = "N/D"; 
	
	public FlightController() {
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
	
	public void loadFlightInfo(TripBean bean) throws DatabaseException {
		bean.setFlight(new FlightBean());
		this.retrieveFlightPrice(bean);
		this.retrieveFlightDestination(bean);
		this.retrieveFlightOrigin(bean);
		this.retrieveFlightCarrier(bean);
	}
	
	private void retrieveFlightPrice(TripBean bean) throws DatabaseException {
		// Adding variable flight ticket price
		int ticketPrice = 0;
		try {
			Trip trip = Trip.getTrip(bean.getTitle());
			String destination = trip.getDays().get(0).getLocation().getCity();
			
			ticketPrice = new FlightFinderAdapter(new SkyscannerAPI()).getFlightPrice(userLocation, destination, trip.getDepartureDate());
			bean.getFlight().setPrice(ticketPrice);
		} catch (FlightNotFoundException | APIException e) {
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
			e.printStackTrace();
			bean.getFlight().setPrice(0);
		}
	}
	
	private void retrieveFlightOrigin(TripBean bean) throws DatabaseException {
		// Retrieve flight origin airport name
		String origin;
		try {
			Trip trip = Trip.getTrip(bean.getTitle()); 
			String destination = trip.getDays().get(0).getLocation().getCity();
			origin = new FlightFinderAdapter(new SkyscannerAPI()).getFlightOrigin(userLocation, destination, trip.getDepartureDate());
			bean.getFlight().setOriginAirport(origin);
		} catch (FlightNotFoundException | APIException e) {
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
			e.printStackTrace();
			bean.getFlight().setOriginAirport(ND);
		}
	}
	
	private void retrieveFlightDestination(TripBean bean) throws DatabaseException {
		// Retrieve flight destination airport name
		String destAirport;
		try {
			Trip trip = Trip.getTrip(bean.getTitle());
			String destination = trip.getDays().get(0).getLocation().getCity();
			destAirport = new FlightFinderAdapter(new SkyscannerAPI()).getFlightDestination(userLocation, destination, trip.getDepartureDate());
			bean.getFlight().setDestAirport(destAirport);
		} catch (FlightNotFoundException | APIException e) {
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
			e.printStackTrace();
			bean.getFlight().setDestAirport(ND);
		}
	}
	
	private void retrieveFlightCarrier(TripBean bean) throws DatabaseException {
		// Retrieve flight carrier name
		String carrier;
		try {
			Trip trip =Trip.getTrip(bean.getTitle());
			String destination = trip.getDays().get(0).getLocation().getCity();
			carrier =  new FlightFinderAdapter(new SkyscannerAPI()).getFlightCarrier(userLocation, destination, trip.getDepartureDate());
			bean.getFlight().setCarrier(carrier);
		} catch (FlightNotFoundException | APIException e) {
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
			e.printStackTrace();
			bean.getFlight().setCarrier(ND);
		}
	}
}
