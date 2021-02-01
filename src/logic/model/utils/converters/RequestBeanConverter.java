package logic.model.utils.converters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.bean.RequestBean;
import logic.model.Request;
import logic.persistence.dao.TripDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class RequestBeanConverter implements BeanConverter<Request, RequestBean> {


	@Override
	public RequestBean convertToBean(Request o) {
		RequestBean bean = new RequestBean();
		Request req = o;
		bean.setTripTitle(req.getTarget().getTitle());
		bean.setReceiverEmail(req.getTarget().getOrganizer().getEmail());
		bean.setReceiverName(req.getTarget().getOrganizer().getName());
		bean.setReceiverSurname(req.getTarget().getOrganizer().getSurname());
		bean.setSenderEmail(o.getSender().getEmail());
		bean.setSenderName(o.getSender().getName());
		bean.setSenderSurname(o.getSender().getSurname());
		bean.setSenderAge(o.getSender().calculateUserAge());
		return bean;
	}

	@Override
	public Request convertFromBean(RequestBean o) throws DatabaseException {
		RequestBean bean = o;
		Request req = new Request();
		try {
			req.setTarget(TripDao.getInstance().getTripByTitle(bean.getTripTitle()));
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
		return req;
	}

	@Override
	public List<RequestBean> convertToListBean(List<Request> list) {
		List<RequestBean> requestBeans = new ArrayList<>();
		for (Request o: list) {
			RequestBean bean = convertToBean(o);
			requestBeans.add(bean);
		}
		return requestBeans;
	}

	@Override
	public List<Request> convertFromListBean(List<RequestBean> list) throws DatabaseException {
		List<Request> requests = new ArrayList<>();
		for (RequestBean bean: list) {
			Request request = convertFromBean(bean);
			requests.add(request);
		}
		return requests;
	}

}
