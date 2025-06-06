package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGwalkthroughclicklink {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/walkthrough";

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
    public void clickAllWalkthroughLinks() {
        test = extent.createTest("Walkthrough Page - Link Click Test");

        try {
            driver.get(url);
            Thread.sleep(2000);

            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            int total = allLinks.size();
            test.info("Total links found: " + total);
            System.out.println("Found total links: " + total);

            for (int i = 0; i < total; i++) {
                try {
                    driver.get(url);
                    Thread.sleep(2000);

                    allLinks = driver.findElements(By.tagName("a"));
                    WebElement link = allLinks.get(i);
                    String linkText = link.getText().trim();
                    String href = link.getAttribute("href");

                    if (href != null && href.startsWith("https://weisetech.dev")) {
                        link.click();
                        Thread.sleep(3000);

                        String currentTitle = driver.getTitle();
                        String currentUrl = driver.getCurrentUrl();
                        test.pass("Clicked link: '" + linkText + "' | URL: " + currentUrl + " | Title: " + currentTitle);

                        driver.navigate().back();
                        Thread.sleep(2000);
                    } else {
                        test.warning("Skipped invalid or external link: " + href);
                    }
                } catch (Exception e) {
                    test.fail("Failed to click link at index " + i + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            test.fail("Exception during walkthrough link test: " + e.getMessage());
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
