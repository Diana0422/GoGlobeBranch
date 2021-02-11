package logic.persistence.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.control.FormatManager;
import logic.model.Trip;
import logic.model.TripCategory;
import logic.persistence.ConnectionManager;
import logic.persistence.exceptions.DBConnectionException;

public class TripDao {
	
	public static final String STORE_TRIP = "call register_trip(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_TRIP = "call fetch_trip(?)";
	public static final String GET_TRIPS = "call fetch_trips()";
	public static final String GET_SHARED_TRIPS = "call fetch_shared_trips()";
	public static final String GET_FOR_CATEGORY = "call fetch_trips_category(?)";
	public static final String STORE_PARTICIPANT = "call register_trip_participation(?, ?)";
	public static final String TITLE_COLUMN = "title";
	public static final String PRICE_COLUMN = "price";
	public static final String DEPARTURE_COLUMN = "departure";
	public static final String RETURN_COLUMN = "return_date";
	public static final String CATEGORY1_COLUMN = "category1";
	public static final String CATEGORY2_COLUMN = "category2";
	public static final String ORG_COLUMN = "organizator";
	public static final String MAX_AGE_COLUMN = "max_age";
	public static final String MIN_AGE_COLUMN = "min_age";
	public static final String MAX_PART_COLUMN = "max_participants";
	public static final String DESC_COLUMN = "description";
	public static final String COUNTRY_COLUMN = "country";
	
	
	private static TripDao instance = null;
	
	private TripDao() {/*empty*/}
	
	public static TripDao getInstance() {
		if (instance == null) {
			instance = new TripDao();
		}
		return instance;
	}
	
