package logic.bean;

import java.util.ArrayList;
import java.util.List;

import logic.model.exceptions.TripNotCompletedException;

public class TripBean {
	
	private int id;
	private boolean shared;
	private String title;
	private int price;
	private int ticketPrice;
	private String category1;
	private String category2;
	private String departureDate;
	private String returnDate;	
	private List<DayBean> days;
	private long tripLength;
	private String description;
	private String minAge;
	private String maxAge;
	private String maxParticipants;
	private UserBean organizer;
	private List<UserBean> participants;
	private int availability;
	
	
	public void addActivity(int day, ActivityBean activity) {
		this.getDays().get(day).getActivities().add(activity);
	}
	
	//Creates dayBean instances
	public void createDays(){
		days = new ArrayList<>();
		for (int i = 0; i < this.tripLength; i++){
			DayBean day = new DayBean();
			this.days.add(day);
		}
	}
	
	public void validateTrip() throws TripNotCompletedException{
		for (int i = 0; i < days.size(); i++) {
			if (days.get(i).getActivities().isEmpty())
				throw new TripNotCompletedException("Plan all days before going to the next step.");
		}	
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCategory1() {
		return category1;
	}

	public void setCategory1(String category1) {
		this.category1 = category1;
	}

	public String getCategory2() {
		return category2;
	}

	public void setCategory2(String category2) {
		this.category2 = category2;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public long getTripLength() {
		return tripLength;
	}

	public void setTripLength(long tripLength) {
		this.tripLength = tripLength;
	}

	public List<DayBean> getDays() {
		return days;
	}

	public void setDays(List<DayBean> days) {
		this.days = days;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
	public String getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(String maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserBean getOrganizer() {
		return organizer;
	}

	public void setOrganizer(UserBean organizer) {
		this.organizer = organizer;
	}

	public List<UserBean> getParticipants() {
		return participants;
	}

	public void setParticipants(List<UserBean> participants) {
		this.participants = participants;
	}

	public int getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}
	
	
}