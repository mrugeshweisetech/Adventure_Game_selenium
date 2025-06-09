package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGtopgameslinksclicktest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String baseUrl = "https://development:development@weisetech.dev/adventuregamers/topgames";

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
    public void clickAllLinksOnDailyDealsPage() {
        test = extent.createTest("Click All Valid Links - Daily Deals");

        try {
            driver.get(baseUrl);
            Thread.sleep(2000);

            List<WebElement> links = driver.findElements(By.tagName("a"));
            test.info("Total links found: " + links.size());

            for (int i = 0; i < links.size(); i++) {
                try {
                    driver.get(baseUrl);
                    Thread.sleep(2000);

                    links = driver.findElements(By.tagName("a"));
                    WebElement link = links.get(i);
                    String linkText = link.getText().trim();
                    String href = link.getAttribute("href");

                    if (href == null || href.isEmpty() || href.startsWith("javascript")) {
                        test.warning("Skipped non-clickable link at index " + i);
                        continue;
                    }

                    link.click();
                    Thread.sleep(3000);

                    String title = driver.getTitle();
                    String currentUrl = driver.getCurrentUrl();

                    test.pass("Clicked link: '" + linkText + "' | URL: " + currentUrl + " | Title: " + title);
                    driver.navigate().back();
                    Thread.sleep( 2000 );
                } catch (Exception e) {
                    test.fail("Failed to click link at index " + i + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            test.fail("Exception during link click test: " + e.getMessage());
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
