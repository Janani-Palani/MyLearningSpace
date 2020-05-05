package program;

import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.ToolTipManager;
import javax.swing.plaf.ToolTipUI;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day14ZalandoUK {
	public static ChromeDriver driver;
	
//Method for bootstrap drop-down
	public void dropdownSelect(List<WebElement> eleSelect, String strSelect) throws InterruptedException
	{
		for(int i=0;i<eleSelect.size();i++)
		{
			if (eleSelect.get(i).getAttribute("innerHTML").contains(strSelect))
			{
				eleSelect.get(i).click();
				System.out.println(eleSelect.get(i).getAttribute("innerHTML")+" selected");
				break;
			}
		}
		Thread.sleep(1000);
		driver.findElementByXPath("//button[text()='Save']").click();
	}

	
	public static void main(String[] args) throws InterruptedException {
		Day14ZalandoUK obj=new Day14ZalandoUK();
//Launch Zalando site
	System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
	System.setProperty("webdriver.chrome.silentOutput", "true");
	driver = new ChromeDriver();
	driver.get("https://www.zalando.com/");
	
//Handle alert
	Alert alert = driver.switchTo().alert();
	System.out.println("Alert message: "+alert.getText());
	alert.accept();
	
//Check if page load is successful
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	String title = driver.getTitle();
	if (title.contains("Zalando"))
		System.out.println("Zalando launched successfully: " + title);
	else
		System.err.println("Zalando Study Abroad launch failed");
	
//Mouse over on Colleges and click MS in Computer Science &Engg under MS Colleges 
	Actions build=new Actions(driver);
	WebDriverWait wait=new WebDriverWait(driver,20);
	
//Click on Zalando.uk 
	driver.findElementByXPath("//a[text()='Zalando.uk']").click();
	
//Click Women--> Clothing and click Coat  
	Thread.sleep(500);
	driver.findElementByXPath("//span[text()='Women']").click();
	driver.findElementByXPath("//span[text()='Clothing']").click();
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("(//a[text()='Coats'])[3]")));
	driver.findElementByXPath("(//a[text()='Coats'])[3]").click();
	Thread.sleep(1000);
	if(driver.findElementByXPath("//h1/span").getText().contains("Women's Coats"))
	{
		System.out.println("Screen navigated to Women's coats");
	}
	
