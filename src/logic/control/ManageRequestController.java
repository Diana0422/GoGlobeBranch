package logic.control;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import logic.bean.RequestBean;
import logic.bean.UserBean;
import logic.persistence.dao.RequestDao;
import logic.persistence.dao.TripDao;
import logic.persistence.dao.UserDaoDB;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;
import logic.model.Request;
import logic.model.User;
import logic.model.utils.converters.BeanConverter;
import logic.model.utils.converters.RequestBeanConverter;
import logic.model.utils.converters.UserBeanConverter;

public class ManageRequestController {

	private static ManageRequestController instance = null;
	private static RequestBeanConverter converter = null;
	
	private ManageRequestController() {
		//empty constructor
	}
	
	public static ManageRequestController getInstance() {
		if (instance == null) {
			instance = new ManageRequestController();
			converter = new RequestBeanConverter();
		}
		
		return instance;
	} 
	
	public UserBean getSenderBean(RequestBean requestBean) throws DatabaseException {
		User sender;
		BeanConverter<User,UserBean> userConverter = new UserBeanConverter();
		try {
			sender = UserDaoDB.getInstance().get(requestBean.getSenderEmail());
			return userConverter.convertToBean(sender);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public synchronized boolean acceptRequest(RequestBean requestBean) throws DatabaseException {
		// get request from persistence
		Request req;
		try {
			req = RequestDao.getInstance().getRequest(requestBean.getSenderEmail(), requestBean.getTripTitle());
			
			// Get request sender and receiver
			User receiver = UserDaoDB.getInstance().getRequestReceiver(requestBean.getSenderEmail(), req.getTarget().getTitle());
			
			// Add sender user to trip participants
			req.getTarget().addParticipant(req.getSender());
			String logStr = "Participants: "+req.getTarget().getParticipants();
			Logger.getGlobal().info(logStr);
			
			// Update trip's participant users in persistence
			receiver.deleteIncomingRequest(req);
			req.getSender().deleteSentRequest(req);
			return TripDao.getInstance().saveParticipant(req.getSender().getEmail(), req.getTarget().getTitle()) && RequestDao.getInstance().delete(req.getTarget().getTitle(), req.getSender().getEmail());
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public synchronized boolean declineRequest(RequestBean requestBean) throws DatabaseException {
		try {
			// get request from persistence
			Request r = RequestDao.getInstance().getRequest(requestBean.getSenderEmail(), requestBean.getTripTitle());
			// Delete request
			return RequestDao.getInstance().delete(r.getTarget().getTitle(), requestBean.getSenderEmail());
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public List<RequestBean> getUserIncomingRequests(String userEmail) throws DatabaseException {
		List<Request> incRequests;
		try {
			User sessionUser = UserDaoDB.getInstance().get(userEmail);
			incRequests = RequestDao.getInstance().getRequestsByReceiver(sessionUser.getEmail());
			return converter.convertToListBean(incRequests);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	
	public List<RequestBean> getUserSentRequests(String userEmail) throws DatabaseException{
		try { 
			List<Request> sentRequests = RequestDao.getInstance().getRequestsBySender(userEmail);
			return converter.convertToListBean(sentRequests);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
}
