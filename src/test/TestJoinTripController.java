package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import logic.bean.SessionBean;
import logic.bean.TripBean;
import logic.control.JoinTripController;
import logic.control.LoginController;
import logic.model.User;
import logic.model.exceptions.UnloggedException;
import logic.persistence.dao.RequestDao;
import logic.persistence.dao.UserDaoDB;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

/**
 * @author diana pasquali
 */

@TestMethodOrder(OrderAnnotation.class)
public class TestJoinTripController {
	
	private static final String TRIP_TITLE = "TestViaggio";
	private static final String ORGANIZER_ID = "organizer@gmail.com";
	private static final String NOT_ORGANIZER_ID = "notorganizer@gmail.com";
	private static final String PARTICIPANT = "participant@gmail.com";
	private JoinTripController controller;
	
	public TestJoinTripController() {
		this.controller = new JoinTripController();
	}

	/* SEARCH TRIPS TEST */
	@Test
	@Order(1)
	void testSearchTripsSuccessful() {
		String value = TRIP_TITLE;
		try {
			List<TripBean> trips = controller.searchTrips(value);
			TripBean onlyTrip = trips.get(0);
			assertEquals(value, onlyTrip.getTitle());
		} catch (DatabaseException e) {
			String logStr = "testSearchTripsSuccessful() SHOULD NOT throw exception "+e;
			Logger.getGlobal().log(Level.INFO, logStr);
		}
	}
	
	@Test
	@Order(2)
	void testSearchTripsNoResult() {
		// No trips are matching the searched value
		String value = null;
		List<TripBean> empty = null;
		try {
			empty = controller.searchTrips(value);
			String logStr = "Trips search result for value ="+value+"is ---"+empty;
			Logger.getGlobal().log(Level.INFO, logStr);
			assertEquals(false, empty.isEmpty());
		} catch (DatabaseException e) {
			String logStr = "testSearchTripsNoResults() SHOULD NOT THROW exception "+e;
			Logger.getGlobal().log(Level.INFO, logStr);
		}
	}
	
	/* SEND REQUEST TESTS*/
	@Test
	@Order(3)
	void testSendRequestNotSuccessfulUnloggedUser() {
		// If the user isn't logged
		String tripTitle = TRIP_TITLE;
		String userEmail = PARTICIPANT;
		TripBean trip = new TripBean();
		trip.setTitle(tripTitle);
		SessionBean session = new SessionBean();
		session.setSessionEmail(userEmail);
		
		try {
			boolean result = controller.sendRequest(trip.getTitle(), session.getSessionEmail());
			assertEquals(false, result);
		} catch (DatabaseException | UnloggedException e) {
			String logStr = "testSendRequestNotSuccessfulUnloggedUser() threw exception "+e;
			Logger.getGlobal().log(Level.INFO, logStr);
		}
		
	}
	
	@Test
	@Order(4)
	void testJoinTripNotSuccessfulAgeNotInRange() {
		// If the user age is not in trip's range
		String title = TRIP_TITLE;
		String email = NOT_ORGANIZER_ID;
		TripBean trip = new TripBean();
		trip.setTitle(title);
		SessionBean userSession = new SessionBean();
		userSession.setSessionEmail(email);
		
		try {
			boolean result = controller.sendRequest(trip.getTitle(), userSession.getSessionEmail());
			assertEquals(false, result);
		} catch (DatabaseException | UnloggedException e) {
			String logStr = "No exception has occurred during testJoinTripNotSuccessfulAgeNotInRange";
			Logger.getGlobal().log(Level.INFO, logStr);
		}
	}
	
	@Test
	@Order(5)
	void testSendRequestNotSuccessfulUserOrganizer() {
		// If the user is the organizer
		String tripTitle = TRIP_TITLE;
		String userEmail = ORGANIZER_ID;
		TripBean trip = new TripBean();
		trip.setTitle(tripTitle);
		SessionBean session = new SessionBean();
		session.setSessionEmail(userEmail);
		
		try {
			boolean result = controller.sendRequest(trip.getTitle(), session.getSessionEmail());
			assertEquals(false, result);
		} catch (DatabaseException | UnloggedException e) {
			String logStr = "No exception has occurred during testJoinTripNotSuccessfulUserOganizer";
			Logger.getGlobal().log(Level.INFO, logStr);
		}
	}
	
	@Test
	@Order(6)
	void testSendRequestSuccessful() {
		// If the user isn't the organizer and its age is in the accepted age range
		String tripTitle = TRIP_TITLE;
		String userEmail = PARTICIPANT;
		TripBean trip = new TripBean();
		trip.setTitle(tripTitle);
		try {
			/* User is logged */
			User logged = UserDaoDB.getInstance().get(userEmail);
			LoginController.getInstance().login(logged.getEmail(), logged.getPassword());
			/* Delete if another request exists */
			if (RequestDao.getInstance().getRequest(userEmail, tripTitle) != null) RequestDao.getInstance().delete(tripTitle, userEmail);
			boolean result = controller.sendRequest(trip.getTitle(), logged.getEmail());
			assertEquals(true, result);
		} catch (DatabaseException | UnloggedException | DBConnectionException | SQLException e) {
			String logStr = "testSendRequestSuccessful() SHOULD NOT THROW exception "+e;
			Logger.getGlobal().log(Level.INFO, logStr);
		}
		
	}
	
	@Test
	@Order(7)
	void testSendRequestNotSuccessfulRequestAlreadySent() {
		// If the user already sent the request for the trip
		String tripTitle = TRIP_TITLE;
		String userEmail = PARTICIPANT;
		TripBean trip = new TripBean();
		trip.setTitle(tripTitle);
		try {
			// user is logged
			User logged = UserDaoDB.getInstance().get(userEmail);
			LoginController.getInstance().login(logged.getEmail(), logged.getPassword());
			controller.sendRequest(trip.getTitle(), logged.getEmail());
		} catch (DatabaseException | UnloggedException | DBConnectionException | SQLException e) {
			String logStr = "testSendRequestNotSuccessfulRequestAlreadySent() threw exception "+e;
			assertEquals("Cannot save request on database.", e.getMessage());
			Logger.getGlobal().log(Level.INFO, logStr);
		}
		
	}
	
	/* JOIN TRIP TESTS*/
	@Test
	void testJoinTripSuccessful() {
		// trip organizer accepted user request
		String tripTitle = TRIP_TITLE;
		String userEmail = PARTICIPANT;
		TripBean trip = new TripBean();
		trip.setTitle(tripTitle);
		try {
			boolean result = controller.joinTrip(trip.getTitle(), userEmail);
			assertEquals(true, result);
		} catch (DatabaseException e) {
			String logStr = "testSendRequestSuccessful() SHOULD NOT THROW exception "+e;
			Logger.getGlobal().log(Level.INFO, logStr);
		}
	}
}
