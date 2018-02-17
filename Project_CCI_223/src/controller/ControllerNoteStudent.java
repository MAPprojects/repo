package controller;

import java.util.ArrayList;
import java.util.List;
import domain.Nota;
import domain.Student;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.Service;

public class ControllerNoteStudent extends ControllerNote {

    @Override
    public void initialize(){
        super.initialize();
        tableViewNote.setEditable(false);
        imageAdd.setVisible(false);
        imageUpdate.setVisible(false);
        checkComboBoxIdStudent.setVisible(false);
        labelIdStudent.setVisible(false);
    }

    @Override
    protected void setComboBoxFiltrariList(){
        ObservableList<String> listFiltrari= FXCollections.observableArrayList();
        listFiltrari.addAll("dupa Valoare","dupa Numar tema");
        comboBoxFiltrariNote.setItems(listFiltrari);
    }

    @Override
    protected List<Nota> setDataInit(Service service){
        Student student=service.getStudentByEmail(currentUser.getEmail());
        if (student==null){
           return new ArrayList<>();
        }
        else {
            return service.getNoteByIdStudent(student.getId());
        }
    }
}
