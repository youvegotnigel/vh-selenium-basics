package com.vh.auto.team.basics.owasp;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

public class OWASP_ZAP_Test {

    static final String ZAP_PROXY_ADDRESS = "localhost";
    static final int ZAP_PROXY_PORT = 1010;
    static final String ZAP_API_KEY = "";
    WebDriver driver;
    ClientApi api;


    @BeforeTest
    public void setup() {
        String proxyStr = ZAP_PROXY_ADDRESS + ":" + ZAP_PROXY_PORT;
        Assert.assertTrue(isOnline("http://" + proxyStr));

        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyStr);
        proxy.setSslProxy(proxyStr);

        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--allowed-ips");
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setProxy(proxy);
        chromeOptions.setBrowserVersion("stable");
        driver = new ChromeDriver(chromeOptions);

        api = new ClientApi(ZAP_PROXY_ADDRESS, ZAP_PROXY_PORT, ZAP_API_KEY);

    }


    @AfterMethod
    public void tearDown() throws ClientApiException {
        if (api != null) {
            String title = "My ZAP report";
            String template = "modern";
            String description = "This is a sample report";
            String reportFileName = "zap-modern-report.html";
            String targetFolder = new File("").getAbsolutePath();
            ApiResponse response = api.reports.generate(title, template, null,
                    description, null, null, null, null, null, reportFileName,
                    null, targetFolder, null);
            System.out.println("ZAP report generated at: " + response.toString());
        }
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    public void testSecurity() {
        driver.get("https://www.dialog.lk/");
        Assert.assertTrue(driver.getTitle().contains("Dialog"));
    }


    private static boolean isOnline(String str) {
        try {
            URL url = new URL(str);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            return connection.getResponseCode() == 200;
        } catch (Exception var2) {
            return false;
        }
    }


}
