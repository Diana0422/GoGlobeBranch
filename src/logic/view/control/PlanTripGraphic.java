package logic.view.control;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.bean.ActivityBean;
import logic.bean.PlanTripBean;
import logic.bean.SessionBean;
import logic.control.PlanTripController;
import logic.model.PlaceBean;
import logic.model.exceptions.APIException;
import logic.model.exceptions.FormInputException;
import logic.model.exceptions.TripNotCompletedException;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Session;
import logic.view.control.dynamic.ActivityCardGraphic;
import logic.view.threads.LoadSuggestionsFX;
import logic.view.threads.LoadVBox;
import logic.view.utils.GUIType;
import logic.view.utils.GraphicControl;
import logic.view.utils.GraphicLoader;

public class PlanTripGraphic implements GraphicControl {

    @FXML
    private VBox vbDays;

    @FXML
    private Text txtTripTitle;
    
    @FXML
    private Text txtCountry;

    @FXML
    private Text txtDayNumber;

    @FXML
    private TextField tfLocation;
    
    @FXML 
    private Label lblErrorMsg;
    
    @FXML
    private Label lblNoSugg;

    @FXML
    private VBox vbActivities;
    
    @FXML	
    private ScrollPane spActivities;

    @FXML
    private Button btnSaveLocation;

    @FXML
    private TextField tfActivityTitle;

    @FXML
    private TextField tfActivityTime;
    
    @FXML
    private TextField tfActivityCost;

    @FXML
    private TextArea taActivityDescription;

    @FXML
    private Button btnAddActivity;
    
    @FXML
    private Button btnSaveTrip;
    
    @FXML
    private Button btnShareTrip;

    @FXML
    private Text txtLocation;
    
    @FXML	
    private Text txtLocationForm;
    
    @FXML	
    private AnchorPane apActivityForm;
    
    @FXML 
    private ScrollPane spSuggestions;
    
    @FXML
    private VBox vbSuggestions;


	private Session session;
	private SessionBean sessionBean;
	private PlanTripBean planTripBean;
	private PlanTripController controller;
	
	public PlanTripGraphic(PlanTripBean bean, SessionBean sessionBean) {
		this.planTripBean = bean;
		this.sessionBean = sessionBean;
		controller = new PlanTripController();
	}

    public PlanTripGraphic(PlanTripBean bean) {
    	this.planTripBean = bean;
    	controller = new PlanTripController();
	}

	@FXML
    void addActivity(ActionEvent event) {
    	//create new activity from user input 
    	ActivityBean newActivity = new ActivityBean();
    	newActivity.setTitle(tfActivityTitle.getText());
    	newActivity.setTime(tfActivityTime.getText());
    	newActivity.setDescription(taActivityDescription.getText());
    	newActivity.setEstimatedCost(tfActivityCost.getText());
 
    	try {
			if (newActivity.validateActivity()) {
				controller.addActivity(this.planTripBean.getTripBean(), planTripBean.getPlanningDay(), newActivity);
				loadActivity(newActivity);
				lblErrorMsg.setText("");
			}
		} catch (FormInputException e) {
			//reset activity form's text fields
	    	
			lblErrorMsg.setText(e.getMessage());
		}	
    	
    	tfActivityTitle.setText("");
    	tfActivityTime.setText("");
    	taActivityDescription.setText("");
    	tfActivityCost.setText("");
 
    
    }

    @FXML
    void saveLocation(ActionEvent event) {
    	try {
        	// validate location name
        	if (controller.checkLocationValidity(tfLocation.getText(), this.planTripBean.getTripBean())) {
            	planTripBean.setLocation(tfLocation.getText());
    			if (planTripBean.validateLocation())
    				planTripBean.saveLocation();
    			refresh();
        	} else {
        		lblErrorMsg.setText("This city is not in country "+planTripBean.getTripBean().getCountry());
        	}
		} catch (FormInputException | APIException e) {
			lblErrorMsg.setText(e.getMessage());
		}
    	
    }
    
