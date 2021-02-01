package logic.persistence.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;

import logic.model.TripCategory;
import logic.model.UserStats;
import logic.persistence.ConnectionManager;
import logic.persistence.exceptions.DBConnectionException;

public class UserStatsDao {
	
	private static final String UPDATE_STATS = "call update_user_stats(?, ?, ?, ?)";
	private static final String GET_STATS = "call fetch_user_stats(?)";
	private static final String GET_ATTITUDE = "call fetch_attitude(?)";
	private static final String UPDATE_ATTITUDE ="call update_attitude(?, ?, ?, ?, ?)";
	private static UserStatsDao instance = null;
	
	private UserStatsDao() {/*empty*/}
	
	public static UserStatsDao getInstance() {
		if (instance == null) {
			instance = new UserStatsDao();
		}
		return instance;
	}

	public boolean updateStats(String userId, int newPoints, Double newOrgRating, Double newTravRating) throws DBConnectionException, SQLException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(UPDATE_STATS)) {
				
			//Parameter binding and conversion
			stmt.setString(1, userId);
			stmt.setInt(2, newPoints);
			stmt.setDouble(3, newOrgRating);
			stmt.setDouble(4, newTravRating);
			return stmt.execute();
		} catch (SQLException e) {
			throw new SQLException("Cannot update user stats on database.", new Throwable(e.getMessage()));
		}
	}
	
	public UserStats getUserStats(String userEmail) throws DBConnectionException, SQLException {
		ResultSet rs = null;
		UserStats us = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
				CallableStatement stmt = conn.prepareCall(GET_STATS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			stmt.setString(1, userEmail);
			if (stmt.execute()) rs = stmt.getResultSet();
			
			if (rs != null) {
				rs.first();
				Double orgRating = rs.getDouble("organizer_rating");
				Double travRating = rs.getDouble("traveler_rating");
				
				us = new UserStats();
				us.setOrganizerRating(orgRating);
				us.setTravelerRating(travRating);
			}
			
			return us;
		} catch (SQLException e) {
			throw new SQLException("Cannot get user stats from database.", new Throwable(e.getMessage()));
		}
	}
	
	public Map<TripCategory, Integer> getUserAttitude(String userEmail) throws DBConnectionException, SQLException {
		ResultSet rs = null;
		Map<TripCategory, Integer> mapping = new EnumMap<>(TripCategory.class);
		try (Connection conn = ConnectionManager.getInstance().getConnection();
				CallableStatement stmt = conn.prepareCall(GET_ATTITUDE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			stmt.setString(1, userEmail);
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			if (rs != null) {
				rs.first();
				Integer funVal = rs.getInt("fun_attitude");
				Integer culVal = rs.getInt("cul_attitude");
				Integer advVal = rs.getInt("adv_attitude");
				Integer relVal = rs.getInt("rel_attitude");
				mapping.putIfAbsent(TripCategory.FUN, funVal);
				mapping.putIfAbsent(TripCategory.CULTURE, culVal);
				mapping.putIfAbsent(TripCategory.ADVENTURE, advVal);
				mapping.putIfAbsent(TripCategory.RELAX, relVal);
			}
			
			return mapping;
		} catch (SQLException e) {
			throw new SQLException("Cannot get traveling attitude for user: "+userEmail+" from database.", e);
		}
	}
	
	
	public boolean updateAttitude(String userEmail, Integer funVal, Integer culVal, Integer relVal, Integer advVal) throws SQLException, DBConnectionException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
				CallableStatement stmt = conn.prepareCall(UPDATE_ATTITUDE)) {
			stmt.setString(1, userEmail);
			stmt.setInt(2, culVal);
			stmt.setInt(3, relVal);
			stmt.setInt(4, advVal);
			stmt.setInt(5, funVal);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Cannot update traveling attitude for user: "+userEmail+" from database.", e);
		}
	}

}
