package logic.view.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import logic.view.exceptions.UnavailableConfigurationException;

public class PropManager {

	private static PropManager instance;
	private Properties prop;

	private PropManager() throws UnavailableConfigurationException {
		try (InputStream input = PropManager.class.getResourceAsStream("../../goglobe_config.properties")) {
			prop = new Properties();
			prop.load(input);
		}
		catch(IOException e) {
			throw new UnavailableConfigurationException("Unable to load config file");
		}
	}
	
	public static PropManager getInstance() throws UnavailableConfigurationException {
		if (instance == null) instance = new PropManager();
		return instance;
	}
	
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
}
