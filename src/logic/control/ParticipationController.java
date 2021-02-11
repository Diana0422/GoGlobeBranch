package logic.control;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.model.Day;
import logic.model.Trip;
import logic.model.exceptions.IPNotFoundException;
import logic.model.exceptions.LocationNotFoundException;
import logic.model.utils.GeolocationPicker;

public class ParticipationController {
	
	private Date today;
	
	public ParticipationController() {
		today = new Date();
	}
	
	public boolean checkParticipation(Trip trip) {
		String userIP = null;
		String userPos = null;
		List<Day> days = trip.getDays();
		Date departure = trip.getDepartureDate();
		Date returnDate = trip.getReturnDate();
		
		// Get user position
		try {
			userIP = GeolocationPicker.getInstance().forwardIPRequestToAPI();
		} catch (IPNotFoundException e) {
			userIP = "N/D";
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
		}
		try {
			userPos = GeolocationPicker.getInstance().forwardLocationRequestToAPI(userIP);
		} catch (LocationNotFoundException e) {
			userPos = "N/D";
			Logger.getGlobal().log(Level.WARNING, e.getMessage());
		}
		Logger.getGlobal().info(userPos);
		
		
		for (Day day: days) {
			if (today.after(departure) && today.before(returnDate)) {
				if (userPos.equals(day.getLocation().getCity())) {
					Logger.getGlobal().log(Level.INFO, "VALID PARTICIPATION. You are participating to this trip.");
					return true;
				} else {
					Logger.getGlobal().log(Level.INFO, "You are not participating to this trip.");
					return false;
				}
			}
		}
		
		Logger.getGlobal().log(Level.SEVERE, "This trip is not happening today.");
		return false;	
	}
}
