package com.vh.auto.team.basics.waits;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class WaitTest {

    private WebDriver driver;
    private By start = By.cssSelector("#start button");
    private By finish = By.cssSelector("#finish h4");
    private By loadingIndicator = By.id("loading");


    @BeforeClass
    public void setup() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.get("http://the-internet.herokuapp.com/dynamic_loading/1");
        driver.manage().window().maximize();

        //explicitWaitMethod(start);
        driver.findElement(start).click();
    }

    @Test
    public void test1() {

        try {
            System.out.println("Waiting for 5 seconds using Thread.sleep()");
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assert.assertEquals(driver.findElement(finish).getText().trim(), "Hello World!");
    }

    @Test
    public void test2() {

        driver.navigate().to("https://google.com/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        Assert.assertEquals(driver.findElement(finish).getText().trim(), "Hello World!");
    }

    @Test
    public void test3() {

        StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIndicator));

        Assert.assertEquals(driver.findElement(finish).getText().trim(), "Hello World!");

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

        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIndicator));

        Assert.assertEquals(driver.findElement(finish).getText().trim(), "Hello World!");

        stopwatch.stop();
        System.out.println(stopwatch);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    public void explicitWaitMethod(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }


}
