package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AGdailydealslinkclicktest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/daily-deals";

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
    public void printAllLinksWithoutClicking() {
        test = extent.createTest("Print All Links (No Click) - Daily Deals Page");

        try {
            driver.get(url);
            Thread.sleep(2000);

            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            Set<String> printedLinks = new HashSet<>();

            test.info("Total <a> tags found: " + allLinks.size());

            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");

                if (href != null && !href.trim().isEmpty() && !href.startsWith("javascript")) {
                    if (!printedLinks.contains(href)) {
                        System.out.println(href);
                        test.pass("Found link: " + href);
                        printedLinks.add(href);
                    }
                }
            }

            test.info("Total valid links printed: " + printedLinks.size());

        } catch (Exception e) {
            test.fail("Exception during link printing: " + e.getMessage());
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
