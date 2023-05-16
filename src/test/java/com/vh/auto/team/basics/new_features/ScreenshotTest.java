package com.vh.auto.team.basics.new_features;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class ScreenshotTest {


    private ChromeDriver driver;
    private By element_image = By.cssSelector("#hmenu-canvas");

    @BeforeMethod
    public void setUp() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.amazon.com/");
    }

    @Test
    public void takeFullPageScreenshot_aShot() {

        //take screenshot of the entire page
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        try {
            ImageIO.write(screenshot.getImage(),"PNG", new File(".\\Amazon Full Page Screenshot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void takeElementScreenshot() throws IOException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element_image));

        WebElement webElement = driver.findElement(element_image);

        File screenshot = webElement.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("navigation-bar.png"));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

}
