package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGpaginationtest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String baseUrl = "https://development:development@weisetech.dev/adventuregamers/article-type/reviews";

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
    public void clickAllPaginationPages() {
        test = extent.createTest("Full Pagination Test - PC Category");

        try {
            driver.get(baseUrl);
            Thread.sleep(2000);

            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1500);

            List<WebElement> paginationLinks = driver.findElements(By.cssSelector("ul.page-numbers li a"));

            int totalPages = paginationLinks.size();
            test.info("Total pagination links found: " + totalPages);

            for (int i = 1; i <= totalPages; i++) {
                try {
                    driver.get(baseUrl);
                    Thread.sleep(2000);

                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep(1500);

                    paginationLinks = driver.findElements(By.cssSelector("ul.page-numbers li a"));
                    WebElement pageLink = paginationLinks.get(i - 1);
                    String pageNum = pageLink.getText().trim();
                    pageLink.click();
                    Thread.sleep(2500);

                    String currentUrl = driver.getCurrentUrl();
                    String pageTitle = driver.getTitle();
                    test.pass("Opened Page " + pageNum + " | URL: " + currentUrl + " | Title: " + pageTitle);
                } catch (Exception e) {
                    test.fail("Failed to open page " + i + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            test.fail("Pagination test error: " + e.getMessage());
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
