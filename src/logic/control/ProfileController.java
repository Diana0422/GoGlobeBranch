package logic.control;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.bean.TripBean;
import logic.bean.UserBean;
import logic.bean.UserStatsBean;
import logic.model.Review;
import logic.model.Trip;
import logic.model.TripCategory;
import logic.model.User;
import logic.model.utils.converters.ReviewBeanConverter;
import logic.model.utils.converters.TripBeanConverter;
import logic.persistence.exceptions.DatabaseException;

public class ProfileController {

   
   public List<TripBean> getRecentTrips(String userEmail) throws DatabaseException {
	   List<Trip> trips;
	   User profile;
	   List<TripBean> recent = new ArrayList<>();
	   TripBeanConverter tripConverter = new TripBeanConverter();
	   profile = User.getUserByEmail(userEmail);
	   trips = Trip.getTrips(false, null);
	   for (Trip t: trips) {
		   if (t.getReturnDate().before(new Date()) && (t.getParticipants().contains(profile) || t.getOrganizer().getEmail().equals(profile.getEmail()))) {
			   recent.add(tripConverter.convertToBean(t));
		   }
	   }
	   return recent;
   }
   
	public List<TripBean> getUpcomingTrips(String userEmail) throws DatabaseException {
	   List<Trip> trips;
	   User profile;
	   List<TripBean> upcoming = new ArrayList<>();
	   TripBeanConverter tripConverter = new TripBeanConverter();
	   profile = User.getUserByEmail(userEmail);
	   trips = Trip.getTrips(false, null);
	   for (Trip t: trips) {
		   if (t.getDepartureDate().after(new Date()) && (t.getParticipants().contains(profile) || t.getOrganizer().getEmail().equals(profile.getEmail()))) {
			   upcoming.add(tripConverter.convertToBean(t));
		   }
	   }
	   return upcoming;
	}
	
	public List<TripBean> getMyTrips(String userEmail) throws DatabaseException {
	   List<Trip> trips;
	   User profile;
	   List<TripBean> myTrips = new ArrayList<>();
	   TripBeanConverter tripConverter = new TripBeanConverter();
	   profile = User.getUserByEmail(userEmail);
	   trips = Trip.getTrips(false, null);
	   for (Trip t: trips) {
		   if (t.getOrganizer().getEmail().equals(profile.getEmail())) {
			   myTrips.add(tripConverter.convertToBean(t));
		   }
	   }
	   return myTrips;
	}
	
	public UserBean getProfileUser(String userEmail) throws DatabaseException {
		ReviewBeanConverter reviewConverter = new ReviewBeanConverter();
		
		User user = User.getUserByEmail(userEmail);
		UserBean bean = new UserBean();
		bean.setEmail(user.getEmail());
		bean.setAge(user.calculateUserAge());
		bean.setBio(user.getBio());
		bean.setName(user.getName());
		bean.setSurname(user.getSurname());
		bean.setPoints(user.getStats().getPoints());
		UserStatsBean statsBean = new UserStatsBean();
		statsBean.setOrgRating(user.getStats().getOrganizerRating());
		statsBean.setTravRating(user.getStats().getTravelerRating());
		bean.setStatsBean(statsBean);
		bean.setReviews(reviewConverter.convertToListBean(Review.getReviewsByUser(userEmail)));
		return bean;
	}
	
	public void updateUserBio (String userEmail, String newUserBio) throws DatabaseException {
		User user = User.getUserByEmail(userEmail);
		user.updateUserInfo(null, null, newUserBio);
	}
	
	public Map<String, Integer> getPercentageAttitude(String userEmail) throws DatabaseException {
		/* Calculate user's traveling attitude (in percentage) */
		Map<String, Integer> percAttitude = new HashMap<>();
		int total = 0;
		int percent = 0;
		// Find user attitude
		User user = User.getUserByEmail(userEmail);
		Map<TripCategory, Integer> attitude = user.getAttitude();
		for (Map.Entry<TripCategory, Integer> entry: attitude.entrySet()) {
			total += entry.getValue();
		}

		for (Map.Entry<TripCategory, Integer> entry: attitude.entrySet()) {
			if (total != 0) {
				percent = entry.getValue()*100/total;
			} else {
				percent = 0;
			}
			percAttitude.putIfAbsent(entry.getKey().toString(), percent);
		}

		
		return percAttitude;
	}
}
