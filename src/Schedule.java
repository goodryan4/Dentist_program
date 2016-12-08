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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JList;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class Schedule extends JFrame implements TableModelListener{

	private JPanel contentPane;
	private JTable table;
	public JComboBox box;
	static String directory = "src/table";
	static JLabel check;
	public static String [] CurrentDay;
	public static String CurDate;
	private Thread t;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		
	EventQueue.invokeLater(new Runnable() 
		{
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
		
		String [] columeNames = {"Patient Names", "Start Time","End Time"};
		
		table = new JTable(7,5);
		
		table.setBounds(20, 50, 400, 250);
		table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(box));
		table.setColumnSelectionAllowed(true);
		contentPane.add(table);
		
		table.setShowVerticalLines(false);
		table.setValueAt("Edit", 0, 0);
		table.setValueAt("Patient Name", 0, 1);
		table.setValueAt("Start Time", 0, 2);
		table.setValueAt("End Time", 0, 3);
		
		/*
		table.setValueAt("Edit", 1, 0);
		table.setValueAt("free", 1, 1);
		table.setValueAt("7:00", 1, 2);
		table.setValueAt("8:30", 1, 3);
		
		table.setValueAt("Edit", 2, 0);
		table.setValueAt("Ryan", 2, 1);
		table.setValueAt("8:30", 2, 2);
		table.setValueAt("9:30", 2, 3);
		
		table.setValueAt("Edit", 3, 0);
		table.setValueAt("free", 3, 1);
		table.setValueAt("9:30", 3, 2);
		table.setValueAt("11:30", 3, 3);
		
		table.setValueAt("Edit", 4, 0);
		table.setValueAt("lunch", 4, 1);
		table.setValueAt("11:30", 4, 2);
		table.setValueAt("12:30", 4, 3);
		
		table.setValueAt("Edit", 5, 0);
		table.setValueAt("Robert", 5, 1);
		table.setValueAt("12:30", 5, 2);
		table.setValueAt("1:00", 5, 3);
		
		table.setValueAt("Edit", 6, 0);
		table.setValueAt("free", 6, 1);
		table.setValueAt("1:00", 6, 2);
		table.setValueAt("4:00", 6, 3);
		*/
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		
		String [] CurrentDay;
		//filestuff.fileInit(date);
		filestuff.updateDates();
		CurrentDay = filestuff.getCurrentDate(date);
		//notification R1 = new notification( "Thread-1", CurrentDay);
	      //R1.start();
		Double a = 8.30;
		Double b = 9.30;
		String m = "ryan";
		String k = "rhys";
		//filestuff.AddApointment(CurrentDay, a ,  b ,  m );
		//filestuff.editApointment(CurrentDay, a ,  b ,m, k);
		//filestuff.deleteApointment(CurrentDay, "free");
		setTable(CurrentDay);
		table.getModel().addTableModelListener(this);
		double l = filecontrol.getStartTime("");
	}
	
	public void variableCheck(String oldtype, String newtype){
		
		
	}
	public void setTable(String [] data){
		for(int i = 0; i<data.length; i++){
			String[] timeSlot = data [i].split(";");
			String startTime = timeSlot[0];
			String endTime = timeSlot[1];
			String difiner = timeSlot[2];
			
			table.setValueAt(startTime, i+1, 1);
			table.setValueAt(endTime, i+1, 2);
			table.setValueAt(difiner, i+1, 3);
		}
	}
	 public void tableChanged(TableModelEvent e) {
	        int row = e.getFirstRow();
	        int column = e.getColumn();
	        TableModel model = (TableModel)e.getSource();
	        String columnName = model.getColumnName(column);
	        Object data = model.getValueAt(row, column);
	        String Name = "free";
	
	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
	        CurrentDay = filestuff.getCurrentDate(date);
	       
	        if(column==3){
	        	Name = (String) model.getValueAt(row, column);
	 	        String endTime = (String) model.getValueAt(row, column-1);
	 	        String startTime = (String) model.getValueAt(row, column-2);
	 	       
	 	       double start = getTime(startTime);
		 	     double end = getTime(endTime);
		 	     String oldName = "free";
	 	         
	 	      if(Name.compareTo("free")!=0 && Name.compareTo("")!=0){
	 	    	 filestuff.AddApointment(CurrentDay, start, end, Name);
	 		        }
	 	      if(Name.compareTo("")==0){
	 	    	  filestuff.deleteApointment(CurrentDay, oldName);
	 	      }
	        }
	        if(column==2){
	        	Name = (String) model.getValueAt(row, column+1);
	 	        String endTime = (String) model.getValueAt(row, column);
	 	        String startTime = (String) model.getValueAt(row, column-1);
	 	       
	 	       
	 	      double start = getTime(startTime);
		 	     double end = getTime(endTime);
		 	     String oldName = "ryan";
		 	     
	 	      if(Name.compareTo("free")!=0){
	 	    	 filestuff.editApointment(CurrentDay, start, end, oldName, Name);
	 		        }
	        }
	        if(column==1){
	        	Name = (String) model.getValueAt(row, column+2);
	 	        String endTime = (String) model.getValueAt(row, column+1);
	 	        String startTime = (String) model.getValueAt(row, column);
	 	       
	 	     
	 	      double start = getTime(startTime);
	 	     double end = getTime(endTime);
	 	     String oldName = "ryan";
	 	       
	 	   
	 	       if(Name.compareTo("free")!=0){
	 		       	filestuff.editApointment(CurrentDay, start, end, oldName, Name);
	 		        }
	        }
	        
	       
	        
	        
	        
	        
	        
	 }
	 public static double getTime(String d){
		 if (d.length() < 5) {
				double startTime = Double.parseDouble(d.substring(0, 1))
						+ Double.parseDouble(d.substring(2, 4)) / 100;
				return startTime;
			} else {
				double startTime = Double.parseDouble(d.substring(0, 2))
						+ Double.parseDouble(d.substring(3, 5)) / 100;
				return startTime;

			}
	 }
	
}
