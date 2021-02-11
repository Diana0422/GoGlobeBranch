package test.selenium;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class TestPlanTripSelenium {
	
	WebDriver driver;
	private static final String DRIVER_PROPERTY = "webdriver.chrome.driver";
	private static final String DRIVER_PATH = "Drivers/chromedriverMacOS";
	
	private static final String USERNAME = "lorenzo@gmail.com";
	private static final String PASS = "ciao";
	private static final String TRIPNAME = "Test Trip";
	private static final String DEP_DATE = "16/03/2021";
	private static final String RET_DATE = "16/03/2021";
	
	//Login Xpaths
	private static final String LOGIN_PAGE_URL = "http://localhost:8080/ISPWProject20-21/view/login.jsp";
	private static final String XPATH_EMAIL_TF = "//*[@id=\"username\"]";
	private static final String XPATH_PASSWORD_TF = "//*[@id=\"password\"]";
	private static final String XPATH_SIGNIN_BTN = "//*[@id=\"login-body\"]/div/div/form/div[3]/input";

	
	//Home Xpaths
	private static final String XPATH_PLANTRIP_BTN = "//*[@id=\"bootstrap-override-home\"]/div[2]/div[2]/form/button";
	
	//SelectTripPreferences Xpaths
	private static final String XPATH_TRIPNAME_TF = "//*[@id=\"tripName\"]";
	private static final String XPATH_DEPDATE_TF = "//*[@id=\"depDate\"]";
	private static final String XPATH_RETDATE_TF = "//*[@id=\"retDate\"]";
	private static final String XPATH_CATEGORY1_BOX = "//*[@id=\"selectCategory1\"]";
	private static final String XPATH_CATEGORY2_BOX = "//*[@id=\"selectCategory2\"]";
	private static final String XPATH_NEXT_BTN = "//*[@id=\"form-btns\"]/button[2]";
	
	//PlanTripView Xpaths
	private static final String XPATH_TRIPNAME = "//*[@id=\"trip-title\"]";



	@Test
	void test() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);		
		 driver = new ChromeDriver();
		 
		 driver.get(LOGIN_PAGE_URL);
		 
		 //Inserts email and password and logs in
			driver.findElement(By.xpath(XPATH_EMAIL_TF)).sendKeys(USERNAME);
			driver.findElement(By.xpath(XPATH_PASSWORD_TF)).sendKeys(PASS);
			driver.findElement(By.xpath(XPATH_SIGNIN_BTN)).click();
			
		//Opens plan trip 
			driver.findElement(By.xpath(XPATH_PLANTRIP_BTN)).click();
			
		//Inserts data to create new trip
			driver.findElement(By.xpath(XPATH_TRIPNAME_TF)).sendKeys(TRIPNAME);
			driver.findElement(By.xpath(XPATH_DEPDATE_TF)).sendKeys(DEP_DATE);
			driver.findElement(By.xpath(XPATH_RETDATE_TF)).sendKeys(RET_DATE);
			Select category1 = new Select(driver.findElement(By.xpath(XPATH_CATEGORY1_BOX)));
			category1.selectByVisibleText("Adventure");
			Select category2 = new Select(driver.findElement(By.xpath(XPATH_CATEGORY2_BOX)));
			category2.selectByVisibleText("Culture");
		    
			driver.findElement(By.xpath(XPATH_NEXT_BTN)).click();
			
			WebElement titleResult = driver.findElement(By.xpath(XPATH_TRIPNAME));
			String title = titleResult.getText();
			assertEquals(TRIPNAME, title);
			
			driver.close();
		
	}

}
