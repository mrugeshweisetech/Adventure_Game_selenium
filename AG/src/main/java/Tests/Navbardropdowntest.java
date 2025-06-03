package Tests;

import base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
//import org.example.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;
import utils.ExtentReportManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Navbardropdowntest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;
    private final String baseUrl = "https://development:development@weisetech.dev/adventuregamers/daily-deals/";

    @BeforeClass
    public void setupReport() {
        extent = ExtentReportManager.getReportInstance();
    }

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void clickNavbarDropdownItems() {
        test = extent.createTest("Navbar Dropdown Menu Test");

        try {
            driver.get(baseUrl);
            Thread.sleep(2000);

            Actions actions = new Actions(driver);

            List<WebElement> dropdownParents = driver.findElements(By.cssSelector("ul.main-nav > li.menu-item-has-children"));

            test.info("Dropdown menus found: " + dropdownParents.size());

            for (int i = 0; i < dropdownParents.size(); i++) {
                WebElement parentMenu = dropdownParents.get(i);

                actions.moveToElement(parentMenu).perform();
                Thread.sleep(1000);

                List<WebElement> dropdownItems = parentMenu.findElements(By.cssSelector("ul.sub-menu li a"));
                test.info("Dropdown " + (i + 1) + " has " + dropdownItems.size() + " sub-items.");

                for (int j = 0; j < dropdownItems.size(); j++) {
                    try {
                        driver.get(baseUrl);
                        Thread.sleep(1500);
                        parentMenu = driver.findElements(By.cssSelector("ul.main-nav > li.menu-item-has-children")).get(i);
                        actions.moveToElement(parentMenu).perform();
                        Thread.sleep(1000);

                        dropdownItems = parentMenu.findElements(By.cssSelector("ul.sub-menu li a"));
                        WebElement item = dropdownItems.get(j);
                        String linkText = item.getText().trim();

                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);
                        Thread.sleep(2000);

                        String screenshot = takeScreenshot("dropdown_" + i + "_" + j + "_" + linkText.replace(" ", "_"));
                        test.pass("Clicked dropdown item: '" + linkText + "' | Page title: " + driver.getTitle(),
                                MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());

                        driver.get(baseUrl);
                        Thread.sleep(1000);

                    } catch (Exception e) {
                        String ss = takeScreenshot("dropdown_error_" + i + "_" + j);
                        test.fail("Failed to click dropdown item " + j + ": " + e.getMessage(),
                                MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
                    }
                }
            }

        } catch (Exception e) {
            String ss = takeScreenshot("navbar_dropdown_exception");
            test.fail("Test error: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
        }
    }

    public String takeScreenshot(String fileName) {
        try {
            File folder = new File("screenshots");
            if (!folder.exists()) folder.mkdir();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + fileName + "_" + timestamp + ".png";
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
