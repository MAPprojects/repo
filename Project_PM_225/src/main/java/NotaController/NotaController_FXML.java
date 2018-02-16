package NotaController;


import domain.Nota;
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
import service.ServiceNota;
import util.CurrentWeek;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotaController_FXML implements Observer<Nota> {

    private ObservableList<Nota> model;
    private ServiceNota service;

    @FXML
    public TableView tableView;

    @FXML
    public TableColumn columnIdStudent;

    @FXML
    public TableColumn columnValoare;

    @FXML
    public TableColumn columnNrTema;

    @FXML
    public Pagination paginationBar;

    @FXML
    public Button incSaptamana;

    @FXML
    public Label nrSaptamana;



    @FXML
    public void initialize() {
        columnIdStudent.setCellValueFactory(new PropertyValueFactory<Nota, String>("idStudent"));
        columnValoare.setCellValueFactory(new PropertyValueFactory<Nota, String>("valoare"));
        columnNrTema.setCellValueFactory(new PropertyValueFactory<Nota, String>("nrTema"));

        this.tableView.setItems(model);

        ArrayList<String> l = new ArrayList<>();
        l.add("Id student");
        l.add("Valoare");
        l.add("Numar tema");
        nrSaptamana.setText(String.valueOf(CurrentWeek.CURRENT_WEEK));

    }

    public void setService(ServiceNota service) throws Exception {
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
    public void notifyEvent(observer.ListEvent<Nota> e) {
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
    public void AddNotaHandler(ActionEvent ev) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/notaView/NotaAddView_FXML.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Adaugare nota");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            NotaAddController_FXML addController = loader.getController();
            addController.setService(this.service, dialogStage);
            this.refreshTable(paginationBar.getCurrentPageIndex(), this.service.returnall());
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void refreshTable(Integer pageIndex, List<Nota> list) throws Exception {
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
    public void UpdateNotaHandler(ActionEvent ev) throws Exception {
        if (this.tableView.getSelectionModel().getSelectedItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Nu s-a selectat niciun nota pentru modificare");

            alert.showAndWait();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/notaView/NotaUpdateView_FXML.fxml"));
                AnchorPane root = (AnchorPane) loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Modificare nota");
                dialogStage.initModality(Modality.WINDOW_MODAL);

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                NotaUpdateController_FXML updateController = loader.getController();
                Nota nota = (Nota) this.tableView.getSelectionModel().getSelectedItem();
                updateController.setService(this.service, dialogStage, nota);


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
    public void FilterNotaHandler(ActionEvent ev) {
        if (this.tableView.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Nu exista niciun nota memorat pentru a putea face filtrarea");

            alert.showAndWait();


        } else
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/notaView/NotaFilterView_FXML.fxml"));
                AnchorPane root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Filtrare");
                dialogStage.initModality(Modality.WINDOW_MODAL);

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                NotaFilterController_FXML filterController = loader.getController();
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

    public void incrementeazaNumarulSaptamanii(ActionEvent actionEvent) {
        if (Integer.parseInt(nrSaptamana.getText()) == 14) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setContentText("Numarul maxim de saptamani este 14");

            alert.showAndWait();
        } else {
            CurrentWeek.incWeek();
            nrSaptamana.setText(String.valueOf(CurrentWeek.CURRENT_WEEK));
        }
    }

}
