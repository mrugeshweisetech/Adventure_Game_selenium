package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGfootermenuprinttest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/";

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
    public void printFooterLinkNames() {
        test = extent.createTest("Print All Footer Link Names - Daily Deals");

        try {
            driver.get(url);
            Thread.sleep(2000);

            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1500);

            WebElement footer = driver.findElement(By.className("Footer"));

            List<WebElement> footerLinks = footer.findElements(By.className("fmenu1 fmenu"));

            test.info("Total footer links found: " + footerLinks.size());

            for (WebElement link : footerLinks) {
                String text = link.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println( "" + text);
                    test.pass("Footer link: " + text);
                }
            }

        } catch (Exception e) {
            test.fail("Exception during footer link name extraction: " + e.getMessage());
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
