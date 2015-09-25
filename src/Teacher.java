import java.util.Date;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;


public class Teacher {
	
	private String name;
	private ArrayList<String> freePeriods = new ArrayList<String>();
	private ArrayList<String> classPeriods = new ArrayList<String>();
	private int subCount;
	private DateTime lastSubDate;
	private Row row;
	
	public Teacher(Row row) {
		this.row = row;
		this.name = row.getCell(0).getStringCellValue();
		this.freePeriods = getPeriods(row.getCell(1).getStringCellValue());
		this.classPeriods = getPeriods(row.getCell(2).getStringCellValue());
		this.subCount = (int)row.getCell(3).getNumericCellValue();
		if (this.subCount > 0)
			this.lastSubDate = new DateTime(row.getCell(4).getDateCellValue());
	}
	
	public Row getRow() {
		return this.row;
	}
	
	private ArrayList<String> getPeriods(String periods) {
		
		ArrayList<String> periodList = new ArrayList<String>();
		
		for (int x = 0; x < periods.length(); x++) {
			if (periods.charAt(x) == ' ' || periods.charAt(x) == ',');
			else
				periodList.add(String.valueOf(periods.charAt(x)));
		}
		
		return periodList;
	}
	
	public ArrayList<String> getFreePeriods() {
		return this.freePeriods;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSubCount() {
		return this.subCount;
	}
	
	public DateTime getLastSubDate() {
		return this.lastSubDate;
	}
	
	public void updateSubCount() {
		this.subCount++;
	}
	
	public void updateLastSubDate(DateTime date) {
		this.lastSubDate = date;
		this.row.getCell(4).setCellValue(date.toDate());
	}
	
}
