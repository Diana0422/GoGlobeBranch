package logic.model;

import java.util.ArrayList;
import java.util.List;

public class Day {
	
	private int id;
	private Location location;
	private List<Activity> activities;
	private int budget;
	
	public Day() {
		activities = new ArrayList<>();	
	}
	
	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		for (Activity a: activities) {
			this.addToActivities(a);
		}
	}
	
	private void addToActivities(Activity activity) {
		Activity a = new Activity(activity.getTitle(), activity.getTime(), activity.getDescription(), activity.getEstimatedCost());
		this.activities.add(a);
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}
	
	public int calclulateBudget() {
		int costSum = 0;
		
		for (Activity act: this.activities) {
			costSum += act.getEstimatedCost();
		}
		setBudget(costSum);
		return costSum;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
