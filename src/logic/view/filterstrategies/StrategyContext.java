package logic.view.filterstrategies;

import java.util.ArrayList;
import java.util.List;

import logic.bean.TripBean;

public class StrategyContext{
	
	TripFilter filter1;
	
	public void setFilter(TripFilter filter) {
		this.filter1 = filter;
	}
	
	
	public List<TripBean> filter(List<TripBean> trips){
		List<TripBean> filteredTrips = new ArrayList<>();
		if (filter1 != null) {
			filteredTrips = filter1.filter(trips);
		}
		return filteredTrips;	
	}
	
}
