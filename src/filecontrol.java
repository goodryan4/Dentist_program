import java.awt.Color;
import java.awt.Container;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class filecontrol extends GUI {
	public static String[] information;
	public static File CurrentPat;
	public static String path;

	// Make each file with blank info
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void instantiat(String dir, String file) {
		String nullData = ":; :; :; :; :; :; :; :; :; :; :; :; :; :; ";
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
		if (add.exists()) {
			check.setText("the patient already exist");
		} else {
			add.mkdir();
			try {
				String[] files = { "/data.txt", "/info.txt", "/procedure.txt", "/balance.txt" };
				for (int i = 0; i < files.length; i++) {
					new File(dir + files[i]).createNewFile();
					filecontrol.instantiat(dir, files[i]);
				}
				check.setText("added " + name);
				list.add(name);
				// check for "There are no names" item in list and if it is
				// there then delete it
				if (list.getItem(0).contains("There are no patients in the list")) {
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
			TextFields[i].setText(currentData[i]);
		}
		for (int i = 0; i < textareas.length; i++) {
			textareas[i].setText(currentData[TextFields.length + i]);
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
			btnHome.setVisible(true);
			btnHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					hidepanels(search);
				}
			});
			a.add(btnHome);

			textarea = new JTextArea();
			textarea.setEditable(false);
			textarea.setBounds(250, 60, 175, 70);
			a.add(textarea);

			textarea_1 = new JTextArea();
			textarea_1.setEditable(false);
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
			lblNewLabel.setBounds(391, 21, 92, 26);
			a.add(lblNewLabel);

			comboBox = new JComboBox();
			comboBox.setBounds(479, 21, 92, 26);
			a.add(comboBox);
			
			table = new JTable();
			table.setBounds(61, 33, 393, 292);
			a.add(table);
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
				filecontrol.addobjects(info);
				TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
						textField_5, textField_6 };
				textareas = new JTextArea[] { textarea, textarea_1 };
				currentData = filecontrol.getData(list.getSelectedItem().toString(), "info");
				filecontrol.setData();
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
				filecontrol.addobjects(procedure);
				TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
						textField_5, textField_6 };
				textareas = new JTextArea[] { textarea, textarea_1 };
				currentData = filecontrol.getData(list.getSelectedItem().toString(), "procedure");
				filecontrol.setData();
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
				TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
						textField_5, textField_6, textField_7, textField_8, textField_9 };
				currentData = filecontrol.getData(list.getSelectedItem().toString(), "data");
				filecontrol.setData();
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
			data = data + info[i] + ":;";
		}
		try {
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			writer.print(data);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
