package UI.GUI;

import Domain.Student;
import Domain.TemaLaborator;
import Service.Filter;
import Service.NotaService;
import Service.TemeService;
import Utils.ListEvent;
import Utils.Observer;
import Validator.ValidationException;
import com.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerTeme implements Observer<TemaLaborator> {

    @FXML
    TableView tableViewTeme;
    @FXML
    TableColumn<TemaLaborator, String> tableColumnNrTema;
    @FXML
    TableColumn<TemaLaborator, String> tableColumnCerinta;
    @FXML
    TableColumn<TemaLaborator, String> tableColumnDeadline;
    @FXML
    JFXButton btnBack;
    @FXML
    JFXTextField textFieldNrTema;
    @FXML
    JFXTextField textFieldCerinta;
    @FXML
    JFXTextField textFieldDeadline;
    @FXML
    JFXButton btnAdd;
    @FXML
    JFXButton btnUpdate;
    @FXML
    JFXTextField textFieldUpNrTema;
    @FXML
    JFXTextField textFieldUpCerinta;
    @FXML
    JFXTextField textFieldUpDeadline;
    @FXML
    JFXTextField textFieldNrTemaFil;
    @FXML
    JFXTextField textFieldCerintaFil;
    @FXML
    JFXComboBox comboBoxDeadline;
    @FXML
    JFXButton btnDelete;
    @FXML
    JFXButton btnPrevious;
    @FXML
    JFXButton btnForward;
    @FXML
    JFXComboBox comboBoxItems;
    @FXML
    JFXButton btnSignOut;

    IntegerProperty index = new SimpleIntegerProperty(3);
    IntegerProperty page = new SimpleIntegerProperty(0);

    ObservableList<String> deadline =
            FXCollections.observableArrayList(
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14"
            );

    ObservableList<String> items =
            FXCollections.observableArrayList(
                    "3",
                    "5",
                    "10"
            );

    TemeService service;
    NotaService notaService;
    ObservableList<TemaLaborator> model;

    public void setTableColumns(){
        tableColumnCerinta.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("cerinta"));
        tableColumnDeadline.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("deadline"));
        tableColumnNrTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("numarTema"));
    }

    public void setButtons(){
        comboBoxItems.setItems(items);
        btnPrevious.setDisable(true);
        comboBoxItems.getSelectionModel().selectFirst();
        comboBoxDeadline.setItems(deadline);
        comboBoxDeadline.getSelectionModel().selectFirst();
        btnBack.getStyleClass().add("button_home");

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = new Scene(ControllerLogin.initView(), 800, 500);
                scene.getStylesheets().add("Resources/CSS/stylesheets.css");
                Main.st.setScene(scene);            }
        });
        comboBoxItems.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                index.set(Integer.valueOf(newValue.toString()));
                page.setValue(0);
                if(index.getValue() + page.getValue() >= service.getAllTeme().size())
                    btnForward.setDisable(true);
                else
                    btnForward.setDisable(false);
                loadData();
            }
        });
        page.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0)
                    btnPrevious.setDisable(true);
                else
                    btnPrevious.setDisable(false);
                if(newValue.intValue() >= service.getAllTeme().size() - index.getValue())
                    btnForward.setDisable(true);
                else
                    btnForward.setDisable(false);

            }
        });
        btnForward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                page.setValue(index.getValue() + page.getValue());
                loadData();
            }
        });
        btnPrevious.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                page.setValue(page.getValue() - index.getValue());
                loadData();
            }
        });
        btnSignOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.logOut();
            }
        });
    }

    public void initialize(){
        setTableColumns();
        setButtons();

    }

    public void filter(){
        Predicate<TemaLaborator> predFinal = (temaLaborator -> true);
        boolean ok1 = false, ok2 = false, ok3 = false;
        if(!textFieldCerintaFil.getText().equals(""))
            predFinal = predFinal.and(Filter.contineCerinta( textFieldCerintaFil.getText()));
        else
            ok1 = true;
        if(!comboBoxDeadline.getSelectionModel().getSelectedItem().toString().equals("0"))
            predFinal = predFinal.and(Filter.areDeadlineulMaiMic(Integer.valueOf(comboBoxDeadline.getSelectionModel().getSelectedItem().toString())));
        else
            ok2 = true;
        if(!textFieldNrTemaFil.getText().equals(""))
                predFinal = predFinal.and(Filter.esteMaiMicaNrTema(Integer.valueOf(textFieldNrTemaFil.getText())));
        else
            ok3 = true;
        if(!(ok1 == true && ok2 == true && ok3 == true)) {
            List<TemaLaborator> filtered = this.service.getAllTeme().stream().filter(predFinal).collect(Collectors.toList());
            model.setAll(FXCollections.observableArrayList(filtered));
            btnForward.setDisable(true);
            btnPrevious.setDisable(true);
            comboBoxItems.setDisable(true);
        }
        else{
            loadData();
            btnPrevious.setDisable(false);
            btnForward.setDisable(false);
            comboBoxItems.setDisable(false);
        }
    }

    public void addTema(){
        try{
            if(this.service.add(new TemaLaborator(1, textFieldCerinta.getText(), Integer.valueOf(textFieldDeadline.getText()))) != null)
                    showMessage(Alert.AlertType.WARNING,"Warning","Tema exista deja.");
            else
                    showMessage(Alert.AlertType.INFORMATION, "Salvare", "Tema a fost salvata cu succes!");
        }catch (ValidationException e){
            showErrorMessage(e.getMessage());
        }
    }

    public void updateTema(){
        try{
            service.update(new TemaLaborator(Integer.valueOf(textFieldUpNrTema.getText()), textFieldUpCerinta.getText(), Integer.valueOf(textFieldUpDeadline.getText())));
            showMessage(Alert.AlertType.INFORMATION, "Update", "Tema fost updatata cu succes!");
        }catch (ValidationException e){
            showErrorMessage(e.getMessage());
        }
    }

    public void setService(TemeService service, NotaService notaService){
        this.notaService = notaService;
        this.service = service;
        this.model = FXCollections.observableArrayList(this.service.getBetween(index.getValue(), page.getValue()));
        tableViewTeme.setItems(model);
    }

    public void deleteTema(){
        TemaLaborator tema = (TemaLaborator) tableViewTeme.getSelectionModel().getSelectedItem();
        if(notaService.findTema(tema.getNumarTema()) == false)
            this.service.delete(tema.getId());
        else
            showErrorMessage("Tema nu poate fi stearsa deoarece are asignata o nota.");
    }
    public void loadTema(){
        TemaLaborator tema = (TemaLaborator) tableViewTeme.getSelectionModel().getSelectedItem();
        if(tema == null)
            return;
        else{
            textFieldUpDeadline.setText(String.valueOf(tema.getDeadline()));
            textFieldUpCerinta.setText(String.valueOf(tema.getCerinta()));
            textFieldUpNrTema.setText(String.valueOf(tema.getNumarTema()));
        }
    }

    public void loadData(){
        if(index.getValue() + page.getValue() >= service.getAllTeme().size())
            btnForward.setDisable(true);
        else
            btnForward.setDisable(false);
        model.setAll(FXCollections.observableArrayList(this.service.getBetween(index.getValue(), page.getValue())));
    }

    @Override
    public void notifyEvent(ListEvent<TemaLaborator> e) {
        //model.setAll(StreamSupport.stream(e.getList().spliterator(),false)
        //        .collect(Collectors.toList()));
       // model.setAll(FXCollections.observableArrayList(this.service.getAllTeme()));
        loadData();
    }

    static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}
