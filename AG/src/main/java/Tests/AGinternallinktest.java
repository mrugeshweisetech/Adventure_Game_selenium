package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AGinternallinktest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String baseUrl = "https://development:development@weisetech.dev/adventuregamers/article-type/reviews";
    String domainPrefix = "https://weisetech.dev";

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
    public void checkInternalLinksStatus() {
        test = extent.createTest("Internal Link Validation Test - PC Category");

        try {
            driver.get(baseUrl);
            Thread.sleep(2000);

            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            Set<String> internalLinks = new HashSet<>();

            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");
                if (href != null && href.startsWith(domainPrefix)) {
                    internalLinks.add(href.trim());
                }
            }

            test.info("Total internal links found: " + internalLinks.size());
            System.out.println("Checking " + internalLinks.size() + " internal links...");

            for (String url : internalLinks) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        test.pass( url + " is valid (200 OK)");
                    } else {
                        test.fail(url + " returned HTTP " + responseCode);
                    }

                } catch (Exception e) {
                    test.fail("Exception for URL: " + url + " - " + e.getMessage());
                }
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
