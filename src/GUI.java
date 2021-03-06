import java.awt.*;
import java.io.*;
import java.util.Scanner;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class GUI {

	public static JFrame frame;
	public static List list;
	public static JLabel check, lblFirstName, iconsearch, lblHealthNumber, lblHealthIssues, lblMedicalNotes,
			lblPhoneNumber, lblLastName, lblSex, lblDateOfBirth, lblPostalCode, lblNewLabel;
	public static JPanel info, procedure, allinfo, search, schedule, currjpanel, settings, balance;
	public static String directory = "src/patients", name, x, starttimestring, endtimestring, accountdir = "src/Costandprocedures.txt";
	public static JButton newperson, removeperson, btnGoToCompressed, btnGoToProcedure, btnGoTobalance, btnHome,
			btnSchedule, btnSchedule_1, btnRemoveAll, btnGoToPatientInfo, btnAddEvent, btnNewButton;
	public static JScrollPane scrollPane, scrollPane_1;
	public static String[] currentData;
	public static JToggleButton btnUpdateInfo;
	public static JTextField text, textField, textField_1, textField_2, textField_3, textField_4, textField_5,
			textField_6, textField_7, textField_8, textField_9,textField_10, starttimetext, endtimetext, Usernametext, Passwordtext;
	public static JTextField[] TextFields;
	public static JTextArea textarea, textarea_1;
	public static JTextArea[] textareas;
	public static JComboBox listdates, status, time, specificprocedure;
	public static JTable table, table_1;
	public static File bob, accounting;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("unused")
			public void run() {
				try {
					GUI window = new GUI();
					GUI.frame.setVisible(true);
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
		bob = new File(directory);
		if (bob.exists() == false) {
			bob.mkdirs();
			
		}
		accounting = new File(accountdir);
		if(!accounting.exists()){
			try {
				accounting.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(accountdir));
				writer.write("Cleaning teeth, Filling teeth");
				writer.newLine();
				for(int i=1; i<33; i++){
					writer.write("Tooth "+i);
					if(i!=32){
						writer.write(", ");
					}
				}
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		frame = new JFrame();
		frame.setTitle("Home");
		frame.setResizable(false);
		frame.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent arg0) {
				text.setBounds(frame.getWidth() / 2 - 180, frame.getHeight() / 5, 280, 25);
			}
		});

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (settings.isVisible()) {
					btnHome.grabFocus();
				}
			}
		});
		frame.setBounds(100, 100, 614, 431);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		frame.setLocation(Login.dim.width / 2 - frame.getWidth() / 2, Login.dim.height / 2 - frame.getHeight() / 2);

		// main page
		search = new JPanel();
		frame.getContentPane().add(search, "name_210219853183045");
		search.setLayout(null);

		// search icon beside the user input

		iconsearch = new JLabel();
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

		// scrolling down list of people in folder when searching
		scrollPane = new JScrollPane();
		scrollPane.setBounds(127, 122, 280, 150);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		search.add(scrollPane);

		// list of people when searching
		list = new List();
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2 && list.getSelectedIndex() != -1
						&& !list.getSelectedItem().equals("There are no patients in the list")) {
					name = list.getSelectedItem();
					currentData = filecontrol.getData(name, "info");
					filecontrol.addobjects(info);
					TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
							textField_5, textField_6 };
					textareas = new JTextArea[] { textarea, textarea_1 };
					filecontrol.setData();
					filecontrol.hidepanels(info);
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
					text.setText("Enter the patient you wish to search or add");
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
				if (text.getText().equals("Enter the patient you wish to search or add")) {
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
		text.setText("Enter the patient you wish to search or add");
		text.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		text.setBounds(frame.getWidth() / 2 - 180, frame.getHeight() / 5, 280, 25);
		search.add(text);

		// delete patient button
		removeperson = new JButton("Remove Entry");
		removeperson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = list.getSelectedItem();
				if (name != null && (!name.equals("There are no patients in the list"))) {
					filecontrol.removefolder(name);
				}
			}
		});
		removeperson.setBounds(417, 247, 113, 25);
		search.add(removeperson);

		// delete all patient button
		btnRemoveAll = new JButton("Remove all");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filecontrol.removeallfolders();
			}
		});
		btnRemoveAll.setBounds(18, 247, 99, 25);
		search.add(btnRemoveAll);

		btnSchedule_1 = new JButton("Schedule");
		btnSchedule_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filecontrol.hidepanels(schedule);
				filecontrol.addobjects(schedule);
			}
		});
		btnSchedule_1.setBounds(417, 215, 113, 25);
		search.add(btnSchedule_1);

		// JPanels
		info = new JPanel();
		frame.getContentPane().add(info, "name_210221315294922");
		info.setLayout(null);

		procedure = new JPanel();
		frame.getContentPane().add(procedure, "name_210203575219193");
		procedure.setLayout(null);

		allinfo = new JPanel();
		frame.getContentPane().add(allinfo, "name_210279605001369");
		allinfo.setLayout(null);

		schedule = new JPanel();
		frame.getContentPane().add(schedule, "name_2570594671192");
		schedule.setLayout(null);

		settings = new JPanel();
		frame.getContentPane().add(settings, "name_5114555335704");
		settings.setLayout(null);

		balance = new JPanel();
		frame.getContentPane().add(balance, "name_22847323594590");
		balance.setLayout(null);

		// set the name of each JPanel
		info.setName("info");
		procedure.setName("procedure");
		allinfo.setName("allinfo");
		search.setName("search");
		balance.setName("balance");
		
		JButton btnNewProcedure = new JButton("New Procedure");
		btnNewProcedure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addprocedure procede = new addprocedure();
				procede.frmEnterProcedure.setTitle("Add Procedure for "+name);
				procede.frmEnterProcedure.show();
			}
		});
		btnNewProcedure.setBounds(21, 10, 175, 35);
		balance.add(btnNewProcedure);

		btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filecontrol.addobjects(settings);
				filecontrol.hidepanels(settings);
			}
		});
		btnNewButton.setBounds(532, 324, 35, 35);
		icon = new ImageIcon(getClass().getClassLoader().getResource("Settings.png"));
		newimg = icon.getImage().getScaledInstance(btnNewButton.getWidth(), btnNewButton.getHeight(),
				java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		btnNewButton.setIcon(icon);
		btnNewButton.setBorder(null);
		btnNewButton.setContentAreaFilled(false);
		search.add(btnNewButton);

		schedule.setName("schedule");

		File file = new File("src/settings.txt");
		try {
			if (file.exists() == false) {
				PrintWriter writer = new PrintWriter(file);
				file.mkdirs();
				writer.println("7:00");
				writer.print("14:00");
				writer.close();
			}

			Scanner setstartandend;
			setstartandend = new Scanner(file);
			starttimestring = setstartandend.nextLine();
			endtimestring = setstartandend.nextLine();
			setstartandend.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}