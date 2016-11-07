import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class GUI {

	public JFrame frame;
	public static JTextField text;
	static List list;
	static JLabel check, lblFirstName, iconsearch, lblHealthNumber, lblHealthIssues, lblMedicalNotes, lblPhoneNumber,
			lblLastName, lblSex, lblDateOfBirth, lblPostalCode;
	static JPanel info, procedure, allinfo, search, schedule;
	static String directory = "src/patients";
	public static JButton newperson, removeperson, btnGoToCompressed, btnGoToProcedure, btnHome, btnSchedule,
			btnRemoveAll;
	public JScrollPane scrollPane;
	public static String[] currentData;
	public static JToggleButton btnUpdateInfo;
	public static JTextField textField, textField_1, textField_2, textField_3, textField_4, textField_5, textField_6,
			textField_7, textField_8;
	public static JTextField[] TextFields;
	public static JTextArea textarea, textarea_1;
	public static JTextArea[] textareas;

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

		// main page
		search = new JPanel();
		frame.getContentPane().add(search, "name_210219853183045");
		search.setLayout(null);

		// search icon beside the user input
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
						currentData = filecontrol.getData(a, "info.txt");
						filecontrol.addtoinfo();
						filecontrol.setData();
						info.show();
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

		JButton btnSchedule_1 = new JButton("Schedule");
		btnSchedule_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				schedule.show();
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

		JLabel lblNewLabel = new JLabel("change date");
		lblNewLabel.setBounds(391, 21, 92, 26);
		schedule.add(lblNewLabel);

		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(479, 21, 92, 26);
		schedule.add(comboBox);
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
