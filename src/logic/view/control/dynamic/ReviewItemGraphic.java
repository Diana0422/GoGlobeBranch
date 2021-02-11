package logic.view.control.dynamic;

import java.io.IOException;

import org.controlsfx.control.Rating;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import logic.bean.ReviewBean;
import logic.model.exceptions.LoadGraphicException;
import logic.view.control.GraphicController;

public class ReviewItemGraphic implements GraphicController {
	
    @FXML
    private Label lblUser;

    @FXML
    private Rating lblRating;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblComment;
    
    public Node initializeNode(ReviewBean bean) throws LoadGraphicException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/logic/view/res/fxml/dynamic/ReviewItem.fxml"));
		AnchorPane anchor;
		try {
			anchor = loader.load();
			ReviewItemGraphic graphic = loader.getController();
			graphic.initializeData(bean, null);
			return anchor;
		} catch (IOException e) {
			throw new LoadGraphicException(e.getMessage(), e);
		}
	}

	@Override
	public void initializeData(Object recBundle, Object forBundle) {
		ReviewBean bean = (ReviewBean) recBundle;
		lblUser.setText(bean.getReviewerName()+" "+bean.getReviewerSurname());
		lblRating.setRating(bean.getVote());
		lblDate.setText(bean.getDate());
		lblTitle.setText(bean.getTitle());
		lblComment.setText(bean.getComment());
		
	}

}
