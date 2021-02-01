

package logic.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import logic.model.exceptions.DateFormatException;
import logic.model.exceptions.FormInputException;
import logic.model.exceptions.TripNotCompletedException;

public class PlanTripBean {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy"; 
	private TripBean tripBean;
	private String errorMsg;
	private String location;	
	private int planningDay = 0;

	public PlanTripBean(){
		//Bean classes are supposed to have empty constructors
	}   
	
	//Validates all the fields in the share trip view
	public void validateSharingPref() throws FormInputException {
		if (tripBean.getDescription() == null || tripBean.getDescription().equals("")) {
			throw new FormInputException("Error on trip description.");
		}
		
		if (tripBean.getMinAge() == null || tripBean.getMinAge().equals("")) {
			throw new FormInputException("Error on minimum age.");
		}
		
		if (tripBean.getMaxAge() == null || tripBean.getMaxAge().equals("")) {
			throw new FormInputException("Error on maximum age.");			
		}
		
		if (tripBean.getMaxParticipants() == null || tripBean.getMaxParticipants().equals("")) {
			throw new FormInputException("Insert maximum participants.");
		}
		
		try {
			int min = Integer.parseInt(tripBean.getMinAge());
			int max = Integer.parseInt(tripBean.getMaxAge());
			if (min > max) {
				throw new FormInputException("Age input not valid.");
			}
		}catch (NumberFormatException e){
			throw new FormInputException("Age input not valid");

		}
			
		String logStr = "SHARE FORM INFO: "+"trip description: " + tripBean.getDescription()+" min age: " + tripBean.getMinAge()+" max age: " + tripBean.getMaxAge();
		Logger.getGlobal().info(logStr);
		
	}
		
	//Validates location string in the Plan trip view
	public boolean validateLocation() throws FormInputException{
		if ((this.location == null || this.location.equals(""))) {
			throw new FormInputException("Insert a valid location");
		}		
		return true;
	}

	//Validates the whole trip 
	//TODO - mettere in tripBean
	public void validateTrip() throws TripNotCompletedException{	
		tripBean.validateTrip();
	}
	
	//Validate all the inputs in the form
	public boolean validateForm()throws FormInputException{
		
		Boolean res = validateData(tripBean.getTitle(), tripBean.getDepartureDate(), tripBean.getReturnDate(), tripBean.getCategory1(), tripBean.getCategory2());
		if (Boolean.TRUE.equals(res)){
			try {
				validateDates(tripBean.getDepartureDate(), tripBean.getReturnDate());
			}catch(DateFormatException e) {
				throw new FormInputException(e.getMessage());
			}
			
			return validateCategories(tripBean.getCategory1(), tripBean.getCategory2());
		}else{
			return false;
		}		
	}
	
	//Check if the dates are set correctly
	private boolean validateDates(String departureDate, String returnDate) throws FormInputException, DateFormatException {
			
		try {
			Date depDate = new SimpleDateFormat(DATE_FORMAT).parse(departureDate);
			Date retDate = new SimpleDateFormat(DATE_FORMAT).parse(returnDate);
			Date currentDate = new Date();			
			if (depDate.before(currentDate)) {
				this.setErrorMsg("Departure date must be later than current date");
				return false;
			}
			
			if (retDate.before(depDate)) {
				throw new FormInputException("Return date must be later than departure date");
			}			
		} catch (ParseException e) {			
			throw new DateFormatException(DATE_FORMAT);
		}			
		return true;
	}
	
	//Check if categories are not equal
	private boolean validateCategories(String category1, String category2) throws FormInputException{
			
		if (category1.equals(category2)) {
			throw new FormInputException("Categories can not be of the same type.");	
		}		
		return true;
	}
	
	//Check if all the inputs have been inserted 
	private Boolean validateData(String tripName, String departureDate, String returnDate, String category1, String category2) throws FormInputException {
		
		if (tripName == null || tripName.equals("")) {
			throw new FormInputException("Insert trip name");
		}
		
		if (departureDate == null || departureDate.equals("")) {
			throw new FormInputException("Insert Departure date");
		}
		if (returnDate == null || returnDate.equals("")) {
			throw new FormInputException("Insert return date");
		}
		
		try {
			validateDateString(departureDate);
			validateDateString(returnDate);
			
		} catch (DateFormatException e) {
			throw new FormInputException(e.getMessage());
		} 
		if (category1.equals("NONE") || category2.equals("NONE")) {
			throw new FormInputException("Both categories must be selected");
		}
		return true;
	}
	
	//Check if dates match the right format
	private boolean validateDateString(String date) throws DateFormatException {
		
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setLenient(false);
		try {	
			format.parse(date);
			
		}catch(ParseException e){
			throw new DateFormatException(DATE_FORMAT);
		}
		
		return true;
	}
	
	public void saveLocation() {
		String logStr = "SAVING LOCATION: " + this.location;
		Logger.getGlobal().info(logStr);
		this.tripBean.getDays().get(planningDay).setLocationCity(location);
	}
	
	//Checks if the day has a location 
	public boolean checkDay() {
		String locationCheck = tripBean.getDays().get(planningDay).getLocationCity();
		String logStr = "LOCATION CHECK: " + locationCheck;
		Logger.getGlobal().info(logStr);
		return (locationCheck == null || locationCheck.equals(""));
	}	
	
	//Gets the location for the current planning day
	public String getDayLocation() {
		return this.tripBean.getDays().get(planningDay).getLocationCity();
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}	
	
	public int getPlanningDay() {
		return planningDay;
	}

	public void setPlanningDay(int planningDay) {
		this.planningDay = planningDay;
	}


	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public TripBean getTripBean() {
		return tripBean;
	}

	public void setTripBean(TripBean tripBean) {
		this.tripBean = tripBean;
	}	
}
