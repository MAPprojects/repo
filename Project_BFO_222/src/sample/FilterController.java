package sample;

import Domain.GradeDTO;
import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.xml.bind.annotation.XmlAnyAttribute;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;

public class FilterController{

    @FXML
    ToggleGroup groupEntites,groupFilters,groupGrades;
    @FXML
    CheckBox deadPar,checkName,checkGroup,checkMail,checkID,checkGrade;
    @FXML
    RadioButton stud,lab,grade;
    @FXML
    RadioButton deadMare,deadMic;
    @FXML
    ListView listView;
    @FXML
    TextField nameField,groupField,mailField,temaField,gradeField,deadField;
    @FXML
    RadioButton gradeBig,gradeEqual;

    private Service service;
    public FilterController(){}

    public void clearAllForStudent(){
        nameField.clear();
        groupField.clear();
        mailField.clear();
        checkName.setDisable(true);
        checkGroup.setDisable(true);
        checkMail.setDisable(true);
        nameField.setDisable(true);
        groupField.setDisable(true);
        mailField.setDisable(true);
    }

    public void clearAllForHomeworks(){
        deadMare.setSelected(false);
        deadMic.setSelected(false);
        deadPar.setSelected(false);
        deadPar.setDisable(true);
        deadMare.setDisable(true);
        deadMic.setDisable(true);
        deadField.setDisable(true);
    }

    public void clearAllForGrades(){
        temaField.clear();
        gradeField.clear();
        gradeBig.setSelected(false);
        gradeEqual.setSelected(false);
        checkID.setDisable(true);
        checkGrade.setDisable(true);
        gradeBig.setDisable(true);
        gradeEqual.setDisable(true);
        gradeField.setDisable(true);
        temaField.setDisable(true);
    }

    public void setServices(Service service){
        this.service=service;
        clearAllForGrades();
        clearAllForHomeworks();
        checkName.setDisable(false);
        checkGroup.setDisable(false);
        checkMail.setDisable(false);
        nameField.setDisable(false);
        groupField.setDisable(false);
        mailField.setDisable(false);
        stud.setSelected(true);
        listView.setItems(FXCollections.observableArrayList(service.filterAndSorter(service.fromIterableToList(service.getAllStud()),x->{return true;},(x,y)->{return 1;})));
    }

    @FXML
    public void initForStudents(ActionEvent actionEvent){
        clearAllForGrades();
        clearAllForHomeworks();
        checkName.setDisable(false);
        checkGroup.setDisable(false);
        checkMail.setDisable(false);
        nameField.setDisable(false);
        groupField.setDisable(false);
        mailField.setDisable(false);
        listView.setItems(FXCollections.observableArrayList(service.filterAndSorter(service.fromIterableToList(service.getAllStud()),x->{return true;},(x,y)->{return 1;})));
    }

    @FXML
    public void initForLabs(ActionEvent actionEvent){
        clearAllForGrades();
        clearAllForStudent();
        deadPar.setDisable(false);
        deadMare.setDisable(false);
        deadMic.setDisable(false);
        deadField.setDisable(false);
        listView.setItems(FXCollections.observableArrayList(service.filterAndSorter(service.fromIterableToList(service.getAllLabs()),x->{return true;},(x,y)->{return 1;})));
    }

    @FXML
    public void initForGrades(ActionEvent actionEvent){
        clearAllForStudent();
        clearAllForHomeworks();
        checkID.setDisable(false);
        checkGrade.setDisable(false);
        gradeEqual.setDisable(false);
        gradeBig.setDisable(false);
        gradeField.setDisable(false);
        temaField.setDisable(false);
        listView.setItems(FXCollections.observableArrayList(service.filterAndSorter(service.fromIterableToList(service.getAllGrades()),x->{return true;},(x,y)->{return 1;})));
    }
    @FXML
    public void handleRadioStudents(ActionEvent actionEvent){
        Predicate<Student> predicate= x->{return true;};
        Comparator<Student> comparator=(x,y)->{
            int rez=1;
            if(x.getNume().compareTo(y.getNume())==1)
                rez=1;
            else if(x.getNume().compareTo(y.getNume())==-1)
                rez=-1;
            else if(x.getNume().compareTo(y.getNume())==0){
                rez=x.getCadru().compareTo(y.getCadru());
                if(rez==0){
                    rez=x.getId().compareTo(y.getId());
                }
            }
            return rez;
        };
        ArrayList<Student> list=new ArrayList<>();
        for(Student s:service.getAllStud()){
            list.add(s);
        }
        if(checkName.isSelected()){
            predicate=predicate.and(x->{return x.getNume().contains(nameField.getText());});
        }
        if(checkGroup.isSelected()){
            predicate=predicate.and(x->{return x.getGrupa().equals(groupField.getText());});
        }
        if(checkMail.isSelected()){
            predicate=predicate.and(x->{return x.getEmail().contains(mailField.getText());});
        }
        listView.setItems(FXCollections.observableArrayList(service.filterAndSorter(list,predicate,comparator)));
    }

