package com.company.GUI;

import com.company.Domain.*;
import com.company.Service.Service;
import com.company.Utility.Event;
import com.company.Utility.Observable;
import com.company.Utility.Observer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TablesCtrl {
    @FXML
    public TableColumn<Student,Integer> idStudentC;
    @FXML
    public TableColumn<Student,String> numeStudentC;
    @FXML
    public TableColumn<Student,Integer> grupaStudentC;
    @FXML
    public TableColumn<Student,String> emailStudentC;
    @FXML
    public TableColumn<Student,String> profStudentC;
    @FXML
    public TableView<Student> studentTable;
    @FXML
    public TableView<Tema> temaTable;
    @FXML
    public TableView<ExtendedNota> notaTable;
    @FXML
    public TitledPane studentsTab;
    @FXML
    public Accordion tableTabs;
    @FXML
    public TableColumn descTemaC;
    @FXML
    public TableColumn deadlineTemaC;
    public TableColumn studentNameC;
    public TableColumn temaDescC;
    public TableColumn notaC;
    public Slider sliderS;
    public Button prevS;
    public Button nextS;
    public Slider sliderT;
    public Button prevT;
    public Button nextT;
    public Slider sliderN;
    public Button prevN;
    public Button nextN;
    Service service;

    private int lastPageS =1;
    private int lastPageT =1;
    private int lastPageN =1;

    public MainWindowCtrl mainWindowCtrl;


    public void setParentCtrl(MainWindowCtrl mwc)
    {
        this.mainWindowCtrl = mwc;
    }

    public void getStudentControlls(MouseEvent mouseEvent) {
         mainWindowCtrl.setStudentControlls();
         if(!tableTabs.getPanes().get(0).isExpanded())
             tableTabs.getPanes().get(0).setExpanded(true);
    }

    public void getTemaControlls(MouseEvent mouseEvent){
        mainWindowCtrl.setTemeControlls();
        if(!tableTabs.getPanes().get(1).isExpanded())
            tableTabs.getPanes().get(1).setExpanded(true);
    }

    public void getNotaControlls(MouseEvent mouseEvent)
    {
        mainWindowCtrl.setNotaControlls();
        if(!tableTabs.getPanes().get(2).isExpanded())
            tableTabs.getPanes().get(2).setExpanded(true);
    }

    private class Triggerer<E> implements Observer<E> {

        @Override
        public void notifyOnEvent(Event<E> event) {
            populate();
        }
    }

    public void setService(Service service)
    {
        this.service=service;
    }

    public void initialize()
    {
        //idStudentC.setCellValueFactory(new PropertyValueFactory<Student,Integer>("idStudent"));
        numeStudentC.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
        grupaStudentC.setCellValueFactory(new PropertyValueFactory<Student,Integer>("grupa"));
        emailStudentC.setCellValueFactory(new PropertyValueFactory<Student,String>("email"));
        profStudentC.setCellValueFactory(new PropertyValueFactory<Student,String>("profLab"));
        descTemaC.setCellValueFactory(new PropertyValueFactory<Tema,String>("descriere"));
        deadlineTemaC.setCellValueFactory(new PropertyValueFactory<Tema,Integer>("deadline"));
        studentNameC.setCellValueFactory(new PropertyValueFactory<ExtendedNota,String>("nume"));
        temaDescC.setCellValueFactory(new PropertyValueFactory<ExtendedNota,String>("tema"));
        //notaC.setCellValueFactory(new PropertyValueFactory<ExtendedNota,Integer>("nota.getNota()"));
        notaC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ExtendedNota, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ExtendedNota, Integer> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                ObservableValue<Integer> value = new ReadOnlyObjectWrapper<>(p.getValue().getNota().getNota());
                return value;
            }
        });

        stylise();
        System.out.println(sliderS.getParent().getClass().getName());

        if(Globals.getInstance().accessLevel==1)
        {
            tableTabs.getPanes().get(2).setDisable(true);
        }


        tableTabs.setExpandedPane(studentsTab);


    }

    public void stylise()
    {
        tableTabs.getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        sliderS.getParent().getStyleClass().add("grid");
        sliderN.getParent().getStyleClass().add("grid");
        sliderT.getParent().getStyleClass().add("grid");
        studentTable.getParent().getStyleClass().add("pane");
    }

    public void setup()
    {
        service.addObserverOnStudents(new Triggerer<Student>());
        service.addObserverOnTeme(new Triggerer<Tema>());
        service.addObserverOnNote(new Triggerer<Nota>());
    }

    public void initSliders()
    {
        double heightS = studentTable.getPrefHeight();
        studentTable.setFixedCellSize(23);
        //studentTable.setFixedCellSize(24);

        double cellSizeS = studentTable.getFixedCellSize();
        int entitiesOnPageS = (int)((heightS-cellSizeS)/cellSizeS);
        //System.out.println(entitiesOnPageS);
        List<Student> students = new ArrayList<>();
        students.addAll((Collection<? extends Student>) service.getStudents());
        sliderS.setMin(1);
        sliderS.setMax(((students.size()-1)/entitiesOnPageS)+1);
        sliderS.setMajorTickUnit(1);
        sliderS.setValue(lastPageS);
        sliderS.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                lastPageS = (int) sliderS.getValue();
                pagPopulateStudent();

            }
        });
        if(((Collection<? extends Student>) service.getStudents()).size() < entitiesOnPageS+1) {
            sliderS.setDisable(true);
            prevS.setDisable(true);
            nextS.setDisable(true);
        }
        else
        {
            sliderS.setDisable(false);
            prevS.setDisable(false);
            nextS.setDisable(false);
        }

        double heightT = temaTable.getPrefHeight();
        temaTable.setFixedCellSize(23);
        double cellSizeT = temaTable.getFixedCellSize();
        int entitiesOnPageT = (int)((heightT-cellSizeT)/cellSizeT);
        //System.out.println(entitiesOnPageT);
        List<Tema> teme = new ArrayList<>();
        teme.addAll((Collection<? extends Tema>) service.getTeme());
        sliderT.setMin(1);
        sliderT.setMax(((teme.size()-1)/entitiesOnPageT)+1);
        sliderT.setMajorTickUnit(1);
        sliderT.setValue(lastPageT);
        sliderT.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                lastPageT = (int) sliderT.getValue();
                pagPopulateTema();
            }
        });
        if(((Collection<? extends Tema>) service.getTeme()).size() < entitiesOnPageT+1) {
            sliderT.setDisable(true);
            prevT.setDisable(true);
            nextT.setDisable(true);
        }
        else
        {
            sliderT.setDisable(false);
            prevT.setDisable(false);
            nextT.setDisable(false);
        }

        double heightN = notaTable.getPrefHeight();
        notaTable.setFixedCellSize(23);
        double cellSizeN = notaTable.getFixedCellSize();
        int entitiesOnPageN = (int)((heightN-cellSizeN)/cellSizeN);
        //System.out.println(entitiesOnPageN);
        List<Nota> note = new ArrayList<>();
        note.addAll((Collection<? extends Nota>) service.getNote());
        sliderN.setMin(1);
        sliderN.setMax(((note.size()-1)/entitiesOnPageN)+1);
        sliderN.setMajorTickUnit(1);
        sliderN.setValue(lastPageN);
        sliderN.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                lastPageN = (int) sliderN.getValue();
                pagPopulateNota();
            }
        });
        if(((Collection<? extends Nota>) service.getNote()).size() < entitiesOnPageN+1) {
            sliderN.setDisable(true);
            prevN.setDisable(true);
            nextN.setDisable(true);
        }
        else
        {
            sliderN.setDisable(false);
            prevN.setDisable(false);
            nextN.setDisable(false);
        }
    }

    public void navigateLeftS()
    {
        if(sliderS.getValue()>sliderS.getMin())
            sliderS.setValue(sliderS.getValue()-1);
        pagPopulateStudent();
    }

    public void navigateRightS()
    {
        if(sliderS.getValue()<sliderS.getMax())
            sliderS.setValue(sliderS.getValue()+1);
        pagPopulateStudent();
    }

    public void navigateLeftT()
    {
        if(sliderT.getValue()>sliderT.getMin())
            sliderT.setValue(sliderT.getValue()-1);
        pagPopulateTema();
    }

    public void navigateRightT()
    {
        if(sliderT.getValue()<sliderT.getMax())
            sliderT.setValue(sliderT.getValue()+1);
        pagPopulateTema();
    }

    public void navigateLeftN()
    {
        if(sliderN.getValue()>sliderN.getMin())
            sliderN.setValue(sliderN.getValue()-1);
        pagPopulateNota();
    }

    public void navigateRightN()
    {
        if(sliderN.getValue()<sliderN.getMax())
            sliderN.setValue(sliderN.getValue()+1);
        pagPopulateNota();
    }

    public void pagPopulateStudent()
    {
        //out.println((int)sliderS.getValue());
        List<Student> students = new ArrayList<>();
        students.addAll((Collection<? extends Student>) service.getStudents());
        ArrayList<Student> pageStudents = new ArrayList<>();
        int studentsToTake = (int) ((studentTable.getPrefHeight()-studentTable.getFixedCellSize())/studentTable.getFixedCellSize());
        for(int i = ((int)sliderS.getValue()-1)*studentsToTake, init = i; i<init+studentsToTake && i<students.size(); i++)
        {
            pageStudents.add(students.get(i));
        }
        ObservableList<Student> obsStudents= FXCollections.observableList(pageStudents);
        studentTable.setItems(obsStudents);

        prevS.setText(String.valueOf((int)sliderS.getValue()-1)+"<<");
        nextS.setText(">>"+String.valueOf((int)sliderS.getValue()+1));

        if(sliderS.getMax()==sliderS.getValue())
            nextS.setText("----");
        if(sliderS.getMin()==sliderS.getValue())
            prevS.setText("----");

//        System.out.println(height);
//        System.out.println(cellSize);
//        System.out.println(entitiesOnPage);
    }

    public void pagPopulateTema()
    {
        //System.out.println((int)sliderS.getValue());
        List<Tema> teme = new ArrayList<>();
        teme.addAll((Collection<? extends Tema>) service.getTeme());
        ArrayList<Tema> pageTeme = new ArrayList<>();
        int temeToTake = (int) ((temaTable.getPrefHeight()-temaTable.getFixedCellSize())/temaTable.getFixedCellSize());
        for(int i = ((int)sliderT.getValue()-1)*temeToTake, init = i; i<init+temeToTake && i<teme.size(); i++)
        {
            pageTeme.add(teme.get(i));
        }
        ObservableList<Tema> obsStudents= FXCollections.observableList(pageTeme);
        temaTable.setItems(obsStudents);

        prevT.setText(String.valueOf((int)sliderT.getValue()-1)+"<<");
        nextT.setText(">>"+String.valueOf((int)sliderT.getValue()+1));

        if(sliderT.getMax()==sliderT.getValue())
            nextT.setText("----");
        if(sliderT.getMin()==sliderT.getValue())
            prevT.setText("----");

//        System.out.println(height);
//        System.out.println(cellSize);
//        System.out.println(entitiesOnPage);
    }

    public void pagPopulateNota()
    {
        //System.out.println((int)sliderS.getValue());
        ExtendedNota.service = service;
        List<ExtendedNota> note = new ArrayList<>();
        ArrayList<Nota> listNote = new ArrayList<>();
        listNote.addAll((Collection<? extends Nota>) service.getNote());
        int noteToTake = (int) ((notaTable.getPrefHeight()-notaTable.getFixedCellSize())/notaTable.getFixedCellSize());
        for(int i = ((int)sliderN.getValue()-1)*noteToTake, init = i; i<init+noteToTake && i<listNote.size(); i++)
        {
            note.add(new ExtendedNota(listNote.get(i)));
        }
        ObservableList<ExtendedNota> obsNote = FXCollections.observableList(note);
        notaTable.setItems(obsNote);

        prevN.setText(String.valueOf((int)sliderN.getValue()-1)+"<<");
        nextN.setText(">>"+String.valueOf((int)sliderN.getValue()+1));

        if(sliderN.getMax()==sliderN.getValue())
            nextN.setText("----");
        if(sliderN.getMin()==sliderN.getValue())
            prevN.setText("----");

//        System.out.println(height);
//        System.out.println(cellSize);
//        System.out.println(entitiesOnPage);
    }

    public void populate()
    {
        initSliders();
        pagPopulateStudent();
        pagPopulateTema();
        pagPopulateNota();


//        List<Student> students = new ArrayList<>();
//        students.addAll((Collection<? extends Student>) service.getStudents());
//        ObservableList<Student> obsStudents= FXCollections.observableList(students);
//        studentTable.setItems(obsStudents);
//
//        List<Tema> teme = new ArrayList<>();
//        teme.addAll((Collection<? extends Tema>) service.getTeme());
//        ObservableList<Tema> obsTeme= FXCollections.observableList(teme);
//        temaTable.setItems(obsTeme);
//
//        ExtendedNota.service = service;
//
//        List<ExtendedNota> note = new ArrayList<>();
//        for(Nota n:service.getNote())
//        {
//            note.add(new ExtendedNota(n));
//        }
//        ObservableList<ExtendedNota> obsNote = FXCollections.observableList(note);
//        notaTable.setItems(obsNote);
    }



}
