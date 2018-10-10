package application;

// TODO:
// Do some CSS
	
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Schedule schedule = new Schedule();
			reloadSavedSchedule(schedule);
			Screens screen = new Screens();
			screen.setScreen("timed", primaryStage, schedule);
			Runnable runSchedule = new ChangesThread(schedule);
			new Thread(runSchedule).start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	public class ChangesThread implements Runnable{
			private Schedule x;
		public ChangesThread(Schedule schedule) {
			x = schedule;
		}
		@SuppressWarnings("deprecation")
		public void run() {
			while(true) {
				Date date = new Date();
				for(Change change: x.getList()) {
					if (change.getHours() == date.getHours() && change.getMinutes() == date.getMinutes()) {
						User32.INSTANCE.SystemParametersInfo(0x0014, 0, change.getPath() , 1); 
					}
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo (int one, int two, String s ,int three);  
  
    }
    public static void reloadSavedSchedule(Schedule schedule) throws IOException {
    	File file = new File(schedule.getLocalPath() + "\\schedule.txt");
    	if(file.exists()) {
	    	String path = "";
	    	try {
				FileReader reader = new FileReader(file);
				BufferedReader buffer = new BufferedReader(reader);
				String line;
				String[] arrayLine;
				while((line = buffer.readLine()) != null) {
					arrayLine = line.split(" "); 
					int hour = Integer.parseInt(arrayLine[0]); 
					int minutes = Integer.parseInt(arrayLine[1]);
					for(int i = 2; i<arrayLine.length; i++) {
						path = path.concat(arrayLine[i]);
						if(arrayLine.length!=i+1) {
							path = path.concat(" ");
						}
					}
					Change change = new Change(hour, minutes, path);
					schedule.addChange(change, true);
					path = "";
				}
				buffer.close();
			} catch (FileNotFoundException e) {
	
				e.printStackTrace();
			}
    	}
    }
}
