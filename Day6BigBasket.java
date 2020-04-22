package program;

import static org.testng.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day6BigBasket {
	public static ChromeDriver driver;

	public static void main(String[] args) throws InterruptedException {
//Launch BigBasket site
		System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.bigbasket.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		String title = driver.getTitle();
		
		if (title.contains("bigbasket")) {
			System.out.println("BigBasket launched successfully: " + title);
		} else
			fail("BigBasket launch failed");
		
		Day6BigBasket bb=new Day6BigBasket();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Actions builder = new Actions(driver);

//Set location pincode
		driver.findElementByXPath("//span[@class='arrow-marker']").click();
		driver.findElementById("areaselect").sendKeys("600026");
		Thread.sleep(500);
		driver.findElementById("areaselect").sendKeys(Keys.ENTER);
		driver.findElementByXPath("//button[text()='Continue']").click();
		
//Mouse hover on  Shop by Category 
		builder.moveToElement(driver.findElementByXPath("//a[text()=' Shop by Category ']")).perform();
		
//Go to FOODGRAINS, OIL & MASALA --> RICE & RICE PRODUCTS 
		builder.moveToElement(driver.findElementByXPath("(//a[text()='Foodgrains, Oil & Masala'])[2]")).build().perform();
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("(//a[text()='Rice & Rice Products'])[2]")));
		builder.moveToElement(driver.findElementByXPath("(//a[text()='Rice & Rice Products'])[2]")).build().perform();
		
//Click on Boiled & Steam Rice
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("(//a[text()='Boiled & Steam Rice'])[2]")));
		builder.moveToElement(driver.findElementByXPath("(//a[text()='Boiled & Steam Rice'])[2]")).click().build().perform();
		if((driver.getTitle()).contains("Boiled Steam Rice"))
		{
			System.out.println("Navigated to Boiled & Steam Rice products page");
		}
		else fail("Navigation to Boiled & Steam Rice page failed");
		
//Choose the Brand as bb Royal
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//span[text()='bb Royal']")));
		driver.findElementByXPath("//span[text()='bb Royal']").click();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//div[contains(@class,'prod-name')]/h6")));
		int count = 0;
		List<WebElement> eleBrandNames = driver.findElementsByXPath("//div[contains(@class,'prod-name')]/h6");
		Thread.sleep(2000);
		for (WebElement eachBrand : eleBrandNames) {
			if((eachBrand.getText()).equalsIgnoreCase("bb Royal"))
			{
				count++;
			}}
		if(count==eleBrandNames.size())
		{
			System.out.println("bb Royal brand products are displayed");
		}

//Go to Ponni Boiled Rice - Super Premium and select 5kg bag from Dropdown
		String riceType="Ponni Boiled Rice - Super Premium";
		WebElement eleRiceQuantity = driver.findElementByXPath("//a[text()='"+riceType+"']/parent::div/following-sibling::div//button/span");
		if(eleRiceQuantity.getText().contains("5 kg"))
		{
			System.out.println("Rice Quantity as 5kg selected by default");
		}
		else 
		{
			driver.findElementByXPath("//a[text()='"+riceType+"']/parent::div/following-sibling::div//button").click();
			driver.findElementByXPath("//a[text()='"+riceType+"']/parent::div/following-sibling::div//ul//a").click();
			System.out.println("Rice Quantity selected as 5kg");
		}
			
//Print the price of the product
		String riceprodPrice = driver.findElementByXPath("//a[text()='"+riceType+"']/parent::div/following-sibling::div[2]//span[@class='discnt-price']/span").getText();
		System.out.println("Product price of Rice: "+riceprodPrice.replaceAll("\\D", ""));
		
//Click Add button
		driver.findElementByXPath("//a[text()='"+riceType+"']/parent::div/following-sibling::div[2]//div[@class='delivery-opt']//button").click();

//Verify the success message displayed 
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//div[@class='toast-title']")));
		String riceAddedMsg = driver.findElementByXPath("//div[@class='toast-title']").getText();
		System.out.println("Second item added: "+riceAddedMsg);
		driver.findElementByClassName("toast-close-button").click();
		
//Type Dal in Search field and enter
		driver.findElementById("input").sendKeys("Dal");
		driver.findElementByXPath("//button[@qa='searchBtn']").click();
		System.out.println("Search for Dal products");
		Thread.sleep(1000);

