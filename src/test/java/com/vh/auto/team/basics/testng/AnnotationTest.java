package com.vh.auto.team.basics.testng;

import org.testng.annotations.*;

public class AnnotationTest {


    @BeforeSuite
    public void beforeSuite() {
        System.out.println("Executing beforeSuite");
        // Set up any necessary configurations or resources before the test suite runs
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("Executing beforeTest");
        // Set up any necessary configurations or resources before the test runs
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println("Executing beforeClass");
        // Set up any necessary configurations or resources before the test class runs
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("Executing beforeMethod");
        // Set up any necessary configurations or resources before each test method runs
    }

    @Test
    public void testMethod3() {
        System.out.println("Executing testMethod3");
        // Test logic for method 3
    }

    @Test
    public void testMethod1() {
        System.out.println("Executing testMethod1");
        // Test logic for method 1
    }

    @Test(enabled = false)
    public void testMethod2() {
        System.out.println("Executing testMethod2");
        // Test logic for method 2
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("Executing afterMethod");
        // Clean up resources or perform any necessary actions after each test method runs
    }

    @AfterClass
    public void afterClass() {
        System.out.println("Executing afterClass");
        // Clean up resources or perform any necessary actions after the test class runs
    }

    @AfterTest
    public void afterTest() {
        System.out.println("Executing afterTest");
        // Clean up resources or perform any necessary actions after the test runs
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("Executing afterSuite");
        // Clean up resources or perform any necessary actions after the test suite runs
    }
}
