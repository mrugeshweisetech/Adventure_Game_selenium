package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class AGsearchicontest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/";
    String searchKeyword = "game";

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
    public void testSearchFunctionality() {
        test = extent.createTest("Search Functionality Test - Homepage");

        try {
            driver.get(url);
            Thread.sleep(2000);

            WebElement searchBox = driver.findElement(By.cssSelector("input[type='search']"));
            searchBox.sendKeys(searchKeyword);
            searchBox.sendKeys(Keys.ENTER);

            Thread.sleep(3000);

            String title = driver.getTitle();
            String pageText = driver.findElement(By.tagName("body")).getText();

            if (title.toLowerCase().contains(searchKeyword.toLowerCase()) ||
                    pageText.toLowerCase().contains(searchKeyword.toLowerCase())) {

                test.pass("Search successful. Keyword '" + searchKeyword + "' found in results.");
            } else {
                test.fail(" Search failed. Keyword '" + searchKeyword + "' not found.");
            }

        } catch (Exception e) {
            test.fail(" Exception during search: " + e.getMessage());
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
