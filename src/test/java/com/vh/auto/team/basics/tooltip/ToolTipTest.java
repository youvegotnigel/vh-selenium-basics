package com.vh.auto.team.basics.tooltip;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class ToolTipTest {

    private WebDriver driver;

    private By qty = By.xpath("//input[contains(@id,'quantity')]");
    private By cart_btn = By.xpath("//button[normalize-space()='Add to cart']");

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://askomdch.com/product/anchor-bracelet/");
    }

    @Test
    public void toolTipTest() throws InterruptedException {

        explicitWaitMethod(qty);
        driver.findElement(qty).clear();
        driver.findElement(qty).sendKeys("0");

        driver.findElement(cart_btn).click();

        Thread.sleep(1000);
        String toolTipText = driver.findElement(qty).getAttribute("validationMessage");
        System.out.println("Tool Tip Text ::: " + toolTipText);

        Assert.assertEquals(toolTipText, "Value must be greater than or equal to 1.");
    }

    public void explicitWaitMethod(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }
}
