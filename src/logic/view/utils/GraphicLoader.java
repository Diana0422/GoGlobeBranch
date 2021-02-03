package logic.view.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import logic.util.Session;
import logic.view.GUIType;
import logic.view.UpperNavbarGraphic;

public class GraphicLoader {
	
	private static final String ITEMPATH = "../";

	private GraphicLoader() {/* empty constructor */}

	public static Scene switchView(GUIType nextView, GraphicControl graphic, Session session) {
		session.setCurrView(nextView, graphic);
		try {
			if (nextView.equals(GUIType.MAIN)) {
				return new Scene(loadFXML(nextView).load());
			} else {
				FXMLLoader loader = loadFXML(nextView);
				if (graphic != null) {
					graphic.setSession(session);
					loader.setController(graphic);
				}
				BorderPane pane = loader.load();
				pane.setTop(loadNavbar(Status.LOGGED, session));
				return new Scene(pane);
			}
		} catch (IOException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "view not found.");
			Logger.getGlobal().log(Level.SEVERE, "view not found.");
			return new Scene(new BorderPane());
		}
	}
	
	public static Scene switchView(GUIType nextView, GraphicControl graphic) {
		try {
			if (nextView.equals(GUIType.MAIN)) {
				return new Scene(loadFXML(nextView).load());
			} else {
				FXMLLoader loader = loadFXML(nextView);
				if (graphic != null) {
					loader.setController(graphic);
				}
				BorderPane pane = loader.load();
				pane.setTop(loadNavbar(Status.UNLOGGED, null));
				return new Scene(pane);
			}
		} catch (IOException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "view not found.");
			Logger.getGlobal().log(Level.SEVERE, "view not found.");
			return new Scene(new BorderPane());
		}
	}

	private static BorderPane loadNavbar(Status status, Session session) throws IOException {
		FXMLLoader loader = loadFXML(GraphicItem.NAVBAR);
		BorderPane nav = loader.load();
		UpperNavbarGraphic navControl = loader.getController();
		navControl.setSession(session);
		
		if (status.equals(Status.LOGGED)) {
			navControl.setVisibleLoggedButtons(true);
			loader.setController(navControl);
			return nav;
		} else {
			navControl.setVisibleLoggedButtons(false);
			loader.setController(navControl);
			return nav;
		}
	}

	private static FXMLLoader loadFXML(GUIType nextView) {
		switch(nextView) {
		case HOME:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "Home.fxml"));
		case JOIN:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "JoinTrip.fxml"));
		case INFO:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "TripInfo.fxml"));
		case PREFTRIP:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "SelectTripPreferences.fxml"));
		case PLAN:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "PlanTrip.fxml"));
		case SHARE:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "ShareTrip.fxml"));
		case REQUESTS:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "ManageRequests.fxml"));
		case GAIN:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "GainPoints.fxml"));
		case PROFILE:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "Profile.fxml"));
		case REGISTER:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "Registration.fxml"));
		case LOGIN:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "Login.fxml"));
		default:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "Main.fxml"));
		}
	}
	
	public static FXMLLoader loadFXML(GraphicItem item) {
		switch(item) {
		case NAVBAR:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "UpperNavbar.fxml"));
		case CARD:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "TripCard.fxml"));
		case ACTIVITY:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "ActivityCard.fxml"));
		case USER:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "UserItem.fxml"));
		case REVIEW:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "ReviewItem.fxml"));
		case REQUEST:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "RequestItem.fxml"));
		case SUGGESTION:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "PlaceSuggestion.fxml"));
		default:
			return new FXMLLoader(GraphicLoader.class.getResource(ITEMPATH + "UpperNavbar.fxml"));
		}
	}

	public static void showAlert(AlertType error, String message) {
		// TODO Auto-generated method stub
		
	}

}
