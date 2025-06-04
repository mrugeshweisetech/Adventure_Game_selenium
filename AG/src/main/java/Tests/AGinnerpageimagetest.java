package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGinnerpageimagetest {

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
    public void countImagesInInnerContent() {
        test = extent.createTest("Image Count Test");

        try {
            driver.get(url);
            Thread.sleep(2000);

            WebElement innerContent = driver.findElement(By.cssSelector("div#inner")); // adjust if needed
            List<WebElement> images = innerContent.findElements(By.tagName("img"));
            int count = images.size();

            System.out.println("Image count: " + count);
            test.pass("Total images in inner content: " + count);

        } catch (Exception e) {
            test.fail("Exception: " + e.getMessage());
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
