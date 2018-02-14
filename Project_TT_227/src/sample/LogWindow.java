package sample;

import Domain.Student;
import Service.Service;
import Utils.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.io.*;
import java.util.ArrayList;

public class LogWindow implements Observer {

    public Button btnRefresh;
    public ChoiceBox<String> choiceBox;
    public TextArea txtArea;
    private Service service;

    @FXML
    private void initialize() {
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> showLog(choiceBox.getItems().get((Integer) number2)));
    }

    public void setService(Service service) {
        this.service = service;
        service.getStudentRepository().addObserver(this);
        refresh();
    }

    private void showLog(String studentID) {
        txtArea.setText("");
        String filePath = new File("").getAbsolutePath();
        filePath = filePath.concat("/src/Output/" + studentID.split(" ")[0] + ".txt");
        File file = new File(filePath);
        String line;
        if(file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                while((line = br.readLine()) != null) {
                    txtArea.appendText(line);
                    txtArea.appendText("\n");
                }
                br.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void refresh() {
        Iterable<Student> it = this.service.findAllStudents();
        ArrayList<String> allStudents = new ArrayList<>();
        for(Student st : it) {
            allStudents.add(st.getID() + " (" + st.getNume() + ")");
        }
        ObservableList<String> choices = FXCollections.observableArrayList(allStudents);
        choiceBox.setItems(choices);
    }

    @Override
    public void notifyObs() {
        refresh();
    }
}
