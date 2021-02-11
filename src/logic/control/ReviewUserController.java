package logic.control;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
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
	
	public boolean postReview(String type, double rating, String comment, String title, String reviewerEmail, String targetEmail, Observer o) throws DatabaseException, UnloggedException {
		if (reviewerEmail == null) throw new UnloggedException();
		User reviewer = User.getUserByEmail(reviewerEmail);
		User target = User.getUserByEmail(targetEmail);
		target.setReviews(Review.getReviewsByUser(targetEmail));
			
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
		return target.addReview(review);
	}
	
	
	public List<ReviewBean> getUserReviews(UserBean user) throws DatabaseException {
		ReviewBeanConverter reviewConverter = new ReviewBeanConverter();
		List<Review> list = Review.getReviewsByUser(user.getEmail());
		User target = User.getUserByEmail(user.getEmail());
		// Complete the missing reviewer information
		for (Review r: list) {
			User reviewer = User.getUserByEmail(r.getReviewer().getEmail());
			r.setReviewer(reviewer);
			target.addReview(r);
		}
			
		List<Review> reviews = target.getReviews();
		return reviewConverter.convertToListBean(reviews);
	}
}