//Go to Toor/Arhar Dal and select 2kg & set Qty 2 
		String dalType="Toor/Arhar Dal/Thuvaram Paruppu";
		driver.findElementByXPath("//a[text()='"+dalType+"']/parent::div//following-sibling::div//button").click();
		driver.findElementByXPath("//a[text()='Toor/Arhar Dal/Thuvaram Paruppu']/parent::div//following-sibling::div//ul/li[4]").click();
		System.out.println("Dal Quantity selected as 2kg");
		driver.findElementByXPath("//a[text()='"+dalType+"']/parent::div/following-sibling::div[2]//div[3]//input").clear();
		driver.findElementByXPath("//a[text()='"+dalType+"']/parent::div/following-sibling::div[2]//div[3]//input").sendKeys("2");
		System.out.println("2 Dal products selected");
		
//Print the price of Dal
		String dalPrice = driver.findElementByXPath("//a[text()='"+dalType+"']/parent::div/following-sibling::div[2]//h4/span[2]/span").getText();
		System.out.println("Price of Dal product: "+dalPrice.replaceAll("//D", ""));
		
//Click Add button and verify success message
		driver.findElementByXPath("//a[text()='"+dalType+"']/parent::div/following-sibling::div[2]//div[3]//button").click();
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//div[@class='toast-title']")));
		String dalAddedMsg = driver.findElementByXPath("//div[@class='toast-title']").getText();
		System.out.println("Second item added: "+dalAddedMsg);
		driver.findElementByClassName("toast-close-button").click();
		
//Mouse hover on My Basket 
		Thread.sleep(500);
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//span[text()='My Basket']")));
		builder.moveToElement(driver.findElementByXPath("//span[text()='My Basket']")).click().build().perform();
	
//Validate the Sub Total displayed for the selected items
		List<WebElement> eleProd = driver.findElementsByXPath("//div[@class='container-fluid item-wrap']");
		System.out.println("Total calculation for first set of item selection");
	//method called to verify Total against selected items
		bb.verifyTotal(eleProd);

//Reduce the Quantity of Dal as 1 
		driver.findElementByXPath("//a[text()='bb Popular Toor/Arhar Dal 2 kg Pouch']/ancestor::div[3]/following-sibling::div//button").click();
		Thread.sleep(1000);
		
//Validate the Sub Total for the current items
		List<WebElement> eleReduceProd = driver.findElementsByXPath("//div[@class='container-fluid item-wrap']");
		System.out.println("Total calculation after reduction of an item quantity");
	//method called to verify Total against selected items
		bb.verifyTotal(eleReduceProd);
		
//Close browser
		driver.close();
	}
	
	
//Resuable method to verify Grand total against sub total of selected items
	public void verifyTotal(List<WebElement> eleProducts) throws InterruptedException
	{
		int prodCount = eleProducts.size();
		WebDriverWait wt = new WebDriverWait(driver, 10);
		wt.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//p[text()='Sub Total : ']/span/span")));
		String strTotal = driver.findElementByXPath("//p[text()='Sub Total : ']/span/span").getText();
		double grandTotal = Double.parseDouble(strTotal);
		System.out.println("Grand total: "+grandTotal);
		double splitTotal=0;
		for(int i=0;i<prodCount;i++)
		{
			String strItemPrice = driver.findElementsByXPath("//div[@class='row mrp']/span").get(i).getText();
			double itemPrice = Double.parseDouble(strItemPrice);
			System.out.println("Item "+(i+1)+" price: "+itemPrice);
			Thread.sleep(1000);
			wt.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//div[@class='product-qty ng-binding']")));
			String rawstrQty = driver.findElementsByXPath("//div[@class='product-qty ng-binding']").get(i).getText();
			String[] split = rawstrQty.split("x");
			double itemQty = Double.parseDouble(split[0]);
			System.out.println("Item "+(i+1)+" quantity: "+itemQty);
			splitTotal=splitTotal+(itemPrice*itemQty);
		}
		if(splitTotal==grandTotal)
		{
			System.out.println("Total matches with selected items: "+grandTotal);
		}
		else fail("Total mismatch");
		
	}
}
