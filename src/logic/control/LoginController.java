package logic.control;

import logic.bean.LoginBean;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Cookie;
import logic.util.Session;
import logic.model.User;

public class LoginController {
	
	public LoginBean login(String username, String password) throws DatabaseException { 
		
		
		LoginBean loginBean = new LoginBean();
		User tempUser = null;
		Session session = null;
		if ((tempUser = User.getUserByEmail(username)) != null) {
			if (tempUser.getEmail().equals(username) && tempUser.getPassword().equals(password)){
				// Search for user instance in cookies or add to logged users
				if ((session = Cookie.getInstance().getSession(username))==null) {
					session = new Session();
					session.setUserEmail(username);
					session.setUserName(tempUser.getName());
					session.setUserSurname(tempUser.getSurname());
					session.setUserPoints(tempUser.getStats().getPoints());
					Cookie.getInstance().addSession(session);
				}
				loginBean.setUsername(session.getUserEmail());
				loginBean.setPassword(password);
				loginBean.setNome(tempUser.getName());
				loginBean.setCognome(tempUser.getSurname());
				loginBean.setPoints(tempUser.getStats().getPoints());			
			}
		} else {
			loginBean = null;
		}
		return loginBean;
	}	
}