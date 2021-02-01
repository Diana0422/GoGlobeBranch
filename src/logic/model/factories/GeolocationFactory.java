package logic.model.factories;

import logic.model.interfaces.IPFinder;
import logic.model.interfaces.PositionFinder;

public interface GeolocationFactory {

	public IPFinder getIPFinderAdapter();
	public PositionFinder getPositionFinderAdapter();
}
