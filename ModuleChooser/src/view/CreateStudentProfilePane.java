package view;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Course;
import model.Name;

public class CreateStudentProfilePane extends VBox {

	private ComboBox<Course> cboCourse;
	private TextField txtSurname, txtFirstName, txtPnumber, txtEmail;
	private DatePicker inputDate;
	private Button btnCreate;
	private Text error;
	private GridPane gridpane;

	public CreateStudentProfilePane() {

		// error
		error = new Text();

		gridpane = new GridPane();
		gridpane.setVgap(15);
		gridpane.setHgap(20);
		gridpane.setAlignment(Pos.CENTER);

		ColumnConstraints column0 = new ColumnConstraints();
		column0.setHalignment(HPos.RIGHT);

		gridpane.getColumnConstraints().addAll(column0);

		// create labels
		Label lblTitle = new Label("Select course: ");
		Label lblPnumber = new Label("Input P number: ");
		Label lblFirstName = new Label("Input first name: ");
		Label lblSurname = new Label("Input surname: ");
		Label lblEmail = new Label("Input email: ");
		Label lblDate = new Label("Input date: ");

		// setup combobox
		cboCourse = new ComboBox<Course>(); // will be populated via method towards end of class

		// setup text fields
		txtFirstName = new TextField();
		txtSurname = new TextField();
		txtPnumber = new TextField();
		txtEmail = new TextField();

		inputDate = new DatePicker();

		// initialise create button
		btnCreate = new Button("Create Profile");

		// add controls and labels to container
		gridpane.add(lblTitle, 0, 0);
		gridpane.add(cboCourse, 1, 0);

		gridpane.add(lblPnumber, 0, 1);
		gridpane.add(txtPnumber, 1, 1);

		gridpane.add(lblFirstName, 0, 2);
		gridpane.add(txtFirstName, 1, 2);

		gridpane.add(lblSurname, 0, 3);
		gridpane.add(txtSurname, 1, 3);

		gridpane.add(lblEmail, 0, 4);
		gridpane.add(txtEmail, 1, 4);

		gridpane.add(lblDate, 0, 5);
		gridpane.add(inputDate, 1, 5);

		gridpane.add(new HBox(), 0, 6);
		gridpane.add(btnCreate, 1, 6);

		gridpane.add(new HBox(), 0, 7);
		gridpane.add(error, 1, 7);

		HBox gridpaneRow = new HBox();
		gridpaneRow.getChildren().add(gridpane);

		gridpaneRow.setAlignment(Pos.CENTER);
		gridpane.setAlignment(Pos.CENTER);
		VBox.setVgrow(gridpaneRow, Priority.ALWAYS);
		VBox.setVgrow(gridpane, Priority.ALWAYS);

		HBox errorRow = new HBox();
		errorRow.getChildren().add(error);
		error.setStyle("-fx-fill: red;");
		errorRow.setPadding(new Insets(0, 0, 10, 10));

		this.getChildren().addAll(gridpaneRow, errorRow);
	}

	// method to allow the controller to populate the course combobox
	public void populateCourseComboBox(Course[] courses) {
		cboCourse.getItems().addAll(courses);
		cboCourse.getSelectionModel().select(0); // select first course by default
	}

	// methods to retrieve the form selection/input
	public Course getSelectedCourse() {
		return cboCourse.getSelectionModel().getSelectedItem();
	}

	public String getPnumberInput() {
		return txtPnumber.getText();
	}

	public Name getName() {
		return new Name(txtFirstName.getText(), txtSurname.getText());
	}

	public String getEmail() {
		return txtEmail.getText();
	}

	public LocalDate getDate() {
		return inputDate.getValue();
	}
	

	public void setError(String error) {
		if (error.endsWith("\n")) {
			StringBuilder newError = new StringBuilder(error);
			newError.deleteCharAt(error.length() - 1);
			this.error.setText(newError.toString());
		} else {
			this.error.setText(error);
		}
	}

	// method to attach the create profile button event handler
	public void addCreateProfileHandler(EventHandler<ActionEvent> handler) {
		btnCreate.setOnAction(handler);
	}
}