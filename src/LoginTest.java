import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class LoginTest {
	
	String classAerr="The email and password you entered don't match.";
	String classBerr="Please enter your email.";
	String classCerr="Please enter your password.";
	String classDerr="Sorry, Google doesn't recognize that email.";
	
	public WebDriver driver;
	
	String errMessage;
	
	@Test (dataProvider="getData")
	public void doLogin(String browser,String username,String password,String expected) throws InterruptedException
	{
		if(browser.equals("Mozilla"))
		{
			driver = new FirefoxDriver();
		}
		else if(browser.equals("Chrome"))
		{
			System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
			
			driver = new ChromeDriver();
		}
		else if(browser.equals("IE"))
		{
			System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer_x64_2.49.0\\IEDriverServer.exe");
			
			driver = new InternetExplorerDriver();
		}
		
		driver.get("http://gmail.com");
		
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		WebDriverWait wait = new WebDriverWait(driver,20);
		
		Thread.sleep(3000L);
		
		if(driver.findElements(By.xpath("//a[@id='gmail-sign-in']")).size() > 0)
				driver.findElement(By.xpath("//a[@id='gmail-sign-in']")).click();
		
		Thread.sleep(3000L);
		
		System.out.println("Enetring Username");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='Email']")));
		
		driver.findElement(By.xpath("//*[@id='Email']")).sendKeys(username);
		
		System.out.println("Clicking Next button");
		
		driver.findElement(By.xpath("//*[@id='next']")).click();
		
		Thread.sleep(2000L);
		
		errMessage = null;
		
		if(isErrorPresent()== false)
		{
			System.out.println("Enetring Password");
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='Passwd']")));
			
			driver.findElement(By.xpath("//*[@id='Passwd']")).sendKeys(password);
			
			if(driver.findElements(By.xpath("//*[@id='signIn']")).size() > 0)
			{
				System.out.println("Clicking signIn button");
				(driver.findElements(By.xpath("//*[@id='signIn']"))).get(0).click();
			}
		}
		
		Thread.sleep(2000L);
		
		List<WebElement> errors = driver.findElements(By.xpath("//*[starts-with(@id,'errormsg_')]"));
		
		if(errors != null && errors.size()>0)
		{
			System.out.println("errors not null:"+errors.size());
		  			
		   if(errors.get(0).getText() != null && errors.get(0).getText().length() > 0)
			   	errMessage = errors.get(0).getText();
		   else
			   errMessage = errors.get(1).getText();
		}
		
		System.out.println("Actual err:"+errMessage);
		
		if(expected.equals("ClassA"))
		{
			Assert.assertEquals(errMessage, classAerr);
		}
		else if(expected.equals("ClassB"))
		{
			Assert.assertEquals(errMessage, classBerr);
		}
		else if(expected.equals("ClassC"))
		{
			Assert.assertEquals(errMessage, classCerr);
		}
		else if(expected.equals("ClassD"))
		{
			Assert.assertEquals(errMessage, classDerr);
		}
		else if(expected.equals("ClassE"))
		{
			if(errMessage != null && errMessage.isEmpty())
				errMessage = null;
			Assert.assertNull(errMessage);
		}
		Thread.sleep(2000L);
		
		driver.quit();
	}

	
	public boolean isErrorPresent()
	{
		System.out.println("Inside isErrorPresent method");
		boolean result = false;
		List<WebElement> errors = driver.findElements(By.xpath("//*[starts-with(@id,'errormsg_')]"));
		
		if(errors != null && errors.size() > 0)
		{
			System.out.println("Errors not null");
			if(errors.get(0).getText() != null && errors.get(0).getText().length() > 0)
			{
				System.out.println("Error text:"+errors.get(0).getText());
			   	result= true;
			}
		   else if(errors.size()> 1 && errors.get(1).getText() != null && errors.get(1).getText().length() > 0)
		   {
			   System.out.println("Error text:"+errors.get(1).getText());
			   result= true;
		   }
		   else
		   {
			   result= false;
		   }
		}
		else
		{
			result= false;
		}
		
		return result;
	}
	
	@DataProvider
	public static Object[][] getData()
	{
		Object[][] data;
		
		XSLX_Reader loginXLSX = new XSLX_Reader(System.getProperty("user.dir")+"/Login.xlsx");
		
		int rows = loginXLSX.getRowCount("Login");
		
		int cols = loginXLSX.getColumnCount("Login");
		
		data = new Object[rows-1][cols];
		
		for(int rowNum=2;rowNum<=rows;rowNum++)
		{
			for(int colNum=0;colNum<cols;colNum++)
			{
				data[rowNum-2][colNum]=loginXLSX.getCellData("Login",colNum+1,rowNum);
				
			}
		}
		
		return data;
	}
}
