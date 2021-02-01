package logic.bean;

public class UserStatsBean {
	private double orgRating;
	private double travRating;
	
	public UserStatsBean() {/*empty for bean*/}
	
	public double getOrgRating() {
		return orgRating;
	}
	
	public void setOrgRating(double orgRating) {
		this.orgRating = orgRating;
	}
	
	public double getTravRating() {
		return travRating;
	}
	
	public void setTravRating(double travRating) {
		this.travRating = travRating;
	}
}
