package Constrollers;

import Domain.Student;
import Domain.Tema;
import Service.Service;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class AssignmentController implements Observer
{
    Service service;
    ObservableList<Tema> modelTema;

    @FXML
    AnchorPane extendableSearchPane;
    @FXML
    Rectangle clipRect;
    @FXML
    TableView<Tema> tableView;
    @FXML TableColumn<Tema,String> colDescriere;
    @FXML TableColumn<Tema,String>  colId;
    @FXML TableColumn<Tema,String> colDeadline;

    @FXML Button addButton;
    @FXML Button updateButton;

    @FXML TextField txtId;
    @FXML TextArea txtDescriere;
    @FXML TextField txtTermen;

    @FXML TextField txtDescriereFiltr;
    @FXML TextField txtTermenFiltr;
    @FXML CheckBox chkTermenFiltr;

    @FXML
    SplitPane splitPane;

    public AssignmentController()
    {
    }

    public void setService(Service service)
    {
        this.service=service;
        this.modelTema = FXCollections.observableArrayList(service.iterableToArrayList(service.getTeme()));
        tableView.setItems(modelTema);
    }

    @FXML
    public void initialize() {
        colDescriere.setCellValueFactory(new PropertyValueFactory<Tema,String>("descriere"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<Tema, String>("deadline"));
        colId.setCellValueFactory(new PropertyValueFactory<Tema,String>("ID"));
//        colGrupa.setCellValueFactory(new PropertyValueFactory<Student,String>("grupa"));



        Node divider = splitPane.lookup(".split-pane-divider");
        if(divider!=null){
            divider.setStyle("-fx-background-color: #C94B46;");
        }

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tema>() {
            @Override
            public void changed(ObservableValue observable, Tema oldValue, Tema newValue)
            {
                if(newValue!= null)
                {
                    fillTxtFields(newValue);
                    if(clipRect.heightProperty().get()==0)
                        toggleExtendableSearch();
                    addButton.setDisable(true);
                }
            }
        });

        double widthInitial = 627;
        double heightInitial = 134;

        clipRect = new Rectangle();
        clipRect.setWidth(widthInitial);
        clipRect.setHeight(0);
        clipRect.translateYProperty().set(heightInitial);
        extendableSearchPane.setClip(clipRect);
        extendableSearchPane.translateYProperty().set(-heightInitial);
        extendableSearchPane.prefHeightProperty().set(0);

        addButton.setOnAction(this::addTema);
        updateButton.setOnAction(this::updateTema);

        txtDescriereFiltr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterManager(txtDescriereFiltr.getText(),txtTermenFiltr.getText(),chkTermenFiltr.isSelected());
            }
        });

        txtTermenFiltr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterManager(txtDescriereFiltr.getText(),txtTermenFiltr.getText(),chkTermenFiltr.isSelected());
            }
        });

        chkTermenFiltr.setOnAction(this::chkEventHandle);



    }

    private void chkEventHandle(ActionEvent actionEvent) {
        filterManager(txtDescriereFiltr.getText(),txtTermenFiltr.getText(),chkTermenFiltr.isSelected());
    }

    private void filterManager(String descriere, String termen, boolean termenExpirat)
    {
        List<Tema> temp = service.iterableToArrayList(service.getTeme());
        if(!descriere.equals(""))
            temp = service.filterDescription(temp,descriere);
        if(!termen.equals(""))
            temp = service.filterDeadline(temp,Integer.parseInt(termen));
        if(termenExpirat)
            temp = service.filterFinishedDeadline(temp);
        modelTema.setAll(FXCollections.observableArrayList(temp));
    }

    private void addTema(ActionEvent actionEvent) {

        try {
            Integer id = Integer.parseInt(txtId.getText());
            String descriere = txtDescriere.getText();
            String termen = txtTermen.getText();

            service.addTema(id,descriere,new Integer(termen));

            clearTxtFields();
        }
        catch(RuntimeException ex){}

    }

    private void updateTema(ActionEvent actionEvent)
    {
        Integer id = Integer.parseInt(txtId.getText());
        String termen = txtTermen.getText();
        String descriere = txtDescriere.getText();

        try {
            if (!termen.equals(""))
                service.modifyTema(id, new Integer(termen));

            if (!descriere.equals(""))
                service.modifyTemaDescr(id, descriere);

            clearTxtFields();
        }
        catch (RuntimeException ex)
        {

        }
    }

    private void fillTxtFields(Tema tema)
    {
        txtDescriere.setText(tema.getDescriere());
        txtId.setText(tema.getID().toString());
        txtTermen.setText(tema.getDeadline().toString());

        txtId.setDisable(true);
    }

    private void clearTxtFields()
    {
        txtId.setText("");
        txtDescriere.setText("");
        txtTermen.setText("");

        txtId.setDisable(false);
        addButton.setDisable(false);
    }

    @FXML
    public void toggleExtendableSearch() {

        clipRect.setHeight(extendableSearchPane.getHeight());
        clipRect.setWidth(extendableSearchPane.getWidth());

        if (clipRect.heightProperty().get() != 0) {

            // Animation for scroll up.
            Timeline timelineUp = new Timeline();

            clearTxtFields();

            // Animation of sliding the search pane up, implemented via
            // clipping.
            final KeyValue kvUp1 = new KeyValue(clipRect.heightProperty(), 0);
            final KeyValue kvUp2 = new KeyValue(clipRect.translateYProperty(), extendableSearchPane.getHeight());

            // The actual movement of the search pane. This makes the table
            // grow.
            final KeyValue kvUp4 = new KeyValue(extendableSearchPane.prefHeightProperty(), 0);
            final KeyValue kvUp3 = new KeyValue(extendableSearchPane.translateYProperty(), -extendableSearchPane.getHeight());

            final KeyFrame kfUp = new KeyFrame(Duration.millis(250), kvUp1, kvUp2, kvUp3, kvUp4);
            timelineUp.getKeyFrames().add(kfUp);
            timelineUp.play();
        } else {

            // Animation for scroll down.
            Timeline timelineDown = new Timeline();

            // Animation for sliding the search pane down. No change in size,
            // just making the visible part of the pane
            // bigger.
            final KeyValue kvDwn1 = new KeyValue(clipRect.heightProperty(), 134);
            final KeyValue kvDwn2 = new KeyValue(clipRect.translateYProperty(), 0);

            // Growth of the pane.
            final KeyValue kvDwn4 = new KeyValue(extendableSearchPane.prefHeightProperty(), 134);
            final KeyValue kvDwn3 = new KeyValue(extendableSearchPane.translateYProperty(), 0);

            final KeyFrame kfDwn = new KeyFrame(Duration.millis(250), kvDwn1, kvDwn2,
                    kvDwn3, kvDwn4);
            timelineDown.getKeyFrames().add(kfDwn);

            timelineDown.play();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        modelTema.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getTeme())));

    }
}
