package com.vh.auto.team.basics.testng;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

public class WeatherGridTest {

    private final By CELSIUS_TEMP = By.id("wob_tm");
    private final By FAHRENHEIT_TEMP = By.id("wob_ttm");
    private final By DATE_TIME = By.id("wob_dts");
    private final By WEATHER = By.id("wob_dc");
    private final By LOCATION = By.cssSelector(".BBwThe");
    private final By TEMP_BUTTON = By.xpath("(//span[@aria-label='°Fahrenheit'])[1]");
    private static final String REMOTE_URL = "http://localhost:4444";
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @Parameters({"browser", "url"})
    @BeforeMethod
    public void setup(String browser, String url) {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser); //MicrosoftEdge, chrome, firefox
        capabilities.setPlatform(Platform.LINUX);

        // Create a RemoteWebDriver instance with the Selenium Grid URL and desired capabilities
        try {
            driver.set(new RemoteWebDriver(new URL(REMOTE_URL), capabilities));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        getDriver().manage().window().maximize();
        getDriver().get(url);
    }

    public static WebDriver getDriver(){
        return driver.get();
    }

    @Test(dataProvider = "LOCATIONS")
    public void weather_test(String city){

        String site = "https://www.google.com/search?q=weather=" + city;
        getDriver().navigate().to(site);

        System.out.println("City     : " + getDriver().findElement(LOCATION).getText());
        System.out.println("Datetime : " + getDriver().findElement(DATE_TIME).getText());
        System.out.println("Weather  : " + getDriver().findElement(WEATHER).getText());
        System.out.println("Temperature in Celcius : " + getDriver().findElement(CELSIUS_TEMP).getText());
        getDriver().findElement(TEMP_BUTTON).click();
        System.out.println("Temperature in Fahrenheit : " + getDriver().findElement(FAHRENHEIT_TEMP).getText());
        System.out.println("------------------------------------------------------------------------------\n");

    }

    @AfterMethod
    public void teardown(){
        getDriver().quit();
        driver.remove();
    }

    @DataProvider(name = "LOCATIONS", parallel = true)
    public static Object[][] locations() {
        return new Object[][]{
                {"Colombo"},
                {"Kandy"},
                {"Negombo"},
                {"Galle"},
                {"Singapore"},
                {"Tokyo"},
                {"Vancouver"},
                {"Toronto"}
        };
    }


}
