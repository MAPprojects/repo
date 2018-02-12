package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Domain.Nota;
import com.company.Domain.Student;
import com.company.Domain.Tema;
import com.company.Service.Service;
import com.company.Utility.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FiltersCtrl {
    public TextField inputField;
    public Label inputText;
    public Button goButton;
    public Slider sliderSelector;
    public Label f1;
    public Label f2;
    public Label f3;
    public ComboBox entitySelection;
    public Service service;

    public BorderPane mainPaneReference;

    public ListView<String> listView = new ListView<>();
    public Slider sliderS;
    public Button prevS;
    public Button nextS;

    public int perPage;

    public ObservableList<String> current;

    public void initialize()
    {
        listView.setPrefHeight(380);
        listView.setFixedCellSize(23);
        ArrayList<String> list = new ArrayList<>();
        //list.add("");
        list.add("Students");
        list.add("Projects");
        list.add("Grades");
        entitySelection.getItems().addAll(list);
        //entitySelection.setValue(entitySelection.getItems().get(0));
        entitySelection.getSelectionModel().select(0);
        //setInterfaceMethod();
        /*
        sliderSelector.setVisible(false);
        f1.setVisible(false);
        f2.setVisible(false);
        f3.setVisible(false);
        goButton.setVisible(false);
        inputField.setVisible(false);
        inputText.setVisible(false);
        */
        sliderS.setMin(0);
        sliderS.setMax(0);
        sliderS.getParent().setDisable(true);
        stylise();
    }

    public void initSlider(ObservableList<String> entities)
    {
        double heigth = listView.getPrefHeight();
        double rowH = listView.getFixedCellSize();
        perPage = (int)(heigth/rowH);


        sliderS.setMin(1);
        sliderS.setMax(((entities.size()-1)/perPage)+1);
        sliderS.setMajorTickUnit(1);
        sliderS.setValue(1);
        pagPopulate(entities);
        if(sliderS.getMax()<=1)
            sliderS.getParent().setDisable(true);
        else
            sliderS.getParent().setDisable(false);
        sliderS.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pagPopulate(entities);
            }
        });
        sliderSelector.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                enableDisable();
            }
        });

    }

    private void enableDisable() {
        if(entitySelection.getSelectionModel().isSelected(0) && sliderSelector.getValue()==3)
        {
            inputField.setDisable(false);
            inputField.clear();
            inputField.setPromptText("<Nume profesor>");
        }
        if(entitySelection.getSelectionModel().isSelected(0) && sliderSelector.getValue()==2)
        {
            inputField.setDisable(false);
            inputField.clear();
            inputField.setPromptText("<Domeniu email>");
        }
        if(entitySelection.getSelectionModel().isSelected(0) && sliderSelector.getValue()==1)
        {
            inputField.setDisable(false);
            inputField.clear();
            inputField.setPromptText("<Valoare nota>");
        }

        if(entitySelection.getSelectionModel().isSelected(1) && sliderSelector.getValue()==3)
        {
            inputField.setDisable(false);
            inputField.clear();
            inputField.setPromptText("<Deadline>");
        }
        if(entitySelection.getSelectionModel().isSelected(1) && sliderSelector.getValue()==2)
        {
            inputField.setDisable(false);
            inputField.clear();
            inputField.setPromptText("<Sir de caractere>");
        }
        if(entitySelection.getSelectionModel().isSelected(1) && sliderSelector.getValue()==1)
        {
            inputField.setDisable(true);
            inputField.clear();
            inputField.setPromptText("nenecesar");
        }

        if(entitySelection.getSelectionModel().isSelected(2) && sliderSelector.getValue()==3)
        {
            inputField.setDisable(false);
            inputField.clear();
            inputField.setPromptText("<Id student>");
        }
        if(entitySelection.getSelectionModel().isSelected(2) && sliderSelector.getValue()==2)
        {
            inputField.setDisable(false);
            inputField.clear();
            inputField.setPromptText("<Numar tema>");
        }
        if(entitySelection.getSelectionModel().isSelected(2) && sliderSelector.getValue()==1)
        {
            inputField.setDisable(true);
            inputField.clear();
            inputField.setPromptText("nenecesar");
        }

    }

    private void pagPopulate(ObservableList<String> entities) {
        int i = ((int)sliderS.getValue()-1)*perPage;
        List<String> toAdd = new ArrayList<>();
        current = entities;
        for(int j = i;j<i+perPage && j<entities.size();j++)
            toAdd.add(entities.get(j));
        ObservableList<String> obsList = FXCollections.observableList(toAdd);
        listView.setItems(obsList);
    }

    public void stylise()
    {
        goButton.getParent().getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        goButton.getParent().getStyleClass().add("pane1");
        f1.getParent().getStyleClass().add("pane1");
        entitySelection.getParent().getStyleClass().add("pane1");
        sliderSelector.getParent().getStyleClass().add("pane1");
        sliderSelector.getStyleClass().add("slider1");
        sliderS.getStyleClass().add("slider1");
        listView.getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        listView.getStyleClass().add("table-view");
        listView.getStyleClass().add("list-cell");
        entitySelection.setStyle("-fx-background-color: white");
        for(String o: entitySelection.getStyleClass())
            System.out.println(o);
    }

    public void setInterface(ActionEvent actionEvent) {
        setInterfaceMethod();
    }

    public void setInterfaceMethod()
    {

        if(Globals.getInstance().accessLevel==2) {
            if (entitySelection.getSelectionModel().isSelected(0)) {
                sliderSelector.setVisible(false);
                f1.setVisible(false);
                f2.setVisible(false);
                f3.setVisible(false);
                goButton.setVisible(false);
                inputField.setVisible(false);
                inputText.setVisible(false);

                listView.getItems().clear();


            }

            if (entitySelection.getSelectionModel().isSelected(0)) {
                sliderSelector.setVisible(true);
                f1.setText("Filtrare: profesor\nSortare: grupa");
                f2.setText("Filtrare: domeniu email\nSortare: nume");
                f3.setText("Filtrare: nota >\nSortare: nume");
                f1.setVisible(true);
                f2.setVisible(true);
                f3.setVisible(true);
                goButton.setVisible(true);
                inputField.setVisible(true);
                inputText.setVisible(true);

                listView.getItems().clear();
                List<String> students = new ArrayList<>();
                for (Student st : service.getStudents()) {
                    students.add("" + st.getID() + ": " + st.getNume() + "  " + st.getGrupa() + "  " + st.getEmail() + "  " + st.getProfLab());
                }
                ObservableList<String> obsStudents = FXCollections.observableList(students);
                initSlider(obsStudents);
                //listView.setItems(obsStudents);
            }
            if (entitySelection.getSelectionModel().isSelected(1)) {
                sliderSelector.setVisible(true);
                f1.setText("Filtrare: deadline <\nSortare: deadline");
                f2.setText("Filtrare: contin sir\nSortare: deadline");
                f3.setText("Filtrare: nu exista nota\nSortare: descriere");
                f1.setVisible(true);
                f2.setVisible(true);
                f3.setVisible(true);
                goButton.setVisible(true);
                inputField.setVisible(true);
                inputText.setVisible(true);

                listView.getItems().clear();
                List<String> teme = new ArrayList<>();
                for (Tema tema : service.getTeme()) {
                    teme.add("" + tema.getID() + ": " + tema.getDescriere() + "  " + tema.getDeadline());
                }
                ObservableList<String> obsStudents = FXCollections.observableList(teme);
                initSlider(obsStudents);
                //listView.setItems(obsStudents);
            }
            if (entitySelection.getSelectionModel().isSelected(2)) {
                sliderSelector.setVisible(true);
                f1.setText("Filtrare: student\nSortare: crescator");
                f2.setText("Filtrare: tema \nSortare: descrescator");
                f3.setText("Filtrare: sub 5\nSortare: crescator");
                f1.setVisible(true);
                f2.setVisible(true);
                f3.setVisible(true);
                goButton.setVisible(true);
                inputField.setVisible(true);
                inputText.setVisible(true);

                listView.getItems().clear();
                List<String> note = new ArrayList<>();
                for (Nota nota : service.getNote()) {
                    Student student = null;
                    Tema tema = null;
                    for (Student st : service.getStudents())
                        if (nota.getIdStudent() == st.getID()) {
                            student = st;
                            break;
                        }
                    for (Tema t : service.getTeme())
                        if (t.getID() == nota.getNrTema()) {
                            tema = t;
                            break;
                        }
                    note.add("" + nota.getIdStudent() + "-" + student.getNume() + ": " + nota.getNrTema() + "-" + tema.getDescriere() + ":  " + nota.getNota());
                }
                ObservableList<String> obsStudents = FXCollections.observableList(note);
                initSlider(obsStudents);
                //listView.setItems(obsStudents);

            }
            enableDisable();
        }
        if(Globals.getInstance().accessLevel==1)
        {
            mainPaneReference.setRight(null);
            List<String> note = new ArrayList<>();
            for(Nota nota:service.getNote())
            {
                if(nota.getIdStudent()==Globals.getInstance().nonAdminID)
                {
                    Tema tema = null;
                    for(Tema t:service.getTeme())
                        if(t.getID()==nota.getNrTema())
                        {
                            tema = t;
                            break;
                        }
                    note.add("" + nota.getNrTema() + "-" + tema.getDescriere() + ":  " + nota.getNota());
                }
            }
            listView.setItems(FXCollections.observableList(note));
        }

    }

    public void apply() {
        System.out.println(entitySelection.getSelectionModel().isSelected(1));
        System.out.println(sliderSelector.getValue());
        if(entitySelection.getSelectionModel().isSelected(0) && sliderSelector.getValue()==3)
        {
            listView.getItems().clear();
            List<String> students = new ArrayList<>();
            for(Student st:service.studenti_Fprof_Sgrupa(inputField.getText()))
            {
                students.add("" + st.getID() + ": " + st.getNume() + "  " + st.getGrupa() + "  " + st.getEmail() + "  " + st.getProfLab());
            }
            ObservableList<String> obsStudents= FXCollections.observableList(students);
            initSlider(obsStudents);
            //listView.setItems(obsStudents);
        }
        if(entitySelection.getSelectionModel().isSelected(0) && sliderSelector.getValue()==2)
        {
            listView.getItems().clear();
            List<String> students = new ArrayList<>();
            for(Student st:service.studenti_Femail_Snume(inputField.getText()))
            {
                students.add("" + st.getID() + ": " + st.getNume() + "  " + st.getGrupa() + "  " + st.getEmail() + "  " + st.getProfLab());
            }
            ObservableList<String> obsStudents= FXCollections.observableList(students);
            initSlider(obsStudents);
            //listView.setItems(obsStudents);
        }
        if(entitySelection.getSelectionModel().isSelected(0) && sliderSelector.getValue()==1)
        {
            listView.getItems().clear();
            List<String> students = new ArrayList<>();
            for(Student st:service.studenti_Fnota_Snume(Integer.parseInt(inputField.getText())))
            {
                students.add("" + st.getID() + ": " + st.getNume() + "  " + st.getGrupa() + "  " + st.getEmail() + "  " + st.getProfLab());
            }
            ObservableList<String> obsStudents= FXCollections.observableList(students);
            initSlider(obsStudents);
            //listView.setItems(obsStudents);
        }

        if(entitySelection.getSelectionModel().isSelected(1) && sliderSelector.getValue()==3)
        {
            listView.getItems().clear();
            List<String> teme = new ArrayList<>();
            for(Tema tema:service.teme_Fdeadline_Sdeadline(Integer.parseInt(inputField.getText())))
            {
                teme.add("" + tema.getID() + ": " + tema.getDescriere() + "  " + tema.getDeadline());
            }
            ObservableList<String> obsStudents= FXCollections.observableList(teme);
            initSlider(obsStudents);
            //listView.setItems(obsStudents);
        }
        if(entitySelection.getSelectionModel().isSelected(1) && sliderSelector.getValue()==2)
        {
            listView.getItems().clear();
            List<String> teme = new ArrayList<>();
            for(Tema tema:service.teme_Fdescriere_Sdeadline(inputField.getText()))
            {
                teme.add("" + tema.getID() + ": " + tema.getDescriere() + "  " + tema.getDeadline());
            }
            ObservableList<String> obsStudents= FXCollections.observableList(teme);
            initSlider(obsStudents);
            //listView.setItems(obsStudents);
        }
        if(entitySelection.getSelectionModel().isSelected(1) && sliderSelector.getValue()==1)
        {
            listView.getItems().clear();
            List<String> teme = new ArrayList<>();
            for(Tema tema:service.teme_Fnote_Sdescriere())
            {
                teme.add("" + tema.getID() + ": " + tema.getDescriere() + "  " + tema.getDeadline());
            }
            ObservableList<String> obsStudents= FXCollections.observableList(teme);
            initSlider(obsStudents);
            //listView.setItems(obsStudents);
        }

        if(entitySelection.getSelectionModel().isSelected(2) && sliderSelector.getValue()==3)
        {
            listView.getItems().clear();
            List<String> note = new ArrayList<>();
            for(Nota nota:service.nota_Fstudent_Screscator(Integer.parseInt(inputField.getText())))
            {
                Student student=null;
                Tema tema=null;
                for(Student st:service.getStudents())
                    if(nota.getIdStudent() == st.getID()) {
                        student = st;
                        break;
                    }
                for(Tema t:service.getTeme())
                    if(t.getID()==nota.getNrTema())
                    {
                        tema = t;
                        break;
                    }
                note.add("" + nota.getIdStudent() + "-" + student.getNume() + ": " + nota.getNrTema() + "-" + tema.getDescriere() + ":  " + nota.getNota());
            }
            ObservableList<String> obsStudents= FXCollections.observableList(note);
            initSlider(obsStudents);
            //listView.setItems(obsStudents);
        }
        if(entitySelection.getSelectionModel().isSelected(2) && sliderSelector.getValue()==2)
        {
            listView.getItems().clear();
            List<String> note = new ArrayList<>();
            for(Nota nota:service.nota_Ftema_Sdescrescator(Integer.parseInt(inputField.getText())))
            {
                Student student=null;
                Tema tema=null;
                for(Student st:service.getStudents())
                    if(nota.getIdStudent() == st.getID()) {
                        student = st;
                        break;
                    }
                for(Tema t:service.getTeme())
                    if(t.getID()==nota.getNrTema())
                    {
                        tema = t;
                        break;
                    }
                note.add("" + nota.getIdStudent() + "-" + student.getNume() + ": " + nota.getNrTema() + "-" + tema.getDescriere() + ":  " + nota.getNota());
            }
            ObservableList<String> obsStudents= FXCollections.observableList(note);
            initSlider(obsStudents);
            //listView.setItems(obsStudents);
        }
        if(entitySelection.getSelectionModel().isSelected(2) && sliderSelector.getValue()==1)
        {
            listView.getItems().clear();
            List<String> note = new ArrayList<>();
            for(Nota nota:service.nota_Fsub5_Stema())
            {
                Student student=null;
                Tema tema=null;
                for(Student st:service.getStudents())
                    if(nota.getIdStudent() == st.getID()) {
                        student = st;
                        break;
                    }
                for(Tema t:service.getTeme())
                    if(t.getID()==nota.getNrTema())
                    {
                        tema = t;
                        break;
                    }
                note.add("" + nota.getIdStudent() + "-" + student.getNume() + ": " + nota.getNrTema() + "-" + tema.getDescriere() + ":  " + nota.getNota());
            }
            ObservableList<String> obsStudents= FXCollections.observableList(note);
            initSlider(obsStudents);
            //listView.setItems(obsStudents);
        }
    }

    public void navigateLeftS()
    {
        if(sliderS.getValue()>sliderS.getMin())
            sliderS.setValue(sliderS.getValue()-1);
        pagPopulate(current);
    }

    public void navigateRightS()
    {
        if(sliderS.getValue()<sliderS.getMax())
            sliderS.setValue(sliderS.getValue()+1);
        pagPopulate(current);
    }
}
