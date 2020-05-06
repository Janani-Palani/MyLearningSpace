package program;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.ToolTipManager;
import javax.swing.plaf.ToolTipUI;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day16Ajio {
	public static ChromeDriver driver;
	
	public static void main(String[] args) throws InterruptedException {
		Day16Ajio obj=new Day16Ajio();
//1) Launch Ajio site
		ChromeOptions options=new ChromeOptions();
		options.addArguments("--disable-notifications");
	System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
	System.setProperty("webdriver.chrome.silentOutput", "true");
	driver = new ChromeDriver(options);
	driver.get("https://www.ajio.com/shop/sale");
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	String title = driver.getTitle();
	if (title.contains("AJIO"))
		System.out.println("AJIO launched successfully: " + title);
	else
		System.err.println("AJIO Study Abroad launch failed");
	
	Actions build=new Actions(driver);
	WebDriverWait wait=new WebDriverWait(driver,20);
	
//2) Enter Bags in the Search field and Select Bags in Women Handbags
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//input[@placeholder='Search AJIO']")));
	driver.findElementByXPath("//input[@placeholder='Search AJIO']").sendKeys("Bags");
	Thread.sleep(1000);
	driver.findElementByXPath("//span[text()='Women Handbags']/parent::a").click();
	String pageHeader = driver.findElementByXPath("//h1/div[2]").getText();
	System.out.println(pageHeader);
	
//3) Click on five grid and Select SORT BY as "What's New"
	driver.findElementByXPath("//div[@class='five-grid']/parent::div").click();
	WebElement eleSort = driver.findElementByXPath("//div[@class='filter-dropdown']/select");
	Select sortFilter=new Select(eleSort);
	sortFilter.selectByVisibleText("What's New");
	System.out.println("Grid selected as Five and Sorted by What's New");
	
//4) Enter Price Range Min as 2000 and Max as 5000
	driver.findElementByXPath("//span[text()='price']").click();
	driver.findElementById("minPrice").sendKeys("2000");
	driver.findElementById("maxPrice").sendKeys("5000");
	driver.findElementByXPath("//div[@class='facet-min-price-filter']/button").click();
	System.out.println("Price range set between 2000 and 5000");
	
//5) Click on the product "Puma Ferrari LS Shoulder Bag"
	Thread.sleep(2000);
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//div[text()='Ferrari LS Shoulder Bag']/ancestor::div[2]")));
	JavascriptExecutor executor =(JavascriptExecutor) driver;
	executor.executeScript("arguments[0].click();",driver.findElementByXPath("//div[text()='Ferrari LS Shoulder Bag']/ancestor::div[2]"));
	//build.moveToElement(driver.findElementByXPath("//div[text()='Ferrari LS Shoulder Bag']/ancestor::div[2]")).click().build().perform();
	//driver.findElementByXPath("//div[text()='Ferrari LS Shoulder Bag']/ancestor::div[2]").click();
	Set<String> setHandle = driver.getWindowHandles();
	List<String> listHandle=new ArrayList<String>(setHandle);
	driver.switchTo().window(listHandle.get(1));
	
//6) Verify the Coupon code for the price above 2690 is applicable for your product, if applicable the get the Coupon Code and Calculate the discount price for the coupon
	String brandName = driver.findElementByXPath("//div[@class='prod-content']/h2").getText();
	String prodName = driver.findElementByXPath("//div[@class='prod-content']/h1").getText();
	System.out.println("Brand: "+brandName);
	System.out.println("Product: "+prodName);
	String rawPrice = driver.findElementByXPath("//div[@class='prod-price-section']/div[@class='prod-sp']").getText();
	int prodPrice = Integer.parseInt(rawPrice.replaceAll("\\D", ""));
	System.out.println("Product price: "+prodPrice);
	if(prodPrice>2690)
	{
		Thread.sleep(500);
		if(driver.findElementByXPath("//div[@class='promo-desc']").getText().contains("Extra Upto 28% Off on 2690 and Above "))
		{
			String couponCode = driver.findElementByXPath("//div[@class='promo-title']").getText();
			System.out.println("Coupon code applicable for product price available above 2690 is: "+couponCode);
		}
		else System.err.println("Coupon code not displayed for product price available above 2690");
	}
	else System.out.println("Coupon code not applicable for product price above 2690");
	String discountedPrice = driver.findElementByXPath("//div[text()='Get it for ']/span").getText();
	int discPrice = Integer.parseInt(discountedPrice.replaceAll("\\D", ""));
	System.out.println("Discounted price: "+discPrice);
	int couponAmount=prodPrice-discPrice;
	System.out.println("Coupon Amount: "+couponAmount);
	
//7) Check the availability of the product for pincode 560043, print the expected delivery date if it is available
	Thread.sleep(1000);
	driver.findElementByXPath("//span[contains(text(),'Enter pin-code')]").click();
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//input[@name='pincode']")));
	driver.findElementByXPath("//input[@name='pincode']").sendKeys("560043");
	Thread.sleep(500);
	driver.findElementByXPath("//button[text()='CONFIRM PINCODE']").click();
	Thread.sleep(2000);
	String pincodeMsg = driver.findElementByXPath("//ul[@class='edd-message-success-details']/li").getText();
	if(pincodeMsg.contains("Expected Delivery"))
	{
		System.out.println("Product available for pincode specified");
	}
	else System.err.println("Product not available for pincode specified");
	
//8) Click on Other Informations under Product Details and Print the Customer Care address, phone and email
	build.moveToElement(driver.findElementByXPath("//div[text()='Other information']")).click().build().perform();
	Thread.sleep(500);
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//span[text()='Customer Care Address']")));
	String customerCareDetails = driver.findElementByXPath("//span[text()='Customer Care Address']/parent::li/span[@class='other-info']").getText();
	System.out.println("Customer care address, phone and email: "+customerCareDetails);
	
//9) Click on ADD TO BAG and then GO TO BAG
	driver.findElementByXPath("//span[text()='ADD TO BAG']/ancestor::div[1]").click();
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//span[text()='GO TO BAG']/ancestor::div[1]")));
	executor.executeScript("arguments[0].click();",driver.findElementByXPath("//span[text()='GO TO BAG']/ancestor::div[1]"));
	Thread.sleep(1000);
	if(driver.getTitle().contains("Shopping Bag"))
	{
		System.out.println("Navigated to Shopping bag");
	}
	
//10) Check the Order Total before apply coupon
	Thread.sleep(2000);
	String strOrderTotal = driver.findElementByXPath("//span[text()='Order Total']/following-sibling::span").getText();
	String strOrderTot = strOrderTotal.replaceAll("\\D", "");
	int total = Integer.parseInt(strOrderTot.substring(0, strOrderTot.length()-2));
	System.out.println("Order total before applying coupon code: "+total);
	
//11) Enter Coupon Code and Click Apply
	driver.findElementById("couponCodeInput").sendKeys("EPIC");
	driver.findElementByXPath("//button[text()='Apply']").click();
	System.out.println("Coupon applied");
	
//12) Verify the Coupon Savings amount(round off if it in decimal) under Order Summary and the matches the amount calculated in Product details
	Thread.sleep(1000);
	String rawPriceAfterCoupon = driver.findElementByXPath("//span[@class='applied-coupon-section']/p").getText();
	String rawPriceAfter = rawPriceAfterCoupon.replaceAll("[,a-zA-Z ]", "");
	String[] split = rawPriceAfter.split(".", 2);
	String strPriceAfterCoupon = split[1];
	double dpriceAfterCoupon = Double.parseDouble(strPriceAfterCoupon);
	int priceAfterCoupon=(int)Math.round(dpriceAfterCoupon);
	if(priceAfterCoupon==couponAmount)
	{
		System.out.println("Price after couple applied matches with coupon amount: "+priceAfterCoupon);
	}
	
//13) Click on Delete and Delete the item from Bag
	driver.findElementByXPath("//div[@class='product-delete']/div").click();
	Thread.sleep(1000);
	driver.findElementByXPath("//div[@class='card-delete-button']//div[text()='DELETE']").click();
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//div[@class='empty-cart']/p")));
	String emptyCartMsg = driver.findElementByXPath("//div[@class='empty-cart']/p").getText();
	if(emptyCartMsg.contains("Your Shopping Bag is Empty"))
	{
		System.out.println("Item deleted from bag");
	}
	
	
	
	
	
	
	
	
	
	
}
}