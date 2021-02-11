package logic.control;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import logic.bean.TripBean;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Cookie;
import logic.util.Session;
import logic.view.filterstrategies.TripFilterManager;
import logic.model.FilterType;
import logic.model.Request;
import logic.model.Trip;
import logic.model.TripCategory;
import logic.model.User;
import logic.model.exceptions.DuplicateException;
import logic.model.exceptions.UnloggedException;
import logic.model.utils.converters.BeanConverter;
import logic.model.utils.converters.TripBeanConverter;
 
public class JoinTripController {
	
	private BeanConverter<Trip,TripBean> converter;
	
	public JoinTripController() {
		this.converter = new TripBeanConverter();
	}

	public List<TripBean> searchTrips(String value) throws DatabaseException {
		String logStr = "Search trips by value started.\n";
		Logger.getGlobal().info(logStr);
		List<Trip> filteredTrips = new ArrayList<>();
		List<Trip> trips = Trip.getTrips(true, null);
		if (value == null) return converter.convertToListBean(trips);
		for (Trip trip: trips) {
			boolean cond1 = trip.getAvailableSpots() != 0;
			boolean cond2 = trip.getTitle().toLowerCase().contains(value.toLowerCase());
			boolean cond3 = trip.getCountry().equalsIgnoreCase(value);
			boolean cond4 = !filteredTrips.contains(trip);
			if (cond1 && cond4 &&(cond2 || cond3)) {
				filteredTrips.add(trip);
			}
		}
		/* Convert List<Trip> into List<TripBean> */
		return converter.convertToListBean(filteredTrips);			
	}
	
	public List<TripBean> applyFilterToTrips(List<TripBean> trips, FilterType type, TripCategory category) {
		List<TripBean> def = new ArrayList<>();
		TripFilterManager filterManager = new TripFilterManager();
		if (category != null) {
			switch(category) {
			case ADVENTURE:
				filterManager.setAdventureFilter();
				return filterManager.filterTrips(trips);
			case CULTURE:
				filterManager.setCultureFilter();
				return filterManager.filterTrips(trips);
			case RELAX:
				filterManager.setRelaxFilter();
				return filterManager.filterTrips(trips);
			case FUN:
				filterManager.setFunFilter();
				return filterManager.filterTrips(trips);
			default:
				return def;
			}
		} else {
			switch (type) {
			case PRICE:
				filterManager.setPriceFilter();
				return filterManager.filterTrips(trips);
			case ALPHABETIC:
				filterManager.setAlphabeticFilter();
				return filterManager.filterTrips(trips);
			default:
				return def;
			}
		}
	}

	
	public List<TripBean> getSuggestedTrips(String userEmail) throws DatabaseException {
		// Fetch user from cookie
		TripCategory favourite = null;
		Integer topValue = 0;
		if (userEmail != null) {
			Session logged = Cookie.getInstance().getSession(userEmail);
			User user = User.getUserByEmail(logged.getUserEmail());
				
			// Get user favorite category
			Map<TripCategory, Integer> attitude = user.getAttitude();
			for (Map.Entry<TripCategory, Integer> entry: attitude.entrySet()) {
				if (topValue <= entry.getValue()) {
					topValue = entry.getValue();
					favourite = entry.getKey();
				}
			}
				
			if (favourite != null && !(Trip.getTrips(false, favourite)).isEmpty()) return converter.convertToListBean(Trip.getTrips(false, favourite));
		}
		return converter.convertToListBean(Trip.getTrips(true, null));
	}
	
	private boolean checkIfUserIsCompliant(String userEmail, String tripTitle) throws DatabaseException, UnloggedException {
		Trip trip;
		boolean check = true;
		if (userEmail == null) throw new UnloggedException();
		trip = Trip.getTrip(tripTitle);
		Session logged = Cookie.getInstance().getSession(userEmail);
		User applicant = User.getUserByEmail(logged.getUserEmail());
		if (applicant != null) {
			int userAge = applicant.calculateUserAge();
			// Do the checks
			check = checkAvailableSpots(trip);
			if (trip.getOrganizer().getEmail().equals(applicant.getEmail())) check = false;
			if (userAge<trip.getMinAge() || userAge>trip.getMaxAge()) check = false;
			for (User part: trip.getParticipants()) {
				if (part.getEmail().equals(applicant.getEmail())) check = false;
			}
			return check;
		} else {
			throw new UnloggedException();
		}
	}
	
	
	private boolean checkAvailableSpots(Trip trip) {
		boolean check = true;
		if (trip.getAvailableSpots() == 0) check = false;
		return check;
	}

	public boolean sendRequest(String tripTitle, String applicantEmail) throws DatabaseException, UnloggedException, DuplicateException {
		if (checkIfUserIsCompliant(applicantEmail, tripTitle)) {
			// Instantiate a new request
			Request request = new Request();
			request.setTarget(Trip.getTrip(tripTitle));
			request.setAccepted(false);
			Session logged = Cookie.getInstance().getSession(applicantEmail);
			User senderUser = User.getUserByEmail(logged.getUserEmail());
			User receiverUser = request.getTarget().getOrganizer();
			receiverUser.addToIncRequests(request);
			return senderUser.addToSentRequests(request);
		} else {
			return false;
		}
	}
	
	public boolean joinTrip(String tripTitle, String userEmail) throws DatabaseException{
		
		// Pick the trip corresponding to the tripBean from persistence
		Trip trip = Trip.getTrip(tripTitle);
		Session session = Cookie.getInstance().getSession(userEmail);
		User applicant = User.getUserByEmail(session.getUserEmail());
		return trip.addParticipant(applicant);
	}

}