package logic.persistence.exceptions;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = -7754927762120586556L;

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
