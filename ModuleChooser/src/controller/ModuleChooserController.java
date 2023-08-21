package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import model.Course;
import model.Schedule;
import model.Module;
import model.StudentProfile;
import view.CreateStudentProfilePane;
import view.ModuleChooserMenuBar;
import view.ModuleChooserRootPane;
import view.OverviewSelectionPane;
import view.ReserveModulesPane;
import view.SelectModulesPane;

public class ModuleChooserController {

	private CreateStudentProfilePane cpp;
	private ModuleChooserMenuBar msmb;
	private OverviewSelectionPane osp;
	private ReserveModulesPane rmp;
	private SelectModulesPane smp;
	private ModuleChooserRootPane view;

	private ArrayList<Module> t1MandatoryModules;
	private ArrayList<Module> t2MandatoryModules;

	private int t1Credit;
	private int t2Credit;

	private final static int MAX_TOTAL_CREDIT_TERM_1 = 60;
	private final static int MAX_TOTAL_CREDIT_TERM_2 = 60;

	private int RMPterm1Credit;
	private int RMPterm2Credit;

	private StudentProfile model;
	private ArrayList<Module> t1Modules;
	private ArrayList<Module> t2Modules;
	private ArrayList<Module> yearLongModules;

	private String smpError = "";
	private String cppError = "";
	private String rmpError = "";

	public ModuleChooserController(StudentProfile model, ModuleChooserRootPane view) {
		RMPterm2Credit = 0;
		RMPterm1Credit = 0;

		this.model = model;
		this.view = view;

		cpp = view.getCreateProfilePane();
		msmb = view.getModuleSelectionMenuBar();
		osp = view.getOverviewSelectionPane();
		rmp = view.getReserveModulePane();
		smp = view.getSelectModulePane();

		cpp.populateCourseComboBox(generateAndGetCourses());

		this.attachEventHandlers();
	}

