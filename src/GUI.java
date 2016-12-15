import java.awt.*;
import java.io.*;
import java.util.Scanner;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class GUI {

	public JFrame frame;
	public static List list;
	public static JLabel check, lblFirstName, iconsearch, lblHealthNumber, lblHealthIssues, lblMedicalNotes,
			lblPhoneNumber, lblLastName, lblSex, lblDateOfBirth, lblPostalCode;
	public static JPanel info, procedure, allinfo, search, schedule, currjpanel, settings;
	public static String directory = "src/patients", name, x, starttimestring, endtimestring;
	public static JButton newperson, removeperson, btnGoToCompressed, btnGoToProcedure, btnHome, btnSchedule,
			btnSchedule_1, btnRemoveAll, btnGoToPatientInfo, btnAddEvent, btnNewButton;
	public JScrollPane scrollPane;
	public static String[] currentData;
	public static JToggleButton btnUpdateInfo;
	public static JTextField text, textField, textField_1, textField_2, textField_3, textField_4, textField_5,
			textField_6, textField_7, textField_8, textField_9, starttimetext, endtimetext;
	public static JTextField[] TextFields;
	public static JTextArea textarea, textarea_1;
	public static JTextArea[] textareas;
	public static JComboBox listdates, status, time;
	public static JTable table;
	public static File bob;

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
		bob = new File(directory);
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
				if (arg0.getClickCount() == 2 && list.getSelectedItem().length() > 0) {
					name = list.getSelectedItem();
					System.out.println(name);
					if (!(list.countItems() == 0) && !name.equals("There are no patients in the list")) {
						currentData = filecontrol.getData(name, "info");
						filecontrol.addobjects(info);
						TextFields = new JTextField[] { textField, textField_1, textField_2, textField_3, textField_4,
								textField_5, textField_6 };
						textareas = new JTextArea[] { textarea, textarea_1 };
						filecontrol.setData();
						filecontrol.hidepanels(info);
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

		// delete patient button
		removeperson = new JButton("Remove Entry");
		removeperson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = list.getSelectedItem();
				if (!name.equals(null) && (!name.equals("There are no patients in the list"))) {
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

		// set the name of each JPanel
		info.setName("info");
		procedure.setName("procedure");
		allinfo.setName("allinfo");
		search.setName("search");

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
		search.add(btnNewButton);

		schedule.setName("schedule");

		File file = new File("src/settings.txt");
		try {
			if(file.exists()==false){
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