package StudentController;


import domain.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.ServiceLaborator;
import service.ServiceStudent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class StudentController_FXML implements observer.Observer<Student> {

    private ObservableList<Student> model;
    private ServiceStudent service;

    @FXML
    public TableView tableView;

    @FXML
    public TableColumn columnID;

    @FXML
    public TableColumn columnNume;

    @FXML
    public TableColumn columnGrupa;

    @FXML
    public TableColumn columnEmail;

    @FXML
    public TableColumn columnProfesor;

    @FXML
    public Pagination paginationBar;

    @FXML
    public void initialize() throws Exception {

        columnID.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        columnNume.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        columnGrupa.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        columnProfesor.setCellValueFactory(new PropertyValueFactory<Student, String>("indrumator"));

        this.tableView.setItems(model);

        ArrayList<String> l = new ArrayList<>();
        l.add("Numar matricol");
        l.add("Nume");
        l.add("Grupa");
        l.add("Email");
        l.add("Prof. Indrumator");
    }

    public void setService(ServiceStudent service) throws Exception {
        this.service = service;
        refreshTable(0, this.service.returnall());
        this.tableView.setItems(model);
        Integer nrOfPage = this.service.returnall().size() / 10;
        if (this.service.returnall().size() % 10 != 0) {
            nrOfPage++;
        }
        paginationBar.setPageCount(nrOfPage);
        paginationBar.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {

            try {
                refreshTable(paginationBar.getCurrentPageIndex(), this.service.returnall());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }


    @Override
    public void notifyEvent(observer.ListEvent<Student> e) {
        try {
            this.refreshTable(paginationBar.getCurrentPageIndex(),this.service.returnall());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            model.setAll(this.service.returnall());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    public void AddStudentHandler(ActionEvent ev) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/studentView/StudentAddView_FXML.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("TStudent");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            StudentAddController_FXML addController = loader.getController();
            addController.setService(this.service, dialogStage);
            dialogStage.show();


            this.refreshTable(0, this.service.returnall());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void RemoveSelectedStudentHandler(ActionEvent ev) {
        if (this.tableView.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Nu s-a selectat niciun student pentru stergere");

            alert.showAndWait();
        } else {
            try {
                Student student = (Student) this.tableView.getSelectionModel().getSelectedItem();
                this.service.delete(student.getId());
                this.refreshTable(paginationBar.getCurrentPageIndex(), this.service.returnall());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succes");
                alert.setContentText("Student eliminat cu succes");
                alert.setContentText("Student eliminat cu succes");
                this.refreshTable(paginationBar.getCurrentPageIndex(), this.service.returnall());


                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Eroare");
                alert.setContentText("Student imposibil de sters!");
                alert.showAndWait();

            }

        }

    }

    public void refreshTable(Integer pageIndex, List<Student> list) throws Exception {
        Integer primulIndexDinPaginaCurenta = (pageIndex) * 10;
        Integer ultimulIndexDinPaginaCurenta = (pageIndex + 1) * 10;
        if (ultimulIndexDinPaginaCurenta > list.size()) {
            ultimulIndexDinPaginaCurenta = list.size();
        }
        if (primulIndexDinPaginaCurenta < 0) {
            primulIndexDinPaginaCurenta = 0;
        }

        Integer nrOfPage = this.service.returnall().size() / 10;
        if (this.service.returnall().size() % 10 != 0) {
            nrOfPage++;
        }
        this.paginationBar.setPageCount(nrOfPage);
        model = FXCollections.observableArrayList(list.subList(primulIndexDinPaginaCurenta, ultimulIndexDinPaginaCurenta));
        this.tableView.setItems(model);
        this.tableView.refresh();
    }

    @FXML
    public void UpdateStudentHandler(ActionEvent ev) throws Exception {
        if (this.tableView.getSelectionModel().getSelectedItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Nu s-a selectat niciun student pentru modificare");

            alert.showAndWait();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/studentView/StudentUpdateView_FXML.fxml"));
                AnchorPane root = (AnchorPane) loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Modificare student");
                dialogStage.initModality(Modality.WINDOW_MODAL);

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                StudentUpdateController_FXML updateController = loader.getController();
                Student student = (Student) this.tableView.getSelectionModel().getSelectedItem();
                updateController.setService(this.service, dialogStage, student);

                dialogStage.show();
                this.refreshTable(paginationBar.getCurrentPageIndex(),this.service.returnall());

            } catch (IOException ex) {//DE RETINUT CUM FACI ALERTE CU ERORILE TALE
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Eroare");
                alert.setContentText("Eroare neasteptata: " + ex.getMessage());

                alert.showAndWait();

            }
        }
    }

    @FXML
    public void FilterStudentHandler(ActionEvent ev) {
        if (this.tableView.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Nu exista niciun student memorat pentru a putea face filtrarea");

            alert.showAndWait();


        } else
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/studentView/StudentFilterView_FXML.fxml"));
                AnchorPane root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Filtrare");
                dialogStage.initModality(Modality.WINDOW_MODAL);

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                StudentFilterController_FXML filterController = loader.getController();
                filterController.setService(this.service, dialogStage, this.model);

                dialogStage.show();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Eroare");
                alert.setContentText("Eroare neasteptata: " + ex.getMessage());

                alert.showAndWait();
            }
    }

    @FXML
    public void ReloadTableHandler(ActionEvent ev) throws Exception {
        this.model.setAll(this.service.returnall());
    }

}
