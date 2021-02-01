package logic.model.factories;

import logic.model.Trip;

public class TripFactory implements ModelFactory {
	
	private static TripFactory instance;
	
	private TripFactory() {}
	
	public static synchronized TripFactory getInstance() {
		if (instance == null) {
			instance = new TripFactory();
		}
		return instance;		
	}

	@Override
	public Trip createModel() {
		return new Trip();
	}
	
	

}
