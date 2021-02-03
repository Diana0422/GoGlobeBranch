package logic.view;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.bean.LoginBean;
import logic.control.LoginController;
import logic.persistence.exceptions.DatabaseException;
import logic.util.Cookie;
import logic.util.Session;
import logic.view.utils.GraphicLoader;

public class LoginGraphic {
	
	@FXML
	private TextField tfEmail;
	
	@FXML
	private PasswordField pfPassword;

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;
    
    @FXML
    private Label lblError;
    
    private String email;

    @FXML
    public void forwardHome(ActionEvent event) {
    	LoginBean loginBean = new LoginBean();
    	loginBean.setUsername(tfEmail.getText());
    	loginBean.setPassword(pfPassword.getText());
    	this.email = loginBean.getUsername();
    	String logStr = "LOGIN TRIAL:"+"\n"+"USERNAME: " + tfEmail.getText()+"\n"+"PASSWORD: " + pfPassword.getText();
    	Logger.getGlobal().info(logStr);
    	if (loginBean.validate()) {
    		try {
    			LoginController controller = new LoginController();
				if ((loginBean = controller.login(loginBean.getUsername(), loginBean.getPassword())) != null) {
					Stage stage = (Stage) lblError.getScene().getWindow();
					stage.setScene(GraphicLoader.switchView(GUIType.HOME, new HomeGraphic(), Cookie.getInstance().getSession(loginBean.getUsername())));
				} else {
					lblError.setText("This user isn't registered.");
					lblError.setVisible(true);
				}
			} catch (DatabaseException e) {
				AlertGraphic alert = new AlertGraphic();
				alert.display(e.getMessage(), e.getCause().toString());
			}
    	}else {
			lblError.setText("Some fields are still empty.");
			lblError.setVisible(true);
    		logStr = "INVALID LOGIN.";
    		Logger.getGlobal().info(logStr);
    	}
    }

    @FXML
    public void forwardRegistration(ActionEvent event){
    	Stage stage = (Stage) lblError.getScene().getWindow();
    	stage.setScene(GraphicLoader.switchView(GUIType.REGISTER, null, new Session()));
    }

	public String getEmail() {
		return email;
	}
}
