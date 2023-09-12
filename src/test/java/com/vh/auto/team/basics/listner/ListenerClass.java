package com.vh.auto.team.basics.listner;

import com.vh.auto.team.basics.sauce.SauceTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ListenerClass implements ITestListener {


    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("*** Test "+ result.getName() +" has started ***");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("*** Test "+ result.getName() +" has passed ***");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("*** Test "+ result.getName() +" has failed ***");
        takeScreenshot(result.getName());
    }

    private void takeScreenshot(String fileName){

        System.out.println("*** Taking a screenshot ***");
        try {
            TakesScreenshot screenshot = (TakesScreenshot) SauceTest.getDriver();
            File file = screenshot.getScreenshotAs(OutputType.FILE);

            // Create the "screenshot" folder if it doesn't exist
            Path screenshotFolderPath = Path.of("screenshots");
            if (!Files.exists(screenshotFolderPath)) {
                Files.createDirectory(screenshotFolderPath);
            }

            FileUtils.copyFile(file, new File(screenshotFolderPath.toFile(), fileName +  ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
