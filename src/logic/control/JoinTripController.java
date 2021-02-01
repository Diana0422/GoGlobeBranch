package logic.control;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import logic.bean.TripBean;
import logic.persistence.dao.RequestDao;
import logic.persistence.dao.TripDao;
import logic.persistence.dao.UserDaoDB;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Cookie;
import logic.util.Session;
import logic.model.Request;
import logic.model.Trip;
import logic.model.TripCategory;
import logic.model.User;
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
		try {
			List<Trip> trips = TripDao.getInstance().getSharedTrips();
			if (value == null) return converter.convertToListBean(trips);
			for (Trip trip: trips) {
				if (trip.getTitle().toLowerCase().contains(value.toLowerCase()) && trip.getAvailableSpots() != 0) filteredTrips.add(trip);
				
			}
			/* Convert List<Trip> into List<TripBean> */
			return converter.convertToListBean(filteredTrips);			
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}

	}
	
	public List<TripBean> getSuggestedTrips(String userEmail) throws DatabaseException {
		// Fetch user from cookie
		TripCategory favourite = null;
		Integer topValue = 0;
		try {
			if (userEmail != null) {
				Session logged = Cookie.getInstance().getSession(userEmail);
				User user = UserDaoDB.getInstance().get(logged.getUserEmail());
				
				// Get user favorite category
				Map<TripCategory, Integer> attitude = user.getAttitude();
				for (Map.Entry<TripCategory, Integer> entry: attitude.entrySet()) {
					if (topValue <= entry.getValue()) {
						topValue = entry.getValue();
						favourite = entry.getKey();
					}
				}
				
				if (favourite != null && !(TripDao.getInstance().getTripsForCategory(favourite)).isEmpty()) return converter.convertToListBean(TripDao.getInstance().getTripsForCategory(favourite));
			}
			return converter.convertToListBean(TripDao.getInstance().getSharedTrips());
		} catch (SQLException | DBConnectionException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	private boolean checkIfUserIsCompliant(String userEmail, String tripTitle) throws DatabaseException, UnloggedException {
		Trip trip;
		boolean check = true;
		try {
			if (userEmail == null) throw new UnloggedException();
			trip = TripDao.getInstance().getTripByTitle(tripTitle);
			Session logged = Cookie.getInstance().getSession(userEmail);
			User applicant = UserDaoDB.getInstance().get(logged.getUserEmail());
			if (applicant != null) {
				int userAge = applicant.calculateUserAge();
				// Fetch trip organizer from database
				trip.setOrganizer(UserDaoDB.getInstance().getTripOrganizer(tripTitle));
				
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
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	
	private boolean checkAvailableSpots(Trip trip) {
		boolean check = true;
		if (trip.getAvailableSpots() == 0) check = false;
		return check;
	}

	public boolean sendRequest(String tripTitle, String applicantEmail) throws DatabaseException, UnloggedException {
		if (checkIfUserIsCompliant(applicantEmail, tripTitle)) {
			// Instantiate a new request
			Request request = new Request();
			try {
				request.setTarget(TripDao.getInstance().getTripByTitle(tripTitle));
				request.setAccepted(false);
				Session logged = Cookie.getInstance().getSession(applicantEmail);
				User senderUser = UserDaoDB.getInstance().get(logged.getUserEmail());
				User receiverUser = request.getTarget().getOrganizer();
				senderUser.addToSentRequests(request);
				receiverUser.addToIncRequests(request);
				
				// Save the request in persistence
				return RequestDao.getInstance().save(request, applicantEmail);
			} catch (DBConnectionException | SQLException e) {
				throw new DatabaseException(e.getMessage(), e.getCause());
			}
		} else {
			return false;
		}
	}
	
	public boolean joinTrip(String tripTitle, String userEmail) throws DatabaseException{
		
		// Pick the trip corresponding to the tripBean from persistence
		Trip trip;
		User applicant;
		Session session;
		try {
			trip = TripDao.getInstance().getTripByTitle(tripTitle);
			session = Cookie.getInstance().getSession(userEmail);
			applicant = UserDaoDB.getInstance().get(session.getUserEmail());
			trip.addParticipant(applicant);
			return TripDao.getInstance().saveParticipant(userEmail, tripTitle);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}

}