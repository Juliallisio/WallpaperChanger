package application;


public class Change {
	private int hours;
	private int minutes;
	private String imgPath;

	
	public Change(int hours, int minutes, String imgPath) {
		this.hours = hours;
		this.minutes = minutes;
		this.imgPath = imgPath;
	}
	public int getHours() {
		return this.hours;
	}
	public int getMinutes() {
		return this.minutes;
	}
	public String getPath() {
		return this.imgPath;
	}
	public void changeTime(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}
	public void changeImg(String imgPath) {
		this.imgPath = imgPath;
	}

	public String toFile() {
		return String.format("%02d", this.hours) + " " + String.format("%02d", this.minutes) + " " + this.imgPath;
	}
	public String toString() {
		return String.format("%02d", this.hours) + ":" + String.format("%02d", this.minutes) + " - " + this.imgPath;
	}
	public boolean equals(Change change) {
		if(this.hours == change.getHours() && this.minutes == change.getMinutes()) {
			return true;
		}
		return false;
	}
	
	
}
