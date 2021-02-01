package logic.bean;

import java.util.logging.Logger;

import logic.model.exceptions.FormInputException;

public class ActivityBean {
	
	private String time;
	private String title;
	private String description;
	private String estimatedCost;
	
	
	public ActivityBean() {
		//Constructor of a bean must be empty 
	}
	
	public boolean validateActivity() throws FormInputException {
		String infoStr = "TITLE CHECK:" + validateTitle(this.title);
		Logger.getGlobal().info(infoStr);
		infoStr = "TIME CHECK:" + validateTime(this.time);
		Logger.getGlobal().info(infoStr);
		infoStr = "DESCRIPTION CHECK:" + validateDescription(this.description);
		Logger.getGlobal().info(infoStr);
		validateTitle(this.title);
		validateTime(this.time);
		validateDescription(this.description);
		validateCost(this.estimatedCost);
		return true;
	}
	
	private boolean validateTitle(String title) throws FormInputException {
		if ((title == null || title.equals(""))){
			throw new FormInputException("Insert an activity title.");
		}else {
			return true;
		}
		
	}
	
	//Check if description is not null
	private boolean validateDescription(String description) throws FormInputException {
		
		if ((description == null || description.equals(""))){
			throw new FormInputException("Insert a description for the activity.");	
		}
		
		return true;
	}
	
	//Check if the activity time is inserted according to hh:mm format
	private boolean validateTime (String time) throws FormInputException {
		if (time == null || time.equals("") || time.length() != 5) 
			throw new FormInputException("Insert a valid time (hh:mm) for the activity.");	
		try {
			int hour = Integer.parseInt(time.substring(0, 2));
			int minute = Integer.parseInt(time.substring(3));
				validateHour(hour);
				validateMinute(minute);			
		}catch(NumberFormatException e) {
			throw new FormInputException("Time must follow the format: hh:mm");
		}
		
		return true;
	}
		
	private boolean validateCost(String strCost) throws FormInputException {
		if (strCost == null || strCost.equals("")) {
			throw new FormInputException("Insert a cost for the activity.");
		}
		try {
			int cost = Integer.parseInt(strCost);
			if (cost < 0) 
				throw new FormInputException("Insert a positive number as the activity cost.");

		}catch(NumberFormatException e) {
			throw new FormInputException("Insert a valid number as the activity cost.");
		}
		return true;
		
	}
	
	private boolean validateHour(int hour) throws FormInputException {
		
		if (!((hour >= 0) && (hour < 24))) {
			throw new FormInputException("Insert a valid number as the activity cost.");
		}		
		return true;
	}
	
	private boolean validateMinute(int minute) {
		return ((minute >= 0) && (minute < 60));
	}



	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	public String getEstimatedCost() {
		return estimatedCost;
	}


	public void setEstimatedCost(String estimatedCost) {
		this.estimatedCost = estimatedCost;
	}



}
