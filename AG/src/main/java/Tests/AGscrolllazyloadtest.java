package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class AGscrolllazyloadtest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/adventure/";

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
    public void testScrollAndLazyLoad() {
        test = extent.createTest("Scroll & Lazy Load Check - Homepage");

        try {
            driver.get(url);
            Thread.sleep(2000);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
            int scrollCount = 0;

            while (true) {
                scrollCount++;
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                test.info("Scrolled to bottom - attempt #" + scrollCount);
                Thread.sleep(2000);

                long newHeight = (long) js.executeScript("return document.body.scrollHeight");

                if (newHeight == lastHeight) {
                    test.pass("No more content to load. Final scroll height: " + newHeight);
                    break;
                }
                lastHeight = newHeight;
            }

        } catch (Exception e) {
            test.fail("Scroll test failed: " + e.getMessage());
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
