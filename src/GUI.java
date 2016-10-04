import java.awt.EventQueue;
import java.awt.Image;
import java.awt.List;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

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
import java.util.Calendar;

import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;

public class GUI {

	public JFrame frame;
	public Calendar currdate = Calendar.getInstance();
	static List list;
	public JPanel info, procedure, data, allinfo;
	static JLabel name, number, check, date;
	public JTextField text, newname, textField, textField_1;
	static String directory = "src/patients";
	
	//get screen resolution
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();
	private JTextField txtXxxxxxxxxx;

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
		//create temporary file for main directory
		File bob = new File(directory);
		//if the file exists then do not make one
		if (bob.exists() == false) {
			bob.mkdirs();
		}
		
		//create main container
		frame = new JFrame();
		frame.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent arg0) {
				text.setBounds(frame.getWidth() / 2 - 180, frame.getHeight() / 5, 280, 25);
			}
		});
		frame.setBounds(100, 100, 614, 431);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		//add a panel to the container for main page (searching)
		JPanel search = new JPanel();
		frame.getContentPane().add(search, "name_210219853183045");
		search.setLayout(null);

		//button to search
		JLabel iconsearch = new JLabel("");
		iconsearch.setBounds(406, 86, 24, 25);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icon.png"));
		Image newimg = icon.getImage().getScaledInstance(iconsearch.getWidth(), iconsearch.getHeight(),
				java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		iconsearch.setIcon(icon);
		search.add(iconsearch);

		newname = new JTextField();
		newname.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent arg0) {
				newname.setText("");
			}
		});
		newname.setBounds(127, 255, 280, 25);
		search.add(newname);
		newname.setColumns(10);

		// button to add person to database
		JButton newperson = new JButton("Add Entry");
		newperson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = newname.getText().toLowerCase();
				String dir = directory + "/" + name;
				File add = new File(dir);
				if (add.exists()) {
					check.setText("already exist");
				} else {
					if (name.contains("(") || name.contains(")")) {
						check.setText("really RHYS");
					}else{
						add.mkdir();
						try {
							new File(dir + "/data.txt").createNewFile();
							new File(dir + "/info.txt").createNewFile();
							new File(dir + "/procedure.txt").createNewFile();
							new File(dir + "/balance.txt").createNewFile();
							check.setText("added " + name);
							list.add(newname.getText().toLowerCase());
							checklist();
						} catch (IOException e) {
							check.setText("Failed to add");
							e.printStackTrace();
						}
					}
				}
			}
		});
		newperson.setBounds(418, 256, 125, 25);
		search.add(newperson);

		// scrolling down list of people in folder when searching
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(127, 122, 280, 122);
		search.add(scrollPane);

		// list of people when searching
		list = new List();
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					if (!(list.countItems() == 0)) {
						list.getSelectedItem().toString();
						search.hide();

						info.show();
					}
				}
			}
		});
		scrollPane.setViewportView(list);

		//this is displayed under the add or remove user
		check = new JLabel("");
		check.setBounds(127, 289, 280, 25);
		search.add(check);

		//input from user to search
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
		
		//check files then change list according to the files that exist
		text.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				list.removeAll();
				filestolist(list, bob);
			}
		});

		//if user clicks on the textbox then this erases the text if it is equal to "Enter the person you wish to Search"
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
		filestolist(list, bob);
		text.setText("Enter the person you wish to search");
		text.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
		text.setBounds(frame.getWidth() / 2 - 180, frame.getHeight() / 5, 280, 25);
		search.add(text);
		text.setColumns(10);

		//delete the file with the specified and remove the name from the list
		JButton removeperson = new JButton("Remove Entry");
		removeperson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = newname.getText().toLowerCase();
				filecontrol.removefolder(name);
			}
		});
		removeperson.setBounds(418, 289, 125, 25);
		search.add(removeperson);

		//remove all names by looping the "removefolder" method with a for loop of names in the main folder
		JButton btnRemoveAll = new JButton("Remove all");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int response = JOptionPane.showConfirmDialog(null, "Are you sure you want all the patients deleted");
				if(response == 1){
					System.out.println("1");
				}else{
					filecontrol.removeallfolders();
				}	
			}
		});
		btnRemoveAll.setBounds(0, 289, 125, 25);
		search.add(btnRemoveAll);
		
		JLabel lblTodaysDate = new JLabel("Today's Date:");
		lblTodaysDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTodaysDate.setBounds(357, 335, 125, 26);
		search.add(lblTodaysDate);
		
		date = new JLabel("date");
		date.setBounds(487, 335, 101, 26);
		search.add(date);

		//create the info panel
		info = new JPanel();
		frame.getContentPane().add(info, "name_210221315294922");
		info.setLayout(null);

		//labels
		JLabel lblfirstname = new JLabel("First name: ");
		lblfirstname.setBounds(34, 34, 60, 14);
		info.add(lblfirstname);

		JLabel lblPhoneNumber = new JLabel("Phone number:");
		lblPhoneNumber.setBounds(34, 136, 86, 14);
		info.add(lblPhoneNumber);

		//this is the info from the info file being displayed to the user
		name = new JLabel("");
		name.setBounds(104, 32, 144, 20);
		info.add(name);

		number = new JLabel("");
		number.setBounds(104, 63, 144, 20);
		info.add(number);

		//this goes to the procedure panel
		JButton btnGoToProcedure = new JButton("Go to procedure");
		btnGoToProcedure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				info.hide();
				procedure.show();
			}
		});
		btnGoToProcedure.setBounds(21, 304, 141, 35);
		info.add(btnGoToProcedure);

		//this goes to the compressed info panel
		JButton btnGoToCompressed = new JButton("Go to compressed info");
		btnGoToCompressed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info.hide();
				allinfo.show();
			}
		});
		btnGoToCompressed.setBounds(183, 304, 167, 35);
		info.add(btnGoToCompressed);

		//this goes to the data pael
		JButton btnGoToData = new JButton("Go to data");
		btnGoToData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info.hide();
				data.show();
			}
		});
		btnGoToData.setBounds(375, 304, 141, 35);
		info.add(btnGoToData);
		
		JLabel lbllastname = new JLabel("Last name:");
		lbllastname.setBounds(34, 68, 60, 14);
		info.add(lbllastname);
		
		JLabel lblpostalcode = new JLabel("Postal Code:");
		lblpostalcode.setBounds(34, 102, 68, 14);
		info.add(lblpostalcode);
		
		JLabel lblSex = new JLabel("Sex:");
		lblSex.setBounds(34, 170, 28, 14);
		info.add(lblSex);
		
		JLabel lblDateOfBirth = new JLabel("Date of birth:");
		lblDateOfBirth.setBounds(34, 204, 68, 14);
		info.add(lblDateOfBirth);
		
		JLabel lblHealthNotes = new JLabel("Health Notes:");
		lblHealthNotes.setBounds(258, 34, 68, 14);
		info.add(lblHealthNotes);
		
		JLabel lblMedicalIssues = new JLabel("Medical Issues:");
		lblMedicalIssues.setBounds(258, 67, 78, 14);
		info.add(lblMedicalIssues);
		
		JLabel postalcode = new JLabel("");
		postalcode.setBounds(104, 94, 144, 20);
		info.add(postalcode);
		
		JLabel phonenumber = new JLabel("");
		phonenumber.setBounds(115, 136, 144, 20);
		info.add(phonenumber);
		
		JLabel sex = new JLabel("");
		sex.setBounds(66, 170, 144, 20);
		info.add(sex);
		
		JLabel dateofbirth = new JLabel("");
		dateofbirth.setBounds(104, 198, 144, 20);
		info.add(dateofbirth);
		
		JLabel healthnotes = new JLabel("");
		healthnotes.setBounds(350, 34, 144, 20);
		info.add(healthnotes);
		
		JLabel medicalissues = new JLabel("");
		medicalissues.setBounds(346, 63, 144, 20);
		info.add(medicalissues);

		//creating the procedure, allinfo and data panels
		procedure = new JPanel();
		frame.getContentPane().add(procedure, "name_210203575219193");
		procedure.setLayout(null);

		allinfo = new JPanel();
		frame.getContentPane().add(allinfo, "name_210279605001369");

		data = new JPanel();
		frame.getContentPane().add(data, "name_210281137029020");
		data.setLayout(null);

		//labels in the data panel
		JLabel dataname = new JLabel("Name: ");
		dataname.setBounds(21, 27, 39, 14);
		data.add(dataname);

		JLabel datanumber = new JLabel("Phone number:");
		datanumber.setBounds(21, 99, 76, 14);
		data.add(datanumber);
		
		//
		textField = new JTextField();
		textField.setBounds(98, 21, 127, 26);
		data.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(98, 58, 127, 26);
		data.add(textField_1);
		
		JLabel label = new JLabel("Postal Code:");
		label.setBounds(21, 64, 68, 14);
		data.add(label);
		
		txtXxxxxxxxxx = new JTextField();
		txtXxxxxxxxxx.setText("xxxxxxxxxx");
		txtXxxxxxxxxx.setColumns(10);
		txtXxxxxxxxxx.setBounds(98, 96, 127, 26);
		data.add(txtXxxxxxxxxx);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(512, 303, 76, 23);
		data.add(btnSave);
	}

	public void filestolist(List list, File bob) {
		File[] files = bob.listFiles();
		int numfiles = files.length;
		if (numfiles == 0) {
			list.add("there are no names");
		} else {
			for (int i = 0; i < numfiles; i++) {
				if (files[i].getName().contains(text.getText().toLowerCase())) {
					list.add(files[i].getName());
				}
			}
		}
	}
	
	public void checklist (){
		String[] stuff = list.getItems();
		for(String string : stuff){
		if(string.contains("there are no names")){
			list.remove(string);
		}
		}
	}
	
	public void settime(){
		String time = currdate.getTime().toString();
		int appearance = time.indexOf(":")-3;
		date.setText(time.substring(0, appearance));
	}
}
