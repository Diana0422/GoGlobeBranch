package logic.model;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logic.control.FormatManager;
import logic.persistence.dao.TripDao;
import logic.persistence.exceptions.DBConnectionException;
import logic.persistence.exceptions.DatabaseException;

public class Trip {
	
	private String title;
	private String country;
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
	
	public boolean addParticipant(User participant) throws DatabaseException {
		try {
			getParticipants().add(participant);
			participant.recalculateAttitude(this.category1, this.category2);
			return TripDao.getInstance().saveParticipant(participant.getEmail(), this.title);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
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
	
	public static Trip getTrip(String title) throws DatabaseException {
		try {
			return TripDao.getInstance().getTripByTitle(title);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public static List<Trip> getTrips(boolean shared, TripCategory category) throws DatabaseException {
		try {
			if (category == null) {
				if (shared) {
					return TripDao.getInstance().getSharedTrips();
				} else {
					return TripDao.getInstance().getTrips();
				}
			} else {
				return TripDao.getInstance().getTripsForCategory(category);
			}
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	
	public boolean storeTrip() throws DatabaseException {
		try {
			return TripDao.getInstance().saveTrip(this, this.shared);
		} catch (DBConnectionException | SQLException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
