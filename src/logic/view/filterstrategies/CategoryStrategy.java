package logic.view.filterstrategies;

import java.util.ArrayList;
import java.util.List;

import logic.bean.TripBean;
import logic.model.TripCategory;

public class CategoryStrategy implements TripFilter{
	
	private String category;
	
	public CategoryStrategy(TripCategory category) {
		this.category = category.name();
	}

	@Override
	public List<TripBean> filter(List<TripBean> trips) {
		List<TripBean> filteredTrips = new ArrayList<>();
		for (TripBean trip : trips) {
			if (trip.getCategory1().equalsIgnoreCase(category) || trip.getCategory2().equalsIgnoreCase(category)) {
				filteredTrips.add(trip);
			}
		}
		return filteredTrips;
	}

}
