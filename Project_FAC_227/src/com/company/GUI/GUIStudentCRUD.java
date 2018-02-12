package com.company.GUI;

import com.company.Domain.Nota;
import com.company.Domain.Student;
import com.company.Service.Service;
import com.company.Utility.Event;
import com.company.Utility.Observable;
import com.company.Utility.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GUIStudentCRUD implements Observer<Student>{

    private Scene thisScene;
    private GUIStudentCRUDController controller;

    private TextField tidStudent;
    private TextField tNume;
    private TextField tGrupa;
    private TextField tEmail;
    private TextField tProfLab;

    private Button add;
    private Button del;
    private Button upd;

    private TableView<Student> studentTableView;


    public GUIStudentCRUD(GUIStudentCRUDController controller)
    {
        this.studentTableView = new TableView<>();
        this.controller = controller;
        this.thisScene = setStudentCRUD();
        updateTable();

    }

    public Scene getScene()
    {
        return thisScene;
    }

    public Scene setStudentCRUD() {

        TableColumn<Student,String>  column_name = new TableColumn<>("Nume");
        TableColumn<Student,Integer>  column_grupa = new TableColumn<>("Grupa");
        TableColumn<Student,String> column_prof = new TableColumn<>("Profesor");

        studentTableView.getColumns().addAll(column_name,column_grupa,column_prof);

        column_name.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
        column_grupa.setCellValueFactory(new PropertyValueFactory<Student,Integer>("grupa"));
        column_prof.setCellValueFactory(new PropertyValueFactory<Student,String >("profLab"));


        studentTableView.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->fillFields(newValue));

        Label lidStudent = new Label("Id Student:");
        Label lNume = new Label("Nume Student:");
        Label lGrupa = new Label("Grupa:");
        Label lEmail = new Label("eMail:");
        Label lProfLab = new Label("Profesor:");

        this.tidStudent = new TextField();
        this.tNume = new TextField();
        this.tGrupa = new TextField();
        this.tEmail = new TextField();
        this.tProfLab = new TextField();

        GridPane inputSection = new GridPane();
        inputSection.setAlignment(Pos.CENTER);
        inputSection.add(lidStudent,0,0);
        inputSection.add(tidStudent,1,0);
        inputSection.add(lNume,0,1);
        inputSection.add(tNume,1,1);
        inputSection.add(lGrupa,0,2);
        inputSection.add(tGrupa,1,2);
        inputSection.add(lEmail,0,3);
        inputSection.add(tEmail,1,3);
        inputSection.add(lProfLab,0,4);
        inputSection.add(tProfLab,1,4);


        this.add = new Button("Add");
        add.setPrefSize(100,5);
        //add.setOnAction(e->addButtonAction());
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addButtonAction();
            }
        });

        this.upd = new Button("Update");
        upd.setPrefSize(100,5);
        upd.setOnAction(e->updButtonAction());

        this.del = new Button("Delete");
        del.setPrefSize(100,5);
        del.setOnAction(e->delButtonAction());


        VBox inputZone = new VBox();
        inputZone.setAlignment(Pos.CENTER);;
        inputZone.getChildren().addAll(inputSection,add,upd,del);

        HBox layout = new HBox();
        layout.getChildren().addAll(studentTableView,inputZone);
        layout.setSpacing(50);
        return new Scene(layout,600,600);
    }

    private void addButtonAction()
    {
        String idStudent = tidStudent.getText();
        String numeStudent = tNume.getText();
        String grupaStudent = tGrupa.getText();
        String emailStudent = tEmail.getText();
        String profStudent = tProfLab.getText();

        controller.addStudent(idStudent,numeStudent,grupaStudent,emailStudent,profStudent);
    }

    private void delButtonAction()
    {
        String idStudent = tidStudent.getText();

        controller.deleteStudent(idStudent);
    }

    private void updButtonAction()
    {
        String idStudent = tidStudent.getText();
        String numeStudent = tNume.getText();
        String grupaStudent = tGrupa.getText();
        String emailStudent = tEmail.getText();
        String profStudent = tProfLab.getText();

        controller.updateStudent(idStudent,numeStudent,grupaStudent,emailStudent,profStudent);
    }

    private void updateTable()
    {
        Iterable<Student> iterable = controller.getStudentsList();
        List<Student> studentList = new ArrayList<Student>((Collection<? extends Student>) iterable);
        ObservableList<Student> obList = FXCollections.observableList(studentList);
        studentTableView.setItems(obList);

    }

    private void getSelected()
    {
        Student student = studentTableView.getSelectionModel().getSelectedItem();
        tidStudent.setText(String.valueOf(student.getID()));
        tNume.setText(student.getNume());
        tGrupa.setText(String.valueOf(student.getGrupa()));
        tEmail.setText(student.getEmail());
        tProfLab.setText(student.getProfLab());
    }

    private void fillFields(Student student) {
        if(student == null) return;
        tidStudent.setText(String.valueOf(student.getID()));
        tNume.setText(student.getNume());
        tGrupa.setText(String.valueOf(student.getGrupa()));
        tEmail.setText(student.getEmail());
        tProfLab.setText(student.getProfLab());
    }

    @Override
    public void notifyOnEvent(Event<Student> event) {
        updateTable();
    }

}
