package com.vh.auto.team.basics.facker_data;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Locale;

import static net.andreinc.mockneat.types.enums.NameType.*;
import static net.andreinc.mockneat.unit.address.Countries.countries;
import static net.andreinc.mockneat.unit.financial.Money.money;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.user.Emails.emails;
import static net.andreinc.mockneat.unit.user.Names.names;


public class BankFormTest {

    private WebDriver driver;

    private static final By INPUT_NAME = By.id("name");
    private static final By SUMMARY_NAME = By.id("summary-name");
    private static String _name;

    private static final By INPUT_AGE = By.id("age");
    private static final By SUMMARY_AGE = By.id("summary-age");
    private static int _age;

    private static final By INPUT_DOB = By.id("dob");
    private static String _dob;

    private static final By INPUT_ADDRESS = By.id("address");
    private static String _address;

    private static final By INPUT_MONTHLY_SALARY = By.id("salary");
    private static String _monthly_salary;

    private static final By SELECT_ACCOUNT_TYPE = By.id("account-type");
    private static String _account_type;

    private static final By INPUT_OCCUPATION = By.id("occupation");
    private static String _occupation;

    private static final By INPUT_EMAIL = By.id("email");
    private static String _email;

    private static final By INPUT_PHONE_NUMBER = By.id("phone");
    private static String _phone;

    private static final By INPUT_CITIZENSHIP = By.id("citizenship");
    private static String _citizenship;

    private static final By BUTTON_SUBMIT = By.xpath("//input[@type='submit']");
    private static final By TABLE_SUMMARY = By.cssSelector("table.summary-table");

    @BeforeClass
    public void setup() {

        driver = new ChromeDriver();

        String filePath = System.getProperty("user.dir") + "/pages/bank-form.html";
        driver.get("file://" + filePath);

        driver.manage().window().maximize();
        generateTestData();
    }


    private void generateTestData(){

        _name = names().type(FIRST_NAME_FEMALE).get() + " " + names().type(LAST_NAME).get();
        _age = ints().range(10, 100).get();

        LocalDate min = LocalDate.of(1987, 1, 30);
        _dob = String.valueOf(localDates().past(min).get());

        _address = "106, 7th Floor, Red Cross Building, Srimath Anagarika Dharmapala Mawatha, 00700";
        _monthly_salary = money().locale(Locale.CHINA).range(2000, 5000).get();
        _account_type = "savings";
        _occupation = "Software Engineer";
        _email = emails().get();
        _phone = "011-123-4567";
        _citizenship = "From " + countries().names().get();
    }

    @Test
    public void formTest(){

        explicitWaitMethod(INPUT_NAME);
        driver.findElement(INPUT_NAME).sendKeys(_name);

        driver.findElement(INPUT_AGE).sendKeys(String.valueOf(_age));
        driver.findElement(INPUT_DOB).sendKeys(_dob);
        driver.findElement(INPUT_ADDRESS).sendKeys(_address);
        driver.findElement(INPUT_MONTHLY_SALARY).sendKeys(_monthly_salary);

        Select select = new Select(driver.findElement(SELECT_ACCOUNT_TYPE));
        select.selectByValue(_account_type);

        driver.findElement(INPUT_OCCUPATION).sendKeys(_occupation);
        driver.findElement(INPUT_EMAIL).sendKeys(_email);
        driver.findElement(INPUT_PHONE_NUMBER).sendKeys(_phone);
        driver.findElement(INPUT_CITIZENSHIP).sendKeys(_citizenship);

        driver.findElement(BUTTON_SUBMIT).submit();
    }


    @Test
    public void verifyData(){

        explicitWaitMethod(TABLE_SUMMARY);
        Assert.assertEquals(driver.findElement(SUMMARY_NAME).getText().trim(), _name);
        Assert.assertEquals(driver.findElement(SUMMARY_AGE).getText().trim(), _age);

        //Implement here for rest of the assertions in the summary table


    }


    public void explicitWaitMethod(By by) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));

        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(by));
        actions.perform();
    }

    //@AfterClass
    public void tearDown() {
        driver.quit();
    }

}
