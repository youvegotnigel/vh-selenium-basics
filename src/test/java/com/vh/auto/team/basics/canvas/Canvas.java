package com.vh.auto.team.basics.canvas;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;

public class Canvas {

    public By locator;
    public WebDriver driver;

    public Canvas(By locator, WebDriver driver) {
        this.locator = locator;
        this.driver = driver;
    }

    public void getCanvasData(double radius) {

        Actions actions = new Actions(driver);
        WebElement element = driver.findElement(locator);

        actions.moveToElement(element).perform();
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);

        List<Pair<Double, Double>> coordinates = getCoordinatesOnCircle(radius);
        for(var cord : coordinates) {
            int x = (int) Math.round(cord.getLeft());
            int y = (int) Math.round(cord.getRight());
            System.out.println("(" + x + ", " + y + ")");
            actions.moveToElement(element, x, y).perform();
            System.out.println("tool tip text :: " + getToolTipText(By.id("tooltip")));
        }
    }

    public static List<Pair<Double, Double>> getCoordinatesOnCircle(double radius) {

        List<Pair<Double, Double>> coordinates = new ArrayList<>();
        for (int angleInDegrees = 0; angleInDegrees <= 360; angleInDegrees++) {
            double angleInRadians = Math.toRadians(angleInDegrees); // Convert degrees to radians
            double x = radius * Math.cos(angleInRadians) / 2.0;
            double y = radius * Math.sin(angleInRadians) / 2.0;
            coordinates.add(Pair.of(x,y));
        }
        return coordinates;
    }

    private String getToolTipText(By by) {
        return driver.findElement(by).getText().trim();
    }



}
