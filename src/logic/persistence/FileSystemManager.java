package logic.persistence;

import java.io.File;
import java.util.logging.Logger;
import logic.model.ModelClassType;

public class FileSystemManager {
	
    private String projPath = "user.dir";
	private static FileSystemManager instance = null;
	
	private FileSystemManager() {}
	
	public static FileSystemManager getInstance() {
		if (instance == null) {
			instance = new FileSystemManager();
		}
		
		return instance;
	}
	
	public File getBackendFile(ModelClassType type) {
		String projectPath = System.getProperty(projPath);
		Logger.getGlobal().info(projectPath);
		
		switch(type) {
			case TRIP:
				return new File(projectPath + "/trips.out");
			case USER:
				return new File(projectPath + "/users.out");
			case REQUEST:
				return new File(projectPath + "/requests.out");
			case REVIEW:
			default:
				break;
		}
		
		// return file not found exception?
		return null;
	}
	
	
	
}
	

	