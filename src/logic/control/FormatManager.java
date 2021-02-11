package logic.control;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.model.TripCategory;

public class FormatManager {
	
	private FormatManager() {/* private default */}
	
	/* Controller METHODS */
	
	public static TripCategory parseTripCategory(String category) {
		if (category.equalsIgnoreCase("Fun")) return TripCategory.FUN;	
		if (category.equalsIgnoreCase("Culture")) return TripCategory.CULTURE;	
		if (category.equalsIgnoreCase("Relax")) return TripCategory.RELAX;
		if (category.equalsIgnoreCase("Adventure")) return TripCategory.ADVENTURE;
			
		return TripCategory.NONE;
	}
	
	public static Date parseDate(String string) {
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			return formatter.parse(string);
		} catch (ParseException e) {
			String strLog = "Cannot parse date:"+string;
			Logger.getGlobal().log(Level.WARNING, strLog);
			return new Date();
		}
	}
	
	public static String formatDate(Date date) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}
	
	public static String formatDateSQL(Date date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	
	public static String prepareToURL(String s) {
		return s.replace(" ", "+");
	}
	
	public static String formatLocale() {
		Locale userLocale = Locale.getDefault();
		return userLocale.getLanguage()+"-"+userLocale.getCountry();
	}
}
