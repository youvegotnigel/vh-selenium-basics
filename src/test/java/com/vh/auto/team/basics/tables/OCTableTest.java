package com.vh.auto.team.basics.tables;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OCTableTest {

    private WebDriver driver;

    @BeforeMethod
    public void setup() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("file:///C:/Users/nigel.mulholland/Desktop/oc.html");
        driver.manage().window().maximize();
    }

    @Test
    public void test1(){

        String tableHeaderName = "ALC Destination Summary";
        List<String> columnHeaders = Arrays.asList("LOC", "CCC", "HOME", "OTHERS");

        WebElement table = identifyTable(tableHeaderName, columnHeaders);

        System.out.println("Is table dispayed? :: " + table.isDisplayed());
    }

    /**
     * Identify and return the table WebElement based on the table header and column headers.
     *
     * @param tableHeaderName   the value of the table header in the headerNamePanel
     * @param columnHeaders     a list of column header names in the table
     * @return the WebElement representing the identified table
     * @throws NoSuchElementException if the table is not found
     */
    public WebElement identifyTable(String tableHeaderName, List<String> columnHeaders) {
        String tableHeaderXpath = String.format("(//div[@class='headerNamePanel' and normalize-space(.)=\"%s\"]/../following-sibling::table|//div[@class='headerNamePanel']/*[normalize-space(.)=\"%s\"]/../../following::table)[1]", tableHeaderName, tableHeaderName);
        String columnHeadersXpath = getColumnHeadersXpath(columnHeaders);
        System.out.println("columnHeadersXpath ::: \n" + columnHeadersXpath);

        String combinedXpath = String.format("(//*[%s]/following-sibling::table)[%s]", tableHeaderXpath, columnHeadersXpath);
        System.out.println("combinedXpath ::: \n" + combinedXpath);

        WebElement table = findChild(null, By.xpath(combinedXpath));

        if (Objects.nonNull(table)) {
            //jsScrollIntoView(table);
            return table;
        } else {
            throw new NoSuchElementException("Table not found with the given header and column headers.");
        }
    }

    // Helper method to generate the XPath for column headers
    private static String getColumnHeadersXpath(List<String> columnHeaders) {
        StringBuilder xpathBuilder = new StringBuilder();

        for (String columnHeader : columnHeaders) {
            String columnHeaderXpath = String.format(".//th[normalize-space(.)='%s']", columnHeader);
            xpathBuilder.append(columnHeaderXpath).append(" and ");
        }

        String columnHeadersXpath = xpathBuilder.toString();
        return columnHeadersXpath.substring(0, columnHeadersXpath.length() - 5); // Remove the trailing " and "
    }

    public WebElement findChild(WebElement parent, By findBy) {
        List<WebElement> elements = findChildren(parent, findBy);
        return !elements.isEmpty() ? elements.get(0) : null;
    }

    public List<WebElement> findChildren(WebElement parent, By findBy) {

        int timeout = 10;
        int retryCount = 0;
        while (retryCount < 3) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
                if (parent != null) {
                    return wait.until(d -> parent.findElements(findBy));
                } else {
                    return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(findBy));
                }
            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException caught, retrying...");
            }
            retryCount++;
        }
        System.out.println("Maximum number of retries exceeded, unable to find children");
        return Collections.emptyList();
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
