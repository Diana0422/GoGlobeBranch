package logic.view.control;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.bean.PlanTripBean;
import logic.bean.TripBean;
import logic.control.FormatManager;
import logic.control.PlanTripController;
import logic.model.TripCategory;
import logic.model.exceptions.FormInputException;
import logic.util.Session;
import logic.view.utils.GUIType;
import logic.view.utils.GraphicControl;
import logic.view.utils.GraphicLoader;

public class SelectTripPreferencesGraphic implements GraphicControl {

    @FXML
    private TextField tfTripTitle;
    
    @FXML
    private TextField tfCountry;
    
    @FXML
    private Label lblErrorMsg;

    @FXML
    private TextField tfDepartureDate;

    @FXML
    private TextField tfReturnDate;

    @FXML
    private ComboBox<String> cbCategory1;

    @FXML
    private ComboBox<String> cbCategory2;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnNext;
    
    private Session session;
    private PlanTripController controller;

    @FXML
    void onCancelClick(ActionEvent event) {
    	Stage stage = (Stage) lblErrorMsg.getScene().getWindow();
    	stage.setScene(GraphicLoader.switchView(session.getPrevView(), session.getPrevGraphicControl(), session));
    }

    @FXML
    void onNextClick(ActionEvent event) {
    	PlanTripBean planTripBean = new PlanTripBean();
    	TripBean tripBean = new TripBean();
    	tripBean.setTitle(tfTripTitle.getText());
    	tripBean.setCountry(tfCountry.getText());
    	tripBean.setDepartureDate(tfDepartureDate.getText());
    	tripBean.setReturnDate(tfReturnDate.getText());
    	tripBean.setCategory1(cbCategory1.getValue());
    	tripBean.setCategory2(cbCategory2.getValue());
    	Date depDate = FormatManager.parseDate(tripBean.getDepartureDate());
		Date retDate = FormatManager.parseDate(tripBean.getReturnDate());
    	long tripLength = controller.calculateTripLength(depDate, retDate) + 1;
    	tripBean.setTripLength(tripLength);
    	controller.addDays(tripBean);
    	planTripBean.setTripBean(tripBean);
    	
    	//Validate form and load next GUI
    	try {
	    	if (planTripBean.validateForm()) {
	    		Stage stage = (Stage) btnNext.getScene().getWindow();
	    		stage.setScene(GraphicLoader.switchView(GUIType.PLAN, new PlanTripGraphic(planTripBean), session));
	    	} 
    	}catch (FormInputException e){
    		tripBean.setTitle("");
			tripBean.setDepartureDate("");
			tripBean.setReturnDate("");
			tripBean.setCategory1("");
			tripBean.setCategory2("");
    		lblErrorMsg.setText(e.getMessage());
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		controller = new PlanTripController();
		TripCategory[] categories = TripCategory.values();
		for (int i = 0; i < categories.length; i++) {
			cbCategory1.getItems().add((categories[i].toString()));
			cbCategory2.getItems().add((categories[i].toString()));			
		}
		cbCategory1.setValue("NONE");
		cbCategory2.setValue("NONE");
	}

	@Override
	public void setSession(Session session) {
		this.session = session;
	}
	

}
