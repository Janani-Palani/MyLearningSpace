package program;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

public class Day3MakeMyTrip {
	public static ChromeDriver driver;
	
//Launch Nykaa e-commerce site in Chrome Browser and get Title
	public void launchBrowser() {
		System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.makemytrip.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		String title = driver.getTitle();
		if (title.contains("MakeMyTrip"))
			System.out.println("MakeMyTrip launched successfully: " + title);
		else
			System.err.println("MakeMyTrip launch failed");
	}
	

	public static void main(String[] args) throws InterruptedException {
		
//Setting up objects
		Day3MakeMyTrip obj = new Day3MakeMyTrip();
		obj.launchBrowser();
		Actions builder = new Actions(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);

//Click Hotels
		driver.findElementByXPath("(//a[contains(@href,'hotels')])[1]").click();
		System.out.println("Navigated to Search Hotels screen");
		
//Enter city as Goa, and choose Goa, India 
		WebElement eleCity = driver.findElementById("city");
		builder.moveToElement(eleCity).click().perform();
		driver.findElementByXPath("//input[contains(@class,'react-autosuggest')]").sendKeys("Goa");
		Thread.sleep(500);
		driver.findElementByXPath("//input[contains(@class,'react-autosuggest')]").sendKeys(Keys.TAB);
		
//Enter Check in date as May 15 and add 5 for end date
		driver.findElementById("checkin").click();
		String startDate = driver.findElementByXPath("(//div[text()='15'])[2]").getText();
		driver.findElementByXPath("(//div[text()='15'])[2]").click();
		int endDate=Integer.parseInt(startDate)+5;
		Thread.sleep(500);
		driver.findElementByXPath("(//div[text()='"+endDate+"'])[2]").click();
		
//Print selected dates
		String checkinDate = driver.findElementByXPath("//p[@data-cy='checkInDate']").getText();
		String checkoutDate = driver.findElementByXPath("//p[@data-cy='checkOutDate']").getText();
		System.out.println("Check-in date selected :"+checkinDate);
		System.out.println("Check-out date selected :"+checkoutDate);

//Click on ROOMS & GUESTS and click 2 Adults and one Children(age 12). Click Apply Button
		driver.findElementById("guest").click();
		Thread.sleep(500);
		WebElement eleAdults = driver.findElementByXPath("(//li[@class='selected'])[1]");
		if(!eleAdults.isSelected())
		{
			driver.findElementByXPath("(//li[@class='selected'])[1]").click();
			System.out.println("Adults selected as 2");
		}
		else System.out.println("Adults defaulted as 2");
		driver.findElementByXPath("//li[@data-cy='children-1']").click();
		WebElement eleChildAge = driver.findElementByXPath("//label[@class='lblAge']/select");
		eleChildAge.click();
		Select selectAge=new Select(eleChildAge);
		selectAge.selectByVisibleText("12");
		System.out.println("1 child added");
		driver.findElementByXPath("//button[text()='APPLY']").click();
		String roomGuestCount = driver.findElementByXPath("//p[@data-cy='roomGuestCount']").getText();
		System.out.println(roomGuestCount);
			
//Click Search button 
		driver.findElementByXPath("//button[text()='Search']").click();
		
//Search Locality as Baga and 5 star in star category
		driver.findElementByXPath("//body[contains(@class,'overlayWholeBlack')]").click();
		driver.findElementByXPath("//div[contains(@id,'locality')]//ul/li[4]//label").click();
		WebElement eleStar = driver.findElementByXPath("//div[contains(@id,'star_category')]//li//label[1]");
		JavascriptExecutor click=(JavascriptExecutor)driver;
		click.executeScript("arguments[0].click();", eleStar);		
		String confirmFilter = driver.findElementByXPath("//ul[@class='appliedFilters']").getText();
		System.out.println("Star category and Locality selected are: "+confirmFilter);
		
//Click on the first resulting hotel, go to the new window and Print hotel name
		driver.findElementById("Listing_hotel_0").click();
		Set<String> setHandles = driver.getWindowHandles();
		List<String> listHandles=new ArrayList<String>(setHandles);
		driver.switchTo().window(listHandles.get(1));
		String hotelTitle = driver.getTitle();
		if(hotelTitle.contains("Acron Waterfront"))
		{
			System.out.println("Navigated to Acron Waterfront hotel page");
		}
		else System.err.println("Hotel navigation failed");
		String hotelName = driver.findElementById("detpg_hotel_name").getText();
		System.out.println("Hotel name: "+hotelName);

//Click MORE OPTIONS link and Select 3Months plan and close 
		driver.findElementByXPath("//span[text()='MORE OPTIONS']").click();
		driver.findElementByXPath("//table[@class='tblEmiOption']//tr[2]/td[@class='textRight']/span").click();
		driver.findElementByClassName("close").click();
		
//Click on BOOK THIS NOW 
		Thread.sleep(500);
		driver.findElementByXPath("//a[text()='BOOK THIS NOW']").click();
		System.out.println("Hotel booking initiated");
		
//Print the Total Payable amount
		String totalPayable = driver.findElementById("revpg_total_payable_amt").getText();
		System.out.println("Payable amount: "+totalPayable);
		
//Close browser
		driver.quit();
		
		

}}