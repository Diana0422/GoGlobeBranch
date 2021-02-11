package logic.model;

import java.sql.SQLException;
import java.util.List;

import logic.model.exceptions.DuplicateException;
import logic.persistence.dao.RequestDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class Request {
	private int id;
	private Trip target;
	private Boolean accepted;
	private User sender;
	
	public Boolean getAccepted() {
		return accepted;
	}
	
	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Trip getTarget() {
		return target;
	}

	public void setTarget(Trip target) {
		this.target = target;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}
	
	public boolean storeRequest(String email) throws DatabaseException, DuplicateException {
		try {
			if ((RequestDao.getInstance().getRequest(email, this.target.getTitle()) != null)) throw new DuplicateException("You already applied to join for this trip.");
			return RequestDao.getInstance().save(this, email);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public static Request getRequest(String email, String tripTitle) throws DatabaseException {
		try {
			return RequestDao.getInstance().getRequest(email, tripTitle);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public boolean deleteRequest() throws DatabaseException {
		try {
			return RequestDao.getInstance().delete(this.target.getTitle(), this.sender.getEmail());
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public static List<Request> getRequestsBySender(String email) throws DatabaseException {
		try {
			return RequestDao.getInstance().getRequestsBySender(email);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public static List<Request> getRequestsByReceiver(String email) throws DatabaseException {
		try {
			return RequestDao.getInstance().getRequestsByReceiver(email);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
}
