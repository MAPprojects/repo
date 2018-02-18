package Controller;

import Domain.Student;
import Service.Service;
import View.StudentView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.function.Consumer;

public class StudentController implements java.util.Observer
{
    Service service;
    StudentView studentView;
    ObservableList<Student> modelStudent;


    public StudentController(Service service)
    {
        this.service=service;
        this.modelStudent=FXCollections.observableArrayList(service.iterableToArrayList(service.getAllStudents()));
    }
    public StudentController()
    {

    }

    @FXML
    public void initialize()
    {
        System.out.println("daa");

    }

    public void setView(StudentView view)
    {
        this.studentView=view;
    }

   public ObservableList<Student> getModelStudent()
    {
        return modelStudent;
    }


    public void fillFiledsFromSelectedItem(Student newValue)
    {
        if(newValue!=null)
        {
            studentView.textFieldID.setText(newValue.getID().toString());
            studentView.textFieldID.setDisable(true);
            studentView.textFieldNume.setText(newValue.getNume());
            studentView.textFieldGrupa.setText(""+newValue.getGrupa());
            studentView.textFieldEmail.setText(newValue.getEmail());
            studentView.textFieldProf.setText(newValue.getCadruDidactic());
        }
    }

    public void handleClearFields(ActionEvent actionEvent)
    {
        studentView.textFieldID.clear();
        studentView.textFieldID.setDisable(false);
        studentView.textFieldNume.clear();
        studentView.textFieldGrupa.clear();
        studentView.textFieldEmail.clear();
        studentView.textFieldProf.clear();
    }

    public void handleAddStudentFromFilds(ActionEvent actionEvent)
    {

        try
        {
            Integer id=Integer.parseInt(studentView.textFieldID.getText());
            String nume=studentView.textFieldNume.getText();
            int grupa=Integer.parseInt(studentView.textFieldGrupa.getText());
            String email=studentView.textFieldEmail.getText();
            String prof=studentView.textFieldProf.getText();
            service.addStudent(id,nume,grupa,email,prof);
        }
        catch (NumberFormatException ne)
        {
            showError("Atributele studentului nu sunt valide\n ID, grupa: numere intregi pozitive\n nume, email,profesor: siruri de caractere ");
        }catch (Exception e)
        {
            showError(e.getMessage());
        }
    }

    private Student getStudentFromFields()
    {
        Integer id=Integer.parseInt(studentView.textFieldID.getText());
        String nume=studentView.textFieldNume.getText();
        int grupa=Integer.parseInt(studentView.textFieldGrupa.getText());
        String email=studentView.textFieldEmail.getText();
        String prof=studentView.textFieldProf.getText();
        return  new Student(id,nume,grupa,email,prof);
    }
    @Override
    public void update(Observable o, Object arg) {
        modelStudent.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getAllStudents())));
    }

    public void handleDeleteStudentSelected(ActionEvent actionEvent)
    {

        try
        {
            Integer id=Integer.parseInt(studentView.textFieldID.getText());
            service.deleteStudent(id);
        }
        catch (NumberFormatException ne)
        {
            showError("Selectati studentul pe care doriti sa il stergeti");
        }
        catch(Exception e)
        {
            showError(e.getMessage());
        }
        handleClearFields(actionEvent);
    }

    private void showError(String message) {
            Alert alerta=new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Mesaj eroare");
            alerta.setContentText(message);
            alerta.showAndWait();
    }

    public void handleUpdateStudentFromFields(ActionEvent actionEvent)
    {
        try
        {
            Student student=getStudentFromFields();
            service.updateStudent(student.getID(),student.getNume(),student.getGrupa(),student.getEmail(),student.getCadruDidactic());
        }catch(Exception e)
        {
            showError(e.getMessage());
        }
    }

    public void handleFiler(ActionEvent actionEvent)
    {
//        String filterName=studentView.comboBoxFilter.getSelectionModel().getSelectedItem().toString();
//        List<Student> lista_fil=new ArrayList<>();
//        try {
//            if (filterName.equals("Filtreaza dupa nume student")) {
//                lista_fil = service.filterByStudentNameSortedByGroup(studentView.textFieldFilter.getText());
//            } else if (filterName.equals("Filtreaza dupa profesor")) {
//                lista_fil = service.filterByTeacherSortedByGroup(studentView.textFieldFilter.getText());
//            } else if (filterName.equals("Filtreaza dupa grupa")) {
//                lista_fil = service.filterByGroupSortedByName(Integer.parseInt(studentView.textFieldFilter.getText()));
//            } else {
//                studentView.textFieldFilter.clear();
//                lista_fil = service.iterableToArrayList(service.getAllStudents());
//            }
//        }catch (Exception e)
//        {
//            showError("Introduceti campul pentru filtrare \n nume/ grupa/ profesor");
//        }
//        lista_fil=FXCollections.observableArrayList(lista_fil);
//        modelStudent.setAll(lista_fil);
    }
    @FXML
    public void handleHi(ActionEvent actionEvent)
    {
        Alert alerta=new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Mesaj eroare");
        alerta.setContentText("hi");
        alerta.showAndWait();
    }
}
