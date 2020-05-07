package program;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day17AzureMS {
	public static ChromeDriver driver;

//To check if expected file is downloaded
	public void checkFileDownload(String downloadPath, String fileName) {
	  File file = new File(downloadPath);
	  File[] eachFile = file.listFiles();
	  boolean isFilePresent=false;
	  for (int i = 0; i < eachFile.length; i++) //Traverse through all files in the path specified
	  {
	      if (eachFile[i].getName().equals(fileName)) 
	      {
	    	  isFilePresent=true;
	    	  eachFile[i].delete();//Delete the file that's found
	    	  break;
	      }
	  }
	      if(isFilePresent=false)
	      {
	    	  System.err.println(fileName+" is not present");
	      }
	      else System.out.println(fileName+" is present in the path specified");
	  }
	
	public static void main(String[] args) throws InterruptedException {
		Day17AzureMS obj=new Day17AzureMS();
//1) Launch Azure site
	System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
	System.setProperty("webdriver.chrome.silentOutput", "true");
	driver = new ChromeDriver();
	driver.get("https://azure.microsoft.com/en-in/");
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	String title = driver.getTitle();
	if (title.contains("Microsoft Azure"))
		System.out.println("Microsoft Azure launched successfully: " + title);
	else
		System.err.println("Microsoft Azure launch failed");
	

	Actions build=new Actions(driver);
	WebDriverWait wait=new WebDriverWait(driver,20);
	
//2) Click on Pricing 
	driver.findElementByLinkText("Pricing").click();
	if(driver.findElementByXPath("//h1[text()='Azure pricing']").isDisplayed())
	{
		System.out.println("Navigated to Azure Pricing screen");
	}
	else System.err.println("Azure pricing screen not loaded");
	
//3) Click on Pricing Calculator
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//a[contains(text(),'Pricing calculator')]")));
	driver.findElementByXPath("//a[contains(text(),'Pricing calculator')]").click();
	if(driver.findElementByXPath("//h1[text()='Pricing calculator']").isDisplayed())
	{
		System.out.println("Navigated to Pricing calculator screen");
	}
	else System.err.println("Pricing calculator screen not loaded");
	
//4) Click on Containers
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//button[text()='Containers']")));
	build.moveToElement(driver.findElementByXPath("//button[text()='Containers']")).pause(2000).click().build().perform();
	
//5) Select Container Instances 
	Thread.sleep(1000);
	driver.findElementByXPath("(//button[@title='Container Instances'])[2]").click();
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//div[@class='notification-copy']/div")));
	String notificationmsg = driver.findElementByXPath("//div[@class='notification-copy']/div").getText();
	System.out.println(notificationmsg);
	
//6) Click on Container Instance Added View 
	driver.findElementByXPath("//div[@class='notification-copy']//a[text()='View']").click();
	System.out.println("Contanier Instance estimation process initiated");
	
//7) Select Region as "South India" 
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//input[@placeholder='Container Instances']")));
	driver.findElementByXPath("//select[@aria-label='Region']");
	Select eleRegSelect=new Select(driver.findElementByXPath("//select[@aria-label='Region']"));
	eleRegSelect.selectByVisibleText("South India");
	String selectedRegion = eleRegSelect.getFirstSelectedOption().getText();
	if(selectedRegion.equalsIgnoreCase("South India"))
	{
		System.out.println("South India selected as Region");
	}

//8) Set the Duration as 180000 seconds
	driver.findElementByXPath("//input[@aria-label='Seconds']").sendKeys(Keys.chord(Keys.CONTROL,"a"),"180000");
	Thread.sleep(1000);
	String durationSet = driver.findElementByXPath("//input[@aria-label='Seconds']").getAttribute("value");
	if(durationSet.equalsIgnoreCase("180000"))
	{
		System.out.println("Duration set as 180000 seconds");
	}
	
//9) Select the Memory as 4GB 
	Select eleMemory=new Select(driver.findElementByXPath("//select[@aria-label='Memory']"));
 	eleMemory.selectByVisibleText("4 GB");
 	String selectedMemory = eleMemory.getFirstSelectedOption().getText();
	if(selectedMemory.equalsIgnoreCase("4 GB"))
	{
		System.out.println("4GB selected as Memory");
	}
	
//10) Enable SHOW DEV/TEST PRICING 
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//span[text()='Show Dev/Test Pricing']")));
	build.moveToElement(driver.findElementByXPath("//span[text()='Show Dev/Test Pricing']")).pause(500).click().build().perform();
	System.out.println("Show dev/test pricing is enabled");
	
//11) Select Indian Rupee  as currency 
	Select eleCurrency=new Select(driver.findElementByXPath("//select[@aria-label='Currency']"));
	eleCurrency.selectByValue("INR");
 	String selectedCurrency = eleCurrency.getFirstSelectedOption().getText();
	if(selectedCurrency.contains("Indian Rupee"))
	{
		System.out.println("INR selected as Currency");
	}
	
//12) Print the Estimated monthly price 
	Thread.sleep(1000);
	String monthlyEstimatedPrice = driver.findElementByXPath("//h3[text()='Estimated monthly cost']/ancestor::div[1]/following-sibling::div/div[2]//span/span").getText();
	System.out.println("Monthly Estimated price: "+monthlyEstimatedPrice);
	
//13) Click on Export to download the estimate as excel 
	driver.findElementByXPath("//button[contains(@class,'export-button')]").click();
	Thread.sleep(3000);

//14)Verify the downloded file in the local folder 
	System.out.println("*Container instance exported file check*");
	obj.checkFileDownload("C:\\Users\\Janani\\Downloads", "ExportedEstimate.xlsx");
	
//15) Navigate to Example Scenarios and Select CI/CD for Containers
	Thread.sleep(2000);
	JavascriptExecutor js = ((JavascriptExecutor) driver);
	js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//a[text()='Example Scenarios']")));
	driver.findElementByXPath("//a[text()='Example Scenarios']").click();
	build.moveToElement(driver.findElementByXPath("//span[text()='CI/CD for Containers']/ancestor::button[1]")).pause(1000).click().build().perform();
	System.out.println("CI/CD for Containers is selected");
	
//16) Click Add to Estimate
	Thread.sleep(2000);
	driver.findElementByXPath("//button[text()='Add to estimate']").click();
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//a[text()='CI/CD for containers']")));
	if(driver.findElementByXPath("//a[text()='CI/CD for containers']").isDisplayed())
	{
		System.out.println("CI/CD for Containers added to estimates");
		System.out.println("CI/CD estimation process initiated");
	}
	
//17) Change the Currency as Indian Rupee
	Thread.sleep(2000);
	js.executeScript("window.scrollBy(0,250)","");
	Select currencySelect=new Select(driver.findElementByXPath("//select[@aria-label='Currency']"));
	currencySelect.selectByValue("INR");
 	String currency = currencySelect.getFirstSelectedOption().getText();
	if(currency.contains("Indian Rupee"))
	{
		System.out.println("INR selected as Currency");
	}
	
//18) Enable SHOW DEV/TEST PRICING
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//span[text()='Show Dev/Test Pricing']")));
	build.moveToElement(driver.findElementByXPath("//span[text()='Show Dev/Test Pricing']")).pause(500).click().build().perform();
	System.out.println("Show dev/test pricing is enabled");
	
//19) Export the Estimate 
	driver.findElementByXPath("//button[contains(@class,'export-button')]").click();
	Thread.sleep(3000);

//20)Verify the downloded file in the local folder 
	System.out.println("*CI/CD for Containers exported file check*");
	obj.checkFileDownload("C:\\Users\\Janani\\Downloads", "ExportedEstimate.xlsx");
	
}
}