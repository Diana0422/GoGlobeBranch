package logic.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.view.utils.GraphicLoader;

public class MainGraphic {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;
    
    @FXML
    private TextField tfDestination;
    

    @FXML
    void onGetStarted(MouseEvent event) {
    	
    }

    @FXML
    void onLogin(MouseEvent event) {
    	Stage stage = (Stage) btnLogin.getScene().getWindow();
    	stage.setScene(GraphicLoader.switchView(GUIType.LOGIN, null));
    }

    @FXML
    void onRegister(MouseEvent event) {
    	Stage stage = (Stage) btnRegister.getScene().getWindow();
    	stage.setScene(GraphicLoader.switchView(GUIType.REGISTER, null));
    }

}