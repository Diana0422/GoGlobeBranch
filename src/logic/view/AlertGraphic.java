package logic.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertGraphic {
	
	 @FXML
	 private Label lblAlertMessage;

	 @FXML
	 private Label lblAlertDescription;

	 @FXML
	 private Button btnOption1;

	 @FXML
	 private Button btnOption2;
	
	 
	 public void setData(Stage alertStage, String message, String description, GUIType current, GUIType buttonOption2, Object recBundle, Object forBundle) {
		 String buttonOption1 = "Close";
		 lblAlertMessage.setText(message);
		 lblAlertDescription.setText(description);
		 btnOption1.setText(buttonOption1);
		 btnOption2.setText(buttonOption2.toString());
		 
		 btnOption2.setOnAction(e -> {
			 alertStage.close();
			 DesktopSessionContext.getGuiLoader().loadGUIStateful(recBundle, forBundle, buttonOption2, current);
		 	}
		 );
		 
		 btnOption1.setOnAction(e-> alertStage.close());
		 
		 
	 }
	 
	 public void display(GUIType current, GUIType forwardOption, Object recBundle, Object forBundle, String message, String description) {
		 Stage popup = new Stage();
		 popup.initModality(Modality.APPLICATION_MODAL);
		 popup.setTitle("Alert");
		 
		 FXMLLoader loader = new FXMLLoader();
		 loader.setLocation(getClass().getResource("/logic/view/Alert.fxml"));
		 try {
			Parent root = loader.load();
			AlertGraphic alert = loader.getController();
			alert.setData(popup, message, description, current, forwardOption, recBundle, forBundle);
			Scene scene = new Scene(root, 444,271);
			popup.setScene(scene);
			popup.show();
			
		} catch (IOException e1) {
			Logger.getGlobal().log(Level.WARNING, e1.getMessage());
		}
		 
	 }
}
