package com.vh.auto.team.basics.exceptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StaleElementReferenceTest {


    private WebDriver driver;

    @BeforeClass
    public void setup(){

        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();

        driver = new ChromeDriver(chromeOptions);

        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();

    }

    @Test
    public void test(){


        int retryCount = 0;


        while (retryCount < 3) {
            try {

                WebElement loginBtn = driver.findElement(By.cssSelector("#login-button"));


                try {
                    System.out.println("Sleeping...");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Assert.assertTrue(loginBtn.isDisplayed(), "Login button should be displayed");
                break;

            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException Caught, Retrying....");

            }

            retryCount++;
        }


    }


    @AfterClass
    public void teardown(){
        driver.quit();
    }

}
