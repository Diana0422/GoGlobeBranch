package logic.control;

import java.sql.SQLException;
import java.util.Date;

import logic.bean.SessionBean;
import logic.persistence.dao.UserDaoDB;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Cookie;
import logic.util.Session;
import logic.model.User;

public class RegistrationController {
	
	private static RegistrationController instance = null;
	
	private RegistrationController() {}
	
	public static RegistrationController getInstance() {
		if (instance == null) {
			instance = new RegistrationController();
		}
		
		return instance;
	}
	
	public synchronized SessionBean register(String email, String password, String name, String surname, String birthday) throws DatabaseException {
		User user;
		Date birth = FormatManager.parseDate(birthday);
		user = new User(name, surname, birth, email, password);
		try {
			if (UserDaoDB.getInstance().save(user)) {
				Session session = new Session();
				session.setUserName(user.getName());
				session.setUserSurname(user.getSurname());
				session.setUserEmail(email);
				Cookie.getInstance().addSession(session);
				SessionBean bean = new SessionBean(); 
				bean.setSessionEmail(email);
				bean.setSessionName(name);
				bean.setSessionSurname(surname);
				bean.setSessionPoints(0);
				return bean;
			}
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
		return null;
	}
	
}
