package logic.view.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.bean.PlanTripBean;
import logic.bean.SessionBean;
import logic.control.PlanTripController;
import logic.model.exceptions.FormInputException;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Session;
import logic.view.utils.GUIType;
import logic.view.utils.GraphicControl;
import logic.view.utils.GraphicLoader;

public class ShareTripGraphic implements GraphicControl {
	
	private static final String IC_ADDIMAGE = "icAddImage.png";
	private final FileChooser fileChooser = new FileChooser();
	private Desktop desktop = Desktop.getDesktop();
	PlanTripBean planTripBean;
	
    @FXML
    private TextField tfMaxParticipants;

    @FXML
    private TextField tfMinimumAge;

    @FXML	
    private TextField tfMaximumAge;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnNext;

    @FXML
    private TextArea taTripDescription;

    @FXML
    private Button btnChooseFile;

    @FXML
    private Label lblFilePath;
    
    @FXML 
    private Label lblErrorMsg;
	private Session session;
	private SessionBean sessionBean;
	private PlanTripBean planBean;
	private PlanTripController controller;
	
	public ShareTripGraphic(SessionBean sessionBean, PlanTripBean planBean) {
		this.sessionBean = sessionBean;
		this.planBean = planBean;
		this.controller = new PlanTripController();
	}

    @FXML
    void onCancelClick(ActionEvent event) {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		stage.setScene(GraphicLoader.switchView(GUIType.PLAN, new PlanTripGraphic(planBean, sessionBean), session));
    }

    @FXML
    void onNextClick(ActionEvent event) {
    	planBean.getTripBean().setShared(true);
    	planBean.getTripBean().setMinAge(tfMinimumAge.getText());
    	planBean.getTripBean().setMaxAge(tfMaximumAge.getText());
    	planBean.getTripBean().setDescription(taTripDescription.getText());
    	planBean.getTripBean().setMaxParticipants(tfMaxParticipants.getText());
    		
    	try {
				planBean.validateSharingPref();
				controller.saveTrip(planBean.getTripBean(), session.getUserEmail()); 
				Stage stage = (Stage) lblErrorMsg.getScene().getWindow();
				stage.setScene(GraphicLoader.switchView(GUIType.HOME, new HomeGraphic(), session));
			
		} catch (FormInputException e) {
	    	planBean.getTripBean().setShared(false);
			planBean.getTripBean().setMinAge("");
	    	planBean.getTripBean().setMaxAge("");
	    	planBean.getTripBean().setDescription("");
	    	planBean.getTripBean().setMaxParticipants("");
			lblErrorMsg.setText(e.getMessage());
		} catch (DatabaseException e) {
			AlertGraphic graphic = new AlertGraphic();
			graphic.display(e.getMessage(), e.getCause().toString());
		}
    }
    
    @FXML 
    void openFilePicker(ActionEvent event){
    	  fileChooser.getExtensionFilters().addAll(
                  new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG", "*.jpg","*.JPEG", "*.jpeg"),
                  new FileChooser.ExtensionFilter("PNG", "*.png", "*.PNG")
              );
    	 Stage filePickerStage = new Stage();
    	 File file = fileChooser.showOpenDialog(filePickerStage);
         if (file != null) {
             openFile(file);
             lblFilePath.setText(file.getPath());
         }
    }
    
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException e) {
        	AlertGraphic graphic = new AlertGraphic();
			graphic.display(e.getMessage(), e.getCause().toString());
        }
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	Image icAddImage = new Image("/logic/view/res/images/" + IC_ADDIMAGE);
		ImageView imgView = new ImageView(icAddImage);
		btnChooseFile.setGraphic(imgView);
		
	}

	@Override
	public void setSession(Session session) {
		this.session = session;
		
	}

}
