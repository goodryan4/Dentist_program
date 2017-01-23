import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Buttons extends filecontrol{

	//Button to go home to main menu
	public static void btnhome(JPanel a){
		btnHome = new JButton("Home");
		btnHome.setBounds(463, 30, 101, 24);
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeobjects();
				hidepanels(search);
				frame.setTitle("Home");
			}
		});
		a.add(btnHome);
	}
	
	//Add row to JTable for a new appointment
	public static void btnevent(JPanel a){
		btnAddEvent = new JButton("Add Event");
		btnAddEvent.setBounds(463, 100, 100, 25);
		btnAddEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table.getValueAt(0, 0).equals("") || table.getValueAt(0, 1).equals("")
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
						TableColumn column1 = table.getTableHeader().getColumnModel().getColumn(i);

						column1.setHeaderValue(columnNames[i]);
					}
					table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(time));
					table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(time));
					table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(status));
					addtablelistener();
					if (newentry == true) {
						newentry = false;
						table.setValueAt(name, 0, 2);
					}
				}
			}
		});
		a.add(btnAddEvent);
	}
	
	//Enable/disable jTextareas and JTextfields in patient info database
	public static void btnupdateinfo(JPanel a){
		btnUpdateInfo = new JToggleButton("Update Info");
		btnUpdateInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filecontrol.updatetoggle();
			}
		});
		btnUpdateInfo.setBounds(463, 60, 101, 23);
		a.add(btnUpdateInfo);
	}

	// Hide all other buttons beside what is called
	public static void hidebuttons(JPanel a) {
		if (a.equals(allinfo)) {
			createbutton1(a, 10, 326, 140, 35);
			createbutton2(a, 235, 326, 140, 35);
			createbutton4(a, 459, 326, 140, 35);
		} else if (a.equals(procedure)) {
			createbutton1(a, 10, 326, 140, 35);
			createbutton3(a, 165, 326, 180, 35);
			createbutton4(a, 360, 326, 120, 35);
			createbutton5(a, 495, 326, 100, 35);

		} else if (a.equals(info)) {
			createbutton2(a, 10, 326, 140, 35);
			createbutton3(a, 215, 326, 180, 35);
			createbutton5(a, 459, 326, 140, 35);

		} else if (a.equals(schedule)) {
			createbutton1(a, 10, 326, 140, 35);
			createbutton2(a, 215, 326, 140, 35);
			createbutton3(a, 419, 326, 180, 35);

		} else if (a.equals(balance)) {
			createbutton1(a, 10, 326, 140, 35);
			createbutton2(a, 160, 326, 140, 35);
			createbutton3(a, 310, 326, 180, 35);
		}
	}

	// Patient info button
	public static void createbutton1(JPanel a, int b, int c, int d, int e) {
		btnGoToPatientInfo = new JButton("Go to Patient Info");
		btnGoToPatientInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeobjects();
				addobjects(info);
				TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
						textField_5, textField_6 };
				textareas = new JTextArea[] { textarea, textarea_1 };
				currentData = filecontrol.getData(list.getSelectedItem().toString(), "info");
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
				TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
						textField_5, textField_6 };
				textareas = new JTextArea[] { textarea, textarea_1 };
				currentData = filecontrol.getData(list.getSelectedItem().toString(), "procedure");
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
				TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
						textField_5, textField_6, textField_7, textField_8, textField_9 };
				currentData = filecontrol.getData(list.getSelectedItem().toString(), "allinfo");
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
				try {
					Scanner scan = new Scanner(new File(dir));
					Boolean found = false;
					while (scan.hasNextLine() && found == false) {
						String a = scan.nextLine();
						if (a.toLowerCase().contains(name.toLowerCase())) {
							found = true;
							listdates.setSelectedItem(a.substring(0, 10));
							removeobjects();
							filecontrol.hidepanels(schedule);
							hidebuttons(schedule);
						}
					}
					scan.close();
					if (found == false) {
						int n = JOptionPane.showConfirmDialog(null, "Would you like to add an appointment?",
								"No appointment found", JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							removeobjects();
							filecontrol.hidepanels(schedule);
							hidebuttons(schedule);
							addobjects(schedule);
							newentry = true;
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		btnSchedule.setBounds(b, c, d, e);
		a.add(btnSchedule);
	}

	// Balance button
	public static void createbutton5(JPanel a, int b, int c, int d, int e) {
		btnGoTobalance = new JButton("Accounting");
		btnGoTobalance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeobjects();
				addobjects(balance);
				TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
						textField_5 };
				textareas = new JTextArea[] { textarea, textarea_1 };
				currentData = filecontrol.getData(list.getSelectedItem().toString(), "balance");
				setData();
				hidebuttons(balance);
				hidepanels(balance);
			}
		});
		btnGoTobalance.setBounds(b, c, d, e);
		a.add(btnGoTobalance);
	}

}
