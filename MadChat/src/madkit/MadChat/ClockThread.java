package madkit.MadChat;
import javax.swing.*;
import java.util.*;
import java.text.*;


public class ClockThread extends Thread {
	private JLabel horloge;
	
	public ClockThread(JLabel tz){
		horloge=tz;
	}
	
	public void run() {
		while(true){
			Date d=new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			
			horloge.setText(formatter.format(d));
			try {
				sleep(1000*60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	

}