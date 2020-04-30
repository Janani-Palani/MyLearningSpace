package program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day12Carwale {

	public static void main(String[] args) throws InterruptedException {
//Launch Carwale site
	ChromeOptions options=new ChromeOptions();
	options.addArguments("--disable-notifications");
	System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
	ChromeDriver driver = new ChromeDriver();
	driver.get("https://www.carwale.com/");
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	String title = driver.getTitle();
	if (title.contains("CarWale"))
		System.out.println("CarWale launched successfully: " + title);
	else
		System.err.println("CarWale launch failed");
	
//Click on Used 
	driver.findElementByXPath("//li[@data-tabs='usedCars']").click();
	
//Select the City as Chennai
	WebDriverWait wait=new WebDriverWait(driver,10);
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//div[@class='used-cars-search']")));
	driver.findElementByXPath("//div[@class='used-cars-search']/input").click();
	driver.findElementByXPath("//input[@id='usedCarsList']").sendKeys("Chennai");
	Thread.sleep(1000);
	driver.findElementByXPath("//input[@id='usedCarsList']").sendKeys(Keys.ENTER);

//Select budget min (8L) and max(12L) and Click Search 	
	Thread.sleep(1000);
	JavascriptExecutor click =(JavascriptExecutor) driver;
	click.executeScript("arguments[0].click();", driver.findElementByXPath("//span[text()='Choose your budget']"));
	click.executeScript("arguments[0].click();", driver.findElementByXPath("//li[text()='8 Lakh']"));
	//Thread.sleep(500);
	click.executeScript("arguments[0].click();", driver.findElementByXPath("//input[@id='maxInput']/parent::div/following-sibling::ul[2]/li[text()='12 Lakh']"));
	driver.findElementByXPath("//button[@id='btnFindCar']").click();
	if(driver.getTitle().contains("Used Cars"))
	{
		System.out.println("Navigated to used cars in Chennai");
	}
	if(driver.findElementByXPath("//span[text()='8L - 12L']").isDisplayed())
	{
		System.out.println("Budget filtered with 8-12L");
	}
	
//Handling popup
	driver.findElementByXPath("//input[@id='placesQuery']").sendKeys("Chennai");
	driver.findElementByXPath("//a[@cityname='chennai,tamilnadu']").click();
	
//Select Cars with Photos under Only Show Cars With
	driver.findElementByName("CarsWithPhotos").click();
	if(driver.findElementByXPath("//span[@id='filterbyadditional']/span[text()=' Cars with Photos ']").isDisplayed())
	{
		System.out.println("Cars with photos are displayed");
	}
	
//Select Manufacturer as "Hyundai" --> Creta 
	click.executeScript("arguments[0].click();", driver.findElementByXPath("//li[@data-manufacture-en='Hyundai']"));
	wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//span[text()='Creta']")));
	click.executeScript("arguments[0].click();", driver.findElementByXPath("//span[text()='Creta']"));
	System.out.println("Hyundai creta model selected");

//Select Fuel Type as Petrol 
	click.executeScript("arguments[0].click();", driver.findElementByXPath("//h3[contains(text(),'Fuel Type')]"));
	//wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//span[text()='Petrol']")));
	Thread.sleep(2000);
	click.executeScript("arguments[0].click();", driver.findElementByXPath("//span[text()='Petrol']/parent::li"));
	//Actions builder=new Actions(driver);
	//builder.moveToElement(driver.findElementByXPath("//span[text()='Petrol']/parent::li")).click().build().perform();
	System.out.println("Fuel type selected as petrol");
	
//Select Best Match as "KM: Low to High"
	Thread.sleep(2000);
	driver.findElementById("sort").click();
	Select eleSort=new Select(driver.findElementById("sort"));
	eleSort.selectByVisibleText("KM: Low to High");
	if(driver.findElementByXPath("//option[text()='KM: Low to High']").isSelected())
	{
		System.out.println("KM Low to High sort is selected");
	}
	
//Validate the Cars are listed with KMs Low to High 
	Map<Integer,String> leastKMmap=new LinkedHashMap<Integer,String>();
	List<WebElement> eleKM = driver.findElementsByXPath("//span[contains(@class,'slkms vehicle')]");
	List<Integer> vehicleKM=new ArrayList<Integer>();
	for(int i=0;i<eleKM.size();i++)
	{
		String rawKM = eleKM.get(i).getText();
		int intKM = Integer.parseInt(rawKM.replaceAll("\\D", ""));
		vehicleKM.add(intKM);
		leastKMmap.put(intKM, rawKM);
	}
//Create copy of the Integer List, sort and compare two lists
	List<Integer> sortedvehicleKM=new ArrayList<Integer>(vehicleKM);
	Collections.sort(sortedvehicleKM);
	if(vehicleKM.equals(sortedvehicleKM))
	{
		System.out.println("Hyundai Creta cars are sorted by KM: Low to high");
	}
	else System.err.println("Hyundai Creta cars are NOT sorted by KM: Low to high");

//Add the least KM ran car to Wishlist 
	Integer leastKM = sortedvehicleKM.get(0);
	String strLeastKM="";
	for (Entry<Integer, String> eachEntry : leastKMmap.entrySet())
	{
		if(eachEntry.getKey().equals(leastKM))
		{
			strLeastKM = eachEntry.getValue();
		}
	}
	System.out.println("Least KM utilised by Hyundai Creta car: "+strLeastKM);
	Thread.sleep(1000);
	WebElement eleCartoWishlist = driver.findElementByXPath("(//span[text()='"+strLeastKM+"']//ancestor::div[@class='stock-detail']//span[contains(@class,'shortlist')])[2]");
	click.executeScript("arguments[0].click();", eleCartoWishlist);
	
//Go to Wishlist and Click on More Details
	Thread.sleep(2000);
	driver.findElementByXPath("//li[@data-action='ShortList&CompareWindow_Click']").click();
	String carWithLeastKM = driver.findElementByXPath("//a[@data-action='ShortListedCar_Click']/span").getText();
	System.out.println("Car that utolised least KM: "+carWithLeastKM);
	Thread.sleep(1000);
	driver.findElementByXPath("//a[text()='More details »']").click();
	
//Window switch
	Set<String> setHandle = driver.getWindowHandles();
	List<String> listHandle=new ArrayList<String>(setHandle);
	driver.switchTo().window(listHandle.get(1));
	
//Print all the details under Overview  
	Thread.sleep(3000);
	Map<String,String> carDeets=new LinkedHashMap<String,String>();
	List<WebElement> eleDesc = driver.findElementsByXPath("//div[contains(@class,'overview-list')]//div[contains(@class,'text-light-grey')]");
	List<WebElement> eleValue = driver.findElementsByXPath("//div[contains(@class,'dark-text')]");
	for(int i=0;i<eleDesc.size();i++)
	{
		String strDeet = eleDesc.get(i).getText();
		String strDeetValue = eleValue.get(i).getText();
		carDeets.put(strDeet, strDeetValue);
	}
	System.out.println("Overview details of "+carWithLeastKM+" are as follows:");
	for (Entry<String, String> eachDeet : carDeets.entrySet())
	{
		System.out.println(eachDeet.getKey()+" -> "+eachDeet.getValue());
	}
	Thread.sleep(2000);
	
//Close browser
	driver.quit();
		
	}

}
