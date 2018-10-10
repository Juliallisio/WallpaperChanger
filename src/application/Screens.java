package application;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;


import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Screens {
	public void setScreen(String screen, Stage primaryStage, Schedule schedule) {
			showRoot(screen, primaryStage, schedule);
	}
	public void showRoot(String screen, Stage primaryStage, Schedule schedule) {
		BorderPane root = new BorderPane();
		loadRootText(root);
		if(screen.equals("timed")) {
			loadDateForm(root, schedule);
		}
		Scene scene = new Scene(root,400,400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(300); 
		primaryStage.setMinWidth(300);
		primaryStage.setTitle("Wallpaper Changer");
		primaryStage.show();
	}
	public static void loadRootText(BorderPane root) { // This always loads
		Text text = new Text("Wallpaper Changer");
		HBox top = new HBox(text);
		top.getStyleClass().add("titleBox");
		text.getStyleClass().add("titleText");
		root.setTop(top);	
	}
	public static void loadDateForm(BorderPane root, Schedule schedule) {
		Alert alert = new Alert(AlertType.ERROR);	// Creating stuff needed
		Alert done = new Alert(AlertType.INFORMATION);
		FileChooser fileChooser = new FileChooser();
		ComboBox<String> hour = new ComboBox<String>();
		ComboBox<String> minutes = new ComboBox<String>();
		Stage fileSelection = new Stage();
		Button button = new Button("Submit");
		Button scheduleButton = new Button("Schedule");
		Button selectFile = new Button("Browse");
		TextField textField = new TextField ();
		HBox file = new HBox(textField, selectFile);
		HBox comboBoxes = new HBox(hour, minutes);
		HBox bottom = new HBox(button, scheduleButton);
		VBox bottomBox = new VBox(15);
		HBox spacing = new HBox();
		VBox center = new VBox(10);
		center.getChildren().addAll(comboBoxes, file);
		bottomBox.getChildren().addAll(bottom, spacing);
		
		hour.setValue("Hours");	// Setting placeholders
		minutes.setValue("Minutes"); 
		
		center.getStyleClass().add("centerBox"); // Creating style classes 
		comboBoxes.getStyleClass().add("timeSelector");
		bottom.getStyleClass().add("bottomBox");
		file.getStyleClass().add("fileSelector");
		
		root.setCenter(center);
		root.setBottom(bottomBox);
		
		// Configuring some things
		textField.setEditable(false); // The user is not allowed to write inside the text field, the only way to change it is using the file chooser.
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

		
		for(int i = 0; i<24; i++) {
			if(i<10) {
				hour.getItems().add("0" + Integer.toString(i));
			}
			else {
				hour.getItems().add(Integer.toString(i));
			}
		}
		for(int i = 0; i<60; i++) {
			if(i<10) {
				minutes.getItems().add("0" + Integer.toString(i));
			}
			else {
				minutes.getItems().add(Integer.toString(i));
			}
		}
		selectFile.setOnAction(e -> {
		    fileChooser.setTitle("Open File");
			File fileSelected = fileChooser.showOpenDialog(fileSelection);
			if(fileSelected!=null) {
				textField.setText(fileSelected.getAbsolutePath());
			}
		});
		button.setOnAction(e -> {
			if(minutes.getValue().equals("Minutes") || hour.getValue().equals("Hours")) { // If time is not selected, i.e ignored one or both checkboxes.
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Time not selected.");
				
				alert.showAndWait();
			}
			else if(textField.getText().equals("")) { // If file is not selected
 				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("File not selected.");
				
				alert.showAndWait();
			}
			else {
				int hourSelected = Integer.parseInt(hour.getValue());
				int minutesSelected = Integer.parseInt(minutes.getValue());
				Change change = new Change(hourSelected, minutesSelected, textField.getText());
				if(schedule.addChange(change, false)){
					done.setTitle("Success!");
					done.setHeaderText(null);
					done.setContentText("Your request was added to the schedule");
					
					done.showAndWait();
				}
				else {
					alert.setTitle("Error");
					alert.setHeaderText(null);
					alert.setContentText("There is another change scheduled at that time.");
					
					alert.showAndWait();
				}
			}
		});
		scheduleButton.setOnAction(e -> {
			loadScheduleScreen(schedule);
		});
	}
	public static void loadScheduleScreen(Schedule schedule) {
		JFrame scheduleScreen = new JFrame("Schedule");
		JPanel textPanel = new JPanel();
		Dimension screenSize = new Dimension(400, 300);
		scheduleScreen.setMinimumSize(screenSize);
		scheduleScreen.pack();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		for(Change change : schedule.getList()) {
			JTextField openLink = new JTextField("Open");
			JTextField deleteLink = new JTextField("Delete");
			openLink.setForeground(Color.decode("0x5a92ed"));
			deleteLink.setForeground(Color.RED);
			openLink.addMouseListener(new MouseAdapter() {
			    public void mouseClicked(MouseEvent e) {
			        File file = new File(change.getPath());
			        try {
						Desktop.getDesktop().open(file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			    }
			});
			JTextField changes = new JTextField(change.toString());
			JPanel line = new JPanel();
			line.setLayout(new BoxLayout(line, BoxLayout.LINE_AXIS));
			line.add(changes);
			line.add(openLink);
			line.add(deleteLink);
			textPanel.add(line);
			deleteLink.addMouseListener(new MouseAdapter() {
			    public void mouseClicked(MouseEvent e) {
			        textPanel.remove(line);
			        scheduleScreen.setVisible(false); // Refresh schedule screen after deleting element
			        scheduleScreen.setVisible(true);
			        schedule.remove(change);
			    }
			});
		}
		scheduleScreen.add(textPanel);
		scheduleScreen.setVisible(true);
		
	}
}
