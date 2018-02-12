package Controller;

import Domain.Grade;
import Domain.LabHomework;
import Domain.LabHomeworkSuper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ControllerFXMLAddGrades {

    @FXML
    private TableColumn columnId;
    @FXML
    private TableColumn columnGrade=new TableColumn();
    @FXML
    TableView<LabHomework> tableViewHw=new TableView<>();
    ObservableList<LabHomework> model= FXCollections.observableArrayList();
    List<Grade> newGrades=new ArrayList<>();

    public ControllerFXMLAddGrades(){}

    public List<Grade> getNewGrades() {
        return newGrades;
    }

    @FXML public void initialize()
    {
        newGrades.clear();
        tableViewHw.setEditable(true);
        columnGrade.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public ObservableList<LabHomework> getModel() {
        return model;
    }


    public void setModel(ObservableList<LabHomework> model) {
        this.model = model;
        tableViewHw.setItems(model);

    }

    private int containId(int id)
    {
        for(int i=0;i<newGrades.size();i++)
            if(newGrades.get(i).getIdHomework()==id)
                return i;
        return -1;
    }
    public void handlerOnEdit(TableColumn.CellEditEvent cellEditEvent) {
        try
        {
            String oldValue = cellEditEvent.getNewValue().toString();
            String newValue = cellEditEvent.getOldValue().toString();
        }
        catch (RuntimeException e)
        {
            try {
                newGrades.add(0, new Grade(1, tableViewHw.getSelectionModel().getSelectedItem().getId(), Integer.parseInt(cellEditEvent.getNewValue().toString())));
            }
            catch(NumberFormatException ex)
            {
                System.out.println("dghfhdfcgv");
                while(!newGrades.isEmpty() && containId(tableViewHw.getSelectionModel().getSelectedItem().getId())>-1)
                 {
                     System.out.println("dghfhdfcgv");
                    newGrades.remove(containId( tableViewHw.getSelectionModel().getSelectedItem().getId()));
                 }
            }
        }

    }
}
