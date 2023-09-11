package com.vh.auto.team.basics.window_handling;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Set;

public class MultipleWindowTest {

    private WebDriver driver;

    @BeforeClass
    public void setUp () 	{
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void test_open_new_tab(){

        SoftAssert softAssert = new SoftAssert();

        driver.get("https://www.google.com");
        softAssert.assertEquals(driver.getTitle(), "Google");
        String parentWindow= driver.getWindowHandle();
        System.out.println("Parent Window Handle = " + parentWindow);

        // new tab in the same browser window
        driver.switchTo().newWindow(WindowType.TAB);

        driver.get("https://www.youtube.com");
        softAssert.assertEquals(driver.getTitle(), "YouTube");
        String youTubeWindow= driver.getWindowHandle();
        System.out.println("YouTube Window Handle = " + youTubeWindow);

        // new tab in the same browser window
        driver.switchTo().newWindow(WindowType.TAB);

        driver.get("https://www.selenium.dev/");
        softAssert.assertEquals(driver.getTitle(), "Selenium");

        // new tab in the same browser window
        driver.switchTo().newWindow(WindowType.TAB);

        driver.get("https://www.facebook.com");
        softAssert.assertEquals(driver.getTitle(), "Facebook – log in or sign up");
        String facebookWindow= driver.getWindowHandle();
        System.out.println("Facebook Window Handle = " + facebookWindow);

        // new tab in the same browser window
        driver.switchTo().newWindow(WindowType.TAB);

        driver.get("https://twitter.com/");
        softAssert.assertEquals(driver.getTitle(), "Explore / Twitter");


        //print out all window handles
        Set<String> allWindows = driver.getWindowHandles();
        System.out.println(allWindows);
        System.out.println("TAB SIZE = " + allWindows.size());

        for (String handle1 : driver.getWindowHandles()) {
            System.out.println(handle1);
            driver.switchTo().window(handle1);
        }

        driver.switchTo().window(parentWindow);
        softAssert.assertEquals(driver.getTitle(), "Google");

        driver.switchTo().window(facebookWindow);
        softAssert.assertEquals(driver.getTitle(), "Facebook – log in or sign up");

        driver.switchTo().window(youTubeWindow);
        softAssert.assertEquals(driver.getTitle(), "YouTube");

        softAssert.assertAll();
    }

    @Test(priority = 2)
    public void test_open_new_window(){

        SoftAssert softAssert = new SoftAssert();

        driver.get("https://www.google.com");
        softAssert.assertEquals(driver.getTitle(), "Google");

        // new browser window
        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get("https://www.youtube.com");
        softAssert.assertEquals(driver.getTitle(), "YouTube");

        // new browser window
        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get("https://www.gmail.com");
        softAssert.assertEquals(driver.getTitle(), "Gmail: Private and secure email at no cost | Google Workspace");

        // new browser window
        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get("https://www.facebook.com");
        softAssert.assertEquals(driver.getTitle(), "Facebook – log in or sign up");

        // new tab in the same browser window
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://twitter.com/");
        softAssert.assertEquals(driver.getTitle(), "Explore / Twitter");

        softAssert.assertAll();
    }


    @AfterClass
    public void tearDown () {
        driver.quit();
    }
}
