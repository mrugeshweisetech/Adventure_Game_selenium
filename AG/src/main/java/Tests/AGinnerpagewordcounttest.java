package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class AGinnerpagewordcounttest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/article/elroy-and-the-aliens/";

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
    public void countWordsInInnerContent() {
        test = extent.createTest("Word Count in Inner Article Test");

        try {
            driver.get(url);
            Thread.sleep(2000);

            WebElement content = driver.findElement(By.cssSelector("article-news-info"));
            String text = content.getText();

            int wordCount = text.trim().split("\\s+").length;

            System.out.println("Word count: " + wordCount);
            test.pass("Total words in inner article: " + wordCount);

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
