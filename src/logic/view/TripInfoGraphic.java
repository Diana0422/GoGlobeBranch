package logic.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.bean.ActivityBean;
import logic.bean.DayBean;
import logic.bean.TripBean;
import logic.bean.UserBean;
import logic.control.FlightController;
import logic.control.JoinTripController;
import logic.model.exceptions.LoadGraphicException;
import logic.model.exceptions.UnloggedException;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Session;
import logic.view.utils.GraphicControl;
import logic.view.utils.GraphicLoader;

public class TripInfoGraphic implements GraphicControl {
	
	@FXML
    private AnchorPane tripBackground;
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Label lblTitle;
	
	@FXML
	private Label lblPrice;
	
	@FXML
	private Label lblDescription;
	
	@FXML
	private Label lblDeparture;
	
	@FXML
	private Label lblReturn;
	
    @FXML
    private Label lblOrigin;

    @FXML
    private Label lblDestination;

    @FXML
    private Label lblCarrier;

    @FXML
    private Label lblTicketPrice;
	
	@FXML
	private Label lblCategory1;
	
	@FXML
	private Label lblCategory2;
	
	@FXML
	private ImageView imgCategory1;
	
	@FXML
	private ImageView imgCategory2;
	
	@FXML
    private VBox boxOrganizer;

    @FXML
    private VBox boxTravelers;
    
    @FXML
    private VBox boxFlight;
    
    @FXML
    private Button btnBack;
    
    JoinTripController controller;
	private TripBean tripBean;
	private Session session;
    public static final String WIDGET_ERROR = "Widget loading error.";

    public TripInfoGraphic(TripBean tripBean) {
		this.tripBean = tripBean;
	}

	@FXML
    void back(MouseEvent event) {
    	Stage stage = (Stage) btnBack.getScene().getWindow();
    	if (session == null) stage.setScene(GraphicLoader.switchView(GUIType.JOIN, new JoinTripGraphic(null)));
    	if (session != null) stage.setScene(GraphicLoader.switchView(GUIType.JOIN, new JoinTripGraphic(null), session));
    }
	
	public void setCategory1Image(Image cat1Img) {
		imgCategory1.setImage(cat1Img);
	}
	
	public void setCategory2Image(Image cat2Img) {
		imgCategory2.setImage(cat2Img);
	}
	
	private void addDayTabs(List<DayBean> days) {
		for (int i=0; i<days.size(); i++) {
			
			VBox table = new VBox();
			VBox container = new VBox();
		
			container.setPrefSize(500f,500f);
			container.setPadding(new Insets(10));
			container.setId("tab-container");
			
			Label locationTitle = new Label("Location:");
			container.getChildren().add(locationTitle);
			
			Label location = new Label();
			location.setText(days.get(i).getLocationCity());
			container.getChildren().add(location);
			
			Label activitiesTitle = new Label("Activities of the day:");
			container.getChildren().add(activitiesTitle);
			
			List<ActivityBean> activities = days.get(i).getActivities();
			for (ActivityBean bean: activities) {
				displayActivity(table, bean);
			}
			ScrollPane scroll = new ScrollPane(table);
			scroll.setPrefSize(500f, 500f);
			
			container.getChildren().add(scroll);
			
			Tab tab = new Tab("Day "+Integer.toString(i+1));
			tab.setContent(container);
			tabPane.getTabs().add(tab);
		}
	}
	
	
	private void displayFlightInfo() {
		String notDefined = "N/D";
		int price; 
		
		try {
			if ((price = FlightController.getInstance().retrieveFlightPrice(tripBean)) == 0) {
				lblTicketPrice.setText(notDefined);
			} else {
				lblTicketPrice.setText(price+"€");
			}
			
			lblOrigin.setText(FlightController.getInstance().retrieveFlightOrigin(tripBean));
			lblDestination.setText(FlightController.getInstance().retrieveFlightDestination(tripBean));
			lblCarrier.setText(FlightController.getInstance().retrieveFlightCarrier(tripBean));
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(GUIType.INFO, GUIType.HOME, null, DesktopSessionContext.getInstance().getSession(), e.getMessage(), e.getCause().toString());
		}
	
	}
	
	
	private void displayActivity(VBox vbox, ActivityBean activity) {
		ActivityItemGraphic graphic = new ActivityItemGraphic();
		AnchorPane anchor;
		try {
			anchor = (AnchorPane) graphic.initializeNode(activity);
			vbox.getChildren().add(anchor);
		} catch (LoadGraphicException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(GUIType.JOIN, GUIType.HOME, null, DesktopSessionContext.getInstance().getSession(), WIDGET_ERROR, "Something unexpected occurred loading activity cards.");
		}
	}

	@FXML
	void joinTrip(MouseEvent event) {
		if (session != null) {
			try {
				if (controller.sendRequest(tripBean.getTitle(), session.getUserEmail())) {
					AlertGraphic alert = new AlertGraphic();
					alert.display(GUIType.INFO, GUIType.REQUESTS, null, tripBean, "Request sent to organizer.", "Choose an option.");
				} else {
					AlertGraphic alert = new AlertGraphic();
					alert.display(GUIType.INFO, GUIType.REQUESTS, null, tripBean, "You are not eligible to join.", "Choose an option.");
				}
			} catch (DatabaseException | UnloggedException e) {
				AlertGraphic alert = new AlertGraphic();
				alert.display(GUIType.INFO, GUIType.HOME, null, session, e.getMessage(), e.getCause().toString());
			}
		} else {
			AlertGraphic alert = new AlertGraphic();
			alert.display(GUIType.INFO, GUIType.LOGIN, null, tripBean, "You are not logged in.", "Register or login first.");
		}
	}
	
	private void displayOrganizer(TripBean trip) {
		UserBean organizer = trip.getOrganizer();
		UserItemGraphic graphic = new UserItemGraphic();
		try {
			AnchorPane anchor = (AnchorPane) graphic.initializeNode(organizer, trip, this.session);
			boxOrganizer.getChildren().add(anchor);
		} catch (LoadGraphicException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(GUIType.JOIN, GUIType.HOME, null, session, WIDGET_ERROR, "Something unexpected occurred displaying organizer.");
		}
	}
	
	private void initializeParticipants(TripBean bean) {
		List<UserBean> participants = bean.getParticipants();
		if (participants != null) {
			for (UserBean user: participants) {
				displayParticipant(user);
			}
		}
	}

	private void displayParticipant(UserBean user) {
		UserItemGraphic graphic = new UserItemGraphic();
		AnchorPane anchor;
		try {
			anchor = (AnchorPane) graphic.initializeNode(user, this.tripBean, this.session);
			boxTravelers.getChildren().add(anchor);
		} catch (LoadGraphicException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(GUIType.JOIN, GUIType.HOME, null, session, WIDGET_ERROR, "Something unexpected occurred displaying user.");
		}	
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.controller = new JoinTripController();
		initializeParticipants(this.tripBean);
		displayOrganizer(this.tripBean);
		lblTitle.setText(this.tripBean.getTitle());
		lblPrice.setText(this.tripBean.getPrice()+"€");
		lblDescription.setText(this.tripBean.getDescription());
		lblDeparture.setText(this.tripBean.getDepartureDate());
		lblReturn.setText(this.tripBean.getReturnDate());
		lblCategory1.setText(this.tripBean.getCategory1());
		lblCategory2.setText(this.tripBean.getCategory2());
		addDayTabs(this.tripBean.getDays());
		displayFlightInfo();
	}

	@Override
	public void setSession(Session session) {
		this.session = session;
	}

}