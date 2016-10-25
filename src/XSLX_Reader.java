

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XSLX_Reader {

	public FileInputStream fistr=null;
	public FileOutputStream fostr = null;
	
	private String path;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFRow row;
	private XSSFCell cell;
	
	
	
	public XSLX_Reader(String path)
	{
		this.path = path;
		try {
			fistr = new FileInputStream(path);
			workbook = new XSSFWorkbook(fistr);
			sheet = workbook.getSheetAt(0);
			fistr.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	public int getRowCount(String sheetname)
	{
		int rowcount = 0;
		
		sheet = workbook.getSheet(sheetname);
		
		if(sheet != null)
		rowcount = sheet.getLastRowNum()+1;
		
		return rowcount;
	}
	
	public String getCellData(String sheetname,String colName,int rowNum)
	{
		String data="";
		int colindex = -1;
		sheet = workbook.getSheet(sheetname);
		
		if(sheet != null)
		{
			row = sheet.getRow(0);
			
			if(row != null)
			{
				Iterator<Cell> cellitr = row.cellIterator();
				while(cellitr.hasNext())
				{
					Cell cell = cellitr.next();
					if(cell.getStringCellValue().equals(colName))
					{
						colindex = cell.getColumnIndex();
						break;
					}
				}
			}
				if(colindex > -1)
				{
					row = sheet.getRow(rowNum-1);
					if(row != null)
					{
						cell = row.getCell(colindex);
						
						if(cell == null)
							return null;
						if(cell.getCellType() == Cell.CELL_TYPE_STRING)
						{
							data = cell.getStringCellValue();
											
						}
						else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA)
						{
							data = String.valueOf(cell.getNumericCellValue());
							
							if(HSSFDateUtil.isCellDateFormatted(cell))
							{
								Date d = cell.getDateCellValue();
								
								Calendar cal = Calendar.getInstance();
								
								cal.setTime(d);
								
								data = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR);
							}
						}
						else if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
						{
							data ="";
						}
						else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
						{
							data = String.valueOf(cell.getBooleanCellValue());
						}
						else
						{
							data = "No data found for the column-"+colName+" and row number -"+rowNum; 
						}
					}
				}
			}
		
				return data;
	}
	
	
	
	public String getCellData(String sheetname,int colNum,int rowNum)
	{
		String data = "";
		   
		sheet = workbook.getSheet(sheetname);
		
		if(sheet != null)
		{
			row  = sheet.getRow(rowNum-1);
			
			if(row != null)
			{
				cell = row.getCell(colNum-1);
				
				if(cell==null)
					return null;
				
				if(cell.getCellType()== Cell.CELL_TYPE_STRING)
				{
					data = cell.getStringCellValue();
				}
				else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA)
				{
					data = String.valueOf(cell.getNumericCellValue());
					
					if(HSSFDateUtil.isCellDateFormatted(cell))
					{
						Date d = cell.getDateCellValue();
						
						Calendar cal = Calendar.getInstance();
						
						cal.setTime(d);
						
						data = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR);
					}
				}
				else if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
				{
					data = "";
				}
				else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
				{
					data = String.valueOf(cell.getBooleanCellValue());
				}
				else
				{
					data = "No data found for row number - "+rowNum+" and column number - "+colNum+" in the given sheet";
				}
			}
		}
		
		return data;
	}
	
	
	public boolean setCellData(String sheetname,String colName,int rowNum,String data)
	{
		boolean result = false;
		int colindex = -1;
		try{
			
			fistr = new FileInputStream(path);
			workbook = new XSSFWorkbook(fistr);
			
			sheet = workbook.getSheet(sheetname);
			
			if(sheet != null)
			{
				row = sheet.getRow(0);
				if(row != null)
				{
					Iterator<Cell> cellitr = row.cellIterator();
					while(cellitr.hasNext())
					{
						Cell cell = cellitr.next();
						if(cell.getStringCellValue().equals(colName))
						{
							colindex = cell.getColumnIndex();
							break;
						}
					}
				}
				
				row = sheet.getRow(rowNum-1);
				if(row == null)
				{
					row = sheet.createRow(rowNum-1);
				}
				if(colindex > -1)
				{
					cell = row.getCell(colindex);
					if(cell == null)
						cell = row.createCell(colindex);
					CellStyle style = workbook.createCellStyle();
					style.setWrapText(true);
					cell.setCellStyle(style);
					cell.setCellValue(data);
					
					fostr = new FileOutputStream(path);
					workbook.write(fostr);
					fostr.close();
					fistr.close();
					result = true;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	
	public boolean setCellData(String sheetname,int colNum,int rowNum,String data,String url)
	{
		boolean result = false;
		
		try{
			fistr = new FileInputStream(path);
			workbook = new XSSFWorkbook(fistr);
			
			sheet = workbook.getSheet(sheetname);
			if(sheet != null)
			{
				row = sheet.getRow(rowNum-1);
				if(row == null)
				{
					row = sheet.createRow(rowNum-1);
				}
				
				cell = row.getCell(colNum-1);
				if(cell == null)
				{
					cell = row.createCell(colNum-1);
				}
				
				cell.setCellValue(data);
				
				//Bydefault, hyperlinks are blue and underlined
				CellStyle style = workbook.createCellStyle();
				XSSFFont font = workbook.createFont();
				font.setColor(IndexedColors.BLUE.getIndex());
				font.setUnderline(XSSFFont.U_SINGLE);
				style.setFont(font);
				cell.setCellStyle(style);
				
				XSSFCreationHelper helper = workbook.getCreationHelper();
				XSSFHyperlink link = helper.createHyperlink(XSSFHyperlink.LINK_URL);
				link.setAddress(url);
				cell.setHyperlink(link);
				
				fostr = new FileOutputStream(path);
				workbook.write(fostr);
				fostr.close();
				fistr.close();
				
				result = true;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	
	public boolean addSheet(String sheetname)
	{
		boolean result = false;
		
		try{
			
			fostr = new FileOutputStream(path);
			workbook.createSheet(sheetname);
			workbook.write(fostr);
			fostr.close();
			result=true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			result=false;
		}
		
		return result;
	}
	
	
	public boolean removeSheet(String sheetname)
	{
		boolean result=false;
		try{
			
			  int sheetIndex = workbook.getSheetIndex(sheetname);
			  workbook.removeSheetAt(sheetIndex);
			  
			  fostr = new FileOutputStream(path);
			  workbook.write(fostr);
			  fostr.close();
			  result= true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			result=false;
		}
		
		return result;
	}
	
	public boolean addColumn(String sheetname,String colName)
	{
		boolean result = false;
		boolean duplicateColumn = false;
		try{
			
			fistr = new FileInputStream(path);
			workbook = new XSSFWorkbook(fistr);
			
			sheet = workbook.getSheet(sheetname);
			if(sheet == null)
			{
				sheet = workbook.createSheet(sheetname);
			}
			
			row = sheet.getRow(0);
			if(row == null)
			{
				row = sheet.createRow(0);
			}
			
			Iterator<Cell> cellitr = row.cellIterator();
			if(cellitr == null || cellitr.hasNext()== false)
			{
				cell = row.createCell(0);
				
				cell.setCellValue(colName);
			}
			else
			{
				while(cellitr.hasNext())
				{
					 Cell cell = cellitr.next();
					 if(cell.getStringCellValue().equals(colName))
					 {
						 duplicateColumn = true;
						 break;
					 }
					
				}
				
			    if(duplicateColumn)
			    {
			    	result = false;
			    	return result;
			    }
			    else
			    {
			    	
			    	int colNum = row.getLastCellNum();
			    	
			    	cell = row.createCell(colNum);
			    	
			    	cell.setCellValue(colName);
			    	
			    }
			}
			
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style);
			
			fostr = new FileOutputStream(path);
			workbook.write(fostr);
			fostr.close();
			result = true;
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	
	public boolean removeColumn(String sheetname,int colNum)
	{
		boolean result = false;
		try{
			 sheet = workbook.getSheet(sheetname);
			 if(sheet != null)
			 {
				int rowcount = this.getRowCount(sheetname);
				
				XSSFCellStyle style = workbook.createCellStyle();
				style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
				style.setFillPattern(HSSFCellStyle.NO_FILL);
				
				for(int i=0;i<rowcount;i++)
				{
					cell =  sheet.getRow(i).getCell(colNum-1);
					if(cell != null)
					{
						cell.setCellStyle(style);
						sheet.getRow(i).removeCell(cell);
					}
				}
				
				fostr = new FileOutputStream(path);
				workbook.write(fostr);
				fostr.close();
				result=true;
			 }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			result=false;
		}
		return result;
	}
	
	public boolean isSheetExists(String sheetname)
	{
		boolean result = false;
		
		try{
			
			sheet = workbook.getSheet(sheetname);
			
			if(sheet != null)
			{
				result = true;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			result=false;
		}
		return result;
	}
	
	
	public int getColumnCount(String sheetname)
	{
		int cols = 0;
		try{
			  sheet = workbook.getSheet(sheetname);
			  
			  if(sheet != null)
			  {
				  row = sheet.getRow(0);
				  
				  if(row != null)
				  {
					  cols = row.getLastCellNum();
				  }
			  }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			cols = 0;
		}
		return cols;
	}
	
	
	public int getCellRowNumber(String sheetname,String colName,String cellValue)
	{
		int result = -1;
		
		try{
			
			sheet = workbook.getSheet(sheetname);
			
			if(sheet != null)
			{
				for(int i=2;i<getRowCount(sheetname);i++)
				{
					if(getCellData(sheetname,colName,i).equalsIgnoreCase(cellValue))
					{
						result = i;
						break;
					}
				}
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			result = -1;
		}
		
		return result;
	}
	
	
	public boolean addHyperLink(String sheetname,String screenShotColName,String testcaseName,int index,String url,String message)
	{
		boolean result = false;
		int screenshotColIndex = -1;
		try{
			   sheet = workbook.getSheet(sheetname);
			   
			   if(sheet != null)
			   {
				   url = url.replace("\\","/");
				   
				   row = sheet.getRow(0);
				   
				   if(row != null)
				   {
					   for(int i=0;i<getColumnCount(sheetname);i++)
					   {
						  if( row.getCell(i).getStringCellValue().equals(screenShotColName))
						  {
							  screenshotColIndex = i+1;
						  }
					   }
				   }
				   
				   for(int i=2;i<getRowCount(sheetname);i++)
				   {
					   if(getCellData(sheetname,0,i).equalsIgnoreCase(testcaseName))
					   {
						   setCellData(sheetname,screenshotColIndex,i+index,message,url);
						   result = true;
						   break;
					   }
				   }
			   }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		
		XSLX_Reader reader = new XSLX_Reader("C:\\workspace\\Selenium_Java\\data.xlsx");
		
		System.out.println(reader.getRowCount("person_data"));

		System.out.println(reader.getCellData("person_data","BirthDate",3));
		
		System.out.println(reader.getCellData("person_data",4,3));
		
		System.out.println(reader.setCellData("person_data","Name",5,"Vivek"));
		
		System.out.println(reader.setCellData("person_data",5,2,"Angel Flowers","http://angelflowers.netii.net"));
		
		System.out.println(reader.addColumn("xpath_data", "xpathvar"));
		
		System.out.println(reader.removeColumn("person_data", 5));
		
		System.out.println(reader.isSheetExists("xpath_data"));
		
		System.out.println(reader.getColumnCount("person_data"));
	}

}
