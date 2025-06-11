package Tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

public class AGgamevalidationtest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/adventure/all";

    @BeforeClass
    public void setupReport() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/GameListingReport.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void validateGameListings() {
        test = extent.createTest("🎮 Game Listing Validation Test");

        try {
            driver.get(url);
            Thread.sleep(3000);

            List<WebElement> gameCards = driver.findElements(By.cssSelector(".games-database .games-database-game"));

            test.info("Total games found: " + gameCards.size());

            for (WebElement card : gameCards) {
                try {
                    String title = card.findElement(By.cssSelector(".games-database-game-title")).getText().trim();
                    String genre = card.findElement(By.cssSelector(".games-database-game-genre")).getText().trim();
                    String ratingText = "";

                    try {
                        ratingText = card.findElement(By.cssSelector(".games-database-game-rating")).getText().trim();
                    } catch (NoSuchElementException e) {
                        ratingText = "N/A";
                    }

                    test.info("Title: " + title);
                    test.info("Genre: " + genre);
                    test.info(" Rating: " + ratingText);

                    if (title.isEmpty()) {
                        test.warning(" Game title missing!");
                    }

                    if (genre.isEmpty()) {
                        test.warning(" Genre is missing for: " + title);
                    }

                    if (!ratingText.equals("N/A")) {
                        try {
                            double rating = Double.parseDouble(ratingText);
                            if (rating < 1 || rating > 5) {
                                test.fail("Invalid rating (" + rating + ") for: " + title);
                            }
                        } catch (NumberFormatException e) {
                            test.fail("Rating not numeric for: " + title + " → '" + ratingText + "'");
                        }
                    }

                } catch (Exception e) {
                    test.warning("Error parsing one of the game entries: " + e.getMessage());
                }
            }

            test.pass("Game listing validation completed.");

        } catch (Exception e) {
            test.fail("Exception in main test: " + e.getMessage());
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