	private void attachEventHandlers() {
		
		cpp.addCreateProfileHandler(new CreateProfileHandler());

		smp.resetEventHandler(new ResetHandler());
		smp.addt1ModuleHandler(new AddTerm1ButtonHandler());
		smp.removeTerm1ModuleHandler(new RemoveTerm1ButtonHandler());
		smp.addt2ModuleHandler(new AddTerm2ButtonHandler());
		smp.removeTerm2ModuleHandler(new RemoveTerm2ButtonHandler());
		smp.submitEventHandler(new SubmitHandler());

		rmp.addTerm1ModuleHandler(new RMPAddTerm1ButtonHandler());
		rmp.removeTerm1ModuleHandler(new RMPRemoveTerm1ButtonHandler());
		rmp.addTerm2ModuleHandler(new RMPAddTerm2ButtonHandler());
		rmp.removeTerm2ModuleHandler(new RMPRemoveTerm2ButtonHandler());
		rmp.confirmTerm1Handler(new RMPConfirmTerm1ButtonHandler());
		rmp.confirmTerm2Handler(new RMPConfirmTerm2ButtonHandler());
		
		osp.addSaveProfileHandler(new SaveProfileHandler());
		msmb.addLoadHandler(new LoadMenuHandler());
		msmb.addSaveHandler(new SaveMenuHandler());
		msmb.addExitHandler(e -> System.exit(0));
		msmb.addAboutHandler(e -> this.alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", null,
	                "Module Chooser Wiktor Kumor P2602600"));
	}

	private class CreateProfileHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			smp.cleanView();
			rmp.cleanView();
			osp.cleanView();
			rmp.expandTerm1Pane();
			t1Credit = 0;
			t2Credit = 0;

			smpError = "";
			cppError = "";
			cpp.setError(cppError);
			smp.setError(smpError);

			if (cppIsValid() && cppError.isEmpty()) {
				cppError = "";
				cpp.setError(cppError);
				setupAndSetModules();
				view.changeTab(1);
			} else {
				cpp.setError(cppError);
				cppError = "";
			}
		}
	}

	private class ResetHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			smp.cleanView();
			rmp.cleanView();
			osp.cleanView();
			rmp.expandTerm1Pane();
			t1Credit = 0;
			t2Credit = 0;
			RMPterm1Credit = 0;
			RMPterm2Credit = 0;

			smpError = "";
			cppError = "";
			rmpError = "";

			cpp.setError(cppError);
			smp.setError(smpError);
			rmp.setError(rmpError);

			if (cppIsValid()) {
				setupAndSetModules();
			}
		}
	}

	private class AddTerm1ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			rmp.cleanView();

			if (smp.getSelectedUnselectedTerm1Module() != null) {

				if (t1Credit < MAX_TOTAL_CREDIT_TERM_1) {
					smpError = "";
					smp.setError(smpError);

					Module m = smp.getSelectedUnselectedTerm1Module();
					t1Credit += m.getModuleCredits();
					smp.removeSelectedUnselectedTerm1Module();
					smp.setSelectedTerm1Module(m);
					smp.setTerm1CreditScore(t1Credit);
				} else {
					smpError = "";
					smpError += "Term 1 credit score is 60.\n";
					smp.setError(smpError);
				}

			} else {
				smpError = "";
				smpError += "Please select an item in unselectected term 1 modules list.\n";
				smp.setError(smpError);
			}
		}
	}

	private class RemoveTerm1ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			smpError = "";
			smp.setError(smpError);

			rmp.cleanView();
			RMPterm1Credit = 0;
			RMPterm2Credit = 0;

			if (!smp.term1SelectedModulesIsEmpty()) {
				Module m = smp.getSelectedSelectedTerm1Module();

				if (!(m.isMandatory())) {
					smpError = "";
					smp.setError(smpError);

					smp.removeSelectedSelectedTerm1Module();
					smp.setUnselectedTerm1Module(m);

					t1Credit -= m.getModuleCredits();
					smp.setTerm1CreditScore(t1Credit);

					smpError = "";
					smp.setError(smpError);
				} else {
					smpError = "";
					smpError += "Cannot remove (" + m + ") because it is mandatory.\n";
					smp.setError(smpError);
				}
			} else {
				Module m = smp.getSelectedSelectedTerm1Module();

				if (m == null) {
					smpError = "";
					smpError += "Please select an item in selectected term 1 modules list.\n";
					smp.setError(smpError);
				}
			}
		}
	}

	private class AddTerm2ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			rmp.cleanView();

			if (smp.getSelectedUnselectedTerm2Module() != null) {
				if (t2Credit < MAX_TOTAL_CREDIT_TERM_2) {
					smpError = "";
					smp.setError(smpError);

					Module m = smp.getSelectedUnselectedTerm2Module();
					t2Credit += m.getModuleCredits();
					smp.removeSelectedUnselectedTerm2Module();
					smp.setSelectedTerm2Module(m);
					smp.setTerm2CreditScore(t2Credit);
				} else {
					smpError = "";
					smpError += "Term 2 credit score is 60.\n";
					smp.setError(smpError);
				}
			} else {
				smpError = "";
				smpError += "Please select an item in unselectected term 2 modules list.\n";
				smp.setError(smpError);
			}
		}
	}

	private class RemoveTerm2ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			rmp.cleanView();
			RMPterm1Credit = 0;
			RMPterm2Credit = 0;

			if (!smp.term2SelectedModulesIsEmpty()) {
				Module m = smp.getSelectedSelectedTerm2Module();

				if (!(m.isMandatory())) {
					smpError = "";
					smp.setError(smpError);

					smp.removeSelectedSelectedTerm2Module();
					smp.setUnselectedTerm2Module(m);

					t2Credit -= m.getModuleCredits();
					smp.setTerm2CreditScore(t2Credit);

					smpError = "";
					smp.setError(smpError);
				} else {
					smpError = "";
					smpError += "Cannot remove (" + m + ") because it is mandatory.\n";
					smp.setError(smpError);
				}
			} else {
				Module m = smp.getSelectedSelectedTerm2Module();

				if (m == null) {
					smpError = "";
					smpError += "Please select an item in selectected term 2 modules list.\n";
					smp.setError(smpError);
				}
			}
		}
	}

	private class SubmitHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			smpError = "";
			cppError = "";
			cpp.setError(cppError);
			smp.setError(smpError);

			rmp.cleanView();

			if (t1Credit == 60) {
				ArrayList<Module> unselectedTerm1Modules = smp.getUnselectedTerm1Modules();
				rmp.setUnselectedTerm1Modules(unselectedTerm1Modules);
			} else {
				if (t2Credit == 60) {
					smpError = "";
				}

				smpError += "Total credit for term 1 should be 60. Only " + t1Credit
						+ " credits worth is selected.\n";
				smp.setError(smpError);
			}

			if (t2Credit == 60) {
				ArrayList<Module> unselectedTerm2Modules = smp.getUnselectedTerm2Modules();
				rmp.setUnselectedTerm2Modules(unselectedTerm2Modules);
			} else {
				if (t1Credit == 60) {
					smpError = "";
				}

				smpError += "Total credit for term 2 should be 60. Only " + t2Credit
						+ " credits worth is selected.\n";
				smp.setError(smpError);
			}

			if (t1Credit == MAX_TOTAL_CREDIT_TERM_1 && t2Credit == MAX_TOTAL_CREDIT_TERM_2 && smpError == "") {
				view.changeTab(2);
				rmp.expandTerm1Pane();
			}
		}
	}
	
	public class SaveProfileHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            Collection<Module> modules = model.getAllSelectedModules();
            File file = new File(model.getStudentPnumber().toUpperCase() + ".txt");
            try {
                PrintWriter writer = new PrintWriter(file);
                writer.println("Name: " + model.getStudentName().getFullName());
                writer.println("PNum: " + model.getStudentPnumber().toUpperCase());
                writer.println("Email: " + model.getStudentEmail());
                writer.println("Date: " + model.getSubmissionDate());
                writer.println("Course: " + model.getStudentCourse());
                writer.println("Selected Modules: ");
                writer.println("================================");
                modules.forEach(m -> {
                    writer.println(m.toString());
                });
                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            alertDialogBuilder(AlertType.CONFIRMATION, "Success", null, "Your profile has been saved to " + model.getStudentPnumber().toUpperCase() + ".txt! \n\n" +
                    "This file can be found in this program's root directory.");
        }
    }
	
	public class SaveMenuHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            try {
                if(cpp.getPnumberInput().isEmpty()) {
                    alertDialogBuilder(AlertType.ERROR, "PNumber Required", null, "PNumber is required in order to save.");
                    return;
                }
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cpp.getPnumberInput().toUpperCase() + "Obj.dat"));
                oos.writeObject(cpp);
                oos.flush();

                alertDialogBuilder(AlertType.CONFIRMATION, "Success", null, "Your data has been saved");
            }
            catch (IOException ioE) {
                ioE.printStackTrace();
            }
        }
    }

    //load binary student data
    public class LoadMenuHandler implements EventHandler<ActionEvent> {
        CreateStudentProfilePane student;
        String pNum; //must be declared outside of handle to be final / effectively final
        public void handle(ActionEvent e) {
            TextInputDialog input = new TextInputDialog();
            input.setTitle("Enter PNumber");
            input.setContentText("Please enter a PNumber to load: ");
            Optional<String> inputPNum = input.showAndWait();
            inputPNum.ifPresent(p -> pNum = p); //checks the inputted PNumber data exists
            try {
                FileInputStream fis = new FileInputStream(pNum.toUpperCase() + "Obj.dat");
                ObjectInputStream ois = new ObjectInputStream(fis);

                student = (CreateStudentProfilePane) ois.readObject();

            }
            catch(IOException ioE) {
                alertDialogBuilder(AlertType.ERROR, "Error", null, "The requested ssp data was not found.");
            }
            catch (ClassNotFoundException c) {
                c.printStackTrace();
            }

            model.setStudentPnumber(student.getPnumberInput());
            model.setStudentName(student.getName());
            model.setStudentEmail(student.getEmail());
            model.setSubmissionDate(student.getDate());

        }
    }

    public void setProfileOverview() {
        TextArea profileArea = osp.getStudentProfile();
        Collection<Module> modules = model.getAllSelectedModules();
        profileArea.setText(
                "Name: " + model.getStudentName().getFullName() + "\n" +
                "PNum: " + model.getStudentPnumber().toUpperCase() + "\n" +
                "Email: " + model.getStudentEmail() + "\n" +
                "Date: " + model.getSubmissionDate() + "\n" +
                "Course: " + model.getStudentCourse() + "\n\n" +
                "Selected Modules: " + "\n" + "================================" + "\n"
        );
        modules.forEach(m -> {
            profileArea.appendText(m.toString() + "\n"); //utilises the toString method in module.java to return formatted module list.
        });
    }

	private Course[] generateAndGetCourses() {
		Module imat3423 = new Module("IMAT3423", "Systems Building: Methods", 15, true, Schedule.TERM_1);
		Module ctec3451 = new Module("CTEC3451", "Development Project", 30, true, Schedule.YEAR_LONG);
		Module ctec3902_SoftEng = new Module("CTEC3902", "Rigorous Systems", 15, true, Schedule.TERM_2);	
		Module ctec3902_CompSci = new Module("CTEC3902", "Rigorous Systems", 15, false, Schedule.TERM_2);
		Module ctec3110 = new Module("CTEC3110", "Secure Web Application Development", 15, false, Schedule.TERM_1);
		Module ctec3605 = new Module("CTEC3605", "Multi-service Networks 1", 15, false, Schedule.TERM_1);	
		Module ctec3606 = new Module("CTEC3606", "Multi-service Networks 2", 15, false, Schedule.TERM_2);	
		Module ctec3410 = new Module("CTEC3410", "Web Application Penetration Testing", 15, false, Schedule.TERM_2);
		Module ctec3904 = new Module("CTEC3904", "Functional Software Development", 15, false, Schedule.TERM_2);
		Module ctec3905 = new Module("CTEC3905", "Front-End Web Development", 15, false, Schedule.TERM_2);
		Module ctec3906 = new Module("CTEC3906", "Interaction Design", 15, false, Schedule.TERM_1);
		Module ctec3911 = new Module("CTEC3911", "Mobile Application Development", 15, false, Schedule.TERM_1);
		Module imat3410 = new Module("IMAT3104", "Database Management and Programming", 15, false, Schedule.TERM_2);
		Module imat3406 = new Module("IMAT3406", "Fuzzy Logic and Knowledge Based Systems", 15, false, Schedule.TERM_1);
		Module imat3611 = new Module("IMAT3611", "Computer Ethics and Privacy", 15, false, Schedule.TERM_1);
		Module imat3613 = new Module("IMAT3613", "Data Mining", 15, false, Schedule.TERM_1);
		Module imat3614 = new Module("IMAT3614", "Big Data and Business Models", 15, false, Schedule.TERM_2);
		Module imat3428_CompSci = new Module("IMAT3428", "Information Technology Services Practice", 15, false, Schedule.TERM_2);


		Course compSci = new Course("Computer Science");
		compSci.addModuleToCourse(imat3423);
		compSci.addModuleToCourse(ctec3451);
		compSci.addModuleToCourse(ctec3902_CompSci);
		compSci.addModuleToCourse(ctec3110);
		compSci.addModuleToCourse(ctec3605);
		compSci.addModuleToCourse(ctec3606);
		compSci.addModuleToCourse(ctec3410);
		compSci.addModuleToCourse(ctec3904);
		compSci.addModuleToCourse(ctec3905);
		compSci.addModuleToCourse(ctec3906);
		compSci.addModuleToCourse(ctec3911);
		compSci.addModuleToCourse(imat3410);
		compSci.addModuleToCourse(imat3406);
		compSci.addModuleToCourse(imat3611);
		compSci.addModuleToCourse(imat3613);
		compSci.addModuleToCourse(imat3614);
		compSci.addModuleToCourse(imat3428_CompSci);

		Course softEng = new Course("Software Engineering");
		softEng.addModuleToCourse(imat3423);
		softEng.addModuleToCourse(ctec3451);
		softEng.addModuleToCourse(ctec3902_SoftEng);
		softEng.addModuleToCourse(ctec3110);
		softEng.addModuleToCourse(ctec3605);
		softEng.addModuleToCourse(ctec3606);
		softEng.addModuleToCourse(ctec3410);
		softEng.addModuleToCourse(ctec3904);
		softEng.addModuleToCourse(ctec3905);
		softEng.addModuleToCourse(ctec3906);
		softEng.addModuleToCourse(ctec3911);
		softEng.addModuleToCourse(imat3410);
		softEng.addModuleToCourse(imat3406);
		softEng.addModuleToCourse(imat3611);
		softEng.addModuleToCourse(imat3613);
		softEng.addModuleToCourse(imat3614);

		Course[] courses = new Course[2];
		courses[0] = compSci;
		courses[1] = softEng;

		return courses;
	}
	
	private void alertDialogBuilder(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

	private void setupAndSetModules() {
		model = new StudentProfile();
		model.setStudentEmail(cpp.getEmail());
		model.setStudentPnumber(cpp.getPnumberInput());
		model.setStudentCourse(cpp.getSelectedCourse());
		model.setStudentName(cpp.getName());
		model.setSubmissionDate(cpp.getDate());

		Course[] courses = generateAndGetCourses();

		t1Modules = new ArrayList<>();
		t1MandatoryModules = new ArrayList<>();

		t2Modules = new ArrayList<>();
		t2MandatoryModules = new ArrayList<>();

		yearLongModules = new ArrayList<>();

		for (Course course : courses) {
			if (course.getCourseName() == model.getStudentCourse().getCourseName()) {
				for (Module module : course.getAllModulesOnCourse()) {
					if (module.getSchedule() == Schedule.TERM_1) {
						if (module.isMandatory()) {
							t1MandatoryModules.add(module);
						} else {
							t1Modules.add(module);
						}
					} else if (module.getSchedule() == Schedule.TERM_2) {
						if (module.isMandatory()) {
							t2MandatoryModules.add(module);
						} else {
							t2Modules.add(module);
						}
					} else if (module.getSchedule() == Schedule.YEAR_LONG) {
						yearLongModules.add(module);
					}
				}
			}
		}

		smp.setUnselectedTerm1Modules(t1Modules);
		for (Module m : t1MandatoryModules) {
			t1Credit += m.getModuleCredits();
			smp.setSelectedTerm1Modules(t1MandatoryModules);
			smp.setTerm1CreditScore(t1Credit);
		}

		smp.setUnselectedTerm2Modules(t2Modules);
		for (Module m : t2MandatoryModules) {
			t2Credit += m.getModuleCredits();
			smp.setSelectedTerm2Modules(t2MandatoryModules);
			smp.setTerm2CreditScore(t2Credit);
		}

		smp.setSelectedYearLongModules(yearLongModules);

		ArrayList<Integer> tempYearLongList = new ArrayList<>();
		for (Module m : yearLongModules) {
			int half = m.getModuleCredits() / 2;
			tempYearLongList.add(half);
		}

		for (int c : tempYearLongList) {
			smp.setTerm1CreditScore(t1Credit += c);
		}

		for (int c : tempYearLongList) {
			smp.setTerm2CreditScore(t2Credit += c);
		}
	}

	public boolean cppIsValid() {
		boolean isValid = true;

		if (cpp.getPnumberInput().isEmpty()) {
			cppError += "PNumber should not be empty.\n";
			isValid = false;
		}

		if (cpp.getName().getFirstName().isEmpty()) {
			cppError += "First name should not be empty.\n";
			isValid = false;
		}

		if (cpp.getName().getFamilyName().isEmpty()) {
			cppError += "Family name should not be empty.\n";
			isValid = false;
		}

		if (cpp.getEmail().isEmpty()) {
			cppError += "Email should not be empty.\n";
			isValid = false;
		}

		if (cpp.getDate() == null) {
			cppError += "Date should not be empty.\n";
			isValid = false;
		}

		return isValid;
	}

	private class RMPAddTerm1ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (rmp.getSelectedUnselectedTerm1Module() != null) {

				if (RMPterm1Credit < 30) {
					rmpError = "";
					rmp.setError(rmpError);

					Module m = rmp.getSelectedUnselectedTerm1Module();

					RMPterm1Credit += m.getModuleCredits();
					rmp.removeSelectedUnselectedTerm1Module();
					rmp.addToReservedTerm1Module(m);
				} else {
					rmpError = "";
					rmp.setError(rmpError);

					rmpError += "You cannot reserve more than 30 credits.\n";
					rmp.setError(rmpError);
				}
			} else {
				rmpError = "";
				rmp.setError(rmpError);

				rmpError += "Please select an item in the unselected term 1 modules list.\n";
				rmp.setError(rmpError);
			}
		}
	}

	private class RMPRemoveTerm1ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (rmp.getSelectedReservedTerm1Module() != null) {
				rmpError = "";
				rmp.setError(rmpError);

				Module m = rmp.getSelectedReservedTerm1Module();

				RMPterm1Credit -= m.getModuleCredits();
				rmp.removeSelectedReservedTerm1Module();
				rmp.addToUnselectedTerm1Module(m);
			} else {
				rmpError = "";
				rmp.setError(rmpError);

				rmpError += "Please select an item in term 1 reserved modules list.\n";
				rmp.setError(rmpError);
			}
		}
	}

	private class RMPConfirmTerm1ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			rmpError = "";
			rmp.setError(rmpError);

			int term1Credit = 0;

			for (Module m : rmp.getReservedTerm1Modules()) {
				term1Credit += m.getModuleCredits();
			}

			if (!rmp.term1PaneIsEmpty()) {
				if (term1Credit == 30) {
					rmp.expandTerm2Pane();
				} else {
					rmpError = "";
					rmp.setError(rmpError);

					rmpError += "Please reserve 30 credits of term 1 modules.";
					rmp.setError(rmpError);
				}
			} else {
				rmpError = "";
				rmp.setError(rmpError);

				rmpError += "Please select a total amount of 30 credits for term 1 reserved modules.";
				rmp.setError(rmpError);
			}
		}
	}

	private class RMPAddTerm2ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (rmp.getSelectedUnselectedTerm2Module() != null) {

				if (RMPterm2Credit < 30) {
					rmpError = "";
					rmp.setError(rmpError);

					Module m = rmp.getSelectedUnselectedTerm2Module();

					RMPterm2Credit += m.getModuleCredits();
					rmp.removeSelectedUnselectedTerm2Module();
					rmp.addToReservedTerm2Module(m);
				} else {
					rmpError = "";
					rmp.setError(rmpError);

					rmpError += "You cannot reserve more than 30 credits.\n";
					rmp.setError(rmpError);
				}
			} else {
				rmpError = "";
				rmp.setError(rmpError);

				rmpError += "Please select an item in the unselected term 2 modules list.\n";
				rmp.setError(rmpError);
			}
		}
	}

	private class RMPRemoveTerm2ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (rmp.getSelectedReservedTerm2Module() != null) {
				rmpError = "";
				rmp.setError(rmpError);

				Module m = rmp.getSelectedReservedTerm2Module();

				RMPterm2Credit -= m.getModuleCredits();
				rmp.removeSelectedReservedTerm2Module();
				rmp.addToUnselectedTerm2Module(m);
			} else {
				rmpError = "";
				rmp.setError(rmpError);

				rmpError += "Please select an item in term 2 reserved modules list.\n";
				rmp.setError(rmpError);
			}
		}
	}

	private class RMPConfirmTerm2ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			rmpError = "";
			rmp.setError(rmpError);

			int term2Credit = 0;

			for (Module m : rmp.getReservedTerm2Modules()) {
				term2Credit += m.getModuleCredits();
			}

			if (!rmp.term2PaneIsEmpty()) {

				if (term2Credit >= 30) {
					for (Module m : smp.getSelectedTerm1Modules())
						model.addSelectedModule(m);

					for (Module m : smp.getSelectedTerm2Modules())
						model.addSelectedModule(m);

					for (Module m : smp.getYearLongModules())
						model.addSelectedModule(m);

					for (Module m : rmp.getReservedTerm1Modules())
						model.addReservedModule(m);

					for (Module m : rmp.getReservedTerm2Modules())
						model.addReservedModule(m);

					view.changeTab(3);

					StringBuilder userDetails = new StringBuilder();
					userDetails.append("Name: " + model.getStudentName().getFirstName() + " "
							+ model.getStudentName().getFamilyName() + "\n");
					userDetails.append("PNo: " + model.getStudentPnumber() + "\n");
					userDetails.append("Email: " + model.getStudentEmail() + "\n");
					userDetails.append("Date: " + model.getSubmissionDate() + "\n");
					userDetails.append("Course: " + model.getStudentCourse().getCourseName());

					osp.setDetailsTextArea(userDetails.toString());

					StringBuilder selectedModulesDetails = new StringBuilder();
					selectedModulesDetails.append("Selected Modules" + "\n");
					selectedModulesDetails.append("=========" + "\n");

					for (Module m : model.getAllSelectedModules()) {
						selectedModulesDetails.append("Module Code: " + m.getModuleCode() + ", ");
						selectedModulesDetails.append("Module Name: " + m.getModuleName() + ",\n");
						selectedModulesDetails.append("Credits: " + m.getModuleCredits() + ", ");
						selectedModulesDetails
								.append("Mandatory on your course? " + isMandatoryYesNo(m.isMandatory()) + ", ");
						selectedModulesDetails.append("Schedule: " + m.getSchedule() + "\n\n");
					}

					osp.setSelectedModulesTextArea(selectedModulesDetails.toString());

					StringBuilder reservedModulesDetails = new StringBuilder();
					reservedModulesDetails.append("Reserved Modules" + "\n");
					reservedModulesDetails.append("=========" + "\n");

					for (Module m : model.getAllReservedModules()) {
						reservedModulesDetails.append("Module Code: " + m.getModuleCode() + ", ");
						reservedModulesDetails.append("Module Name: " + m.getModuleName() + ",\n");
						reservedModulesDetails.append("Credits: " + m.getModuleCredits() + ", ");
						reservedModulesDetails.append("Schedule: " + m.getSchedule() + "\n\n");
					}

					osp.setReservedModulesTextArea(reservedModulesDetails.toString());

				} else {
					rmpError = "";
					rmp.setError(rmpError);

					rmpError += "Please select a total amount of 30 credits for term 2 reserved modules.";
					rmp.setError(rmpError);
				}
			} else {
				rmpError = "";
				rmp.setError(rmpError);

				rmpError += "Please select a total amount of 30 credits for term 2 reserved modules.";
				rmp.setError(rmpError);
			}
		}

		public String isMandatoryYesNo(boolean bool) {
			if (bool)
				return "yes";
			else
				return "no";
		}
	}
}

