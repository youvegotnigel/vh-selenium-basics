package com.vh.auto.team.basics.downloads;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TestFileDownload {

    public enum BrowserType {
        CHROME,
        CHROME_HEADLESS,
        FIREFOX,
        FIREFOX_HEADLESS,
        EDGE,
        EDGE_HEADLESS,
    }



    private WebDriver driver;
    private static final String LOGIN_URL = "http://mcap_auto.vh.local/";
    private static final String FILE_DOWNLOAD_PATH = "C:\\mcap_downloads";
    private By username = By.cssSelector("#login_username");
    private By password = By.cssSelector("#login_password");
    private By login_btn = By.cssSelector(".submit.login");


    @BeforeClass
    public void setup(){

        BrowserType browserType = BrowserType.CHROME_HEADLESS;
        Map<String, Object> prefs = new HashMap<>();

        switch (browserType){

            case CHROME:
                WebDriverManager.chromedriver().setup();
                prefs.put("download.default_directory", FILE_DOWNLOAD_PATH);
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.open_pdf_in_system_reader", false);
                prefs.put("plugins.always_open_pdf_externally", true);

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--plugins-disabled");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.setExperimentalOption("prefs", prefs);

                driver = new ChromeDriver(chromeOptions);
                break;

            case CHROME_HEADLESS:
                WebDriverManager.chromedriver().setup();
                prefs.put("download.default_directory", FILE_DOWNLOAD_PATH);
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.open_pdf_in_system_reader", false);
                prefs.put("plugins.always_open_pdf_externally", true);

                ChromeOptions chromeHeadlessOptions = new ChromeOptions();
                chromeHeadlessOptions.addArguments("--remote-allow-origins=*");
                chromeHeadlessOptions.addArguments("--disable-extensions");
                chromeHeadlessOptions.addArguments("--disable-popup-blocking");
                chromeHeadlessOptions.addArguments("--disable-infobars");
                chromeHeadlessOptions.addArguments("--plugins-disabled");
                chromeHeadlessOptions.setExperimentalOption("prefs", prefs);
                chromeHeadlessOptions.addArguments("--headless=new");

                driver = new ChromeDriver(chromeHeadlessOptions);
                break;

            case EDGE:
                WebDriverManager.edgedriver().setup();
                prefs.put("download.default_directory", FILE_DOWNLOAD_PATH);
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.open_pdf_in_system_reader", false);
                prefs.put("plugins.always_open_pdf_externally", true);

                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-extensions");
                edgeOptions.addArguments("--disable-popup-blocking");
                edgeOptions.addArguments("--disable-infobars");
                edgeOptions.addArguments("--plugins-disabled");
                edgeOptions.setExperimentalOption("prefs", prefs);

                driver = new EdgeDriver(edgeOptions);
                break;

            case EDGE_HEADLESS:
                WebDriverManager.edgedriver().setup();
                prefs.put("download.default_directory", FILE_DOWNLOAD_PATH);
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.open_pdf_in_system_reader", false);
                prefs.put("plugins.always_open_pdf_externally", true);

                EdgeOptions edgeHeadlessOptions = new EdgeOptions();
                edgeHeadlessOptions.addArguments("--disable-extensions");
                edgeHeadlessOptions.addArguments("--disable-popup-blocking");
                edgeHeadlessOptions.addArguments("--disable-infobars");
                edgeHeadlessOptions.addArguments("--plugins-disabled");
                edgeHeadlessOptions.setExperimentalOption("prefs", prefs);
                edgeHeadlessOptions.addArguments("--headless=new");

                driver = new EdgeDriver(edgeHeadlessOptions);
                break;

            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("browser.download.folderList", 2);
                profile.setPreference("browser.download.dir", FILE_DOWNLOAD_PATH);
                profile.setPreference("pdfjs.disabled", true);
                profile.setPreference("plugin.disable_full_page_plugin_for_types", "application/pdf");
                profile.setPreference("plugin.always_open_pdf_externally", true);

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setProfile(profile);

                driver = new FirefoxDriver(firefoxOptions);
                break;

            case FIREFOX_HEADLESS:
                WebDriverManager.firefoxdriver().setup();
                FirefoxProfile headlessProfile = new FirefoxProfile();
                headlessProfile.setPreference("browser.download.folderList", 2);
                headlessProfile.setPreference("browser.download.dir", FILE_DOWNLOAD_PATH);
                headlessProfile.setPreference("pdfjs.disabled", true);
                headlessProfile.setPreference("plugin.disable_full_page_plugin_for_types", "application/pdf");
                headlessProfile.setPreference("plugin.always_open_pdf_externally", true);

                FirefoxOptions firefoxHeadlessOptions = new FirefoxOptions();
                firefoxHeadlessOptions.setProfile(headlessProfile);
                firefoxHeadlessOptions.addArguments("-headless");

                driver = new FirefoxDriver(firefoxHeadlessOptions);
                break;

            default:
                throw new IllegalArgumentException("Invalid browser type specified: " + browserType);

        }


        driver.get(LOGIN_URL);
        driver.manage().window().maximize();
        login();
    }

    public void login() {
        driver.findElement(username).sendKeys("Mali");
        driver.findElement(password).sendKeys("Mali@123");
        driver.findElement(login_btn).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        Assert.assertEquals(driver.getTitle(), "Patient Search");
    }

    public void explicitWaitMethod(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    public void wait_for_feedback() {
        By element = By.cssSelector(".g_feedback");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
    }

    @AfterClass
    public void teardown(){
        driver.quit();
        clearDirectory(FILE_DOWNLOAD_PATH);
    }


    @Test
    public void test_file_download(){

        click_on_link("Report",1);
        set_radio_button_for("List",1);
        set_radio_button_for("Inpatients",1);
        selectByValue("#report_type", "Inpatient Admission Reviews");
        selectByValue("#date", "Year to date");

        driver.findElement(By.xpath("//input[@name='add_filter']")).click();
        selectByNameAndValue("filter_by[0][field]", "hospital");
        selectByNameAndValue("filter_by[0][rule]", "=");
        selectByNameAndValue("filter_by[0][value]", "RV5 - SLP");

        click_on_span("Report",1);
        wait_for_feedback();

        click_on_link("Export to PDF",1);

        System.out.println("FILE IS DOWNLOADING...");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



        //Assertion
        File downloadFolder = new File(FILE_DOWNLOAD_PATH);
        File[] downloadedFiles = downloadFolder.listFiles();

        // Find the most recently downloaded file in the download folder
        File latestDownloadedFile = null;
        long lastModifiedTime = Long.MIN_VALUE;
        for (File file : downloadedFiles) {
            if (file.lastModified() > lastModifiedTime) {
                lastModifiedTime = file.lastModified();
                latestDownloadedFile = file;
            }
        }

        // Validate the file extension
        String extension = FilenameUtils.getExtension(latestDownloadedFile.getName());

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(extension, "pdf", "File extension is not matching with expected extension.");
        softAssert.assertAll();

        // Delete the downloaded file
        latestDownloadedFile.delete();

    }


    public void click_on_link(String name, int index){

        String xpath = "(//a[normalize-space()='"+name+"'])["+index+"]";
        explicitWaitMethod(By.xpath(xpath));
        driver.findElement(By.xpath(xpath)).click();
    }

    public void click_on_span(String name, int index){

        String xpath = "(//span[normalize-space()='"+name+"'])["+index+"]";
        explicitWaitMethod(By.xpath(xpath));
        driver.findElement(By.xpath(xpath)).click();
    }

    public void set_radio_button_for(String value, int index){

        String xpath = "(//input[@type='radio' and @value='"+value+"'])["+index+"]";
        explicitWaitMethod(By.xpath(xpath));
        driver.findElement(By.xpath(xpath)).click();
    }

    public void selectByValue(String by, String value) {
        By dropdown = By.cssSelector(by);
        explicitWaitMethod(dropdown);
        Select select = new Select(driver.findElement(dropdown));
        select.selectByValue(value);
    }

    public void selectByNameAndValue(String name, String value) {
        String xpath = "(//select[@name='"+name+"'])[1]";
        By dropdown = By.xpath(xpath);
        explicitWaitMethod(dropdown);
        Select select = new Select(driver.findElement(dropdown));
        select.selectByValue(value);
    }

    public void set_date_from_calendar(String year, String month, String date, int index) {

        By calendar = By.xpath("(//button[@class='ui-datepicker-trigger'])[" + index + "]");

        explicitWaitMethod(calendar);
        driver.findElement(calendar).click();

        selectByVisibleText(".ui-datepicker-year", year);
        selectByVisibleText(".ui-datepicker-Month", month);
        click_on_normalize_text(date);
    }

    public void selectByVisibleText(String by, String value) {
        By dropdown = By.cssSelector(by);
        explicitWaitMethod(dropdown);
        Select select = new Select(driver.findElement(dropdown));
        select.selectByVisibleText(value);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Assert.assertEquals(select.getFirstSelectedOption().getText(), value, "Invalid option selected");
    }

    public void click_on_normalize_text(String text) {
        String xpath = "(//*[normalize-space()='" + text + "'])[1]";
        By element = By.xpath(xpath);
        explicitWaitMethod(element);
        //scroll_to_element_js(element);
        driver.findElement(element).click();
        //System.out.println("Clicked on element by xpath ::: " + xpath);
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

