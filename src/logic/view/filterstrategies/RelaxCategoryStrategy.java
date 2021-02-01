package logic.view.filterstrategies;

import java.util.ArrayList;
import java.util.List;

import logic.bean.TripBean;

public class RelaxCategoryStrategy implements TripFilter {

	@Override
	public List<TripBean> filter(List<TripBean> trips) {
		List<TripBean> filteredTrips = new ArrayList<>();
		for (TripBean trip : trips) {
			if (trip.getCategory1().equalsIgnoreCase("RELAX")|| trip.getCategory2().equalsIgnoreCase("RELAX")) {
				filteredTrips.add(trip);
			}
		}
		return filteredTrips;
	}

}
