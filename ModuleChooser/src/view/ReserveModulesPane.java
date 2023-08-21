package view;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Module;

public class ReserveModulesPane extends VBox {

	private ListView<Module> unselectedt1Modules, reservedt1Modules, unselectedt2Modules, reservedt2Modules;
	private Button t1AddButton, t1RemoveButton, t1ConfirmButton, t2AddButton, t2RemoveButton, t2ConfirmButton;
	TitledPane t1Modules, t2Modules;
	private Text error;

	public ReserveModulesPane() {
		error = new Text();
		error.setStyle("-fx-fill: red;");

		HBox errorscene = new HBox();
		errorscene.getChildren().add(error);
		errorscene.setPadding(new Insets(10, 0, 0, 0));

		VBox unselectedTerm1ModulesContainer = new VBox();
		unselectedt1Modules = new ListView<>();
		unselectedTerm1ModulesContainer.getChildren().addAll(new Label("Unselected Term 1 modules"),
				unselectedt1Modules);

		VBox reservedTerm1ModulesContainer = new VBox();
		reservedt1Modules = new ListView<>();
		reservedTerm1ModulesContainer.getChildren().addAll(new Label("Reserved Term 1 modules"), reservedt1Modules);

		HBox term1ModulesContainerTop = new HBox(10);
		term1ModulesContainerTop.getChildren().addAll(unselectedTerm1ModulesContainer, reservedTerm1ModulesContainer);

		HBox term1ModulesButtons = new HBox(10);
		t1AddButton = new Button("Add");
		t1RemoveButton = new Button("Remove");
		t1ConfirmButton = new Button("Confirm");
		term1ModulesButtons.getChildren().addAll(new Label("Reserve 30 credits worth of term 1 modules"),
				t1AddButton, t1RemoveButton, t1ConfirmButton);
		term1ModulesButtons.setAlignment(Pos.BASELINE_CENTER);

		VBox term1ModulesContainer = new VBox(10);
		term1ModulesContainer.getChildren().addAll(term1ModulesContainerTop, term1ModulesButtons);

		VBox unselectedTerm2ModulesContainer = new VBox();
		unselectedt2Modules = new ListView<>();
		unselectedTerm2ModulesContainer.getChildren().addAll(new Label("Unselected Term 2 modules"),
				unselectedt2Modules);

		VBox reservedTerm2ModulesContainer = new VBox();
		reservedt2Modules = new ListView<>();
		reservedTerm2ModulesContainer.getChildren().addAll(new Label("Reserved Term 2 modules"), reservedt2Modules);

		HBox term2ModulesContainerTop = new HBox(10);
		term2ModulesContainerTop.getChildren().addAll(unselectedTerm2ModulesContainer, reservedTerm2ModulesContainer);
		HBox.setHgrow(unselectedTerm2ModulesContainer, Priority.ALWAYS);
		HBox.setHgrow(reservedTerm1ModulesContainer, Priority.ALWAYS);

		HBox term2ModulesButtons = new HBox(10);
		t2AddButton = new Button("Add");
		t2RemoveButton = new Button("Remove");
		t2ConfirmButton = new Button("Confirm");
		term2ModulesButtons.getChildren().addAll(new Label("Reserve 30 credits worth of term 2 modules"),
				t2AddButton, t2RemoveButton, t2ConfirmButton);
		term2ModulesButtons.setAlignment(Pos.BASELINE_CENTER);

		VBox term2ModulesContainer = new VBox(10);
		term2ModulesContainer.getChildren().addAll(term2ModulesContainerTop, term2ModulesButtons);

		t1Modules = new TitledPane();
		t1Modules.setText("Term 1 Modules");
		t1Modules.setContent(term1ModulesContainer);

		t2Modules = new TitledPane();
		t2Modules.setText("Term 2 Modules");
		t2Modules.setContent(term2ModulesContainer);

		Accordion accordion = new Accordion();
		accordion.getPanes().setAll(t1Modules, t2Modules);
		accordion.setExpandedPane(t1Modules);

		VBox.setVgrow(unselectedt1Modules, Priority.ALWAYS);
		VBox.setVgrow(reservedt1Modules, Priority.ALWAYS);
		VBox.setVgrow(term1ModulesContainerTop, Priority.ALWAYS);
		VBox.setVgrow(unselectedt2Modules, Priority.ALWAYS);
		VBox.setVgrow(reservedt2Modules, Priority.ALWAYS);
		VBox.setVgrow(term2ModulesContainerTop, Priority.ALWAYS);
		VBox.setVgrow(accordion, Priority.ALWAYS);
		HBox.setHgrow(unselectedTerm1ModulesContainer, Priority.ALWAYS);
		HBox.setHgrow(reservedTerm1ModulesContainer, Priority.ALWAYS);
		HBox.setHgrow(unselectedTerm2ModulesContainer, Priority.ALWAYS);
		HBox.setHgrow(reservedTerm2ModulesContainer, Priority.ALWAYS);

		this.getChildren().addAll(accordion, errorscene);
		this.setPadding(new Insets(10));
	}

