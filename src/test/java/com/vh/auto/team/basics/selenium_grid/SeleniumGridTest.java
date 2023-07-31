package com.vh.auto.team.basics.selenium_grid;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class SeleniumGridTest {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final String REMOTE_URL = "http://localhost:4444";
    private static final By USERNAME_TEXT_BOX = By.id("user-name");
    private static final By PASSWORD_TEXT_BOX = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By BACKPACK_BUTTON = By.cssSelector("[data-test=add-to-cart-sauce-labs-backpack]");
    private static final By BIKE_LIGHT_BUTTON = By.cssSelector("[data-test=add-to-cart-sauce-labs-bike-light]");
    private static final By CART_ITEMS = By.cssSelector(".shopping_cart_badge");
    private static final By SHOPPING_CART_BUTTON = By.id("shopping_cart_container");
    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By CONTINUE_BUTTON = By.id("continue");
    private static final By FINISH_BUTTON = By.id("finish");


    @Parameters({"browser", "browser_version"})
    @BeforeTest
    private void setup(String browser, String version) {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setPlatform(Platform.LINUX);
        capabilities.setVersion(version);
        capabilities.setBrowserName(browser); //MicrosoftEdge, chrome, firefox

        switch (browser) {

            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                //chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--start-maximized");
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                //firefoxOptions.addArguments("-headless");
                firefoxOptions.addArguments("--start-maximized");
                capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                break;

            case "MicrosoftEdge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                capabilities.setCapability(EdgeOptions.CAPABILITY, edgeOptions);
                break;
        }

        // Create a RemoteWebDriver instance with the Selenium Grid URL and desired capabilities
        try {
            driver.set(new RemoteWebDriver(new URL(REMOTE_URL), capabilities));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Navigate to the login page
        getDriver().get("https://www.saucedemo.com/");
        getDriver().manage().window().maximize();

        // getDriver().manage().window().setSize(new Dimension(1440, 900));
    }

    @Parameters("browser")
    @Test
    public void login_test(String browser) {

        getDriver().findElement(USERNAME_TEXT_BOX).sendKeys("standard_user");
        getDriver().findElement(PASSWORD_TEXT_BOX).sendKeys("secret_sauce");
        getDriver().findElement(LOGIN_BUTTON).click();
        takeScreenshot(browser, "LANDING_PAGE");

        Assert.assertEquals(getDriver().getCurrentUrl(), "https://www.saucedemo.com/inventory.html"); // verify landing page
    }

    @Parameters("browser")
    @Test
    public void add_to_cart_test(String browser) {
        getDriver().findElement(BACKPACK_BUTTON).click(); // add backpack to cart
        getDriver().findElement(BIKE_LIGHT_BUTTON).click(); // add bike light to cart
        String totalItems = getDriver().findElement(CART_ITEMS).getText().trim();
        takeScreenshot(browser, "INVENTORY_PAGE");

        Assert.assertEquals(totalItems, "2"); // verify items added to cart
    }

    @Parameters("browser")
    @Test
    public void checkout_test(String browser) {
        getDriver().findElement(SHOPPING_CART_BUTTON).click();
        getDriver().findElement(CHECKOUT_BUTTON).click();
        takeScreenshot(browser, "CHECKOUT_INFORMATION_PAGE");

        fillCheckoutInformation("First Name", "John");
        fillCheckoutInformation("Last Name", "Taylor");
        fillCheckoutInformation("Zip/Postal Code", "60611");
        getDriver().findElement(CONTINUE_BUTTON).click();
        takeScreenshot(browser, "CHECKOUT_OVERVIEW_PAGE");

        Assert.assertEquals(getAmountForLabel(".summary_subtotal_label"), "Item total: $39.98");
        Assert.assertEquals(getAmountForLabel(".summary_tax_label"), "Tax: $3.20");
        Assert.assertEquals(getAmountForLabel(".summary_info_label.summary_total_label"), "Total: $43.18");
    }

    @Parameters("browser")
    @Test
    public void complete_checkout_test(String browser) {
        getDriver().findElement(FINISH_BUTTON).click();
        takeScreenshot(browser, "CHECKOUT_COMPLETE_PAGE");

        Assert.assertEquals(getDriver().getCurrentUrl(), "https://www.saucedemo.com/checkout-complete.html"); // verify landing page
        Assert.assertEquals(getDriver().findElement(By.xpath("//h2")).getText().trim(), "Thank you for your order!");
    }

    private void fillCheckoutInformation(String question, String answer) {
        String xpath = String.format("//input[@placeholder='%s']", question);
        getDriver().findElement(By.xpath(xpath)).sendKeys(answer);
    }

    private String getAmountForLabel(String label) {
        return getDriver().findElement(By.cssSelector(label)).getText().trim();
    }

    private static WebDriver getDriver() {
        return driver.get();
    }

    @AfterTest
    private void teardown() {
        getDriver().quit();
        driver.remove();
    }

    private void takeScreenshot(String browser, String fileName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) getDriver();
            File file = screenshot.getScreenshotAs(OutputType.FILE);

            // Create the "screenshot" folder if it doesn't exist
            Path screenshotFolderPath = Path.of("screenshots");
            if (!Files.exists(screenshotFolderPath)) {
                Files.createDirectory(screenshotFolderPath);
            }

            FileUtils.copyFile(file, new File(screenshotFolderPath.toFile(), fileName + "_" + browser.toUpperCase() + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
