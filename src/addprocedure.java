import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class addprocedure {

	public JFrame frmEnterProcedure;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	JRadioButton rdbtnCompleted, rdbtnTreatmentPlan;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					addprocedure window = new addprocedure();
					window.frmEnterProcedure.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public addprocedure() {
		initialize();
	}

	private void initialize() {
		frmEnterProcedure = new JFrame();
		frmEnterProcedure.setTitle("Enter Procedure");
		frmEnterProcedure.setType(Type.UTILITY);
		frmEnterProcedure.setBounds(GUI.frame.getLocation().x, GUI.frame.getLocation().y, 681, 450);
		frmEnterProcedure.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEnterProcedure.getContentPane().setLayout(null);
		
		JLabel lblDate = new JLabel("Date:");
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(21, 21, 81, 26);
		frmEnterProcedure.getContentPane().add(lblDate);
		
		GUI.listdates = new JComboBox();
		filecontrol.adddates();
		GUI.listdates.setBounds(102, 15, 160, 32);
		frmEnterProcedure.getContentPane().add(GUI.listdates);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(102, 68, 160, 32);
		frmEnterProcedure.getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setEnabled(false);
		textField_2.setColumns(10);
		textField_2.setBounds(102, 121, 160, 32);
		frmEnterProcedure.getContentPane().add(textField_2);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(336, 21, 92, 26);
		frmEnterProcedure.getContentPane().add(lblStatus);
		
		rdbtnTreatmentPlan = new JRadioButton("Treatment Plan");
		rdbtnTreatmentPlan.setSelected(true);
		rdbtnTreatmentPlan.setEnabled(false);
		rdbtnTreatmentPlan.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(rdbtnTreatmentPlan.isSelected()){
					rdbtnCompleted.setSelected(false);
					rdbtnTreatmentPlan.setEnabled(false);
					rdbtnCompleted.setEnabled(true);
				}
			}
		});
		rdbtnTreatmentPlan.setBounds(445, 17, 201, 35);
		frmEnterProcedure.getContentPane().add(rdbtnTreatmentPlan);
		
		rdbtnCompleted = new JRadioButton("Completed");
		rdbtnCompleted.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(rdbtnCompleted.isSelected()){
					rdbtnTreatmentPlan.setSelected(false);
					rdbtnTreatmentPlan.setEnabled(true);
					rdbtnCompleted.setEnabled(false);
				}
			}
		});
		rdbtnCompleted.setBounds(445, 53, 201, 35);
		frmEnterProcedure.getContentPane().add(rdbtnCompleted);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(45, 209, 441, 130);
		frmEnterProcedure.getContentPane().add(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		
		JButton btnOkpost = new JButton("Ok/Post");
		btnOkpost.setBounds(159, 342, 141, 35);
		frmEnterProcedure.getContentPane().add(btnOkpost);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(345, 342, 141, 35);
		frmEnterProcedure.getContentPane().add(btnCancel);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GUI.table_1.setValueAt(GUI.listdates.getSelectedItem(),0,0);
				GUI.table_1.setValueAt(GUI.name, 0, 1);
				//GUI.table_1.setValueAt(, row, column);
			}
		});
		btnAdd.setBounds(208, 174, 92, 35);
		frmEnterProcedure.getContentPane().add(btnAdd);
		
		JButton btnChange = new JButton("Change");
		btnChange.setBounds(302, 174, 92, 35);
		frmEnterProcedure.getContentPane().add(btnChange);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(394, 174, 92, 35);
		frmEnterProcedure.getContentPane().add(btnDelete);
		
		JLabel lblProcedureList = new JLabel("Procedure List");
		lblProcedureList.setBounds(20, 174, 92, 26);
		frmEnterProcedure.getContentPane().add(lblProcedureList);
		
		JLabel lblAmount = new JLabel("Amount:");
		lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmount.setBounds(10, 124, 92, 26);
		frmEnterProcedure.getContentPane().add(lblAmount);
		
		JLabel lblProcedure = new JLabel("Procedure:");
		lblProcedure.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProcedure.setBounds(21, 71, 81, 26);
		frmEnterProcedure.getContentPane().add(lblProcedure);
	}
}
