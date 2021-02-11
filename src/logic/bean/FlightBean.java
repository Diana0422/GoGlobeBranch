package logic.bean;

public class FlightBean {

	private String originAirport;
	private String destAirport;
	private String departure;
	private String ret;
	private String carrier;
	private int price;
	
	public String getOriginAirport() {
		return originAirport;
	}
	public void setOriginAirport(String originAirport) {
		this.originAirport = originAirport;
	}
	public String getDestAirport() {
		return destAirport;
	}
	public void setDestAirport(String destAirport) {
		this.destAirport = destAirport;
	}
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
