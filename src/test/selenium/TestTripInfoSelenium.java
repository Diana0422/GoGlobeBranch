package test.selenium;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import logic.control.FormatManager;
import logic.model.Trip;
import logic.persistence.dao.TripDao;
import logic.persistence.exceptions.DBConnectionException;

/**
 * @author diana pasquali
 */

public class TestTripInfoSelenium {

	private static final String TRIP_TITLE = "TestViaggio";
	private static final String DRIVER_PROPERTY = "webdriver.chrome.driver";
	private static final String DRIVER_PATH = "Drivers/chromedriver.exe";
	private static final String PAGE_URL = "http://localhost:8080/ISPWProject20-21/view/joinTrip.jsp";
	private static final String SEARCHBAR_XPATH = "//*[@id=\"bar\"]";
	private static final String SEARCHBTN_XPATH = "//*[@id=\"search-btn\"]";
	private static final String INFOBTN_XPATH = "//*[@id=\"bootstrap-override\"]/div[2]/form/div/div/div/div/div/button";
	private static final String ERROR_MSG = "EXCEPTION IN TEST SEARCH TRIP PRICE: "+TRIP_TITLE+" NOT FOUND IN DATABASE.";
	
	@Test
	void testTripInfoTitleSelenium() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Click button to forward to trip info
		driver.findElement(By.xpath(INFOBTN_XPATH)).click();
		
		// Read trip info
		WebElement titleResult = driver.findElement(By.xpath("//*[@id=\"general\"]/div/div[1]/h1"));
		String resTitle = titleResult.getText();
	
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(TRIP_TITLE);
			assertEquals(trip.getTitle(), resTitle);
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		} finally {
			driver.close();
		}
	}
	
	@Test
	void testTripInfoOrganizerNameSelenium() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Click button to forward to trip info
		driver.findElement(By.xpath(INFOBTN_XPATH)).click();
		
		// Read trip organizer name
		WebElement orgNameResult = driver.findElement(By.xpath("//*[@id=\"general\"]/div/div[2]/div/form/div/h4[1]"));
		String resOrganizerName = orgNameResult.getText();
		
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(TRIP_TITLE);
			assertEquals(trip.getOrganizer().getName(), resOrganizerName);
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		} finally {
			driver.close();
		}
	}
	
	@Test
	void testTripInfoDescriptionSelenium() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Click button to forward to trip info
		driver.findElement(By.xpath(INFOBTN_XPATH)).click();
		
		// Read trip description
		WebElement descResult = driver.findElement(By.xpath("//*[@id=\"general\"]/div/div[2]/div/div[1]/p"));
		String resDescription = descResult.getText();
		
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(TRIP_TITLE);
			assertEquals(trip.getDescription(), resDescription);
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		} finally {
			driver.close();
		}
	}
	
	@Test
	void testTripInfoOrganizerSurnameSelenium() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Click button to forward to trip info
		driver.findElement(By.xpath(INFOBTN_XPATH)).click();
		
		// Read trip organizer name
		WebElement orgSurnameResult = driver.findElement(By.xpath("//*[@id=\"general\"]/div/div[2]/div/form/div/h4[2]"));
		String resOrganizerSurname = orgSurnameResult.getText();
		
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(TRIP_TITLE);
			assertEquals(trip.getOrganizer().getSurname(), resOrganizerSurname);
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		} finally {
			driver.close();
		}
	}
	
	
	@Test
	void testTripInfoCategory1Selenium() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Click button to forward to trip info
		driver.findElement(By.xpath(INFOBTN_XPATH)).click();
		
		// Read trip category 1
		WebElement cat1Result = driver.findElement(By.xpath("//*[@id=\"general\"]/div/div[2]/div/ul/li[1]/h4"));
		String resCat1 = cat1Result.getText();
		
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(TRIP_TITLE);
			assertEquals(trip.getCategory1().toString(), resCat1);
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		} finally {
			driver.close();
		}
	}
	
	
	@Test
	void testTripInfoCategory2Selenium() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Click button to forward to trip info
		driver.findElement(By.xpath(INFOBTN_XPATH)).click();
		
		// Read trip category2
		WebElement cat2Result = driver.findElement(By.xpath("//*[@id=\"general\"]/div/div[2]/div/ul/li[2]/h4"));
		String resCat2 = cat2Result.getText();
		
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(TRIP_TITLE);
			assertEquals(trip.getCategory2().toString(), resCat2);
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		} finally {
			driver.close();
		}
	}
	
	@Test
	void testTripInfoPriceSelenium() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Click button to forward to trip info
		driver.findElement(By.xpath(INFOBTN_XPATH)).click();
		
		// Read trip category2
		WebElement priceResult = driver.findElement(By.xpath("//*[@id=\"general\"]/div/div[2]/div/div[2]/h4"));
		String resPrice = priceResult.getText();
		
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(TRIP_TITLE);
			int i = Integer.parseInt(resPrice);
			assertEquals(trip.getPrice(), i);
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		} finally {
			driver.close();
		}
	}
	
	@Test
	void testTripInfoDepartureSelenium() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Click button to forward to trip info
		driver.findElement(By.xpath(INFOBTN_XPATH)).click();
		
		// Read trip category2
		WebElement departResult = driver.findElement(By.xpath("//*[@id=\"general\"]/div/div[3]/ul/li[1]/h4[2]"));
		String resDeparture = departResult.getText();
		
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(TRIP_TITLE);
			String dep = FormatManager.formatDate(trip.getDepartureDate());
			assertEquals(dep, resDeparture);
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		} finally {
			driver.close();
		}
	}
	
	@Test
	void testTripInfoReturnSelenium() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Click button to forward to trip info
		driver.findElement(By.xpath(INFOBTN_XPATH)).click();
		
		// Read trip category2
		WebElement returnResult = driver.findElement(By.xpath("//*[@id=\"general\"]/div/div[3]/ul/li[2]/h4[2]"));
		String resReturn = returnResult.getText();
		
		try {
			Trip trip = TripDao.getInstance().getTripByTitle(TRIP_TITLE);
			String dep = FormatManager.formatDate(trip.getReturnDate());
			assertEquals(dep, resReturn);
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		} finally {
			driver.close();
		}
	}

}
