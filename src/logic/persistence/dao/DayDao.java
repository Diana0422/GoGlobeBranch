package logic.persistence.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.model.Day;
import logic.model.Location;
import logic.persistence.ConnectionManager;
import logic.persistence.exceptions.DBConnectionException;

public class DayDao {
	
	public static final String STORE_DAY = "call register_day(?, ?, ?)";
	public static final String GET_DAYS = "call fetch_trip_days(?)";
	
	private static DayDao instance = null;
	
	private DayDao() {/*empty*/}
	
	public static DayDao getInstance() {
		if (instance == null) {
			instance = new DayDao();
		}
		return instance;
	}
	
	
	public boolean saveDay(Day day, String tripTitle) throws DBConnectionException, SQLException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(STORE_DAY)) {
			stmt.setInt(1, day.getId());
			stmt.setString(2, tripTitle);
			stmt.setString(3, day.getLocation().getCity());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Day with id:"+day.getId()+"cannot be saved on database.", new Throwable(e.getMessage()));
		}
	}
	
	public List<Day> getTripDays(String tripTitle) throws DBConnectionException, SQLException {
		List<Day> days = new ArrayList<>();
		ResultSet rs = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(GET_DAYS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			stmt.setString(1, tripTitle);
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			
			if (rs != null) {
				rs.first();
				do {
					//Fetch columns
					int dayId = rs.getInt("day_id");
					String locCity = rs.getString("location_city");
					int budget = rs.getInt("budget");
					
					Day d = new Day();
					d.setLocation(new Location(locCity, null, null));
					d.setBudget(budget);
					d.setId(dayId);
					
					days.add(d);
				} while (rs.next());
			}
			
			return days;
		} catch (SQLException e) {
			throw new SQLException("Cannot fetch days for the trip with title:"+tripTitle+"from the database.", new Throwable(e.getMessage()));
		}
	}
	
}
