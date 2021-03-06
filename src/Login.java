import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Login {

	private static JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	public static File a = new File("src/userandpassandjunk.txt");
	public static File folder = new File("src/");
	static String line3;
	JButton btnLogin;
	static String firstscan, secondscan;
	static boolean skip = false;
	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings({ "deprecation", "unused" })
			public void run() {
				try {
					if (!a.exists()) {
						folder.mkdir();
						a.createNewFile();
						filecontrol.settingstofile("user", "pass", true);
					}
					line3 = Files.readAllLines(Paths.get(a.getAbsolutePath())).get(filecontrol.numlines).substring(4,
							36);
					try {
						Scanner scan = new Scanner(a);
						if (scan.hasNextLine()) {
							String locations = scan.nextLine();
							String[] location = locations.split("-");
							filecontrol.location1 = Integer.parseInt(location[3]);
							filecontrol.location2 = Integer.parseInt(location[4]);
							firstscan = Files.readAllLines(Paths.get(Login.a.getAbsolutePath()))
									.get(filecontrol.location1 - 1);
							secondscan = Files.readAllLines(Paths.get(Login.a.getAbsolutePath()))
									.get(filecontrol.location2 - 1);
							scan.close();
						} else {
							scan.close();
							System.exit(0);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (!line3.equals(filecontrol.MD5("false"))) {
						filecontrol.checkbox = true;
						new GUI();
						GUI.frame.show();
					} else {
						Login window = new Login();
						Login.frame.setVisible(true);
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
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.white);
		frame.setLocation(dim.width/2-frame.getWidth()/2, dim.height/2-frame.getHeight()/2);

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
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
				}
			}
		});
		frame.getContentPane().add(textField);

		textField_1 = new JTextField();
		textField_1.setBounds(70, 90, 280, 32);
		textField_1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		textField_1.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
				}
			}
		});
		frame.getContentPane().add(textField_1);

		btnLogin = new JButton();
		btnLogin.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent arg0) {
				ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("button_login (1).png"));
				Image newimg = icon.getImage().getScaledInstance(btnLogin.getWidth(), btnLogin.getHeight(),
						java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(newimg);
				btnLogin.setIcon(icon);
			}

			public void mouseExited(MouseEvent e) {
				ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("button_login.png"));
				Image newimg = icon.getImage().getScaledInstance(btnLogin.getWidth(), btnLogin.getHeight(),
						java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(newimg);
				btnLogin.setIcon(icon);
			}

			public void mousePressed(MouseEvent arg0) {
				ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("button_login (2).png"));
				Image newimg = icon.getImage().getScaledInstance(btnLogin.getWidth(), btnLogin.getHeight(),
						java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(newimg);
				btnLogin.setIcon(icon);
			}

			public void mouseReleased(MouseEvent e) {
				ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("button_login (1).png"));
				Image newimg = icon.getImage().getScaledInstance(btnLogin.getWidth(), btnLogin.getHeight(),
						java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(newimg);
				btnLogin.setIcon(icon);
			}
		});
		btnLogin.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if (!textField.getText().isEmpty() && !textField_1.getText().isEmpty()) {
					String first = textField.getText();
					String second = textField_1.getText();
					if (filecontrol.MD5(first).equals(firstscan.substring(4, 36))) {
						if (filecontrol.MD5(second).equals(secondscan.substring(4, 36))) {
							frame.dispose();
							new GUI();
							GUI.frame.show();
						} else {
							JOptionPane.showMessageDialog(null, "Try again username and password are case sensitive.");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Try again username and password are case sensitive.");
					}
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