package controller;

import domain.Nota;
import domain.Student;
import domain.TemaLaborator;
import domain.User;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import sablonObserver.Observable;
import sablonObserver.Observer;
import service.Service;
import view_FXML.AlertMessage;

import java.util.Optional;

public class EditNotaController{
    Service service;
    Nota nota;
    Stage dialogStage;
    Scene scene;

    @FXML
    private TextField textFieldValoare;
    @FXML
    private ChoiceBox choiceBoxIdStudent;
    @FXML
    private ChoiceBox choiceBoxNrTema;
    @FXML
    private TextArea textAreaCerinta;
    @FXML
    private TextField textFieldDeadline;
    @FXML
    private TextField textFieldGrupa;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldProfesor;
    @FXML
    private TextField textFieldNume;
    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public EditNotaController() {
    }

    public void setService(Service service,Stage stage,Nota nota){
        this.service=service;
        this.nota=nota;
        this.dialogStage=stage;
        initComboBoxes();
        if (nota!=null){
            setFields();
            setFieldsDetalii(nota);
        }
    }

    private void setFieldsDetalii(Nota nota) {
        try {
            Student student=service.getStudent(nota.getIdStudent()).get();
            TemaLaborator temaLaborator=service.getTemaLab(nota.getNrTema()).get();
            setFieldsTema(temaLaborator);
            setFieldsStudent(student);

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setFieldsStudent(Student student) {
        textFieldEmail.setText(student.getEmail());
        textFieldGrupa.setText(String.valueOf(student.getGrupa()));
        textFieldNume.setText(student.getNume());
        textFieldProfesor.setText(student.getCadru_didactic_indrumator_de_laborator());
    }

    private void setFieldsTema(TemaLaborator temaLaborator) {
        textAreaCerinta.setText(temaLaborator.getCerinta());
        textFieldDeadline.setText(String.valueOf(temaLaborator.getDeadline()));
    }

    private void initComboBoxes() {
        ObservableList<Integer> listNrTeme= FXCollections.observableArrayList();
        ObservableList<Integer> listIdStudenti=FXCollections.observableArrayList();

        if (nota!=null){
            listIdStudenti.add(nota.getIdStudent());
            listNrTeme.add(nota.getNrTema());
        }
        else{
            service.getStudentiForProfesor(currentUser.getNume()).forEach((x)->listIdStudenti.add(x.getId()));
            service.getListTemeLaborator().forEach((x)->listNrTeme.add(x.getId()));
        }

        choiceBoxIdStudent.getItems().setAll(listIdStudenti);
        choiceBoxNrTema.getItems().setAll(listNrTeme);
    }

    private void setFields() {
        choiceBoxIdStudent.setValue(nota.getIdStudent());
        choiceBoxNrTema.setValue(nota.getNrTema());
        textFieldValoare.setText(nota.getValoare().toString());
        choiceBoxNrTema.setDisable(true);
        choiceBoxIdStudent.setDisable(true);
    }

    @FXML
    public void initialize(){
        choiceBoxNrTema.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Integer value=(Integer) choiceBoxNrTema.getValue();
                try {
                    TemaLaborator tema=service.getTemaLab((Integer) value).get();
                    setFieldsTema(tema);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

        choiceBoxIdStudent.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Integer value=(Integer) choiceBoxIdStudent.getValue();
                try{
                    Student student=service.getStudent((Integer) value).get();
                    setFieldsStudent(student);

                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void handleSave(ActionEvent event){
        if (nota==null) saveNota();
        else updateNota();
    }

    private void updateNota() {
        Nota notaNoua=new Nota(nota.getIdNota(),nota.getIdStudent(),nota.getNrTema(),Float.parseFloat(textFieldValoare.getText()));
        try {
            Nota aux=service.modificareNota(notaNoua);
            if (aux==null){
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.INFORMATION,"Modificare efectuata","Nota a fost modificata cu succes!");
                dialogStage.close();
            }
            else {
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.INFORMATION,"Modificare neefectuata","Nota nu a putut fi modificata. " +
                        "Nota recalculata este mai mica decat cea initiala.");
            }
        } catch (ValidationException e) {
            AlertMessage message=new AlertMessage();
            message.showMessage(dialogStage, Alert.AlertType.ERROR,"Eroare",e.toString());
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveNota() {
        try {
            int idSt=(int) choiceBoxIdStudent.getValue();
            int nrteme=(int) choiceBoxNrTema.getValue();
            Optional<Nota> aux=service.addNota(idSt,nrteme,Float.parseFloat(textFieldValoare.getText()));
            if (!aux.isPresent()){
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.INFORMATION,"Salvare efectuata","Nota a fost salvata cu succes!");
                dialogStage.close();
            }
            else {
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.INFORMATION,"Salvare neefctuata","Nota nu a fost salvata, aceluiasi student i s-a mai acordat o nota la aceeasi tema de laborator");
            }

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            AlertMessage message=new AlertMessage();
            message.showMessage(dialogStage, Alert.AlertType.ERROR,"Eroare",e.toString());
        }catch (NullPointerException e){
            AlertMessage message=new AlertMessage();
            message.showMessage(dialogStage, Alert.AlertType.ERROR,"Eroare","Nu ati selectat valori pentru Nr tema si Id student!");
        }

    }

    public void setScene(Scene scene) {
        this.scene=scene;

        scene.setOnKeyPressed(event -> {
            if (event.getCode()== KeyCode.S && event.isControlDown()){
                handleSave(new ActionEvent());
            }
        });
    }

    @FXML
    public void handleCancel(ActionEvent event){
        dialogStage.close();
    }

}
