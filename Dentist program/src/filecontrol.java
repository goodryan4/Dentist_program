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

public class filecontrol {
	public static String[] info;
	public static File CurrentPat;
	public static String path;

	
	public static void instantiat(String dir, String file) {
		String nullData = "::::::::::::::";
		String directory = dir + file;

		PrintWriter writer;
		try {
			writer = new PrintWriter(directory);
			writer.print(nullData);
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String[] getData(String name, String type) {
		path = GUI.directory + "/" + name + "/" + type;
		File CurrentPat = new File(path);
		Scanner in = null;
		try {
			in = new Scanner(CurrentPat);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String data = in.nextLine();
		info = data.split(":");
		in.close();

		return info;

	}

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
	
	public static void removeallfolders() {
		File last = new File(GUI.directory);
		File[] things = last.listFiles();
		for (File file : things) {
			removefolder(file.getName());
		}
	}

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

	
}