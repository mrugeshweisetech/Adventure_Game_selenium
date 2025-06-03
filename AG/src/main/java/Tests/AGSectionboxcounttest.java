package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AGSectionboxcounttest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    int expectedBoxCount = 9;

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
    public void validateSectionBoxCount() {
        test = extent.createTest("Validate Section Box Count");

        try {
            driver.get("https://development:development@weisetech.dev/adventuregamers/daily-deals/");
            Thread.sleep(2000);

            List<WebElement> boxes = driver.findElements(By.cssSelector(".forum_threads"));
            int actualCount = boxes.size();

            test.info("Expected box count: " + expectedBoxCount);
            test.info("Actual box count: " + actualCount);

            String ss = takeScreenshot("section_box_count");
            if (actualCount == expectedBoxCount) {
                test.pass("Box count matched: " + actualCount,
                        MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
            } else {
                test.fail("Box count mismatch! Expected: " + expectedBoxCount + ", Found: " + actualCount,
                        MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
            }

            Assert.assertEquals(actualCount, expectedBoxCount, "Box count validation failed!");

        } catch (Exception e) {
            String ss = takeScreenshot("section_box_count_error");
            test.fail("Exception: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
        }
    }

    public String takeScreenshot(String name) {
        try {
            File folder = new File("screenshots");
            if (!folder.exists()) folder.mkdir();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + name + "_" + timestamp + ".png";
            File dest = new File(path);
            FileUtils.copyFile(src, dest);
            return path;
        } catch (Exception e) {
            return null;
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
