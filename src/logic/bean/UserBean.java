package logic.bean;

import java.util.List;

import logic.model.UserStats;
import logic.model.interfaces.Observer;
import logic.model.interfaces.Subject;
import logic.view.control.ProfileGraphic;

public class UserBean implements Observer {
	private String email;
	private String name;
	private String surname;
	private int age;
	private int points;
	private String bio;
	private UserStatsBean statsBean;
	private List<ReviewBean> reviews;
	private ProfileGraphic graphic;
	
	public UserBean() {
		//empty constructor
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public List<ReviewBean> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewBean> reviews) {
		this.reviews = reviews;
	}

	@Override
	public void updateValue(Subject s) {
		getStatsBean().setTravRating(((UserStats) s).getTravelerRating());
		getStatsBean().setOrgRating(((UserStats) s).getOrganizerRating());
		if (getGraphic()!= null) {
			getGraphic().displayOrganizerRating(getStatsBean().getOrgRating());
			getGraphic().displayTravelerRating(getStatsBean().getTravRating());
		}
	}

	public ProfileGraphic getGraphic() {
		return graphic;
	}

	public void setGraphic(ProfileGraphic graphic) {
		this.graphic = graphic;
	}

	public UserStatsBean getStatsBean() {
		return statsBean;
	}

	public void setStatsBean(UserStatsBean statsBean) {
		this.statsBean = statsBean;
	}

}
