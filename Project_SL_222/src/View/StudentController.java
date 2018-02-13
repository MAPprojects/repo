package View;

import Domain.Student;
import Repository.RepositoryException;
import Service.Service;
import Util.ListEvent;
import Util.Observer;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentController implements Observer<Student> {
    private Service ctr;
    private ObservableList<Student> model;
    StudentView view;

    public StudentController(Service ctr){
        this.ctr=ctr;
        model= FXCollections.observableArrayList(ctr.getAllS());
    }

    @Override
    public void notifyEvent(ListEvent<Student> e){
        model.setAll(StreamSupport.stream(e.getList().spliterator(),false).collect(Collectors.toList()));
    }

    private Student getStudentFromFields(){
        Integer id = Integer.parseInt(view.idField.getText());
        String nume=view.nameField.getText();
        Integer grupa=Integer.parseInt(view.groupField.getText());
        String email=view.emailField.getText();
        String profesor=view.teacherField.getText();

        return new Student(id,nume,grupa,email,profesor);
    }

    public void clearFields() {
        view.nameField.clear();
        view.groupField.clear();
        view.idField.clear();
        view.idField.setDisable(false);
        view.emailField.clear();
        view.teacherField.clear();
    }

    public void connectAdd(ActionEvent actionEvent){
        /*
        Stage stage = new Stage();
        stage.setTitle("Add Student");
        BorderPane addPane=view.initaddView();
        stage.setScene(new Scene(addPane, 450, 300));
        stage.show();
        */
        try{
            ctr.addStudent(getStudentFromFields().getID(),getStudentFromFields().getNume(),getStudentFromFields().getGrupa(),getStudentFromFields().getEmail(),getStudentFromFields().getProfesor());
            showMsg(Alert.AlertType.INFORMATION,"ADDED","Student added successfully!");
            clearFields();
        }
        catch(ValidationException e){
            showError(e.getMessage());
        }
        catch(RepositoryException e) {
            showError(e.getMessage());
        }
        catch (NumberFormatException e){
            showMsg(Alert.AlertType.ERROR,"ERROR","Invalid fields!");
        }
    }


    public void connectDelete(Student stud,ActionEvent actionEvent){
        try {
            /*
            TablePosition pos = view.tableView.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            Student item = view.tableView.getItems().get(row);
            TableColumn col = pos.getTableColumn();
            Integer data =  (Integer) col.getCellObservableValue(item).getValue();
            */

            //ctr.deleteStudent(data);
            //ctr.deleteStudent(getStudentFromFields().getID());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you want to delete this item?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, noButton);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == okButton)) {
                    //ctr.deleteStudent(data);
                    ctr.deleteStudent(stud.getID());
                    showMsg(Alert.AlertType.INFORMATION, "Deleted", "Student deleted successfully!");
                    clearFields();
            }

            //showMsg(Alert.AlertType.INFORMATION,"Deleted","Student deleted successfully!");
            //clearFields();
        }
        catch (ValidationException e){
            showError(e.getMessage());
        }
        catch (RepositoryException e){
            showError(e.getMessage());
        }
        catch (IndexOutOfBoundsException e){
            showMsg(Alert.AlertType.ERROR,"ERROR","Invalid selection! Select the ID of the student!");
        }
        catch(ClassCastException e){
            showMsg(Alert.AlertType.ERROR,"ERROR","Invalid selection! Select the ID of the student!");
        }
    }

    public void connectUpdate(ActionEvent actionEvent){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you want to update this item?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, noButton);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == okButton)) {
                ctr.updateStudent(getStudentFromFields());
                showMsg(Alert.AlertType.INFORMATION, "Updated", "Student updated successfully");
                clearFields();
            }
        }
        catch (ValidationException e){
            showError(e.getMessage());
        }
        catch (RepositoryException e){
            showError(e.getMessage());
        }
        catch (NumberFormatException e){
            showMsg(Alert.AlertType.ERROR,"ERROR","Invalid fields");
        }
    }

    public ObservableList<Student> getModel() {
        return model;
    }

    public void setView(StudentView view) {
        this.view = view;
    }

    public void showStudentDetails(Student newValue){
        if(newValue!=null){
            view.idField.setText(Integer.toString(newValue.getID()));
            view.idField.setDisable(true);
            view.nameField.setText(newValue.getNume());
            view.groupField.setText(Integer.toString(newValue.getGrupa()));
            view.emailField.setText(newValue.getEmail());
            view.teacherField.setText(newValue.getProfesor());
        }
    }

    static void showMsg(Alert.AlertType type, String header, String text){
        Alert msg=new Alert(type);
        msg.setTitle(header);
        msg.setContentText(text);
        msg.showAndWait();
    }
    static void showError(String text){
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setTitle("ERROR");
        msg.setContentText(text);
        msg.showAndWait();
    }
}
