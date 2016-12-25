import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.xml.stream.events.Characters;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;

public class Login {

	private static JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	public static File a = new File("src/userandpassandjunk.txt");
	public static File folder = new File("src/");
	static String line3;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (!a.exists()) {
						System.out.println("made a file");
						folder.mkdir();
						a.createNewFile();
						try {
							PrintWriter writer = new PrintWriter(a);
							writer.println();
							writer.println();
							writer.print(filecontrol.MD5("true"));
							writer.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					line3 = Files.readAllLines(Paths.get(a.getAbsolutePath())).get(2);
					if (!line3.equals(filecontrol.MD5("false"))) {
						JOptionPane.showMessageDialog(frame,!line3.equals(filecontrol.MD5("false")));
						GUI bob = new GUI();
						bob.frame.show();
					} else {
						Login window = new Login();
						window.frame.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.white);

		JLabel lblUsername = new JLabel();
		lblUsername.setBounds(30, 30, 32, 32);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("Usericon.png"));
		Image newimg = icon.getImage().getScaledInstance(lblUsername.getWidth(), lblUsername.getHeight(),
				java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		lblUsername.setIcon(icon);
		frame.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(30, 90, 32, 32);
		icon = new ImageIcon(getClass().getClassLoader().getResource("Passwordicon.png"));
		newimg = icon.getImage().getScaledInstance(lblPassword.getWidth(), lblPassword.getHeight(),
				java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		lblPassword.setIcon(icon);
		lblPassword.setBackground(Color.white);
		frame.getContentPane().add(lblPassword);

		textField = new JTextField();
		textField.setBounds(70, 30, 280, 32);
		textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		frame.getContentPane().add(textField);

		textField_1 = new JTextField();
		textField_1.setBounds(70, 90, 280, 32);
		textField_1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		frame.getContentPane().add(textField_1);

		JButton btnLogin = new JButton();
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Scanner scan;
				try {
					scan = new Scanner(a);
					if (!textField.getText().isEmpty() && !textField_1.getText().isEmpty() && scan.hasNextLine()) {
						String first = textField.getText();
						String second = textField_1.getText();
						if (filecontrol.MD5(first).equals(scan.nextLine())) {
							if (filecontrol.MD5(second).equals(scan.nextLine())) {
								GUI bob = new GUI();
								bob.frame.show();
								frame.dispose();
							}
						}
					} else {
						GUI bob = new GUI();
						bob.frame.show();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnLogin.setBorder(null);
		btnLogin.setBounds(frame.getWidth() / 5, 150, frame.getWidth() / 2, 40);
		icon = new ImageIcon(getClass().getClassLoader().getResource("button_login.png"));
		newimg = icon.getImage().getScaledInstance(btnLogin.getWidth(), btnLogin.getHeight(),
				java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		btnLogin.setIcon(icon);
		btnLogin.setContentAreaFilled(false);
		frame.getContentPane().add(btnLogin);
	}
}