package Tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NavbarTest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    private final String baseUrl = "https://development:development@weisetech.dev/adventuregamers/daily-deals/";

    @BeforeClass
    public void setupReport() {
        utils.ExtentReportManager ExtentReportManager = null;
        extent = ExtentReportManager.getReportInstance();
    }

    @BeforeMethod
    public void launchBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void clickNavbarLinksAndReturnHome() {
        test = extent.createTest("Navbar Menu Test - AdventureGamers");

        try {
            driver.get(baseUrl);
            Thread.sleep(2000);

            List<WebElement> navbarLinks = driver.findElements(By.cssSelector("ul.main-nav > li > a"));
            int totalLinks = navbarLinks.size();
            test.info("Total navbar items found: " + totalLinks);

            for (int i = 0; i < totalLinks; i++) {
                try {
                    driver.get(baseUrl);
                    Thread.sleep(1500);


                    navbarLinks = driver.findElements(By.cssSelector("ul.main-nav > li > a"));
                    WebElement navItem = navbarLinks.get(i);
                    String linkText = navItem.getText().trim();
                    if (linkText.isEmpty()) linkText = navItem.getAttribute("href");

                    navItem.click();
                    Thread.sleep(2000);


                    String ssPath = takeScreenshot("navbar_" + i + "_" + linkText.replaceAll("\\s+", "_"));
                    test.pass("Clicked: '" + linkText + "' | Page title: " + driver.getTitle(),
                            MediaEntityBuilder.createScreenCaptureFromPath(ssPath).build());

                    driver.get(baseUrl);
                    Thread.sleep(1000);

                } catch (Exception e) {
                    String errSS = takeScreenshot("navbar_error_" + i);
                    test.fail("Error on navbar item " + i + ": " + e.getMessage(),
                            MediaEntityBuilder.createScreenCaptureFromPath(errSS).build());
                }
            }

        } catch (Exception e) {
            String ss = takeScreenshot("navbar_test_failure");
            test.fail("Test failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
        }
    }

    public String takeScreenshot(String name) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + name + "_" + timestamp + ".png";
            File dest = new File(path);
            FileUtils.copyFile(src, dest);
            return path;
        } catch (Exception e) {
            return null;
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
