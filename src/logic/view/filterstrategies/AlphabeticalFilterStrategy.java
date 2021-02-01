package logic.view.filterstrategies;

import java.util.List;

import logic.bean.TripBean;

public class AlphabeticalFilterStrategy implements TripFilter{

	@Override
	public List<TripBean> filter(List<TripBean> trips){
		List<TripBean> filteredTrips = trips;
		if (trips.size() > 1) {
			for (int i = 0;  i < filteredTrips.size()-i; i++) {
				if (compareByName(filteredTrips.get(i), filteredTrips.get(i+1)) > 0) {
					TripBean temp = filteredTrips.get(i);
					filteredTrips.set(i, filteredTrips.get(i+1));
					filteredTrips.set(i+1, temp);
				}
			}
		}
		return filteredTrips;
	}
	
	public int compareByName(TripBean trip1, TripBean trip2) {
		int ret = 0;
		if (trip1.getTitle().compareTo(trip2.getTitle()) > 0) {
			ret = 1;
		}
		if (trip1.getTitle().compareTo(trip2.getTitle()) < 0) {
			ret = -1;
		}
		
		return ret;
	}
	
	

}
