package logic.control;

import java.util.Date;
import java.util.List;
import java.sql.SQLException;
import java.util.Calendar;

import logic.persistence.dao.ReviewDao;
import logic.persistence.dao.UserDaoDB;
import logic.persistence.dao.UserStatsDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;
import logic.bean.ReviewBean;
import logic.bean.UserBean;
import logic.model.Review;
import logic.model.RoleType;
import logic.model.User;
import logic.model.exceptions.UnloggedException;
import logic.model.interfaces.Observer;
import logic.model.utils.converters.ReviewBeanConverter;

public class ReviewUserController {

	private static ReviewUserController instance = null;
	
	private ReviewUserController() {}
	
	public static ReviewUserController getInstance() {
		
		if (instance == null) {
			instance = new ReviewUserController();
		}
		return instance;
	}
	
	public boolean postReview(String type, double rating, String comment, String title, String reviewerEmail, String targetEmail, Observer o) throws DatabaseException, UnloggedException {
		User reviewer;
		try {
			if (reviewerEmail == null) throw new UnloggedException();
			reviewer = UserDaoDB.getInstance().get(reviewerEmail);
			User target = UserDaoDB.getInstance().get(targetEmail);
			target.setReviews(ReviewDao.getInstance().getUserReviews(targetEmail));
			target.setStats(UserStatsDao.getInstance().getUserStats(targetEmail));
			
			if (o != null) {
				target.getStats().attach(o);
			}
			
			Date today = Calendar.getInstance().getTime();
			
			Review review = new Review();
			review.setReviewer(reviewer);
			if (type.equalsIgnoreCase("traveler")) {
				review.setType(RoleType.TRAVELER);
			} else {
				review.setType(RoleType.ORGANIZER);
			}
			review.setVote(rating);
			review.setDate(today);
			review.setComment(comment);
			review.setTitle(title);
			target.addReview(review);
			return ReviewDao.getInstance().save(review, target.getEmail());
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	
	public List<ReviewBean> getUserReviews(UserBean user) throws DatabaseException {
		ReviewBeanConverter reviewConverter = new ReviewBeanConverter();
		try {
			List<Review> list = ReviewDao.getInstance().getUserReviews(user.getEmail());
			User target = UserDaoDB.getInstance().get(user.getEmail());
			// Complete the missing reviewer information
			for (Review r: list) {
				User reviewer = UserDaoDB.getInstance().get(r.getReviewer().getEmail());
				r.setReviewer(reviewer);
				target.addReview(r);
			}
			
			List<Review> reviews = target.getReviews();
			return reviewConverter.convertToListBean(reviews);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
}
