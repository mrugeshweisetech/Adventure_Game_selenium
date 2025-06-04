package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGtitleclickinnertest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String baseUrl = "https://development:development@weisetech.dev/adventuregamers/reviews";

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
    public void clickAllVisibleTitles() {
        test = extent.createTest("Title Click Test");

        try {
            driver.get(baseUrl);
            Thread.sleep(2000);

            List<WebElement> titleElements = driver.findElements(By.cssSelector("div[class='article-right'] h2 a"));

            int total = titleElements.size();
            test.info("Total clickable titles found: " + total);
            System.out.println("Total titles: " + total);

            for (int i = 0; i < total; i++) {
                try {
                    driver.get(baseUrl);
                    Thread.sleep(2000);

                    titleElements = driver.findElements(By.cssSelector("h2 a, .entry-title a"));
                    WebElement title = titleElements.get(i);
                    String titleText = title.getText().trim();
                    String href = title.getAttribute("href");

                    if (href != null && !href.trim().isEmpty()) {
                        title.click();
                        Thread.sleep(3000);
                        test.pass("Clicked title: " + titleText + " | Title: " + driver.getTitle());
                        driver.navigate().back();
                        Thread.sleep(2000);
                    } else {
                        test.warning("Skipped non-clickable title at index " + i);
                    }
                } catch (Exception e) {
                    test.fail("Failed to click title at index " + i + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            test.fail("Exception during title click test: " + e.getMessage());
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
