import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Schedule extends filecontrol {

	//Refreshes the schedule JTable with info that is in the file
	public static void refresh(Object startDate) {
		File CurrentPat = new File(dir);
		try {
			Scanner in = new Scanner(CurrentPat);
			Boolean start = true;
			
			while (in.hasNextLine() && start == true) {
				String data = in.nextLine();
				String date = data.substring(0, 10);
				if (date.equals(startDate)) {
					start = false;
					String[] separator = data.substring(13, data.length()).split("__");
					
					CurrentDay = separator;
					// remake the jtable to the proper amount of
					// rows
					DefaultTableModel model = new DefaultTableModel(separator.length, 4);

					table.setModel(model);
					for (int i = 0; i < table.getColumnCount(); i++) {
						TableColumn column1 = table.getTableHeader().getColumnModel().getColumn(i);
						column1.setHeaderValue(columnNames[i]);
					}
					table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(time));
					table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(time));
					table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(status));

					for (int i = 0; i < separator.length; i++) {
						String[] timeandevent = separator[i].split(";");
						table.setValueAt(timeandevent[0], i, 0);
						table.setValueAt(timeandevent[1], i, 1);
						table.setValueAt(timeandevent[2], i, 2);
					}
					addtablelistener();
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// get the selected date
	// get start time
	@SuppressWarnings("resource")
	public static String[] getCurrentDate(Date d) {
		
		BufferedReader br = null;
		String CurrentDays = null;
		File file = new File(dir);
		try {
			br = new BufferedReader(new FileReader(dir));
			for (int i = 0; i < file.length(); i++) {
				CurrentDays = br.readLine();
				if (CurrentDate1.equals(CurrentDays.substring(0, 10))) {
					CurrentDay = CurrentDays.substring(13, CurrentDays.length()).split("__");
					return CurrentDays.substring(13, CurrentDays.length()).split("__");
				}

			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static double getStartTime(String d) {
		if (d.length() == 4) {
			double startTime = Double.parseDouble(d.substring(0, 1)) + Double.parseDouble(d.substring(2, 4)) / 100;
			return startTime;
		} else if (d.length() == 5) {
			double startTime = Double.parseDouble(d.substring(0, 2)) + Double.parseDouble(d.substring(3, 5)) / 100;
			return startTime;

		} else {
			return 0.00;
		}
	}

	// checks
	public static boolean[] checks(double start, double end, double startA, double endA) {
		boolean[] gaps;

		if (startA - start >= 0.15 && end - endA >= 0.15) {
			gaps = new boolean[] { true, true };
			return gaps;
		} else if (startA - start >= 0.15) {
			gaps = new boolean[] { true, false };
			return gaps;
		} else if (end - endA >= 0.15) {
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

	// add, edit and delete appointments
	public static void AddApointment(String[] DaysProcedings, Double ApointStart, Double ApointEnd, String name) {

		

		String newTextLine = "";
		String newTimeSlots = "";
		String conflict = "";
		boolean change = false;
		boolean oneTime = false;
		boolean conflict2 = false;
		boolean didRun = false;
		boolean runYet = false;

		if(ApointEnd-ApointStart>=0.15){
		for (int i = 0; i < DaysProcedings.length; i++) {
			String[] timeSlot = DaysProcedings[i].split(";");
			double startTime = getStartTime(timeSlot[0]);
			double endTime = getStartTime(timeSlot[1]);
			String event = timeSlot[2];

			// if appointment start is greater than free start and appointment
			// end
			// is less then free end then cut a whole in free and place
			// appointment in
			if (ApointStart >= startTime && ApointEnd <= endTime && event.compareTo("free") == 0 && runYet == false) {
				boolean[] gaps = checks(startTime, endTime, ApointStart, ApointEnd);
				change = true;
				oneTime = true;
				didRun = true;
				runYet = true;

				if (gaps[0] == true && gaps[1] == true) {
					String freeStart = moreChecks(Double.toString(startTime).replace(".", ":"));
					String freeEnd = moreChecks(Double.toString(ApointStart).replace(".", ":"));
					String ApointBegin = moreChecks(Double.toString(ApointStart).replace(".", ":"));
					String ApointFinish = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
					String freeStart1 = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
					String freeEnd1 = moreChecks(Double.toString(endTime).replace(".", ":"));
					newTimeSlots = freeStart + ";" + freeEnd + ";free__" + ApointBegin + ";" + ApointFinish + ";" + name
							+ "__" + freeStart1 + ";" + freeEnd1 + ";free__";
				} else if (gaps[0] == true && gaps[1] == false) {
					String freeStart = moreChecks(Double.toString(startTime).replace(".", ":"));
					String freeEnd = moreChecks(Double.toString(ApointStart).replace(".", ":"));
					String ApointBegin = moreChecks(Double.toString(ApointStart).replace(".", ":"));
					String ApointFinish = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
					newTimeSlots = freeStart + ";" + freeEnd + ";free__" + ApointBegin + ";" + ApointFinish + ";" + name
							+ "__";

				} else if (gaps[0] == false && gaps[1] == true) {

					String ApointBegin = moreChecks(Double.toString(ApointStart).replace(".", ":"));
					String ApointFinish = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
					String freeStart1 = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
					String freeEnd1 = moreChecks(Double.toString(endTime).replace(".", ":"));
					newTimeSlots = ApointBegin + ";" + ApointFinish + ";" + name + "__" + freeStart1 + ";" + freeEnd1
							+ ";free__";
				} else if (gaps[0] == false && gaps[1] == false) {

					String ApointBegin = moreChecks(Double.toString(ApointStart).replace(".", ":"));
					String ApointFinish = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
					newTimeSlots = ApointBegin + ";" + ApointFinish + ";" + name + "__";

				}

			}

			// checks to see if the previous if statement did anything
			if (change == false) {
				// if appointment starts earlier then free start time and ends
				// earlier than end time then
				// create the appointment
				if (ApointStart < startTime && !(DaysProcedings[i - 1].endsWith("free")) && ApointEnd < endTime
						&& conflict.compareTo("") == 0) {
					if (ApointEnd > startTime && !(DaysProcedings[i].endsWith("free"))) {
						conflict = DaysProcedings[i - 1] + DaysProcedings[i];
						conflict2 = true;
					} else if (conflict.compareTo("") == 0) {
						conflict = DaysProcedings[i - 1];
						conflict2 = true;
					}
				}

				// if appointment starts earlier than free start time and ends
				// after end time of free time then delete free time
				if (ApointStart < startTime && ApointEnd > endTime) {
					if (!(DaysProcedings[i].endsWith("free")) && !(DaysProcedings[i + 1].endsWith("free"))
							&& !(DaysProcedings[i - 1].endsWith("free"))) {
						conflict = DaysProcedings[i - 1] + DaysProcedings[i] + DaysProcedings[i + 1];
						conflict2 = true;
					} else if (!(DaysProcedings[i + 1].endsWith("free")) && !(DaysProcedings[i - 1].endsWith("free"))) {
						conflict = DaysProcedings[i - 1] + DaysProcedings[i + 1];
						conflict2 = true;
					} else if (!(DaysProcedings[i].endsWith("free")) && !(DaysProcedings[i + 1].endsWith("free"))) {
						conflict = DaysProcedings[i] + DaysProcedings[i + 1];
						conflict2 = true;
					} else if (!(DaysProcedings[i].endsWith("free")) && !(DaysProcedings[i - 1].endsWith("free"))) {
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
		newTextLine = CurrentDate1 + ":-:" + newTextLine;
		if (conflict2 == false && didRun == true) {
			wrightToFile1(newTextLine);
		}
		}
	}
	public static void editApointment(String[] DaysProcedings, Double ApointStart, Double ApointEnd, String oldName,
			String newName) {

		boolean hasAdded = false;
		boolean freeBefore = false;
		boolean firstTime = true;
		double endTimeAfter = 0.0;
		double startTimeBefore = 0.0;
		String newFreeBefore = "";
		String newFreeAfter = "";
		String replace = "";
		String newTextLine = "";
		int x;

		if(ApointEnd-ApointStart>=0.15){
			
		

		boolean IsConflict = checkForConflicts(DaysProcedings, ApointStart, ApointEnd, oldName);

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
					String newStart = moreChecks(Double.toString(ApointStart).replace(".", ":"));
					String newEnd = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
					replace = newStart + ";" + newEnd + ";" + newName + "__";

					if (endTime - ApointEnd >= 0.15) {
						String tempS1 = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
						String tempE1 = moreChecks(Double.toString(endTime).replace(".", ":"));
						newFreeAfter = tempS1 + ";" + tempE1 + ";" + "free__";
					}
					if (ApointStart - startTime >= 0.15) {
						String tempS2 = moreChecks(Double.toString(startTime).replace(".", ":"));
						String tempE2 = moreChecks(Double.toString(ApointStart).replace(".", ":"));
						newFreeBefore = tempS2 + ";" + tempE2 + ";" + "free__";
					}

					if (i + 1 != DaysProcedings.length) {
						String[] temp = DaysProcedings[i + 1].split(";");

						getStartTime(temp[0]);
						endTimeAfter = getStartTime(temp[1]);
					}

					if (i > 0) {
						String[] temp1 = DaysProcedings[i - 1].split(";");

						startTimeBefore = getStartTime(temp1[0]);
						getStartTime(temp1[1]);

						if (DaysProcedings[i - 1].endsWith("free") && (i + 1 != DaysProcedings.length)
								&& DaysProcedings[i + 1].endsWith("free")) {

							System.out.println("check 1");
							if (endTimeAfter - ApointEnd >= 0.15) {
								String tempS3 = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
								String tempE3 = moreChecks(Double.toString(endTimeAfter).replace(".", ":"));
								newFreeAfter = tempS3 + ";" + tempE3 + ";" + "free__";
							}

							if (ApointStart - startTimeBefore >= 0.15) {
								String tempS4 = moreChecks(Double.toString(startTimeBefore).replace(".", ":"));
								String tempE4 = moreChecks(Double.toString(ApointStart).replace(".", ":"));
								newFreeBefore = tempS4 + ";" + tempE4 + ";" + "free__";
							}

							i++;
							freeBefore = true;

						} else if (DaysProcedings[i - 1].endsWith("free")) {

							if (ApointStart - startTimeBefore >= 0.15) {
								String tempS4 = moreChecks(Double.toString(startTimeBefore).replace(".", ":"));
								String tempE4 = moreChecks(Double.toString(ApointStart).replace(".", ":"));
								newFreeBefore = tempS4 + ";" + tempE4 + ";" + "free__";

							}
							freeBefore = true;

						} else if (DaysProcedings[i + 1].endsWith("free") && (i + 1 != DaysProcedings.length)) {

						System.out.println("check 1");
						if (endTimeAfter - ApointEnd >= 0.15) {
							String tempS3 = moreChecks(Double.toString(ApointEnd).replace(".", ":"));
							String tempE3 = moreChecks(Double.toString(endTimeAfter).replace(".", ":"));
							newFreeAfter = tempS3 + ";" + tempE3 + ";" + "free__";
						}
						i++;
					}

					newTextLine = newTextLine + newFreeBefore + replace + newFreeAfter;
					hasAdded = true;
					}
				}

				if (freeBefore == false && hasAdded == true && firstTime == true) {
					String P = newTextLine;
					newTextLine = "";
					for (int b = 0; b < x; b++) {
						newTextLine = newTextLine + DaysProcedings[b] + "__";
					}
					newTextLine = newTextLine + P;
					firstTime = false;

				}
				if (freeBefore == true) {
					freeBefore = false;
					String P = newTextLine;
					newTextLine = "";
					for (int b = 0; b < x - 1; b++) {
						newTextLine = newTextLine + DaysProcedings[b] + "__";
					}
					newTextLine = newTextLine + P;
					firstTime = false;
				}
			}
		}

		newTextLine = CurrentDate1 + ":-:" + newTextLine;
		if (IsConflict == false) {
			wrightToFile1(newTextLine);
		}
	}
}
	public static void deleteApointment(String[] DaysProcedings, String name) {
		/*
		 This method is shorter then the others because less variable checking is required simply delete the appointment and
		 replace it with a free time slot the only variable checking being done is whether there is a need to merge the free
		 time slots with pre exsiting free time slots
		 */
			
			String newTextLine = "";
			double startTime = 0.0;
			double endTime = 0.0;
			double startTime1 = 0.0;
			double endTime1 = 0.0;
			double oldstart = 0.0;
			double oldend = 7.30;
			boolean deleted = false;

			for (int i = 0; i < DaysProcedings.length; i++) {

				String[] timeSlot = DaysProcedings[i].split(";");
				startTime = getStartTime(timeSlot[0]);
				endTime = getStartTime(timeSlot[1]);

				if (deleted == true) {
					newTextLine = newTextLine + DaysProcedings[i] + "__";
				} else if (timeSlot[2].compareTo(name) == 0) {
					System.out.println("check 3");
					
					if(DaysProcedings.length >= (i+2)){
						
					String[] timeSlot2 = DaysProcedings[i + 1].split(";");
					startTime1 = getStartTime(timeSlot2[0]);
					endTime1 = getStartTime(timeSlot2[1]);
					}

					
					if (i-1 >= 0 && DaysProcedings[i - 1].endsWith("free") && DaysProcedings.length >= i+2 && DaysProcedings[i + 1].endsWith("free") && i > 0) {
						String tempS3 = moreChecks(Double.toString(oldstart).replace(".", ":"));
						String tempE3 = moreChecks(Double.toString(endTime1).replace(".", ":"));
						newTextLine = newTextLine + tempS3 + ";" + tempE3 + ";" + "free__";
						i++;

					} else if (i- 1>= 0 && DaysProcedings[i - 1].endsWith("free") && i > 0) {
						String tempS3 = moreChecks(Double.toString(oldstart).replace(".", ":"));
						String tempE3 = moreChecks(Double.toString(endTime).replace(".", ":"));
						newTextLine = newTextLine + tempS3 + ";" + tempE3 + ";" + "free__";
						

					} else if (DaysProcedings[i + 1].endsWith("free")) {
						String tempS3 = moreChecks(Double.toString(oldend).replace(".", ":"));
						String tempE3 = moreChecks(Double.toString(endTime1).replace(".", ":"));
						String temp1 = moreChecks(Double.toString(startTime).replace(".", ":"));
						String temp2 = moreChecks(Double.toString(endTime).replace(".", ":"));
						if(i-1<0){
							newTextLine = newTextLine + temp1 + ";" + tempE3 + ";" + "free__";
							i++;
						}else{
						newTextLine = newTextLine + DaysProcedings[i - 1] + "__" + tempS3 + ";" + tempE3 + ";" + "free__";
						i++;
						}

					} else {
						String tempS3 = moreChecks(Double.toString(oldend).replace(".", ":"));
						String tempE3 = moreChecks(Double.toString(startTime1).replace(".", ":"));
						String temp1 = moreChecks(Double.toString(startTime).replace(".", ":"));
						String temp2 = moreChecks(Double.toString(endTime).replace(".", ":"));
						if(i-1<0){
							newTextLine = newTextLine + temp1 + ";" + temp2 + ";" + "free__";
						}else{
						newTextLine = newTextLine + DaysProcedings[i - 1] + "__" + tempS3 + ";" + tempE3 + ";"
								+ "free__";
						}
					}
					deleted = true;
					
				} else if (i > 0 && deleted == false) {
					newTextLine = newTextLine + DaysProcedings[i - 1] + "__";
				}

				
				oldstart = startTime;
				oldend = endTime;

			}
			newTextLine = CurrentDate1 + ":-:" + newTextLine;
			wrightToFile1(newTextLine);
		}

	// write information to files
	public static void wrightToFile1(String line) {

		String CurrentDate = line.substring(0, 10);
		BufferedReader br = null;
		BufferedReader br2 = null;
		String input = "";
		String CurrentDay = "";
		File file = new File(dir);

		try {
			br = new BufferedReader(new FileReader(dir));
			br2 = new BufferedReader(new FileReader(dir));
			if (file.exists()) {
				while (br2.readLine() != null) {
					CurrentDay = br.readLine();
					if (CurrentDate.compareTo(CurrentDay.substring(0, 10)) == 0) {
						input = input + line + '\n';
					} else {
						input = input + CurrentDay + '\n';
					}
				}
				FileOutputStream fileOut = new FileOutputStream(dir);
				fileOut.write(input.getBytes());
				fileOut.close();
			}
			br.close();
			br2.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println(CurrentDate1);
		refresh(CurrentDate1);
	}

	// checks for conflicts between added appointment and appointments in the
	// file
	public static boolean checkForConflicts(String[] DaysProcedings, Double ApointStart, Double ApointEnd,
			String name) {
		String conflict = "";
		boolean hasStarted = false;

		for (int i = 0; i < DaysProcedings.length; i++) {
			String[] timeSlot = DaysProcedings[i].split(";");
			double startTime = 0;
			double endTime = 0;

			startTime = getStartTime(timeSlot[0]);
			endTime = getStartTime(timeSlot[1]);

			if (ApointEnd > startTime && !(timeSlot[2].compareTo(name) == 0) && hasStarted == true
					&& !(timeSlot[2].compareTo("free") == 0)) {
				conflict = conflict + DaysProcedings[i];

				return true;

			} else if (ApointStart >= startTime && endTime > ApointStart) {
				hasStarted = true;
				if (!(timeSlot[2].compareTo("free") == 0) && timeSlot[2].compareTo(name) != 0) {
					conflict = conflict + DaysProcedings[i];

					return true;
				}

			}

		}
		return false;
	}

	// check for leap year and adjust month accordingly
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
