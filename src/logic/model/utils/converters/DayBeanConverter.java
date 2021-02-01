package logic.model.utils.converters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.bean.DayBean;
import logic.model.Day;
import logic.model.Location;
import logic.persistence.dao.ActivityDao;
import logic.persistence.dao.DayDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class DayBeanConverter implements BeanConverter<Day, DayBean> {
	
	private String trip;
	
	public DayBeanConverter(String tripTitle) {
		this.trip = tripTitle;
	}

	@Override
	public DayBean convertToBean(Day o) {
		DayBean dayBean = new DayBean();
		dayBean.setLocationCity(o.getLocation().getCity());
		dayBean.setLocationCountry(o.getLocation().getCountry());
		return dayBean;
	}

	@Override
	public Day convertFromBean(DayBean o) throws DatabaseException {
		Day day = new Day();
		Location loc = new Location(o.getLocationCity(), o.getLocationCountry(), null);
		day.setLocation(loc);
		day.calclulateBudget();
		return day;
	}

	@Override
	public List<DayBean> convertToListBean(List<Day> list) throws DatabaseException {
		List<DayBean> dayBeans = new ArrayList<>();
		ActivityBeanConverter activityConverter;
		for (int i = 0; i < list.size(); i++) {
			int dayNum = i+1;
			DayBean dayBean = convertToBean(list.get(i));
			activityConverter = new ActivityBeanConverter(trip, dayNum);
			try {
				dayBean.setActivities(activityConverter.convertToListBean(ActivityDao.getInstance().getActivitiesByTrip(this.trip, dayNum)));
				dayBeans.add(dayBean);
			} catch (DBConnectionException | SQLException e) {
				throw new DatabaseException(e.getMessage(), e.getCause());
			}
		}
		return dayBeans;
	}

	@Override
	public List<Day> convertFromListBean(List<DayBean> list) throws DatabaseException {
		List<Day> days = new ArrayList<>();
		ActivityBeanConverter activityConverter;
		for (int i = 0; i < list.size(); i++) {
			Day day = convertFromBean(list.get(i));
			day.setId(i+1);
			activityConverter = new ActivityBeanConverter(trip, day.getId());
			// save day to persistence
			try {
				DayDao.getInstance().saveDay(day, trip);
				day.setActivities(activityConverter.convertFromListBean(list.get(i).getActivities()));
				days.add(day);
			} catch (DBConnectionException | SQLException e) {
				throw new DatabaseException(e.getMessage(), e.getCause());
			}
		}
		return days;
	}

}
