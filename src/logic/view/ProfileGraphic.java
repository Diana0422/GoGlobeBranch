package logic.view;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.Rating;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.bean.ReviewBean;
import logic.bean.TripBean;
import logic.bean.UserBean;
import logic.control.FormatManager;
import logic.control.ProfileController;
import logic.control.ReviewUserController;
import logic.model.exceptions.LoadGraphicException;
import logic.model.exceptions.UnloggedException;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Session;
import logic.view.utils.GraphicControl;
import logic.view.utils.GraphicLoader;

public class ProfileGraphic implements GraphicControl {

	
	@FXML
    private VBox vbReviews;
    
    @FXML
    private RadioButton rdTraveler;

    @FXML
    private RadioButton rdOrganizer;

    @FXML
    private Rating rating;

    @FXML
    private Button btnPost;

    @FXML
    private TextArea txtComment;
    
    @FXML
    private TextField txtTitle;
    
    @FXML
    private Label lblError;

    @FXML
    private GridPane upcomingGrid;

    @FXML
    private GridPane previousGrid;

    @FXML
    private GridPane myTripsGrid;

    @FXML
    private Text txtNameSurname;

    @FXML
    private Label txtAge;

    @FXML
    private ImageView ivAdventureAttitude;

    @FXML
    private ImageView ivRelaxAttitude;

    @FXML
    private ImageView ivFunAttitude;

    @FXML
    private ImageView ivCultureAttitude;

    @FXML
    private ImageView ivProfilePic;
    
    @FXML
    private Label lblFun;

    @FXML
    private Label lblAdv;

    @FXML
    private Label lblCult;

    @FXML
    private Label lblRelax;
    
    @FXML
    private Button btnBack;
   
    @FXML
    private Rating ratingTra;

    @FXML
    private Rating ratingOrg;
    
    @FXML 
    private TextArea taBio;
    
    @FXML 
    private Button btnSaveBio;
    
    @FXML
    private Label lblUserBio;
    
    private UserBean target;
    private TripBean trip;
    private Number vote;
    private Session session;
    
    public ProfileGraphic(UserBean bean) {
    	this.target = bean;
    }
    
    public ProfileGraphic(UserBean bean, TripBean trip) {
    	this.target = bean;
    	this.trip = trip;
    }

    @FXML
    void back(MouseEvent event) {
    	Stage stage = (Stage) lblUserBio.getScene().getWindow();
    	if (session != null) stage.setScene(GraphicLoader.switchView(session.getPrevView(), session.getPrevGraphicControl(), session));
    	if (session == null) stage.setScene(GraphicLoader.switchView(GUIType.INFO, new TripInfoGraphic(this.trip)));
    }	
    
    @FXML
    void saveBio(MouseEvent event) {
    	try {
			ProfileController.getInstance().updateUserBio(session.getUserEmail(), taBio.getText());
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(e.getMessage(), e.getCause().toString());
		}
    }
    
    @FXML
    void postReview(MouseEvent event) {
    	double d = (double) getVote();
    	
    	Date today = Calendar.getInstance().getTime();
    	String date = FormatManager.formatDate(today);
    	ReviewBean bean = new ReviewBean();
    	bean.setTitle(txtTitle.getText());
    	bean.setVote(d);
    	bean.setDate(date);
    	bean.setComment(txtComment.getText());
    	
    	if (session != null) {
        	bean.setReviewerName(session.getUserName());
        	bean.setReviewerSurname(session.getUserSurname());
        	
        	try {
            	if (rdTraveler.isSelected()) {
        			ReviewUserController.getInstance().postReview("TRAVELER", d, txtComment.getText(), txtTitle.getText(), session.getUserEmail(), target.getEmail(), target);
            	} else {
        			ReviewUserController.getInstance().postReview("ORGANIZER", d, txtComment.getText(), txtTitle.getText(), session.getUserEmail(), target.getEmail(), target);
            	}
        	} catch (DatabaseException e) {
				AlertGraphic alert = new AlertGraphic();
				alert.display(e.getMessage(), e.getCause().toString());
    		} catch (UnloggedException e) {
    			lblError.setText(e.getMessage());
			} 
       
        	ReviewItemGraphic graphic = new ReviewItemGraphic();
    		try {
    			AnchorPane anchor = (AnchorPane) graphic.initializeNode(bean);
    			vbReviews.getChildren().add(anchor);
    		} catch (LoadGraphicException e) {
				AlertGraphic alert = new AlertGraphic();
				alert.display(e.getMessage(), e.getCause().toString());
    		}
    	} else {
    		lblError.setText("You need to log in first");
    	}
    }
 
	
	public void displayOrganizerRating(double rating) {
		ratingOrg.setRating(rating);
	}
	
