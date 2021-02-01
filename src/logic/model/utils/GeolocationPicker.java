package logic.model.utils;
import logic.model.exceptions.APIException;
import logic.model.exceptions.IPNotFoundException;
import logic.model.exceptions.LocationNotFoundException;
import logic.model.factories.IPFindGeolocationFactory;
import logic.model.factories.WhoIsGeolocationFactory;

public class GeolocationPicker {

	private static GeolocationPicker instance = null;
	
	private GeolocationPicker() {}
	
	public static GeolocationPicker getInstance() {
		if (instance == null) {
			instance = new GeolocationPicker();
		}
		return instance;
	}

	
	public String forwardIPRequestToAPI() throws IPNotFoundException {
		try {
			return IPFindGeolocationFactory.getFactoryInstance().getIPFinderAdapter().getCurrentIP();
		} catch (APIException e) {
			try {
				return WhoIsGeolocationFactory.getFactoryInstance().getIPFinderAdapter().getCurrentIP();
			} catch (APIException e1) {
				throw new IPNotFoundException(e1.getCause(), "Cannot find user current IP.");
			}
		}
	}
	
	public String forwardLocationRequestToAPI(String ip) throws LocationNotFoundException {
		try {
			return IPFindGeolocationFactory.getFactoryInstance().getPositionFinderAdapter().getUserPosition(ip);
		} catch (LocationNotFoundException e) {
			try {
				return WhoIsGeolocationFactory.getFactoryInstance().getPositionFinderAdapter().getUserPosition(ip);
			} catch (LocationNotFoundException e1) {
				throw new LocationNotFoundException(e1.getCause(), "Cannot find user current position.");
			}
		}
	}
}
