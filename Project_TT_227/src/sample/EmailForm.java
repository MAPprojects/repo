package sample;

import Domain.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import Service.Service;
import Service.EmailSender;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class EmailForm {

    public TextArea txtArea;
    public TextField txtFrom;
    public TextField txtSubject;
    public ChoiceBox<String> choiceTo;
    public Button btnSend;
    public Button btnClear;
    private Service service;

    public void setService(Service service) {
        this.service = service;
        Iterable<Student> it = this.service.findAllStudents();
        ArrayList<String> allStudents = new ArrayList<>();
        for(Student st : it) {
            allStudents.add(st.getNume() + " (" + st.getEmail() + ")");
        }
        ObservableList<String> choices = FXCollections.observableArrayList(allStudents);
        choiceTo.setItems(choices);
        choiceTo.setValue(choices.get(0));
    }

    @FXML
    private void sendEmail() {
        String sendTo = "";
        if(!Objects.equals(choiceTo.getValue(), "")) {
            sendTo = choiceTo.getValue();
            int firstP = sendTo.indexOf("(");
            int secondP = sendTo.indexOf(")");
            sendTo = sendTo.substring(firstP+1,secondP);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Trebuie sa alegeti un student!");
            alert.showAndWait();
        }
        if(sendTo.length() > 0) {
            if(txtArea.getText().length() == 0 || txtSubject.getText().length() == 0) {
                ButtonType btnYes = new ButtonType("Da",ButtonBar.ButtonData.YES);
                ButtonType btnNo = new ButtonType("Nu",ButtonBar.ButtonData.NO);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Mesajul si/sau subiectul este/sunt gol/goale. Doriti sa trimiteti e-mailul oricum?",btnYes,btnNo);
                alert.setHeaderText("");
                alert.setTitle("");

                Optional<ButtonType> result = alert.showAndWait();

                if(result.isPresent() && result.get() == btnYes) {
                    EmailSender email = new EmailSender("smtp.gmail.com", "tudortritean@gmail.com", "Iwillbethebest1", sendTo, "tudortritean@gmail.com", txtSubject.getText(), "");
                    email.send();
                    txtArea.setText("");
                    txtSubject.setText("");
                    Alert succes = new Alert(Alert.AlertType.INFORMATION,"E-mail-ul a fost trimis!");
                    succes.setTitle("");
                    succes.setHeaderText("");
                    succes.showAndWait();
                }
            }
            else {
                EmailSender email = new EmailSender("smtp.gmail.com", "tudortritean@gmail.com", "Iwillbethebest1", sendTo, "tudortritean@gmail.com", txtSubject.getText(), txtArea.getText());
                email.send();
                txtArea.setText("");
                txtSubject.setText("");
                Alert succes = new Alert(Alert.AlertType.INFORMATION,"E-mail-ul a fost trimis!");
                succes.setTitle("");
                succes.setHeaderText("");
                succes.showAndWait();
            }
        }
    }

    @FXML
    private void clear() {
        txtArea.setText("");
    }
}
