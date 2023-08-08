package com.vh.auto.team.basics.new_features;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v115.network.Network;
import org.openqa.selenium.devtools.v115.network.model.ConnectionType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.openqa.selenium.devtools.v115.network.Network.loadingFailed;
import static org.testng.AssertJUnit.assertEquals;

public class SlowNetworkTest {

    private ChromeDriver driver;
    DevTools devTools;

    @BeforeClass
    public void setUp () {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        devTools = driver.getDevTools();
    }

    @Test(priority = 2)
    public void enableSlowNetwork () {
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.emulateNetworkConditions(false, 150, 25000,2000, Optional.of(ConnectionType.CELLULAR3G)));

        driver.get("https://www.amazon.com/");
    }


    @Test(priority = 1)
    public void enableOfflineNetwork () {
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.emulateNetworkConditions(true, 10, 100, 50, Optional.of(ConnectionType.WIFI)));
        devTools.addListener(loadingFailed(), loadingFailed -> assertEquals(loadingFailed.getErrorText(), "net::ERR_INTERNET_DISCONNECTED"));

        try {
            driver.get("https://www.google.com");
        } catch (WebDriverException exc) {
            driver.findElement(By.id("game-button")).click();
            System.out.println("Offline " + driver.getTitle() + " Page Did Not Load");
        }


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }
}
