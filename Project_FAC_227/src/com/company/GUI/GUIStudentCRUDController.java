package com.company.GUI;

import com.company.Domain.Student;
import com.company.Exceptions.ServiceException;
import com.company.Service.Service;
import javafx.scene.control.Alert;

public class GUIStudentCRUDController {

    private Service service;

    public GUIStudentCRUDController(Service service)
    {
        this.service = service;
    }

    public void addStudent(String id,String nume, String grupa, String email, String prof)
    {
        Integer id_a = Integer.parseInt(id);
        Integer grupa_a = Integer.parseInt(grupa);
        try {
            service.addStudent(id_a,nume,grupa_a,email,prof);
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            //System.out.println(e.getMessage());
        }
    }

    public void deleteStudent(String id)
    {
        Integer id_a = Integer.parseInt(id);
        try{
            service.deleteStudent(id_a);
        }
        catch (ServiceException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            //System.out.println(e.getMessage());
        }
    }

    public void updateStudent(String id,String nume, String grupa, String email, String prof){
        Integer id_a = Integer.parseInt(id);
        Integer grupa_a = Integer.parseInt(grupa);
        try{
            service.updateStudent(id_a,nume,grupa_a,email,prof);
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            //System.out.println(e.getMessage());
        }
    }

    public Iterable<Student> getStudentsList()
    {
        return service.getStudents();
    }


}
