package logic.model.exceptions;

public class DateFormatException extends Exception{

	private static final long serialVersionUID = -7649302323009444862L;
	
	public DateFormatException(String format) {
		super("Date should follow the format: " + format);
	}
}
