package logic.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import logic.model.PlaceBean;
import logic.model.exceptions.LoadGraphicException;

public class SuggestionCardGraphic {
	

	private static final String  IC_SUGG_BTN = "googlemaps-icon-50.png";
	private static final String WIDGET_ERROR = "Widget loading error.";
	private String placeName;

    @FXML
    private ImageView ivPlaceIcon;

    @FXML
    private Label lblPlaceName;

    @FXML
    private Label lblPlaceAddress;

    @FXML
    private Label lblOpeningHours;

    @FXML
    private Button btnAddSuggestion;
    
    @FXML 
    public void lookupPlace(MouseEvent event){
    	String url = "https://maps.google.com/?q=" + placeName.replace(" ", "+");
    	try {
			Desktop.getDesktop().browse(new URL(url).toURI());
		} catch (MalformedURLException | URISyntaxException e) {
			AlertGraphic graphic = new AlertGraphic();
			graphic.display(GUIType.PLAN, GUIType.HOME, null, DesktopSessionContext.getInstance().getSession(), "The url is not correct.", e.toString());
		} catch (IOException e) {
			AlertGraphic graphic = new AlertGraphic();
			graphic.display(GUIType.PLAN, GUIType.HOME, null, DesktopSessionContext.getInstance().getSession(), e.getMessage(), e.toString());
		}
    }
    
    public void setData(PlaceBean place) {

    	placeName = place.getName();
    	lblPlaceName.setText(place.getName());
    	String placeAddress = place.getAddress();
    	int ind = placeAddress.indexOf("<");
    	if (ind != -1) {
    		placeAddress = placeAddress.substring(0, ind);
    	}
    	lblPlaceAddress.setText(placeAddress);
    	lblOpeningHours.setText(place.getOpeningHours());
    	Image image = new Image(place.getIcCategoryRef());
    	ivPlaceIcon.setImage(image);
    	Image ic = new Image("/logic/view/images/" + IC_SUGG_BTN);
		ImageView icSuggestionBtn = new ImageView(ic);
		btnAddSuggestion.setGraphic(icSuggestionBtn);
		
    }
    
	public Node initializeNode(PlaceBean bean) throws LoadGraphicException {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/logic/view/PlaceSuggestion.fxml"));
			Node node = loader.load();
			SuggestionCardGraphic controller = loader.getController();
			controller.setData(bean);
			return node;
		} catch (IOException e) {
			throw new LoadGraphicException(e.getMessage(), e);
		}
		
	}

	public void loadSuggestionCard(VBox vbSuggestions, PlaceBean place) {
		AnchorPane anchor;
		try {
			anchor = (AnchorPane) initializeNode(place);
			vbSuggestions.getChildren().add(anchor);
		} catch (LoadGraphicException e) {
			AlertGraphic graphic = new AlertGraphic();
			graphic.display(GUIType.PLAN, GUIType.HOME, null, DesktopSessionContext.getInstance().getSession(), WIDGET_ERROR, "Something unexpected occurred loading the suggestion cards.");
		}
	}
    
    

}
