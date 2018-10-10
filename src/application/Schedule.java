package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class Schedule {
	private ArrayList<Change> schedule = new ArrayList<Change>();
	private File file;
	
	public Schedule() {
		createFile();
	}
	public boolean addChange(Change change, Boolean reloading) {
		for (Change element : this.schedule) {
			if(element.equals(change)) {
				return false;
			}
		}
		this.schedule.add(change);
		if(!reloading) {
			addToFile(change);
		}
		return true;
	}
	public ArrayList<Change> getList(){
		return this.schedule;
	}
	
	public void remove(Change change) {
		this.schedule.remove(change);
		try {
			removeFromFile(change);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void printSchedule() {
		for (Change change: this.schedule) {
			System.out.println(change);
		}
	}
	public void addToFile(Change change) {
		String write = (change.toFile());
		File file = this.file;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
	        writer.write(write);
	        writer.newLine();
	        writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}

	public void removeFromFile(Change change) throws IOException{
	    File temp = new File("temp.txt");
	    File schedule = this.file;
	    BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
	    BufferedReader br = new BufferedReader(new FileReader(schedule));
	    String removeID = change.toFile();
	    String currentLine;
	    while((currentLine = br.readLine()) != null){
	        String trimmedLine = currentLine.trim();
	        if(!trimmedLine.equals(removeID)){
	        	bw.write(currentLine + System.getProperty("line.separator"));
	        }
	    }
	    bw.close();
	    br.close();
	    schedule.delete();
	    temp.renameTo(schedule);
	}
	public void createFile() {
		File directory = new File (getLocalPath());
		directory.mkdir();
		this.file = new File(directory.getAbsolutePath()+"\\schedule.txt");
	}
	public String getLocalPath() {
		String path = "";
		String sysProps = System.getProperty("os.name");
		if(sysProps.contains("Windows")) {
			path = System.getenv("APPDATA")+"\\WallpaperChanger";
		}
		return path;
	}
}
