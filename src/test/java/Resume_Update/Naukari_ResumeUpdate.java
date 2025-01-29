package Resume_Update;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.openqa.selenium.support.ui.WebDriverWait;
	import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.testng.Assert;
    import org.testng.annotations.AfterClass;
	import org.testng.annotations.BeforeClass;
	import org.testng.annotations.Test;

	public class Naukari_ResumeUpdate {
	    private WebDriver driver;
	    private WebDriverWait wait;

	    @BeforeClass
	    public void setUp() {
	        driver = new ChromeDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        driver.manage().window().maximize();
	    }

	    @Test
	    public void testNaukriProfileUpdate() throws InterruptedException, IOException {
	        driver.get("https://www.naukri.com/");

	        driver.findElement(By.xpath("//a[@title='Jobseeker Login']")).click();

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

	        String filePath = Paths.get("C:\\Users\\shubh\\Downloads\\Shubham_Resume.pdf").toString();
	        resumeUpload.sendKeys(filePath);

	        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//span[@id='attachCVMsgBox']//p[@class='msg']")
	        ));
	        String messageText = successMessage.getText();
	        System.out.println("Success Message: " + messageText);
	        String expectedText = "Resume has been successfully uploaded.";
	        Assert.assertEquals(messageText,expectedText);
	     // Capture screenshot on success
	        if (messageText.equals(expectedText)) {
	            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	            // Define the location to save the screenshot
	            File destination = new File("C:\\Users\\shubh\\Pictures\\ResumeUploadSuccess.png");
	            FileUtils.copyFile(screenshot, destination);
	            System.out.println("Screenshot saved at: " + destination.getAbsolutePath());
	        }
	    }

	    @AfterClass
	    public void tearDown() throws InterruptedException {
	        if (driver != null) {
	            driver.quit();
	        }
	    }
	}