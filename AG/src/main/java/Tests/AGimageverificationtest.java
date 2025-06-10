package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGimageverificationtest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/adventure/";

    @BeforeClass
    public void setupReport() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void validateAllImagesLoaded() {
        test = extent.createTest("Image Load Validation - AdventureGamers Home");

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
                        test.warning("Image with empty or missing src");
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
                        System.out.println("" + src);
                    }

                } catch (Exception e) {
                    test.fail("Error checking image: " + e.getMessage());
                    brokenCount++;
                }
            }

            if (brokenCount == 0) {
                test.pass("All images loaded successfully.");
            } else {
                test.warning("Broken images found: " + brokenCount);
            }

        } catch (Exception e) {
            test.fail("Test error: " + e.getMessage());
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
