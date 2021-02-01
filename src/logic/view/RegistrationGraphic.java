package logic.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.bean.SessionBean;
import logic.control.RegistrationController;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Session;
import logic.view.utils.GraphicLoader;

public class RegistrationGraphic {
	@FXML
	private TextField txtEmail;
	
	@FXML
	private PasswordField txtPassword;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtSurname;
	
	@FXML
	private TextField txtBirthday;
	
	@FXML
	private Label lblMessage;
	
	/* Beans */
	
	private SessionBean sessionBean;
	
	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public SessionBean setSessionBean(SessionBean session) {
		this.sessionBean = session;
		return sessionBean;
	}
	
	/* Action methods */
	
    @FXML
    void validate(MouseEvent event) {
		String email = txtEmail.getText();
		String password = txtPassword.getText();
		String name = txtName.getText();
		String surname = txtSurname.getText();
		String birthday = txtBirthday.getText();
		
		if (email.equals("") || password.equals("") || name.equals("") || surname.equals("") || birthday.equals("")) {
			lblMessage.setText("Input values not valid.");
		} else {
			/* Call the controller to register the user */
			try {
				if ((setSessionBean(RegistrationController.getInstance().register(email, password, name, surname, birthday)))!= null) {
					Stage stage = (Stage) lblMessage.getScene().getWindow();
					stage.setScene(GraphicLoader.switchView(GUIType.HOME, null, new Session()));
				} else {
					lblMessage.setText("User already registered with this email.");
				}
			} catch (DatabaseException e) {
				AlertGraphic alert = new AlertGraphic();
				alert.display(GUIType.REGISTER, GUIType.HOME, null, DesktopSessionContext.getInstance().getSession(), e.getMessage(), e.getCause().toString());
			}
		}
    }
}