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

import logic.persistence.dao.TripDao;
import logic.persistence.exceptions.DBConnectionException;

/**
 * @author diana pasquali
 */

public class TestSearchTripSelenium {
	
	private static final String TRIP_TITLE = "TestViaggio";
	private static final String DRIVER_PROPERTY = "webdriver.chrome.driver";
	private static final String DRIVER_PATH = "Drivers/chromedriver.exe";
	private static final String PAGE_URL = "http://localhost:8080/ISPWProject20-21/view/joinTrip.jsp";
	private static final String SEARCHBAR_XPATH = "//*[@id=\"bar\"]";
	private static final String SEARCHBTN_XPATH = "//*[@id=\"search-btn\"]";
	private static final String ERROR_MSG = "EXCEPTION IN TEST SEARCH TRIP: "+TRIP_TITLE+" NOT FOUND IN DATABASE.";

	@Test
	void testSearchTripTitle() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		// Read cards
		WebElement titleResult = driver.findElement(By.xpath("//*[@id=\"title\"]"));
		String title = titleResult.getText();
		Logger.getGlobal().log(Level.INFO, title);
		assertEquals(TRIP_TITLE, title);
		driver.close();
	}
	
	@Test
	void testSearchTripPrice() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		WebElement priceResult = driver.findElement(By.xpath("//*[@id=\"bootstrap-override\"]/div[2]/form/div[1]/div/div/div/div/div[1]/h5"));
		String priceStr = priceResult.getText();
		Logger.getGlobal().log(Level.INFO, priceStr);
		try {
			int expected = TripDao.getInstance().getTripByTitle(TRIP_TITLE).getPrice();
			String expectedStr = expected+"€";
			assertEquals(expectedStr, priceStr);
			driver.close();
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		}
	}
	
	@Test
	void testSearchTripAgeRange() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		WebElement ageResult = driver.findElement(By.xpath("//*[@id=\"bootstrap-override\"]/div[2]/form/div[1]/div/div/div/div/div[3]/h6"));
		String ageStr = ageResult.getText();
		Logger.getGlobal().log(Level.INFO, ageStr);
		try {
			int expectedMin = TripDao.getInstance().getTripByTitle(TRIP_TITLE).getMinAge();
			int expectedMax = TripDao.getInstance().getTripByTitle(TRIP_TITLE).getMaxAge(); 
			String expectedStr = "age "+expectedMin+" - "+expectedMax;
			assertEquals(expectedStr, ageStr);
			driver.close();
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		}
	}
	
	@Test
	void testSearchTripCategory1() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		WebElement cat1Result = driver.findElement(By.xpath("//*[@id=\"bootstrap-override\"]/div[2]/form/div[1]/div/div/div/div/div[5]/h6[1]"));
		String catStr = cat1Result.getText();
		Logger.getGlobal().log(Level.INFO, catStr);
		try {
			String expectedCat1 = TripDao.getInstance().getTripByTitle(TRIP_TITLE).getCategory1().toString();
			assertEquals(expectedCat1, catStr);
			driver.close();
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		}
	}
	
	@Test
	void testSearchTripCategory2() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		driver.get(PAGE_URL);
		
		// Find search bar element and search trip
		driver.findElement(By.xpath(SEARCHBAR_XPATH)).sendKeys(TRIP_TITLE);
		driver.findElement(By.xpath(SEARCHBTN_XPATH)).click();
		
		WebElement cat2Result = driver.findElement(By.xpath("//*[@id=\"bootstrap-override\"]/div[2]/form/div[1]/div/div/div/div/div[5]/h6[3]"));
		String catStr = cat2Result.getText();
		Logger.getGlobal().log(Level.INFO, catStr);
		try {
			String expectedCat2 = TripDao.getInstance().getTripByTitle(TRIP_TITLE).getCategory2().toString();
			assertEquals(expectedCat2, catStr);
			driver.close();
		} catch (DBConnectionException | SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, ERROR_MSG);
		}
	}

}
