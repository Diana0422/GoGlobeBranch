package logic.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.bean.TripBean;
import logic.bean.UserBean;
import logic.bean.UserStatsBean;
import logic.model.Trip;
import logic.model.TripCategory;
import logic.model.User;
import logic.model.UserStats;
import logic.model.utils.converters.ReviewBeanConverter;
import logic.model.utils.converters.TripBeanConverter;
import logic.persistence.dao.ReviewDao;
import logic.persistence.dao.TripDao;
import logic.persistence.dao.UserDaoDB;
import logic.persistence.dao.UserStatsDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class ProfileController {
	
	private static ProfileController instance;

	
   public static synchronized ProfileController getInstance() {
    	if (instance == null) {
    		instance = new ProfileController();
    	}	    	
    	return instance;
    }
   
   public List<TripBean> getRecentTrips(String userEmail) throws DatabaseException {
	   List<Trip> trips;
	   User profile;
	   List<TripBean> recent = new ArrayList<>();
	   TripBeanConverter tripConverter = new TripBeanConverter();
	   try {
		   profile = UserDaoDB.getInstance().get(userEmail);
		   trips = TripDao.getInstance().getTrips();
		   for (Trip t: trips) {
			   t.setParticipants(UserDaoDB.getInstance().getTripParticipants(t.getTitle()));
			   t.setOrganizer(UserDaoDB.getInstance().getTripOrganizer(t.getTitle()));
			   if (t.getReturnDate().before(new Date()) && (t.getParticipants().contains(profile) || t.getOrganizer().getEmail().equals(profile.getEmail()))) {
				   recent.add(tripConverter.convertToBean(t));
			   }
		   }
		   return recent;
	   } catch (DBConnectionException | SQLException e) {
		   throw new DatabaseException(e.getMessage(), e.getCause());
	   }
   }
   
	public List<TripBean> getUpcomingTrips(String userEmail) throws DatabaseException {
	   List<Trip> trips;
	   User profile;
	   List<TripBean> upcoming = new ArrayList<>();
	   TripBeanConverter tripConverter = new TripBeanConverter();
	   try {
		   profile = UserDaoDB.getInstance().get(userEmail);
		   trips = TripDao.getInstance().getTrips();
		   for (Trip t: trips) {
			   t.setParticipants(UserDaoDB.getInstance().getTripParticipants(t.getTitle()));
			   t.setOrganizer(UserDaoDB.getInstance().getTripOrganizer(t.getTitle()));
			   if (t.getDepartureDate().after(new Date()) && (t.getParticipants().contains(profile) || t.getOrganizer().getEmail().equals(profile.getEmail()))) {
				   upcoming.add(tripConverter.convertToBean(t));
			   }
		   }
		   return upcoming;
	   } catch (DBConnectionException | SQLException e) {
		   throw new DatabaseException(e.getMessage(), e.getCause());
	   }
	}
	
	public List<TripBean> getMyTrips(String userEmail) throws DatabaseException {
	   List<Trip> trips;
	   User profile;
	   List<TripBean> myTrips = new ArrayList<>();
	   TripBeanConverter tripConverter = new TripBeanConverter();
	   try {
		   profile = UserDaoDB.getInstance().get(userEmail);
		   trips = TripDao.getInstance().getTrips();
		   for (Trip t: trips) {
			   t.setOrganizer(UserDaoDB.getInstance().getTripOrganizer(t.getTitle()));
			   if (t.getOrganizer().getEmail().equals(profile.getEmail())) {
				   myTrips.add(tripConverter.convertToBean(t));
			   }
		   }
		   return myTrips;
	   } catch (DBConnectionException | SQLException e) {
		   throw new DatabaseException(e.getMessage(), e.getCause());
	   }
	}
	
	public UserBean getProfileUser(String userEmail) throws DatabaseException {
		ReviewBeanConverter reviewConverter = new ReviewBeanConverter();
		try {
			User user = UserDaoDB.getInstance().get(userEmail);
			UserBean bean = new UserBean();
			bean.setEmail(user.getEmail());
			bean.setAge(user.calculateUserAge());
			bean.setBio(user.getBio());
			bean.setName(user.getName());
			bean.setSurname(user.getSurname());
			bean.setPoints(user.getStats().getPoints());
			UserStats stats = UserStatsDao.getInstance().getUserStats(userEmail);
			UserStatsBean statsBean = new UserStatsBean();
			statsBean.setOrgRating(stats.getOrganizerRating());
			statsBean.setTravRating(stats.getTravelerRating());
			bean.setStatsBean(statsBean);
			bean.setReviews(reviewConverter.convertToListBean(ReviewDao.getInstance().getUserReviews(userEmail)));
			return bean;
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public void updateUserBio (String userEmail, String newUserBio) throws DatabaseException {
		try {
			UserDaoDB.getInstance().updateUserBio(userEmail, newUserBio);
		} catch (DBConnectionException | SQLException e ) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public Map<String, Integer> getPercentageAttitude(String userEmail) throws DatabaseException {
		/* Calculate user's traveling attitude (in percentage) */
		Map<String, Integer> percAttitude = new HashMap<>();
		int total = 0;
		int percent = 0;
		// Find user attitude
		User user;
		try {
			user = UserDaoDB.getInstance().get(userEmail);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
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
