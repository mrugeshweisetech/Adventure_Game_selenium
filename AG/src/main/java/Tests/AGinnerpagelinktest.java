package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.*;

public class AGinnerpagelinktest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String baseUrl = "https://development:development@weisetech.dev/adventuregamers/article/elroy-and-the-aliens/";

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
    public void clickAllLinksOnPage() {
        test = extent.createTest("Click All Links on Page");

        try {
            driver.get(baseUrl);
            Thread.sleep(2000);

            List<WebElement> allLinks = driver.findElements(By.tagName("a"));

            Set<String> hrefs = new LinkedHashSet<>();
            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.trim().isEmpty() && !href.startsWith("javascript")) {
                    hrefs.add(href.trim());
                }
            }

            test.info("Total unique clickable links: " + hrefs.size());
            System.out.println("Total clickable links: " + hrefs.size());

            int index = 1;
            for (String href : hrefs) {
                try {
                    driver.get(href);
                    Thread.sleep(3000);
                    test.pass("[" + index + "] Clicked: " + href + " | Title: " + driver.getTitle());
                } catch (Exception e) {
                    test.fail(" [" + index + "] Failed to open: " + href + " | Error: " + e.getMessage());
                }
                index++;
            }

        } catch (Exception e) {
            test.fail("Exception in link loop: " + e.getMessage());
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
