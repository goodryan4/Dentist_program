import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class filecontrol {
	public static String [] info;
	public static File CurrentPat;
	public static URI Current;
	

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
	public static String [] getData(String name, String type) throws IOException{
		File CurrentPat = new File(GUI.directory + "/" + name + "/" + type);
		Current = ;
		
		Scanner in = new Scanner(CurrentPat);
		String data = in.nextLine();
		info = data.split(":");
		
		return info;
		
	}
	public static void newInfo (String data, int position) throws IOException{
		info [position] = data;
		info.toString();
		String test = "hello";
		Files.write(Paths.get(GUI.directory + "/" + Patname + "/data.txt"), test.getBytes());
		System.out.println(info.toString());
	
	}
}
