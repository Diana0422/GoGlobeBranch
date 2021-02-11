package logic.bean;

import java.util.Calendar;

import logic.control.FormatManager;

public class ProfileBean {
	
	private UserBean user;
	
	private String comment;
	private String title;
	private double vote;
	
	public ProfileBean() {
		//empty constructor
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDate() {
		return FormatManager.formatDate(Calendar.getInstance().getTime());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getVote() {
		return vote;
	}

	public void setVote(double vote) {
		this.vote = vote;
	}

}
