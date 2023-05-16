package com.vh.auto.team.basics.new_features;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.security.Security;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class IgnoreCertificateTest {

    private ChromeDriver driver;
    DevTools devTools;

    @BeforeClass
    public void setUp() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        devTools = driver.getDevTools();
    }

    @Test(priority = 1)
    public void loadInsecureWebsiteTest(){

        devTools.createSession();
        devTools.send(Security.setIgnoreCertificateErrors(true));
        driver.get("https://self-signed.badssl.com/");

        sleep(5);
        Assert.assertTrue(driver.findElement(By.cssSelector("#content")).isDisplayed());
    }

    @Test(priority = 2)
    public void doNotLoadInsecureWebsiteTest(){

        devTools.createSession();
        devTools.send(Security.setIgnoreCertificateErrors(false));
        driver.get("https://self-signed.badssl.com/");

        sleep(5);
        Assert.assertTrue(driver.findElement(By.cssSelector("#content")).isDisplayed());
    }

    public void sleep(int seconds){

        try {
            System.out.println("Waiting for " + seconds + " seconds...");
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
