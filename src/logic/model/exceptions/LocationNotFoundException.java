package logic.model.exceptions;

public class LocationNotFoundException extends Exception {

	private static final long serialVersionUID = 8843468460564887821L;

	public LocationNotFoundException(Throwable throwable, String message) {
		super(message, throwable);
	}
}