    @FXML
    public void handleRadioLabs(ActionEvent actionEvent){
        Predicate<Tema> predicate=x->{return true;};
        Comparator<Tema> comparator=(x,y)->{
            int rez=1;
            if(x.getDeadline()<x.getDeadline())
                rez=1;
            else if(x.getDeadline()>x.getDeadline())
                rez=1;
            else if(x.getDeadline()==y.getDeadline()){
                if(x.getDescriere().length()<x.getDescriere().length())
                    rez= 1;
                else if(x.getDescriere().length()>x.getDescriere().length())
                     rez=1;
                else
                    rez=x.getId().compareTo(y.getId());
            }
            return rez;
        };

        ArrayList<Tema> list=new ArrayList<>();
        for(Tema t:service.getAllLabs()){
            list.add(t);
        }

        if(deadMare.isSelected()){
            predicate=predicate.and(x->{return x.getDeadline()>Integer.parseInt(deadField.getText());});
        }
        if(deadMic.isSelected()){
            predicate=predicate.and(x->{return x.getDeadline()<Integer.parseInt(deadField.getText());});
        }
        if(deadPar.isSelected()){
            predicate=predicate.and(x->{return x.getDeadline()%2==0;});
        }

        listView.setItems(FXCollections.observableArrayList(service.filterAndSorter(list,predicate,comparator)));

    }


    @FXML
    public void handleRadioGrades(ActionEvent actionEvent){
        Predicate<Nota> predicate= x->{return true;};
        Comparator<Nota> comparator=(x,y)->{
            int rez=1;
            if(x.getGrade().compareTo(y.getGrade())==1)
                rez=1;
            else if(x.getGrade().compareTo(y.getGrade())==-1)
                rez=-1;
            else if(x.getGrade().compareTo(y.getGrade())==0){
                rez=x.getTema().compareTo(y.getTema());
                if(rez==0)
                    rez=x.getStud().compareTo(y.getStud());
            }
            return rez;
        };

        ArrayList<Nota> list=new ArrayList<>();
        for(Nota n:service.getGrades()){
            list.add(n);
        }

        if(checkID.isSelected()){
            predicate=predicate.and(x->{return x.getTema()<=Integer.parseInt(temaField.getText());});
        }

        if(checkGrade.isSelected() && gradeBig.isSelected()){
            predicate=predicate.and(x->{return x.getGrade()>Integer.parseInt(gradeField.getText());});
        }

        if(checkGrade.isSelected() && gradeEqual.isSelected()){
            predicate=predicate.and(x->{return x.getGrade()==Integer.parseInt(gradeField.getText());});
        }

        listView.setItems(FXCollections.observableArrayList(service.getListWithDTO(service.filterAndSorter(list,predicate,comparator))));
    }
    @FXML
    public void initialize(){
        groupFilters=new ToggleGroup();
        groupEntites=new ToggleGroup();
        groupGrades=new ToggleGroup();
        stud.setToggleGroup(groupEntites);
        lab.setToggleGroup(groupEntites);
        grade.setToggleGroup(groupEntites);
        deadMare.setToggleGroup(groupFilters);
        deadMic.setToggleGroup(groupFilters);
        gradeEqual.setToggleGroup(groupGrades);
        gradeBig.setToggleGroup(groupGrades);
    }
}
