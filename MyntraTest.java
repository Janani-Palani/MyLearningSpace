package program;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public class MyntraTest {
	public static ChromeDriver driver;
	
//Convert String to Integer
	public int stringToInt(String str) 
	{
		String strvalue = str.replaceAll("\\D", "");
		int intvalue = Integer.parseInt(strvalue);
		return intvalue;
	}

//Launch Myntra e-commerce site in Chrome Browser and get Title
	public void launchBrowser() 
	{
		System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.myntra.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		String title = driver.getTitle();
		System.out.println("Myntra launched successfully: "+title);
	}

	public static void main(String[] args) throws InterruptedException 
	{
//Object creation for the class to call the methods
		MyntraTest obj = new MyntraTest();
		obj.launchBrowser();

//Object creation for Actions class
		Actions builder = new Actions(driver);

//Mouse over on Women category
		WebElement ele1 = driver.findElementByXPath("//a[text()='Women']");
		builder.moveToElement(ele1).perform();

//Click Jackets & Coats
		driver.findElementByXPath("//a[text()='Jackets & Coats']").click();

//Surpass notifications
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");

//Fetch the total count of items
		String strtotal = driver.findElementByXPath("//span[@class='title-count']").getText();
		int totalitems = obj.stringToInt(strtotal);

//Ensure the total items count matches with the categories split
		String strjacket = driver.findElementByXPath("//span[@class='categories-num']").getText();
		String strcoat = driver.findElementByXPath("(//span[@class='categories-num'])[2]").getText();
		int totalof_jacketsandcoats = obj.stringToInt(strjacket) + obj.stringToInt(strcoat);
		if (totalitems == totalof_jacketsandcoats) {
			System.out.println("Total count matches with the splitup: " + totalitems);
		}

//Select Coats Category
		driver.findElementByXPath("(//span[@class='categories-num'])[2]").click();

//Click for More options under Brand section
		driver.findElementByXPath("//div[@class='brand-more']").click();

//Search and select MANGO brand 
		driver.findElementByClassName("FilterDirectory-searchInput").sendKeys("MANGO");
		driver.findElementByXPath("(//input[@value='MANGO']/parent::label)[2]").click();

//Close the pop-up box after search
		driver.findElementByXPath("//span[contains(@class,'FilterDirectory-close')]").click();
		Thread.sleep(1000);
		
//To confirm all coats are from brand MANGO
		List<WebElement> brandlist = driver.findElementsByXPath("//h3[@class='product-brand']");
		boolean flag=false;
		for (WebElement eachbrand : brandlist) 
		{
			String brands = eachbrand.getText();
			if (!brands.equalsIgnoreCase("MANGO")) 
			{
			flag=true;
			}
		}
		if(flag=false)
		{
			System.out.println("All coats are from brand MANGO");
		}

//Sort by Better Discount
		WebElement ele2 = driver.findElementByXPath("//div[@class='sort-sortBy']");
		builder.moveToElement(ele2).perform();
		driver.findElementByXPath("//ul[@class='sort-list']/li[3]/label").click();
		Thread.sleep(500);
		
//Find the price of first displayed item
		List<WebElement> allitemsprice = driver.findElementsByXPath("//span[@class='product-discountedPrice']");
		String firstitemprice = allitemsprice.get(0).getText();
		System.out.println("Price of first displayed coat is: " + firstitemprice);
		
//Mouse over on size of the first item
		WebElement ele3 = driver.findElementByXPath("//span[@class='product-discountedPrice']");
		builder.moveToElement(ele3).perform();
		
//Click on WishList Now and ensure being re-directed to login
		driver.findElementByXPath("//div[contains(@class,'product-actions')]/span/span").click();
		String logintitle = driver.getTitle();
		System.out.println("Back to login page: "+logintitle);
		
//Close Browser
		driver.close();

	}

}
