package com.vh.auto.team.basics.exceptions;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class MCAPTest {

    private WebDriver driver;

    private By username = By.cssSelector("#login_username");
    private By password = By.cssSelector("#login_password");
    private By login = By.name("page_login");
    private By permission_checkbox = By.xpath("//input[@id='set_permissions']");
    private By admin_panel_permission_checkbox = By.xpath("//input[@id='apps[1][pages]']");
    private By application_permission_checkbox = By.xpath("//input[@id='apps[13][pages]']");


    @BeforeMethod
    public void setup() {


        driver = new ChromeDriver();
        driver.get("MCAP_BACKEND_URL");
        driver.manage().window().maximize();

        driver.findElement(username).sendKeys("MCAP_BACKEND_USERNAME");
        driver.findElement(password).sendKeys("MCAP_BACKEND_PASSWORD");
        driver.findElement(login).click();
    }

    @Test
    public void test1(){

        click_on_tab_menu_link("User Groups");
        driver.findElement(By.xpath("//div[@class='page_actions']/a")).click();

        //Click on checkbox for set basic user permission
        explicitWaitMethod(permission_checkbox);
        driver.findElement(permission_checkbox).click();

        //click on page access for Admin Panel
        explicitWaitMethod(admin_panel_permission_checkbox);
        driver.findElement(admin_panel_permission_checkbox).click();



        int retryCount = 0;
        while (retryCount < 3) {
            try {

                //click on page access for Application
                explicitWaitMethod(application_permission_checkbox);
                driver.findElement(application_permission_checkbox).click();

                try {
                    System.out.println("Sleeping...");
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                break;

            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException Caught, Retrying....");

            }

            retryCount++;
        }


        //click on page access for Application
        //explicitWaitMethod(application_permission_checkbox);
        //driver.findElement(application_permission_checkbox).click();
    }


    private void click_on_tab_menu_link(String link){

        String xpath = String.format("//div[@class='admin_menu']/following-sibling::ul/descendant-or-self::a[text()='%s']", link);
        By by = By.xpath(xpath);
        explicitWaitMethod(by);
        driver.findElement(by).click();
    }


    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    public void explicitWaitMethod(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

}