//Choose Material as cotton (100%) and Length as thigh-length 
	Thread.sleep(2000);
	  try 
	  {
		  wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//button[contains(text(),'OK')]")));
		  driver.findElementByXPath("//button[contains(text(),'OK')]").click();
	  }
	 catch(Exception e)
	  {
		 System.out.println("Pop-up did not occur");
	  }
	finally 
	{
		driver.findElementByXPath("//span[text()='Material']").click();
		List<WebElement> eleMaterials = driver.findElementsByXPath("//button[contains(@aria-label,'Material')]/parent::div//ul/li/span");
		obj.dropdownSelect(eleMaterials, "cotton (100%)");
		Thread.sleep(1000);
		driver.findElementByXPath("//span[text()='Length']").click();
		List<WebElement> eleLength = driver.findElementsByXPath("//span[text()='Length']/ancestor::button/following-sibling::div//ul/li/span");
		obj.dropdownSelect(eleLength, "thigh-length");

//Click on Q/S designed by MANTEL - Parka coat 
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//div[contains(text(),'MANTEL')]/ancestor::div[3]//img")));
		driver.findElementByXPath("//div[contains(text(),'MANTEL')]/ancestor::div[3]/a").click();
		Thread.sleep(1000);
		if(driver.getTitle().contains("MANTEL"))
		{
			System.out.println("Navigated to Q/S Designed by MANTEL Parka COAT");
		}
	
//Check the availability for Color as Olive and Size as 'M' and if the previous preference is not available, check  availability for Color Navy and Size 'M' 
		driver.findElementByXPath("(//a[contains(@href,'parka-olive')])[2]//img").click();
		Thread.sleep(1000);
		if(driver.findElementByXPath("//h2[text()='Out of stock']").isDisplayed())
		{
			driver.findElementByXPath("(//a[contains(@href,'parka')]//img[@alt='navy'])[2]").click();
			System.out.println("Color navy selected as olive is out of stock");
		}
		else 
		{
			driver.findElementByXPath("(//a[contains(@href,'parka-olive')])[2]//img").click();
			System.out.println("Color olive selected");
		}
		driver.findElementByXPath("//span[text()='Choose your size']").click();
		Thread.sleep(1000);
		driver.findElementByXPath("//span[text()='M']/ancestor::li[1]").click();
		
//Add to bag only if Standard Delivery is free 
		if(driver.findElementByXPath("//span[text()='Standard delivery']/parent::div//span[text()='Free']").isDisplayed())
		{
			System.out.println("Standard delivery is free");
			driver.findElementByXPath("//span[text()='Add to bag']/parent::button").click();
			System.out.println("Item added to bag");
		}
		else fail("Standard delivery charge is applied");
		
//Mouse over on Your Bag and Click on "Go to Bag" 
		build.moveToElement(driver.findElementByXPath("(//a[contains(@href,'cart')])[3]")).perform();
		Thread.sleep(1000);
		driver.findElementByXPath("//div[text()='Go to bag']/parent::a").click();
		Thread.sleep(1000);
		String coatColor = driver.findElementByXPath("//a[contains(text(),'MANTEL - Parka')]").getText();
		if(coatColor.contains("navy"))
		{
			System.out.println("MANTEL Parka Navy coat is displayed in bag");
		}
		else System.out.println("MANTEL Parka Olive coat is displayed in bag");
		
//Capture the Estimated Deliver Date and print 
		String rawDeldate = driver.findElementByXPath("//div[@data-id='delivery-estimation']/span").getText();
		String[] split = rawDeldate.split(",");
		String estimatedDelDate = split[0];
		System.out.println("Estimated delivery date: "+estimatedDelDate);
		
//Mouse over on FREE DELIVERY & RETURNS*, get the tool tip text and print 
		build.moveToElement(driver.findElementByXPath("//a[contains(text(),'Free delivery')]/parent::span")).pause(1000).perform();
		String toolTipText = driver.findElementByXPath("//a[contains(text(),'Free delivery')]/parent::span").getAttribute("title");
		System.out.println("Retrieved tool tip message: "+toolTipText);
		Thread.sleep(1000);
		build.moveToElement(driver.findElementByXPath("//a[contains(text(),'Free delivery')]/parent::span")).click().build().perform();
		
//Click on Start chat in the Start chat and go to the new window
		Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//span[text()='Start chat']/parent::button")));
		JavascriptExecutor click =(JavascriptExecutor) driver;
		click.executeScript("arguments[0].click();", driver.findElementByXPath("//span[text()='Start chat']/parent::button"));
		Set<String> setHandle = driver.getWindowHandles();
		List<String> listHandle=new ArrayList<String> (setHandle);
		driver.switchTo().window(listHandle.get(1));
		
//Enter you first name and a dummy email and click Start Chat
		driver.findElementById("prechat_customer_name_id").sendKeys("Janani");
		driver.findElementById("prechat_customer_email_id").sendKeys("jann@cactus.com");
		driver.findElementById("prechat_submit").click();
		
//Type Hi, click Send and print the reply message and close the chat window
		wait.until(ExpectedConditions.visibilityOf(driver.findElementById("liveAgentChatTextArea")));
		driver.findElementById("liveAgentChatTextArea").sendKeys("Hi");
		Thread.sleep(500);
		driver.findElementByXPath("//button[text()='Send']").click();
		String chatReplyMsg = driver.findElementByXPath("//span[@class='messageText']").getText();
		System.out.println("Chat box replay message: "+chatReplyMsg);	
	}
}
}