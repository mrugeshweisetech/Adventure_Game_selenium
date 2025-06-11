package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGabtesting {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/adventure/";

    @BeforeClass
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ABTestingReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void detectABTest() {
        test = extent.createTest("A/B Testing Detection - Homepage Content Comparison");

        try {
            driver.get(url);
            Thread.sleep(3000);
            List<WebElement> firstLoadArticles = driver.findElements(By.cssSelector(".views-row .field-content"));
            StringBuilder firstLoadText = new StringBuilder();
            for (WebElement el : firstLoadArticles) {
                firstLoadText.append(el.getText().trim()).append("||");
            }
            String snapshotA = firstLoadText.toString();

            driver.quit();
            driver = new ChromeDriver();
            driver.manage().window().maximize();

            driver.get(url);
            Thread.sleep(3000);
            List<WebElement> secondLoadArticles = driver.findElements(By.cssSelector(".views-row .field-content"));
            StringBuilder secondLoadText = new StringBuilder();
            for (WebElement el : secondLoadArticles) {
                secondLoadText.append(el.getText().trim()).append("||");
            }
            String snapshotB = secondLoadText.toString();

            if (snapshotA.equals(snapshotB)) {
                test.pass(" No A/B test variation detected. Homepage content consistent across sessions.");
            } else {
                test.warning("Possible A/B testing variation detected! Content differs between sessions.");
                test.info("Snapshot A: " + snapshotA.substring(0, Math.min(snapshotA.length(), 300)) + "...");
                test.info("Snapshot B: " + snapshotB.substring(0, Math.min(snapshotB.length(), 300)) + "...");
            }

        } catch (Exception e) {
            test.fail("Exception during A/B testing detection: " + e.getMessage());
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
