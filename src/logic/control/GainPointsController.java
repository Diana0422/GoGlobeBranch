package logic.control;

import java.util.Date;
import java.util.List;
import logic.bean.TripBean;
import logic.bean.UserBean;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Cookie;
import logic.util.Session;
import logic.model.Trip;
import logic.model.User;
import logic.model.utils.converters.TripBeanConverter;
import logic.model.utils.converters.UserBeanConverter;

public class GainPointsController {
	
	
	public TripBean getTripOfTheDay(String userEmail) throws DatabaseException {
		Date today = new Date();
		UserBeanConverter userConverter = new UserBeanConverter();
		TripBeanConverter tripConverter = new TripBeanConverter();
		UserBean logged;
		try {
			logged = userConverter.convertToBean(User.getUserByEmail(userEmail));
			List<TripBean> list = tripConverter.convertToListBean(Trip.getTrips(false, null));
			
			for (TripBean bean: list) {
				Date dep = FormatManager.parseDate(bean.getDepartureDate());
				Date ret = FormatManager.parseDate(bean.getReturnDate());
				if (today.after(dep) && today.before(ret)) {
					if (bean.getOrganizer().getEmail().equals(logged.getEmail())) return bean;
					
					if (isParticipant(logged.getEmail(), bean.getParticipants())) return bean;
				}
			}
			return null;
		} catch (DatabaseException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
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
		ParticipationController partController = new ParticipationController(); 
		Trip trip = tripConverter.convertFromBean(tripBean);
		if (partController.checkParticipation(trip)) {
			User user;
			user = User.getUserByEmail(sessionEmail);
			user.addPoints(100);
			Session session = Cookie.getInstance().getSession(sessionEmail);
			session.setUserPoints(user.getStats().getPoints());
			user.updateStats();
			return true;
		} else {
			return false;
		}
	}
	
}
