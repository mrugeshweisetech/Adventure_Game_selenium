package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AGHomepagevideotest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;
    private final String url = "https://development:development@weisetech.dev/adventuregamers/daily-deals/";

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
    public void playVideoIfPresent() {
        test = extent.createTest("Video Detection and Play Test");

        try {
            driver.get(url);
            Thread.sleep(3000);

            // Scroll to load all content
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);

            // Check <video> tags
            List<WebElement> html5Videos = driver.findElements(By.tagName("video"));
            test.info("HTML5 <video> tags found: " + html5Videos.size());
            for (WebElement v : html5Videos) {
                test.info(" - HTML5 Video visible: " + v.isDisplayed());
            }

            // Check <iframe> tags
            List<WebElement> iframes = driver.findElements(By.tagName("home-flex video-flex home-video-section"));
            test.info("Iframe count: " + iframes.size());

            for (WebElement iframe : iframes) {
                String src = iframe.getAttribute("src");
                test.info(" - iframe src: " + src);

                if (src != null && src.contains("youtube")) {
                    driver.switchTo().frame(iframe);
                    try {
                        WebElement playBtn = driver.findElement(By.cssSelector("home-flex video-flex home-video-section"));
                        if (playBtn.isDisplayed()) {
                            test.pass("YouTube Play button is visible inside iframe.");
                        } else {
                            test.warning("YouTube Play button is NOT visible.");
                        }
                    } catch (Exception e) {
                        test.warning("No clickable button inside YouTube iframe.");
                    }
                    driver.switchTo().defaultContent();
                }
            }

        } catch (Exception e) {
            test.fail("Error during video detection: " + e.getMessage());
        }
    }

    public String takeScreenshot(String name) {
        try {
            File folder = new File("screenshots");
            if (!folder.exists()) folder.mkdirs();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + name + "_" + timestamp + ".png";
            FileUtils.copyFile(src, new File(path));
            return path;
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
            return null;
        }
    }

    @AfterMethod
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public void flushReport() {
        extent.flush();
    }
}
