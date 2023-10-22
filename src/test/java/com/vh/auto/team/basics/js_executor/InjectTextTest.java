package com.vh.auto.team.basics.js_executor;

import com.vh.auto.team.basics.listner.ListenerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class InjectTextTest {

    private static WebDriver driver;


    @BeforeClass
    public void setup() {
        ChromeOptions co = new ChromeOptions();
        co.setBrowserVersion("stable");
        driver = new ChromeDriver(co);
        driver.manage().window().maximize();
        driver.get("https://test.intouchwithhealth.com/8.6/FlowManager");
    }


    @Test
    public void injectTextTest() throws InterruptedException {

        findElement(By.id("ctl00_ctl00_cphMain_cphContent_LoginView_Login_Username")).sendKeys("*********");
        findElement(By.id("ctl00_ctl00_cphMain_cphContent_LoginView_Login_Password")).sendKeys("*********");
        findElement(By.id("ctl00_ctl00_cphMain_cphContent_LoginView_Login_LoginButton")).click();
        findElement(By.xpath("//a[normalize-space()='Room Manager']")).click();
        findElement(By.xpath("//a[@id='cphNav_lkbSearch']")).click();
        findElement(By.id("cphMain_AdhocSearchFilters_rblBookingType_0")).click();

        Thread.sleep(3000);

        WebElement location = findElement(By.xpath("//*[@id='ctl00_cphMain_AdhocSearchFilters_rdtSearchLocation']/descendant-or-self::*[@class='rddtFakeInput']"));
        String locationText = "Children's Unit First Floor";

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].textContent = arguments[1];", location, locationText);

        System.out.println("JS script done....");

        Thread.sleep(30000);

    }

    public WebElement findElement(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
