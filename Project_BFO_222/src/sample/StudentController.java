package sample;

import Domain.EntitiesException;
import Domain.Student;
import Domain.User;
import Repository.RepositoryException;
import Service.Service;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.ListEvent;
import utils.Observer;
import Service.ServiceException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentController  implements java.util.Observer
{
    private final static int rowsPerPage = 3;
    @FXML
    TableView<Student> tableView;
    @FXML
    TableColumn<Student,String> id,nume,grupa,email,cadru;
    @FXML
    TextField idField,nameField,groupField,emailField,profesorField,passText;
    @FXML
    Pagination pagination;
    @FXML
    Button addStudentButton,doneButton;
    private String currentUser;

    private AnchorPane pass;

    private Service service;
    private ObservableList<Student> model;

    public StudentController(){}

    public void setServices(Service service) {
        this.service = service;
        model = FXCollections.observableArrayList(this.service.fromIterableToList(service.getAllStud()));
        tableView.setItems(model);

        pagination.setPageFactory(this::createPage);
        setPagination();
    }

    private void setPagination(){
        int size=service.fromIterableToList(service.getAllStud()).size();
        int pageCount=size/rowsPerPage;
        if(size%rowsPerPage>0){
            pageCount=pageCount+1;
        }
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, model.size());
        //tableView.setItems(FXCollections.observableArrayList(service.fromIterableToList(service.getAllStud()).subList(fromIndex, toIndex)));
        tableView.setItems(FXCollections.observableArrayList(service.getStudentSublist(fromIndex,toIndex)));
        if(fromIndex!=toIndex)
            return new BorderPane(tableView);
        else{
            List<Student> list=new ArrayList<>();
            tableView.setItems(FXCollections.observableArrayList(list));
            return new BorderPane(tableView);
        }
    }

    public void setAbilities(){
        idField.setDisable(true);
        nameField.setDisable(true);
        groupField.setDisable(true);
        profesorField.setDisable(true);
        emailField.setDisable(true);
    }


