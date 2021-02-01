package logic.bean;

public class RequestBean {
	private String tripTitle;
	private String senderName;
	private String senderSurname;
	private String receiverName;
	private String receiverSurname;
	private String senderEmail;
	private String receiverEmail;
	private int senderAge;
	
	public String getTripTitle() {
		return tripTitle;
	}
	
	public void setTripTitle(String tripTitle) {
		this.tripTitle = tripTitle;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderSurname() {
		return senderSurname;
	}

	public void setSenderSurname(String senderSurname) {
		this.senderSurname = senderSurname;
	}

	public int getSenderAge() {
		return senderAge;
	}

	public void setSenderAge(int senderAge) {
		this.senderAge = senderAge;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverSurname() {
		return receiverSurname;
	}

	public void setReceiverSurname(String receiverSurname) {
		this.receiverSurname = receiverSurname;
	}
}
