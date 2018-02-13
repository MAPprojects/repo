package View;
import Domain.EntitiesException;
import Domain.Student;
import Repository.RepositoryException;
import Service.Service;
import View.StudentView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import utils.ListEvent;
import utils.Observer;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentViewController implements Observer<Student>{
    private Service service;
    private ObservableList<Student> model;
    private StudentView stdw;
    public StudentViewController(Service service){
        this.service=service;
        model= FXCollections.observableArrayList(service.fromIterableToList(service.getAllStud()));
    }

    public void update(ListEvent<Student> e){
        model.setAll(StreamSupport.stream(e.getList().spliterator(),false)
        .collect(Collectors.toList()));
    }

    public ObservableList<Student> getModel() {
        return model;
    }

    public void setView(StudentView stdw){
        this.stdw=stdw;
    }

    public void addStudent(ActionEvent actionEvent){
        try{
            service.saveStudent(getStudentFromFields());
            showMessage(Alert.AlertType.INFORMATION,"Adaugare...","S-a adaugat studentul.");
            clearAll();
        }catch (RepositoryException re) {
            showErrorMessage("Exista deja un student cu acest id!");
            clearAll();
        }catch (EntitiesException e){
            showErrorMessage("Nu ati introdus date valide. ID NEVALID!");
        }catch (NumberFormatException ne){
            showErrorMessage("Date invalide! Nu ati completat campurile!");
            clearAll();
        }
    }

    public void deleteStudent(ActionEvent actionEvent){
        Student st=stdw.tableView.getSelectionModel().getSelectedItem();
        if(st!=null){
            int id=st.getId();
            service.deleteStudent(id);
            showMessage(Alert.AlertType.INFORMATION,"Stergere...","Studentul a fost sters.");
        }
        else
            showMessage(Alert.AlertType.WARNING, "Stergere", "Studentul cu acest ID nu exista");
    }

    public void updateStudent(ActionEvent actionEvent){
        if(stdw.idField.getText().equals("") || stdw.numeField.getText().equals("") || stdw.grupaField.getText().equals("") || stdw.emailField.getText().equals("") || stdw.profesorField.getText().equals(""))
            showErrorMessage("Nu ati completat toate campurile!!!");
        else{
            try {
                Student st = getStudentFromFields();
                service.updateStudent(st);
                showMessage(Alert.AlertType.INFORMATION,"Modificare...","Datele au fost modificate.");
                clearAll();
            }catch (RepositoryException re){
                showMessage(Alert.AlertType.ERROR,"Modificare...","Studentul cu acest ID nu exista!");
            }
        }
    }

    private Student getStudentFromFields(){
        String id=stdw.idField.getText();
        String nume=stdw.numeField.getText();
        String grupa=stdw.grupaField.getText();
        String email=stdw.emailField.getText();
        String prof=stdw.profesorField.getText();
        Student st=new Student(Integer.parseInt(id),nume,grupa,email,prof);
        return st;
    }

    private void clearAll(){
        stdw.idField.clear();
        stdw.numeField.clear();
        stdw.grupaField.clear();
        stdw.emailField.clear();
        stdw.profesorField.clear();
    }

    private static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    private static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    public void showDetails(Student othStudent){
        if(othStudent!=null) {
            stdw.idField.setText(othStudent.getId().toString());
            stdw.numeField.setText(othStudent.getNume());
            stdw.grupaField.setText(othStudent.getGrupa());
            stdw.emailField.setText(othStudent.getEmail());
            stdw.profesorField.setText(othStudent.getCadru());
        }
    }
}
