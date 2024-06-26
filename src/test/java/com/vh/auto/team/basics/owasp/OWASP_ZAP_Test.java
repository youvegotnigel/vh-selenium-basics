package com.vh.auto.team.basics.owasp;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

public class OWASP_ZAP_Test {

    static final String ZAP_PROXY_ADDRESS = "localhost";
    static final int ZAP_PROXY_PORT = 1010;
    static final String ZAP_API_KEY = "2esc81e5kaalo5n7g8dntva1r1";
    private static final String ZAP_FILE_PATH = "C:\\Program Files\\OWASP\\Zed Attack Proxy";
    WebDriver driver;
    ClientApi api;


    @BeforeMethod
    public void setup() throws ClientApiException {
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
        if (driver != null) {
            driver.quit();
        }
        //api.core.shutdown();
        //api.core.deleteSiteNode();
    }

    @Test
    public void scanJuiceShop() {
        driver.get("https://juice-shop.herokuapp.com/#/");
        Assert.assertTrue(driver.getTitle().contains("Juice"));
        generateZAPReport("This is to scan Juice SHOP site", "juice-shop-scan");
    }


    @Test
    public void scanDialog() {
        driver.get("https://www.dialog.lk/");
        Assert.assertTrue(driver.getTitle().contains("Dialog"));
        generateZAPReport("This is to scan dialog site", "dialog-scan");
    }

    @Test
    public void scanSLT() {
        driver.get("https://www.slt.lk/");
        Assert.assertTrue(driver.getTitle().contains("SLTMobitel"));
        generateZAPReport("This is to scan SLT site", "slt-scan");
    }

    @Test
    public void scanRooter() {
        driver.get("https://rooter.lk/");
        Assert.assertTrue(driver.getTitle().contains("Apple"));
        generateZAPReport("This is to scan rooter site", "rooter-scan");
    }

    private void generateZAPReport(String description, String fileName){
        if (api != null) {
            String title = "My ZAP report";
            String template = "high-level-report";
            //String description = "This is a sample report";
            String reportFileName = String.format("%s-zap-report.html", fileName);
            String targetFolder = new File("").getAbsolutePath();
            ApiResponse response = null;
            try {
                response = api.reports.generate(title, template, null,
                        description, null, null, null, null, null, reportFileName,
                        null, targetFolder, null);
            } catch (ClientApiException e) {
                throw new RuntimeException(e);
            }
            System.out.println("ZAP report generated at: " + response.toString());

        }
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
