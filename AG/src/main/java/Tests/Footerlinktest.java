package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class Footerlinktest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String baseUrl = "https://development:development@weisetech.dev/adventuregamers/";

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
    public void clickAllFooterLinks() {
        test = extent.createTest("Footer Menu Link Test");

        try {
            driver.get(baseUrl);
            Thread.sleep(2000);

            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1500);

            List<WebElement> footerLinks = driver.findElements(By.cssSelector("ul#menu-footer-menu li a"));

            int total = footerLinks.size();
            test.info("Total footer links found: " + total);
            System.out.println("Found footer links: " + total);

            for (int i = 0; i < total; i++) {
                try {

                    driver.get(baseUrl);
                    Thread.sleep(2000);
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep(1500);

                    footerLinks = driver.findElements(By.cssSelector("ul#menu-footer-menu li a"));
                    WebElement link = footerLinks.get(i);

                    String linkText = link.getText().trim();
                    String href = link.getAttribute("href");

                    if (href != null && !href.trim().isEmpty()) {
                        link.click();
                        Thread.sleep(3000);

                        test.pass(" Clicked footer link: '" + linkText + "' | Title: " + driver.getTitle());
                        driver.navigate().back();
                        Thread.sleep(2000);
                    } else {
                        test.warning(" Skipped link with no href at index " + i);
                    }
                } catch (Exception e) {
                    test.fail("Error clicking footer link " + i + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            test.fail("Exception during footer test setup: " + e.getMessage());
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