	public void setUnselectedTerm1Modules(ArrayList<Module> modules) {
		this.unselectedt1Modules.getItems().addAll(modules);
	}

	public void setUnselectedTerm2Modules(ArrayList<Module> modules) {
		this.unselectedt2Modules.getItems().addAll(modules);
	}

	public void addToReservedTerm1Module(Module module) {
		this.reservedt1Modules.getItems().add(module);
	}

	public void addToReservedTerm2Module(Module module) {
		this.reservedt2Modules.getItems().add(module);
	}

	public void addTerm1ModuleHandler(EventHandler<ActionEvent> handler) {
		this.t1AddButton.setOnAction(handler);
	}

	public Module getSelectedUnselectedTerm1Module() {
		return this.unselectedt1Modules.getSelectionModel().getSelectedItem();
	}

	public Module getSelectedUnselectedTerm2Module() {
		return this.unselectedt2Modules.getSelectionModel().getSelectedItem();
	}

	public void removeSelectedUnselectedTerm1Module() {
		this.unselectedt1Modules.getItems().remove(this.getSelectedUnselectedTerm1Module());
		this.unselectedt1Modules.getSelectionModel().clearSelection();
	}

	public void removeSelectedUnselectedTerm2Module() {
		this.unselectedt2Modules.getItems().remove(this.getSelectedUnselectedTerm2Module());
		this.unselectedt2Modules.getSelectionModel().clearSelection();
	}

	public Module getSelectedReservedTerm1Module() {
		return this.reservedt1Modules.getSelectionModel().getSelectedItem();
	}

	public Module getSelectedReservedTerm2Module() {
		return this.reservedt2Modules.getSelectionModel().getSelectedItem();
	}

	public void removeSelectedReservedTerm1Module() {
		this.reservedt1Modules.getItems().remove(this.getSelectedReservedTerm1Module());
		this.reservedt1Modules.getSelectionModel().clearSelection();
	}

	public void removeSelectedReservedTerm2Module() {
		this.reservedt2Modules.getItems().remove(this.getSelectedReservedTerm2Module());
		this.reservedt2Modules.getSelectionModel().clearSelection();
	}

	public void addToUnselectedTerm1Module(Module m) {
		this.unselectedt1Modules.getItems().add(m);
	}

	public void addToUnselectedTerm2Module(Module m) {
		this.unselectedt2Modules.getItems().add(m);
	}

	public void removeTerm1ModuleHandler(EventHandler<ActionEvent> handler) {
		this.t1RemoveButton.setOnAction(handler);
	}

	public void removeTerm2ModuleHandler(EventHandler<ActionEvent> handler) {
		this.t2RemoveButton.setOnAction(handler);
	}

	public void addTerm2ModuleHandler(EventHandler<ActionEvent> event) {
		this.t2AddButton.setOnAction(event);
	}

	public void confirmTerm1Handler(EventHandler<ActionEvent> event) {
		this.t1ConfirmButton.setOnAction(event);
	}

	public void confirmTerm2Handler(EventHandler<ActionEvent> event) {
		this.t2ConfirmButton.setOnAction(event);
	}

	public void expandTerm2Pane() {
		this.t2Modules.setExpanded(true);
	}

	public void expandTerm1Pane() {
		this.t1Modules.setExpanded(true);
	}

	public boolean term1PaneIsEmpty() {
		return this.reservedt1Modules.getItems().isEmpty();
	}

	public boolean term2PaneIsEmpty() {
		return this.reservedt2Modules.getItems().isEmpty();
	}

	public ArrayList<Module> getReservedTerm1Modules() {
		ArrayList<Module> items = new ArrayList<>();
		for (Object m : this.reservedt1Modules.getItems().toArray())
			items.add((Module) m);

		return items;
	}

	public ArrayList<Module> getReservedTerm2Modules() {
		ArrayList<Module> items = new ArrayList<>();
		for (Object m : this.reservedt2Modules.getItems().toArray())
			items.add((Module) m);

		return items;
	}

	public void cleanView() {
		this.reservedt1Modules.getItems().clear();
		this.reservedt2Modules.getItems().clear();
		this.unselectedt1Modules.getItems().clear();
		this.unselectedt2Modules.getItems().clear();
	}

	public void setError(String error) {
		if (error.endsWith("\n")) {
			StringBuilder newError = new StringBuilder(error);
			newError.deleteCharAt(error.length() - 1);
			this.error.setText(newError.toString());
		} else {
			this.error.setText(error);
		}
	}}