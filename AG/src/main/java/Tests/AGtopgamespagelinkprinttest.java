package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AGtopgamespagelinkprinttest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/topgames";

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
    public void printAllValidLinks() {
        test = extent.createTest("Print All Valid Links - Generic Page");

        try {
            driver.get(url);
            Thread.sleep(2000);

            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            Set<String> uniqueLinks = new HashSet<>();

            test.info("Total anchor tags found: " + allLinks.size());

            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");

                if (href != null && !href.trim().isEmpty() && !href.startsWith("javascript")) {
                    if (!uniqueLinks.contains(href)) {
                        System.out.println(href);
                        test.pass("Link found: " + href);
                        uniqueLinks.add(href);
                    }
                }
            }

            test.info("Total unique, valid links printed: " + uniqueLinks.size());

        } catch (Exception e) {
            test.fail("Exception during link extraction: " + e.getMessage());
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
