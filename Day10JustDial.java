package program;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

public class Day10JustDial {

//Write data from Map to excel
public void writeExcel(Map<String, String> map) throws IOException {
//Create a excel in user directory path
String filePath = System.getProperty("user.dir") + "\\testdata\\Day10JustDial.xlsx";
System.out.println(filePath);
File fis = new File(filePath);
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("Output");
	for (Entry<String, String> eachEntry : map.entrySet()) {
		for (int k = 0; k < map.size(); k++) {
			sheet.createRow(k).createCell(0).setCellValue(eachEntry.getKey());
			sheet.getRow(k).createCell(1).setCellValue(eachEntry.getValue());
		}
	}
	FileOutputStream fos = new FileOutputStream(fis);
	workbook.write(fos);
}

//Decipher Phone number
public Map<String, String> decipherPhoneNumber() {
	Map<String, String> decipher = new LinkedHashMap<String, String>();
	decipher.put("0", "acb");
	decipher.put("1", "yz");
	decipher.put("2", "wx");
	decipher.put("3", "vu");
	decipher.put("4", "ts");
	decipher.put("5", "rq");
	decipher.put("6", "po");
	decipher.put("7", "nm");
	decipher.put("8", "lk");
	decipher.put("9", "ji");
	decipher.put("+", "dc");
	decipher.put("(", "fe");
	decipher.put(")", "hg");
	decipher.put("-", "ba");
	return decipher;
}

public static void main(String[] args) throws InterruptedException, InvalidFormatException, IOException {

	Day10JustDial jd = new Day10JustDial();
	
//Launch Just Dial
System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
ChromeOptions options = new ChromeOptions();
options.addArguments("--disable-notifications");
DesiredCapabilities cap = new DesiredCapabilities();
cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
options.merge(cap);
ChromeDriver driver = new ChromeDriver(options);
driver.get("https://www.justdial.com/");
driver.manage().window().maximize();
driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
String title = driver.getTitle();
if (title.contains("Justdial")) 
{
System.out.println("Justdial launched successfully: " + title);
} else
	System.err.println("Justdial launch failed");

WebDriverWait wait = new WebDriverWait(driver, 20);
Actions builder = new Actions(driver);

//Click Auto Care in the left menu 
Thread.sleep(2000);
driver.findElementByXPath("//span[text()='Auto care']").click();
if (driver.getTitle().contains("Auto Care")) 
{
System.out.println("Auto Care screen loaded");
}

//Click Car repair
Thread.sleep(2000);
wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//span[text()='Car Repair']")));
driver.findElementByXPath("//span[text()='Car Repair']").click();
if (driver.getTitle().contains("List of Car Repair")) {
System.out.println("Car Repair selected");
}

//Click Car Brand as Hyundai and make as Hyundai xcent
Thread.sleep(2000);
wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//span[text()='Hyundai']")));
builder.moveToElement(driver.findElementByXPath("//span[text()='Hyundai']")).click().build().perform();
Thread.sleep(2000);
driver.findElementByXPath("//span[text()='Hyundai Xcent']").click();
if (driver.getTitle().contains("Hyundai Xcent")) {
System.out.println("Hyundai Xcent car service page is loaded");
}
Thread.sleep(2000);
try {
	wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath(
			"(//section[@class='jpbg'])[26]//span[text()='Car Repair Services Hyundai Xcent']")));
driver.findElementByXPath("(//section[@class='jpbg']/span[contains(@onclick,'closePopUp')])[4]").click();
} catch (Exception e) {
	System.out.println("Pop-up did not occur");
}

//Identify all the Service Center having Ratings >=4.5 and Votes >=50
Thread.sleep(5000);
List<WebElement> eleRatings = driver.findElementsByXPath("//p[@class='newrtings ']//span[@class='green-box']");
Map<String, String> map = new LinkedHashMap<String, String>();
String phoneNumber = "";
String storeName = "";
Map<String, String> decipherPhone = jd.decipherPhoneNumber();
for (int i = 0; i < eleRatings.size(); i++) {
	String strRating = eleRatings.get(i).getText();
	double ratingVal = Double.parseDouble(strRating);
	if (ratingVal >= 4.5) 
	{
		String strVote = driver.findElementByXPath("//span[text()='" + ratingVal + "']/following-sibling::span[2]").getText();
		int intVote = Integer.parseInt(strVote.replaceAll("\\D", ""));
			if (intVote >= 50) 
			{
//Save all the Service Center name and Phone number matching the above condition in excel 
				storeName = driver.findElementByXPath("//span[text()='" + ratingVal + "']/ancestor::div[1]//h2[@class='store-name']//span//span").getText();
				List<WebElement> elePhoneNumber = driver.findElementsByXPath("//span[contains(text(),'" + intVote
				+ "')]/ancestor::div[1]//h2[@class='store-name']/following-sibling::p[2]/span/span");
					for (int j = 0; j < elePhoneNumber.size(); j++) 
					{
					String fullCode = elePhoneNumber.get(j).getAttribute("class");
					String lastCode = fullCode.substring(14);
					System.out.println(lastCode);
					for (Entry<String, String> eachPhone : decipherPhone.entrySet()){
						if (eachPhone.getValue().equalsIgnoreCase(lastCode)) {
							phoneNumber = phoneNumber + (eachPhone.getKey());
						}
						}}
				//Assign key value pair to Map
				map.put(storeName, phoneNumber);
				phoneNumber = "";
		}
	}
}

//To print map values just for verification purpose
for (Entry<String, String> eachEntry : map.entrySet()) 
{
	System.out.println(eachEntry.getKey() + "--->" + eachEntry.getValue());
		}

//Calling method to write map data to excel
		jd.writeExcel(map);
		
//Close browser
		driver.close();
	}
}
