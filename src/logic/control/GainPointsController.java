package logic.control;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import logic.bean.TripBean;
import logic.bean.UserBean;
import logic.persistence.dao.TripDao;
import logic.persistence.dao.UserDaoDB;
import logic.persistence.dao.UserStatsDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;
import logic.model.Trip;
import logic.model.User;
import logic.model.utils.converters.TripBeanConverter;
import logic.model.utils.converters.UserBeanConverter;

public class GainPointsController {
	
	private static GainPointsController instance = null;
	
	private GainPointsController() {
		//empty constructor for singleton
	}
	
	public static GainPointsController getInstance() {
		if (instance == null) {
			instance = new GainPointsController();
		}
		
		return instance;
	}
	
	
	public TripBean getTripOfTheDay(String userEmail) throws DatabaseException {
		Date today = new Date();
		UserBeanConverter userConverter = new UserBeanConverter();
		TripBeanConverter tripConverter = new TripBeanConverter();
		UserBean logged;
		try {
			logged = userConverter.convertToBean(UserDaoDB.getInstance().get(userEmail));
			List<TripBean> list = tripConverter.convertToListBean(TripDao.getInstance().getTrips());
			
			for (TripBean bean: list) {
				Date dep = FormatManager.parseDate(bean.getDepartureDate());
				Date ret = FormatManager.parseDate(bean.getReturnDate());
				System.out.println("here");
				System.out.println(bean.getOrganizer().getEmail());
				System.out.println(userEmail);
				if (today.after(dep) && today.before(ret)) {
					System.out.println("user participating to:");
					if (bean.getOrganizer().getEmail().equals(logged.getEmail())) return bean;
					
					if (isParticipant(logged.getEmail(), bean.getParticipants())) return bean;
				}
			}
			System.out.println("No trip is happening today.");
			return null;
		} catch (DatabaseException | DBConnectionException | SQLException e1) {
			throw new DatabaseException(e1.getMessage(), e1.getCause());
		}
	}
	
	private boolean isParticipant(String email, List<UserBean> participants) {
		for (UserBean user: participants) {
			if (user.getEmail().equals(email)) return true;
		}
		return false;
	}

	public boolean verifyParticipation(String sessionEmail, TripBean tripBean) throws DatabaseException {
		TripBeanConverter tripConverter = new TripBeanConverter();
		Trip trip = tripConverter.convertFromBean(tripBean);
		try {
			if (ParticipationController.getInstance().checkParticipation(trip)) {
				User user;
				user = UserDaoDB.getInstance().get(sessionEmail);
				user.addPoints(100);
				UserStatsDao.getInstance().updateStats(sessionEmail, user.getStats().getPoints(), user.getStats().getOrganizerRating(), user.getStats().getTravelerRating());
				return true;
			} else {
				return false;
			}
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
}
