package com.vh.auto.team.basics.remotewebdriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class RemoteWebDriverTest {


    private WebDriver driver;

    @BeforeMethod
    public void setup() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.get("https://www.saucedemo.com/");

        driver.manage().window().maximize();
    }


    @Test
    public void test1(){
        System.out.println(driver.getTitle());

        Capabilities caps = ((RemoteWebDriver)driver).getCapabilities();

        System.out.println(caps.getBrowserName());
        System.out.println(caps.getBrowserVersion());
        System.out.println(caps.getPlatformName());
        System.out.println(caps.getCapabilityNames());

    }


    @AfterMethod
    public void tearDown() {
        driver.quit();
    }


    public void explicitWaitMethod(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }
}
