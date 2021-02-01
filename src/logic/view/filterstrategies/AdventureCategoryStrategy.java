package logic.view.filterstrategies;

import java.util.ArrayList;
import java.util.List;

import logic.bean.TripBean;

public class AdventureCategoryStrategy implements TripFilter{

	@Override
	public List<TripBean> filter(List<TripBean> trips) {
		List<TripBean> filteredTrips = new ArrayList<>();
		for (TripBean trip : trips) {
			if (trip.getCategory1().equalsIgnoreCase("ADVENTURE") || trip.getCategory2().equalsIgnoreCase("ADVENTURE")) {
				filteredTrips.add(trip);
			}
		}
		return filteredTrips;
	}

}
