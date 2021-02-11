package logic.persistence.dao;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import logic.control.FormatManager;
import logic.model.User;
import logic.persistence.ConnectionManager;
import logic.persistence.exceptions.DBConnectionException;

public class UserDaoDB {
	
	private static final String GET_USER = "call login_user(?)";
	private static final String STORE_USER = "call register_user(?, ?, ?, ?, ?)";
	private static final String UPDATE_USER = "call update_user(?, ?, ?, ?)";
	private static final String DELETE_USER = "call delete_user(?)";
	private static final String GET_ORGANIZER = "call fetch_trip_organizer(?)";
	private static final String GET_PARTICIPANTS = "call fetch_trip_participants(?)";
	private static final String GET_REQUEST_RECEIVER = "call fetch_request_receiver(?, ?)";
	private static final String EMAIL_COLUMN = "email";
	private static final String NAME_COLUMN = "name";
	private static final String SURNAME_COLUMN = "surname";
	private static final String PASS_COLUMN = "password";
	private static final String BIRTH_COLUMN = "birthday";
	private static final String BIO_COLUMN = "bio";
	
	private static UserDaoDB instance = null;
	
	private UserDaoDB() {/*empty*/}
	
	public static UserDaoDB getInstance() {
		if (instance == null) {
			instance = new UserDaoDB();
		}
		return instance;
	}
	

	public User get(String...params) throws DBConnectionException, SQLException {
		ResultSet rs = null;
		User u = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(GET_USER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
			
			stmt.setString(1, params[0]);
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			
			if (rs != null) {
				if (!rs.first()) return null;
				String email = rs.getString(EMAIL_COLUMN);
				String name = rs.getString(NAME_COLUMN);
				String surname = rs.getString(SURNAME_COLUMN);
				String password = rs.getNString(PASS_COLUMN);
				Date date = rs.getDate(BIRTH_COLUMN);
				String bio = rs.getString(BIO_COLUMN);
			
				u = new User();				
					
				u.setName(name);
				u.setSurname(surname);
				u.setEmail(email);
				u.setPassword(password);
				u.setBirthday(date);
				u.setBio(bio);
				u.setStats(UserStatsDao.getInstance().getUserStats(email));
				u.copyAttitude(UserStatsDao.getInstance().getUserAttitude(email));
			}
			return u;
		} catch (SQLException e) {
			throw new SQLException("Cannot get user from database.", e);
		}
		
	}


	public boolean save(User t) throws DBConnectionException, SQLException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			  CallableStatement stmt = conn.prepareCall(STORE_USER)) {
			stmt.setString(1, t.getEmail());
			stmt.setString(2, t.getPassword());
			stmt.setString(3, t.getName());
			stmt.setString(4, t.getSurname());
			stmt.setDate(5, Date.valueOf(FormatManager.formatDateSQL(t.getBirthday())));
			stmt.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Cannot save user on database.", e);
		}
		
	}


	public boolean update(User t, String...params) throws DBConnectionException, SQLException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(UPDATE_USER)) {
			
			//Parameter binding and conversion
			stmt.setString(1, t.getEmail());
			stmt.setString(2, params[0]);
			stmt.setString(3, params[1]);
			stmt.setString(4, params[2]);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Cannot update user info on database.", e);
		}
		
	}


	public boolean delete(User t) throws DBConnectionException, SQLException {
		try (Connection conn = ConnectionManager.getInstance().getConnection();
			CallableStatement stmt = conn.prepareCall(DELETE_USER)) {
			stmt.setString(1, t.getEmail());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Cannot delete user from database.", e);
		}
		
	}
	
	public User getTripOrganizer(String tripTitle) throws DBConnectionException, SQLException {
		ResultSet rs = null;
		User u = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
				CallableStatement stmt = conn.prepareCall(GET_ORGANIZER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
			stmt.setString(1, tripTitle);
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			
			if (rs != null) {
				rs.first();
				// Only one row: fetch columns
				String email = rs.getString(EMAIL_COLUMN);
				String pass = rs.getString(PASS_COLUMN);
				String name = rs.getString(NAME_COLUMN);
				String surname = rs.getString(SURNAME_COLUMN);
				Date birthday = rs.getDate(BIRTH_COLUMN);
				String bio = rs.getString(BIO_COLUMN);
				
				u = new User();
				u.setEmail(email);
				u.setBio(bio);
				u.setBirthday(birthday);
				u.setName(name);
				u.setPassword(pass);
				u.setSurname(surname);
				u.setStats(UserStatsDao.getInstance().getUserStats(email));
				u.copyAttitude(UserStatsDao.getInstance().getUserAttitude(email));
			}
			
			return u;
		} catch (SQLException e) {
			throw new SQLException("Cannot get trip organizer from database.", e);
		}
	}
	
	public List<User> getTripParticipants(String tripTitle) throws DBConnectionException, SQLException {
		List<User> participants = new ArrayList<>();
		ResultSet rs = null;
		
		try (Connection conn = ConnectionManager.getInstance().getConnection();
				CallableStatement stmt = conn.prepareCall(GET_PARTICIPANTS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			
			stmt.setString(1, tripTitle);
			
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			
			if (rs != null) {
				if (!rs.next()) return participants;
				rs.first();
				do {
					String name = rs.getString(NAME_COLUMN);
					String surname = rs.getString(SURNAME_COLUMN);
					String email = rs.getString(EMAIL_COLUMN);
					String pass = rs.getString(PASS_COLUMN);
					Date birthday = rs.getDate(BIRTH_COLUMN);
					String bio = rs.getString(BIO_COLUMN);
					
					User u = new User();
					u.setBio(bio);
					u.setEmail(email);
					u.setPassword(pass);
					u.setName(name);
					u.setSurname(surname);
					u.setBirthday(birthday);
					u.setStats(UserStatsDao.getInstance().getUserStats(email));
					u.copyAttitude(UserStatsDao.getInstance().getUserAttitude(email));
					participants.add(u);
				} while (rs.next());
			}
			return participants;
		} catch (SQLException e) {
			throw new SQLException("Cannot get participants to the trip:"+tripTitle+" from database.", e);
		}
		
	}
	
	public User getRequestReceiver(String userEmail, String tripTitle) throws DBConnectionException, SQLException {
		ResultSet rs = null;
		User u = null;
		try (Connection conn = ConnectionManager.getInstance().getConnection();
				CallableStatement stmt = conn.prepareCall(GET_REQUEST_RECEIVER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			stmt.setString(1, tripTitle);
			stmt.setString(2, userEmail);
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
			
			if (rs != null) {
				rs.first();
				String name = rs.getString(NAME_COLUMN);
				String surname = rs.getString(SURNAME_COLUMN);
				String email = rs.getString(EMAIL_COLUMN);
				Date birth = rs.getDate(BIRTH_COLUMN);
				String bio = rs.getString(BIO_COLUMN);
				u = new User();
				u.setEmail(email);
				u.setName(name);
				u.setSurname(surname);
				u.setBirthday(birth);
				u.setIncRequests(RequestDao.getInstance().getRequestsByReceiver(email));
				u.setStats(UserStatsDao.getInstance().getUserStats(email));
				u.copyAttitude(UserStatsDao.getInstance().getUserAttitude(email));
				u.setBio(bio);
			}
			return u;
		} catch (SQLException e) {
			throw new SQLException("Cannot get receiver of the request by: "+userEmail+" for trip: "+tripTitle+" from database.", e);
		}
	}
}
