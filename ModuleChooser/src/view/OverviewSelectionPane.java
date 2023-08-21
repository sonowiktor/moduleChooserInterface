package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class OverviewSelectionPane extends VBox {

	private TextArea txtDetails;
	private TextArea txtSelectedModules;
	private TextArea txtReservedModules;
	private Button btnSaveOverview;

	public OverviewSelectionPane() {

		txtDetails = new TextArea();
		txtDetails.setEditable(false);

		HBox container1 = new HBox();
		container1.getChildren().add(txtDetails);
		container1.setPadding(new Insets(0, 0, 10, 0));

		txtSelectedModules = new TextArea();
		txtSelectedModules.setEditable(false);

		txtReservedModules = new TextArea();
		txtReservedModules.setEditable(false);

		HBox container2 = new HBox(10);
		container2.getChildren().addAll(txtSelectedModules, txtReservedModules);

		btnSaveOverview = new Button("Save Overview");

		HBox container3 = new HBox();
		container3.getChildren().add(btnSaveOverview);
		container3.setAlignment(Pos.BASELINE_CENTER);
		container3.setPadding(new Insets(10, 0, 0, 0));

		HBox.setHgrow(txtDetails, Priority.ALWAYS);
		HBox.setHgrow(txtSelectedModules, Priority.ALWAYS);
		HBox.setHgrow(txtReservedModules, Priority.ALWAYS);

		VBox.setVgrow(container2, Priority.ALWAYS);
		VBox.setVgrow(container1, Priority.ALWAYS);

		this.getChildren().addAll(container1, container2, container3);
		this.setPadding(new Insets(10));
	}

	public void setSelectedModulesTextArea(String data) {
		this.txtSelectedModules.setText(data);
	}

	public void setReservedModulesTextArea(String data) {
		this.txtReservedModules.setText(data);
	}

	public void setDetailsTextArea(String data) {
		this.txtDetails.setText(data);
	}

	public void cleanView() {
		this.txtDetails.clear();
		this.txtReservedModules.clear();
		this.txtSelectedModules.clear();
	}
	
	 public void addSaveProfileHandler(EventHandler<ActionEvent> handler) {
		 btnSaveOverview.setOnAction(handler);
	    }
	
	public TextArea getStudentProfile() {
        return txtDetails;
    }
}