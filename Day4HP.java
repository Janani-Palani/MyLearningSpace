package program;

import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day4HP {
	public static ChromeDriver driver;
	
//Reusable method to handle random pop-up
		public void handlePopup() throws InterruptedException {
			System.out.println("Pop handling initiated");
			if (driver.findElementByXPath("//div[contains(@class,'closeButton')]").isDisplayed()) 
			{
				driver.findElementByXPath("//div[contains(@class,'closeButton')]").click();
				System.out.println("Pop up closed");
			} 
			else System.out.println("Pop up did not occur");
		}
		
//Launch HP site in Chrome Browser and get Title
	public void launchBrowser() {
		System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://store.hp.com/in-en/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		String title = driver.getTitle();
		if (title.contains("HP")) {
			System.out.println("HP launched successfully: " + title);
		} else
			fail("HP launch failed");
	}


	public static void main(String[] args) throws InterruptedException 
	{
		Day4HP hp = new Day4HP();
		hp.launchBrowser();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Actions builder = new Actions(driver);

//Mouse over on Laptops menu and click on Pavilion
		builder.moveToElement(driver.findElementByXPath("//span[text()='Laptops']")).perform();
		builder.moveToElement(driver.findElementByXPath("//span[text()='Pavilion']")).click().build().perform();
		if (driver.findElementByXPath("//h1[text()='Pavilion Laptops']").isDisplayed()) {
			System.out.println("Navigated to Pavilion laptop products screen");
		}

		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//button[@aria-label='Close']")));
		driver.findElementByXPath("//button[@aria-label='Close']").click();

//Under SHOPPING OPTIONS -->Processor -->Select Intel Core i7
		driver.findElementByXPath("(//span[text()='Processor'])[2]").click();
		driver.findElementByXPath("//span[text()='Intel Core i7']").click();
		if (driver.findElementByXPath("//div[@class='filter-current']//span[2]").isDisplayed()) {
			System.out.println("Intel Core i7 Processor selected");
		}
		
//Call popup handle method
		hp.handlePopup();

//Hard Drive Capacity -->More than 1TB
		driver.findElementByXPath("//span[text()='More than 1 TB']").click();
		if (driver.findElementByXPath("//div[@class='filter-current']//span[text()='More than 1 TB']").isDisplayed()) {
			System.out.println("More than 1 TB is selected");
		}

//Call popup handle method
		hp.handlePopup();

//Select Sort By: Price: Low to High
		WebElement eleSelect = driver.findElementById("sorter");
		Select sortByPrice = new Select(eleSelect);
		sortByPrice.selectByValue("price_asc");
		if (driver.findElementByXPath("//option[@value='price_asc' and @selected='selected']").isSelected()) {
			System.out.println("Sorted by price low to high");
		}
		Thread.sleep(500);

//Print the First resulting Product Name and Price
		String productName = driver.findElementByXPath("//div[contains(@class,'product-item-details')]//a").getText();
		System.out.println("Product name: " + productName);
		String productPrice = driver.findElementByXPath("//span[contains(@class,'final_price')]//span/span").getText();
		System.out.println("Price: " + productPrice);

//Add to Cart
		wait.until(ExpectedConditions
				.elementToBeClickable(driver.findElementByXPath("//span[text()='Add To Cart']/parent::button")));
		driver.findElementByXPath("//span[text()='Add To Cart']/parent::button").click();

//Click on Shopping cart icon and view cart
		WebElement eleClickCart = driver.findElementByXPath("//a[contains(@class,'showcart')]");
		JavascriptExecutor click = (JavascriptExecutor) driver;
		click.executeScript("arguments[0].click();", eleClickCart);
		Thread.sleep(500);
		driver.findElementByXPath("//a[contains(@class,'viewcart')]").click();
		if ((driver.getTitle()).contains("Shopping Cart")) {
			System.out.println("Landed on shopping cart screen");
		}

//Check the Shipping Option --> Check availability at Pincode
		driver.findElementByName("pincode").sendKeys("600078");
		Thread.sleep(500);
		driver.findElementByXPath("//button[text()='check']").click();
		Thread.sleep(500);
		
//If dispatch mode is available at valid pincode
		try
		{
			if(driver.findElementByXPath("//span[@class='available']").isDisplayed())
			{
			 System.out.println("Product available for delivery at selected pincode");

			// Verify the order Total against the product price
				if ((driver.findElementByXPath("//tr[@class='grand totals']/td//span").getText())
					.equalsIgnoreCase(productPrice)) 
				{
				System.out.println("Product and price in cart matches with the one selected");
				System.out.println("Grand total: " + productPrice);
				}
			// Proceed to Checkout if Order Total and Product Price matches
				driver.findElementByXPath("//span[text()='Proceed to Checkout']/parent::button").click();
				Thread.sleep(500);

			// Click on Place Order
				driver.findElementByXPath("(//span[text()='Place Order'])[4]/parent::button").click();

			// Capture error message
				String errorMsg = driver.findElementByXPath("//div[@class='message notice']/span").getText();
				System.err.println("Mandatory details are not filled in: "+errorMsg);
			}
		}
		
//Dispatch mode not available at valid pincode (676523)
		catch(Exception e)
		{
			System.err.println("Dispatch cannot happen at the pincode specified");
		}

//Close browser
		driver.close();
	}
}
