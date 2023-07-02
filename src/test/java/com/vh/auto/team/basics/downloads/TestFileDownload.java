package com.vh.auto.team.basics.downloads;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFileDownload {

    public enum BrowserType {
        CHROME,
        CHROME_HEADLESS,
        FIREFOX,
        FIREFOX_HEADLESS,
        EDGE
    }

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final String PDF_URL = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf";
    public static final String FILE_DOWNLOAD_DIRECTORY = System.getProperty("user.dir") + "\\downloads";
    private static final String REMOTE_URL = "http://localhost:4444";


    @BeforeTest
    public void setup() throws MalformedURLException, InterruptedException {

        BrowserType browserType = BrowserType.CHROME;
        driver.set(new RemoteWebDriver(new URL(REMOTE_URL), getDesiredCapabilities(browserType)));
        getDriver().navigate().to(PDF_URL);
        Thread.sleep(5000); // wait for file download
    }

    @Test
    public void read_downloaded_pdf_file_test() throws IOException {

        File file = new File(FILE_DOWNLOAD_DIRECTORY + "\\pdf-sample.pdf");

        PDDocument pdfDocument = PDDocument.load(file);
        String pdfContent = new PDFTextStripper().getText(pdfDocument);

        System.out.println("Total number of pages :: " + pdfDocument.getNumberOfPages());
        System.out.println("PDF Content :: \n" + pdfContent);
    }

    private static DesiredCapabilities getDesiredCapabilities(BrowserType browserType) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setPlatform(Platform.ANY);

        switch (browserType) {
            case FIREFOX:
                capabilities.setBrowserName("firefox");
                capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, createFirefoxOptions());
                break;
            case FIREFOX_HEADLESS:
                capabilities.setBrowserName("firefox");
                capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, createFirefoxHeadlessOptions());
                break;
            case CHROME:
                capabilities.setBrowserName("chrome");
                capabilities.setCapability(ChromeOptions.CAPABILITY, createChromeOptions());
                break;
            case CHROME_HEADLESS:
                capabilities.setBrowserName("chrome");
                capabilities.setCapability(ChromeOptions.CAPABILITY, createChromeHeadlessOptions());
                break;
            case EDGE:
                capabilities.setBrowserName("MicrosoftEdge");
                capabilities.setCapability(EdgeOptions.CAPABILITY, createEdgeOptions());
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid BROWSER_TYPE = %s specified in the conf file", browserType));
        }
        return capabilities;
    }

    private static FirefoxOptions createFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        FirefoxProfile profile = createFirefoxProfile();
        options.setProfile(profile);
        options.addArguments("--start-maximized");
        return options;
    }

    private static FirefoxOptions createFirefoxHeadlessOptions() {
        FirefoxOptions options = new FirefoxOptions();
        FirefoxProfile profile = createFirefoxProfile();
        options.setProfile(profile);
        options.addArguments("-headless");
        options.addArguments("--start-maximized");
        return options;
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("prefs", createChromePreferences());
        return options;
    }

    private static ChromeOptions createChromeHeadlessOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--start-maximized");
        options.setExperimentalOption("prefs", createChromePreferences());
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        return options;
    }

    private static EdgeOptions createEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("prefs", createEdgePreferences());
        return options;
    }

    private static FirefoxProfile createFirefoxProfile() {
        FirefoxProfile profile = new FirefoxProfile();
        // Set additional Firefox profile settings if needed
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", FILE_DOWNLOAD_DIRECTORY);
        profile.setPreference("pdfjs.disabled", true);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv,application/pdf,application/csv,application/vnd.ms-excel");
        return profile;
    }

    private static Map<String, Object> createChromePreferences() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", FILE_DOWNLOAD_DIRECTORY);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.open_pdf_in_system_reader", false);
        prefs.put("plugins.always_open_pdf_externally", true);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        return prefs;
    }

    private static Map<String, Object> createEdgePreferences() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", FILE_DOWNLOAD_DIRECTORY);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.open_pdf_in_system_reader", false);
        prefs.put("plugins.always_open_pdf_externally", true);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        return prefs;
    }

    private static WebDriver getDriver() {
        return driver.get();
    }

    @AfterTest
    public void teardown() {
        getDriver().quit();
        driver.remove();
    }

    private void clearDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        // Check if the directory exists
        if (!directory.exists()) {
            System.err.println("Directory does not exist: " + directoryPath);
            return;
        }

        // Check if it's a directory
        if (!directory.isDirectory()) {
            System.err.println("Path is not a directory: " + directoryPath);
            return;
        }

        // Get all the files in the directory
        File[] files = directory.listFiles();

        // Loop through the files and delete them
        for (File file : files) {
            if (file.isDirectory()) {
                // If it's a directory, call this method recursively
                clearDirectory(file.getAbsolutePath());
            } else {
                // Otherwise, delete the file
                file.delete();
            }
        }
    }






}

