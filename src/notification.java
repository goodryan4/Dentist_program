import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class notification implements Runnable {

	private Thread t;
	private String threadName;
	private String [] day;
	public boolean notify = false;
	double nextApoint = 0.0;
	
	 notification ( String name, String [] pro) {
	      threadName = name;
	      day = pro;
	   } 
	 
	public void run() {
		while(notify == false){
		DateFormat dateFormat2 = new SimpleDateFormat("HH.mm");
		Date date2 = new Date();
		String curTime = dateFormat2.format(date2).toString();
		double time = Double.parseDouble(curTime);
		
		
		for (int i = 0; i < day.length; i++) {
			String[] timeSlot = day[i].split(";");
			double startTime = 0;
			double endTime = 0;

			if (timeSlot[0].length() < 5) {
				 startTime = Double.parseDouble(timeSlot[0].substring(0, 1))
						+ Double.parseDouble(timeSlot[0].substring(2, 4)) / 100;
				
			} else {
				 startTime = Double.parseDouble(timeSlot[0].substring(0, 2))
						+ Double.parseDouble(timeSlot[0].substring(3, 5)) / 100;
				
			}
			
			if(time<=startTime){
				nextApoint = startTime;
				break;
			}
				
		}
		
		if(time<nextApoint-0.15){
			System.out.println(time + " " +  nextApoint);
			try {
				t.sleep(600000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(time<nextApoint){
			System.out.println(time + " " +  nextApoint);
			try {
				t.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(time==nextApoint){
			System.out.println("appointment");
			try {
				t.sleep(600000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
		}
		
	}
	public void start () {
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	}

}
