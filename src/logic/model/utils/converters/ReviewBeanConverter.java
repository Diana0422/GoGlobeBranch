package logic.model.utils.converters;

import java.util.ArrayList;
import java.util.List;

import logic.bean.ReviewBean;
import logic.control.FormatManager;
import logic.model.Review;
import logic.persistence.exceptions.DatabaseException;

public class ReviewBeanConverter implements BeanConverter<Review,ReviewBean> {

	@Override
	public ReviewBean convertToBean(Review o) {
		ReviewBean bean = new ReviewBean();
		bean.setReviewerName(o.getReviewer().getName());
		bean.setReviewerSurname(o.getReviewer().getSurname());
		bean.setTitle(o.getTitle());
		bean.setComment(o.getComment());
    	String date = FormatManager.formatDate(o.getDate());
    	bean.setDate(date);
    	bean.setVote(o.getVote());
    	return bean;
	}

	@Override
	public Review convertFromBean(ReviewBean o) throws DatabaseException {
		Review rev = new Review();
		rev.setTitle(o.getTitle());
		rev.setComment(o.getComment());
		rev.setDate(FormatManager.parseDate(o.getDate()));
		rev.setVote(o.getVote());
		return rev;
	}

	@Override
	public List<ReviewBean> convertToListBean(List<Review> list) {
		List<ReviewBean> listBean = new ArrayList<>();
		if (list.isEmpty()) return listBean;
		for (Review rev: list) {
			ReviewBean bean = convertToBean(rev);
	    	listBean.add(bean);
		}
		return listBean;
	}

	@Override
	public List<Review> convertFromListBean(List<ReviewBean> list) throws DatabaseException {
		List<Review> reviews = new ArrayList<>();
		for (ReviewBean bean: list) {
			Review rev = convertFromBean(bean);
			reviews.add(rev);
		}
		return reviews;
	}


}
