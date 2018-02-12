package com.company.GUI;

import com.company.Domain.Account;
import com.company.Domain.Globals;
import com.company.Domain.Student;
import com.company.Service.Service;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class LoginCtrl {

    public PasswordField pass;
    public TextField user;
    public Service service;

    public void tryLogin() {
        String u = user.getText();
        String p = pass.getText();
        boolean valid=false;

        if(u.equals("") || p.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Username and password fields must be completed!");
            alert.showAndWait();
        }
        else
        {
            if (u.getBytes()[0] == 'P' || p.equals("admin")) {
                if (u.equals("P_Czibula Istvan") || u.equals("P_Serban Camelia") || u.equals("P_Cretu Camelia") || u.equals("admin") && p.equals("admin")) {
                    valid = true;
                    Stage stage = (Stage) pass.getScene().getWindow();
                    stage.close();
                    Globals.getInstance().accessLevel = 2;
                }

            } else {
                Map<Integer, String> accounts = service.getAccounts();
                ArrayList<Student> students = new ArrayList<>((Collection<? extends Student>) service.getStudents());
                for (Student ac : students) {
                    //System.out.println(ac.getUsername());
                    //System.out.println(ac.getPassword());
                    if ((ac.getID() + "_" + ac.getNume()).compareTo(u) == 0 && accounts.get(ac.getID()).compareTo(p) == 0) {
                        valid = true;
                        Stage stage = (Stage) pass.getScene().getWindow();
                        stage.close();
                        Globals.getInstance().accessLevel = 1;
                        if (u.compareTo("admin") != 0) {
                            String[] strings = u.split("_");
                            Globals.getInstance().nonAdminID = Integer.parseInt(strings[0]);
                        }
                        break;
                    }
                }
            }
            if (!valid) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid username or password!");
                alert.showAndWait();
            }
        }
    }


    public void checkForEnter(KeyEvent keyEvent) {

        try {
            String s = keyEvent.getText();
            char c = s.charAt(0);
            //System.out.println((int) c);
            if ((int) c == 13)
                tryLogin();
        }
        catch (StringIndexOutOfBoundsException exc)
        {

        }
    }

}
