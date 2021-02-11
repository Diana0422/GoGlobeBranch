package logic.view.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import logic.bean.TripBean;
import logic.control.GainPointsController;
import logic.model.exceptions.LoadGraphicException;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Session;
import logic.view.control.dynamic.CardGraphic;
import logic.view.utils.GUIType;
import logic.view.utils.GraphicControl;
import logic.view.utils.GraphicLoader;

public class GainPointsGraphic implements GraphicControl {
	
	@FXML
	private GridPane cardsLayout;

	@FXML
	private AnchorPane alertPane;

    @FXML
    private Label lblPoints;
    
    private TripBean trip;
    private Session session;

    @FXML
    private Button btnBack;
    private GainPointsController controller;
   

    @FXML
    void back(MouseEvent event) {
    	Stage stage = (Stage) btnBack.getScene().getWindow();
    	stage.setScene(GraphicLoader.switchView(GUIType.HOME, new HomeGraphic(), session));
    }

	@FXML
	void onGainPoints(MouseEvent event) {
		try {
			if (controller.verifyParticipation(session.getUserEmail(), getTrip())) {
				AlertGraphic alert = new AlertGraphic();
				alert.display("Trip validated successfully.", "You gained 100 points");
			} else {
				AlertGraphic alert = new AlertGraphic();
				alert.display("Can't validate trip.", "You don't gain any points");
			}
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(e.getMessage(), e.getCause().toString());
		}
	}
	
	public void setPointsText(String text) {
		lblPoints.setText(text);
	}

	public TripBean getTrip() {
		return trip;
	}

	public void setTrip(TripBean trip) {
		this.trip = trip;
	}
	
	public void loadTrip() {
		try {
			setTrip(controller.getTripOfTheDay(session.getUserEmail()));
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(e.getMessage(), e.getCause().toString());
		}
		if (getTrip() != null) {
			int column = 0;
			int row = 1;
		
			CardGraphic cc = new CardGraphic();
			AnchorPane anchor;
			try {
				anchor = (AnchorPane) cc.initializeNode(trip, cardsLayout, session, trip.isShared());
				cardsLayout.add(anchor, column, row);
				GridPane.setMargin(anchor, new Insets(20));
			} catch (LoadGraphicException e) {
				AlertGraphic alert = new AlertGraphic();
				alert.display(e.getMessage(), e.getCause().toString());
			}
								
			// Set grid height
			cardsLayout.setMaxHeight(Region.USE_PREF_SIZE);
			cardsLayout.setPrefHeight(Region.USE_COMPUTED_SIZE);
			cardsLayout.setMinHeight(Region.USE_COMPUTED_SIZE);
								
			// Set grid width
			cardsLayout.setMaxWidth(Region.USE_PREF_SIZE);
			cardsLayout.setPrefWidth(Region.USE_COMPUTED_SIZE);
			cardsLayout.setMinWidth(Region.USE_COMPUTED_SIZE);
		}
					
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.controller = new GainPointsController();
		loadTrip();
		lblPoints.setText("You have "+session.getUserPoints()+" points.");
	}

	@Override
	public void setSession(Session session) {
		this.session = session;
	}
				
}
