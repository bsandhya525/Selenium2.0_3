import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Dice {

	static WebDriver driver;
	
	public static void main(String[] args) throws InterruptedException {
		
		String searchKeyword="selenium";
		
		driver = new FirefoxDriver();
		
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		driver.get("http://dice.com");
		
		driver.findElement(By.xpath("//*[@id='monetate_lightbox_contentMap']/area[1]")).click();
		
		Thread.sleep(3000L);
		
		driver.findElement(By.xpath("//*[@id='search-field-keyword']")).sendKeys(searchKeyword);
		
		driver.findElement(By.xpath("//*[@id='search-form']/fieldset/div[4]/div/div[1]/button")).click();
		
		int i=1;
		
		while(isElementPresent("//a[text()='"+i+"']"))
		{
			driver.findElement(By.xpath("//a[text()='"+i+"']")).click();
			
			List<WebElement> results = driver.findElements(By.xpath("//div[@id='serp']/div[@class='serp-result-content']"));
			
			for(int result=1;result<results.size();result++)
			{
				String text = results.get(result).getText();
				
				if(text!=null && !text.isEmpty()&& text.length()>0)
				{
					System.out.println(text);
							
					System.out.println("----------------------");
				}
			}
			
			i++;
		}
		

	}
	
	public static boolean isElementPresent(String xpath)
	{
		if(driver.findElements(By.xpath(xpath)).size() > 0)
		{
			return true;
		}
		else{
			return false;
		}
	}

}
