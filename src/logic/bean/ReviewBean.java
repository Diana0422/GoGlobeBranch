package logic.bean;

public class ReviewBean {
	
	private String reviewerName;
	private String reviewerSurname;
	private double vote;
	private String date;
	private String title;
	private String comment;

	public ReviewBean() {
		//empty
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getReviewerSurname() {
		return reviewerSurname;
	}

	public void setReviewerSurname(String reviewerSurname) {
		this.reviewerSurname = reviewerSurname;
	}

	public double getVote() {
		return vote;
	}

	public void setVote(double vote) {
		this.vote = vote;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
