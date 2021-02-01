package logic.view.filterstrategies;

import java.util.List;
import logic.bean.TripBean;

public class TripFilterManager {
	StrategyContext context;
	
	public TripFilterManager() {
		context = new StrategyContext();
	}
	
	public void setAlphabeticFilter() {
		context.setFilter(new AlphabeticalFilterStrategy());
	}
	
	public void setAdventureFilter() {
		context.setFilter(new AdventureCategoryStrategy());
	}
	
	public void setCultureFilter() {
		context.setFilter(new CultureCategoryStrategy());
	}
	
	public void setRelaxFilter() {
		context.setFilter(new RelaxCategoryStrategy());
	}
	
	public void setFunFilter() {
		context.setFilter(new FunCategoryStrategy());
	}
	
	public List<TripBean> filterTrips(List<TripBean> trips){
		return context.filter(trips);
	}
}