package logic.bean;

import java.util.ArrayList;
import java.util.List;

public class DayBean {
	
	private String locationCity;
	private String locationCountry;
	private List<ActivityBean> activities;
	private int budget;
	
	public DayBean() {
		activities = new ArrayList<>();
	}
	
	public void addActivity(ActivityBean newActivityBean) {
		activities.add(newActivityBean);
	}
	
	public List<ActivityBean> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivityBean> activities) {
		this.activities = activities;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getLocationCountry() {
		return locationCountry;
	}

	public void setLocationCountry(String locationCountry) {
		this.locationCountry = locationCountry;
	}

}
