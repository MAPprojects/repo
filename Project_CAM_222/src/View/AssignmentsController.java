package View;

import Domain.Tema;
import Domain.ValidationException;
import Repository.RepositoryException;
import Service.Service;
import Utils.HyperlinkObserver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.Objects;
import java.util.function.Predicate;

public class AssignmentsController implements HyperlinkObserver {
    private Service service;
    private int rowsPerPage = 9;
    private Predicate<Tema> filter = (x -> true);
    private String accountName;
    private String accountRole;
    private String accountPicturePath;
    private RootController rootController;

    @FXML
    private TableView<Tema> assignmentsTableView;
    @FXML
    private Pagination assignmentsPagination;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField descriereTextField;
    @FXML
    private TextField deadlineTextField;
    @FXML
    private CheckBox idCheckBox;
    @FXML
    private CheckBox descriereCheckBox;
    @FXML
    private CheckBox deadlineCheckBox;
    @FXML
    private Button addAssignmentButton;
    @FXML
    private Button modifyDeadlineButton;
    @FXML
    private Button updateAssignmentButton;
    @FXML
    private Button deleteAssignmentButton;
    @FXML
    private Button clearWorkspaceButton;

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }

    public void setAccountPicturePath(String accountPicturePath) {
        this.accountPicturePath = accountPicturePath;
    }

    private void createTable() {
        TableColumn<Tema, Integer> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        column1.setMinWidth(60);
        column1.setMaxWidth(60);

        TableColumn<Tema, String> column2 = new TableColumn<>("Description");
        column2.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        column2.setMinWidth(420);
        column2.setMaxWidth(420);

        TableColumn<Tema, Integer> column3 = new TableColumn<>("Deadline");
        column3.setCellValueFactory(new PropertyValueFactory<>("deadlineString"));
        column3.setMinWidth(120);
        column3.setMaxWidth(120);

        if (!accountRole.equals("Standard User")) {
            TableColumn column4 = new TableColumn("Grades");
            column4.setCellFactory(tableCell -> new ViewGradesCell<>("View", this));
            column4.setMinWidth(100);
            column4.setMaxWidth(100);
            assignmentsTableView.getColumns().addAll(column4);
        }
        else {
            assignmentsTableView.setMinWidth(assignmentsTableView.getMinWidth() - 100);
            assignmentsTableView.setMaxWidth(assignmentsTableView.getMaxWidth() - 100);
        }

        assignmentsTableView.getColumns().addAll(column1,column2,column3);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, service.getTemeCount());
        assignmentsTableView.setItems(FXCollections.observableArrayList(service.assignmentsSublist(fromIndex, toIndex, filter)));
        return new Pane();
    }

    private void reloadTableView() {
        if (service.getTemeCount(filter) % rowsPerPage == 0)
            assignmentsPagination.setPageCount(service.getTemeCount(filter) / rowsPerPage);
        else
            assignmentsPagination.setPageCount(service.getTemeCount(filter) / rowsPerPage + 1);
        if (service.getTemeCount(filter) == 0)
            assignmentsPagination.setPageCount(1);
        //assignmentsPagination.setCurrentPageIndex(0);
        createPage(assignmentsPagination.getCurrentPageIndex());
    }

    public void setService(Service service) {
        this.service = service;

        createTable();
        reloadTableView();
        assignmentsPagination.setPageFactory(this::createPage);

        assignmentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Tema tema = assignmentsTableView.getSelectionModel().getSelectedItem();
                        populateFields(tema);
                    }
                });
            }
        });

        idTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        descriereTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        deadlineTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());

        if (accountRole.equals("Standard User")) {
            addAssignmentButton.setDisable(true);
            modifyDeadlineButton.setDisable(true);
            updateAssignmentButton.setDisable(true);
            deleteAssignmentButton.setDisable(true);
            clearWorkspaceButton.setDisable(false);
        }
        else if (accountRole.equals("Moderator")) {
            addAssignmentButton.setDisable(true);
            modifyDeadlineButton.setDisable(false);
            updateAssignmentButton.setDisable(true);
            deleteAssignmentButton.setDisable(true);
            clearWorkspaceButton.setDisable(false);
        }
        else if (accountRole.equals("Administrator")) {
            addAssignmentButton.setDisable(false);
            modifyDeadlineButton.setDisable(false);
            updateAssignmentButton.setDisable(false);
            deleteAssignmentButton.setDisable(false);
            clearWorkspaceButton.setDisable(false);
        }
    }

    public void filtreaza() {
        Predicate<Tema> predicate = (x -> true);
        if (idCheckBox.isSelected()) {
            Predicate<Tema> aux = (x -> (x.getId().toString().equals(idTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (descriereCheckBox.isSelected()) {
            Predicate<Tema> aux = (x -> (x.getDescriere().contains(descriereTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (deadlineCheckBox.isSelected()) {
            Predicate<Tema> aux = (x -> (x.getDeadline().toString().equals(deadlineTextField.getText())));
            predicate = predicate.and(aux);
        }

        filter = predicate;
        reloadTableView();
    }

    @Override
    public void onHyperlinkClick(int tableRowIndex) {
        rootController.showGradesOfAssignment(assignmentsTableView.getItems().get(tableRowIndex));
    }

    private Tema getTemaFromFields() {
        Integer id = Integer.parseInt(idTextField.getText());
        String descriere = descriereTextField.getText();
        Integer deadline = Integer.parseInt(deadlineTextField.getText());

        return new Tema(id, descriere, deadline);
    }

    public void populateFields(Tema tema) {
        idTextField.setText(tema.getId().toString());
        descriereTextField.setText(tema.getDescriere());
        deadlineTextField.setText(tema.getDeadline().toString());
    }

    public void clearWorkspace(ActionEvent actionEvent) {
        idTextField.clear();
        descriereTextField.clear();
        deadlineTextField.clear();
        idCheckBox.setSelected(false);
        descriereCheckBox.setSelected(false);
        deadlineCheckBox.setSelected(false);
        filtreaza();
    }

    public void clearTableViewSelection(KeyEvent keyEvent) {
        assignmentsTableView.getSelectionModel().clearSelection();
    }

    public void addAssignment(ActionEvent actionEvent) {
        if (Objects.equals(idTextField.getText(), "") ||
                Objects.equals(descriereTextField.getText(), "") ||
                Objects.equals(deadlineTextField.getText(), ""))
            LoginController.showErrorMessage("The Text Fields must not be empty!");
        else {
            try {
                service.addTema(getTemaFromFields());
                reloadTableView();
                LoginController.showMessage(Alert.AlertType.INFORMATION, "Add", "The assignment was successfully added!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The ID and Deadline fields must contain natural numbers!");
            }
        }
    }

    public void updateAssignment(ActionEvent actionEvent) {
        if (Objects.equals(idTextField.getText(), "") ||
                Objects.equals(descriereTextField.getText(), "") ||
                Objects.equals(deadlineTextField.getText(), ""))
            LoginController.showErrorMessage("The Text Fields must not be empty!");
        else {
            try {
                service.modifyTema(getTemaFromFields());
                reloadTableView();
                LoginController.showMessage(Alert.AlertType.INFORMATION, "Modify", "The assignment was successfully modified!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The ID and Deadline fields must contain natural numbers!");
            }
        }
    }

    public void modifyDeadline(ActionEvent actionEvent) {
        if (Objects.equals(idTextField.getText(), "") ||
                Objects.equals(deadlineTextField.getText(), ""))
            LoginController.showErrorMessage("The ID and Deadline Text Fields must not be empty!");
        else {
            try {
                service.modificareDeadline(Integer.parseInt(idTextField.getText()), Integer.parseInt(deadlineTextField.getText()));
                reloadTableView();
                LoginController.showMessage(Alert.AlertType.INFORMATION, "Extend", "The deadline was successfully extended!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The ID and Deadline fields must contain natural numbers!");
            }
        }

    }

    public void deleteAssignment(ActionEvent actionEvent) {
        if (Objects.equals(idTextField.getText(), "")) {
            LoginController.showErrorMessage("The ID Text Field must not be empty!");
        }
        else {
            try {
                Integer id = Integer.parseInt(idTextField.getText());
                service.deleteTema(id);
                reloadTableView();
                LoginController.showMessage(Alert.AlertType.INFORMATION, "Delete", "The assignment was successfully deleted!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The ID field must contain a natural number!");
            }
        }
    }

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }
}
