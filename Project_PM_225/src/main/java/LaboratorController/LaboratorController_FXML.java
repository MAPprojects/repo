package LaboratorController;


import domain.Laborator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import observer.Observer;
import service.ServiceLaborator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LaboratorController_FXML implements Observer<Laborator> {

    private ObservableList<Laborator> model;
    private ServiceLaborator service;

    @FXML
    public TableView tableView;

    @FXML
    public TableColumn columnNrTema;

    @FXML
    public TableColumn columnCerinta;

    @FXML
    public TableColumn columnDeadline;

    @FXML
    public Pagination paginationBar;


    @FXML
    public void initialize() {
        columnNrTema.setCellValueFactory(new PropertyValueFactory<Laborator, String>("nrTema"));
        columnCerinta.setCellValueFactory(new PropertyValueFactory<Laborator, String>("cerinta"));
        columnDeadline.setCellValueFactory(new PropertyValueFactory<Laborator, String>("deadline"));

        this.tableView.setItems(model);

        ArrayList<String> l = new ArrayList<>();
        l.add("Numar tema");
        l.add("Cerinta");
        l.add("Deadline");
    }

    public void setService(ServiceLaborator service) throws Exception {
        this.service = service;
        model = FXCollections.observableArrayList(this.service.returnall());
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
    public void notifyEvent(observer.ListEvent<Laborator> e) {
        try {
            this.refreshTable(paginationBar.getCurrentPageIndex(), this.service.returnall());
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
    public void AddLaboratorHandler(ActionEvent ev) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/laboratorView/LaboratorAddView_FXML.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Adaugare laborator");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            LaboratorAddController_FXML addController = loader.getController();
            addController.setService(this.service, dialogStage);
            dialogStage.show();
            this.refreshTable(paginationBar.getCurrentPageIndex(), this.service.returnall());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void RemoveSelectedLaboratorHandler(ActionEvent ev) {
        if (this.tableView.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Nu s-a selectat niciun laborator pentru stergere");

            alert.showAndWait();
        } else {
            try {
                Laborator laborator = (Laborator) this.tableView.getSelectionModel().getSelectedItem();
                this.service.delete(laborator.getNrTema());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succes");
                alert.setContentText("Laborator eliminat cu succes");

                alert.showAndWait();
                this.refreshTable(paginationBar.getCurrentPageIndex(), this.service.returnall());

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Eroare");
                alert.setContentText("Laborator imposibil de sters!");
                alert.showAndWait();

            }

        }

    }

    public void refreshTable(Integer pageIndex, List<Laborator> list) throws Exception {
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
    public void UpdateLaboratorHandler(ActionEvent ev) throws Exception {
        if (this.tableView.getSelectionModel().getSelectedItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Nu s-a selectat niciun laborator pentru modificare");

            alert.showAndWait();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/laboratorView/LaboratorUpdateView_FXML.fxml"));
                AnchorPane root = (AnchorPane) loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Modificare laborator");
                dialogStage.initModality(Modality.WINDOW_MODAL);

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                LaboratorUpdateController_FXML updateController = loader.getController();
                Laborator laborator = (Laborator) this.tableView.getSelectionModel().getSelectedItem();
                updateController.setService(this.service, dialogStage, laborator);

//                this.refreshTable(FXCollections.observableArrayList(this.service.returnall()));

                dialogStage.show();
                this.refreshTable(paginationBar.getCurrentPageIndex(), this.service.returnall());


            } catch (IOException ex) {//DE RETINUT CUM FACI ALERTE CU ERORILE TALE
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Eroare");
                alert.setContentText("Eroare neasteptata: " + ex.getMessage());

                alert.showAndWait();

            }
        }
    }

    @FXML
    public void FilterLaboratorHandler(ActionEvent ev) {
        if (this.tableView.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Nu exista niciun laborator memorat pentru a putea face filtrarea");

            alert.showAndWait();


        } else
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/laboratorView/LaboratorFilterView_FXML.fxml"));
                AnchorPane root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Filtrare");
                dialogStage.initModality(Modality.WINDOW_MODAL);

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                LaboratorFilterController_FXML filterController = loader.getController();
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
