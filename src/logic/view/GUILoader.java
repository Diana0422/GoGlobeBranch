package logic.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;

public class GUILoader {
	
	private FXMLLoader loader;
	private Node prevNode;
	private Initializable prevCtrl;
	private GUIType prevState;
	private Object prevBundle;
	
	
	public void loadGUI(Node prev, Object data , GUIType type) {
		setPrevNode(prev);
		loadContent(type, data);
		loadNavbar();
	}
	
	public void loadGUIStateful(Object currentData, Object forData, GUIType next, GUIType current) {
		loadContent(next, currentData); 
		setPrevState(current);
		setPrevBundle(forData);
		loadNavbar();
	}
	
	private void loadNavbar() {
		loadNav("/logic/view/UpperNavbar.fxml");
	}
	
	private void loadContent(GUIType type, Object bundle) {
		loader = new FXMLLoader();
		Parent root = null;
		String ui = null;
		String logStr = "Loaded UI: ";

		//Load previous user interface, if exists.
		if (getPrevState() != null) {
			GUIType previous = getPrevState();
			setPrevState(null);
			loadGUI(null, getPrevBundle(), previous);
		}
		
		switch(type) {
			case MAIN:
				ui = "/logic/view/Main.fxml"; 
				loader.setLocation(getClass().getResource(ui));
				root = loadUI(loader);
				logStr += ui;
				Logger.getGlobal().info(logStr);
				break;
			case HOME:
				ui = "/logic/view/Home.fxml";
				loader.setLocation(getClass().getResource(ui));
				root = loadUI(loader);
				logStr += ui;
				HomeGraphic homeGraphic = loader.getController();
				homeGraphic.initializeData(bundle, getPrevBundle());
				Logger.getGlobal().info(logStr);
				break;
			case JOIN:
				loader.setLocation(getClass().getResource("/logic/view/JoinTrip.fxml"));
				root = loadUI(loader);
				logStr += ui;
				Logger.getGlobal().info(logStr);
				break;
			case INFO:
				loader.setLocation(getClass().getResource("/logic/view/TripInfo.fxml"));
				root = loadUI(loader);
				logStr += ui;
				TripInfoGraphic infoGraphic = loader.getController();
				infoGraphic.initializeData(bundle, getPrevBundle());
				Logger.getGlobal().info(logStr);
				break;
			case PREFTRIP:
				loader.setLocation(getClass().getResource("/logic/view/SelectTripPreferences.fxml"));
				root = loadUI(loader);
				logStr += ui;
				SelectTripPreferencesGraphic selGraphic = loader.getController();
				selGraphic.initializeData(bundle, null);
				Logger.getGlobal().info(logStr);
				break;
			case PLAN:
				loader.setLocation(getClass().getResource("/logic/view/PlanTrip.fxml"));
				root = loadUI(loader);
				logStr += ui;
				PlanTripGraphic planGraphic = loader.getController();
				planGraphic.initializeData(bundle, null);
				Logger.getGlobal().info(logStr);
				break;
			case SHARE:
				loader.setLocation(getClass().getResource("/logic/view/ShareTrip.fxml"));
				root = loadUI(loader);
				logStr += ui;
				ShareTripGraphic shareGraphic = loader.getController();
				shareGraphic.initializeData(bundle, null);
				Logger.getGlobal().info(logStr);
				break;
			case REQUESTS:
				loader.setLocation(getClass().getResource("/logic/view/ManageRequests.fxml"));
				root = loadUI(loader);
				logStr += ui;
				ManageRequestGraphic requestGraphic = loader.getController();
				requestGraphic.initializeData(bundle, null);
				Logger.getGlobal().info(logStr);
				break;
			case GAIN:
				loader.setLocation(getClass().getResource("/logic/view/GainPoints.fxml"));
				root = loadUI(loader);
				logStr += ui;
				GainPointsGraphic pointsGraphic = loader.getController();
				pointsGraphic.initializeData(bundle, null);
				Logger.getGlobal().info(logStr);
				break;
			case PROFILE:
				loader.setLocation(getClass().getResource("/logic/view/Profile.fxml"));
				root = loadUI(loader);
				logStr += ui;
				ProfileGraphic profGraphic = loader.getController();
				profGraphic.initializeData(bundle, getPrevBundle());
				Logger.getGlobal().info(logStr);
				break;
			case SETTINGS:
				loader.setLocation(getClass().getResource("/logic/view/Settings.fxml"));
				root = loadUI(loader);
				logStr += ui;
				Logger.getGlobal().info(logStr);
				break;
			case LOGIN:
				loader.setLocation(getClass().getResource("/logic/view/Login.fxml"));
				root = loadUI(loader);
				logStr += ui;
				DesktopSessionContext.getInstance().setSession(null);
				Logger.getGlobal().info(logStr);
				break;
			case REGISTER:
				loader.setLocation(getClass().getResource("/logic/view/Registration.fxml"));
				root = loadUI(loader);
				logStr += ui;
				DesktopSessionContext.getInstance().setSession(null);
				Logger.getGlobal().info(logStr);
				break;
			default:
				break;
		}
		
		DesktopSessionContext.getInstance().addToCenter(root);
	}
	
	private void loadNav(String ui) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource(ui));
			String logStr = "Loaded UI: "+ui;
			Logger.getGlobal().info(logStr);
			DesktopSessionContext.getInstance().addToTop(root);
		} catch (IOException e) {
			Logger.getLogger(DesktopSessionContext.class.getName()).log(Level.SEVERE, null, e);
		}
    }
	
	private Parent loadUI(FXMLLoader loader) {
    	try {
			return loader.load();
		} catch (IOException e) {
			Logger.getLogger(DesktopSessionContext.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
    }
	
	
	/* Setters and getters */
	public Node getPrevNode() {
		return prevNode;
	}
	public void setPrevNode(Node prevNode) {
		this.prevNode = prevNode;
	}
	public Initializable getPrevCtrl() {
		return prevCtrl;
	}
	public void setPrevCtrl(Initializable prevView) {
		this.prevCtrl = prevView;
	}
	public FXMLLoader getLoader() {
		return loader;
	}
	public void setLoader(FXMLLoader loader) {
		this.loader = loader;
	}

	public GUIType getPrevState() {
		return prevState;
	}

	public void setPrevState(GUIType prevState) {
		this.prevState = prevState;
	}

	public Object getPrevBundle() {
		return prevBundle;
	}

	public void setPrevBundle(Object prevBundle) {
		this.prevBundle = prevBundle;
	}
}
