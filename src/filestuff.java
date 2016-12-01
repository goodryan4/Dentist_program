import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
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
	public static String CurDate;
	public static final int[] MONTH_LENGTH = new int[] { 31, 28, 31, 30, 31,
			30, 31, 31, 30, 31, 30, 31 };

	public static void fileInit(Date CurrentDate) {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String CurDate = dateFormat.format(CurrentDate).toString();
		int dateD = Integer.parseInt(CurDate.substring(0, 2));
		int dateM = Integer.parseInt(CurDate.substring(3, 5));
		int dateY = Integer.parseInt(CurDate.substring(6, 10));

		PrintWriter writer;
		writer = null;
		try {
			writer = new PrintWriter(dir);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dateM = dateM - 1;
		if (dateM == 0) {
			dateM = 12;
			dateY--;
		}

		if (dateD < 10) {
			day = "0" + dateD;
		} else {
			day = "" + dateD;
		}
		if (dateM < 10) {
			month = "0" + dateM;
		} else {
			month = "" + dateM;
		}
		date = day + "/" + month + "/" + dateY;
		System.out.println(date);

		int startD = dateD;
		int startM = dateM;
		int startY = dateY;

		boolean leapYear1 = leapYear(startY);
		int CurrMonth = checkMonth(startM, leapYear1);
		if (startM == 2 && startD == 29) {
			startD = startD - 1;
		}

		while (dateY < startY + 1 || dateD != startD || dateM != startM) {
			if (dateD < 10) {
				day = "0" + dateD;
			} else {
				day = "" + dateD;
			}
			if (dateM < 10) {
				month = "0" + dateM;
			} else {
				month = "" + dateM;
			}
			date = day + "/" + month + "/" + dateY;

			dateD++;
			if (dateD > CurrMonth) {
				dateM++;
				dateD = 1;
			}
			if (dateM > 12) {
				dateY++;
				dateM = 1;
				leapYear1 = leapYear(dateY);
			}
			writer.println(date
					+ ":-:7:00;11:30;free__11:30;12:30;lunch__12:30;14:00;free");

			CurrMonth = checkMonth(dateM, leapYear1);
			System.out.println(date);

		}
		writer.close();

	}

	public static void updateDates() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String CurDate = dateFormat.format(date).toString();
		int dateD = Integer.parseInt(CurDate.substring(0, 2));
		int dateM = Integer.parseInt(CurDate.substring(3, 5));
		int dateY = Integer.parseInt(CurDate.substring(6, 10));

		String lastDate = "02/10/2017";// going to get date from file
		int lastDateD = Integer.parseInt(lastDate.substring(0, 2));
		int lastDateM = Integer.parseInt(lastDate.substring(3, 5));
		int lastDateY = Integer.parseInt(lastDate.substring(6, 10));
		dateY++;
		String date2 = "";

		boolean leapYear1 = leapYear(lastDateY);
		int CurrMonth = checkMonth(lastDateM, leapYear1);
		if (lastDateM == 2 && lastDateD == 29) {
			lastDateD = lastDateD - 1;
		}

		while (dateY != lastDateY || dateM != lastDateM || dateD != lastDateD) {
			if (lastDateD < 10) {
				day = "0" + lastDateD;
			} else {
				day = "" + lastDateD;
			}
			if (lastDateM < 10) {
				month = "0" + lastDateM;
			} else {
				month = "" + lastDateM;
			}
			year = "" + lastDateY;
			date2 = day + "/" + month + "/" + year;

			lastDateD++;
			if (lastDateD > CurrMonth) {
				lastDateM++;
				lastDateD = 1;
			}
			if (lastDateM > 12) {
				lastDateY++;
				lastDateM = 1;
				leapYear1 = leapYear(lastDateY);
			}

			CurrMonth = checkMonth(lastDateM, leapYear1);
		}

	}

	public static String[] getCurrentDate(Date d) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String CurrentDate = dateFormat.format(d).toString();
		CurDate = CurrentDate;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(dir));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String CurrentDay = null;
		File file = new File(dir);
		for (int i = 0; i < file.length(); i++) {
			try {
				CurrentDay = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(CurrentDay.substring(0,10));
			if (CurrentDate.compareTo(CurrentDay.substring(0, 10)) == 0) {

				return CurrentDay.substring(13, CurrentDay.length())
						.split("__");
			}

		}
		return null;
	}

	public static void AddApointment(String[] DaysProcedings,
			Double ApointStart, Double ApointEnd, String name) {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String CurrentDate = dateFormat.format(date).toString();

		String previousSlot = "";
		String newTextLine = "";
		String newTimeSlots = "";
		String conflict = "";
		boolean moreConflict = false;
		boolean change = false;
		boolean oneTime = false;
		boolean conflict2 = false;
		boolean didRun = false;

		for (int i = 0; i < DaysProcedings.length; i++) {
			String[] timeSlot = DaysProcedings[i].split(";");
			double startTime = 0;
			double endTime = 0;

			
				startTime = getStartTime(timeSlot[0]);
				endTime = getStartTime(timeSlot[1]);
			

			if (ApointStart >= startTime && ApointEnd <= endTime
					&& timeSlot[2].compareTo("free") == 0) {
				boolean[] gaps = checks(startTime, endTime, ApointStart,
						ApointEnd);
				change = true;
				oneTime = true;
				didRun = true;

				if (gaps[0] == true && gaps[1] == true) {
					String freeStart = moreChecks(Double.toString(startTime)
							.replace(".", ":"));
					String freeEnd = moreChecks(Double.toString(ApointStart)
							.replace(".", ":"));
					String ApointBegin = moreChecks(Double
							.toString(ApointStart).replace(".", ":"));
					String ApointFinish = moreChecks(Double.toString(ApointEnd)
							.replace(".", ":"));
					String freeStart1 = moreChecks(Double.toString(ApointEnd)
							.replace(".", ":"));
					String freeEnd1 = moreChecks(Double.toString(endTime)
							.replace(".", ":"));
					newTimeSlots = freeStart + ";" + freeEnd + ";free__"
							+ ApointBegin + ";" + ApointFinish + ";" + name
							+ "__" + freeStart1 + ";" + freeEnd1 + ";free__";
				} else if (gaps[0] == true && gaps[1] == false) {
					String freeStart = moreChecks(Double.toString(startTime)
							.replace(".", ":"));
					String freeEnd = moreChecks(Double.toString(ApointStart)
							.replace(".", ":"));
					String ApointBegin = moreChecks(Double
							.toString(ApointStart).replace(".", ":"));
					String ApointFinish = moreChecks(Double.toString(ApointEnd)
							.replace(".", ":"));
					newTimeSlots = freeStart + ";" + freeEnd + ";free__"
							+ ApointBegin + ";" + ApointFinish + ";" + name
							+ "__";

				} else if (gaps[0] == false && gaps[1] == true) {

					String ApointBegin = moreChecks(Double
							.toString(ApointStart).replace(".", ":"));
					String ApointFinish = moreChecks(Double.toString(ApointEnd)
							.replace(".", ":"));
					String freeStart1 = moreChecks(Double.toString(ApointEnd)
							.replace(".", ":"));
					String freeEnd1 = moreChecks(Double.toString(endTime)
							.replace(".", ":"));
					newTimeSlots = ApointBegin + ";" + ApointFinish + ";"
							+ name + "__" + freeStart1 + ";" + freeEnd1
							+ ";free__";
				} else if (gaps[0] == false && gaps[1] == false) {

					String ApointBegin = moreChecks(Double
							.toString(ApointStart).replace(".", ":"));
					String ApointFinish = moreChecks(Double.toString(ApointEnd)
							.replace(".", ":"));
					newTimeSlots = ApointBegin + ";" + ApointFinish + ";"
							+ name + "__";

				}

			}
			if (change == false) {

				if (ApointStart < startTime
						&& !(DaysProcedings[i - 1].endsWith("free"))
						&& ApointEnd < endTime && conflict.compareTo("") == 0) {
					if (ApointEnd > startTime
							&& !(DaysProcedings[i].endsWith("free"))) {
						conflict = DaysProcedings[i - 1] + DaysProcedings[i];
						conflict2 = true;
					} else if (conflict.compareTo("") == 0) {
						conflict = DaysProcedings[i - 1];
						conflict2 = true;
					}
				}

				if (ApointStart < startTime && ApointEnd > endTime) {
					if (!(DaysProcedings[i].endsWith("free"))
							&& !(DaysProcedings[i + 1].endsWith("free"))
							&& !(DaysProcedings[i - 1].endsWith("free"))) {
						conflict = DaysProcedings[i - 1] + DaysProcedings[i]
								+ DaysProcedings[i + 1];
						conflict2 = true;
					} else if (!(DaysProcedings[i + 1].endsWith("free"))
							&& !(DaysProcedings[i - 1].endsWith("free"))) {
						conflict = DaysProcedings[i - 1]
								+ DaysProcedings[i + 1];
						conflict2 = true;
					} else if (!(DaysProcedings[i].endsWith("free"))
							&& !(DaysProcedings[i + 1].endsWith("free"))) {
						conflict = DaysProcedings[i] + DaysProcedings[i + 1];
						conflict2 = true;
					} else if (!(DaysProcedings[i].endsWith("free"))
							&& !(DaysProcedings[i - 1].endsWith("free"))) {
						conflict = DaysProcedings[i - 1] + DaysProcedings[i];
						conflict2 = true;
					}
				}
			}

			if (oneTime == true) {
				newTextLine = newTextLine + newTimeSlots;
				oneTime = false;
			} else {
				newTextLine = newTextLine + DaysProcedings[i] + "__";
			}

		}
		newTextLine = CurDate + ":-:" + newTextLine;
		System.out.println(newTextLine);
		System.out.println(conflict);
		if(conflict2 == false && didRun == true){
			wrightToFile1(newTextLine);
		}
		
	}

	public static boolean[] checks(double start, double end, double startA,
			double endA) {
		boolean[] gaps;
		
		if (startA-start>=0.15 && end-endA>=0.15) {
			gaps = new boolean[] { true, true };
			return gaps;
		} else if (startA-start>=0.15) {
			gaps = new boolean[] { true, false };
			return gaps;
		} else if (end-endA>=0.15) {
			gaps = new boolean[] { false, true };
			return gaps;
		} else {
			gaps = new boolean[] { false, false };
			return gaps;
		}
	}

	public static String moreChecks(String m) {
		if (m.indexOf(":") == m.length() - 2) {
			m = m + "0";

			return m;
		}

		return m;
	}

	public static void editApointment(String[] DaysProcedings,
			Double ApointStart, Double ApointEnd, String oldName, String newName) {
		
		String conflict = "";
		boolean hasStarted = false;
		boolean hasAdded = false;
		boolean freeBefore = false;
		boolean firstTime = true;
		double endTimeAfter = 0.0;
		double startTimeAfter = 0.0;
		double endTimeBefore = 0.0;
		double startTimeBefore = 0.0;
		String newFreeBefore = "";
		String newFreeAfter = "";
		String replace = "";
		String newTextLine = "";
		int x;
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String CurrentDate = dateFormat.format(date).toString();
		
		boolean IsConflict = checkForConflicts(DaysProcedings, ApointStart,
				ApointEnd, oldName);
		

		if (IsConflict == false) {
			for (int i = 0; i < DaysProcedings.length; i++) {

				x = i;
				String[] timeSlot = DaysProcedings[i].split(";");
				double startTime = 0;
				double endTime = 0;

				
				
					
					startTime = getStartTime(timeSlot[0]);
					endTime = getStartTime(timeSlot[1]);
					

					if (hasAdded == true) {
						newTextLine = newTextLine + DaysProcedings[i] + "__";
					}

					if (timeSlot[2].compareTo(oldName) == 0) {
						String newStart = moreChecks(Double.toString(
								ApointStart).replace(".", ":"));
						String newEnd = moreChecks(Double.toString(ApointEnd)
								.replace(".", ":"));
						replace = newStart + ";" + newEnd + ";" + newName
								+ "__";

						if (endTime - ApointEnd >= 0.15) {
							String tempS1 = moreChecks(Double.toString(
									ApointEnd).replace(".", ":"));
							String tempE1 = moreChecks(Double.toString(endTime)
									.replace(".", ":"));
							newFreeAfter = tempS1 + ";" + tempE1 + ";"
									+ "free__";
						}
						if (ApointStart - startTime >= 0.15) {
							String tempS2 = moreChecks(Double.toString(
									startTime).replace(".", ":"));
							String tempE2 = moreChecks(Double.toString(
									ApointStart).replace(".", ":"));
							newFreeBefore = tempS2 + ";" + tempE2 + ";"
									+ "free__";
						}

						if(i+1 != DaysProcedings.length){
						String[] temp = DaysProcedings[i + 1].split(";");

						startTimeAfter = getStartTime(temp[0]);
						endTimeAfter = getStartTime(temp[1]);
						}

						if(i>0){
						String[] temp1 = DaysProcedings[i - 1].split(";");

						startTimeBefore = getStartTime(temp1[0]);
						endTimeBefore = getStartTime(temp1[1]);
						
						

						
						if (DaysProcedings[i - 1].endsWith("free") && (i+1 != DaysProcedings.length)
								&& DaysProcedings[i + 1].endsWith("free")) {

							if (endTimeAfter - ApointEnd >= 0.15) {
								String tempS3 = moreChecks(Double.toString(
										ApointEnd).replace(".", ":"));
								String tempE3 = moreChecks(Double.toString(
										endTimeAfter).replace(".", ":"));
								newFreeAfter = tempS3 + ";" + tempE3 + ";"
										+ "free__";
							}
						
							if (ApointStart - startTimeBefore >= 0.15) {
								String tempS4 = moreChecks(Double.toString(
										startTimeBefore).replace(".", ":"));
								String tempE4 = moreChecks(Double.toString(
										ApointStart).replace(".", ":"));
								newFreeBefore = tempS4 + ";" + tempE4 + ";"
										+ "free__";
							}
							
							i++;
							freeBefore = true;

						} else if (DaysProcedings[i - 1].endsWith("free")) {

							if (ApointStart - startTimeBefore >= 0.15) {
								String tempS4 = moreChecks(Double.toString(
										startTimeBefore).replace(".", ":"));
								String tempE4 = moreChecks(Double.toString(
										ApointStart).replace(".", ":"));
								newFreeBefore = tempS4 + ";" + tempE4 + ";"
										+ "free__";

							}
							freeBefore = true;

						} 
						}
						else if (DaysProcedings[i + 1].endsWith("free") && (i+1 != DaysProcedings.length)) {

							if (endTimeAfter - ApointEnd >= 0.15) {
								String tempS3 = moreChecks(Double.toString(
										ApointEnd).replace(".", ":"));
								String tempE3 = moreChecks(Double.toString(
										endTimeAfter).replace(".", ":"));
								newFreeAfter = tempS3 + ";" + tempE3 + ";"
										+ "free__";
							}
							i++;
						}

						newTextLine = newTextLine + newFreeBefore + replace
										+ newFreeAfter;
						hasAdded = true;

					}

					if (freeBefore == false && hasAdded == true
							&& firstTime == true) {
						String P = newTextLine;
						newTextLine = "";
						for (int b = 0; b < x; b++) {
							newTextLine = newTextLine + DaysProcedings[b]
									+ "__";
						}
						newTextLine = newTextLine + P;
						firstTime = false;

					}
					if (freeBefore == true) {
						freeBefore = false;
						String P = newTextLine;
						newTextLine = "";
						for (int b = 0; b < x - 1; b++) {
							newTextLine = newTextLine + DaysProcedings[b]
									+ "__";
						}
						newTextLine = newTextLine + P;
						firstTime = false;
					}
				}
			}
			
		newTextLine = CurDate + ":-:" + newTextLine;
			System.out.println(newTextLine);
			if(IsConflict==false){
			wrightToFile1(newTextLine);
			}
		}
		
		
	
	
	
	public static void wrightToFile1(String line) {
		
		
		String CurrentDate = line.substring(0, 10);
		BufferedReader br = null;
		BufferedReader br2 = null;
		
		try {
			br = new BufferedReader(new FileReader(dir));
			br2 = new BufferedReader(new FileReader(dir));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		String input = "";
		String CurrentDay = "";
		File file = new File(dir);
		if(file.exists()){
		try {
			while(br2.readLine() != null) {
				
				try {
					
					CurrentDay = br.readLine();
					
					
					if (CurrentDate.compareTo(CurrentDay.substring(0, 10)) == 0) {
						
						System.out.println("check4");
						input = input + line + '\n';
					}
					else{
						input = input + CurrentDay + '\n';
					}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				System.out.println(CurrentDay.substring(0, 10) + "and" + CurrentDate);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		try{
			FileOutputStream fileOut = new FileOutputStream(dir);
			fileOut.write(input.getBytes());
			fileOut.close();
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	}

	

	public static double getStartTime(String d) {
		if (d.length() < 5) {
			double startTime = Double.parseDouble(d.substring(0, 1))
					+ Double.parseDouble(d.substring(2, 4)) / 100;
			return startTime;
		} else {
			double startTime = Double.parseDouble(d.substring(0, 2))
					+ Double.parseDouble(d.substring(3, 5)) / 100;
			return startTime;

		}
	}

	public static boolean checkForConflicts(String[] DaysProcedings,
			Double ApointStart, Double ApointEnd, String name) {
		String conflict = "";
		boolean hasStarted = false;

		for (int i = 0; i < DaysProcedings.length; i++) {
			String[] timeSlot = DaysProcedings[i].split(";");
			double startTime = 0;
			double endTime = 0;

			// System.out.println(timeSlot[2]);

			
				startTime = getStartTime(timeSlot[0]);
				endTime = getStartTime(timeSlot[1]);
			

			if (ApointEnd > startTime && !(timeSlot[2].compareTo(name) == 0)
					&& hasStarted == true
					&& !(timeSlot[2].compareTo("free") == 0)) {
				conflict = conflict + DaysProcedings[i];

				return true;

			} else if (ApointStart >= startTime && endTime > ApointStart) {
				hasStarted = true;
				if (!(timeSlot[2].compareTo("free") == 0)
						&& !(timeSlot[2].compareTo(name) == 0)) {
					conflict = conflict + DaysProcedings[i];

					return true;
				}

			}

		}
		System.out.println("check3");
		return false;
	}

	public static void deleteApointment(String[] DaysProcedings, String name) {
		String newTextLine = "";
		double startTime = 0.0;
		double endTime = 0.0;
		double startTime1 = 0.0;
		double endTime1 = 0.0;
		double oldstart = 0.0;
		double oldend = 7.30;
		boolean deleted = false;
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String CurrentDate = dateFormat.format(date).toString();

		for (int i = 0; i < DaysProcedings.length; i++) {

			String[] timeSlot = DaysProcedings[i].split(";");
			startTime = getStartTime(timeSlot[0]);
			endTime = getStartTime(timeSlot[1]);

			if (deleted == true) {
				newTextLine = newTextLine + DaysProcedings[i] + "__";
			} else if (timeSlot[2].compareTo(name) == 0) {

				String[] timeSlot2 = DaysProcedings[i + 1].split(";");
				startTime1 = getStartTime(timeSlot2[0]);
				endTime1 = getStartTime(timeSlot2[1]);

				if (DaysProcedings[i - 1].endsWith("free")
						&& DaysProcedings[i + 1].endsWith("free") && i > 0) {
					newTextLine = newTextLine + moreChecks(Double.toString(oldstart).replace(".", ":"))
							+ ";" + moreChecks(Double.toString(endTime1).replace(".", ":"))+ ";"
							+ "free__";
					i++;

				} else if (DaysProcedings[i - 1].endsWith("free") && i > 0) {
					newTextLine = newTextLine + moreChecks(Double.toString(oldstart).replace(".", ":"))
							+ ";" + moreChecks(Double.toString(endTime).replace(".", ":")) + ";"
							+ "free__";

				} else if (DaysProcedings[i + 1].endsWith("free")) {
					newTextLine = newTextLine + DaysProcedings[i - 1] + "__"
							+ moreChecks(Double.toString(oldend).replace(".", ":"))+ ";" 
							+ moreChecks(Double.toString(endTime1).replace(".", ":")) + ";" + "free__";
					i++;

				} else {
					newTextLine = newTextLine + DaysProcedings[i - 1] + "__"
							+ moreChecks(Double.toString(oldend).replace(".", ":")) + ";" 
							+ moreChecks(Double.toString(startTime1).replace(".", ":")) + ";" + "free__";
				}
				deleted = true;

			} else if (i > 0) {
				newTextLine = newTextLine + DaysProcedings[i - 1] + "__";
			}

			oldstart = startTime;
			oldend = endTime;

		}
		newTextLine = CurDate + ":-:" +newTextLine;
		System.out.println(newTextLine);
		wrightToFile1(newTextLine);
	}

	public static int checkMonth(int m, boolean leapYear) {

		if (m == 2 && leapYear) {
			return 29;
		} else {
			return MONTH_LENGTH[m - 1];
		}

		/*
		 * if (m == 1){ return 31; } if (m == 2){ if (leapYear == true){ return
		 * 29; } else{ return 28; } } if (m == 3){ return 31; } if (m == 4){
		 * return 30; } if (m == 5){ return 31; } if (m == 6){ return 30; } if
		 * (m == 7){ return 31; } if (m == 8){ return 31; } if (m == 9){ return
		 * 30; } if (m == 10){ return 31; } if (m == 11){ return 30; } if (m ==
		 * 12){ return 31; }
		 * 
		 * return 30;
		 */
	}

	public static boolean leapYear(int y) {
		int a = y % 4;
		if (a == 0) {
			return true;

		} else {
			return false;
		}

	}

}
