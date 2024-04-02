package com.vh.auto.team.basics.selenium_grid;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.By;
import org.openqa.selenium.io.Zip;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.remote.http.Contents.string;
import static org.openqa.selenium.remote.http.Contents.asJson;
import static org.openqa.selenium.remote.http.HttpMethod.GET;
import static org.openqa.selenium.remote.http.HttpMethod.POST;

public class FileDownloadTest {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final String REMOTE_URL = "http://10.127.91.203:4444";
    private static final String BROWSER = "chrome"; //MicrosoftEdge, chrome, firefox
    private static final String BROWSER_VERSION = "118.0";


    @BeforeTest
    private void setup() {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setPlatform(Platform.LINUX);
        capabilities.setBrowserName(BROWSER);
        capabilities.setVersion(BROWSER_VERSION);

        switch (BROWSER) {

            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                //chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.setCapability("se:downloadsEnabled", true);
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                //firefoxOptions.addArguments("-headless");
                firefoxOptions.addArguments("--start-maximized");
                capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                break;

            case "MicrosoftEdge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                capabilities.setCapability(EdgeOptions.CAPABILITY, edgeOptions);
                break;
        }

        // Create a RemoteWebDriver instance with the Selenium Grid URL and desired capabilities
        try {
            driver.set(new RemoteWebDriver(new URL(REMOTE_URL), capabilities));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Navigate to the login page
        getDriver().get("https://the-internet.herokuapp.com/download");
        getDriver().manage().window().maximize();

        // getDriver().manage().window().setSize(new Dimension(1440, 900));
    }


    private static WebDriver getDriver() {
        return driver.get();
    }

    @AfterTest
    private void tearDown() {
        getDriver().quit();
        driver.remove();
    }


    @Test
    public void fileDownloadTest() throws InterruptedException {

        TimeUnit.SECONDS.sleep(3);

        // Download the two available files on the page
        getDriver().findElement(By.linkText("code.txt")).click();
        //getDriver().findElement(By.id("file-2")).click();

        // Wait for file to download
        TimeUnit.SECONDS.sleep(15);

        //This is the endpoint which will provide us with list of files to download and also to
        //let us download a specific file.
        String downloadsEndpoint = String.format("/session/%s/se/files", ((RemoteWebDriver) getDriver()).getSessionId());
        System.out.println("FILE DOWNLOAD END POINT ::: " + downloadsEndpoint);


        String fileToDownload;

        try (HttpClient client = HttpClient.Factory.createDefault().createClient(new URL(REMOTE_URL))) {
            // To list all files that are were downloaded on the remote node for the current session
            // we trigger GET request.
            HttpRequest request = new HttpRequest(GET, downloadsEndpoint);
            HttpResponse response = client.execute(request);
            Map<String, Object> jsonResponse = new Json().toType(string(response), Json.MAP_TYPE);
            @SuppressWarnings("unchecked")
            Map<String, Object> value = (Map<String, Object>) jsonResponse.get("value");
            @SuppressWarnings("unchecked")
            List<String> names = (List<String>) value.get("names");
            // Let's say there were "n" files downloaded for the current session, we would like
            // to retrieve ONLY the first file.
            fileToDownload = names.get(0);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }


        // Now, let's download the file
        try (HttpClient client = HttpClient.Factory.createDefault().createClient(new URL(REMOTE_URL))) {
            // To retrieve a specific file from one or more files that were downloaded by the current session
            // on a remote node, we use a POST request.
            HttpRequest request = new HttpRequest(POST, downloadsEndpoint);
            request.setContent(asJson(ImmutableMap.of("name", fileToDownload)));
            HttpResponse response = client.execute(request);
            Map<String, Object> jsonResponse = new Json().toType(string(response), Json.MAP_TYPE);
            @SuppressWarnings("unchecked")
            Map<String, Object> value = (Map<String, Object>) jsonResponse.get("value");
            // The returned map would contain 2 keys,
            // filename - This represents the name of the file (same as what was provided by the test)
            // contents - Base64 encoded String which contains the zipped file.
            String zippedContents = value.get("contents").toString();

            String zippedFileNames = value.get("filename").toString();
            System.out.println("zippedFileNames ::: " + zippedFileNames);
            // The file contents would always be a zip file and has to be unzipped.
            File downloadDir = Zip.unzipToTempDir(zippedContents, "download", "");
            // Read the file contents
            File downloadedFile = Optional.ofNullable(downloadDir.listFiles()).orElse(new File[]{})[0];
            String fileContent = String.join("", Files.readAllLines(downloadedFile.toPath()));
            System.out.println("The file which was "
                    + "downloaded in the node is now available in the directory: "
                    + downloadDir.getAbsolutePath() + " and has the contents: " + fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
