package logic.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logic.view.exceptions.UnavailableConfigurationException;
import logic.view.utils.GUIType;
import logic.view.utils.GraphicLoader;
import logic.view.utils.PropManager;

public class Launcher extends Application {
	
	@Override
	public void start(Stage stage) {
		
		Scene scene = GraphicLoader.switchView(GUIType.MAIN, null);
 		try {
			initStage(stage, scene);
		} catch (UnavailableConfigurationException e) {
			GraphicLoader.showAlert(e.getMessage(), e.getCause().toString());
		}
		stage.show();	
	}
	
	private void initStage(Stage stage, Scene scene) throws UnavailableConfigurationException {
		stage.setTitle(PropManager.getInstance().getProperty("title"));
		
		stage.setWidth(Integer.valueOf(PropManager.getInstance().getProperty("width")));
		stage.setHeight(Integer.valueOf(PropManager.getInstance().getProperty("height")));
		stage.centerOnScreen();
		stage.getIcons().add(new Image(getClass().getResourceAsStream("res/images/goglobe-logo.png")));
		scene.getStylesheets().addAll(
				getClass().getResource("/logic/view/res/css/navbar.css").toExternalForm(),
				getClass().getResource("/logic/view/res/css/application.css").toExternalForm());
		stage.setScene(scene);
	}

	public static void main(String[] args) {
		launch();
	}
}
