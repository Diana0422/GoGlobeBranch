package logic.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.view.utils.GraphicLoader;

public class Cookie {

	private Map<String, Session> activeSessions;
	private Map<String, GraphicLoader> graphicLoaders;
	private static Cookie instance;
	
	private Cookie() {
		activeSessions = new HashMap<>();
	}
	
	public static Cookie getInstance() {
		if (instance == null) {
			instance = new Cookie();
		}
		
		return instance;
	}
	
	public void addSession(Session session) {
		if (getSession(session.getUserEmail()) == null) {
			Cookie.getInstance().activeSessions.put(session.getUserEmail(), session);
			String logStr = "Added active session: "+session;
			Logger.getGlobal().log(Level.INFO, logStr);
		}
	}
	
	public void removeSession(String userEmail) {
		Cookie.getInstance().activeSessions.remove(userEmail);
	}
	
	public Session getSession(String userEmail) {
		return activeSessions.get(userEmail);
	}

	public GraphicLoader getGraphicLoader(String user) {
		return graphicLoaders.get(user);
	}

	public void addGraphicLoader(String user, GraphicLoader loader) {
		this.graphicLoaders.put(user, loader);
	}
	
}
