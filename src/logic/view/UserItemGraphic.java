package logic.view;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.bean.TripBean;
import logic.bean.UserBean;
import logic.model.exceptions.LoadGraphicException;
import logic.util.Session;
import logic.view.utils.GraphicLoader;

public class UserItemGraphic {
	
	@FXML
	private Label lblUserName;

	@FXML
	private Label lblAge;
	
	private UserBean user;
	private TripBean trip;

	private Session session;

	@FXML
	void displayProfile(MouseEvent event) {
		Stage stage = (Stage) lblAge.getScene().getWindow();
		if (session == null) stage.setScene(GraphicLoader.switchView(GUIType.PROFILE, new ProfileGraphic(user, trip)));
		stage.setScene(GraphicLoader.switchView(GUIType.PROFILE, new ProfileGraphic(user, trip), session));
	}

	public void setData(UserBean bean, TripBean trip, Session session) {
		this.user = bean;
		this.trip = trip;
		this.session = session;
		lblUserName.setText(bean.getName()+" "+bean.getSurname());
		lblAge.setText(Integer.toString(bean.getAge()));
		
	}
	
	public Node initializeNode(UserBean user, TripBean trip, Session session) throws LoadGraphicException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/logic/view/UserItem.fxml"));
		AnchorPane anchor;
		try {
			anchor = loader.load();
			UserItemGraphic graphic = loader.getController();
			graphic.setData(user, trip, session);
			return anchor;
		} catch (IOException e) {
			throw new LoadGraphicException(e.getMessage(), e);
		}
	}

}
