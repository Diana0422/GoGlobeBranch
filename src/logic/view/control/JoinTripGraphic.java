package logic.view.control;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.bean.TripBean;
import logic.control.JoinTripController;
import logic.model.FilterType;
import logic.model.TripCategory;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Session;
import logic.view.control.dynamic.CardGraphic;
import logic.view.filterstrategies.TripFilterManager;
import logic.view.utils.GUIType;
import logic.view.utils.GraphicControl;
import logic.view.utils.GraphicLoader;

public class JoinTripGraphic implements GraphicControl {
	
	@FXML
	private TextField txtSearch;
	
	@FXML
	private Button btnSearch;
	
	@FXML
	private Button btnPlan;
	
	@FXML
	private Label lblMessage;
	
	@FXML
    private GridPane cardsLayout;
	
    @FXML
    private Button btnBack;
    
	private List<TripBean> tripBeans;
	private List<TripBean> filteredTripBeans;
	TripFilterManager filterManager;
	JoinTripController controller;
	private Session session;
	private String searchVal;
	
	public JoinTripGraphic(String searchval) {
		this.searchVal = searchval;
	}

    @FXML
    void back(MouseEvent event) {
    	Stage stage = (Stage) lblMessage.getScene().getWindow();
    	stage.setScene(GraphicLoader.switchView(session.getPrevView(), session.getPrevGraphicControl(), session));
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle resource) {
		filterManager = new TripFilterManager();
		controller = new JoinTripController();
		try {
			if (session != null) {
				this.tripBeans = controller.getSuggestedTrips(session.getUserEmail());
			} else {
				if (searchVal != null) {
					this.tripBeans = controller.searchTrips(this.searchVal);
					this.searchVal = null;
				} else {
					this.tripBeans = controller.searchTrips(txtSearch.getText());	
				}
			}
			CardGraphic cc = new CardGraphic();
			cc.loadCardGrid(cardsLayout, this.tripBeans, session);
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(e.getMessage(), e.toString());
		}
	}
	
	@FXML
	public void search(MouseEvent event) {
		try {
			this.tripBeans = controller.searchTrips(txtSearch.getText());
		} catch (DatabaseException e) {
			AlertGraphic alert = new AlertGraphic();
			alert.display(e.getMessage(), e.getMessage());
		}
		CardGraphic cc = new CardGraphic();
		cc.loadCardGrid(cardsLayout, this.tripBeans, session);
	}
	
	@FXML 
	public void filterByAdventure(MouseEvent event) {
		filteredTripBeans = controller.applyFilterToTrips(tripBeans, null, TripCategory.ADVENTURE);
		CardGraphic cc = new CardGraphic();
		cc.loadCardGrid(cardsLayout, this.filteredTripBeans, session);
	}
	
	@FXML 
	public void filterByFun(MouseEvent event) {
		filteredTripBeans = controller.applyFilterToTrips(tripBeans, null, TripCategory.FUN);
		CardGraphic cc = new CardGraphic();
		cc.loadCardGrid(cardsLayout, this.filteredTripBeans, session);
	}
	
	@FXML 
	public void filterByCulture(MouseEvent event) {
		filteredTripBeans = controller.applyFilterToTrips(tripBeans, null, TripCategory.CULTURE);
		CardGraphic cc = new CardGraphic();
		cc.loadCardGrid(cardsLayout, this.filteredTripBeans, session);
	}
	
	@FXML 
	public void filterByRelax(MouseEvent event) {
		filteredTripBeans = controller.applyFilterToTrips(tripBeans, null, TripCategory.RELAX);
		CardGraphic cc = new CardGraphic();
		cc.loadCardGrid(cardsLayout, this.filteredTripBeans, session);
	}
	
	@FXML 
	public void filterFromAToZ(MouseEvent event) {
		filteredTripBeans = controller.applyFilterToTrips(tripBeans, FilterType.ALPHABETIC, null);
		CardGraphic cc = new CardGraphic();
		cc.loadCardGrid(cardsLayout, this.filteredTripBeans, session);
	}
	
	@FXML
	public void priceBtnClick(MouseEvent event) {
		filteredTripBeans = controller.applyFilterToTrips(tripBeans, FilterType.PRICE, null);
		CardGraphic cc = new CardGraphic();
		cc.loadCardGrid(cardsLayout, this.filteredTripBeans, session);
	}
	

    @FXML
    void onPlanTrip(MouseEvent event) {
    	Stage stage = (Stage) txtSearch.getScene().getWindow();
    	if (session == null) stage.setScene(GraphicLoader.switchView(GUIType.LOGIN, null));
    	if (session != null) stage.setScene(GraphicLoader.switchView(GUIType.PREFTRIP, new SelectTripPreferencesGraphic(), session));
    }

	@Override
	public void setSession(Session session) {
		this.session = session;
	}
}