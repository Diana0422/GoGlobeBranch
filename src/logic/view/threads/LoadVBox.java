package logic.view.threads;

import java.util.List;
import javafx.scene.layout.VBox;
import logic.model.PlaceBean;
import logic.view.SuggestionCardGraphic;

public class LoadVBox implements Runnable{
	
	private VBox vbox;
	List<PlaceBean> places;
	
	public LoadVBox(VBox vbox, List<PlaceBean> places) {
		this.vbox = vbox;
		this.places = places;
	}

	@Override
	public void run() {
		for (int i = 0; i < places.size(); i++) {
			loadSuggestion(places.get(i));
		}
	}
	
	
	//load suggestion in the GUI
	private void loadSuggestion(PlaceBean place) {
		SuggestionCardGraphic graphic = new SuggestionCardGraphic();
		graphic.loadSuggestionCard(vbox, place);
	}

}
