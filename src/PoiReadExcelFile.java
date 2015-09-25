import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class PoiReadExcelFile {
	
	static DateTime currentDate = DateTime.now();
	static XSSFSheet worksheet;
	
	public static void main(String[] args) {
		
		try {
			
			FileInputStream fileInputStream = new FileInputStream("test.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			worksheet = workbook.getSheet("Sheet1");
			ArrayList<Teacher> teachers = sortTeacherList(getTeacherList());
			for (int x = 0; x < teachers.size(); x++)
				System.out.println(teachers.get(x).getName());
			
			fileInputStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Teacher> getTeacherList() 
	{
		ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
		Iterator<Row> rowIterator = worksheet.rowIterator();
		while (rowIterator.hasNext()) 
		{
			Row row = rowIterator.next();
			if (row.getCell(0) != null && !row.getCell(0).getStringCellValue().equals("Teacher"))
				teacherList.add(new Teacher(row));
		}
		return teacherList;
	}
	
	public static ArrayList<Teacher> sortTeacherList(ArrayList<Teacher> teachers) 
	{
		ArrayList<Teacher> recentSubs = new ArrayList<Teacher>();
		ArrayList<Teacher> sorted = new ArrayList<Teacher>();
		int comparison;
		
		for (int x = 0; x < teachers.size(); x++)
		{
			if (teachers.get(x).getSubCount() == 0)
			{
				sorted.add(teachers.get(x));
				teachers.remove(x);
				x--;
			}
			else if(getDateDiff(teachers.get(x).getLastSubDate()) < 3)
			{
				recentSubs.add(teachers.get(x));
				teachers.remove(x);
				x--;
			}
		}
		
		for (int x = teachers.size() - 1; x > 0; x--) //Runs through the array once
		{
			for (comparison = 0; comparison < x; comparison++)
			{
				if (teachers.get(comparison).getSubCount() > teachers.get(comparison+1).getSubCount()) //If a value is greater than the one before it
				{												//The two switch places
					Teacher temp = teachers.get(comparison);				//Every time the main for loop runs through the array once, the next largest number is moved into place.
					teachers.set(comparison, teachers.get(comparison+1));
					teachers.set(comparison + 1, temp);
				}
			}
		}
		
		for (int x = recentSubs.size() - 1; x > 0; x--) {
			for (comparison = 0; comparison < x; comparison++)
			{
				if (getDateDiff(recentSubs.get(comparison).getLastSubDate()) > getDateDiff(recentSubs.get(comparison+1).getLastSubDate()))
				{
					Teacher temp = recentSubs.get(comparison);
					recentSubs.set(comparison, recentSubs.get(comparison+1));
					recentSubs.set(comparison+1,  temp);
				}
				else if (getDateDiff(recentSubs.get(comparison).getLastSubDate()) == getDateDiff(recentSubs.get(comparison+1).getLastSubDate()))
				{
					if (recentSubs.get(comparison).getSubCount() > recentSubs.get(comparison+1).getSubCount())
					{
						Teacher temp = recentSubs.get(comparison);
						recentSubs.set(comparison, recentSubs.get(comparison+1));
						recentSubs.set(comparison+1,  temp);
					}
				}
			}
		}
		
		sorted.addAll(teachers);
		sorted.addAll(recentSubs);
		return sorted;
	}
	
	public static int getDateDiff(DateTime date1) {
	
		int diff = new Period(date1, currentDate, PeriodType.days()).getDays();
		return diff;
		
	}
	
	public static ArrayList<Teacher> getTeachersWithFreePeriods(String freePeriod)
	{
		ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
		ArrayList<Teacher> teachers = getTeacherList();
		for (int x = 0; x < teachers.size(); x++)
		{
			if (isFree(teachers.get(x), freePeriod))
				teacherList.add(teachers.get(x));
		}
		
		return teacherList;
	}
	
	public static boolean isFree(Teacher teacher, String period) 
	{
		for (int x = 0; x < teacher.getFreePeriods().size(); x++)
		{
			if (teacher.getFreePeriods().get(x) == period)
				return true;
		}
		return false;
	}
}
	