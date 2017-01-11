import java.awt.Color;
import java.awt.Font;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.WriteAbortedException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.IllegalBlockSizeException;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

public class filecontrol extends GUI {
	public static String[] information, CurrentDay,
			files = { "/allinfo.txt", "/info.txt", "/procedure.txt", "/balance.txt" },
			columnNames = { "Start Time", "End Time", "Event", "Status" };
	public static File CurrentPat;
	public static String dir = "src/table/schedule.txt", oldName, path, day, month, date, year, CurrentDate, starttime2,
			endtime2, settingsdir = "src/settings.txt";
	public static boolean newEvent = false, checkbox = false, newentry = false, firstDate = true;
	public static double starttime = 7.00, endtime = 14;
	public static JCheckBox chckbxNewCheckBox;
	public static int lengthofstring = 50, numlines = 100, location1, location2;
	public static Object CurrentDate1;

	public static final int[] MONTH_LENGTH = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	// Make each file with blank info for patients
	public static void instantiat(String dir, String file, String name) {
		String[] firstandlast = name.split(" ");
		String nullData = " " + firstandlast[0] + ":; " + firstandlast[1] + ":; :; :; :; :; :; :; :; :; :; :; :; :;";
		String directory = dir + file;
		try {
			PrintWriter writer = new PrintWriter(directory);
			writer.print(nullData);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Get data from patient files followed by set data
	public static String[] getData(String name, String type) {
		path = GUI.directory + "/" + name + "/" + type + ".txt";
		File CurrentPat = new File(path);
		try {
			Scanner in = new Scanner(CurrentPat);
			String data = in.nextLine();
			information = data.split(":;");
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return information;
	}

	public static void setData() {
		for (int i = 0; i < TextFields.length; i++) {
			String a = currentData[i].substring(1, currentData[i].length());
			TextFields[i].setText(a);
		}
		for (int i = 0; i < textareas.length; i++) {
			String a = currentData[TextFields.length + i].substring(1, currentData[TextFields.length + i].length());
			if (a.contains("{:")) {
				String[] data = a.split("\\{:");
				for (int j = 0; j < data.length; j++) {
					textareas[i].append(data[j] + "\n");
				}
			} else {
				textareas[i].setText(a);
			}
		}
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

	public static void removefolder(String name) {
		File last = new File(GUI.directory + "/" + name);
		File[] things = last.listFiles();
		if (last.listFiles().length != 0) {
			for (File file : things) {
				file.delete();
			}
		}
		last.delete();
		GUI.check.setText("removed " + name);
		GUI.list.remove(name);
	}

	// Add patients from the patients folder to list
	public static void filestolist(List list, File bob) {
		File[] files = bob.listFiles();
		int numfiles = files.length;
		if (numfiles == 0) {
			list.add("There are no patients in the list");
		} else {
			String userinput = text.getText();
			for (int i = 0; i < numfiles; i++) {
				if (userinput.isEmpty()) {
					list.add(files[i].getName());
				} else {
					if (files[i].getName().toLowerCase().contains(userinput.toLowerCase())) {
						list.add(files[i].getName());
					}
				}
			}
			if (list.getItemCount() == 0) {
				list.add("No patient contains that user input");
			}
		}
	}

	// Add a patient to the list and to the file
	public static void addentry() {
		String name = "";
		if (text.getText().contains(" ")) {
			String[] names = text.getText().split(" ");
			if (names.length == 2) {
				for (int i = 0; i < names.length; i++) {
					name += names[i].substring(0, 1).toUpperCase() + names[i].substring(1).toLowerCase();
					if (i != names.length - 1) {
						name += " ";
					}
				}

				String dir = directory + "/" + name;
				File add = new File(dir);
				if (add.exists()) {
					check.setText("This patient already exists");
				} else if (name.equals("Enter The Patient You Wish To Search Or Add")) {
					check.setText("Type the patient's name in the search bar first");
				} else {
					add.mkdir();
					try {
						for (int i = 0; i < files.length; i++) {
							new File(dir + files[i]).createNewFile();
							filecontrol.instantiat(dir, files[i], name);
						}
						check.setText("added " + name);
						list.add(name);
						if (list.getItem(0).contains("There are no patients in the list")) {
							list.remove(0);
						}
						text.setText("");
						list.requestFocus();
						list.removeAll();
						filestolist(list, bob);
						text.setText("Enter the patient you wish to search or add");
						text.setForeground(Color.gray);
						text.setHorizontalAlignment(SwingConstants.CENTER);
					} catch (IOException e) {
						check.setText("Failed to add " + name + " unknown reason");
						e.printStackTrace();
					}
				}
			} else {
				check.setText("Please enter first and last name of patient");
			}
		} else {
			check.setText("Please enter first and last name of patient");
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
				String[] text = textareas[i].getText().split("\\n");
				String textareatext = "";
				for (int j = 0; j < textareas[i].getLineCount(); j++) {
					if (text[j].equals(null)) {

					} else {
						textareatext += text[j];
						if (j != textareas[i].getLineCount() - 1) {
							textareatext += "{:";
						}
					}
				}
				a[TextFields.length + i] = textareatext;
			}
			try {
				setfiledata(name, currjpanel, a);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/** Add all objects **/ // and remove all object
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

			Buttons.btnupdateinfo(a);
			Buttons.btnhome(a);

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

			lblTitle = new JLabel("", SwingConstants.CENTER);
			lblTitle.setBounds(frame.getWidth() / 2 - 200, -5, 400, 35);
			lblTitle.setFont(new Font("Serif", Font.BOLD, 28));
			a.add(lblTitle);

			// info page setup
			if (a.equals(info)) {
				lblTitle.setText("Basic Patient Information");
				lblMedicalNotes = new JLabel("Medical Notes:");
				lblMedicalNotes.setBounds(250, 150, 175, 14);
				a.add(lblMedicalNotes);

				textField_6 = new JTextField();
				textField_6.setEditable(false);
				textField_6.setBounds(120, 210, 86, 20);
				a.add(textField_6);
				Buttons.hidebuttons(a);

			} else if (a.equals(procedure)) {
				lblTitle.setText("Patient Procedure");
				lblSex.setText("procedure date:");
				lblDateOfBirth.setText("Procedure:");
				lblPostalCode.setText("Date:");
				lblPhoneNumber.setText("End:");
				lblPhoneNumber.setBounds(10, 180, 200, 14);

				specificprocedure = new JComboBox();
				String[] proc = { "Cleaning Teeth", "filling a tooth" };
				specificprocedure.setBounds(120, 120, 86, 20);
				for (int i = 0; i < proc.length; i++) {
					specificprocedure.addItem(proc[i]);
				}
				a.add(specificprocedure);

				listdates = new JComboBox();
				listdates.setBounds(120, 90, 86, 20);
				a.add(listdates);
				adddates();

				a.remove(textField_3);
				a.remove(textField_2);

				lblHealthNumber.setText("Discriptions:");
				lblHealthNumber.setBounds(10, 210, 100, 14);

				lblHealthIssues.setText("Additional rules:");
				lblHealthIssues.setBounds(250, 210, 175, 14);

				textarea.setBounds(10, 230, 175, 70);
				textarea_1.setBounds(250, 230, 175, 70);

				Buttons.hidebuttons(a);

			} else if (a.equals(allinfo)) {
				lblTitle.setText("Compressed Information");
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

				Buttons.hidebuttons(a);
			}
		}
		if (a.equals(schedule)) {
			File settings = new File(settingsdir);
			Scanner input;
			try {
				input = new Scanner(settings);
				String start = input.nextLine();
				String end = input.nextLine();
				String split[][] = { start.split(":"), end.split(":") };
				starttime = Double.parseDouble(split[0][0]) + Double.parseDouble(split[0][1]) / 100;
				endtime = Double.parseDouble(split[1][0]) + Double.parseDouble(split[1][1]) / 100;
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			lblNewLabel = new JLabel("change date");
			lblNewLabel.setBounds(463, 50, 92, 26);
			a.add(lblNewLabel);

			status = new JComboBox();
			status.addItem("waiting");
			status.addItem("done");
			status.addItem("coming soon");
			status.addItem("in progress");

			table = new JTable(4, 4);
			table.setBounds(20, 20, 40, 330);
			table.setCellSelectionEnabled(true);
			table.setRowHeight(30);

			scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(20, 20, 430, 330);
			scrollPane_1.setViewportView(table);
			scrollPane_1.setBorder(BorderFactory.createEmptyBorder());
			scrollPane_1.setColumnHeaderView(table.getTableHeader());
			a.add(scrollPane_1);

			listdates = new JComboBox();
			listdates.setBounds(463, 70, 100, 25);
			listdates.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(firstDate == true){
						Date FirstDate = new Date();
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						String CurDate = dateFormat.format(FirstDate).toString();
						refresh(CurDate);
						firstDate=false;
						CurrentDate1 = CurDate;
					}
					else{
						CurrentDate1 = listdates.getSelectedItem();
					refresh(CurrentDate1);
					}
				}
			});
			a.add(listdates);

			time = new JComboBox();
			addtimes();

			File file = new File(dir);
			Date date = new Date();
			try {
				if (!file.exists()) {
					new File("src/table").mkdirs();
					file.createNewFile();
					fileInit(date);
				} else {
					Scanner scan = new Scanner(file);
					if (!scan.hasNextLine()) {
						fileInit(date);
					} else {
						fixdates();
					}
					scan.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			adddates();

			Buttons.btnhome(a);
			Buttons.btnevent(a);
		}
		if (a.equals(settings)) {
			lblTitle = new JLabel("Settings", SwingConstants.CENTER);
			lblTitle.setBounds((frame.getWidth() / 2) - 200, 0, 400, 35);
			lblTitle.setFont(new Font("Serif", Font.BOLD, 28));
			a.add(lblTitle);

			File file = new File(settingsdir);
			starttimetext = new JTextField();
			starttimetext.setBounds(120, 60, 193, 26);
			a.add(starttimetext);

			endtimetext = new JTextField();
			endtimetext.setBounds(120, 90, 193, 26);
			a.add(endtimetext);

			Usernametext = new JTextField();
			Usernametext.setBounds(120, 120, 193, 26);
			a.add(Usernametext);

			Passwordtext = new JTextField();
			Passwordtext.setBounds(120, 150, 193, 26);
			a.add(Passwordtext);

			chckbxNewCheckBox = new JCheckBox("remove login");
			chckbxNewCheckBox.setBounds(17, 177, 179, 35);
			if (checkbox == true) {
				chckbxNewCheckBox.setSelected(true);
			}
			a.add(chckbxNewCheckBox);

			try {
				Scanner input = new Scanner(file);
				String start = input.nextLine();
				starttimetext.setText(start);
				String end = input.nextLine();
				endtimetext.setText(end);
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			Buttons.btnhome(a);
			btnHome.setText("Save and go Home");

			JLabel lblStartTime = new JLabel("Start Time:");
			lblStartTime.setBounds(30, 60, 92, 26);
			a.add(lblStartTime);

			JLabel lblEndTime = new JLabel("End Time:");
			lblEndTime.setBounds(30, 90, 92, 26);
			a.add(lblEndTime);

			JLabel lblUsername = new JLabel("New Username:");
			lblUsername.setBounds(30, 120, 92, 26);
			a.add(lblUsername);

			JLabel lblPassword = new JLabel("New Password:");
			lblPassword.setBounds(30, 150, 92, 26);
			a.add(lblPassword);
		}
		if (a.equals(balance)) {
			Buttons.btnhome(a);
		}
	}

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
		} else if (balance.isShowing()) {
			a = balance;
		}

		a.removeAll();
	}

	// Hide all panels other than the input panel which is shown
	public static void hidepanels(JPanel panelbeingshown) {
		if (panelbeingshown.equals(procedure)) {
			allinfo.hide();
			info.hide();
			schedule.hide();
			search.hide();
			settings.hide();
			balance.hide();
			procedure.show();
		} else if (panelbeingshown.equals(allinfo)) {
			procedure.hide();
			info.hide();
			schedule.hide();
			search.hide();
			settings.hide();
			balance.hide();
			allinfo.show();
		} else if (panelbeingshown.equals(info)) {
			procedure.hide();
			allinfo.hide();
			schedule.hide();
			search.hide();
			settings.hide();
			balance.hide();
			info.show();
		} else if (panelbeingshown.equals(schedule)) {
			procedure.hide();
			info.hide();
			allinfo.hide();
			search.hide();
			settings.hide();
			balance.hide();
			schedule.show();
		} else if (panelbeingshown.equals(search)) {
			procedure.hide();
			allinfo.hide();
			info.hide();
			schedule.hide();
			settings.hide();
			balance.hide();
			search.show();
		} else if (panelbeingshown.equals(settings)) {
			procedure.hide();
			allinfo.hide();
			info.hide();
			schedule.hide();
			search.hide();
			balance.hide();
			settings.show();
		} else if (panelbeingshown.equals(balance)) {
			procedure.hide();
			allinfo.hide();
			info.hide();
			schedule.hide();
			search.hide();
			settings.hide();
			balance.show();
		}
	}

	// info from textfields and areas to file
	public static void setfiledata(String name, JPanel type, String[] information) throws FileNotFoundException {
		path = GUI.directory + "/" + name + "/" + type.getName() + ".txt";
		String data = "";
		String[] a = new String[20];
		for (int j = 0; j < a.length; j++) {
			if (j >= information.length) {
				data += " :;";
			} else {
				data += " " + information[j] + ":;";
			}
		}
		printdatatofile(data);
		if (type.getName().equals(info)) {

		} else if (type.getName().equals(procedure)) {

		} else if (type.getName().equals(allinfo)) {

		} else if (type.getName().equals(balance)) {

		}
		for (int i = 0; i < 4; i++) {
			data = "";
			if (!files[i].equals("/" + type.getName() + ".txt")) {
				path = GUI.directory + "/" + name + files[i];
				Scanner scan = new Scanner(new File(path));
				String[] fileinfo = scan.nextLine().split(":;");
				for (int j = 0; j < a.length; j++) {
					if (j == 0 || j == 1) {
						data += " " + information[j] + ":;";
					} else if (j < fileinfo.length) {
						data += fileinfo[j] + ":;";
					} else {
						data += " :;";
					}
				}
				printdatatofile(data);
			}
		}
	}

	public static void printdatatofile(String data) {
		try {
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			writer.print(data);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// initialize schedule file with default info
	// fix dates in the schedule file
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
			writer.print(date + ":-:" + starttimestring + ";11:30;free__11:30;12:30;lunch__12:30;" + endtimestring
					+ ";free");
			if (dateY < startY + 1 || dateD != startD || dateM != startM) {
				writer.println("");
			}
			CurrMonth = checkMonth(dateM, leapYear1);
		}
		writer.close();
	}

	public static void fixdates() throws IOException {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String CurDate = dateFormat.format(date).toString();
		int dateD = Integer.parseInt(CurDate.substring(0, 2));
		int dateM = Integer.parseInt(CurDate.substring(3, 5)) - 1;
		int dateY = Integer.parseInt(CurDate.substring(6, 10));
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
		CurDate = day + "/" + month + "/" + dateY;

		File file = new File(dir);
		Scanner scan = new Scanner(file);

		boolean start = false;
		String[] bob = null;

		LineNumberReader lnr = new LineNumberReader(new FileReader(file));
		lnr.skip(Long.MAX_VALUE);
		int numlinesinfile = lnr.getLineNumber() + 1;
		lnr.close();

		int i = 0;
		String datescanned = "";

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			datescanned = line.substring(0, 10);
			i++;
			if (datescanned.equals(CurDate) && start == false) {
				i--;
				bob = new String[numlinesinfile - i];
				i = 0;
				start = true;
			}
			if (start == true) {
				bob[i] = line;
			}
		}

		scan.close();
		PrintWriter writer2 = new PrintWriter(file);
		for (int j = 0; j < bob.length; j++) {
			writer2.print(bob[j]);
			if (j != bob.length - 1) {
				writer2.println("");
			}
		}

		CurDate = dateFormat.format(date).toString();
		dateD = Integer.parseInt(CurDate.substring(0, 2));
		dateM = Integer.parseInt(CurDate.substring(3, 5));
		dateY = Integer.parseInt(CurDate.substring(6, 10)) + 1;
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
		String finaldate = day + "/" + month + "/" + dateY;
		if (datescanned.compareTo(finaldate) != 0) {
			String date2;
			int startD = Integer.parseInt(datescanned.substring(0, 2)) + 1;
			int startM = Integer.parseInt(datescanned.substring(3, 5));
			int startY = Integer.parseInt(datescanned.substring(6, 10));

			boolean leapYear1 = leapYear(startY);
			int CurrMonth = checkMonth(startM, leapYear1);
			if (startM == 2 && startD == 29) {
				startD -= 1;
			}

			leapYear1 = leapYear(dateY);
			CurrMonth = checkMonth(dateM, leapYear1);
			if (dateM == 2 && dateD == 29) {
				dateD -= 1;
			}
			while (startY < dateY || startD != dateD + 1 || startM != dateM) {
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
				writer2.print(date2 + ":-:" + starttimestring + ";11:30;free__11:30;12:30;lunch__12:30;" + endtimestring
						+ ";free");
				if (startY < dateY || startD != dateD + 1 || startM != dateM) {
					writer2.println();
				}
				CurrMonth = checkMonth(startM, leapYear1);
			}

		}
		writer2.close();
	}

	// RHYS should I remove this
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
	// get start time
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

				if (DaysProcedings[i - 1].endsWith("free") && DaysProcedings[i + 1].endsWith("free") && i > 0) {
					newTextLine = newTextLine + oldstart + ";" + endTime1 + ";" + "free__";
					i++;

				} else if (DaysProcedings[i - 1].endsWith("free") && i > 0) {
					newTextLine = newTextLine + oldstart + ";" + endTime + ";" + "free__";

				} else if (DaysProcedings[i + 1].endsWith("free")) {
					newTextLine = newTextLine + DaysProcedings[i - 1] + "__" + oldend + ";" + endTime1 + ";" + "free__";
					i++;

				} else {
					newTextLine = newTextLine + DaysProcedings[i - 1] + "__" + oldend + ";" + startTime1 + ";"
							+ "free__";
				}
				deleted = true;

			} else if (i > 0) {
				newTextLine = newTextLine + DaysProcedings[i - 1] + "__";
			}

			oldstart = startTime;
			oldend = endTime;

		}
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

