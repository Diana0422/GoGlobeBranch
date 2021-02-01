package logic.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.util.Session;
import logic.view.utils.GraphicControl;
import logic.view.utils.GraphicLoader;

public class HomeGraphic implements GraphicControl {
	
	@FXML
	private Label lblWelcome;
	
	@FXML
    private Label lblPoints;
	
	@FXML
	private Button btnJoin;

	@FXML
	private Button btnPlan;
	
    @FXML
    private Button btnGain;
	
    private Session session;
	/*Action methods */

	@FXML
	void forwardGainPoints(MouseEvent event) {
		Stage stage = (Stage) lblWelcome.getScene().getWindow();
		stage.setScene(GraphicLoader.switchView(GUIType.GAIN, null, session));
	}
	
	@FXML
	void forwardJoinTrip(MouseEvent event) {
		Stage stage = (Stage) lblWelcome.getScene().getWindow();
		stage.setScene(GraphicLoader.switchView(GUIType.JOIN, new JoinTripGraphic(), session));
	}

	@FXML
	void forwardPlanTrip(MouseEvent event) {
		Stage stage = (Stage) lblWelcome.getScene().getWindow();
		stage.setScene(GraphicLoader.switchView(GUIType.PREFTRIP, new SelectTripPreferencesGraphic(), session));
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblWelcome.setText("Welcome "+session.getUserName()+" "+session.getUserSurname());
		lblPoints.setText(Integer.toString(session.getUserPoints()));
		
	}

	@Override
	public void setSession(Session session) {
		this.session = session;
		System.out.println("home session:"+this.session);
	}
}