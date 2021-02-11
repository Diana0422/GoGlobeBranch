package logic.control;
import java.util.List;

import logic.bean.RequestBean;
import logic.bean.UserBean;
import logic.persistence.exceptions.DatabaseException;
import logic.model.Request;
import logic.model.User;
import logic.model.utils.converters.BeanConverter;
import logic.model.utils.converters.RequestBeanConverter;
import logic.model.utils.converters.UserBeanConverter;

public class ManageRequestController {

	private RequestBeanConverter converter = null;
	
	public ManageRequestController() {
		this.converter = new RequestBeanConverter();
	}
	
	public UserBean getSenderBean(RequestBean requestBean) throws DatabaseException {
		User sender;
		BeanConverter<User,UserBean> userConverter = new UserBeanConverter();
		sender = User.getUserByEmail(requestBean.getSenderEmail());
		return userConverter.convertToBean(sender);
	}
	
	public synchronized boolean acceptRequest(RequestBean requestBean) throws DatabaseException {
		// get request from persistence
		Request req = Request.getRequest(requestBean.getSenderEmail(), requestBean.getTripTitle());
		// Get request sender and receiver
		User receiver = User.getRequestReceiver(requestBean.getSenderEmail(), req.getTarget().getTitle());
			
		// Update trip's participant users in persistence
		receiver.deleteIncomingRequest(req);
		req.getSender().deleteSentRequest(req);
			
		// Add sender user to trip participants
		return req.getTarget().addParticipant(req.getSender());
	}
	
	public synchronized boolean declineRequest(RequestBean requestBean) throws DatabaseException {
		// get request from persistence
		Request r = Request.getRequest(requestBean.getSenderEmail(), requestBean.getTripTitle());
		// Delete request
		return r.deleteRequest();
	}
	
	public List<RequestBean> getUserIncomingRequests(String userEmail) throws DatabaseException {
		User sessionUser = User.getUserByEmail(userEmail);
		List<Request> incRequests = Request.getRequestsByReceiver(sessionUser.getEmail());
		return converter.convertToListBean(incRequests);
	}
	
	
	public List<RequestBean> getUserSentRequests(String userEmail) throws DatabaseException{
		List<Request> sentRequests = Request.getRequestsBySender(userEmail);
		return converter.convertToListBean(sentRequests);
	}
}
