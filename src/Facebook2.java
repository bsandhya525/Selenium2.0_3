import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;



public class Facebook2 {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new FirefoxDriver();
		
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		driver.get("http://facebook.com");
		
		driver.findElement(By.xpath("//*[@id='email']")).sendKeys("sandhya.boppana@gmail.com");
		
		driver.findElement(By.xpath("//*[@id='pass']")).sendKeys("fk8#K4214");
		
		driver.findElement(By.xpath("//*[@id='pass']")).sendKeys(Keys.ENTER);
		
		driver.findElement(By.xpath("//div[@id='userNav']/ul/li[1]/a")).click();
		
		driver.findElement(By.xpath("//div[@id='fbTimelineHeadline']/div[2]/div[1]/div[1]/a[3]")).click();
		
		List<WebElement> friends = driver.findElements(By.xpath("//div[@class='fsl fwb fcb']/a"));
		
		System.out.println("Friends:"+friends.size());
		
		int newFriends = friends.size();
		int oldFriends = 0;
		
		while(newFriends != oldFriends)
		{
			oldFriends = newFriends;
			
			WebElement lastElm = friends.get(friends.size()-1);
			
			int y= lastElm.getLocation().y;
			
			JavascriptExecutor js = (JavascriptExecutor)driver;
			
			js.executeScript("window.scrollTo(0,"+y+")");
			
			Thread.sleep(3000);
			
			friends = driver.findElements(By.xpath("//div[@class='fsl fwb fcb']/a"));
			
			newFriends = friends.size();
		}
		
		for(int i=0;i<friends.size();i++)
		{
			String friend = friends.get(i).getText();
			
			System.out.println(friend);
		}
		


	}

}
