package logic.model.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import logic.model.interfaces.Observer;
import logic.model.interfaces.Subject;

public class ReviewChangeManager {

	private static ReviewChangeManager instance = null;
	
	private static HashMap<Subject, Observer> map;
	
	private ReviewChangeManager() {}
	
	public static ReviewChangeManager getInstance() {
		if (instance == null) {
			instance = new ReviewChangeManager();
			map = new HashMap<>();
		}
		return instance;
		
	}
	
	
	public void register(Subject s, Observer o) {
		map.put(s, o);
	}
	
	public void unregister(Subject s, Observer o) {
		map.remove(s, o);
	}
	
	public void notifySubject(Subject s) {
		List<Subject> subjectList = new ArrayList<>(map.keySet());
		for (Subject tmp: subjectList) {
			if (tmp.equals(s)) {
				Observer o = map.get(tmp);
				o.updateValue(s);
			}
		}
	}
	
	
}
