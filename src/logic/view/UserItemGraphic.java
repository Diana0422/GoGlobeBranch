package logic.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import logic.bean.TripBean;
import logic.bean.UserBean;
import logic.model.exceptions.LoadGraphicException;

public class UserItemGraphic implements GraphicController{
	
	@FXML
	private Label lblUserName;

	@FXML
	private Label lblAge;
	
	private UserBean user;
	
	private Object prevBundle;

	@FXML
	void displayProfile(MouseEvent event) {
		DesktopSessionContext.getGuiLoader().loadGUIStateful(user, prevBundle, GUIType.PROFILE, GUIType.INFO);
	}

	public void setData(UserBean bean) {
		this.user = bean;
		lblUserName.setText(bean.getName()+" "+bean.getSurname());
		lblAge.setText(Integer.toString(bean.getAge()));
		
	}
	
	public Node initializeNode(UserBean user, TripBean trip) throws LoadGraphicException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/logic/view/UserItem.fxml"));
		AnchorPane anchor;
		try {
			anchor = loader.load();
			UserItemGraphic graphic = loader.getController();
			graphic.initializeData(user, trip);
			return anchor;
		} catch (IOException e) {
			throw new LoadGraphicException(e.getMessage(), e);
		}
	}
	

	@Override
	public void initializeData(Object recBundle, Object forBundle) {
		prevBundle = forBundle;
		setData((UserBean)recBundle);
	}

}
