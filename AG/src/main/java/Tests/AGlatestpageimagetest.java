package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGlatestpageimagetest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/latest-news";

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
    public void validateAllImagesLoaded() {
        test = extent.createTest("Visual Image Content Test - Latest News");

        try {
            driver.get(url);
            Thread.sleep(2000);

            List<WebElement> images = driver.findElements(By.tagName("img"));
            test.info("Total images found: " + images.size());

            int brokenCount = 0;

            for (WebElement img : images) {
                try {
                    String src = img.getAttribute("src");
                    if (src == null || src.trim().isEmpty()) {
                        test.warning("Image with missing or empty src attribute.");
                        brokenCount++;
                        continue;
                    }

                    Boolean isBroken = (Boolean) ((JavascriptExecutor) driver)
                            .executeScript("return arguments[0].naturalWidth == 0", img);

                    if (isBroken) {
                        test.fail("Broken image: " + src);
                        brokenCount++;
                    } else {
                        test.pass("Image loaded: " + src);
                    }
                } catch (Exception e) {
                    test.fail("Exception while checking image: " + e.getMessage());
                    brokenCount++;
                }
            }

            if (brokenCount == 0) {
                test.pass("All images loaded correctly.");
            } else {
                test.warning("Total broken images found: " + brokenCount);
            }

        } catch (Exception e) {
            test.fail("Test failed due to: " + e.getMessage());
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
