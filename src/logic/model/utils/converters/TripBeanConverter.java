package logic.model.utils.converters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.bean.TripBean;
import logic.control.FormatManager;
import logic.model.Day;
import logic.model.Trip;
import logic.persistence.dao.ActivityDao;
import logic.persistence.dao.DayDao;
import logic.persistence.dao.TripDao;
import logic.persistence.dao.UserDaoDB;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class TripBeanConverter implements BeanConverter<Trip,TripBean> {
	
	private UserBeanConverter userConverter;
	
	public TripBeanConverter() {
		this.userConverter = new UserBeanConverter();
	}
	
	@Override
	public TripBean convertToBean(Trip o) throws DatabaseException {
		TripBean bean = new TripBean();
		DayBeanConverter dayConverter = new DayBeanConverter(o.getTitle());
		try {
			bean.setDays(dayConverter.convertToListBean(DayDao.getInstance().getTripDays(o.getTitle())));
			bean.setOrganizer(userConverter.convertToBean(UserDaoDB.getInstance().getTripOrganizer(o.getTitle())));
			bean.setParticipants(userConverter.convertToListBean(UserDaoDB.getInstance().getTripParticipants(o.getTitle())));
			bean.setDescription(o.getDescription());
			bean.setTitle(o.getTitle());
			bean.setPrice(o.getPrice());
			bean.setTicketPrice(o.getTicketPrice());
			bean.setCategory1(o.getCategory1().toString());
			bean.setCategory2(o.getCategory2().toString());
			bean.setShared(o.isShared());
			bean.setAvailability(o.getAvailableSpots());
			bean.setMinAge(Integer.toString(o.getMinAge()));
			bean.setMaxAge(Integer.toString(o.getMaxAge()));
			bean.setMaxParticipants(Integer.toString(o.getMaxParticipants()));
			
			// Converting Dates to String
			String depDateStr = FormatManager.formatDate(o.getDepartureDate());
			String retDateStr = FormatManager.formatDate(o.getReturnDate());
			bean.setDepartureDate(depDateStr);
			bean.setReturnDate(retDateStr);
			return bean;
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public Trip convertFromBean(TripBean o) throws DatabaseException {
		Trip trip;
		try {
			trip = TripDao.getInstance().getTripByTitle(o.getTitle());
			trip.setDays(DayDao.getInstance().getTripDays(trip.getTitle()));
			for (Day d: trip.getDays()) {
				d.setActivities(ActivityDao.getInstance().getActivitiesByTrip(trip.getTitle(), d.getId()));
			}
			trip.setOrganizer(UserDaoDB.getInstance().getTripOrganizer(trip.getTitle()));
			return trip;
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public List<TripBean> convertToListBean(List<Trip> list) throws DatabaseException {
		List<TripBean> tripBeans = new ArrayList<>();
		for (int i=0; i<list.size(); i++) {
			Trip t = list.get(i);
			TripBean bean = convertToBean(t);
			tripBeans.add(bean);
		}
		return tripBeans;
	}

	@Override
	public List<Trip> convertFromListBean(List<TripBean> list) throws DatabaseException {
		List<Trip> trips = new ArrayList<>();
		for (TripBean bean: list) {
			Trip trip = convertFromBean(bean);
			trips.add(trip);
		}
		return trips;
	}



}
