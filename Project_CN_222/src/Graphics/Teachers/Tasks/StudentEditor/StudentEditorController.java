package Graphics.Teachers.Tasks.StudentEditor;

import Domain.Category;
import Domain.Nota;
import Domain.Student;
import Domain.User;
import Exceptions.GuiException;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Graphics.AlertMessage;
import Graphics.MainGui.MainController;
import Graphics.Teachers.Tasks.TaskTableController;
import Services.NoteService;
import Services.StudentService;
import Utilities.ListEvent;
import Utilities.MD5Encrypter;
import Utilities.Observer;
import Utilities.TextFieldFormatter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StudentEditorController implements Observer<Student> {
    private TaskTableController rootController;
    private StudentService service;
    @FXML
    TableView<Student> tableView;

    @FXML
    Button addButton, editButton, updateButton;
    @FXML
    TextField idField, nameField, groupField, emailField, profField, filterField;

    @FXML
    TableColumn deleteColumn;

    @FXML
    ComboBox filterBox;
    ObservableList<Student> model = FXCollections.observableArrayList();

    private int rowsPerPage = 9;
    private int currentPage = 0;
    @FXML private Pagination pagination;
    @FXML
    public void initialize() {

        tableView.setItems(model);
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                if (newValue != null) {
                    loadStudentInFields(newValue);
                    disableButtons();
                    disableFields();
                }
            }
        });
        deleteColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                return new DeleteCell(StudentEditorController.this);
            }
        });


        disableFields();
        disableButtons();
        addButtonsActions();
        restrictNumericFields();
        restrictXMLSafeFields();
        filterField.setDisable(true);
        for(FilterType filter : FilterType.values()) {
            filterBox.getItems().add(filter.toString().toLowerCase());
        }
        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    filterStudents(newValue, filterBox.getSelectionModel().getSelectedItem().toString().toLowerCase());
                    //System.out.println("filterin");
                }
            }
        });

        filterBox.valueProperty().addListener(event -> {
            filterField.setDisable(false);
        });
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, model.size());
        tableView.setItems(FXCollections.observableArrayList(model.subList(fromIndex, toIndex)));
        return new BorderPane(tableView);


    }

    public void disableFields() {
        for (TextField field : Arrays.asList(idField, nameField, groupField, emailField, profField)) {
            field.setDisable(true);
        }
    }

    public void enableFields() {
        for (TextField field : Arrays.asList(idField, nameField, groupField, emailField, profField)) {
            field.setDisable(false);
        }
    }

    public void enableButtons() {
        for (Button button : Arrays.asList(addButton, updateButton)) {
            button.setDisable(false);
        }
    }

    public void disableButtons() {
        for (Button button : Arrays.asList(addButton, updateButton)) {
            button.setDisable(true);
        }
    }

    public void clearFields() {
        for (TextField field : Arrays.asList(idField, nameField, groupField, emailField, profField)) {
            field.setText("");
        }
    }

    private boolean areFieldsDisabled() {
        return idField.isDisabled() && nameField.isDisabled() &&
                groupField.isDisabled() && emailField.isDisabled() &&
                profField.isDisabled();
    }

    private boolean areButtonsDisabled() {
        return updateButton.isDisabled() && addButton.isDisabled();
    }

    private void addButtonsActions() {
        editButton.setOnAction(event -> {
            if (areFieldsDisabled() && areButtonsDisabled()) {
                enableFields();
                enableButtons();
            } else {
                disableFields();
                disableButtons();
                clearFields();
            }
            clearFilter();
        });

        addButton.setOnAction(event -> {
            handleAdd();
            clearFields();
            clearFilter();
        });

        updateButton.setOnAction(event -> {
            handleUpdate();
            clearFields();
            clearFilter();
        });
    }

    private void filterStudents(String value, String style) {
        //System.out.println("value is: " + value + " and style " + style);

        if (value.isEmpty() || style.isEmpty()) {
            model.setAll(service.getAllAsList());
            filterField.getStyleClass().removeAll("bad-field");
            model.setAll(service.getAllAsList());
            refreshPage();
        } else if (style.equals("name")) {
            filterName(value);
        } else if (style.equals("group")) {
            try {
                int grupa = Integer.parseInt(value);
                filterGroup(grupa);
                filterField.getStyleClass().removeAll("bad-field");
            } catch (NumberFormatException e) {
                filterField.getStyleClass().add("bad-field");
            }

        }
        //tableView.setItems(model);
    }

    private void filterName(String value) {
        model.setAll(service.fillterBeginsWith(value));
        refreshPage();
    }

    private void filterGroup(int group) {
        model.setAll(service.fillterGrupa(group));
        refreshPage();
    }

    private void handleAdd() {
        try {
            checkFields();
            Student student = getStudentFromFields();
            service.add(
                    student.getId(),
                    student.getNume(),
                    student.getGrupa(),
                    student.getEmail(),
                    student.getProfesor()
            );
            addNewUser(student);
            refreshPage();
            AlertMessage.show("success", "student success", "student added successfully." + "" +
                    "\nUser account [user: " + student.getEmail() +", pass:0000] has been created", Alert.AlertType.CONFIRMATION);
        } catch (GuiException | ValidationException | RepositoryException |NumberFormatException e) {
            System.out.println(e.getMessage());
            AlertMessage.show("error", "error occured", e.getMessage(), Alert.AlertType.ERROR);
            //e.printStackTrace();
        }
    }


    private class DeleteCell extends TableCell<Student, Boolean> {
        final Button cellButton = new Button("");
        private StudentEditorController controller;

        public DeleteCell(StudentEditorController ctrl) {
            this.controller = ctrl;
            cellButton.getStyleClass().add("deleteButton");
            ImageView imageView = new ImageView("/resources/delete-button.png");
            imageView.setFitWidth(20d);
            imageView.setFitHeight(20d);
            cellButton.setGraphic(imageView);

            cellButton.setOnAction(event -> {
                Student current = DeleteCell.this.getTableView().getItems().get(DeleteCell.this.getIndex());
                controller.getService().remove(current.getId());
                NoteService noteService = rootController.getRootController().getGradeService();
                for (Nota nota : noteService.getAllAsList()) {
                    if (nota.getIdStudent() == current.getId()) {
                        noteService.remove(nota.getId());
                    }
                }
                rootController.getRootController().getRootController().getUsersService().remove(current.getEmail());
                controller.clearFields();
                controller.disableFields();
                controller.disableButtons();
            });

        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }

        }


    }

    public StudentService getService() {
        return service;
    }

    private void refreshPage() {
        int size = model.size();
        int pageCount = size / rowsPerPage;
        if (size * rowsPerPage != model.size()){
            pageCount = pageCount + 1;
        }
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private void clearFilter() {
        filterBox.getSelectionModel().clearSelection();
        filterBox.setPromptText("fgdsgdgas");
        filterBox.setPromptText("filter");
    }

    private void checkFields() {
        if (idField.getText().isEmpty() ||
                nameField.getText().isEmpty() ||
                groupField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                profField.getText().isEmpty()) {
            throw new GuiException("some fields are empty!");
        }
    }

    private void addNewUser(Student student) {
        rootController
                .getRootController()
                .getRootController()
                .getUsersService()
                .add(student.getEmail(), MD5Encrypter.getHash("0000"), Category.STUDENT);

    }

    private void handleUpdate() {
        try {
            checkFields();

            Student student = getStudentFromFields();
            Student oldStudent = service.get(student.getId());
            rootController.getRootController().getRootController().getUsersService().updateUser(oldStudent.getEmail(), student.getEmail());
            service.update(
                    student.getId(),
                    student.getNume(),
                    student.getGrupa(),
                    student.getEmail(),
                    student.getProfesor()
            );

        } catch (ValidationException|RepositoryException|NumberFormatException e) {
            //System.out.println(e.getMessage());
            AlertMessage.show("error", "error occured", e.getMessage(), Alert.AlertType.ERROR);
            //e.printStackTrace();
        }
    }

    private void restrictNumericFields() {
        for (TextField field : Arrays.asList(idField, groupField)) {
            TextFieldFormatter.formatNumericField(field);
        }
    }

    private void restrictXMLSafeFields() {
        for (TextField field : Arrays.asList(nameField, profField)) {
            TextFieldFormatter.formatNameField(field);
        }
        TextFieldFormatter.formatEmailField(emailField);
    }

    private void loadStudentInFields(Student student) {
        idField.setText(String.valueOf(student.getId()));
        nameField.setText(student.getNume());
        groupField.setText(String.valueOf(student.getGrupa()));
        emailField.setText(student.getEmail());
        profField.setText(student.getProfesor());

    }

    private Student getStudentFromFields() {
        return new Student(
                Integer.parseInt(idField.getText()),
                nameField.getText(),
                Integer.parseInt(groupField.getText()),
                emailField.getText(),
                profField.getText()
        );
    }

    public void setRootController(TaskTableController rootController) {
        this.rootController = rootController;
    }

    public void setService(StudentService service) {
        this.service = service;
        model.setAll(service.getAllAsList());
        service.addObserver(this);
        setUpPagination();

    }

    private void setUpPagination() {

//        pagination.setPageCount(service.getAllAsList().size() / rowsPerPage + 1);
//        //System.out.println("pages " + (service.getAllAsList().size() / rowsPerPage + 1));
//        pagination.setCurrentPageIndex(0);
//        pagination.setPageFactory(this::createPage);
//
//        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                model.setAll(service.getAllAsList().subList(newValue.intValue(), (newValue.intValue() + rowsPerPage)));
//            }
//        });
        refreshPage();
    }


    @Override
    public void notifyEvent(ListEvent<Student> x) {
        model.setAll(service.getAllAsList());
        refreshPage();
    }

    private enum FilterType {
        NAME, GROUP
    }
}
