import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class filestuff {
public static String dir = "src/table/schedule.txt";
public static String day;
public static String month;
public static String date;
public static String year;
public static String CurrentDate;
public static final int [] MONTH_LENGTH = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
public static void fileInit(Date CurrentDate){
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String CurDate = dateFormat.format(CurrentDate).toString();
		int dateD = Integer.parseInt(CurDate.substring(0,2));
		int dateM = Integer.parseInt(CurDate.substring(3,5));
		int dateY = Integer.parseInt(CurDate.substring(6,10));
		
		PrintWriter writer;
		writer = null;
		try {
			writer = new PrintWriter(dir);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dateM= dateM-1;
		if (dateM == 0){
			dateM= 12;
			dateY--;
		}
		
		if(dateD <10){
			 day = "0" + dateD;
		}
		else{
			 day = "" + dateD;
		}
		if(dateM <10){
			 month = "0" + dateM;
		}
		else{
			 month = ""+dateM;
		}
		date = day + "/" + month + "/" + dateY;
		System.out.println(date);
		
		int startD = dateD;
		int startM = dateM;
		int startY = dateY;
		
		boolean leapYear1 = leapYear(startY);
		int CurrMonth = checkMonth(startM, leapYear1);
		if (startM == 2 && startD == 29){
			startD = startD-1;
		}
		
		while(dateY<startY+1 || dateD != startD || dateM != startM){
			if(dateD <10){
				 day = "0" + dateD;
			}
			else{
				 day = "" + dateD;
			}
			if(dateM <10){
				 month = "0" + dateM;
			}
			else{
				 month = ""+dateM;
			}
			date = day + "/" + month + "/" + dateY;
			
			dateD++;
			if (dateD>CurrMonth){
				dateM++;
				dateD=1;	
			}
			if (dateM>12){
				dateY++;
				dateM=1;
				leapYear1 = leapYear(dateY);
			}
			writer.println(date + ":-:7:00;11:30;free__11:30;12:30;lunch__12:30;14:00;free");
			
			CurrMonth = checkMonth(dateM, leapYear1);
			System.out.println(date);
			
		}
		writer.close();
		
	}
	public static void updateDates(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String CurDate = dateFormat.format(date).toString();
		int dateD = Integer.parseInt(CurDate.substring(0,2));
		int dateM = Integer.parseInt(CurDate.substring(3,5));
		int dateY = Integer.parseInt(CurDate.substring(6,10));
		
		String lastDate = "02/10/2017";//going to get date from file
		int lastDateD = Integer.parseInt(lastDate.substring(0,2));
		int lastDateM = Integer.parseInt(lastDate.substring(3,5));
		int lastDateY = Integer.parseInt(lastDate.substring(6,10));
		dateY++;
		String date2 = "";
		
		boolean leapYear1 = leapYear(lastDateY);
		int CurrMonth = checkMonth(lastDateM, leapYear1);
		if (lastDateM == 2 && lastDateD == 29){
			lastDateD = lastDateD-1;
		}
	
		while(dateY!=lastDateY || dateM != lastDateM || dateD != lastDateD){
			if(lastDateD <10){
				 day = "0" + lastDateD;
			}
			else{
				 day = "" + lastDateD;
			}
			if(lastDateM <10){
				 month = "0" + lastDateM;
			}
			else{
				 month = ""+ lastDateM;
			}
			year = "" + lastDateY;
			date2 = day + "/" + month + "/" + year;
			
			lastDateD++;
			if (lastDateD>CurrMonth){
				lastDateM++;
				lastDateD=1;
			}
			if (lastDateM>12){
				lastDateY++;
				lastDateM=1;
				leapYear1 = leapYear(lastDateY);
			}
			System.out.println(date2);
			CurrMonth= checkMonth(lastDateM,leapYear1);
		}
		
		
	}
	public static String [] getCurrentDate(Date d){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String CurrentDate = dateFormat.format(d).toString();
		BufferedReader br = null;
		try {
			 br = new BufferedReader(new FileReader(dir));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String CurrentDay = null;
		File file = new File(dir);
		for (int i = 0; i<file.length() ; i++){
			try {
				CurrentDay = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(CurrentDay.substring(0,10));
			if (CurrentDate.compareTo(CurrentDay.substring(0,10)) == 0){
				
				return CurrentDay.substring(13, CurrentDay.length()).split("__");
			}
			
		}
		return null;
	}
	public static void AddApointment(String [] DaysProcedings,Double ApointStart, Double ApointEnd, String name){
		for (int i = 0; i<DaysProcedings.length; i++){
			String [] timeSlot = DaysProcedings [i].split(";");
			double startTime = 0;
			double endTime = 0;
			
			if (timeSlot[0].length()<5){
			startTime = Double.parseDouble(timeSlot[0].substring(0,1)) + Double.parseDouble
				(timeSlot[0].substring(2,4))/100;
			}
			else{
			startTime = Double.parseDouble(timeSlot[0].substring(0,2)) + Double.parseDouble
						(timeSlot[0].substring(3,5))/100;
			}
			if (timeSlot[1].length()<5){
				endTime = Double.parseDouble(timeSlot[1].substring(0,1)) + Double.parseDouble
					(timeSlot[1].substring(2,4))/100;
				}
				else{
				endTime = Double.parseDouble(timeSlot[1].substring(0,2)) + Double.parseDouble
							(timeSlot[1].substring(3,5))/100;
				}
			System.out.println(startTime);
			System.out.println(endTime);
			//if (){
				
			//}
				
			}
		}
		
	
	public static int checkMonth(int m, boolean leapYear){
		
		if (m == 2 && leapYear){
			return 29;
		} else {
			return MONTH_LENGTH[m - 1];
		}
		
	/*	
		if (m == 1){
			return 31;
		}
		if (m == 2){
			if (leapYear == true){
				return 29;
			}
			else{
			return 28;
			}
		}
		if (m == 3){
			return 31;
		}
		if (m == 4){
			return 30;
		}
		if (m == 5){
			return 31;
		}
		if (m == 6){
			return 30;
		}
		if (m == 7){
			return 31;
		}
		if (m == 8){
			return 31;
		}
		if (m == 9){
			return 30;
		}
		if (m == 10){
			return 31;
		}
		if (m == 11){
			return 30;
		}
		if (m == 12){
			return 31;
		}
		
		return 30;
		*/
	}
	public static boolean leapYear(int y){
		int a = y%4;
		if (a==0){
			return true;
			
		}
		else{
		return false;
		}
		
	}
	
}
