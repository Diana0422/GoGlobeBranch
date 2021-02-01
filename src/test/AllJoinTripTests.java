package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.selenium.TestSearchTripSelenium;
import test.selenium.TestTripInfoSelenium;

@RunWith(Suite.class)
@SuiteClasses({ TestJoinTripController.class, TestSearchTripSelenium.class, TestTripInfoSelenium.class })

public class AllJoinTripTests {

}
