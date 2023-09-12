package com.vh.auto.team.basics.sauce;

import com.vh.auto.team.basics.listner.ListenerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;


@Listeners(ListenerClass.class)
public class SauceTest {

    private static WebDriver driver;
    private static final By USERNAME_TEXT_BOX = By.id("user-name");
    private static final By PASSWORD_TEXT_BOX = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_BUTTON = By.id("logout_sidebar_link");
    private static final By BACKPACK_BUTTON = By.cssSelector("[data-test=add-to-cart-sauce-labs-backpack]");
    private static final By BIKE_LIGHT_BUTTON = By.cssSelector("[data-test=add-to-cart-sauce-labs-bike-light]");
    private static final By CART_ITEMS = By.cssSelector(".shopping_cart_badge");
    private static final By SHOPPING_CART_BUTTON = By.id("shopping_cart_container");
    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By CONTINUE_BUTTON = By.id("continue");
    private static final By FINISH_BUTTON = By.id("finish");

    @BeforeClass
    public void setup() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBrowserVersion("118");
        driver = new FirefoxDriver(firefoxOptions);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void login_test() throws InterruptedException {
        findElement(USERNAME_TEXT_BOX).sendKeys("standard_user");
        findElement(PASSWORD_TEXT_BOX).sendKeys("secret_sauce");
        findElement(LOGIN_BUTTON).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html"); // verify landing page

        Thread.sleep(5000);
    }

    @Test(priority = 2)
    public void add_to_cart_test() {
        findElement(BACKPACK_BUTTON).click(); // add backpack to cart
        findElement(BIKE_LIGHT_BUTTON).click(); // add bike light to cart
        String totalItems =findElement(CART_ITEMS).getText().trim();

        Assert.assertEquals(totalItems, "2"); // verify items added to cart
    }

    @Test(priority = 3)
    public void checkout_test() {
        findElement(SHOPPING_CART_BUTTON).click();
        findElement(CHECKOUT_BUTTON).click();

        fillCheckoutInformation("First Name", "John");
        fillCheckoutInformation("Last Name", "Taylor");
        fillCheckoutInformation("Zip/Postal Code", "60611");
        findElement(CONTINUE_BUTTON).click();

        Assert.assertEquals(getAmountForLabel(".summary_subtotal_label"), "Item total: $39.98");
        Assert.assertEquals(getAmountForLabel(".summary_tax_label"), "Tax: $3.20");
        Assert.assertEquals(getAmountForLabel(".summary_info_label.summary_total_label"), "Total: $43.18");
    }

    @Test(priority = 4)
    public void complete_checkout_test() {
        findElement(FINISH_BUTTON).click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-complete.html"); // verify landing page
        Assert.assertEquals(findElement(By.xpath("//h2")).getText().trim(), "Thank you for your order!");
    }

    @Test(priority = 5)
    public void logout_test() {
        findElement(MENU_BUTTON).click();
        findElement(LOGOUT_BUTTON).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/"); // verify landing page
    }

    private void fillCheckoutInformation(String question, String answer) {
        String xpath = String.format("//input[@placeholder='%s']", question);
        findElement(By.xpath(xpath)).sendKeys(answer);
    }

    private String getAmountForLabel(String label) {
        return findElement(By.cssSelector(label)).getText().trim();
    }

    public WebElement findElement(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    public static WebDriver getDriver(){
        return driver;
    }

}
