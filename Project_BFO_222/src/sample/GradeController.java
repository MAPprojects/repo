package sample;

import Domain.*;
import Repository.RepositoryException;
import Service.Service;
import com.itextpdf.layout.element.Table;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Service.ServiceException;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;

import java.awt.event.KeyEvent;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GradeController implements Observer{
    private final static int rowsPerPage = 3;
    private Service service;
    private ObservableList<GradeDTO> model3;
    private ObservableList<String> modelComboStud,modelComboTeme;
    private Integer idNota;
    @FXML
    Pagination pagination;
    @FXML
    ComboBox comboStudenti,comboLabs,comboGrades,labCombo;;

    @FXML
    TableView<GradeDTO> tableGrades;

    @FXML
    Slider groupSlider,weekSlider;

    @FXML
    TableColumn<GradeDTO,String> student,tema,id;

    @FXML TableColumn<GradeDTO,Integer> nota;

    @FXML Button addGrade,deleteGrade,updateGrade;

    private int currentValueWeek=0,currentValueGroup=0;
    public GradeController(){}

    public void populateIntegers(ArrayList<Integer> arr){
        for(int i=1;i<=10;i++)
            arr.add(i);
    }
    public ArrayList<String> studentiForCombo(){
        ArrayList<String> studenti=new ArrayList<>();
        for(Student s:service.getAllStud()){
            studenti.add(s.getNume()+" "+s.getGrupa());
        }
        return studenti;
    }

    public void setAbilities(String user){
        tableGrades.setEditable(false);
        //tableGrades.setItems(FXCollections.observableArrayList(filter(service.fromIterableToList(service.getAllGrades()),x->(x.getEmail().equals(user)))));
        List<GradeDTO> filter = filtreazaPentruMine(service.fromIterableToList(service.getAllGrades()),user);
        System.out.println(filter.size());
        model3.setAll(FXCollections.observableArrayList(filter));

        setPagination();

        ArrayList<String> teme=new ArrayList<>();
        for(Tema t:service.getAllLabs()){
            if(!teme.contains(t.getDescriere()))
                teme.add(t.getDescriere());
        }
        labCombo.setItems(FXCollections.observableArrayList(teme));
        labCombo.setDisable(true);
        groupSlider.setDisable(true);
        comboLabs.setDisable(true);
        comboStudenti.setDisable(true);
        comboGrades.setDisable(true);
        addGrade.setDisable(true);
        deleteGrade.setDisable(true);
        updateGrade.setDisable(true);
        weekSlider.setDisable(true);


    }

    private List<GradeDTO> filtreazaPentruMine(List<GradeDTO> list,String em){
        ArrayList<GradeDTO> respect=new ArrayList<>();
        for(GradeDTO gr:list){
            if(gr.getEmail().equals(em))
                respect.add(gr);
        }
        return respect;
    }

    private <E>List<E> filter(List<E> list, Predicate<E> pred){
        return list.stream().filter(pred).collect(Collectors.toList());
    }

    public ArrayList<String> labsForCombo(){
        ArrayList<String> teme=new ArrayList<>();
        for(Tema t:service.getAllLabs()){
            teme.add(t.getDescriere());
        }
        return teme;
    }

    private ArrayList<String> existentLabs(){
        ArrayList<String> teme=new ArrayList<>();
        for(GradeDTO g:service.getAllGrades()){
            if(!teme.contains(g.getNumeTema()))
                teme.add(g.getNumeTema());
        }
        return teme;
    }

    private ArrayList<String> existentLabs2(){
        ArrayList<String> teme=new ArrayList<>();
        for(Tema t:service.getAllLabs()){
            if(!teme.contains(t.getDescriere()))
                teme.add(t.getDescriere());
        }
        return teme;
    }


    public void setServices(Service service){
        this.service=service;
        modelComboStud=FXCollections.observableArrayList(studentiForCombo());
        comboStudenti.setItems(modelComboStud);
        modelComboTeme=FXCollections.observableArrayList(labsForCombo());
        comboLabs.setItems(modelComboTeme);
        labCombo.setItems(FXCollections.observableArrayList(existentLabs()));
        comboLabs.setItems(FXCollections.observableArrayList(existentLabs2()));
        model3=FXCollections.observableArrayList(service.fromIterableToList(service.getAllGrades()));
        tableGrades.setItems(model3);
        pagination.setPageFactory(this::createPage);
        setPagination();
    }


    public void setPagination(){
        int size=model3.size();
        int pageCount=size/rowsPerPage;
        if(size%rowsPerPage>0){
            pageCount=pageCount+1;
        }
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }


    private Node createPage(int pageIndex) {
        int size=model3.size();
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, size);

        tableGrades.setItems(FXCollections.observableArrayList(model3.subList(fromIndex,toIndex)));
        //tableGrades.setItems(FXCollections.observableArrayList(service.getGradeSublist(fromIndex,toIndex)));

        if(fromIndex!=toIndex)
            return new BorderPane(tableGrades);
        else{
            List<GradeDTO> list=new ArrayList<>();
            tableGrades.setItems(FXCollections.observableArrayList(list));
            return new BorderPane(tableGrades);
        }

    }

    private static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    private Comparator<Nota> comparator(){
        Comparator<Nota> comparator = (x, y) -> {
            int rez = 1;
            if (x.getGrade().compareTo(y.getGrade()) == 1)
                rez = 1;
            else if (x.getGrade().compareTo(y.getGrade()) == -1)
                rez = -1;
            else if (x.getGrade().compareTo(y.getGrade()) == 0) {
                rez = x.getTema().compareTo(y.getTema());
                if (rez == 0)
                    rez = x.getStud().compareTo(y.getStud());
            }
            return rez;
        };
        return comparator;
    }

    private void forFilters(ObservableValue arg1,Object arg2,Object arg3) {
        int nr = (int) weekSlider.getValue();
        int nr2 = (int) groupSlider.getValue();
        Predicate<GradeDTO> predicate = x -> {
            return true;
        };
        ArrayList<GradeDTO> list = new ArrayList<>();
        for (GradeDTO n : service.getAllGrades()) {
            list.add(n);
        }
        if (nr == 0)
            currentValueWeek = nr;
        if (nr != currentValueWeek) {
            currentValueWeek = nr;
            predicate = predicate = x -> {
                int id = service.labGetIdByDesc(x.getNumeTema());
                for (Tema t : service.getAllLabs()) {
                    if (t.getId() == id && t.getDeadline() == nr)
                        return true;
                }
                return false;
            };
        }
        if (nr2 != currentValueGroup) {
            if(nr2==220)
                predicate=predicate.and(x->{return true;});
            else {
                predicate = predicate.and(x -> {
                    //String[] str = x.getNumeStud().split("[ ]");
                    return Integer.parseInt(x.getGrupa()) == nr2;
                });
            }
        }
        if(!labCombo.getEditor().getText().equals(""))
            predicate=predicate.and(x->{return x.getNumeTema().startsWith(labCombo.getEditor().getText());});
        List<GradeDTO> filtered=filter(list,predicate);
        model3.setAll(FXCollections.observableArrayList(filtered));
        setPagination();
        //tableGrades.setItems(model3);
    }

    private void initSliders(){
        weekSlider.valueProperty().addListener((ChangeListener)this::forFilters);
        groupSlider.valueProperty().addListener((ChangeListener)this::forFilters);
    }

    private void forComboLabs(ObservableValue arg1,Object arg2,Object arg3){
            Predicate<String> pred=x->{return true;};
            if(!comboLabs.getEditor().getText().equals("")) {
                if (comboLabs.getSelectionModel().getSelectedIndex() < 0) {
                    pred = x -> {
                        return x.startsWith(comboLabs.getEditor().getText());
                    };
                    comboLabs.setItems(FXCollections.observableArrayList(filter(labsForCombo(), pred)));
                }
            }
            else {
                comboLabs.getSelectionModel().clearSelection();
                comboLabs.setItems(FXCollections.observableArrayList(filter(labsForCombo(), pred)));
            }
    }

    private void forLabCombo(ObservableValue arg1,Object arg2,Object arg3){

        Predicate<String> pred=x->{return true;};
        if(!labCombo.getEditor().getText().equals("")) {
            if (labCombo.getSelectionModel().getSelectedIndex() < 0) {
                pred = x -> {
                    return x.startsWith(labCombo.getEditor().getText());
                };
                labCombo.setItems(FXCollections.observableArrayList(filter(existentLabs(), pred)));
            }
        }
        else {
            labCombo.getSelectionModel().clearSelection();
            labCombo.setItems(FXCollections.observableArrayList(filter(existentLabs(), pred)));
        }
    }

    private void forComboStudenti(ObservableValue observableValue,Object s1,Object s2){
        Predicate<String> pred=x->{return true;};
        if(!comboStudenti.getEditor().getText().equals("")){
            if(comboStudenti.getSelectionModel().getSelectedIndex()<0) {
                pred = x -> {
                    return x.startsWith(comboStudenti.getEditor().getText());
                };
                comboStudenti.setItems(FXCollections.observableArrayList(filter(studentiForCombo(), pred)));
            }
        }
        else {
            comboStudenti.getSelectionModel().clearSelection();
            comboStudenti.setItems(FXCollections.observableArrayList(filter(studentiForCombo(), pred)));
        }
    }

    private void initCombos(){
        comboLabs.getEditor().textProperty().addListener((ChangeListener)this::forComboLabs);
        comboStudenti.getEditor().textProperty().addListener((ChangeListener)this::forComboStudenti);
        labCombo.getEditor().textProperty().addListener((ChangeListener)this::forLabCombo);
        labCombo.getEditor().textProperty().addListener((ChangeListener)this::forFilters);
    }


    private static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    private void clearAll(){
        comboStudenti.getEditor().setText("");
        comboLabs.getEditor().setText("");
        comboGrades.getEditor().setText("");
        comboStudenti.setDisable(false);
        comboLabs.setDisable(false);
    }

    private Nota getGradeFromFields(){
        String[] fields=comboStudenti.getEditor().getText().split("[ ]");
        Nota n;
        if(comboStudenti.getEditor().getText().equals("") || comboLabs.getEditor().getText().equals("")) {
            clearAll();
            throw new ServiceException("Date nevalide!");
        }
        Integer randId,min=1,max=service.getMaxId();
        Random rand=new Random();
        String name=fields[0];
        for(int i=1;i<fields.length-1;i++){
            name=name+" "+fields[i];
        }
        if(comboGrades.getSelectionModel().getSelectedItem()==null || comboGrades.getEditor().getText().equals(""))
            comboGrades.getEditor().setText("0");
        n= new Nota(1, service.studentGetIdByName(name, fields[fields.length -1]), service.labGetIdByDesc(comboLabs.getEditor().getText()), Integer.parseInt(comboGrades.getEditor().getText()));
        while(service.existingGradeId(n)) {
//            if(service.existingGrade(n)) {
//                clearAll();
//                throw new ServiceException("Ati dat deja o nota acestui student!");
//            }
            randId=max+rand.nextInt(max+1);
            n= new Nota(randId,service.studentGetIdByName(name,fields[fields.length-1]), service.labGetIdByDesc(comboLabs.getEditor().getText()), Integer.parseInt(comboGrades.getEditor().getText()));
        }
        return n;
    }

    @FXML
    public void handleAddGrade(ActionEvent actionEvent){
        try {
            service.saveGrade(getGradeFromFields());
            int curr=pagination.getCurrentPageIndex();
            model3=FXCollections.observableArrayList(service.fromIterableToList(service.getAllGrades()));
            setPagination();
            pagination.setCurrentPageIndex(curr);
            showMessage(Alert.AlertType.INFORMATION, "Adaugare...", "S-a adaugat nota in catalog.");
            clearAll();
        }catch (NumberFormatException ne){
            showErrorMessage("Date invalide!");
            clearAll();
        }catch (ServiceException serviceException ){
            showErrorMessage(serviceException.getMessage());
        }
    }

    @FXML
    public void handleDeleteGrade(ActionEvent actionEvent){
        GradeDTO g=tableGrades.getSelectionModel().getSelectedItem();
        if(g!=null){
            int id=g.getId();
            service.deleteGrade(id);
            model3=FXCollections.observableArrayList(service.fromIterableToList(service.getAllGrades()));
            int curr=pagination.getCurrentPageIndex();
            setPagination();
            pagination.setCurrentPageIndex(curr);
            showMessage(Alert.AlertType.INFORMATION,"Stergere...","Nota a fost stearsa.");
            clearAll();
        }
        else
            showErrorMessage("Nu ati selectat nimic!");
    }

    @FXML
    public void handleUpdateGrade(ActionEvent actionEvent){
        if(tableGrades.getSelectionModel().getSelectedIndex()<0)
            showErrorMessage("Nu ati selectat nota!");
        else{
            try {
                GradeDTO g=tableGrades.getSelectionModel().getSelectedItem();
                g.setGrade(Integer.parseInt(comboGrades.getEditor().getText()));
                //g.setGrade(comboGrades.getEditor().getText());
                service.updateGrade(service.getNotaFromDTO(g));
                model3=FXCollections.observableArrayList(service.fromIterableToList(service.getAllGrades()));
                int curr=pagination.getCurrentPageIndex();
                setPagination();
                pagination.setCurrentPageIndex(curr);
                showMessage(Alert.AlertType.INFORMATION, "Modificare...", "Datele au fost modificate.");
                clearAll();
            } catch (RepositoryException re) {
                showErrorMessage(re.getMessage());
            }
        }
    }

    private void showDetails(GradeDTO gr){
        if(gr!=null) {
            comboStudenti.getEditor().setText(gr.getNumeStud());
            comboLabs.getEditor().setText(gr.getNumeTema());
            comboGrades.getEditor().setText("" + gr.getGrade());
            idNota = gr.getId();
        }
    }
    @FXML
    public void changeGradeCellEvent(TableColumn.CellEditEvent editEvent){
        GradeDTO selected=tableGrades.getSelectionModel().getSelectedItem();
        selected.setGrade(Integer.parseInt(editEvent.getNewValue().toString()));
        service.updateGrade(service.getNotaFromDTO(selected));
        model3=FXCollections.observableArrayList(service.fromIterableToList(service.getAllGrades()));
        //int curr=pagination.getCurrentPageIndex();
        //setPagination();
       // pagination.setCurrentPageIndex(curr);
//        double curentSliderValue=weekSlider.getValue();
//        weekSlider.setValue(weekSlider.getMin());
//        weekSlider.setValue(curentSliderValue);

    }

    @FXML
    public void initialize(){
        initSliders();
        initCombos();
        comboStudenti.setEditable(true);
        labCombo.setEditable(true);
        comboLabs.setEditable(true);
        comboGrades.setEditable(true);


        ArrayList<Integer> values=new ArrayList<>();
        populateIntegers(values);
        comboGrades.setItems(FXCollections.observableArrayList(values));
        student.setCellValueFactory(new PropertyValueFactory<GradeDTO,String>("numeStud"));
        tema.setCellValueFactory(new PropertyValueFactory<GradeDTO,String>("numeTema"));
        nota.setCellValueFactory(new PropertyValueFactory<GradeDTO,Integer>("grade"));
        id.setCellValueFactory(new PropertyValueFactory<GradeDTO,String>("id"));
        tableGrades.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GradeDTO>() {
            @Override
            public void changed(ObservableValue<? extends GradeDTO> observable, GradeDTO oldValue, GradeDTO newValue) {
                showDetails(newValue);
                comboStudenti.setDisable(true);
                comboLabs.setDisable(true);
            }
        });
        tableGrades.setEditable(true);
        nota.setCellFactory(TextFieldTableCell.<GradeDTO, Integer>forTableColumn(new IntegerStringConverter()));

    }

    @Override
    public void update(Observable o, Object arg) {
        model3.setAll(service.fromIterableToList(service.getAllGrades()));
        modelComboStud.setAll(studentiForCombo());
        modelComboTeme.setAll(labsForCombo());
        labCombo.setItems(FXCollections.observableArrayList(existentLabs()));
        comboLabs.setItems(FXCollections.observableArrayList(existentLabs2()));
    }
}
