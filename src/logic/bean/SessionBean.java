package logic.bean;

import logic.model.User;

public class SessionBean {
	private User loggedUser;
	private String sessionName;
	private String sessionSurname;
	private String sessionEmail;
	private int sessionPoints;
	
	public SessionBean() {
		// empty constructor
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
		setSessionName(loggedUser.getName());
		setSessionSurname(loggedUser.getSurname());
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getSessionSurname() {
		return sessionSurname;
	}

	public void setSessionSurname(String sessionSurname) {
		this.sessionSurname = sessionSurname;
	}

	public String getSessionEmail() {
		return sessionEmail;
	}

	public void setSessionEmail(String sessionEmail) {
		this.sessionEmail = sessionEmail;
	}

	public int getSessionPoints() {
		return sessionPoints;
	}

	public void setSessionPoints(int sessionPoints) {
		this.sessionPoints = sessionPoints;
	}
	
	
}
