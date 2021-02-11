package logic.model.adapters;

import java.util.Currency;
import java.util.Locale;

import logic.control.FormatManager;
import logic.model.apis.SkyscannerAPI;
import logic.model.exceptions.APIException;
import logic.model.interfaces.CityGeolocation;

public class CityAdapter implements CityGeolocation {
	
	private SkyscannerAPI api;
	
	public CityAdapter(SkyscannerAPI api) {
		this.api = api;
	}

	@Override
	public String getCountryName(String city) throws APIException {
		try {
			return api.getCountry(FormatManager.prepareToURL(city), formatLocale(Locale.getDefault()), formatCountry(Locale.getDefault()), Currency.getInstance(Locale.getDefault()).getCurrencyCode());
		} catch (APIException e) {
			throw new APIException(e.getCause(), "Cannot get country name.");
		}
	}
	
	private String formatLocale(Locale userLocale) {
		return userLocale.getLanguage()+"-"+userLocale.getCountry();
	}
	
	private String formatCountry(Locale userLocale) {
		return userLocale.getCountry();
	}
}
