import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class GUI {

	public JFrame frame;
	public static JTextField text;
	static List list;
	static JLabel check, lblPhoneNumber, lblName, iconsearch;
	public JPanel info, procedure, allinfo, search;
	static String directory = "src/patients";
	public JButton newperson, removeperson, btnGoToCompressed, btnGoToProcedure, btnHome, btnSchedule, btnRemoveAll;
	public JScrollPane scrollPane;
	public static String[] currentData;
	public static JToggleButton btnUpdateInfo;
	public static JTextField textField, textField_1, textField_2, textField_3, textField_4, textField_5;
	public static JTextField[] TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,textField_5 };

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

		//main page
		search = new JPanel();
		frame.getContentPane().add(search, "name_210219853183045");
		search.setLayout(null);
		
		//search icon beside the user input
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
				filecontrol.addentry();
			}
		});
		newperson.setBounds(441, 86, 89, 25);
		search.add(newperson);

		//button to go back to the main page
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
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		search.add(scrollPane);

		// list of people when searching
		list = new List();
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					String a = list.getSelectedItem().toString();
					if (!(list.countItems() == 0) && !a.equals("There are no patients in the list")) {
						search.hide();
						info.add(btnHome);
						currentData = filecontrol.getData(a, "data.txt");
						info.show();
						filecontrol.setData();
					}
				}
			}
		});
		scrollPane.setViewportView(list);

		// user can see if their patient already exists or is added
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
		text.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		text.setBounds(frame.getWidth() / 2 - 180, frame.getHeight() / 5, 280, 25);
		search.add(text);

		//delete patient button
		removeperson = new JButton("Remove Entry");
		removeperson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = list.getSelectedItem();
				if (name == null || name.equals("There are no patients in the list")) {

				} else {
					filecontrol.removefolder(name);
				}
			}
		});
		removeperson.setBounds(417, 247, 113, 25);
		search.add(removeperson);

		//delete all patient button
		btnRemoveAll = new JButton("Remove all");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filecontrol.removeallfolders();
			}
		});
		btnRemoveAll.setBounds(18, 247, 99, 25);
		search.add(btnRemoveAll);

		//info page
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

		// JButtons for navigation
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

		btnSchedule = new JButton("Schedule");
		btnSchedule.setBounds(453, 326, 123, 35);
		info.add(btnSchedule);

		//button to allow editing
		btnUpdateInfo = new JToggleButton("Update Info");
		btnUpdateInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filecontrol.updateinfo();
			}
		});
		btnUpdateInfo.setBounds(463, 68, 101, 23);
		info.add(btnUpdateInfo);

		//textfields
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(93, 30, 86, 20);
		info.add(textField);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(93, 60, 86, 20);
		info.add(textField_1);

		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(93, 91, 86, 20);
		info.add(textField_2);

		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setBounds(93, 122, 86, 20);
		info.add(textField_3);

		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setBounds(93, 153, 86, 20);
		info.add(textField_4);

		textField_5 = new JTextField();
		textField_5.setEditable(false);
		textField_5.setBounds(93, 184, 86, 20);
		info.add(textField_5);

		// JPanels
		procedure = new JPanel();
		frame.getContentPane().add(procedure, "name_210203575219193");
		procedure.setLayout(null);

		allinfo = new JPanel();
		frame.getContentPane().add(allinfo, "name_210279605001369");
	}
}
