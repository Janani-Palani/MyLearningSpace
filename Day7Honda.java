package program;

import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day7Honda {
	public static ChromeDriver driver;

	public static void main(String[] args) throws InterruptedException {
//Launch Honda site
		System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.honda2wheelersindia.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		String title = driver.getTitle();

		if (title.contains("Honda")) {
			System.out.println("Honda launched successfully: " + title);
		} else
			fail("Honda launch failed");

		Day7Honda honda = new Day7Honda();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Actions builder = new Actions(driver);

//Close the pop-up
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//div[@class='modal-body']")));
		driver.findElementByXPath("//div[@class='modal-body']/button").click();
		
//Call method to select Dio scooter and return Displacement value
		double dioDisplacement = getDisplacement("dio");
			System.out.println("Dio Displacement value is: "+dioDisplacement);
			Thread.sleep(1000);
			
//Call method to select Activa125 scooter and return Displacement value
			double activa125Displacement = getDisplacement("activa125");
			System.out.println("Active 125 displacement value is: "+activa125Displacement);
			
//Compare Displacement of Dio and Activa 125 and print the Scooter name having better Displacement
			if(dioDisplacement>activa125Displacement)
			{
				System.out.println("Displacement value of Dio is better");
			}
			else System.out.println("Displacement value of Activa 125 is better");
			
//Click FAQ from Menu  
			driver.findElementByXPath("//a[text()='FAQ']").click();
			if(driver.getTitle().contains("FAQ"))
			{
				System.out.println("FAQ is loaded");
			}
			else System.err.println("FAQ loading failed");
			
//Click Activa 125 BS-VI under Browse By Product
			wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//h6[text()='Browse by Product']")));
			driver.findElementByXPath("//h6[text()='Browse by Product']/following-sibling::div//a[text()='Activa 125 BS-VI']").click();
			Thread.sleep(1000);

//Click  Vehicle Price 
			wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//a[text()=' Vehicle Price']")));
			driver.findElementByXPath("//a[text()=' Vehicle Price']").click();
		
//Make sure Activa 125 BS-VI selected and click submit
			if(driver.findElementByXPath("//option[@selected='selected' and text()='Activa 125 BS-VI']").isSelected())
			{
				System.out.println("Activa 125 is selected");
			}
			else System.err.println("Activa 125 is not selected");
			Thread.sleep(500);
			WebElement eleClickCart = driver.findElementByXPath("(//button[text()='Submit'])[6]");
			JavascriptExecutor click = (JavascriptExecutor) driver;
			click.executeScript("arguments[0].click();", eleClickCart);
			
//click the price link	
			wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//td[3]/a[text()='Click here to know the price of Activa 125 BS-VI.']")));
			driver.findElementByXPath("//td[3]/a[text()='Click here to know the price of Activa 125 BS-VI.']").click();
			
//Go to the new Window and select the state as Tamil Nadu and  city as Chennai
			Set<String> setHandle = driver.getWindowHandles();
			List<String> listHandle=new ArrayList<String>(setHandle);
			driver.switchTo().window(listHandle.get(1));
			if(driver.getTitle().contains("Price"))
			{
				System.out.println("Price page of Activa 125 is loaded");
			}
			wait.until(ExpectedConditions.visibilityOf(driver.findElementById("StateID")));
			Select state=new Select(driver.findElementById("StateID"));
			state.selectByVisibleText("Tamil Nadu");
			Thread.sleep(1000);
			Select city=new Select(driver.findElementById("CityID"));
			city.selectByVisibleText("Chennai");
			Thread.sleep(1000);
			
//Click Search
			driver.findElementByXPath("//button[text()='Search']").click();
			wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//td[text()='Chennai']")));
			System.out.println("Price table loaded for Chennai city");
			
//Print all the 3 models and their prices
			
			Map<String,String> map=new LinkedHashMap<String,String>();
			List<WebElement> eleList = driver.findElementsByXPath("//table[@id='gvshow']/tbody/tr/td[2]");
			List<WebElement> eleModel = driver.findElementsByXPath("//table[@id='gvshow']/tbody/tr/td[contains(text(),'ACTIVA')]");
			List<WebElement> elePrice = driver.findElementsByXPath("//table[@id='gvshow']/tbody/tr/td[contains(text(),'Rs')]");
			for(int i=0;i<eleList.size();i++)
			{
				String modelType= eleModel.get(i).getText();
				String modelPrice=elePrice.get(i).getText();
				map.put(modelType, modelPrice);
			}
			for (Entry<String,String> keyValue : map.entrySet()) 
			{
				System.out.println("Model with prices as below:");
				System.out.println(keyValue.getKey()+" - "+keyValue.getValue());
			}
			
//Close browser
			driver.quit();
}
	
//Method to get Displacement value depending on scooter make
	public static double getDisplacement(String scooterMake) throws InterruptedException
	{
		WebDriverWait wt=new WebDriverWait(driver,10);
		Actions build=new Actions(driver);
		
	////Click on scooters and select scooter make
		wt.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//a[text()='Scooter']")));
		driver.findElementByXPath("//a[text()='Scooter']").click();
		wt.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("(//a[contains(@href,'"+scooterMake+"')])[1]/img")));
		driver.findElementByXPath("(//a[contains(@href,'"+scooterMake+"')])[1]/img").click();
		
	////Click on Specifications and mouseover on ENGINE
		wt.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//a[text()='Specifications']")));
		driver.findElementByXPath("//a[text()='Specifications']").click();
		Thread.sleep(500);
		wt.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//a[text()='ENGINE']")));
		build.moveToElement(driver.findElementByXPath("//a[text()='ENGINE']")).perform();
		
	////Get Displacement value
		wt.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//span[text()='Displacement']/following-sibling::span")));
		String rawDisplacement = driver.findElementByXPath("//span[text()='Displacement']/following-sibling::span").getText();
		double actualDisplacement=Double.parseDouble(rawDisplacement.replaceAll("[a-z]+", ""));
		return actualDisplacement;
	}

}
