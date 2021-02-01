package logic.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logic.control.FormatManager;

public class Trip {
	
	private String title;
	private int price;
	private int ticketPrice;
	private TripCategory category1;
	private TripCategory category2;
	private Date departureDate;
	private Date returnDate;	
	private List<Day> days;
	private User organizer;
	
	/* SHARED TRIP ELEMENTS */
	private boolean shared;
	private String description;
	private int minAge;
	private int maxAge;
	private int maxParticipants;
	private List<User> participants;
	
	
	public Trip() {
		this.days = new ArrayList<>();
		this.participants = new ArrayList<>();
	}
	
	public Trip(String title, int price, String cat1, String cat2, String departureDate, String returnDate) {
		this.title = title;
		this.price = price;
		this.category1 = TripCategory.valueOf(cat1);
		this.category2 = TripCategory.valueOf(cat2);
		this.departureDate = FormatManager.parseDate(departureDate);
		this.returnDate = FormatManager.parseDate(returnDate);
	}
	
	public void addParticipant(User participant) {
		getParticipants().add(participant);
		participant.recalculateAttitude(this.category1, this.category2);
	}
	
	public void setDays(List<Day> days) {
		for (Day d: days) {
			this.addToDays(d);
		}
	}

	
	private void addToDays(Day day) {
		Day d = new Day();
		d.setId(day.getId());
		d.setBudget(day.getBudget());
		d.setLocation(day.getLocation());
		this.days.add(d);
	}
	
	public int getAvailableSpots() {
		return this.maxParticipants-this.participants.size();
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public TripCategory getCategory1() {
		return category1;
	}
	
	public void setCategory1(TripCategory category1) {
		this.category1 = category1;
	}
	
	public TripCategory getCategory2() {
		return category2;
	}
	
	public void setCategory2(TripCategory category2) {
		this.category2 = category2;
	}
	
	public List<Day> getDays() {
		return days;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public List<User> getParticipants() {
		return participants;
	}

	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public int getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
}
