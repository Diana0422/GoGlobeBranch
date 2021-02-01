package logic.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import logic.bean.ActivityBean;
import logic.model.exceptions.LoadGraphicException;

public class ActivityItemGraphic implements Initializable {
	
    @FXML
    private Label lblActivityName;

    @FXML
    private Label lblActivityTime;

    @FXML
    private Label lblDescription;

    
    public void setData(ActivityBean bean) {
    	lblActivityName.setText(bean.getTitle());
    	lblActivityTime.setText(bean.getTime());
    	lblDescription.setText(bean.getDescription());
    }
    
    public Node initializeNode(ActivityBean bean) throws LoadGraphicException {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/logic/view/ActivityItem.fxml"));
		try {
			Node node = loader.load();
			ActivityItemGraphic graphic = loader.getController();
			graphic.setData(bean);
			return node;
		} catch (IOException e) {
			throw new LoadGraphicException(e.getMessage(),e);
		}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// empty method: nothing to do when initialized
		
	}
}
