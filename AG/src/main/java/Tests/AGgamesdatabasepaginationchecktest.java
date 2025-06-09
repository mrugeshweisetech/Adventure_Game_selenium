package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGgamesdatabasepaginationchecktest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String baseUrl = "https://development:development@weisetech.dev/adventuregamers/adventure/all";

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
    public void clickEachPaginationLink() {
        test = extent.createTest("Pagination Click Test - Daily Deals");

        try {
            driver.get(baseUrl);
            Thread.sleep(2000);

            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1500);

            List<WebElement> paginationLinks = driver.findElements(By.cssSelector("cat-pagination li a"));

            int totalPages = paginationLinks.size();
            test.info("Total pagination links found: " + totalPages);

            for (int i = 0; i < totalPages; i++) {
                try {
                    driver.get(baseUrl);
                    Thread.sleep(2000);
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep(1000);

                    paginationLinks = driver.findElements(By.cssSelector("cat-pagination li a"));
                    WebElement pageLink = paginationLinks.get(i);
                    String pageText = pageLink.getText().trim();

                    pageLink.click();
                    Thread.sleep(2500);

                    String title = driver.getTitle();
                    String currentUrl = driver.getCurrentUrl();

                    test.pass("Clicked Page " + pageText + " | URL: " + currentUrl + " | Title: " + title);

                } catch (Exception e) {
                    test.fail("Failed to click pagination link " + (i + 1) + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            test.fail("Pagination test failed: " + e.getMessage());
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
