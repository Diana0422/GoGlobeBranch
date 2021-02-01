package logic.model.exceptions;

public class SerializationException extends Exception {

	private static final long serialVersionUID = -2584928887249725654L;
	
	public SerializationException(Throwable throwable, String message) {
		super(message, throwable);
	}
	
}