	// add the dates to the listdates combobox
	// add times to the times combobox
	public static void adddates() {
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
		if (dateM == 0) {
			dateM = 12;
			dateY--;
		}

		boolean leapYear1 = leapYear(lastDateY);
		int CurrMonth = checkMonth(lastDateM, leapYear1);
		if (lastDateM == 2 && lastDateD == 29) {
			lastDateD = lastDateD - 1;
		}

		while (dateY != lastDateY || dateM != lastDateM || dateD != lastDateD + 1) {
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

	public static void addtimes() {
		String begining = starttime + "";
		String ending = endtime + "";

		String[] splitending = ending.split("\\.");
		ending = "";
		int firstnum = Integer.parseInt(splitending[0]);
		int secondnum = Integer.parseInt(splitending[1]);
		if (firstnum < 10) {
			ending += "0" + firstnum;
		} else {
			ending += firstnum;
		}
		ending += ":";
		if (secondnum < 1) {
			ending += "00";
		} else if (secondnum < 10) {
			ending += secondnum + "0";
		} else {
			ending += secondnum;
		}

		String[] splitbeginning = begining.split("\\.");
		firstnum = Integer.parseInt(splitbeginning[0]);
		secondnum = Integer.parseInt(splitbeginning[1]);
		while (!begining.equals(ending)) {
			begining = "";
			if (secondnum == 60) {
				secondnum = 0;
				firstnum += 1;
			}
			begining += firstnum + ":";
			if (secondnum < 1) {
				begining += "00";
			} else {
				begining += secondnum;
			}
			time.addItem(begining);
			secondnum += 30;
		}
	}

	// add listener to table
	// adds a listener to the table cells
	public static void addtablelistener() {
		table.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				TableModel model = (TableModel) e.getSource();
				model.getColumnName(column);
				model.getValueAt(row, column);
				String Name = "free";
				double start = 0.0;
				double end = 0.0;
				String oldName = "";

				Date date = new Date();
				new SimpleDateFormat("dd/MM/yyyy");
				getCurrentDate(date);

				if (column == 0 || column == 1 || column == 2) {
					String startTime = (String) model.getValueAt(row, 0);
					String endTime = (String) model.getValueAt(row, 1);
					Name = (String) model.getValueAt(row, 2);
					start = getStartTime(startTime);
					end = getStartTime(endTime);
					oldName = Name;
					if (end != 0.0 && start != 0.0 && Name.compareTo("free") != 0 && Name.compareTo("lunch") != 0) {
						if (newEvent == true && row == 0 && Name.compareTo("") != 0) {
							AddApointment(CurrentDay, start, end, Name);
							newEvent = false;
						} else if (Name.compareTo("") == 0
								&& (oldName.compareTo("free") != 0 || oldName.compareTo("lunch") != 0)) {
							if (newEvent == false || row != 0) {
								deleteApointment(CurrentDay, oldName);
							}
						} else if (oldName.compareTo("free") != 0 && oldName.compareTo("lunch") != 0
								&& Name.compareTo("") != 0) {
							editApointment(CurrentDay, start, end, oldName, Name);
						}
					}
				}
			}
		});
	}

	// Settings management methods
	public static String randomgenstringarray() {
		Random r = new Random();
		String random = "";
		String alphabet = "1a2b3c4d5e6f7g8h9i0jklmnopqrstuvwxyz";
		for (int i = 0; i < lengthofstring; i++) {
			random += alphabet.charAt(r.nextInt(alphabet.length()));
		}
		return random;
	}

	public static String randomgenintarray(int first, int second) {
		new Random();
		String random = "";
		for (int i = 0; i < lengthofstring; i++) {
			if (i == 3) {
				random += first + "-";
			} else if (i == 4) {
				random += second + "-";
			} else if (i == lengthofstring - 1) {
				random += (int) (Math.random() * numlines);
			} else {
				random += (int) (Math.random() * numlines) + "-";
			}
		}
		return random;
	}

	public static String MD5(String text) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(text.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {

		}
		return null;
	}

	public static String MD5stringintorandom(String MD5) {
		Random r = new Random();
		String random = "";
		String alphabet = "1a2b3c4d5e6f7g8h9i0jklmnopqrstuvwxyz";
		for (int i = 0; i < lengthofstring; i++) {
			if (i == 4) {
				random += MD5;
				i += MD5.length() - 1;
			} else {
				random += alphabet.charAt(r.nextInt(alphabet.length()));
			}
		}
		return random;
	}

	public static void settingstofile(String username, String password, Boolean newfile) {
		try {
			Boolean change1 = false;
			Boolean change2 = false;
			PrintWriter writer = new PrintWriter(Login.a);
			PrintWriter writer2 = new PrintWriter(settingsdir);

			// location of username and password
			if (!username.isEmpty()) {
				location1 = (int) (1 + Math.random() * (numlines - 11));
				change1 = true;
			}

			// if the password is not empty
			if (!password.isEmpty()) {
				location2 = (int) (Math.floor(Math.random() * (numlines - location1 + 1)) + location1);
				change2 = true;
			}

			// if this is a new file
			if (newfile == true) {
				location1 = (int) (1 + Math.random() * numlines - 11);
				location2 = (int) (Math.floor(Math.random() * (numlines - location1 + 1)) + location1);
				change1 = true;
				change2 = true;
			}
			writer.println(randomgenintarray(location1, location2));
			for (int i = 0; i < location1 - 2; i++) {
				writer.println(randomgenstringarray());
			}

			// if the user has changed or not
			if (change1 == true) {
				writer.println(MD5stringintorandom(MD5(username)));
			} else {
				writer.println(Login.firstscan);
			}

			// filler
			for (int i = 0; i < location2 - location1 - 1; i++) {
				writer.println(randomgenstringarray());
			}

			// if the password has changed or not
			if (change2 == true) {
				writer.println(MD5stringintorandom(MD5(password)));
			} else {
				writer.println(Login.secondscan);
			}

			// filler
			for (int i = 0; i < (numlines - location2); i++) {
				writer.println(randomgenstringarray());
			}

			// if the file exists or not
			if (newfile == false) {
				// if the remove checkbox is selected or not
				if (chckbxNewCheckBox.isSelected()) {
					writer.print(MD5stringintorandom(MD5("true")));
				} else {
					writer.print(MD5stringintorandom(MD5("false")));
				}
			} else {
				// default remove checkbox = false
				writer.print(MD5stringintorandom(MD5("false")));
			}
			writer.close();

			// the start time, end time and checkbox for enterkey
			if (newfile == false) {
				writer2.println(starttimetext.getText());
				writer2.println(endtimetext.getText());
			} else {
				writer2.println("8:00");
				writer2.println("14:00");
			}
			writer2.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
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

}