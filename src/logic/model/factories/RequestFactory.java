package logic.model.factories;

import logic.model.Request;

public class RequestFactory implements ModelFactory {

	private static RequestFactory instance;
	
	private RequestFactory() {}
	
	public static synchronized RequestFactory getInstance() {
		if (instance == null) {
			instance = new RequestFactory();
		}
		return instance;		
	}

	@Override
	public Request createModel() {
		return new Request();
	}
}
