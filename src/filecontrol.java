import java.awt.List;
import java.awt.TextField;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class filecontrol extends GUI{
	public static String[] info;
	public static File CurrentPat;
	public static String path;

	//make each file with blank info
	public static void instantiat(String dir, String file) {
		String nullData = ": : : : : : : : : : : : : :";
		String directory = dir + file;
		try {
			PrintWriter writer = new PrintWriter(directory);
			writer.print(nullData);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	//get data from files
	public static String[] getData(String name, String type) {
		path = GUI.directory + "/" + name + "/" + type;
		File CurrentPat = new File(path);
		try {
			Scanner in = new Scanner(CurrentPat);
			System.out.println(in.hasNextLine());
			String data = in.nextLine();
			info = data.split(":");
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}

	//adds new info to info page
	public static void newInfo(String data, int position) throws IOException {

		info[position] = data;
		String textline = "";
		for (int i = 0; i < info.length; i++) {
			textline = textline + info[i].toString() + ":";
		}
		PrintWriter writer = new PrintWriter(path);
		writer.print(textline);
		writer.close();

	}
	
	//for look through all patients and calling "removefolder" method
	public static void removeallfolders() {
		File last = new File(GUI.directory);
		File[] things = last.listFiles();
		for (File file : things) {
			removefolder(file.getName());
		}
	}

	//remove patient and folders involved with the patient
	public static void removefolder(String name) {
		File last = new File(GUI.directory + "/" + name);
		File[] things = last.listFiles();
		for (File file : things) {
			file.delete();
		}
		last.delete();
		GUI.check.setText("remove " + name);
		GUI.list.remove(name.toLowerCase());
	}
	
	//this is the initial check to see what patients are in the patents folder
	public static void filestolist(List list, File bob) {
		File[] files = bob.listFiles();
		int numfiles = files.length;
		if (numfiles == 0) {
			list.add("There are no patients in the list");
		} else {
			for (int i = 0; i < numfiles; i++) {
				if (files[i].getName().contains(text.getText().toLowerCase())) {
					list.add(files[i].getName());
				}
			}
		}
	}
	
	//add a patient to the list and to the file
	public static void addentry(){
		String name = text.getText().toLowerCase();
		String dir = directory + "/" + name;
		File add = new File(dir);
		if (add.exists()) {
			check.setText("the patient already exist");
		} else {
			add.mkdir();
			try {
				String[] files = {"/data.txt","/info.txt","/procedure.txt","/balance.txt"};
				for(int i=0; i<files.length; i++){
					new File(dir + files[i]).createNewFile();
					filecontrol.instantiat(dir, files[i]);
				}
				check.setText("added " + name);
				list.add(name);
				
				//check for "There are no names" item in list and if it is there then delete it
				if(list.getItem(0).contains("There are no patients in the list")){
					list.remove(0);
				}
				
			//this should never happen!! but if there is an error creating the files then the it will tell user "Failed to add"	
			} catch (IOException e) {
				check.setText("Failed to add");
				e.printStackTrace();
			}
		}
	}
	
	//sets the data to each textfield
	public static void setData() {
		for (int i = 0; i < TextFields.length; i++) {
			System.out.println(currentData[i]);
			TextFields[i].setText(currentData[i]);
		}
	}
	
	//set textfields to editable or disabled
	public static void updateinfo(){
		if (btnUpdateInfo.isSelected()) {
			for (int i = 0; i < TextFields.length; i++) {
				TextFields[i].setEditable(true);
			}
		} else {
			for (int i = 0; i < TextFields.length; i++) {
				TextFields[i].setEditable(false);
			}
		}
	}
}
