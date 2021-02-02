package logic.view;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.view.utils.GraphicLoader;

public class MainGraphic implements Initializable {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;
    
    @FXML
    private ComboBox<String> cbDestination;
    

    @FXML
    void onGetStarted(MouseEvent event) {
    	Stage stage = (Stage) cbDestination.getScene().getWindow();
    	stage.setScene(GraphicLoader.switchView(GUIType.JOIN, new JoinTripGraphic(cbDestination.getValue())));
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<String> cities = FXCollections.observableArrayList();

        String[] locales1 = Locale.getISOCountries();
        for (String countrylist : locales1) {
            Locale obj = new Locale("", countrylist);
            String[] city = { obj.getDisplayCountry() };
            for (int x = 0; x < city.length; x++) {
                cities.add(obj.getDisplayCountry());
            }
        }
        cbDestination.setItems(cities);
		
	}

}