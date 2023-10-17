package com.vh.auto.team.basics.bidi;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.events.DomMutationEvent;
import org.openqa.selenium.devtools.v116.log.Log;
import org.openqa.selenium.logging.HasLogEvents;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.devtools.events.CdpEventTypes.domMutation;

public class BiDiTest {

    private static ChromeDriver driver;

    @BeforeTest
    public void setup() {

        ChromeOptions co = new ChromeOptions();
        co.setBrowserVersion("stable");
        driver = new ChromeDriver(co);
        driver.manage().window().maximize();
    }


    @Test
    public void basicAuthTest() {

        Predicate<URI> uriPredicate = uri -> uri.getHost().contains("the-internet.herokuapp.com");

        ((HasAuthentication) driver).register(uriPredicate, UsernameAndPassword.of("admin", "admin"));
        driver.get("https://the-internet.herokuapp.com/basic_auth");

        Assert.assertEquals(driver.findElement(By.tagName("h3")).getText().trim(), "Basic Auth");
        Assert.assertEquals(driver.findElement(By.tagName("p")).getText().trim(), "Congratulations! You must have the proper credentials.");
    }


    @Test
    public void consoleLogsTest() {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Log.enable());
        devTools.addListener(Log.entryAdded(),
                logEntry -> {
                    System.out.println("log: "+logEntry.getText());
                    System.out.println("level: "+logEntry.getLevel());
                });
        driver.get("http://the-internet.herokuapp.com/broken_images");

    }


    @Test
    public void mutationTest() throws InterruptedException {

        AtomicReference<DomMutationEvent> seen = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        ((HasLogEvents) driver).onLogEvent(domMutation(mutation -> {
            seen.set(mutation);
            latch.countDown();
        }));

        driver.get("https://www.google.com");
        WebElement span = driver.findElement(By.cssSelector("span"));

        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('cheese', 'gouda');", span);

        Assert.assertTrue(latch.await(10, SECONDS));
        Assert.assertEquals(seen.get().getAttributeName(), "cheese");
        Assert.assertEquals(seen.get().getCurrentValue(), "gouda");
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
