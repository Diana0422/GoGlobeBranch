package logic.model;

public class Location {
	
	private String city;
	private String country;
	private String coordinates;
	
	public Location(String city, String country, String coordinates){
        this.city = city;
        this.country = country;
        this.setCoordinates(coordinates);
    }
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
}
