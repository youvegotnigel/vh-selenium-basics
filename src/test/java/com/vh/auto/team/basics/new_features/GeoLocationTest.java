package com.vh.auto.team.basics.new_features;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class GeoLocationTest {

    private ChromeDriver driver;

    private double latitude = 51.509865;
    private double longitude = -0.118092;
    private double accuracy = 1;

    private By geolocation = By.id("geolocation_decimal");
    private By map = By.id("map_wrap");

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void mockGeolocation() {

        Map coordinates = new HashMap();

        coordinates.put("latitude", latitude);
        coordinates.put("longitude", longitude);
        coordinates.put("accuracy", accuracy);

        driver.executeCdpCommand("Emulation.setGeolocationOverride", coordinates);

        driver.get("https://www.where-am-i.co/");

        explicitWait(geolocation);
        String latitudeText = driver.findElement(geolocation).getText().split(",")[0].trim();
        String longitudeText = driver.findElement(geolocation).getText().split(",")[1].trim();

        //scroll to map
        new Actions(driver).scrollToElement(driver.findElement(map)).perform();

        Assert.assertEquals(latitudeText, String.valueOf(latitude));
        Assert.assertEquals(longitudeText, String.valueOf(longitude));

        sleep(20);
    }

    public void explicitWait(By element){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    public void sleep(int seconds){

        try {
            System.out.println("Waiting for " + seconds + " seconds...");
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
