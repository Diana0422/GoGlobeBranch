package logic.view.filterstrategies;

import java.util.List;
import logic.bean.TripBean;
import logic.model.TripCategory;

public class TripFilterManager {
	StrategyContext context;
	
	public TripFilterManager() {
		context = new StrategyContext();
	}
	
	public void setAlphabeticFilter() {
		context.setFilter(new AlphabeticalFilterStrategy());
	}
	
	public void setAdventureFilter() {
		context.setFilter(new CategoryStrategy(TripCategory.ADVENTURE));
	}
	
	public void setCultureFilter() {
		context.setFilter(new CategoryStrategy(TripCategory.CULTURE));
	}
	
	public void setRelaxFilter() {
		context.setFilter(new CategoryStrategy(TripCategory.RELAX));
	}
	
	public void setFunFilter() {
		context.setFilter(new CategoryStrategy(TripCategory.FUN));
	}
	
	public void setPriceFilter() {
		context.setFilter(new PriceFilterStrategy());
		
	}
	
	public List<TripBean> filterTrips(List<TripBean> trips){
		return context.filter(trips);
	}
}