    @FXML
    void onSaveTripClick(ActionEvent event) {
    	 try {
			planTripBean.validateTrip();
			controller.saveTrip(planTripBean.getTripBean(), session.getUserEmail());
			Stage stage = (Stage) lblErrorMsg.getScene().getWindow();
			stage.setScene(GraphicLoader.switchView(GUIType.HOME, null, session));
		} catch (TripNotCompletedException e) {
			lblErrorMsg.setText(e.getMessage());
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(e.getMessage(), e.getCause().toString());
		}     	
    }
    
    @FXML
    void onShareTripClick(ActionEvent event) {
    	try {
			planTripBean.validateTrip();
			Stage stage = (Stage) lblErrorMsg.getScene().getWindow();
			stage.setScene(GraphicLoader.switchView(GUIType.SHARE, new ShareTripGraphic(sessionBean, planTripBean), session));
		} catch (TripNotCompletedException e) {
			lblErrorMsg.setText(e.getMessage());
		}   	    
    }
	
	//load an activity in GUI
	private void loadActivity(ActivityBean activityBean) {
		ActivityCardGraphic graphic = new ActivityCardGraphic();
		graphic.loadActivityCard(vbActivities, activityBean);
	}

	
	//refresh the scene
	private void refresh() {
		Stage stage = (Stage) lblErrorMsg.getScene().getWindow();
		stage.setScene(GraphicLoader.switchView(GUIType.PLAN, this, session));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		String logStr = "TRIP TITLE: " + planTripBean.getTripBean().getTitle();
		Logger.getGlobal().info(logStr);
			
		//set GUI elements visibility
		txtTripTitle.setText(planTripBean.getTripBean().getTitle());
		txtCountry.setText(planTripBean.getTripBean().getCountry());
		txtDayNumber.setText(Integer.toString(planTripBean.getPlanningDay() + 1));
		if (planTripBean.checkDay()) {
			logStr = "THIS DAY HAS NO LOCATION";
			Logger.getGlobal().info(logStr);

			txtLocationForm.setVisible(true);					
			tfLocation.setVisible(true);
			btnSaveLocation.setVisible(true);
			txtLocation.setVisible(false);
			apActivityForm.setVisible(false);
			vbActivities.setVisible(false);	
			spActivities.setVisible(false);
				
		}else{
			logStr = "LOCATION FOUND: SHOWING THINGS.";
			Logger.getGlobal().info(logStr);

			txtLocationForm.setVisible(false);
			tfLocation.setVisible(false);
			btnSaveLocation.setVisible(false);
			txtLocation.setVisible(true);
			apActivityForm.setVisible(true);
			vbActivities.setVisible(true);
			spActivities.setVisible(true);

			txtLocation.setText("Location: " + planTripBean.getTripBean().getDays().get(planTripBean.getPlanningDay()).getLocationCity());
				
			//adds day's activities to the GUI
			List<ActivityBean> activities = planTripBean.getTripBean().getDays().get(planTripBean.getPlanningDay()).getActivities();
			for (int i = 0; i < activities.size(); i++) {
				loadActivity(activities.get(i));						
			}
				
				
			List<PlaceBean> places;
			try {
				int planningDay = planTripBean.getPlanningDay();
				String location = planTripBean.getTripBean().getDays().get(planningDay).getLocationCity();
				places = controller.getNearbyPlaces(location, planTripBean.getTripBean().getCategory1());
				LoadVBox suggestionLoader = new LoadVBox(this.vbSuggestions, places);
				Thread thread = new LoadSuggestionsFX(suggestionLoader);
				thread.setDaemon(true);
				thread.start();
			} catch (APIException e) {
				lblNoSugg.setText(e.getMessage());
			}
		}
		
		//setup side bar buttons	
		for (int i = 0; i < planTripBean.getTripBean().getDays().size(); i++) {
			Button btnDay = new Button("Day " + (i+1));
			btnDay.setPrefHeight(50);
			btnDay.setPrefWidth(210);
			btnDay.setFont(new Font(18));					
			btnDay.setId("btn");
				
			//setting event handler for button click
			btnDay.setUserData(i);
			btnDay.setOnAction( event -> {
				planTripBean.setPlanningDay((int)btnDay.getUserData());
				refresh();					
			});					
			vbDays.getChildren().add(btnDay);
		}
			
	}

	@Override
	public void setSession(Session session) {
		this.session = session;
	}
	
	
}

