package program;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day2NykaaTest {
	public static ChromeDriver driver;

//Window Handling
	public List<String> windowHandling()
	{
		Set<String> setHandle = driver.getWindowHandles();
		List<String> listHandle=new ArrayList<String> (setHandle);
		return listHandle;
	}
	
//Launch Nykaa e-commerce site in Chrome Browser and get Title
	public void launchBrowser() {
		System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.nykaa.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		String title = driver.getTitle();
		if (title.contains("Nykaa"))
			System.out.println("Nykaa launched successfully: " + title);
		else
			System.err.println("Nykaa launch failed");
	}
	

	public static void main(String[] args) throws InterruptedException {
		
//Setting up objects
		Day2NykaaTest obj = new Day2NykaaTest();
		obj.launchBrowser();
		Actions builder = new Actions(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);

//Mouse hover on Brands and Popular
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//a[text()='brands']")));
		builder.moveToElement(driver.findElementByXPath("//a[text()='brands']")).perform();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//a[text()='Popular']")));
		builder.moveToElement(driver.findElementByXPath("//a[text()='Popular']")).perform();
		System.out.println("Brand and Popular selected");

//Click on Loreal Paris
		driver.findElementByXPath("(//a[contains(@href,'loreal-paris')])[3]").click();

//Switch to L'Oreal Paris product window and check title
		driver.switchTo().window(obj.windowHandling().get(1));
		String windowTitle = driver.getTitle();
		if (windowTitle.contains("L'Oreal Paris")) {
			System.out.println("Navigated to L'Oreal page :" + windowTitle);
		} else
			System.err.println("Failed to navigate to L'Oreal page");

// Click sort By and select customer top rated
		driver.findElementByXPath("//span[text()='popularity']").click();
		driver.findElementByXPath("//span[text()='customer top rated']").click();
		System.out.println("Sorted by customer top rated");

// Click Category
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("(//div[contains(@class,'pull-right')])[1]")));
		driver.findElementByXPath("(//div[contains(@class,'pull-right')])[1]").click();
		System.out.println("Clicked Category");

// Check whether the Filter is applied with Shampoo
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("(//label[@for='chk_Shampoo_undefined'])[1]")));
		WebElement eleShampooFilter = driver.findElementByXPath("(//label[@for='chk_Shampoo_undefined'])[1]");
		if (!eleShampooFilter.isSelected()) 
		{
			eleShampooFilter.click();
			System.out.println("Shampoo is filtered now");
		} 
		else
			System.out.println("Shampoo is filtered by default");

// Click on L'Oreal Paris Colour Protect Shampoo
		driver.findElementByXPath("//a[contains(@href,'paris-color-protect')]//img").click();

// Navigate to the product window
		driver.switchTo().window(obj.windowHandling().get(2));
		if (driver.getTitle().contains("L'Oreal Paris Colour Protect"))
		{
			System.out.println("Navigated to the product");
		} else
			System.err.println("Product Navigation failed");

		driver.findElementByXPath("//span[text()='175ml']").click();
		System.out.println("Size selected as 175ml");

// Print price of the product
		String productPrice = driver.findElementByXPath("(//div[@class='price-info']//span)[4]").getText();
		System.out.println("Product price: " + productPrice);

// Add Product to bag and confirm
		driver.findElementByXPath("//span[contains(@class,'Shopping-Bag')]/parent::button").click();
		wait.until(ExpectedConditions
				.visibilityOf(driver.findElementByXPath("//span[text()='Product has been added to bag.']")));
		String confirmMsg = driver.findElementByXPath("//span[text()='Product has been added to bag.']").getText();
		System.out.println(confirmMsg+ " message is displayed");
		
// Go to Shopping bag
		driver.findElementByXPath("//div[@class='AddToBagbox']").click();
		
// Print the Grand Total amount
		String grandTotal = driver.findElementByXPath("//div[@class='payment-tbl-data']/div[4]/div[2]").getText();
		System.out.println("Grand total: "+grandTotal);
		
// Click to Proceed
		driver.findElementByXPath("//button[contains(@class,'proceed')]").click();
		
// Continue as Guest
		driver.findElementByXPath("//button[text()='CONTINUE AS GUEST']").click();
		
// Print warning message
		String warningMsg = driver.findElementByXPath("//div[contains(text(),'lockdown')]").getText();
		System.out.println(warningMsg);
		Thread.sleep(500);
		
// Close all windows
		List<String> handles = obj.windowHandling();
		for(int i=0;i<handles.size();i++)
		{
			driver.switchTo().window(handles.get(i)).close();
		}
		System.out.println("Closed all windows");
	}
}
