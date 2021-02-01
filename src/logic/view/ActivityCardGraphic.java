package logic.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.bean.ActivityBean;
import logic.model.exceptions.LoadGraphicException;

public class ActivityCardGraphic {

    @FXML
    private Text txtDescription;

    @FXML
    private Text txtTitle;

    @FXML
    private Text txtTime;
    
    @FXML
    private Text txtActivityCost;
    
    private static final String WIDGET_ERROR = "Widget loading error.";
    
    
    public void setData(ActivityBean activity) {
    	txtTitle.setText(activity.getTitle());
    	txtTime.setText(activity.getTime());
    	txtActivityCost.setText(activity.getEstimatedCost());
    	txtDescription.setText(activity.getDescription());
    	
    }
    
    public Node initializeNode(ActivityBean bean) throws LoadGraphicException {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/logic/view/ActivityCard.fxml"));
		try {
			Node node = loader.load();
			ActivityCardGraphic graphic = loader.getController();
			graphic.setData(bean);
			return node;
		} catch (IOException e) {
			throw new LoadGraphicException(e.getMessage(),e);
		}
    }

	public void loadActivityCard(VBox vbActivities, ActivityBean activityBean) {
		AnchorPane node;
		try {
			node = (AnchorPane) initializeNode(activityBean);
			vbActivities.getChildren().add(node);
		} catch (LoadGraphicException e) {
			AlertGraphic graphic = new AlertGraphic();
			graphic.display(GUIType.PLAN, GUIType.HOME, null, DesktopSessionContext.getInstance().getSession(), WIDGET_ERROR, "Something unexpected occurred loading the trip cards.");
		}
	}

}
