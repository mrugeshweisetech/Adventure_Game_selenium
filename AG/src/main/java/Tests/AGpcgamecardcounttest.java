package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.List;

public class AGpcgamecardcounttest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/categories/pc";

    int expectedCardCount = 9;

    @BeforeClass
    public void setupReport() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @BeforeMethod
    public void launchBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void countAndValidateGameCards() {
        test = extent.createTest("Count & Validate Game Cards on PC Category Page");

        try {
            driver.get(url);
            Thread.sleep(2000);


            List<WebElement> cards = driver.findElements(By.cssSelector(".forum_threads"));
            int actualCount = cards.size();

            test.info("Total game cards found: " + actualCount);
            System.out.println("Game cards found: " + actualCount);

            if (actualCount == expectedCardCount) {
                test.pass("Game card count matched: " + actualCount);
            } else {
                test.fail("Game card count mismatch. Expected: " + expectedCardCount + ", Found: " + actualCount);
            }

            Assert.assertEquals(actualCount, expectedCardCount, "Game card count validation failed!");

        } catch (Exception e) {
            test.fail("Exception occurred: " + e.getMessage());
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
