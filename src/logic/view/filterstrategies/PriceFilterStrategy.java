package logic.view.filterstrategies;

import java.util.List;

import logic.bean.TripBean;

public class PriceFilterStrategy implements TripFilter {

	@Override
	public List<TripBean> filter(List<TripBean> trips) {
		List<TripBean> filteredTrips = trips;
		int selectedIdx;
		int currIdx;
		int minIdx;
		TripBean selected;
		TripBean min;
		TripBean curr;
		TripBean temp;
		if (trips.size() > 1) {
			for (int i = 0;  i < filteredTrips.size()-i; i++) {
				curr = filteredTrips.get(i);
				currIdx = i;
				min = filteredTrips.get(i);
				minIdx = i;
				for (int j = 1; j<filteredTrips.size(); j++) {
					selected = filteredTrips.get(j);
					selectedIdx = j;
					if (compareByPrice(min, selected)) {
						// new minimum
						min = selected;
						minIdx = selectedIdx;
					}
				}
				// swap
				temp = curr;
				filteredTrips.set(currIdx, min);
				filteredTrips.set(minIdx, temp);
			}
		}
		return filteredTrips;
	}

	private boolean compareByPrice(TripBean tripBean1, TripBean tripBean2) {
		return tripBean1.getPrice()>=tripBean2.getPrice();
	}

}
