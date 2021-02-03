package logic.view.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logic.view.GUIType;
import logic.view.exceptions.UnavailableConfigurationException;

public class Launcher extends Application {
	
	@Override
	public void start(Stage stage) {
		
		Scene scene = GraphicLoader.switchView(GUIType.MAIN, null);
 		try {
			initStage(stage, scene);
		} catch (UnavailableConfigurationException e) {
			GraphicLoader.showAlert(AlertType.ERROR, e.getMessage());
		}
		stage.show();	
	}
	
	private void initStage(Stage stage, Scene scene) throws UnavailableConfigurationException {
		stage.setTitle(PropManager.getInstance().getProperty("title"));
		
		stage.setWidth(Integer.valueOf(PropManager.getInstance().getProperty("width")));
		stage.setHeight(Integer.valueOf(PropManager.getInstance().getProperty("height")));
		stage.centerOnScreen();
		stage.getIcons().add(new Image(getClass().getResourceAsStream("../images/icons8-around-the-globe-50.png")));
		scene.getStylesheets().addAll(
				getClass().getResource("/logic/view/css/navbar.css").toExternalForm(),
				getClass().getResource("/logic/view/css/application.css").toExternalForm(),
				getClass().getResource("/logic/view/css/login.css").toExternalForm());
		stage.setScene(scene);
	}

	public static void main(String[] args) {
		launch();
	}
}
