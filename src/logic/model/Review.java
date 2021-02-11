package logic.model;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import logic.persistence.dao.ReviewDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class Review {
	
	private double vote;
	private String title;
	private String comment;
	private Date date;
	private RoleType type;
	private User reviewer;
	
	/* Setters and getters */
	
	public double getVote() {
		return vote;
	}
	
	public void setVote(double rating) {
		this.vote = rating;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public RoleType getType() {
		return type;
	}
	
	public void setType(RoleType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}
	
	public static List<Review> getReviewsByUser(String email) throws DatabaseException {
		try {
			return ReviewDao.getInstance().getUserReviews(email);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}

}
