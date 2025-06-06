package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class AGwalkthroughpagecounttest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/walkthrough";

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
    public void countWordsInWalkthroughPage() {
        test = extent.createTest("Word Count Test - Walkthrough Page");

        try {
            driver.get(url);
            Thread.sleep(2000);

            WebElement content = driver.findElement(By.cssSelector(".main-content"));

            String text = content.getText().trim();
            int wordCount = text.isEmpty() ? 0 : text.split("\\s+").length;

            System.out.println("Total words: " + wordCount);
            test.info("Total words on walkthrough page: " + wordCount);

            if (wordCount > 0) {
                test.pass("Word count successful. Content is present.");
            } else {
                test.fail("Word count failed. No content found.");
            }

        } catch (Exception e) {
            test.fail("Exception occurred during word count: " + e.getMessage());
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
