package Resume_Update;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

public class Naukari_ResumeUpdate {
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeClass
	public void setUp() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless=new");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--disable-gpu");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-blink-features=AutomationControlled");
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

		driver = new ChromeDriver(options);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

		wait = new WebDriverWait(driver, Duration.ofSeconds(60));
	}

	@Test
	public void testNaukriProfileUpdate() throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
		String timestamp = LocalDateTime.now().format(formatter);

		try {
			driver.get("https://www.naukri.com/");

			WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//a[@title='Jobseeker Login']")
			));
			loginLink.click();

			WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//input[@placeholder='Enter your active Email ID / Username']")
			));
			WebElement password = driver.findElement(By.xpath("//input[@placeholder='Enter your password']"));
			WebElement submit = driver.findElement(By.xpath("//button[@type='submit']"));

			username.sendKeys("contactstoshubham@gmail.com");
			password.sendKeys("Rishabh@108");
			submit.click();

			WebElement profile = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[@class='nI-gNb-drawer__bars']")
			));
			profile.click();

			WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//a[@class='nI-gNb-info__sub-link']")
			));
			menu.click();

			WebElement resumeUpload = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//input[@type='file']")
			));

			String filePath = Paths.get("test-input", "Shubham_Resume25.pdf").toAbsolutePath().toString();
			resumeUpload.sendKeys(filePath);

			WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//span[@id='attachCVMsgBox']//p[@class='msg']")
			));
			String messageText = successMessage.getText();
			System.out.println("Success Message: " + messageText);
			String expectedText = "Resume has been successfully uploaded.";

			Assert.assertEquals(messageText, expectedText);

			// Screenshot on success
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File destination = new File("ResumeUploadSuccess_" + timestamp + ".png");
			FileUtils.copyFile(screenshot, destination);
			System.out.println("Screenshot saved at: " + destination.getAbsolutePath());

		} catch (Exception e) {
			// Screenshot on failure
			File errorShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File dest = new File("FailureScreenshot_" + timestamp + ".png");
			FileUtils.copyFile(errorShot, dest);
			System.err.println("Test failed. Screenshot saved at: " + dest.getAbsolutePath());
			throw e;
		}
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
