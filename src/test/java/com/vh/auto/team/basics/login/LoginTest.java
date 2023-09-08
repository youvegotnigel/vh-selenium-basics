package com.vh.auto.team.basics.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private static final By USERNAME_TEXT_BOX = By.id("user-name");
    private static final By PASSWORD_TEXT_BOX = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_BUTTON = By.id("logout_sidebar_link");

    @BeforeClass
    public void setup() {
        ChromeOptions opt = new ChromeOptions();
        driver = new ChromeDriver(opt);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
    }

    @Test
    public void login_test() {
        findElement(USERNAME_TEXT_BOX).sendKeys("standard_user");
        findElement(PASSWORD_TEXT_BOX).sendKeys("secret_sauce");
        findElement(LOGIN_BUTTON).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html"); // verify landing page
    }

    @Test
    public void logout_test() {
        findElement(MENU_BUTTON).click();
        findElement(LOGOUT_BUTTON).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/"); // verify landing page
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
