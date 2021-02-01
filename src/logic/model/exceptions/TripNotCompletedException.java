package logic.model.exceptions;

public class TripNotCompletedException extends Exception {
	
	private static final long serialVersionUID = -3112437220800649283L;

	public TripNotCompletedException(String message) {
		super(message);
	}

}
