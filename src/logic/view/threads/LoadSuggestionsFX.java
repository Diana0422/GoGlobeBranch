package logic.view.threads;

import javafx.application.Platform;

public class LoadSuggestionsFX extends Thread {
	
		private Runnable suggestionLoader;
		
		public LoadSuggestionsFX(Runnable runnable) {
			this.suggestionLoader = runnable;
		}
		
		
		@Override
		public void run() {
			Platform.runLater(suggestionLoader);
		}
		

}
