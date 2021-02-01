package logic.persistence.exceptions;

public class DBConnectionException extends Exception {

	private static final long serialVersionUID = 5330738222523300610L;

	public DBConnectionException(String message, Throwable cause) {
		super(" +++ " + message + " +++ ", cause);
	}
	
	public DBConnectionException(String message) {
		super("Original message: "+message);
	}
	
	public DBConnectionException(Throwable cause) {
		super(cause);
	}
}
