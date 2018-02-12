package GUI;

import Controller.Controller;
import Domain.LabHomework;
import Domain.Student;
import Repository.RepositoryException;
import Validator.ValidatorException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.control.ListView.*;

import javax.print.DocFlavor;
import java.awt.*;
import Controller.Controller;

public class StudentView {
    private Controller ctr;
    public StudentView(Controller ctr) {
        this.ctr = ctr;
        setOnActionComps();
    }

    private Button buttonAdd = new Button("Adaugare");
    private Button buttonDelete = new Button("Stergere");
    private Button buttonUpdate = new Button("Update");
    private TextField textFieldName = new TextField();
    private TextField textFieldGroup = new TextField();
    private TextField textFieldEmail = new TextField();
    private TextField textFieldProfessor = new TextField();
    private TextField textFieldId= new TextField();
    private TableView<Student> tableViewSts=new TableView<>();
    private TableView<LabHomework> tableViewHw=new TableView<>();

    public TableView<Student> getTableViewSts() {
        tableViewSts.getColumns().add(0,new TableColumn<>("Id"));
        tableViewSts.getColumns().add(1,new TableColumn<>("Name"));
        tableViewSts.getColumns().add(2,new TableColumn<>("Group"));
        tableViewSts.getColumns().add(3,new TableColumn<>("Email"));
        tableViewSts.getColumns().add(4,new TableColumn<>("Professor"));
        (tableViewSts.getColumns()).get(0).setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        (tableViewSts.getColumns()).get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        (tableViewSts.getColumns()).get(2).setCellValueFactory(new PropertyValueFactory<>("group"));
        (tableViewSts.getColumns()).get(3).setCellValueFactory(new PropertyValueFactory<>("email"));
        (tableViewSts.getColumns()).get(4).setCellValueFactory(new PropertyValueFactory<>("professor"));

        tableViewSts.setItems(ctr.getStudentModel());
        tableViewSts.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showStudent(newValue)));
        tableViewSts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tableViewSts;
    }

    public TableView<LabHomework> getTableViewHw() {
        tableViewHw.getColumns().add(0,new TableColumn<>("Id"));
        tableViewHw.getColumns().add(1,new TableColumn<>("Deadline"));
        tableViewHw.getColumns().add(2,new TableColumn<>("Description"));
        (tableViewHw.getColumns()).get(0).setCellValueFactory(new PropertyValueFactory<>("idHomework"));
        (tableViewHw.getColumns()).get(1).setCellValueFactory(new PropertyValueFactory<>("deadline"));
        (tableViewHw.getColumns()).get(2).setCellValueFactory(new PropertyValueFactory<>("description"));

        tableViewHw.setItems(ctr.getHomeworkModel());
        tableViewHw.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showStudent(newValue)));
        tableViewHw.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tableViewHw;
    }

    private void showStudent(Object st) {
        if(st==null)
            clearTextFields();
        else
            setTextFields(st);
    }

    public BorderPane makeMainPane()
    {
            BorderPane bPane=new BorderPane();
            //bPane.setBottom(bottomPane());
            bPane.setRight(rightPane());
            bPane.setLeft(leftPane());
            return bPane;
    }

    private Node leftPane() {
        GridPane gPane=new GridPane();

        gPane.setAlignment(Pos.CENTER);
        gPane.setVgap(5);
        gPane.setHgap(20);
        gPane.setPadding(new Insets(10,10,10,10));
        Label labelName=new Label("Name");
        Label labelGroup=new Label("Group");
        Label labelMail=new Label("Mail");
        Label labelProfessor=new Label("Professor");
        Label labelId=new Label("Id");
        gPane.add(labelId,0,0);
        gPane.add(labelName,0,1);
        gPane.add(labelGroup,0,2);
        gPane.add(labelMail,0,3);
        gPane.add(labelProfessor,0,4);
        gPane.add(textFieldId,1,0);
        gPane.add(textFieldName,1,1);
        gPane.add(textFieldGroup,1,2);
        gPane.add(textFieldEmail,1,3);
        gPane.add(textFieldProfessor,1,4);
        HBox hboxButtons=new HBox();
        hboxButtons.setSpacing(15);
        hboxButtons.getChildren().addAll(buttonAdd,buttonDelete,buttonUpdate);
        gPane.add(hboxButtons,0,5,3,1);
        return gPane;
    }

    private Node rightPane() {
        StackPane sPane=new StackPane();
        FlowPane fPane=new FlowPane();
        fPane.getChildren().add(getTableViewSts());
        fPane.getChildren().add(getTableViewHw());
        return fPane;
    }

    private Node bottomPane() {
        return new GridPane();

    }

    private void setTextFields(Object obj)
    {
        if(obj instanceof Student)
        {
            Student st=(Student)obj;
            textFieldId.setText(st.getId().toString());
            textFieldName.setText(st.getName().toString());
            textFieldEmail.setText(st.getEmail().toString());
            textFieldGroup.setText(st.getGroup().toString());
            textFieldProfessor.setText(st.getProfessor().toString());
        }
        else if(obj instanceof LabHomework)
        {
            LabHomework hw=(LabHomework) obj;
            textFieldId.setText(hw.getId().toString());
            textFieldName.setText(String.valueOf(hw.getDeadline()));
            textFieldEmail.setText(hw.getDescription().toString());
        }
    }



    private void clearTextFields()
    {
        textFieldId.clear();
        textFieldName.clear();
        textFieldEmail.clear();
        textFieldGroup.clear();
        textFieldProfessor.clear();
    }
    private void setOnActionComps()
    {

       /* tableViewSts.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Student st=tableViewSts.getSelectionModel().getSelectedItem();
                setTextFields(st);
            }
        });

        tableViewSts.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Student st=tableViewSts.getSelectionModel().getSelectedItem();
                setTextFields(st);
            }
        });
           */
        buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ctr.getServiceManagerObs().addStudent(Integer.parseInt(textFieldId.getText()),textFieldName.getText(),textFieldEmail.getText(),textFieldGroup.getText(),textFieldProfessor.getText());
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Student " +textFieldName.getText()+"has been succesufully added!");
                    alert.showAndWait();
                    clearTextFields();
                } catch (ValidatorException  |RepositoryException  e) {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(e.getMessage().toString());
                    alert.showAndWait();
                }
                catch(NumberFormatException ex)
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Id must be a valid positive number!");
                    alert.showAndWait();
                }
            }
        });

        buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tableViewSts.getSelectionModel().getSelectedItem()!=null)
                {
                    ctr.getServiceManagerObs().deleteStudent(tableViewSts.getSelectionModel().getSelectedItem().getId());
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Student has been succesufully deleted!");
                    alert.showAndWait();
                    clearTextFields();
                }
                else
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You must select the student you want to delete from the table!");
                    alert.showAndWait();
                }
            }
        });

        buttonUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tableViewSts.getSelectionModel().getSelectedItem()!=null)
                {
                    try {
                        ctr.getServiceManagerObs().updateStudent(Integer.parseInt(textFieldId.getText()),textFieldName.getText(),textFieldEmail.getText(),textFieldGroup.getText(),textFieldProfessor.getText());
                        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Student with id"+textFieldId.getText()+" has been succesufully updated.");
                        alert.showAndWait();
                        clearTextFields();
                    }
                    catch (ValidatorException |RepositoryException e) {
                        Alert alert=new Alert(Alert.AlertType.ERROR);
                        alert.setContentText(e.getMessage().toString());
                        alert.showAndWait();
                    }
                    catch(NumberFormatException ex)
                    {
                        Alert alert=new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Id must be a positive number");
                        alert.showAndWait();
                    }
                }
                else
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You must select the student you want to delete from the table!");
                    alert.showAndWait();
                }
            }
        });


    }

}