package logic.view;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage){
		try {
			/* Set FXML controller factory */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/logic/view/Stage.fxml"));
			
			loader.setControllerFactory(arg0-> DesktopSessionContext.getInstance());
			Parent root = loader.load();
			
			/* Set the scene */
			Scene scene = new Scene(root,1300,700);
			scene.getStylesheets().addAll(
					getClass().getResource("/logic/view/css/navbar.css").toExternalForm(),
					getClass().getResource("/logic/view/css/application.css").toExternalForm(),
					getClass().getResource("/logic/view/css/login.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(null, null, null, null, e.getMessage(), e.getCause().toString());
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
