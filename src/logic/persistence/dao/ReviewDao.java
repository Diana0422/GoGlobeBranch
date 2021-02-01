package logic.persistence.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import logic.control.FormatManager;
import logic.model.Review;
import logic.model.RoleType;
import logic.model.User;
import logic.model.factories.UserFactory;
import logic.persistence.ConnectionManager;
import logic.persistence.exceptions.DBConnectionException;

public class ReviewDao {
	
	private static final String GET_REVIEWS = "call fetch_reviews(?)";
	private static final String STORE_REVIEW = "call register_review(?, ?, ?, ?, ?, ? ,?)";
	
	private static ReviewDao instance = null;
	
	private ReviewDao() {/*empty*/}
	
	public static ReviewDao getInstance() {
		if (instance == null) {
			instance = new ReviewDao();
		}
		return instance;
	}
	
	
	public List<Review> getUserReviews(String userEmail) throws DBConnectionException, SQLException {
		ResultSet rs = null;
		List<Review> list = new ArrayList<>();
		Review r = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(GET_REVIEWS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
	
			stmt.setString(1,userEmail);
				
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
	
			if (rs != null) {
				if (!rs.next()) return list; 
				rs.first();
				do{
					// reading columns
					String reviewerName = rs.getString("name");
					String reviewerSurname = rs.getString("surname");
		            String comment = rs.getString("comment");
		            Date date = rs.getDate("date");
		            String role = rs.getString("role");
		            String title = rs.getString("title");
		            Double vote = rs.getDouble("vote");
		                
		            User revUser = UserFactory.getInstance().createModel();
		            revUser.setName(reviewerName);
		            revUser.setSurname(reviewerSurname);
		            r = new Review();
		            r.setTitle(title);
		            r.setComment(comment);
		            r.setDate(date);
		            r.setReviewer(revUser);
		            r.setType(RoleType.valueOf(role));
		            r.setVote(vote);
		                
		            list.add(r);
				}while(rs.next());
			}
			return list;
		} catch (SQLException e) {
			throw new SQLException("Cannot get reviews from database for user:"+userEmail, new Throwable(e.getMessage()));
		}
			
	}
	
	
	public boolean save(Review t, String target) throws DBConnectionException, SQLException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(STORE_REVIEW)) {
			
			stmt.setString(1, t.getReviewer().getEmail());
			stmt.setString(2, target);
			stmt.setString(3, t.getComment());
			stmt.setDate(4, Date.valueOf(FormatManager.formatDateSQL(t.getDate())));
			stmt.setString(5, t.getType().toString());
			stmt.setString(6, t.getTitle());
			stmt.setDouble(7, t.getVote());
				
			return stmt.execute();
		} catch (SQLException e) {
			throw new SQLException("Cannot save request on database from user:"+t.getReviewer().getEmail()+" for trip:"+target, new Throwable(e.getMessage()));
		}
	}



}
