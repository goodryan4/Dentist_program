import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JList;


public class Schedule extends JFrame {

	private JPanel contentPane;
	private JTable table;
	public JComboBox box;
	static String directory = "src/table";
	static JLabel check;
	public static String [] CurrentDay;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Schedule frame = new Schedule();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

		
	/**
	 * Create the frame.
	 */
	public Schedule() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 474, 359);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String query="Arrived";
		String query2="Canceled";
		String query3="Did not show";
		
		String dir = directory + "/schedule.txt";
		File schedule = new File(dir);
		if (schedule.exists()) {
			
			System.out.println("check");
		}
		else{
			try {
				new File(dir).createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		box = new JComboBox();
		box.setVisible(true);
		box.addItem(query);
		box.addItem(query2);
		box.addItem(query3);
		
		
		table = new JTable(2,4);
		table.setBounds(60, 50, 214, 208);
		table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(box));
		table.setColumnSelectionAllowed(true);
		contentPane.add(table);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		
		String [] CurrentDay;
		//filestuff.fileInit(date);
		//filestuff.updateDates();
		CurrentDay = filestuff.getCurrentDate(date);
		Double a = 8.00;
		Double b = 9.30;
		String m = "Robert";
		String k = "Ryan";
		//filestuff.AddApointment(CurrentDay, a ,  b ,  m );
		//filestuff.editApointment(CurrentDay, a ,  b ,m, k);
		filestuff.deleteApointment(CurrentDay, "Robert");
		
	}
	public void variableCheck(){
		
	}
	
}
