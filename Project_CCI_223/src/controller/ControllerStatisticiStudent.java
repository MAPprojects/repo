package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ControllerStatisticiStudent extends ControllerStatistici{
    public ControllerStatisticiStudent(){
        super();
    }

    @Override
    protected void setComboBoxStatisticiList(){
        ObservableList<String> listaTipuriStatistici= FXCollections.observableArrayList();
        listaTipuriStatistici.addAll("Media notelor pentru fiecare student");
        comboBoxStatistici.setItems(listaTipuriStatistici);
    }
}
