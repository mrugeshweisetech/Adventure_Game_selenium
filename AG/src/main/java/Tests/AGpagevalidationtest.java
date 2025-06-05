package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class AGpagevalidationtest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/article-type/reviews";

    @BeforeClass
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    public void launchBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void validatePaginationWorks() {
        test = extent.createTest("Pagination Validation Test - PC Games");

        try {
            driver.get(url);
            Thread.sleep(2000);

            List<WebElement> cardsPage1 = driver.findElements(By.cssSelector(".forum_threads"));
            int countPage1 = cardsPage1.size();
            test.info("Cards on Page 1: " + countPage1);

            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1500);

            WebElement page2Link = driver.findElement(By.linkText("2"));
            page2Link.click();
            Thread.sleep(2500);

            List<WebElement> cardsPage2 = driver.findElements(By.cssSelector(".forum_threads"));
            int countPage2 = cardsPage2.size();
            test.info("Cards on Page 2: " + countPage2);


            if (countPage2 > 0 && countPage2 != countPage1) {
                test.pass("Pagination works. Page 2 loaded with " + countPage2 + " new cards.");
            } else {
                test.fail("Pagination did not load new content properly.");
            }

            Assert.assertTrue(countPage2 > 0, "Page 2 has no cards!");
            Assert.assertNotEquals(countPage2, countPage1, "Page 2 content matches Page 1. Pagination may not be working!");

        } catch (Exception e) {
            test.fail("Exception during pagination test: " + e.getMessage());
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
