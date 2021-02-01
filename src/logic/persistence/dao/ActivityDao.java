package logic.persistence.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.model.Activity;
import logic.persistence.ConnectionManager;
import logic.persistence.exceptions.DBConnectionException;

public class ActivityDao {

	public static final String STORE_ACTIVITY = "call register_activity(?, ?, ?, ?, ?, ?)";
	public static final String GET_ACTIVITIES = "call fetch_activities(?, ?)";
	
	private static ActivityDao instance = null;
	
	private ActivityDao() {/*empty*/}
	
	public static ActivityDao getInstance() {
		if (instance == null) {
			instance = new ActivityDao();
		}
		return instance;
	}
	
	public boolean saveActivity(Activity activity, int dayId, String tripTitle) throws DBConnectionException, SQLException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(STORE_ACTIVITY)) {
			stmt.setInt(1, dayId);
			stmt.setString(2, tripTitle);
			stmt.setString(3, activity.getTitle());
			stmt.setString(4, activity.getTime());
			stmt.setInt(5, activity.getEstimatedCost());
			stmt.setString(6, activity.getDescription());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Cannot save activity "+activity.getTitle()+" in day "+dayId+" for trip "+tripTitle, new Throwable(e.getMessage()));
		}
	}
	
	public List<Activity> getActivitiesByTrip(String tripTitle, int dayId) throws DBConnectionException, SQLException {
		List<Activity> activities = new ArrayList<>();
		ResultSet rs = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(GET_ACTIVITIES, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			stmt.setString(1, tripTitle);
			stmt.setInt(2, dayId);
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			
			if (rs != null) {
				rs.first();
				
				do {
					//Column fetch
					String title = rs.getString("title");
					String time = rs.getTime("time").toString();
					String description = rs.getString("description");
					int cost = rs.getInt("estimated_cost");
					
					//Instantiate new activity
					Activity a = new Activity(title, time, description, cost);
					activities.add(a);
					
				} while(rs.next());
			}
			
			return activities;
		} catch (SQLException e) {
			throw new SQLException("Cannot get activities for day "+dayId+" int trip "+tripTitle+" from database.", new Throwable(e.getMessage()));
		}
		
		
	}
}