	public void displayTravelerRating(double rating) {
		ratingTra.setRating(rating);
	}
	
	private void displayReviews(List<ReviewBean> list) {
		ReviewItemGraphic graphic = new ReviewItemGraphic();
		for (ReviewBean bean: list) {
			try {
				AnchorPane anchor = (AnchorPane) graphic.initializeNode(bean);
				vbReviews.getChildren().add(anchor);
			} catch (LoadGraphicException e) {
				AlertGraphic alert = new AlertGraphic();
				alert.display(e.getMessage(), e.getCause().toString());
			}
		}
	}

	public Number getVote() {
		return vote;
	}

	public void setVote(Number vote) {
		this.vote = vote;
	}

	public UserBean getTarget() {
		return target;
	}

	public void setTarget(UserBean target) {
		this.target = target;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		try {
			this.target = ProfileController.getInstance().getProfileUser(target.getEmail());
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(e.getMessage(), e.getCause().toString());
		}
		target.setGraphic(this); // Set the reference to this controller in the user bean to receive updates

		List<TripBean> myTripBeans = null;
		List<TripBean> upcomingTripBeans = null;
		List<TripBean> previousTripBeans = null;
		CardGraphic cc = new CardGraphic();
		
		// Display trips planned by user
		try {
			myTripBeans = ProfileController.getInstance().getMyTrips(target.getEmail());
			cc.loadCardGrid(myTripsGrid, myTripBeans, session);
		
			// Display target user's upcoming trips
			upcomingTripBeans = ProfileController.getInstance().getUpcomingTrips(target.getEmail());
			cc.loadCardGrid(upcomingGrid, upcomingTripBeans, session);
			
			// Display target user's passed trips
			previousTripBeans = ProfileController.getInstance().getRecentTrips(target.getEmail());
			cc.loadCardGrid(previousGrid, previousTripBeans, session);
			
			//Display user attitude
			Map<String, Integer> attitude = ProfileController.getInstance().getPercentageAttitude(target.getEmail());
			lblFun.setText(attitude.get("FUN")+"%");
			lblAdv.setText(attitude.get("ADVENTURE")+"%");
			lblCult.setText(attitude.get("CULTURE")+"%");
			lblRelax.setText(attitude.get("RELAX")+"%");
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(e.getMessage(), e.getCause().toString());
		}
		
		txtNameSurname.setText(target.getName()+" "+target.getSurname());
		txtAge.setText(Integer.toString(target.getAge()));
		String targetBio = target.getBio();
		if (targetBio == null || targetBio.equals("")) {
			targetBio = "User has no Bio.";
		}
		
		
		//check if loaded profile is logged user's profile
		if (DesktopSessionContext.getInstance().getSession() == null || !target.getEmail().equals(DesktopSessionContext.getInstance().getSession().getSessionEmail())) {
			taBio.setVisible(false);
			btnSaveBio.setVisible(false);			
			lblUserBio.setText(targetBio);
			btnPost.setDisable(false);
		}else{
			btnPost.setDisable(true);
			taBio.setText(targetBio);
			lblUserBio.setVisible(false);
		}
		
		// Display ratings
		displayOrganizerRating(target.getStatsBean().getOrgRating());
		displayTravelerRating(target.getStatsBean().getTravRating());
		
		
		// Fetch bean reviews and display them
		displayReviews(target.getReviews());
		
		// Initialize listener on rating control.
		rating.ratingProperty().addListener((arg0, arg1, arg2) -> setVote(arg2));
	}

	@Override
	public void setSession(Session session) {
		this.session = session;		
	}


}

	
	

