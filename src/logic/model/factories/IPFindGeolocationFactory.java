package logic.model.factories;

import logic.model.adapters.IPFindIPFinderAdapter;
import logic.model.adapters.IPFindPositionFinderAdapter;
import logic.model.interfaces.IPFinder;
import logic.model.interfaces.PositionFinder;

public class IPFindGeolocationFactory implements GeolocationFactory {
	
	private static IPFindGeolocationFactory instance = null;
	
	private IPFindGeolocationFactory() {/* empty*/}
	
	public static IPFindGeolocationFactory getFactoryInstance() {
		if (instance == null) {
			instance = new IPFindGeolocationFactory();
		}
		
		return instance;
	}

	@Override
	public IPFinder getIPFinderAdapter() {
		return new IPFindIPFinderAdapter();
	}

	@Override
	public PositionFinder getPositionFinderAdapter() {
		return new IPFindPositionFinderAdapter();
	}

}
