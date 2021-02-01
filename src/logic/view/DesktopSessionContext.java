package logic.view;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import logic.bean.SessionBean;

public class DesktopSessionContext implements Initializable {
	
	private static DesktopSessionContext instance = null;
	
	private SessionBean session;
	
	private static GUILoader guiLoader;
	
    @FXML
    private BorderPane borderpane;
    
    private DesktopSessionContext() {
    }
    
    public static DesktopSessionContext getInstance() {
    	if (instance == null) {
    		instance = new DesktopSessionContext();
    		setGuiLoader(new GUILoader());
    	}
    	
    	return instance;
    }
    
    
    public void addToCenter(Node node) {
    	borderpane.setCenter(node);
    }
    
    public void addToTop(Node node) {
    	borderpane.setTop(node);
    }

	public SessionBean getSession() {
		return session;
	}

	public void setSession(SessionBean bean) {
		this.session = bean;
	}

	public static GUILoader getGuiLoader() {
		return guiLoader;
	}

	public static void setGuiLoader(GUILoader guiLoader) {
		DesktopSessionContext.guiLoader = guiLoader;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		guiLoader.loadGUI(null, null, GUIType.LOGIN);
	}

}
