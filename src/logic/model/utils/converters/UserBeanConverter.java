package logic.model.utils.converters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.bean.UserBean;
import logic.bean.UserStatsBean;
import logic.model.User;
import logic.persistence.dao.ReviewDao;
import logic.persistence.dao.UserDaoDB;
import logic.persistence.dao.UserStatsDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class UserBeanConverter implements BeanConverter<User,UserBean> {
	
	private ReviewBeanConverter reviewConverter;
	
	public UserBeanConverter() {
		this.reviewConverter = new ReviewBeanConverter();
	}

	@Override
	public UserBean convertToBean(User o) throws DatabaseException {
		try {
			UserBean bean = new UserBean();
			UserStatsBean statsBean = new UserStatsBean();
			bean.setEmail(o.getEmail());
			bean.setName(o.getName());
			bean.setSurname(o.getSurname());
			bean.setBio(o.getBio());
			bean.setPoints(o.getStats().getPoints());
			bean.setAge(o.calculateUserAge());
			bean.setReviews(reviewConverter.convertToListBean(ReviewDao.getInstance().getUserReviews(o.getEmail())));
			
			bean.setStatsBean(statsBean);
			bean.getStatsBean().setOrgRating(UserStatsDao.getInstance().getUserStats(o.getEmail()).getOrganizerRating());
			bean.getStatsBean().setTravRating(UserStatsDao.getInstance().getUserStats(o.getEmail()).getTravelerRating());
			return bean;
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public User convertFromBean(UserBean o) throws DatabaseException {
		try {
			return UserDaoDB.getInstance().get(o.getEmail());
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public List<UserBean> convertToListBean(List<User> list) throws DatabaseException {
		List<UserBean> beans = new ArrayList<>();
		for (User user: list) {
			UserBean bean = convertToBean(user);
			beans.add(bean);
		}
		return beans;
	}

	@Override
	public List<User> convertFromListBean(List<UserBean> list) throws DatabaseException {
		List<User> users = new ArrayList<>();
		for (UserBean bean: list) {
			User user = convertFromBean(bean);
			users.add(user);
		}
		return users;
	}



}
