import java.awt.EventQueue;
import java.awt.Image;
import java.awt.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JScrollPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;
import java.awt.Window.Type;
import javax.swing.JToggleButton;

public class GUI {

	public JFrame frame;
	public static JTextField text;
	static List list;
	static JLabel check, lblPhoneNumber, lblName, iconsearch;
	public JPanel info, procedure, allinfo, search;
	static String directory = "src/patients";
	public JButton newperson, removeperson, btnGoToCompressed, btnGoToProcedure;
	public JScrollPane scrollPane;
	public JTextField textField;

	public String[] currentData;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	JToggleButton btnUpdateInfo;
	JButton btnHome;
	JTextField[] TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4, textField_5 };

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI() {
		initialize();
	}

	private void initialize() {
		File bob = new File(directory);
		if (bob.exists() == false) {
			bob.mkdirs();
		}
		frame = new JFrame();
		frame.setResizable(false);
		frame.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent arg0) {
				text.setBounds(frame.getWidth() / 2 - 180, frame.getHeight() / 5, 280, 25);
			}
		});
		frame.setBounds(100, 100, 614, 431);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		search = new JPanel();
		frame.getContentPane().add(search, "name_210219853183045");
		search.setLayout(null);

		// button to search
		iconsearch = new JLabel("");
		iconsearch.setBounds(406, 86, 24, 25);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icon.png"));
		Image newimg = icon.getImage().getScaledInstance(iconsearch.getWidth(), iconsearch.getHeight(),
				java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		iconsearch.setIcon(icon);
		search.add(iconsearch);

		// button to add person to database
		newperson = new JButton("Add Entry");
		newperson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String dir = directory + "/" + text.getText().toLowerCase();
				File add = new File(dir);
				if (add.exists()) {
					check.setText("the patient already exist");
				} else {
					add.mkdir();
					try {
						new File(dir + "/data.txt").createNewFile();
						filecontrol.instantiat(dir, "/data.txt");
						new File(dir + "/info.txt").createNewFile();
						filecontrol.instantiat(dir, "/info.txt");
						new File(dir + "/procedure.txt").createNewFile();
						filecontrol.instantiat(dir, "/procedure.txt");
						new File(dir + "/balance.txt").createNewFile();
						filecontrol.instantiat(dir, "/balance.txt");
						check.setText("added " + text.getText().toLowerCase());
						list.add(text.getText().toLowerCase());
						if(list.getItem(0).contains("")){
							
						}
					} catch (IOException e) {
						check.setText("Failed to add");
						e.printStackTrace();
					}
				}
			}
		});

		newperson.setBounds(417, 255, 89, 25);
		search.add(newperson);

		btnHome = new JButton("Home");
		btnHome.setBounds(463, 28, 101, 24);
		btnHome.setVisible(true);
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				info.hide();
				procedure.hide();
				allinfo.hide();
				search.show();
			}
		});

		// scrolling down list of people in folder when searching
		scrollPane = new JScrollPane();
		scrollPane.setBounds(127, 122, 280, 150);
		search.add(scrollPane);

		// list of people when searching
		list = new List();
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					if (!(list.countItems() == 0)) {
						list.getSelectedItem().toString();
						search.hide();
						info.add(btnHome);
						currentData = filecontrol.getData(list.getSelectedItem().toString(), "data.txt");

						info.show();
						setData();

					}
				}
			}
		});
		scrollPane.setViewportView(list);

		//user can see if their patient already exists or is added
		check = new JLabel("");
		check.setBounds(127, 289, 280, 14);
		search.add(check);

		// input from user
		text = new JTextField();
		text.setForeground(Color.GRAY);
		text.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent arg0) {
				if (text.getText().equals("")) {
					text.setText("Enter the person you wish to search");
					text.setForeground(Color.gray);
					text.setHorizontalAlignment(SwingConstants.CENTER);
				}
			}
		});
		text.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				list.removeAll();
				filecontrol.filestolist(list, bob);
			}
		});

		text.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (text.getText().equals("Enter the person you wish to search")) {
					text.setText("");
					text.setForeground(Color.black);
					text.setHorizontalAlignment(SwingConstants.LEADING);
				} else {
					text.selectAll();
				}
			}
		});
		text.setHorizontalAlignment(SwingConstants.CENTER);
		filecontrol.filestolist(list, bob);
		text.setText("Enter the person you wish to search");
		text.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
		text.setBounds(frame.getWidth() / 2 - 180, frame.getHeight() / 5, 280, 25);
		search.add(text);
		text.setColumns(10);

		removeperson = new JButton("Remove Entry");
		removeperson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = text.getText().toLowerCase();
				filecontrol.removefolder(name);
			}
		});
		removeperson.setBounds(417, 292, 113, 25);
		search.add(removeperson);

		JButton btnRemoveAll = new JButton("Remove all");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filecontrol.removeallfolders();
			}
		});
		btnRemoveAll.setBounds(17, 292, 99, 25);
		search.add(btnRemoveAll);

		info = new JPanel();
		frame.getContentPane().add(info, "name_210221315294922");
		info.setLayout(null);

		info.add(btnHome);

		lblName = new JLabel("Name: ");
		lblName.setBounds(10, 33, 46, 14);
		info.add(lblName);

		lblPhoneNumber = new JLabel("Phone number:");
		lblPhoneNumber.setBounds(10, 125, 73, 14);
		info.add(lblPhoneNumber);

		// JButtons
		btnGoToProcedure = new JButton("Go to procedure");
		btnGoToProcedure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				info.hide();
				procedure.show();
				procedure.add(btnHome);
				currentData = filecontrol.getData(list.getSelectedItem().toString(), "procedure.txt");

			}
		});
		btnGoToProcedure.setBounds(31, 326, 141, 35);
		info.add(btnGoToProcedure);

		btnGoToCompressed = new JButton("Go to compressed info");
		btnGoToCompressed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info.hide();
				allinfo.show();
				allinfo.add(btnHome);
				currentData = filecontrol.getData(list.getSelectedItem().toString(), "info.txt");
			}
		});
		btnGoToCompressed.setBounds(182, 326, 141, 35);
		info.add(btnGoToCompressed);

		JButton btnSchedule = new JButton("Schedule");
		btnSchedule.setBounds(453, 326, 123, 35);
		info.add(btnSchedule);

		createTextFields();

		btnUpdateInfo = new JToggleButton("Update Info");
		btnUpdateInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		});
		btnUpdateInfo.setBounds(463, 68, 101, 23);
		info.add(btnUpdateInfo);

		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(93, 60, 86, 20);
		info.add(textField_1);

		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		textField_2.setBounds(93, 91, 86, 20);
		info.add(textField_2);

		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		textField_3.setBounds(93, 122, 86, 20);
		info.add(textField_3);

		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setColumns(10);
		textField_4.setBounds(93, 153, 86, 20);
		info.add(textField_4);

		textField_5 = new JTextField();
		textField_5.setEditable(false);
		textField_5.setColumns(10);
		textField_5.setBounds(93, 184, 86, 20);
		info.add(textField_5);

		// JPanels
		procedure = new JPanel();
		frame.getContentPane().add(procedure, "name_210203575219193");
		procedure.setLayout(null);

		allinfo = new JPanel();
		frame.getContentPane().add(allinfo, "name_210279605001369");

	}

	public void createTextFields() {
		textField = new JTextField();
		textField.setBounds(93, 30, 86, 20);
		info.add(textField);
		textField.setColumns(10);
		textField.setEditable(false);

	}

	public void setData() {
		for (int i = 0; i <= TextFields.length; i++) {
			TextFields[i].setText(currentData[i].toString());
		}
	}
}
