package program;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day8PepperFry {

	public static void main(String[] args) throws InterruptedException, IOException {
		
//Launch PepperFry
		System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		driver.get("https://www.pepperfry.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		String title = driver.getTitle();
		if (title.contains("Pepperfry")) {
			System.out.println("Pepperfry launched successfully: " + title);
		} else
			System.err.println("Pepperfry launch failed");

		WebDriverWait wait = new WebDriverWait(driver, 10);
		Actions builder = new Actions(driver);
		
//Close pop-up
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//div[@class='reg-modal-right-frm-wrap']")));
		driver.findElementByXPath("(//a[@class='popup-close'])[5]").click();
		
//Mouse hover on Furniture and click Office Chairs under Chairs 		
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//a[text()='Furniture']")));
		builder.moveToElement(driver.findElementByXPath("//a[text()='Furniture']")).perform();
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//a[text()='Office Chairs']")));
		builder.moveToElement(driver.findElementByXPath("//a[text()='Office Chairs']")).click().build().perform();
		if(driver.getTitle().contains("Office Chairs"))
		{
			System.out.println("Navigated to Office chairs screen");
		} else System.err.println("Office chairs screen is not loaded");
		
//click Executive Chairs
		Thread.sleep(500);
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//div[@class='cat-wrap-img']/img[@alt='Executive Chairs']")));
		JavascriptExecutor click =(JavascriptExecutor) driver;
		click.executeScript("arguments[0].click();", driver.findElementByXPath("//div[@class='cat-wrap-img']/img[@alt='Executive Chairs']"));
		if(driver.getTitle().contains("Executive Chair"))
		{
			System.out.println("Navigated to Executive chairs screen");
		} else System.err.println("Executive chairs screen is not loaded");
		
//Change the minimum Height as 50 in under Dimensions 
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//div[text()='Height']/parent::li/div[3]/input")));
		driver.findElementByXPath("//div[text()='Height']/parent::li/div[3]/input").clear();
		driver.findElementByXPath("//div[text()='Height']/parent::li/div[3]/input").sendKeys("50",Keys.ENTER);
		String eleHeightFilter = driver.findElementByXPath("//li[contains(@class,'fltrd')]").getText();
		if(eleHeightFilter.equalsIgnoreCase("Height(50-55)"))
		{
			System.out.println("Executive chairs filtered with Height of 50-55 inches");
		}
		
//Add "Poise Executive Chair in Black Colour" chair to Wishlist 
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//a[text()='Poise Executive Chair in Black Colour']")));
		driver.findElementByXPath("//a[text()='Poise Executive Chair in Black Colour']/ancestor::div[2]/following-sibling::div/div[2]/a").click();
		
//Mouseover on Homeware and Click Pressure Cookers under Cookware 
		builder.moveToElement(driver.findElementByXPath("//a[text()='Homeware']")).perform();
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//a[text()='Pressure Cookers']")));
		driver.findElementByXPath("//a[text()='Pressure Cookers']").click();
		if(driver.findElementByXPath("//h1[text()='PRESSURE COOKERS']").isDisplayed())
		{
			System.out.println("Pressue cooker products are displayed");
		}
		
//Select Prestige as Brand
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//label[text()='Prestige']")));
		driver.findElementByXPath("//label[text()='Prestige']").click();
		
//Select Capacity as 1-3 Ltr 
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//label[text()='1 Ltr - 3 Ltr']")));
		driver.findElementByXPath("//label[text()='1 Ltr - 3 Ltr']").click();
		if(driver.findElementByXPath("//li[text()='Prestige']").isDisplayed() && driver.findElementByXPath("//li[text()='1 Ltr - 3 Ltr']").isDisplayed())
		{
			System.out.println("Prestige brand and capacity as 1-3 Ltr is selected");
		}
		
//Add "Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr" to Wishlist 
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr']")));
		driver.findElementByXPath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr']/ancestor::div[2]/following-sibling::div/div[2]/a").click();
		
//Verify the number of items in Wishlist 
		Thread.sleep(1000);
		String wishlistCount = driver.findElementByXPath("//a[contains(@class,'wistlist_img')]/following-sibling::span").getText();
		if(wishlistCount.equalsIgnoreCase("2"))
		{
		System.out.println("Number of items wishlisted: "+wishlistCount);
		}
		else System.err.println("Wishlist count does not match as expected");
		
//Navigate to Wishlist
		driver.findElementByXPath("//a[contains(@class,'wistlist_img')]").click();
		
//Move Pressure Cooker only to Cart from Wishlist 
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr']")));
		driver.findElementByXPath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr By...']/parent::p/following-sibling::div/div/a").click();
		if(driver.findElementByXPath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr By Prestige']").isDisplayed())
		{
			System.out.println("Pressure cooker added to cart");
		}
		else System.err.println("Pressure cooker not present");
		
//Check for the availability for Pincode 600128
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//span[text()='Showing availability at']/following-sibling::input")));
		driver.findElementByXPath("//span[text()='Showing availability at']/following-sibling::input").sendKeys("600128",Keys.ENTER);
		Thread.sleep(1000);
		
//Valid pincode
		if(driver.findElementByXPath("//div[@class='item_cta']/p").getText().contains("Use Coupon "))
		{
			System.out.println("Product can be delivered to the pincode specified");
		//Click Proceed to Pay Securely 
			driver.findElementByXPath("//a[text()='Proceed to pay securely ']").click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//a[text()='PLACE ORDER']")));
		//Click Place order
			driver.findElementByXPath("//a[text()='PLACE ORDER']").click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//span[text()='ORDER SUMMARY']/parent::div/following-sibling::div/span")));
			driver.findElementByXPath("//span[text()='ORDER SUMMARY']/parent::div/following-sibling::div/span").click();
		//Capture the screenshot of the item under Order Item
			File source = driver.findElementByXPath("//div[@class='slick-track']/li").getScreenshotAs(OutputType.FILE);
			File dest = new File("./snap/product.png");
			FileUtils.copyFile(source, dest);
		}
		
//Invalid Pincode or valid pincode where product is not available for delivery
		else
		{
		System.err.println("Product cannot be delivered to the pincode specified");	
		}
		
//Close browser
		driver.close();
	}

}
