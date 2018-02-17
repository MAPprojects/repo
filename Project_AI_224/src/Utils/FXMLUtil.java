package Utils;

import Controller.MainController;
import Domain.Student;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class FXMLUtil {
    public static <T> T load(String location) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLUtil.class.getResource(location));
        return loader.load();
    }

    public static void setGridPane(boolean isStudent, Student student, String email, Label labelStudProf, Label labelUserName, Label labelUserGroup, Label labelUserTeacher){
        if(isStudent){
            labelStudProf.setText("Student");
        }
        else{
            labelStudProf.setText("Teacher");
        }
        if(student!=null){
            labelUserGroup.setText("Group:     " + student.getGroup());
            labelUserName.setText("Name:    " + student.getName());
            labelUserTeacher.setText("Teacher:  " + student.getTeacher());
        }
        else{
            labelUserGroup.setText("Email: " + email);
            labelUserName.setVisible(false);
            labelUserTeacher.setVisible(false);
        }
    }

    public static void handleLogoutAction(MainController mainController){
        mainController.loadScreen(OpenPages.loginID, OpenPages.loginFile);
        mainController.setScreen(OpenPages.loginID);
    }

    public static void handleHomeAction(MainController mainController){
        mainController.loadScreen(OpenPages.startpageID, OpenPages.startpageFile);
        mainController.setScreen(OpenPages.startpageID);
    }

    public static void initializeStudentColumns(TableColumn columnCodMatricol, TableColumn columnName, TableColumn columnGroup, TableColumn columnTeacher){
        columnCodMatricol.setCellValueFactory(new PropertyValueFactory<Student, String>("codMatricol"));
        columnName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        columnGroup.setCellValueFactory(new PropertyValueFactory<Student, Integer>("group"));
        columnTeacher.setCellValueFactory(new PropertyValueFactory<Student, String>("teacher"));
    }
}
