package logic.model.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import logic.model.apis.SkyscannerAPI;
import logic.model.exceptions.APIException;
import logic.model.exceptions.FlightNotFoundException;
import logic.model.interfaces.FlightFinder;

public class FlightFinderAdapter implements FlightFinder {
	
	private SkyscannerAPI api;
	
	public FlightFinderAdapter(SkyscannerAPI api) {
		this.api = api;
	}

	@Override
	public String getFlightOrigin(String userLocation, String destination, Date depDate) throws FlightNotFoundException, APIException {
	
		String origID = api.getCityId(userLocation, formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR");
		String destID = api.getCityId(destination, formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR");
		
		return api.getOrigin(api.getCheapestFlight(origID, destID, formatDate(depDate), formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR"));
 	}

	@Override
	public String getFlightDestination(String userLocation, String destination, Date depDate) throws FlightNotFoundException, APIException {
		
		String origID = api.getCityId(userLocation, formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR");
		String destID = api.getCityId(destination, formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR");
		
		return api.getDestination(api.getCheapestFlight(origID, destID, formatDate(depDate), formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR"));
	}

	@Override
	public String getFlightCarrier(String userLocation, String destination, Date depDate) throws FlightNotFoundException, APIException {
		
		String origID = api.getCityId(userLocation, formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR");
		String destID = api.getCityId(destination, formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR");
		
		return api.getCarrierName(api.getCheapestFlight(origID, destID, formatDate(depDate), formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR"));
	}

	@Override
	public int getFlightPrice(String userLocation, String destination, Date depDate) throws FlightNotFoundException, APIException {
		
		String origID = api.getCityId(userLocation, formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR");
		String destID = api.getCityId(destination, formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR");
		
		
		return api.getPrice(api.getCheapestFlight(origID, destID, formatDate(depDate), formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), "EUR"));
	}
	
	private String formatDate(Date date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	
	private String formatLocale(Locale userLocale) {
		return userLocale.getLanguage()+"-"+userLocale.getCountry();
	}
	
	private String formatCountry(Locale userLocale) {
		return userLocale.getCountry();
	}

}
