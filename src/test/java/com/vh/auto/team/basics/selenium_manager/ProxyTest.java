package com.vh.auto.team.basics.selenium_manager;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class ProxyTest {

    private WebDriver driver;

    @Test
    public void noProxyTest() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBrowserVersion("stable");
        driver = new ChromeDriver(chromeOptions);
        driver.get("https://mylocation.org/");
        driver.manage().window().maximize();
        String ipinfo = findElement(By.xpath("(//table)[1]")).getText().trim();
        System.out.println("\n" + ipinfo);
        driver.quit();
        System.out.println("-----------------------------------------------------------------------------------\n");
    }

    @Test
    public void proxyTest() {

        Proxy proxy = new Proxy();
        //proxy.setAutodetect(false);
        //proxy.setHttpProxy("34.82.224.175:33333");
        proxy.setSslProxy("51.161.208.144:3128"); // https://www.sslproxies.org/

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBrowserVersion("stable");
        chromeOptions.setCapability("proxy", proxy);

        driver = new ChromeDriver(chromeOptions);
        driver.get("https://mylocation.org/");
        driver.manage().window().maximize();
        String ipinfo = findElement(By.xpath("(//table)[1]")).getText().trim();
        System.out.println("\n" + ipinfo);
        driver.quit();
        System.out.println("-----------------------------------------------------------------------------------\n");
    }


    public WebElement findElement(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }
}
