package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class AGtopgameswordcounttest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/topgames";

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
    public void extractAndPrintAllWords() {
        test = extent.createTest("Word Extraction Test - Full Page");

        try {
            driver.get(url);
            Thread.sleep( 2000 );
            WebElement body = driver.findElement(By.tagName("body"));
            String pageText = body.getText();

            String[] words = pageText.trim().split("\\s+");
            int wordCount = words.length;

            test.info("Total words found: " + wordCount);
            System.out.println("Page Words Start");
            for (String word : words) {
                System.out.println(word);
            }
            System.out.println("Page Words End");

            test.pass("Successfully printed all words from the page");

        } catch (Exception e) {
            test.fail("Exception while extracting words:" + e.getMessage());
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
