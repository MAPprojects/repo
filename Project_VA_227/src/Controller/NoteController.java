package Controller;

import Domain.Action;
import Domain.Homework;
import Domain.Note;
import Domain.Student;
import Service.AbstractService;
import Service.NoteService;
import Utils.Observable;
import Utils.ResourceManager;
import Utils.Utils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class NoteController extends AbstractController<Integer, Note> {

    @FXML
    public TableColumn<Note, String> studentColumn;
    public TableColumn<Note, String> homeworkColumn;
    public TableColumn<Object, Object> valueColumn;
    public TableColumn<Object, Object> weekColumn;
    public TableColumn<Object, Object> observationsColumn;
    public TableColumn<Object, Object> actionColumn;

    public TextField observationsText;
    public ComboBox<String> studentCombo;
    public ComboBox<String> homeworkCombo;
    public ComboBox<String> gradeCombo;
    public ComboBox<String> weekCombo;
    public TextField searchStudent;
    public TextField searchHomework;

    public NoteController() {
        super();
    }

    @Override
    protected void showDetails(Note newValue) {
        if (newValue == null) {
            clearDetails();
            return;
        }
        SelectedId = newValue.getId();
        studentCombo.getSelectionModel().select(noteService.getStudentService().find(newValue.getStudentID()).getNume());
        homeworkCombo.getSelectionModel().select(noteService.getHomeworkService().find(newValue.getHomeworkID()).getTask());
        gradeCombo.setValue(newValue.getValue().toString());
        observationsText.setText(newValue.getObservatii());
        weekCombo.setValue(newValue.getSapt_predare().toString());
    }

    private void clearDetails() {
        gradeCombo.setValue(null);
        weekCombo.setValue(null);
        observationsText.clear();
        studentCombo.setValue(null);
        homeworkCombo.setValue(null);
        SelectedId = null;
        searchStudent.clear();
        searchHomework.clear();
    }

    private List<Student> studentList = null;
    private List<Homework> homeworkList = null;

    private void loadStudents() {
        studentList = new ArrayList<>();
        studentCombo.getItems().clear();
        StreamSupport.stream(noteService.getStudentService().getAll().spliterator(), false)
                .map(noteService.getStudentService()::find)
                .forEach(student -> {
                    studentCombo.getItems().add(student.getNume());
                    studentList.add(student);
                });
        studentCombo.setValue(null);
        searchStudent.clear();
    }

    private void loadStudents(List<Student> students) {
        studentList.clear();
        studentCombo.getItems().clear();
        students.forEach(student -> {
            studentCombo.getItems().add(student.getNume());
            studentList.add(student);
        });
        studentCombo.setValue(null);
    }

    private void loadHomeworks() {
        homeworkList = new ArrayList<>();
        homeworkCombo.getItems().clear();
        StreamSupport.stream(noteService.getHomeworkService().getAll().spliterator(), false)
                .map(noteService.getHomeworkService()::find)
                .forEach(homework -> {
                    homeworkCombo.getItems().add(homework.getTask());
                    homeworkList.add(homework);
                });
        homeworkCombo.setValue(null);
        searchHomework.clear();
    }

    private void loadHomeworks(List<Homework> homeworks) {
        homeworkList.clear();
        homeworkCombo.getItems().clear();
        homeworks.forEach(homework -> {
            homeworkCombo.getItems().add(homework.getTask());
            homeworkList.add(homework);
        });
        homeworkCombo.setValue(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchStudent.textProperty().addListener((a, b, c) -> searchS());
        searchHomework.textProperty().addListener((a, b, c) -> searchH());
        for (Integer grade = 1; grade <= 10; grade++)
            gradeCombo.getItems().add(grade.toString());
        for (Integer week = 1; week <= 14; week++)
            weekCombo.getItems().add(week.toString());
        this.model = FXCollections.observableArrayList();
        this.table.setItems(model);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldvalue, newValue) -> showDetails(newValue)
        );

        studentColumn.setCellValueFactory(cd -> Bindings.createStringBinding(noteService.getStudentService().find(cd.getValue().getStudentID())::getNume));
        homeworkColumn.setCellValueFactory(cd -> Bindings.createStringBinding(noteService.getHomeworkService().find(cd.getValue().getHomeworkID())::getTask));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        weekColumn.setCellValueFactory(new PropertyValueFactory<>("sapt_predare"));
        observationsColumn.setCellValueFactory(new PropertyValueFactory<>("observatii"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));

    }

    @Override
    public void setService(AbstractService<Integer, Note> service) {
        super.setService(service);
        noteService = (NoteService) service;
        loadStudents();
        loadHomeworks();
    }

    private NoteService noteService;

    private Integer SelectedId = null;

    public void handleAction() {
        if (actionsChoose.getValue() == null) {
            showError("Select an action");
            return;
        }
        if ((actionsChoose.getValue().equals("Remove") || actionsChoose.getValue().equals("Update")) &&
                SelectedId == null) {
            showError("Select a grade");
            return;
        }
        switch (actionsChoose.getValue()) {
            case "Add":
                try {
                    Note note = new Note(studentList.get(studentCombo.getSelectionModel().getSelectedIndex()).getId(),
                            homeworkList.get(homeworkCombo.getSelectionModel().getSelectedIndex()).getId(),
                            Integer.parseInt(gradeCombo.getValue()),
                            homeworkList.get(homeworkCombo.getSelectionModel().getSelectedIndex()).getDeadline(),
                            Integer.parseInt(weekCombo.getValue()),
                            observationsText.getText(), Action.ADD);
                    this.add(note);
                    if (this.user.getMailOption() == 1)
                        MainMenuController.sendGrades(noteService.filterStudent(note.getStudentID()), Action.ADD);
                    clearDetails();
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
            case "Remove":
                try {
                    this.delete(studentList.get(studentCombo.getSelectionModel().getSelectedIndex()).getId(),
                            homeworkList.get(homeworkCombo.getSelectionModel().getSelectedIndex()).getId());
                    clearDetails();
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
            case "Update":
                try {
                    Note note = new Note(studentList.get(studentCombo.getSelectionModel().getSelectedIndex()).getId(),
                            homeworkList.get(homeworkCombo.getSelectionModel().getSelectedIndex()).getId(),
                            Integer.parseInt(gradeCombo.getValue()),
                            homeworkList.get(homeworkCombo.getSelectionModel().getSelectedIndex()).getDeadline(),
                            Integer.parseInt(weekCombo.getValue()),
                            observationsText.getText(), Action.MODIFY);
                    this.update(note);
                    MainMenuController.sendGrades(noteService.filterStudent(note.getStudentID()), Action.MODIFY);
                    clearDetails();
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
            case "Get All":
                this.populateList(true);
                break;
            case "Filter":
                try {
                    Integer id1 = studentCombo.getSelectionModel().getSelectedIndex();
                    Integer id2 = homeworkCombo.getSelectionModel().getSelectedIndex();
                    Integer id3 = gradeCombo.getSelectionModel().getSelectedIndex();
                    this.populateList(noteService.filter(id1 == -1? null : studentList.get(id1).getId(),
                            id2 == -1 ? null : homeworkList.get(id2).getId(),
                            id3 == -1 ? null : Utils.tryParseInt(gradeCombo.getValue())));
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
        }
    }

    public void delete(Integer studentId, Integer homeworkId) {
        noteService.delete(studentId, homeworkId);
    }

    @Override
    public void notifyOnEvent(Observable<Note> e) {
        super.notifyOnEvent(e);
        if (studentList.size() != noteService.getStudentService().getSize())
            loadStudents();
        if (homeworkList.size() != noteService.getHomeworkService().getSize())
            loadHomeworks();
    }

    public void searchS() {
        if (searchStudent.getText().trim().length() == 0)
        {
            loadStudents();
            return;
        }
        loadStudents(noteService.getStudentService().filterName(searchStudent.getText().trim()));
    }

    public void searchH() {
        if (searchHomework.getText().trim().length() == 0)
        {
            loadHomeworks();
            return;
        }
        loadHomeworks(noteService.getHomeworkService().filterTask(searchHomework.getText().trim()));
    }

    public void handleclear() {
        this.populateList(true);
        clearDetails();
    }
}
