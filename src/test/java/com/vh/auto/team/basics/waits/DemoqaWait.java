package com.vh.auto.team.basics.waits;

import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class DemoqaWait {


    private WebDriver driver;

    private By finish = By.xpath("//button[@id='visibleAfter']");

    @BeforeMethod
    public void setup() {

        driver = new ChromeDriver();

        driver.get("https://demoqa.com/dynamic-properties");
        driver.manage().window().maximize();
    }


    @Test
    public void test1() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assert.assertEquals(driver.findElement(finish).getText().trim(), "Visible After 5 Seconds");
    }


    @Test
    public void test2() {

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        Assert.assertEquals(driver.findElement(finish).getText().trim(), "Visible After 5 Seconds");
    }


    @Test
    public void test3() {

        StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(finish)));

        Assert.assertEquals(driver.findElement(finish).getText().trim(), "Visible After 5 Seconds");

        stopwatch.stop();
        System.out.println(stopwatch);
    }

    @Test
    public void test4() {

        StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(finish)));

        Assert.assertEquals(driver.findElement(finish).getText().trim(), "Visible After 5 Seconds");

        stopwatch.stop();
        System.out.println(stopwatch);
    }



    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

}
