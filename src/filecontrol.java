import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class filecontrol extends GUI {
	public static String[] information;
	public static File CurrentPat;
	public static String path;

	// Make each file with blank info
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void instantiat(String dir, String file) {
		String nullData = ": : : : : : : : : : : : : :";
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
		path = GUI.directory + "/" + name + "/" + type;
		File CurrentPat = new File(path);
		try {
			Scanner in = new Scanner(CurrentPat);
			String data = in.nextLine();
			information = data.split(":");
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
			System.out.println(currentData[i]);
			TextFields[i].setText(currentData[i]);
		}
	}

	// Set textFields to editable or disabled
	public static void updateinfo() {
		if (btnUpdateInfo.isSelected()) {
			for (int i = 0; i < TextFields.length; i++) {
				TextFields[i].setEditable(true);
			}
		} else {
			for (int i = 0; i < TextFields.length; i++) {
				TextFields[i].setEditable(false);
			}
		}
	}

	// Add info components
	public static void addtoinfo() {
		addobjects(info);
		TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4, textField_5,
				textField_6};
		textareas = new JTextArea[] {textarea, textarea_1};
	}

	// Add all objects
	public static void addobjects(JPanel a) {
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

		//TextFields
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
		
		//update button
		btnUpdateInfo = new JToggleButton("Update Info");
		btnUpdateInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filecontrol.updateinfo();
			}
		});
		btnUpdateInfo.setBounds(463, 60, 101, 23);
		a.add(btnUpdateInfo);
		
		// button to go to procedure
		btnGoToProcedure = new JButton("Go to procedure");
		btnGoToProcedure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filecontrol.addobjects(procedure);
				TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
						textField_5, textField_6, textField_7, textField_8 };

				currentData = filecontrol.getData(list.getSelectedItem().toString(), "procedure.txt");
				hidepanels(procedure);
			}
		});
		btnGoToProcedure.setBounds(31, 326, 141, 35);
		a.add(btnGoToProcedure);
		
		// button to go to all info
		btnGoToCompressed = new JButton("Go to compressed info");
		btnGoToCompressed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info.hide();
				procedure.hide();

				addobjects(allinfo);
				TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4, textField_5 };

				currentData = filecontrol.getData(list.getSelectedItem().toString(), "info.txt");
				allinfo.show();
			}
		});
		btnGoToCompressed.setBounds(182, 326, 141, 35);
		a.add(btnGoToCompressed);

		// button to go back to the main page
		btnHome = new JButton("Home");
		btnHome.setBounds(463, 30, 101, 24);
		btnHome.setVisible(true);
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				info.hide();
				procedure.hide();
				allinfo.hide();
				search.show();
			}
		});
		a.add(btnHome);

		//info page setup
		if (a.equals(info)) {

			lblHealthNumber = new JLabel("Health Number:");
			lblHealthNumber.setBounds(10, 210, 100, 14);
			a.add(lblHealthNumber);

			lblHealthIssues = new JLabel("Health Issues:");
			lblHealthIssues.setBounds(10, 240, 100, 14);
			a.add(lblHealthIssues);

			lblMedicalNotes = new JLabel("Medical Notes:");
			lblMedicalNotes.setBounds(250, 30, 173, 14);
			a.add(lblMedicalNotes);

			textField_5 = new JTextField();
			textField_5.setEditable(false);
			textField_5.setBounds(120, 180, 86, 20);
			a.add(textField_5);

			textField_6 = new JTextField();
			textField_6.setEditable(false);
			textField_6.setBounds(120, 210, 86, 20);
			a.add(textField_6);

			textarea= new JTextArea();
			textarea.setEditable(false);
			textarea.setBounds(120, 240, 86, 20);
			a.add(textarea);

			textarea_1 = new JTextArea();
			textarea_1.setEditable(false);
			textarea_1.setBounds(250, 60, 173, 70);
			a.add(textarea_1);
			
			btnSchedule = new JButton("Schedule");
			btnSchedule.setBounds(453, 326, 123, 35);
			a.add(btnSchedule);

		} else if (a.equals(procedure)) {
			lblSex.setText("procedure date:");
			lblDateOfBirth.setText("Procedure starts:"); 
			lblPostalCode.setText("Procedure ends:");
			lblPhoneNumber.setText("Procedure:");
			lblPhoneNumber.setBounds(10, 200, 100, 14);
			
			JLabel label = new JLabel("Name: ");
			label.setBounds(218, 30, 46, 14);
			a.add(label);
		} else if (a.equals(allinfo)) {

		}
	}
	
	public static void hidepanels(JPanel panelbeingshown){
		if(panelbeingshown.equals(procedure)){
			allinfo.hide();
			info.hide();
			schedule.hide();
			search.hide();
			procedure.show();
		}else if(panelbeingshown.equals(allinfo)){
			procedure.hide();
			info.hide();
			schedule.hide();
			search.hide();
			allinfo.show();
		}else if(panelbeingshown.equals(info)){
			procedure.hide();
			allinfo.hide();
			schedule.hide();
			search.hide();
			info.show();
		}else if(panelbeingshown.equals(schedule)){
			procedure.hide();
			info.hide();
			allinfo.hide();
			search.hide();
			schedule.show();
		}else if(panelbeingshown.equals(search)){
			procedure.hide();
			allinfo.hide();
			info.hide();
			schedule.hide();
			search.show();
		}
	}
}
