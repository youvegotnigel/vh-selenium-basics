package com.vh.auto.team.basics.dropdown;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 *  Reference: https://www.selenium.dev/documentation/webdriver/support_features/select_lists/#select-option
 */
public class DropDownTest {


    private WebDriver driver;
    private By dropdown = By.name("selectomatic");

    @BeforeMethod
    public void setup() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBrowserVersion("stable");
        driver = new ChromeDriver(chromeOptions);

        String filePath = System.getProperty("user.dir") + "/pages/select.html";
        driver.get("file://" + filePath);

        driver.manage().window().maximize();
    }


    @Test
    public void select_by_index(){

        explicitWaitMethod(dropdown);

        Select select = new Select(driver.findElement(dropdown));

        //Select an option based on its index, beginning with 0
        select.selectByIndex(1);

        System.out.println("Selected Text = " + select.getFirstSelectedOption().getText());
        Assert.assertEquals(select.getFirstSelectedOption().getText().trim(), "Two");
    }

    @Test
    public void select_by_value(){

        explicitWaitMethod(dropdown);

        Select select = new Select(driver.findElement(dropdown));

        //Select an option based on its 'value' attribute
        select.selectByValue("still learning how to count, apparently");

        System.out.println("Selected Text = " + select.getFirstSelectedOption().getText());
        Assert.assertEquals(select.getFirstSelectedOption().getText().trim(), "Still learning how to count, apparently");
    }

    @Test
    public void select_by_visible_text(){

        explicitWaitMethod(dropdown);

        Select select = new Select(driver.findElement(dropdown));

        //Select an option based on the text over the option
        select.selectByVisibleText("Four");

        System.out.println("Selected Text = " + select.getFirstSelectedOption().getText());
        Assert.assertEquals(select.getFirstSelectedOption().getText().trim(), "Four");
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
