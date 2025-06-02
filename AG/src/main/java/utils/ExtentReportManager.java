package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class ExtentReportManager {
    private static ExtentReports extent;
    // protected ExtentTest test;
    //  private static File TakesScreenshot;

    public static ExtentReports getReportInstance() {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports/ExtentReport.html");
            sparkReporter.config().setReportName("UI Test Report");
            sparkReporter.config().setDocumentTitle("Automation Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
        return extent;
    }

    public static <string> ExtentTest createTest(string testName) {

        ExtentTest test = getReportInstance().createTest("String.valueOf(testName)");
        return test;
    }

    public static String captureScreenshot(WebDriver driver, String scrrenshotName) {
        try {

            File src = ((TakesScreenshot) driver).getScreenshotAs( OutputType.FILE);

            String path = "screenshots/" + scrrenshotName + ".png";
            FileUtils.copyFile(src, new File(path));
            return path;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

