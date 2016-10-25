import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Popups {

	static WebDriver driver;
	
	public static void main(String[] args) throws InterruptedException {
		
		String url="http://qtpselenium.com/test/unpredictable.php";
		
		driver = new FirefoxDriver();
		
		driver.get(url);
		
		Thread.sleep(3000);
		
		generatePopup();
		
		closePopupIfPresent();
		
		
	}

	
	
	
	public static void closePopupIfPresent()
	{
		Set<String> windows  = driver.getWindowHandles();
		
		System.out.println("Windows:"+windows.size());
		
		if(windows.size()== 2)
		{
			Iterator<String> itr = windows.iterator();
			
			String main = itr.next();
			
			String popup = itr.next();
			
			driver.switchTo().window(popup);
			
			driver.close();
			
			driver.switchTo().window(main);
		}
	}
	
	
	public static void generatePopup()
	{
		Set<String>  winds = driver.getWindowHandles();
		
		System.out.println(winds.size());
		
		if(winds.size()!= 2)
		{
			driver.findElement(By.xpath("//body")).sendKeys(Keys.F5);
			
			winds = driver.getWindowHandles();
			
			System.out.println(winds.size());
		}
		
	}
}
