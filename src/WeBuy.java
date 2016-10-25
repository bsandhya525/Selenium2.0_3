import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;


public class WeBuy {

	@Test
	public void testProductPurchaseProcess() throws InterruptedException
	{
		XSLX_Reader weBuyXSLX = new XSLX_Reader(System.getProperty("user.dir")+"/WeBuy.xlsx");
		
		List<String> productNamesToBeAdded = new ArrayList<String>();
		
		List<String> prodNamesList = new ArrayList<String>();
		
		List<String> quantitiesToBeAdded = new ArrayList<String>();
		
		Map<String,String> productAndQuantitiesMap = new HashMap<String,String>();
		
		System.out.println("xsl- rowcount:"+weBuyXSLX.getRowCount("Products"));
		
		for(int i=2;i<=weBuyXSLX.getRowCount("Products");i++)
		{
			String prodName = weBuyXSLX.getCellData("Products", "ProductName", i);
			String prodQty = weBuyXSLX.getCellData("Products", "Quantity", i);
			productNamesToBeAdded.add(prodName);
			
			quantitiesToBeAdded.add(prodQty);
			
			prodNamesList.add(prodName);
		}
		
		
		
		System.out.println("Products:"+productNamesToBeAdded.size());
		
		System.out.println("Products in Map:"+productAndQuantitiesMap.size());
		
		for(int i=0;i<productNamesToBeAdded.size();i++)
		{
			System.out.println("Product:"+productNamesToBeAdded.get(i));
		}
		
		WebDriver driver = new FirefoxDriver();
		
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		driver.get("http://uk.webuy.com");
		
		Thread.sleep(5000);
		
		WebDriverWait wait = new WebDriverWait(driver,20);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//html/body/div[1]/div/a/div")));
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//html/body/div[1]/div/a/div")));
		
		driver.findElement(By.xpath("//html/body/div[1]/div/a/div")).click();
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//*[@id='tabgaming']")).click();
		
		Thread.sleep(2000);
		
		List<WebElement> box = driver.findElements(By.xpath("//div[@class='centreColArea customcontrols']"));
		
		System.out.println("boxes:"+box.size());
		
		Thread.sleep(10000);
		
			
		int count = 0;
		
		int i =0;
		
		int size = 0;
		
		boolean hasMoreElements = true;
		
		while(hasMoreElements && count<weBuyXSLX.getRowCount("Products"))
		{
		
			List<WebElement> itemsList = box.get(0).findElements(By.xpath("//div[@class='hotProductDetails']"));
			
			System.out.println("items:"+itemsList.size());
			
			List<WebElement> buttonsList = box.get(0).findElements(By.xpath("//div[@class='btnSection']"));
			
			System.out.println("buttons:"+buttonsList.size());
			
			List<WebElement> buttons = buttonsList.get(i).findElements(By.xpath("//div[@class='buyNowButton']"));
			
			System.out.println("button-divs:"+buttons.size());
			
			for( i=size;i<itemsList.size();i++)
			{
				List<WebElement> links = itemsList.get(i).findElements(By.tagName("a"));
								
				for(int j=0;j<links.size();j++)
				{
					String text = links.get(j).getText();
					
					System.out.println("text:"+text);
					
					if(productNamesToBeAdded.contains(text))
					{
						String product = links.get(j).getText();
						
						System.out.println("product:"+product);
						
						System.out.println(buttons.get(i).getText());
						
						wait.until(ExpectedConditions.visibilityOf(buttons.get(i)));
						
						wait.until(ExpectedConditions.elementToBeClickable(buttons.get(i)));
						
						buttons.get(i).click();
						
						count++;
						
						Thread.sleep(1000L);
						
						String cartSize = driver.findElement(By.xpath("//*[@id='buyBasketCount']")).getText();
						
						Assert.assertEquals(Integer.parseInt(cartSize), count);
						
						for(int index=0;index<productNamesToBeAdded.size();index++)
						{
							if(productNamesToBeAdded.get(index).equals(product))
							{
								productNamesToBeAdded.remove(index);
								
								System.out.println("current products in the List:"+productNamesToBeAdded.size());
								
								break;
							}
						}
						
						System.out.println("Count:"+count);
						
						System.out.println("i:"+i);
						
						if(count == (weBuyXSLX.getRowCount("Products")- 1))
						{
							System.out.println("count equals to 5.Exiting the loop");
							break;
						}
					}
					
					
				}
			}
			try{
				
				System.out.println("xsl- rowcount:"+weBuyXSLX.getRowCount("Products"));
				
				if(count < (weBuyXSLX.getRowCount("Products")-1))
				{
					if(driver.findElement(By.xpath("//*[@id='showmoreresult']")) != null)
					{
						hasMoreElements = true;
						
						driver.findElement(By.xpath("//*[@id='showmoreresult']")).click();
						
						Thread.sleep(5000);
					}
				}
				else{
					hasMoreElements = false;
				}
			}
			catch(Exception ex)
			{
				hasMoreElements = false;
			}
		
						
	    }//End while
		
		driver.findElement(By.xpath("//*[@id='buyBasketRow']/td[2]/a")).click();
		
		Thread.sleep(2000L);
		
		List<WebElement> rows = driver.findElements(By.xpath("//html/body/div[4]/div[1]/div[3]/div[2]/form/table/tbody/tr"));
		
		System.out.println("Rows:"+rows.size());
		
		for(int row=1;row<=(rows.size()-3);row++)
		{
			String productName = driver.findElement(By.xpath("//html/body/div[4]/div[1]/div[3]/div[2]/form/table/tbody/tr["+row+"]/td[2]")).getText();
			
			String qty ="0";
			
			System.out.println("productName:"+productName);
			
	
			
			for(int index=0;index<prodNamesList.size();index++)
			{
			
				if(productName.startsWith(prodNamesList.get(index)))
				{
					qty = quantitiesToBeAdded.get(index);
			
					System.out.println(productName+"---"+qty);
					
					if(!qty.equals("1"))
					
						driver.findElement(By.xpath("//html/body/div[4]/div[1]/div[3]/div[2]/form/table/tbody/tr["+row+"]/td[1]/div/select")).sendKeys(qty);
					
					Thread.sleep(10000L);
					
					/*((JavascriptExecutor)driver).executeScript("var selectelm = document.getElementsByClassName('buybasket')["+row+"];"
							+ "UpdateBuyQty(selectelm, '250', 'uk');");*/
					
					String actual = driver.findElement(By.xpath("//html/body/div[4]/div[1]/div[3]/div[2]/form/table/tbody/tr["+row+"]/td[1]/div/select")).getAttribute("value");
					
					System.out.println("actual qty:"+actual);
					Thread.sleep(2000L);
					Assert.assertEquals(actual, qty);
					
					productAndQuantitiesMap.put(productName, qty);
					
					break;
				}
			}
			
		}
		
		double totalPrice=0.0;
		
		for(int row=1;row<=productAndQuantitiesMap.size();row++)
		{
			
			String product = driver.findElement(By.xpath("//html/body/div[4]/div[1]/div[3]/div[2]/form/table/tbody/tr["+row+"]/td[2]")).getText();
			
			String qty = productAndQuantitiesMap.get(product);
			
			String unitPrice = driver.findElement(By.xpath("//html/body/div[4]/div[1]/div[3]/div[2]/form/table/tbody/tr["+row+"]/td[3]")).getText().substring(1);
			
			System.out.println("unitPrice:"+unitPrice);
			
			double finalProdPrice = Double.parseDouble(qty)*(Double.parseDouble(unitPrice));
			
			System.out.println("finalProdPrice:"+finalProdPrice);
			
			Thread.sleep(5000L);
			
			String actualFinalProdPrice = driver.findElement(By.xpath("//html/body/div[4]/div[1]/div[3]/div[2]/form/table/tbody/tr["+row+"]/td[4]")).getText().substring(1);
			
			System.out.println(actualFinalProdPrice+"----"+finalProdPrice);
			
			Thread.sleep(2000L);
			
			Assert.assertEquals(Double.parseDouble(actualFinalProdPrice),finalProdPrice);
			
			totalPrice += finalProdPrice;
		}
		
		if(totalPrice > 0)
		{
			
			int totalRows = rows.size();
			
			int deliveryIndex = totalRows-1;
			
			int grandTotalIndex = totalRows;
			
			String delivery = driver.findElement(By.xpath("//html/body/div[4]/div[1]/div[3]/div[2]/form/table/tbody/tr["+deliveryIndex+"]/td[2]")).getText().substring(1);
			
			double grandTotal = totalPrice + Double.parseDouble(delivery);
			
			String actualGrandTotal = driver.findElement(By.xpath("//html/body/div[4]/div[1]/div[3]/div[2]/form/table/tbody/tr["+grandTotalIndex+"]/td[2]/strong")).getText().substring(1);
			
			System.out.println(Double.parseDouble(actualGrandTotal)+"-----"+grandTotal);
			
			Assert.assertEquals(Double.parseDouble(actualGrandTotal), grandTotal);
		}
		
	}

	
}
