package logic.model.exceptions;

public class IPNotFoundException extends Exception {

	private static final long serialVersionUID = 8837974026016394299L;

	public IPNotFoundException(Throwable throwable, String message) {
		super(message, throwable);
	}
}
