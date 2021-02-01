package logic.model.factories;

import logic.model.adapters.WhoIsIPFinderAdapter;
import logic.model.adapters.WhoIsPositionFinderAdapter;
import logic.model.interfaces.IPFinder;
import logic.model.interfaces.PositionFinder;

public class WhoIsGeolocationFactory implements GeolocationFactory {

	private static WhoIsGeolocationFactory instance = null;
	
	private WhoIsGeolocationFactory() {/*empty*/}
	
	public static WhoIsGeolocationFactory getFactoryInstance() {
		if (instance == null) {
			instance = new WhoIsGeolocationFactory();
		}
		
		return instance;
	}
	
	@Override
	public IPFinder getIPFinderAdapter() {
		return new WhoIsIPFinderAdapter();
	}

	@Override
	public PositionFinder getPositionFinderAdapter() {
		return new WhoIsPositionFinderAdapter();
	}

}
