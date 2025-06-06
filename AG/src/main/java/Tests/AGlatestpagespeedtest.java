package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class AGlatestpagespeedtest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/latest-news";
    long threshold = 3000;

    @BeforeClass
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void measurePageLoadPerformance() {
        test = extent.createTest("Page Load Performance Test - Latest News");

        try {
            long startTime = System.currentTimeMillis();

            driver.get(url);

            long endTime = System.currentTimeMillis();
            long loadTime = endTime - startTime;

            test.info("Page loaded in " + loadTime + " ms");
            System.out.println("Page load time: " + loadTime + " ms");

            if (loadTime <= threshold) {
                test.pass("Page loaded within acceptable time: " + loadTime + " ms");
            } else {
                test.fail("Page took too long to load: " + loadTime + " ms (Threshold: " + threshold + " ms)");
            }

        } catch (Exception e) {
            test.fail("Exception during performance test: " + e.getMessage());
        }
    }

    @AfterMethod
    public void closeBrowser() {
        if (driver != null) driver.quit();
    }

    @AfterClass
    public void flushReport() {
        extent.flush();
    }
}
