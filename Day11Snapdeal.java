package program;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day11Snapdeal {

	public static void main(String[] args) throws InterruptedException {

//Launch Snapdeal site
	System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
	ChromeDriver driver = new ChromeDriver();
	driver.get("https://www.snapdeal.com/");
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	String title = driver.getTitle();
	if (title.contains("Snapdeal"))
		System.out.println("Snapdeal launched successfully: " + title);
	else
		System.err.println("Snapdeal launch failed");
	
//Mouse over on Toys, Kids' Fashion & more and click on Toys 
	Actions builder=new Actions(driver);
	builder.moveToElement(driver.findElementByLinkText("Toys, Kids' Fashion & more")).perform();
	builder.moveToElement(driver.findElementByXPath("//span[text()='Toys']/parent::a")).click().build().perform();
	
//Click Educational Toys in Toys & Games
	Thread.sleep(1000);
	driver.findElementByXPath("//div[text()='Toys & Games']/parent::a/following-sibling::ul//div[text()='Educational Toys']").click();
	if(driver.getTitle().contains("Educational Toys"))
	{
		System.out.println("Naivgated to Educational Toys page");
	}
	
//Click the Customer Rating 4 star and Up
	driver.findElementByXPath("//label[@for='avgRating-4.0']").click();
	Thread.sleep(3000);
	if(driver.findElementByXPath("//a[text()='4.0']").isDisplayed())
	{
		System.out.println("Products listed based on customer rating of 4.0 and above");
	}
	
//Click the offer as 40-50 
	driver.findElementByXPath("//label[@for='discount-40%20-%2050']").click();
	Thread.sleep(2000);
	if(driver.findElementByXPath("//a[text()='40 - 50']").isDisplayed())
	{
		System.out.println("Products listed based on discount of 40-50%");
	}
	
//Check the availability for the pincode 
	driver.findElementByXPath("//input[@placeholder='Enter your pincode']").sendKeys("600026");
	driver.findElementByXPath("//button[text()='Check']").click();
	WebDriverWait wait=new WebDriverWait(driver,10);
	String pincodeCheck = driver.findElementById("myPincode").getText();
	if(pincodeCheck.equalsIgnoreCase("600026"))
	{
		System.out.println("Delivery available for the pincode");
	}
	
//Click the Quick View of the first product 
	builder.moveToElement(driver.findElementByXPath("(//section[@data-dpwlbl='Product Grid'])[1]/div[1]")).perform();
	Thread.sleep(1000);
	driver.findElementByXPath("(//section[@data-dpwlbl='Product Grid'])[1]/div[1]//div[@class='clearfix row-disc']/div").click();
	
//Click on View Details 
	Thread.sleep(1000);
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//a[contains(text(),'view details')]")));
	driver.findElementByXPath("//a[contains(text(),'view details')]").click();
	
//Capture the Price of the Product and Delivery Charge 
	Thread.sleep(2000);
	String rawToyPrice = driver.findElementByXPath("//span[@class='pdp-final-price']/span").getText();
	String rawToyDelCharge = driver.findElementByXPath("//span[contains(text(),'Delivery')]/parent::span//span[@class='availCharges']").getText();
	System.out.println("Price of the toy: "+rawToyPrice);
	System.out.println("Delivery charge of the toy: "+rawToyDelCharge);
	driver.findElementByXPath("//span[text()='add to cart']").click();
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//span[contains(text(),'added to your cart')]")));
	if(driver.findElementByXPath("//span[@class='mess-text']").getText().contains("added to your cart"))
	{
		System.out.println("Toy added to cart");
	}
	
//Validate the You Pay amount matches the sum of (price+deliver charge)
	String rawToyTotalPrice = driver.findElementByXPath("//div[@class='you-pay']/span[@class='price']").getText();
	int toyPrice = Integer.parseInt(rawToyPrice);
	int toyDelCharge = Integer.parseInt(rawToyDelCharge.replaceAll("\\D", ""));
	int toyTotalPrice = Integer.parseInt(rawToyTotalPrice.replaceAll("\\D", ""));
	if(toyTotalPrice==(toyPrice+toyDelCharge))
	{
		System.out.println("Total price matches with subtotal of Toy: "+toyTotalPrice);
	}
	else System.err.println("Price mismatch for Toy");
	
//Search for Sanitizer
	driver.findElementById("inputValEnter").sendKeys("Sanitizer");
	Thread.sleep(500);
	driver.findElementByXPath("//a[@data-labelname='sanitizer']").click();
	
//Click on Product "BioAyurveda Neem Power Hand Sanitizer"
	builder.moveToElement(driver.findElementByXPath("//p[contains(text(),'BioAyurveda Neem Power  Hand Sanitizer')]/ancestor::div[2]/preceding-sibling::div[1]")).perform();
	Thread.sleep(1000);
	driver.findElementByXPath("//p[contains(text(),'BioAyurveda Neem Power  Hand Sanitizer')]/ancestor::div[3]//div[@class='clearfix row-disc']//div").click();
		
//Capture the Price and Delivery Charge 
	Thread.sleep(2000);
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//a[contains(text(),'view details')]")));
	driver.findElementByXPath("//a[contains(text(),'view details')]").click();
	Thread.sleep(2000);
	String rawSanitizerPrice = driver.findElementByXPath("//span[@class='pdp-final-price']/span").getText();
	String rawSanitizerDeliveryCharge = driver.findElementByXPath("//span[contains(text(),'Delivery')]/parent::span//span[@class='availCharges']").getText();
	System.out.println("Price of the sanitizer: "+rawSanitizerPrice);
	System.out.println("Delivery charge of the sanitizer: "+rawSanitizerDeliveryCharge);
	
//Click on Add to Cart 
	driver.findElementByXPath("//span[text()='ADD TO CART']").click();
	
//Click on Cart  
	Thread.sleep(2000);
	driver.findElementByXPath("//span[text()='Cart']").click();
	Thread.sleep(2000);
	String strsanitizerTotalPrice = driver.findElementByXPath("//a[text()='BioAyurveda Neem Power  Hand Sanitizer 500 mL Pack of 1']/ancestor::div[3]/following-sibling::div[contains(@class,'item-details')]//span").getText();
	int sanitizerTotalPrice = Integer.parseInt(strsanitizerTotalPrice.replaceAll("\\D", ""));
	
//Validate the Proceed to Pay matches the total amount of both the products 
	Thread.sleep(2000);
	String strGrandTotal = driver.findElementByXPath("//input[contains(@value,'PROCEED TO PAY')]").getAttribute("value");
	int grandTotal = Integer.parseInt(strGrandTotal.replaceAll("\\D", ""));
		if(grandTotal==(toyTotalPrice+sanitizerTotalPrice))
		{
			System.out.println("Grand total matches with subtotal of products added :"+grandTotal);
		}
		else System.err.println("Total mismatch");
	
//Close browser
		driver.close();
	}
}

