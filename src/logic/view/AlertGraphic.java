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
    private Button btnClose;
	
	 
	 public void setData(Stage alertStage, String message, String description) {
		 lblAlertMessage.setText(message);
		 lblAlertDescription.setText(description);
		 btnClose.setText("Close");
		 btnClose.setOnAction(e-> alertStage.close());
	 }
	 
	 public void display(String message, String description) {
		 Stage popup = new Stage();
		 popup.initModality(Modality.APPLICATION_MODAL);
		 popup.setTitle("Alert");
		 
		 FXMLLoader loader = new FXMLLoader();
		 loader.setLocation(getClass().getResource("/logic/view/Alert.fxml"));
		 try {
			Parent root = loader.load();
			AlertGraphic alert = loader.getController();
			alert.setData(popup, message, description);
			Scene scene = new Scene(root, 624, 424);
			popup.setScene(scene);
			popup.show();
			
		} catch (IOException e1) {
			Logger.getGlobal().log(Level.WARNING, e1.getMessage());
		}
		 
	 }
}
