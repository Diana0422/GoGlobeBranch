package logic.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import logic.bean.ActivityBean;
import logic.bean.DayBean;
import logic.bean.TripBean;
import logic.persistence.exceptions.DatabaseException;
import logic.model.Day;
import logic.model.Location;
import logic.model.PlaceBean;
import logic.model.Trip;
import logic.model.User;
import logic.model.exceptions.APIException;
import logic.model.factories.HereAdapterFactory;
import logic.model.factories.TripFactory;
import logic.model.interfaces.LocationFinder;
import logic.model.utils.converters.DayBeanConverter;

public class PlanTripController {
	
	
	public long calculateTripLength(Date depDate, Date retDate) {
		long diff = retDate.getTime() - depDate.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	
	public boolean addActivity(TripBean tripBean, int planningDay, ActivityBean newActivityBean) {
		
		DayBean currentDayBean = tripBean.getDays().get(planningDay);		
		List<ActivityBean> activities = currentDayBean.getActivities();
		activities.add(newActivityBean);
		currentDayBean.setActivities(activities);
		
		return true;
	}
	
	public void addDays(TripBean tripBean) {
		List<DayBean> newDaysList = new ArrayList<>();
		for (int i = 0; i< tripBean.getTripLength(); i++) {
			DayBean newDay = new DayBean();
			newDay.setActivities(new ArrayList<>());
			newDaysList.add(newDay);
		}	
		tripBean.setDays(newDaysList);
	}
	
	
	public boolean saveTrip(TripBean tripBean, String orgEmail) throws DatabaseException{
		Trip trip = TripFactory.getInstance().createModel();
		trip.setOrganizer(User.getUserByEmail(orgEmail));
		trip.setTitle(tripBean.getTitle());
		trip.setCountry(tripBean.getCountry());
			
		//parsing and setting categories
		trip.setCategory1(FormatManager.parseTripCategory(tripBean.getCategory1()));
		trip.setCategory2(FormatManager.parseTripCategory(tripBean.getCategory2()));
			
		//Converting dates
		Date depDate = FormatManager.parseDate(tripBean.getDepartureDate()); 
		Date retDate = FormatManager.parseDate(tripBean.getReturnDate());
	
		//Setting dates
		trip.setDepartureDate(depDate);
		trip.setReturnDate(retDate);
		Logger.getGlobal().info("departure date: " + trip.getDepartureDate());
		Logger.getGlobal().info("return date: " + trip.getReturnDate());
			
		// Setting shared trip preferences
		if (tripBean.isShared()) {
			trip.setShared(true);
			trip.setDescription(tripBean.getDescription());
			trip.setMinAge(Integer.parseInt(tripBean.getMinAge()));
			trip.setMaxAge(Integer.parseInt(tripBean.getMaxAge()));
			trip.setMaxParticipants(Integer.parseInt(tripBean.getMaxParticipants()));
		}	
				
		/* save trip on persistence */
		if (!trip.storeTrip()) return false;
			
		/* update the user traveling attitude */
		trip.getOrganizer().recalculateAttitude(trip.getCategory1(), trip.getCategory2());
		
		/*Converting and setting Days list (and activities)*/
		List<Day> days;
		DayBeanConverter dayConverter = new DayBeanConverter(trip.getTitle());
		if ((days = dayConverter.convertFromListBean(tripBean.getDays())) != null) {
			trip.setDays(days); 
			return true;
		}
		return false;
	}
	
	public boolean checkLocationValidity(String locationName, TripBean bean) throws APIException {
		LocationFinder adapterAPI = HereAdapterFactory.getInstance().createHereAdapter();
		Location dayLocation;
		try {
			dayLocation = adapterAPI.getLocationInfo(locationName);
			dayLocation.setCountry(dayLocation.getCountry().replace("ú", "ù"));
			return bean.getCountry().equals(dayLocation.getCountry());
		} catch (APIException e) {
			throw new APIException(e, "This location doesn't exist.");
		}
	}
	
	//Use HereAPI to get nearby places suggestions
	public List<PlaceBean> getNearbyPlaces(String locationName, String category) throws APIException{
		LocationFinder adapterAPI = HereAdapterFactory.getInstance().createHereAdapter();
		Location dayLocation = adapterAPI.getLocationInfo(locationName);
		return adapterAPI.getNearbyPlaces(dayLocation.getCoordinates(), category);		
	}
	

}







