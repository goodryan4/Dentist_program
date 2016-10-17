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

public class GUI {

	public JFrame frame;
	public static JTextField text;
	public JTextField textField;
	public JTextField textField_1;
	public JTextField newname;
	static List list;
	static JLabel check, name, lblPhoneNumber, lblName, iconsearch, number, dataname, datanumber;
	public JPanel info, procedure, data, allinfo, search;
	static String directory = "src/patients";
	public JButton newperson, removeperson, btnGoToCompressed, btnGoToProcedure, btnGoToData;
	public JScrollPane scrollPane;

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
		newperson = new JButton("Add Entry");
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
							filecontrol.instantiat(dir,"/data.txt");
							new File(dir + "/info.txt").createNewFile();
							filecontrol.instantiat(dir,"/info.txt");
							new File(dir + "/procedure.txt").createNewFile();
							filecontrol.instantiat(dir,"/procedure.txt");
							new File(dir + "/balance.txt").createNewFile();
							filecontrol.instantiat(dir,"/balance.txt");
							check.setText("added " + name);
							list.add(newname.getText().toLowerCase());
						} catch (IOException e) {
							check.setText("Failed to add");
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		newperson.setBounds(417, 255, 89, 25);
		search.add(newperson);

		// scrolling down list of people in folder when searching
		scrollPane = new JScrollPane();
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
						filecontrol.getData(list.getSelectedItem().toString(),"data.txt");

						info.show();
					}
				}
			}
		});
		scrollPane.setViewportView(list);

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
				String name = newname.getText().toLowerCase();
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

		lblName = new JLabel("Name: ");
		lblName.setBounds(34, 32, 46, 14);
		info.add(lblName);

		lblPhoneNumber= new JLabel("Phone number:");
		lblPhoneNumber.setBounds(34, 139, 92, 14);
		info.add(lblPhoneNumber);

		name = new JLabel("");
		name.setBounds(130, 26, 92, 26);
		info.add(name);

		number = new JLabel("");
		number.setBounds(130, 133, 92, 26);
		info.add(number);

		//JButtons
		btnGoToProcedure = new JButton("Go to procedure");
		btnGoToProcedure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				info.hide();
				procedure.show();
				filecontrol.getData(list.getSelectedItem().toString(),"procedure.txt");
				
			}
		});
		btnGoToProcedure.setBounds(21, 304, 141, 35);
		info.add(btnGoToProcedure);

		btnGoToCompressed = new JButton("Go to compressed info");
		btnGoToCompressed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info.hide();
				allinfo.show();
				filecontrol.getData(list.getSelectedItem().toString(),"info.txt");
			}
		});
		btnGoToCompressed.setBounds(183, 304, 141, 35);
		info.add(btnGoToCompressed);

		btnGoToData = new JButton("Go to data");
		btnGoToData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info.hide();
				data.show();
				try {
					filecontrol.newInfo("hello",1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnGoToData.setBounds(375, 304, 141, 35);
		info.add(btnGoToData);

		//JPanels
		procedure = new JPanel();
		frame.getContentPane().add(procedure, "name_210203575219193");
		procedure.setLayout(null);

		allinfo = new JPanel();
		frame.getContentPane().add(allinfo, "name_210279605001369");

		data = new JPanel();
		frame.getContentPane().add(data, "name_210281137029020");
		data.setLayout(null);

		//JLabels
		dataname = new JLabel("Name: ");
		dataname.setBounds(21, 21, 92, 26);
		data.add(dataname);

		datanumber = new JLabel("Phone number");
		datanumber.setBounds(21, 105, 157, 26);
		data.add(datanumber);
		
		//JTextFields
		textField = new JTextField();
		textField.setBounds(94, 21, 108, 26);
		data.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(199, 105, 108, 26);
		data.add(textField_1);
	}
}
