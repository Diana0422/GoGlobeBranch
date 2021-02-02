package logic.model.interfaces;

import logic.model.exceptions.APIException;

public interface CityGeolocation {

	public String getCountryName(String city) throws APIException;
}
