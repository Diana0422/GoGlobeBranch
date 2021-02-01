package logic.model.utils.converters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.bean.ActivityBean;
import logic.model.Activity;
import logic.persistence.dao.ActivityDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class ActivityBeanConverter implements BeanConverter<Activity, ActivityBean> {
	
	private String trip;
	private int day;
	
	public ActivityBeanConverter(String tripTitle, int dayNum) {
		this.trip = tripTitle;
		this.day = dayNum;
	}

	@Override
	public ActivityBean convertToBean(Activity o) {
		String title = o.getTitle();
		String time = o.getTime();
		String description = o.getDescription();
		String cost = String.valueOf(o.getEstimatedCost());
		ActivityBean activityBean = new ActivityBean();
		activityBean.setTitle(title);
		activityBean.setTime(time);
		activityBean.setDescription(description);
		activityBean.setEstimatedCost(cost);
		return activityBean;
	}

	@Override
	public Activity convertFromBean(ActivityBean o) throws DatabaseException {
		String title = o.getTitle();
		String time = o.getTime();
		String description = o.getDescription();
		int cost = Integer.parseInt(o.getEstimatedCost());
		return new Activity(title, time, description, cost);
	}

	@Override
	public List<ActivityBean> convertToListBean(List<Activity> list) {
		List<ActivityBean> activityBeans = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			activityBeans.add(convertToBean(list.get(i)));
		}
		return activityBeans;
	}

	@Override
	public List<Activity> convertFromListBean(List<ActivityBean> list) throws DatabaseException {
		List<Activity> activities = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Activity activity = convertFromBean(list.get(i));
			activities.add(activity);
			//save the activity in persistence
			try {
				ActivityDao.getInstance().saveActivity(activity, day, trip);
			} catch (DBConnectionException | SQLException e) {
				throw new DatabaseException(e.getMessage(), e.getCause());
			}
		}
		return activities;
	}

}
