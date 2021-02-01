package logic.model;

public class PlaceBean {
	
	private String name;
	private String address;
	private String openingHours;
	private String icCategoryRef;
	
	public PlaceBean(String name, String address, String openingHours, String icCategoryRef) {
		this.name = name;
		this.address = address;
		this.openingHours = openingHours;
		this.icCategoryRef = icCategoryRef;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOpeningHours() {
		return openingHours;
	}
	public void setOpeningHours(String openingHours) {
		this.openingHours = openingHours;
	}
	public String getIcCategoryRef() {
		return icCategoryRef;
	}
	public void setIcCategoryRef(String icCategoryRef) {
		this.icCategoryRef = icCategoryRef;
	}
	
	
}

