package logic.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import logic.persistence.exceptions.DBConnectionException;

public class ConnectionManager {
	
	private static ConnectionManager instance = null;
	private static Properties prop;
    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    
    private ConnectionManager() {/*empty for singleton*/}
    
    public static ConnectionManager getInstance() {
    	if (instance == null) {
    		instance = new ConnectionManager();
    		prop = new Properties();
    	}
    	
    	return instance;
    }
    
    public Connection getConnection() throws DBConnectionException {

		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			throw new DBConnectionException("Cannot establish connection to database.", e.getCause());
		}
		
			
		try (InputStream input = ConnectionManager.class.getResourceAsStream("../goglobe_config.properties")) {
			prop.load(input);
			
			String user = prop.getProperty("user");
			String pass = prop.getProperty("pass");
			String dbUrl = prop.getProperty("dbUrl");
			return DriverManager.getConnection(dbUrl, user, pass);
		} catch (IOException | SQLException e) {
			throw new DBConnectionException(e.getMessage());
		}

	}
    
    
    public void closeConnection(Connection conn) throws SQLException {
    	if (conn != null) {
    		conn.close();
    	}
    }
    
    
    
}