	public boolean saveTrip(Trip t, boolean shared) throws DBConnectionException, SQLException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(STORE_TRIP)) {
			stmt.setBoolean(1, shared);
			stmt.setString(2, t.getTitle());
			stmt.setString(3, t.getCategory1().toString());
			stmt.setString(4, t.getCategory2().toString());
			stmt.setDate(5, Date.valueOf(FormatManager.formatDateSQL(t.getDepartureDate())));
			stmt.setDate(6, Date.valueOf(FormatManager.formatDateSQL(t.getReturnDate())));
			stmt.setString(7, t.getOrganizer().getEmail());
			stmt.setString(8, t.getDescription());
			stmt.setInt(9, t.getMinAge());
			stmt.setInt(10, t.getMaxAge());
			stmt.setInt(11, t.getMaxParticipants());
			stmt.setString(12, t.getCountry());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Cannot save trip on database.", e);
		}
	}
	
	public Trip getTripByTitle(String tripTitle) throws DBConnectionException, SQLException {
		Trip trip = new Trip();
		ResultSet rs = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
				CallableStatement stmt = conn.prepareCall(GET_TRIP, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			stmt.setString(1, tripTitle);
			if (stmt.execute()) rs = stmt.getResultSet();
			if (rs != null) {
				rs.first();
				int price = rs.getInt(PRICE_COLUMN);
				Date dep = rs.getDate(DEPARTURE_COLUMN);
				Date ret = rs.getDate(RETURN_COLUMN);
				TripCategory cat1 = TripCategory.valueOf(rs.getString(CATEGORY1_COLUMN));
				TripCategory cat2 = TripCategory.valueOf(rs.getString(CATEGORY2_COLUMN));
				String organizer = rs.getString(ORG_COLUMN);
				String tripDescription = rs.getString(DESC_COLUMN);
				int tripMinAge = rs.getInt(MIN_AGE_COLUMN);
				int tripMaxAge = rs.getInt(MAX_AGE_COLUMN);
				int maxPart = rs.getInt(MAX_PART_COLUMN);
				String country = rs.getString(COUNTRY_COLUMN);
				trip.setTitle(tripTitle);
				trip.setPrice(price);
				trip.setCategory1(cat1);
				trip.setCategory2(cat2);
				trip.setDepartureDate(dep);
				trip.setReturnDate(ret);
				trip.setDescription(tripDescription);
				trip.setMaxAge(tripMaxAge);
				trip.setMaxParticipants(maxPart);
				trip.setMinAge(tripMinAge);
				trip.setCountry(country);
				trip.setDays(DayDao.getInstance().getTripDays(tripTitle));
				trip.setOrganizer(UserDaoDB.getInstance().get(organizer));
				trip.setParticipants(UserDaoDB.getInstance().getTripParticipants(tripTitle));
			}
			
			return trip;
		} catch (SQLException e) {
			throw new SQLException("Cannot get trip with title:"+tripTitle+" from database.", e);
		}
	}
	
	public List<Trip> getTrips() throws DBConnectionException, SQLException {
		List<Trip> trips = new ArrayList<>();
		ResultSet rs = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(GET_TRIPS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			if (rs != null) {
				if (!rs.next()) return trips;
				rs.first();
				do {
					// reading columns
					String title = rs.getString(TITLE_COLUMN);
					Date dep = rs.getDate(DEPARTURE_COLUMN);
					Date ret = rs.getDate(RETURN_COLUMN);
					TripCategory categ1 = TripCategory.valueOf(rs.getString(CATEGORY1_COLUMN));
					TripCategory categ2 = TripCategory.valueOf(rs.getString(CATEGORY2_COLUMN));
					int price = rs.getInt(PRICE_COLUMN);
					String country = rs.getString(COUNTRY_COLUMN);
					
					// Instantiate new trip
					Trip t = new Trip();
					t.setTitle(title);
					t.setCategory1(categ1);
					t.setCategory2(categ2);
					t.setDepartureDate(dep);
					t.setReturnDate(ret);
					t.setShared(false);
					t.setPrice(price);
					t.setCountry(country);
					t.setDays(DayDao.getInstance().getTripDays(title));
					t.setOrganizer(UserDaoDB.getInstance().getTripOrganizer(title));
					t.setParticipants(UserDaoDB.getInstance().getTripParticipants(title));
					trips.add(t);
				} while(rs.next());
			}
			
			return trips;
		} catch (SQLException e) {
			throw new SQLException("Cannot get not shared trips from database.", e);
		}
	}
	
	
	public List<Trip> getSharedTrips() throws DBConnectionException, SQLException {
		List<Trip> trips = new ArrayList<>();
		ResultSet rs = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(GET_SHARED_TRIPS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			
			if (rs != null) {
				if (!rs.next()) return trips;
				rs.first();
				do {
					// reading columns
					String title = rs.getString(TITLE_COLUMN);
					Date depart = rs.getDate(DEPARTURE_COLUMN);
					Date ret = rs.getDate(RETURN_COLUMN);
					TripCategory category1 = TripCategory.valueOf(rs.getString(CATEGORY1_COLUMN));
					TripCategory category2 = TripCategory.valueOf(rs.getString(CATEGORY2_COLUMN));
					int price = rs.getInt(PRICE_COLUMN);
					String desc = rs.getString(DESC_COLUMN);
					int ageMin = rs.getInt(MIN_AGE_COLUMN);
					int ageMax = rs.getInt(MAX_AGE_COLUMN);
					int maxPart = rs.getInt(MAX_PART_COLUMN);
					String country = rs.getString(COUNTRY_COLUMN);
					
					// Instantiate new trip
					Trip t = new Trip();
					
					t.setTitle(title);
					t.setCategory1(category1);
					t.setCategory2(category2);
					t.setDepartureDate(depart);
					t.setReturnDate(ret);
					t.setPrice(price);
					t.setMaxParticipants(maxPart);
					t.setMaxAge(ageMax);
					t.setMinAge(ageMin);
					t.setDescription(desc);
					t.setShared(true);
					t.setCountry(country);
					t.setDays(DayDao.getInstance().getTripDays(title));
					t.setParticipants(UserDaoDB.getInstance().getTripParticipants(title));
					trips.add(t);
				} while(rs.next());
			}
			
			return trips;
		} catch (SQLException e) {
			throw new SQLException("Cannot get shared trips from database.", e);
		}
	}
	
	
	public boolean saveParticipant(String userEmail, String tripTitle) throws DBConnectionException, SQLException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
				CallableStatement stmt = conn.prepareCall(STORE_PARTICIPANT)) {
			stmt.setString(1, tripTitle);
			stmt.setString(2, userEmail);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Cannot save participant for the trip:"+tripTitle+" on database.", e);
		}
	}

	public List<Trip> getTripsForCategory(TripCategory favourite) throws SQLException, DBConnectionException {
		ResultSet rs = null;
		List<Trip> trips = new ArrayList<>();
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(GET_FOR_CATEGORY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			stmt.setString(1, favourite.toString());
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			
			
			if (rs != null) {
				if (!rs.first()) return trips;
				do {
					// reading columns
					String title = rs.getString(TITLE_COLUMN);
					Date d = rs.getDate(DEPARTURE_COLUMN);
					Date r = rs.getDate(RETURN_COLUMN);
					TripCategory cat1 = TripCategory.valueOf(rs.getString(CATEGORY1_COLUMN));
					TripCategory cat2 = TripCategory.valueOf(rs.getString(CATEGORY2_COLUMN));
					int price = rs.getInt(PRICE_COLUMN);
					String desc = rs.getString(DESC_COLUMN);
					int minAge = rs.getInt(MIN_AGE_COLUMN);
					int maxAge = rs.getInt(MAX_AGE_COLUMN);
					int maxParticipants = rs.getInt(MAX_PART_COLUMN);
					String country = rs.getString(COUNTRY_COLUMN);
					
					// Instantiate new trip
					Trip t = new Trip();
					
					t.setTitle(title);
					t.setCategory1(cat1);
					t.setCategory2(cat2);
					t.setDepartureDate(d);
					t.setReturnDate(r);
					t.setPrice(price);
					t.setMaxParticipants(maxParticipants);
					t.setMaxAge(maxAge);
					t.setMinAge(minAge);
					t.setDescription(desc);
					t.setShared(true);
					t.setCountry(country);
					t.setDays(DayDao.getInstance().getTripDays(title));
					t.setParticipants(UserDaoDB.getInstance().getTripParticipants(title));
					
					trips.add(t);
				} while (rs.next());
			}
			return trips;
		} catch (SQLException e) {
			throw new SQLException("Cannot get trips for category: "+favourite+" from database.", e);
		}
	}
	
}
