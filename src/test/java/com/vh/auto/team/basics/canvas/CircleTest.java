package com.vh.auto.team.basics.canvas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

public class CircleTest {

    // Data for the pie chart
    // var data = [25, 35, 40];
    // var labels = ['Red', 'Blue', 'Yellow'];
    // var angles = [ 90,     126,     144];

    private WebDriver driver;
    private static final By CANVAS = By.id("pieChartCanvas");

    @BeforeMethod
    public void setup() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBrowserVersion("stable");
        chromeOptions.addArguments("window-size=500,600");

        driver = new ChromeDriver(chromeOptions);

        String filePath = System.getProperty("user.dir") + File.separator + "pages" + File.separator + "canvas-circle.html";
        driver.get("file://" + filePath);

        //driver.manage().window().maximize();
    }

    @Test
    public void clickOnBlueTest() {

        explicitWaitMethod(CANVAS);
        WebElement circle = driver.findElement(CANVAS);

        var dimensions = circle.getSize();
        int center_x = dimensions.getWidth() / 2;
        int center_y = dimensions.getHeight() / 2;
        int radius = dimensions.getHeight() / 3;

        System.out.printf("center of circle (x,y) = (%d,%d)\n", center_x, center_y);
        System.out.println("Radius = " + radius);

        int angleInDegrees = 90 + 126/2; // measuring angle from x axis clockwise
        double angleInRadians = Math.toRadians(angleInDegrees); // Convert degrees to radians
        int coordinate_x = (int) (radius * Math.cos(angleInRadians) / 2.0);
        int coordinate_y = (int) (radius * Math.sin(angleInRadians) / 2.0);

        System.out.printf("point of circle (x,y) = (%d,%d)\n", coordinate_x, coordinate_y);

        //Click button on the canvas
        new Actions(driver)
                .moveToElement(circle, coordinate_x, coordinate_y)
                .click()
                .perform();

        String tooltip = driver.findElement(By.id("tooltip")).getText().trim();
        Assert.assertEquals(tooltip, "Blue (35%)");
    }

    @Test
    public void clickOnYellowTest() {

        explicitWaitMethod(CANVAS);
        WebElement circle = driver.findElement(CANVAS);

        var dimensions = circle.getSize();
        int center_x = dimensions.getWidth() / 2;
        int center_y = dimensions.getHeight() / 2;
        int radius = dimensions.getHeight() / 3;

        System.out.printf("center of circle (x,y) = (%d,%d)\n", center_x, center_y);
        System.out.println("Radius = " + radius);

        int angleInDegrees = 90 + 126 + 144/2; // measuring angle from x axis clockwise
        double angleInRadians = Math.toRadians(angleInDegrees); // Convert degrees to radians
        int coordinate_x = (int) (radius * Math.cos(angleInRadians) / 2.0);
        int coordinate_y = (int) (radius * Math.sin(angleInRadians) / 2.0);

        System.out.printf("point of circle (x,y) = (%d,%d)\n", coordinate_x, coordinate_y);

        //Click button on the canvas
        new Actions(driver)
                .moveToElement(circle, coordinate_x, coordinate_y)
                .click()
                .perform();

        String tooltip = driver.findElement(By.id("tooltip")).getText().trim();
        Assert.assertEquals(tooltip, "Yellow (40%)");
    }


    @Test
    public void clickOnRedTest() {

        explicitWaitMethod(CANVAS);
        WebElement circle = driver.findElement(CANVAS);

        var dimensions = circle.getSize();
        int center_x = dimensions.getWidth() / 2;
        int center_y = dimensions.getHeight() / 2;
        int radius = dimensions.getHeight() / 3;

        System.out.printf("center of circle (x,y) = (%d,%d)\n", center_x, center_y);
        System.out.println("Radius = " + radius);

        int angleInDegrees = 90/2; // measuring angle from x axis clockwise
        double angleInRadians = Math.toRadians(angleInDegrees); // Convert degrees to radians
        int coordinate_x = (int) (radius * Math.cos(angleInRadians) / 2.0);
        int coordinate_y = (int) (radius * Math.sin(angleInRadians) / 2.0);

        System.out.printf("point of circle (x,y) = (%d,%d)\n", coordinate_x, coordinate_y);

        //Click button on the canvas
        new Actions(driver)
                .moveToElement(circle, coordinate_x, coordinate_y)
                .click()
                .perform();

        String tooltip = driver.findElement(By.id("tooltip")).getText().trim();
        Assert.assertEquals(tooltip, "Red (25%)");
    }

    @Test
    public void rotateOverCanvasTest() {

        Canvas canvas = new Canvas(CANVAS, driver);

        WebElement circle = driver.findElement(CANVAS);
        var dimensions = circle.getSize();
        int radius = dimensions.getHeight() / 3;

        canvas.getCanvasData(radius);
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
