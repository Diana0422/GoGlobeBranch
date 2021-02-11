package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

import logic.bean.ActivityBean;
import logic.bean.SessionBean;
import logic.bean.TripBean;
import logic.control.FormatManager;
import logic.control.PlanTripController;
import logic.model.PlaceBean;
import logic.model.exceptions.APIException;
import logic.persistence.exceptions.DatabaseException;

/**
 * @author lorenzo tanzi
 */

class TestPlanTripController {
	
	private static final String CATEGORY1 = "ADVENTURE";
	private static final String CATEGORY2 = "ADVENTURE";
	private static final String ORGANIZER_ID = "organizer@gmail.com";
	private static final String LOCATION_NAME = "NewYork";
	private static final String DEP_DATE = "16/03/2021";
	private static final String RET_DATE = "16/03/2021";

	
	PlanTripController controller;
	
	public TestPlanTripController() {
		controller = new PlanTripController();
	}

	@Test
	void testGetNearbyPlaces(){
		
		try {
			List<PlaceBean> places = controller.getNearbyPlaces(LOCATION_NAME, CATEGORY1);
			assertEquals(false, places.isEmpty());
		} catch (APIException e) {
			String logStr = "testGetNearbyPlaces() SHOULD NOT throw exception "+e;
			Logger.getGlobal().log(Level.INFO, logStr);
		}
	}
	
	@Test 
	void testCalculateTripLengthSuccessful() {
		Date depDate = FormatManager.parseDate(DEP_DATE);
		Date retDate = FormatManager.parseDate(RET_DATE);
		int length = (int)controller.calculateTripLength(depDate, retDate);
		assertEquals(0, length);	
	}
	
	@Test
	void testSaveTripSuccessful() {
		TripBean tripBean = new TripBean();
		tripBean.setTitle("Viaggio");
		tripBean.setCategory1(CATEGORY1);
		tripBean.setCategory2(CATEGORY2);
		tripBean.setDepartureDate(DEP_DATE);
		tripBean.setReturnDate(RET_DATE);
		tripBean.setTripLength(1);
		controller.addDays(tripBean);
		tripBean.getDays().get(0).setLocationCity("Roma");
		tripBean.getDays().get(0).setBudget(267);
		tripBean.getDays().get(0).setLocationCountry("Italia");
		ActivityBean activityBean= new ActivityBean();
		activityBean.setTitle("Ritorno a Merano");
		activityBean.setTime("16:00");
		activityBean.setEstimatedCost("267");
		activityBean.setDescription("Terminata la visita prenderemo il treno sulla banchina 2.");
		controller.addActivity(tripBean, 0, activityBean);
		tripBean.setShared(false);
		SessionBean session = new SessionBean();
		session.setSessionEmail(ORGANIZER_ID);
		try {
			boolean b = controller.saveTrip(tripBean, session.getSessionEmail());
			assertEquals(true, b);
		} catch (DatabaseException e) {			
			String logStr = "testGetNearbyPlaces() SHOULD NOT throw exception "+e;
			Logger.getGlobal().log(Level.INFO, logStr);
		}		
	}
	
}
