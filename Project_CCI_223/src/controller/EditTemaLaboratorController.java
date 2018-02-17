package controller;

import domain.TemaLaborator;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import service.Service;
import view_FXML.AlertMessage;

import java.util.Optional;

public class EditTemaLaboratorController {
    Stage dialogStage;
    TemaLaborator temaLaborator;
    Service service;
    Scene scene;

    ObservableList<Integer> listDeadline= FXCollections.observableArrayList();

    @FXML
    private TextField textFieldNrTema;
    @FXML
    private TextArea textAreaCerinta;
    @FXML
    private ChoiceBox choiceBoxDeadline;

    @FXML
    public void initialize(){
        initializareListDeadline();

        choiceBoxDeadline.setItems(listDeadline);
    }

    private void initializareListDeadline() {
        Integer sapt=service.getSaptamana_curenta();
        while (sapt<=14){
            if (!listDeadline.contains(sapt))listDeadline.add(sapt);
            sapt=sapt+1;
        }
    }

    public EditTemaLaboratorController() {
    }

    public void setService(Service service,TemaLaborator temaLaborator,Stage dialogStage){
        this.service=service;
        this.dialogStage=dialogStage;
        this.temaLaborator=temaLaborator;
        if (temaLaborator!=null){
            listDeadline.clear();
            listDeadline.add(temaLaborator.getDeadline());
            initializareListDeadline();
            choiceBoxDeadline.setItems(listDeadline);
            setFields();
        }
    }

    public void setScene(Scene scene) {
        this.scene=scene;

        scene.setOnKeyPressed(event -> {
            if (event.getCode()== KeyCode.S && event.isControlDown()){
                handleSave();
            }
        });
    }

    private void setFields() {
        textAreaCerinta.setText(temaLaborator.getCerinta());
        textFieldNrTema.setText(new Integer(temaLaborator.getNr_tema_de_laborator()).toString());
        choiceBoxDeadline.setValue(temaLaborator.getDeadline());
        textFieldNrTema.setDisable(true);
        textAreaCerinta.setDisable(true);
    }

    @FXML
    public void handleSave(){
        if (temaLaborator==null){
            addTemaLaborator();
        }
        else updateTemaLaborator();
    }

    private void updateTemaLaborator()  {
        try {
            Integer deadlineNou=(Integer) choiceBoxDeadline.getValue();
            service.updateTemaLabTermenPredare(temaLaborator.getId(),deadlineNou);
            if ((service.getSaptamana_curenta()<temaLaborator.getDeadline())&&(deadlineNou>temaLaborator.getDeadline())){
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.INFORMATION,"Modificare reusita","Termenul de predare a fost modificat cu succes!");
                dialogStage.close();
            }
            else{
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.WARNING,"Atentie!!!","Modificarea nu a fost efectuata, termenul de predare e invalid");
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void addTemaLaborator() {
        try{
            Integer nrTema=Integer.parseInt(textFieldNrTema.getText());
            if (nrTema<=0) throw new NumberFormatException();
            Integer deadline=(Integer) choiceBoxDeadline.getValue();
            if (deadline==null){
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.WARNING,"Atentie","Va rugam selectati un deadline!");
                return;
            }
            TemaLaborator temaLaborator=new TemaLaborator(nrTema,textAreaCerinta.getText(),deadline);
            Optional<TemaLaborator> aux=service.addTemaLab(temaLaborator);
            if (!aux.isPresent()){
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.INFORMATION,"Salvare reusita","Tema a fost adaugata cu succes!");
                dialogStage.close();
            }
            else{
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.WARNING,"Salvare nereusita","Tema nu a fost adaugata, mai exista o tema cu acelasi ID");
            }

        }catch (NumberFormatException e){
            AlertMessage message=new AlertMessage();
            message.showMessage(dialogStage, Alert.AlertType.ERROR,"Eroare!!!","Numarul temei trebuie sa fie un numar natural strict pozitiv");
        } catch (ValidationException e) {
            AlertMessage message=new AlertMessage();
            message.showMessage(dialogStage, Alert.AlertType.ERROR,"Eroare!!!",e.toString());
        }
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}
