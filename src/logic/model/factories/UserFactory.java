package logic.model.factories;

import logic.model.User;

public class UserFactory implements ModelFactory {
	
	private static UserFactory instance;
	
	private UserFactory() {}
	
	public static synchronized UserFactory getInstance() {
		if (instance == null) {
			instance = new UserFactory();
		}
		return instance;		
	}

	@Override
	public User createModel() {
		return new User();
	}
	
}
