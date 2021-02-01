package logic.model.exceptions;

public class APIException extends Exception {
	
	private static final long serialVersionUID = -8991511068899697340L;

	public APIException(Throwable throwable, String message) {
		super(message, throwable);
	}

}
