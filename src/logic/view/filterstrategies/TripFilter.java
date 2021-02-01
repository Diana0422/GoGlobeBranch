package logic.view.filterstrategies;

import java.util.List;

import logic.bean.TripBean;

public interface TripFilter {
	
	public List<TripBean> filter(List<TripBean> trips);
}
