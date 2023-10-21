package com.vh.auto.team.basics.selenium_manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.manager.SeleniumManager;
import org.testng.annotations.Test;

public class SeleniumManagerTest {

    @Test
    public void chromeTest() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBrowserVersion("115.0");
        WebDriver driver = new ChromeDriver(chromeOptions);
        String driverPath = SeleniumManager.getInstance().getDriverPath(chromeOptions, true).getDriverPath();
        String browserPath = SeleniumManager.getInstance().getDriverPath(chromeOptions, true).getBrowserPath();
        System.out.println("DRIVER PATH  ::: " + driverPath);
        System.out.println("BROWSER PATH ::: " + browserPath);
        driver.get("https://www.google.com/");
        driver.quit();
        System.out.println("-----------------------------------------------------------------------------------\n");
    }


    @Test
    public void fireFoxTest() {

        FirefoxOptions ffo = new FirefoxOptions();
        ffo.setBrowserVersion("110.0");
        WebDriver driver = new FirefoxDriver(ffo);
        String driverPath = SeleniumManager.getInstance().getDriverPath(ffo, true).getDriverPath();
        String browserPath = SeleniumManager.getInstance().getDriverPath(ffo, true).getBrowserPath();
        System.out.println("DRIVER PATH  ::: " + driverPath);
        System.out.println("BROWSER PATH ::: " + browserPath);
        driver.get("https://www.google.com/");
        driver.quit();
        System.out.println("-----------------------------------------------------------------------------------\n");
    }


    @Test
    public void edgeTest() {

        EdgeOptions edgeOptions = new EdgeOptions();
        WebDriver driver = new EdgeDriver(edgeOptions);
        String driverPath = SeleniumManager.getInstance().getDriverPath(edgeOptions, true).getDriverPath();
        String browserPath = SeleniumManager.getInstance().getDriverPath(edgeOptions, true).getBrowserPath();
        System.out.println("DRIVER PATH  ::: " + driverPath);
        System.out.println("BROWSER PATH ::: " + browserPath);
        driver.get("https://www.google.com/");
        driver.quit();
        System.out.println("-----------------------------------------------------------------------------------\n");
    }


}
