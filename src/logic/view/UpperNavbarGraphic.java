package logic.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logic.control.ProfileController;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Session;
import logic.view.utils.GraphicControl;
import logic.view.utils.GraphicLoader;

public class UpperNavbarGraphic implements GraphicControl {
	
	@FXML
    private ImageView imgLogo;

    @FXML
    private Label lblLogo;

    @FXML
    private Button btnJoinTrip;

    @FXML
    private Button btnPlanTrip;

    @FXML
    private Button btnProfile;
    
    @FXML
    private Button btnMain;
    
    @FXML
    private Button btnPrizes;

    @FXML
    private Button btnRequests;

    @FXML
    private Button btnHome;

    @FXML
    private MenuButton menuAccount;

    @FXML
    private MenuItem itemRegister;

    @FXML
    private MenuItem itemLogin;

    @FXML
    private MenuItem itemLogout;

    @FXML
    private BorderPane borderpane;
    
    private Session session;
    
    public void setVisibleLoggedButtons(boolean bool) {
    	btnProfile.setVisible(bool);
    	btnRequests.setVisible(bool);
    	btnHome.setVisible(bool);
    	itemLogout.setVisible(bool);
    }

    @FXML
    void displayJoinTrip(MouseEvent event) {
    	changeScene(GUIType.JOIN, new JoinTripGraphic(), session);
    }

    @FXML
    void displayPlanTrip(MouseEvent event) {
    	changeScene(GUIType.PREFTRIP, new SelectTripPreferencesGraphic(), session);
    }
    
    @FXML
    void displayPrizes(MouseEvent event) {
    	//TODO
    }

    @FXML
    void displayProfile(MouseEvent event) {
    	try {
			DesktopSessionContext.getGuiLoader().loadGUI(null, ProfileController.getInstance().getProfileUser(DesktopSessionContext.getInstance().getSession().getSessionEmail()), GUIType.PROFILE);
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(GUIType.MAIN, GUIType.HOME, null, DesktopSessionContext.getInstance().getSession(), e.getMessage(), e.getCause().toString());
		}
    }

    @FXML
    void displayRequests(MouseEvent event) {
    	changeScene(GUIType.REQUESTS, new ManageRequestGraphic(), session);
    }

    @FXML
    void displayHome(MouseEvent event) {
    	changeScene(GUIType.HOME, new HomeGraphic(), session);
    }
    

    @FXML
    void displayMain(MouseEvent event) {
    	changeScene(GUIType.MAIN, null, session);
    }

    @FXML
    void loadLogin(ActionEvent event) {
    	changeScene(GUIType.LOGIN, null, session);
    }
   
    @FXML
    void logout(ActionEvent event) {
    	changeScene(GUIType.LOGIN, null, session);
    }

    @FXML
    void loadRegistration(ActionEvent event) {
    	changeScene(GUIType.REGISTER, null, session);
    }
   
    
    public void changeScene(GUIType view, GraphicControl control, Session session) {
    	Stage stage = (Stage) btnProfile.getScene().getWindow();
    	if (session == null) stage.setScene(GraphicLoader.switchView(view, control));
    	if (session != null) stage.setScene(GraphicLoader.switchView(view, control, session));
    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblLogo.setText("GoGlobe");
		if (DesktopSessionContext.getInstance().getSession() == null) this.setVisibleLoggedButtons(false);
	}

	@Override
	public void setSession(Session session) {
		this.session = session;
	}

}
