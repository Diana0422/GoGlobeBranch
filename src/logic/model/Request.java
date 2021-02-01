package logic.model;

public class Request {
	private int id;
	private Trip target;
	private Boolean accepted;
	private User sender;
	
	public Boolean getAccepted() {
		return accepted;
	}
	
	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Trip getTarget() {
		return target;
	}

	public void setTarget(Trip target) {
		this.target = target;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}
}
