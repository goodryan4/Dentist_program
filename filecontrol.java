import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class filecontrol extends GUI implements TableModelListener{
	public static String[] information;
	public static File CurrentPat;
	public static String dir = "src/table/schedule.txt";
	public static String path, day, month, date, year, CurrentDate;
	public static String [] CurrentDay;
	public static boolean newEvent = false;

	public static final int[] MONTH_LENGTH = new int[] { 31, 28, 31, 30, 31,
			30, 31, 31, 30, 31, 30, 31 };

	// Make each file with blank info
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void instantiat(String dir, String file) {
		String nullData = " :; :; :; :; :; :; :; :; :; :; :; :; :; :;";
		String directory = dir + file;
		try {
			PrintWriter writer = new PrintWriter(directory);
			writer.print(nullData);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Get data from files
	public static String[] getData(String name, String type) {
		path = GUI.directory + "/" + name + "/" + type + ".txt";
		File CurrentPat = new File(path);
		try {
			Scanner in = new Scanner(CurrentPat);
			String data = in.nextLine();
			System.out.println(data);
			information = data.split(":;");
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return information;
	}

	// Adds new info to info page
	public static void newInfo(String data, int position) throws IOException {

		information[position] = data;
		String textline = "";
		for (int i = 0; i < information.length; i++) {
			textline = textline + information[i].toString() + ":";
		}
		PrintWriter writer = new PrintWriter(path);
		writer.print(textline);
		writer.close();

	}

	// For look through all patients and calling "removefolder" method
	public static void removeallfolders() {
		File last = new File(GUI.directory);
		File[] things = last.listFiles();
		for (File file : things) {
			removefolder(file.getName());
		}
	}

	// Remove patient and folders involved with the patient
	public static void removefolder(String name) {
		File last = new File(GUI.directory + "/" + name);
		File[] things = last.listFiles();
		for (File file : things) {
			file.delete();
		}
		last.delete();
		GUI.check.setText("remove " + name);
		GUI.list.remove(name.toLowerCase());
	}

	// This is the initial check to see what patients are in the patents folder
	public static void filestolist(List list, File bob) {
		File[] files = bob.listFiles();
		int numfiles = files.length;
		if (numfiles == 0) {
			list.add("There are no patients in the list");
		} else {
			for (int i = 0; i < numfiles; i++) {
				if (files[i].getName().contains(text.getText().toLowerCase())) {
					list.add(files[i].getName());
				}
			}
		}
	}

	// Add a patient to the list and to the file
	public static void addentry() {
		String name = text.getText().toLowerCase();
		String dir = directory + "/" + name;
		File add = new File(dir);
		System.out.println(name);
		if (name.equals("enter the person you wish to search")) {
			check.setText("Enter patient name in search bar first");
		} else if (add.exists()) {
			check.setText("the patient already exist");
		} else {
			add.mkdir();
			try {
				String[] files = { "/allinfo.txt", "/info.txt",
						"/procedure.txt", "/balance.txt" };
				for (int i = 0; i < files.length; i++) {
					new File(dir + files[i]).createNewFile();
					filecontrol.instantiat(dir, files[i]);
				}
				check.setText("added " + name);
				list.add(name);
				// check for "There are no names" item in list and if it is
				// there then delete it
				if (list.getItem(0).contains(
						"There are no patients in the list")) {
					list.remove(0);
				}
				text.setText("");
				list.requestFocus();
				list.removeAll();
				filestolist(list, bob);
				text.setText("Enter the person you wish to search");
				text.setForeground(Color.gray);
				text.setHorizontalAlignment(SwingConstants.CENTER);
				// this should never happen!! but if there is an error creating
				// the files then the it will tell user "Failed to add"
			} catch (IOException e) {
				check.setText("Failed to add");
				e.printStackTrace();
			}
		}
	}

	// Sets the data to each textField
	public static void setData() {
		for (int i = 0; i < TextFields.length; i++) {
			String a = currentData[i].substring(1, currentData[i].length());
			TextFields[i].setText(a);
		}
		for (int i = 0; i < textareas.length; i++) {
			String a = currentData[TextFields.length + i].substring(1,
					currentData[TextFields.length + i].length());
			textareas[i].setText(a);
		}
	}

	// Set textFields to editable or disabled as well as sends info to files
	public static void updatetoggle() {
		if (btnUpdateInfo.isSelected()) {
			for (int i = 0; i < TextFields.length; i++) {
				TextFields[i].setEditable(true);
			}
			for (int i = 0; i < textareas.length; i++) {
				textareas[i].setEditable(true);
			}
		} else {
			String[] a = new String[TextFields.length + textareas.length];
			for (int i = 0; i < TextFields.length; i++) {
				TextFields[i].setEditable(false);
				a[i] = TextFields[i].getText();
			}
			for (int i = 0; i < textareas.length; i++) {
				textareas[i].setEditable(false);
				a[TextFields.length + i] = textareas[i].getText();
			}
			setfiledata(name, currjpanel, a);
		}
	}

	/** Add all objects **/
	public static void addobjects(JPanel a) {

		if (a.equals(info) || a.equals(procedure) || a.equals(allinfo)) {
			currjpanel = a;
			lblFirstName = new JLabel("First Name: ");
			lblFirstName.setBounds(10, 30, 100, 14);
			a.add(lblFirstName);

			lblLastName = new JLabel("Last Name:");
			lblLastName.setBounds(10, 60, 100, 14);
			a.add(lblLastName);

			lblSex = new JLabel("Sex:");
			lblSex.setBounds(10, 90, 100, 14);
			a.add(lblSex);

			lblDateOfBirth = new JLabel("Date of Birth:");
			lblDateOfBirth.setBounds(10, 120, 100, 14);
			a.add(lblDateOfBirth);

			lblPostalCode = new JLabel("Postal code:");
			lblPostalCode.setBounds(10, 150, 100, 14);
			a.add(lblPostalCode);

			lblPhoneNumber = new JLabel("Phone number:");
			lblPhoneNumber.setBounds(10, 180, 100, 14);
			a.add(lblPhoneNumber);

			lblHealthNumber = new JLabel("Health Number:");
			lblHealthNumber.setBounds(10, 210, 100, 14);
			a.add(lblHealthNumber);

			lblHealthIssues = new JLabel("Health Issues:");
			lblHealthIssues.setBounds(250, 30, 175, 14);
			a.add(lblHealthIssues);

			// TextFields
			textField = new JTextField();
			textField.setEditable(false);
			textField.setBounds(120, 32, 86, 20);
			a.add(textField);

			textField_1 = new JTextField();
			textField_1.setEditable(false);
			textField_1.setBounds(120, 60, 86, 20);
			a.add(textField_1);

			textField_2 = new JTextField();
			textField_2.setEditable(false);
			textField_2.setBounds(120, 90, 86, 20);
			a.add(textField_2);

			textField_3 = new JTextField();
			textField_3.setEditable(false);
			textField_3.setBounds(120, 120, 86, 20);
			a.add(textField_3);

			textField_4 = new JTextField();
			textField_4.setEditable(false);
			textField_4.setBounds(120, 150, 86, 20);
			a.add(textField_4);

			textField_5 = new JTextField();
			textField_5.setEditable(false);
			textField_5.setBounds(120, 180, 86, 20);
			a.add(textField_5);

			// update button
			btnUpdateInfo = new JToggleButton("Update Info");
			btnUpdateInfo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					filecontrol.updatetoggle();
				}
			});
			btnUpdateInfo.setBounds(463, 60, 101, 23);
			a.add(btnUpdateInfo);

			// button to go back to the main page
			btnHome = new JButton("Home");
			btnHome.setBounds(463, 30, 101, 24);
			btnHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					removeobjects();
					hidepanels(search);
				}
			});
			a.add(btnHome);

			textarea = new JTextArea();
			textarea.setEditable(false);
			textarea.setLineWrap(true);
			textarea.setWrapStyleWord(true);
			textarea.setBounds(250, 60, 175, 70);
			a.add(textarea);

			textarea_1 = new JTextArea();
			textarea_1.setEditable(false);
			textarea_1.setLineWrap(true);
			textarea_1.setWrapStyleWord(true);
			textarea_1.setBounds(250, 180, 175, 70);
			a.add(textarea_1);

			// info page setup
			if (a.equals(info)) {
				lblMedicalNotes = new JLabel("Medical Notes:");
				lblMedicalNotes.setBounds(250, 150, 175, 14);
				a.add(lblMedicalNotes);

				textField_6 = new JTextField();
				textField_6.setEditable(false);
				textField_6.setBounds(120, 210, 86, 20);
				a.add(textField_6);

				hidebuttons(a);

			} else if (a.equals(procedure)) {
				lblSex.setText("procedure date:");
				lblDateOfBirth.setText("Procedure starts:");
				lblPostalCode.setText("Procedure ends:");

				lblPhoneNumber.setText("Procedure:");
				lblPhoneNumber.setBounds(10, 180, 200, 14);

				lblHealthNumber.setText("Discriptions:");
				lblHealthNumber.setBounds(10, 210, 100, 14);

				lblHealthIssues.setText("Additional rules:");
				lblHealthIssues.setBounds(250, 210, 175, 14);

				textarea.setBounds(10, 230, 175, 70);
				textarea_1.setBounds(250, 230, 175, 70);

				hidebuttons(a);

			} else if (a.equals(allinfo)) {
				textField_6 = new JTextField();
				textField_6.setEditable(false);
				textField_6.setBounds(120, 210, 86, 20);
				a.add(textField_6);

				textField_7 = new JTextField();
				textField_7.setEditable(false);
				textField_7.setBounds(326, 30, 86, 20);
				a.add(textField_7);

				textField_8 = new JTextField();
				textField_8.setEditable(false);
				textField_8.setBounds(326, 60, 86, 20);
				a.add(textField_8);

				textField_9 = new JTextField();
				textField_9.setEditable(false);
				textField_9.setBounds(326, 90, 86, 20);
				a.add(textField_9);

				lblHealthIssues.setText("Additional rules:");
				lblHealthIssues.setBounds(215, 30, 175, 14);

				lblMedicalNotes = new JLabel("Medical Notes:");
				lblMedicalNotes.setBounds(215, 60, 175, 14);
				a.add(lblMedicalNotes);

				a.remove(textarea);
				a.remove(textarea_1);

				hidebuttons(a);
			}
		}
		if (a.equals(schedule)) {
			JLabel lblNewLabel = new JLabel("change date");
			lblNewLabel.setBounds(463, 50, 92, 26);
			a.add(lblNewLabel);

			status = new JComboBox();
			status.addItem("waiting");
			status.addItem("done");
			status.addItem("coming soon");
			status.addItem("in progress");

			String[] columnNames = { "Start Time", "End Time", "Event",
					"Status" };

			table = new JTable(4, 4);
			table.setBounds(20, 20, 40, 330);
			table.setCellSelectionEnabled(true);
			table.setRowHeight(30);
			

			JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(20, 20, 430, 330);
			scrollPane_1.setViewportView(table);
			scrollPane_1.setBorder(BorderFactory.createEmptyBorder());
			scrollPane_1.setColumnHeaderView(table.getTableHeader());
			a.add(scrollPane_1);

			listdates = new JComboBox();
			listdates.setBounds(463, 70, 100, 25);
			listdates.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					File CurrentPat = new File(dir);
					try {
						Scanner in = new Scanner(CurrentPat);
						Boolean start = true;
						while (in.hasNextLine() && start == true) {
							String data = in.nextLine();
							String date = data.substring(0, 10);
							if (date.equals(listdates.getSelectedItem())) {
								start = false;
								String[] separator = data.substring(13,
										data.length()).split("__");
								CurrentDay = separator;
								// remake the jtable to the proper amount of
								// rows
								DefaultTableModel model = new DefaultTableModel(
										separator.length, 4);
								
								table.setModel(model);
								
								for (int i = 0; i < table.getColumnCount(); i++) {
									TableColumn column1 = table
											.getTableHeader().getColumnModel()
											.getColumn(i);

									column1.setHeaderValue(columnNames[i]);
								}
								table.getColumnModel()
										.getColumn(3)
										.setCellEditor(
												new DefaultCellEditor(status));

								for (int i = 0; i < separator.length; i++) {
									String[] timeandevent = separator[i]
											.split(";");
									table.setValueAt(timeandevent[0], i, 0);
									table.setValueAt(timeandevent[1], i, 1);
									table.setValueAt(timeandevent[2], i, 2);
								}
								
							}
						}
						in.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					
				}
			});
			a.add(listdates);
			if (new File(dir).exists()) {
				try {
					fixdates();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Date date = new Date();
				fileInit(date);
			}
			adddates();

			btnHome = new JButton("Home");
			btnHome.setBounds(463, 30, 101, 24);
			btnHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					hidepanels(search);
				}
			});
			a.add(btnHome);

			btnAddEvent = new JButton("Add Event");
			btnAddEvent.setBounds(463, 100, 100, 25);
			btnAddEvent.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (table.getValueAt(0, 0).equals("")
							|| table.getValueAt(0, 1).equals("")
							|| table.getValueAt(0, 2).equals("")) {
					} else {
						newEvent = true;
						DefaultTableModel model = new DefaultTableModel(0, 4);
						model.addRow(new Object[] { "", "", "", "" });
						Object[] data = new Object[table.getColumnCount()];
						for (int i = 0; i < table.getRowCount(); i++) {
							for (int j = 0; j < table.getColumnCount(); j++) {
								data[j] = table.getValueAt(i, j);
							}
							model.addRow(data);
						}
						table.setModel(model);
						
						for (int i = 0; i < table.getColumnCount(); i++) {
							TableColumn column1 = table.getTableHeader()
									.getColumnModel().getColumn(i);

							column1.setHeaderValue(columnNames[i]);
						}
						table.getColumnModel().getColumn(3)
								.setCellEditor(new DefaultCellEditor(status));
					}
				}
			});
			a.add(btnAddEvent);
		}

	}
	

	/** Remove all objects from other JPanels **/
	public static void removeobjects() {
		JPanel a = null;
		if (info.isShowing()) {
			a = info;
		} else if (procedure.isShowing()) {
			a = procedure;
		} else if (allinfo.isShowing()) {
			a = allinfo;
		} else if (schedule.isShowing()) {
			a = schedule;
		}

		if (btnUpdateInfo.isSelected()) {
			btnUpdateInfo.doClick();
		}

		a.remove(lblFirstName);
		a.remove(lblLastName);
		a.remove(lblSex);
		a.remove(lblDateOfBirth);
		a.remove(lblPostalCode);
		a.remove(lblPhoneNumber);
		a.remove(lblHealthNumber);
		a.remove(lblHealthIssues);
		a.remove(textField);
		a.remove(textField_1);
		a.remove(textField_2);
		a.remove(textField_3);
		a.remove(textField_4);
		a.remove(textField_5);
		a.remove(btnUpdateInfo);
		a.remove(btnHome);
		a.remove(textarea);
		a.remove(textarea_1);

		// info page setup
		if (a.equals(info)) {
			a.remove(lblMedicalNotes);
			a.remove(textField_6);
			a.remove(btnHome);

		} else if (a.equals(allinfo)) {
			a.remove(textField_6);
			a.remove(textField_7);
			a.remove(textField_8);
			a.remove(textField_9);
			a.remove(lblMedicalNotes);
			a.remove(textarea);
			a.remove(textarea_1);

		}
	}

	// Hide all panels other than the input panel which is shown
	public static void hidepanels(JPanel panelbeingshown) {
		if (panelbeingshown.equals(procedure)) {
			allinfo.hide();
			info.hide();
			schedule.hide();
			search.hide();
			procedure.show();
		} else if (panelbeingshown.equals(allinfo)) {
			procedure.hide();
			info.hide();
			schedule.hide();
			search.hide();
			allinfo.show();
		} else if (panelbeingshown.equals(info)) {
			procedure.hide();
			allinfo.hide();
			schedule.hide();
			search.hide();
			info.show();
		} else if (panelbeingshown.equals(schedule)) {
			procedure.hide();
			info.hide();
			allinfo.hide();
			search.hide();
			schedule.show();
		} else if (panelbeingshown.equals(search)) {
			procedure.hide();
			allinfo.hide();
			info.hide();
			schedule.hide();
			search.show();
		}
	}

	// Hide all other buttons beside what is called
	public static void hidebuttons(JPanel a) {
		if (a.equals(allinfo)) {
			createbutton1(a, 30, 326, 140, 35);
			createbutton2(a, 180, 326, 140, 35);
			createbutton4(a, 450, 326, 123, 35);

		} else if (a.equals(procedure)) {
			createbutton1(a, 30, 326, 140, 35);
			createbutton3(a, 180, 326, 140, 35);
			createbutton4(a, 450, 326, 140, 35);

		} else if (a.equals(info)) {
			createbutton2(a, 30, 326, 140, 35);
			createbutton3(a, 180, 326, 140, 35);
			createbutton4(a, 450, 326, 140, 35);

		} else if (a.equals(schedule)) {
			createbutton1(a, 30, 326, 140, 35);
			createbutton2(a, 180, 326, 140, 35);
			createbutton3(a, 330, 326, 140, 35);
		}
	}

	// Patient info button
	public static void createbutton1(JPanel a, int b, int c, int d, int e) {
		btnGoToPatientInfo = new JButton("Go to Patient Info");
		btnGoToPatientInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeobjects();
				addobjects(info);
				TextFields = new JTextField[] { textField, textField_1,
						textField_2, textField_3, textField_4, textField_5,
						textField_6 };
				textareas = new JTextArea[] { textarea, textarea_1 };
				currentData = filecontrol.getData(list.getSelectedItem()
						.toString(), "info");
				setData();
				hidepanels(info);
			}
		});
		btnGoToPatientInfo.setBounds(b, c, d, e);
		a.add(btnGoToPatientInfo);
	}

	// Procedure button
	public static void createbutton2(JPanel a, int b, int c, int d, int e) {
		btnGoToProcedure = new JButton("Go to procedure");
		btnGoToProcedure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeobjects();
				addobjects(procedure);
				TextFields = new JTextField[] { textField, textField_1,
						textField_2, textField_3, textField_4, textField_5,
						textField_6 };
				textareas = new JTextArea[] { textarea, textarea_1 };
				currentData = filecontrol.getData(list.getSelectedItem()
						.toString(), "procedure");
				setData();
				hidepanels(procedure);
			}
		});
		btnGoToProcedure.setBounds(b, c, d, e);
		a.add(btnGoToProcedure);
	}

	// All info button
	public static void createbutton3(JPanel a, int b, int c, int d, int e) {
		btnGoToCompressed = new JButton("Go to compressed info");
		btnGoToCompressed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeobjects();
				addobjects(allinfo);
				TextFields = new JTextField[] { textField, textField_1,
						textField_2, textField_3, textField_4, textField_5,
						textField_6, textField_7, textField_8, textField_9 };
				currentData = filecontrol.getData(list.getSelectedItem()
						.toString(), "allinfo");
				setData();
				hidepanels(allinfo);
			}
		});
		btnGoToCompressed.setBounds(b, c, d, e);
		a.add(btnGoToCompressed);
	}

	// Schedule button
	public static void createbutton4(JPanel a, int b, int c, int d, int e) {
		btnSchedule = new JButton("Schedule");
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeobjects();
				filecontrol.hidepanels(schedule);
				filecontrol.hidebuttons(schedule);
			}
		});
		btnSchedule.setBounds(b, c, d, e);
		a.add(btnSchedule);
	}

	// Write info given to a file
	public static void setfiledata(String name, JPanel type, String[] info) {
		path = GUI.directory + "/" + name + "/" + type.getName() + ".txt";
		File CurrentPat = new File(path);
		String data = "";
		for (int i = 0; i < info.length; i++) {
			data = data + " " + info[i] + ":;";
		}
		try {
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			writer.print(data);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Initialize file
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
			writer.print(date
					+ ":-:7:00;11:30;free__11:30;12:30;lunch__12:30;14:00;free");
			if (dateY < startY + 1 || dateD != startD || dateM != startM) {
				writer.println("");
			}
			CurrMonth = checkMonth(dateM, leapYear1);
		}
		writer.close();

	}

	//
	public static void updateDates() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String CurDate = dateFormat.format(date).toString();
		int dateD = Integer.parseInt(CurDate.substring(0, 2));
		int dateM = Integer.parseInt(CurDate.substring(3, 5));
		int dateY = Integer.parseInt(CurDate.substring(6, 10));

		String lastDate = "02/10/2017"; // going to get date from file
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

	// get the selected date
	public static String[] getCurrentDate(Date d) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String CurrentDate = dateFormat.format(d).toString();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(dir));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String CurrentDays = null;
		File file = new File(dir);
		for (int i = 0; i < file.length(); i++) {
			try {
				CurrentDays = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(CurrentDay.substring(0,10));
			if (CurrentDate.compareTo(CurrentDays.substring(0, 10)) == 0) {

				CurrentDay = CurrentDays.substring(13, CurrentDays.length())
						.split("__");
				return CurrentDays.substring(13, CurrentDays.length())
						.split("__");
			}

		}
		return null;
	}

	// add appointment
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
		newTextLine = CurrentDate + ":-:" + newTextLine;
		System.out.println(newTextLine);
		System.out.println(conflict);
		if (conflict2 == false && didRun == true) {
			writeToFile1(newTextLine);
		}

	}

	// checks
	public static boolean[] checks(double start, double end, double startA,
			double endA) {
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

	// checks
	public static String moreChecks(String m) {
		if (m.indexOf(":") == m.length() - 2) {
			m = m + "0";

			return m;
		}

		return m;
	}

	// edit appointment
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
					newTextLine = newTextLine + DaysProcedings[i];
				}

				if (timeSlot[2].compareTo(oldName) == 0) {
					String newStart = moreChecks(Double.toString(ApointStart)
							.replace(".", ":"));
					String newEnd = moreChecks(Double.toString(ApointEnd)
							.replace(".", ":"));
					replace = newStart + ";" + newEnd + ";" + newName + "__";

					if (endTime - ApointEnd >= 0.15) {
						String tempS1 = moreChecks(Double.toString(ApointEnd)
								.replace(".", ":"));
						String tempE1 = moreChecks(Double.toString(endTime)
								.replace(".", ":"));
						newFreeAfter = tempS1 + ";" + tempE1 + ";" + "free__";
					}
					if (ApointStart - startTime >= 0.15) {
						String tempS2 = moreChecks(Double.toString(startTime)
								.replace(".", ":"));
						String tempE2 = moreChecks(Double.toString(ApointStart)
								.replace(".", ":"));
						newFreeBefore = tempS2 + ";" + tempE2 + ";" + "free__";
					}

					if (i + 1 != DaysProcedings.length) {
						String[] temp = DaysProcedings[i + 1].split(";");

						startTimeAfter = getStartTime(temp[0]);
						endTimeAfter = getStartTime(temp[1]);
					}

					if (i > 0) {
						String[] temp1 = DaysProcedings[i - 1].split(";");

						startTimeBefore = getStartTime(temp1[0]);
						endTimeBefore = getStartTime(temp1[1]);

						if (DaysProcedings[i - 1].endsWith("free")
								&& (i + 1 != DaysProcedings.length)
								&& DaysProcedings[i + 1].endsWith("free")) {

							if (endTimeAfter - ApointEnd >= 0.15) {
								String tempS3 = moreChecks(Double.toString(
										ApointEnd).replace(".", ":"));
								String tempE3 = moreChecks(Double.toString(
										endTimeAfter).replace(".", ":"));
								newFreeAfter = tempS3 + ";" + tempE3 + ";"
										+ "free__";
							}
							if (ApointStart - endTimeBefore >= 0.15) {
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
					} else if (DaysProcedings[i + 1].endsWith("free")
							&& (i + 1 != DaysProcedings.length)) {

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

		newTextLine = CurrentDate + ":-:" + newTextLine;
		System.out.println(newTextLine);
		writeToFile1(newTextLine);
	}

	// write information to files
	public static void writeToFile1(String line) {

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
		if (file.exists()) {
			try {
				while (br2.readLine() != null) {

					try {

						CurrentDay = br.readLine();

						if (CurrentDate.compareTo(CurrentDay.substring(0, 10)) == 0) {

							System.out.println("check4");
							input = input + line + '\n';
						} else {
							input = input + CurrentDay + '\n';
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println(CurrentDay.substring(0, 10) + "and"
							+ CurrentDate);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				FileOutputStream fileOut = new FileOutputStream(dir);
				fileOut.write(input.getBytes());
				fileOut.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// get start time
	public static double getStartTime(String d) {
		
		if (d.length() < 5 && d.length() == 4) {
			double startTime = Double.parseDouble(d.substring(0, 1))
					+ Double.parseDouble(d.substring(2, 4)) / 100;
			return startTime;
		} else if(d.length() == 5){
			double startTime = Double.parseDouble(d.substring(0, 2))
					+ Double.parseDouble(d.substring(3, 5)) / 100;
			return startTime;

		}
		else{
			return 0.00;
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
					newTextLine = newTextLine + oldstart + ";" + endTime1 + ";"
							+ "free__";
					i++;

				} else if (DaysProcedings[i - 1].endsWith("free") && i > 0) {
					newTextLine = newTextLine + oldstart + ";" + endTime + ";"
							+ "free__";

				} else if (DaysProcedings[i + 1].endsWith("free")) {
					newTextLine = newTextLine + DaysProcedings[i - 1] + "__"
							+ oldend + ";" + endTime1 + ";" + "free__";
					i++;

				} else {
					newTextLine = newTextLine + DaysProcedings[i - 1] + "__"
							+ oldend + ";" + startTime1 + ";" + "free__";
				}
				deleted = true;

			} else if (i > 0) {
				newTextLine = newTextLine + DaysProcedings[i - 1] + "__";
			}

			oldstart = startTime;
			oldend = endTime;

		}
		System.out.println(newTextLine);
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

	// add current date to 1 year ahead to combobox
	private static void adddates() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String CurDate = dateFormat.format(date);

		int dateD, dateM, dateY, lastDateD, lastDateM, lastDateY;
		String date2 = "";

		dateD = lastDateD = Integer.parseInt(CurDate.substring(0, 2));
		lastDateM = dateM = Integer.parseInt(CurDate.substring(3, 5));
		dateY = Integer.parseInt(CurDate.substring(6, 10));
		lastDateY = dateY + 1;
		dateM--;
		if (dateM == -1) {
			dateM = 12;
			dateY--;
		}

		boolean leapYear1 = leapYear(lastDateY);
		int CurrMonth = checkMonth(lastDateM, leapYear1);
		if (lastDateM == 2 && lastDateD == 29) {
			lastDateD = lastDateD - 1;
		}

		while (dateY != lastDateY || dateM != lastDateM || dateD != lastDateD) {
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
			year = "" + dateY;
			date2 = day + "/" + month + "/" + year;
			listdates.addItem(date2);

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

			CurrMonth = checkMonth(dateM, leapYear1);
		}
	}

	/**
	 * get the current date and delete all info from the file that is before the
	 * /date then add dates after until 1 year ahead of the current date
	 *
	 * @throws IOException
	 */
	public static void fixdates() throws IOException {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String CurDate = dateFormat.format(date).toString();
		int dateD = Integer.parseInt(CurDate.substring(0, 2));
		int dateM = Integer.parseInt(CurDate.substring(3, 5)) - 1;
		int dateY = Integer.parseInt(CurDate.substring(6, 10));
		if (dateM == -1) {
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
		CurDate = day + "/" + month + "/" + dateY;

		File file = new File(dir);
		Scanner scan = new Scanner(file);

		boolean start = false;
		String[] bob = null;

		LineNumberReader lnr = new LineNumberReader(new FileReader(file));
		lnr.skip(Long.MAX_VALUE);
		int numlinesinfile = lnr.getLineNumber();
		lnr.close();

		int i = 0;
		String datescanned = null;

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			datescanned = line.substring(0, 10);
			i++;
			if (datescanned.equals(CurDate) && start == false) {
				bob = new String[numlinesinfile - i + 2];
				i = 0;
				start = true;
			}
			if (start == true) {
				bob[i] = line;
			}
		}
		PrintWriter writer = new PrintWriter(file);
		if (bob.length > 0) {
			for (int j = 0; j < bob.length; j++) {
				writer.println(bob[j]);
			}
		}

		if (!datescanned.equals(CurDate)) {
			String date2;

			CurDate = dateFormat.format(date).toString();
			dateD = Integer.parseInt(CurDate.substring(0, 2));
			dateM = Integer.parseInt(CurDate.substring(3, 5));
			dateY = Integer.parseInt(CurDate.substring(6, 10)) + 1;

			int startD = Integer.parseInt(datescanned.substring(0, 2));
			int startM = Integer.parseInt(datescanned.substring(3, 5));
			int startY = Integer.parseInt(datescanned.substring(6, 10));

			boolean leapYear1 = leapYear(dateY);
			int CurrMonth = checkMonth(dateM, leapYear1);
			if (dateM == 2 && dateD == 29) {
				dateD -= 1;
			}
			while (startY < dateY || startD != dateD || startM != dateM) {
				if (startD < 10) {
					day = "0" + startD;
				} else {
					day = "" + startD;
				}
				if (startM < 10) {
					month = "0" + startM;
				} else {
					month = "" + startM;
				}
				date2 = day + "/" + month + "/" + dateY;
				startD++;
				if (startD > CurrMonth) {
					startM++;
					startD = 1;
				}
				if (startM > 12) {
					startY++;
					startM = 1;
					leapYear1 = leapYear(startY);
				}
				writer.print(date2
						+ ":-:7:00;11:30;free__11:30;12:30;lunch__12:30;14:00;free");
				if (startY < dateY || startD != dateD || startM != dateM) {
					writer.println();
				}
				CurrMonth = checkMonth(startM, leapYear1);
			}
			writer.close();
		}
	}

	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int column = e.getColumn();
		TableModel model = (TableModel) e.getSource();
		String columnName = model.getColumnName(column);
		Object data = model.getValueAt(row, column);
		String Name = "free";
		double start =0.0;
		double end = 0.0;
		String oldName = "free";
		
		System.out.println("check1");

		
        if(column==2){
        	Name = (String) model.getValueAt(row, column);
 	        String endTime = (String) model.getValueAt(row, column-1);
 	        String startTime = (String) model.getValueAt(row, column-2);
 	       
 	       start = getStartTime(startTime);
	 	   end = getStartTime(endTime);
	 	   oldName = "ryan";
        }
       if(column==1){
        	Name = (String) model.getValueAt(row, column+1);
 	        String endTime = (String) model.getValueAt(row, column);
 	        String startTime = (String) model.getValueAt(row, column-1);
 	       
 	        start = getStartTime(startTime);
 	        end = getStartTime(endTime);
 	        oldName = "ryan";
	 	     
        }
        if(column==0){
        	Name = (String) model.getValueAt(row, column+2);
 	        String endTime = (String) model.getValueAt(row, column+1);
 	        String startTime = (String) model.getValueAt(row, column);
 	       
	 	    start = getStartTime(startTime);
	 	    end = getStartTime(endTime);
	 	    oldName = "ryan";
        }
        
        
        if(end != 0.0 && start != 0.0 && Name.compareTo("free") != 0){
        	
        	if(newEvent == true && row == 0){
        		AddApointment(CurrentDay, start, end, Name);
        		newEvent = false;
    		}
        	else if(Name.compareTo("")==0 && oldName.compareTo("free")!=0){
        		deleteApointment(CurrentDay, oldName);
        	}
        	else if(oldName.compareTo("free")!=0){
        		editApointment(CurrentDay, start, end, oldName, Name);
        	}
        	
        	
        }
		
	

	}
}