package logic.model.exceptions;

public class UnloggedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UnloggedException() {
		super("You are not logged.");
	}

}
