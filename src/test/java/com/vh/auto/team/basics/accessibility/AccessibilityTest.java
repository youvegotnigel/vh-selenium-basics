package com.vh.auto.team.basics.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.results.ResultType;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.deque.html.axecore.selenium.AxeReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.deque.html.axecore.selenium.AxeReporter.getReadableAxeResults;

public class AccessibilityTest {

    private WebDriver driver;

    @BeforeMethod
    public void setup() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBrowserVersion("stable");
        driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.cdc.gov/dengue/about/index.html");
        driver.manage().window().maximize();
    }


    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void checkAccessibility() {
        System.out.println("Starting Accessibility Test");

        AxeBuilder axeBuilder = new AxeBuilder();

        driver.get("https://www.cdc.gov/dengue/about/index.html");

        try {
            Results axeResults = axeBuilder.analyze(driver);
            System.out.println("Is site accessibility free ? ::: " + axeResults.violationFree());


            List<Rule> violations = axeResults.getViolations();
            if(violations.isEmpty()) {
                System.out.println("No Violation Found");
                return;
            }

            String AxeReportPath = System.getProperty("user.dir") + File.separator;
            String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            String AxeViolationReportPath = AxeReportPath + "AccessibilityViolations_" + timeStamp;
            AxeReporter.writeResultsToJsonFile(AxeViolationReportPath, axeResults);

            System.out.println("Violations Report Path ::: "+ AxeViolationReportPath);

            if(getReadableAxeResults(ResultType.Violations.getKey(), driver, violations)) {
                AxeReporter.writeResultsToJsonFile(AxeViolationReportPath, axeResults);
            }

        } catch (RuntimeException e) {
            // Do something with the error
            e.printStackTrace();
        }



        System.out.println("Accessibility Test is Completed");
    }
}
