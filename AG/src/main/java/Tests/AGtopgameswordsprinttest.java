package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class AGtopgameswordsprinttest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/adventure/top_100_all-time_adventure_games";

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
    public void extractAndPrintWords() {
        test = extent.createTest("Word Extraction Test - Recurring Articles Page");

        try {
            driver.get(url);
            Thread.sleep(2000);

            WebElement body = driver.findElement(By.tagName("body"));
            String text = body.getText();

            String[] words = text.trim().split("\\s+");
            int wordCount = words.length;

            System.out.println("----- Page Words Start -----");
            for (String word : words) {
                System.out.println(word);
            }
            System.out.println("----- Page Words End -----");

            test.pass("Total words found: " + wordCount);

        } catch (Exception e) {
            test.fail("Exception while extracting words: " + e.getMessage());
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
