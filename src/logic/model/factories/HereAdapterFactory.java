package logic.model.factories;

import logic.model.adapters.HereAPIAdapter;
import logic.model.apis.HereImplementation;

public class HereAdapterFactory {
	
private static HereAdapterFactory instance = null;
	
	private HereAdapterFactory() {
		// empty constructor
	}
	
	public static HereAdapterFactory getInstance() {
		if (instance == null) {
			instance = new HereAdapterFactory();
		}
		
		return instance;
	}
	
	public HereAPIAdapter createHereAdapter() {
		return new HereAPIAdapter(new HereImplementation());
	}
}