//    public void update(ListEvent<Student> e){
//        model.setAll(StreamSupport.stream(e.getList().spliterator(),false)
//        .collect(Collectors.toList()));
//    }



    public void showDetails(Student othStudent){
        if(othStudent!=null) {
            idField.setText(othStudent.getId().toString());
            nameField.setText(othStudent.getNume());
            groupField.setText(othStudent.getGrupa());
            emailField.setText(othStudent.getEmail());
            profesorField.setText(othStudent.getCadru());
        }
    }

    private Student getStudentFromFields(){
        String id=idField.getText();
        String nume=nameField.getText();
        String grupa=groupField.getText();
        String email=emailField.getText();
        String prof=profesorField.getText();
        Student st=new Student(Integer.parseInt(id),nume,grupa,email,prof);
        return st;
    }

    private static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    private static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    private void clearAll(){
        idField.clear();
        nameField.clear();
        groupField.clear();
        emailField.clear();
        profesorField.clear();
        idField.setDisable(false);
    }

    private void writeTitle(XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement("useri");
        writer.writeCharacters("\n");
    }

    private void writeEntity(User x, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters("\t");
        writer.writeStartElement("user");

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("nume");
        writer.writeCharacters(x.getUsername());
        writer.writeEndElement();

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("parola");

        writer.writeCharacters(x.getPassword());

        writer.writeEndElement();

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("categorie");
        writer.writeCharacters(x.getCategorie());
        writer.writeEndElement();

        writer.writeCharacters("\n\t");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    private int getNrPagini(){
        int size=service.fromIterableToList(service.getAllStud()).size();
        int pageCount=size/rowsPerPage;
        if(size%rowsPerPage>0){
            pageCount=pageCount+1;
        }
        return pageCount;
    }

    private void writeUser(){
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter streamWriter = factory.createXMLStreamWriter(new FileOutputStream("useri.xml"));
            writeTitle(streamWriter);

            service.getAllUsers().forEach((x) -> {
                try {
                    writeEntity(x, streamWriter);
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            });
            streamWriter.writeCharacters("\n");
            streamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleDoneButton(ActionEvent actionEvent){
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(passText.getText().getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            service.addUser(new User(emailField.getText(),hashtext,"student"));
            writeUser();
        }catch (NoSuchAlgorithmException ex){
            System.out.println("Nu s-a putut produce translatarea paroleri..");
        }

        clearAll();
        pass.getScene().getWindow().hide();

    }

public void handleAddButton(ActionEvent actionEvent) throws  IOException{
    String erori="";
    if(idField.getText().equals(""))
        erori+="Nu ati completat id`ul!";

    if(emailField.getText().equals("")){
        erori+="Nu ati completat emailul";
    }
    try{
        int a = Integer.parseInt(groupField.getText());
    }catch (NumberFormatException nf){
        erori+="Nu ati introdus o grupa valida ( 221---227)";
    }
    if(!erori.equals(""))
        showErrorMessage(erori);
    else {
        try {
            Student st = getStudentFromFields();
            service.saveStudent(getStudentFromFields());
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/utils/pass.fxml"));

            fxmlloader.setController(this);
            doneButton = new Button("Done");
            pass = (AnchorPane) fxmlloader.load();
            Scene scene = new Scene(pass);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Parola");
            doneButton.setOnAction(this::handleDoneButton);
            newStage.showAndWait();
            int curr = pagination.getCurrentPageIndex();
            setPagination();
            pagination.setCurrentPageIndex(curr);
            showMessage(Alert.AlertType.INFORMATION, "Adaugare...", "S-a adaugat studentul.");
            String parola = passText.getText();
            service.writeInfo("Parola ta este:'" + parola + "'");
            service.sendEmail(st, "Logare->Parola ta.", false);

        } catch (RepositoryException re) {
            showErrorMessage("Exista deja un student cu acest id!");
            clearAll();
        } catch (EntitiesException e) {
            showErrorMessage("Nu ati introdus date valide. ID NEVALID!");
        } catch (NumberFormatException ne) {
            showErrorMessage("Date invalide! Nu ati completat campurile!");
            clearAll();
        } catch (ServiceException serviceException) {
            showErrorMessage(serviceException.getMessage());
        }
    }
    }

    public void handleDeleteButton(ActionEvent actionEvent){
        Student st=tableView.getSelectionModel().getSelectedItem();
        if(st!=null){
            int id=st.getId();
            service.deleteStudent(id);
            writeUser();
            int curr=pagination.getCurrentPageIndex();
            setPagination();
            pagination.setCurrentPageIndex(curr);
            showMessage(Alert.AlertType.INFORMATION,"Stergere...","Studentul a fost sters.");
            clearAll();

            service.deleteUser(st.getEmail());
            writeUser();

        }
        else
            showMessage(Alert.AlertType.WARNING, "Stergere", "Studentul cu acest ID nu exista");
    }

    public void handleUpdateButton(ActionEvent actionEvent){
        String erori="";
            if(idField.getText().equals(""))
                erori+="Nu ati completat id`ul!";

            if(emailField.getText().equals("")){
                erori+="Nu ati completat emailul";
            }
            if(!erori.equals(""))
                showErrorMessage(erori);
            else{
                try {
                    Student st = getStudentFromFields();
                    service.updateStudent(st);
                    service.updateUser(currentUser,st.getEmail());
                    writeUser();
                    int curr=pagination.getCurrentPageIndex();
                    setPagination();
                    pagination.setCurrentPageIndex(curr);
                    showMessage(Alert.AlertType.INFORMATION, "Modificare...", "Datele au fost modificate.");
                    clearAll();

                } catch (RepositoryException re) {
                    showMessage(Alert.AlertType.ERROR, "Modificare...", "Studentul cu acest ID nu exista!");
                }catch (ServiceException serviceEx){
                    showErrorMessage(serviceEx.getMessage());
                }
            }
    }

    @FXML
    public void initialize() {
    id.setCellValueFactory(new PropertyValueFactory<Student,String >("id"));
    nume.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
    grupa.setCellValueFactory(new PropertyValueFactory<Student,String>("grupa"));
    email.setCellValueFactory(new PropertyValueFactory<Student,String>("email"));
    cadru.setCellValueFactory(new PropertyValueFactory<Student,String>("cadru"));
    tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
        @Override
        public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
            if(newValue!=null)
                currentUser=newValue.getEmail();
            showDetails(newValue);
            idField.setDisable(true);

        }
    });
    }


    @Override
    public void update(Observable o, Object arg) {
        model.setAll(service.fromIterableToList(service.getAllStud()));

    }
}
