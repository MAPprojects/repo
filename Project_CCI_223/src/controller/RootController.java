package controller;

import domain.*;
import exceptii.EntityNotFoundException;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.*;
import sablonObserver.Observer;
import service.Service;
import service.ServiceUsers;
import validator.*;
import Main.StartApplication;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.control.Tab.CLOSED_EVENT;

public class RootController implements Observer{

    @FXML
    protected TabPane tabPane;
    @FXML
    protected AnchorPane anchorPaneTeme;
    @FXML
    protected AnchorPane anchorPaneNote;
    @FXML
    protected AnchorPane anchorPaneStatistici;
    @FXML
    protected Label labelSaptamana;

    protected User currentUser;
    protected HostServices hostServices;
    protected Stage rootStage;
    protected Integer saptamana;
    protected Service service;
    protected ServiceUsers serviceUsers;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
//        System.out.println(hostServices.toString());
    }

    public void setServiceUsers(ServiceUsers serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public void setRootStage(Stage stage){
        this.rootStage=stage;
    }

    public void setSaptamana(Integer saptamana){
        this.saptamana=saptamana;

        Validator<Student> valSt=new StudentValidator();
        Validator<TemaLaborator> valTeme=new TemeLabValidator();
        Validator<Nota> valNota=new NotaValidator();
        Validator<Intarziere> valIntarziere=new IntarziereValidator();

        //Repository<Student,Integer> repoStudenti=new StudentFileRepository(valSt,"D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\repository\\Studenti");
        //Repository<TemaLaborator,Integer> repoTeme=new TemeLabFileRepository(valTeme,"D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\repository\\Teme");
        //NotaRepository repoNote=new NotaFileRepository(valNota,"D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\repository\\Catalog");

        Repository<Student,Integer> repoStudenti=new StudentXMLFileRepository(valSt,"D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\repository\\studenti.xml");
        Repository<TemaLaborator,Integer> repoTeme=new TemaLaboratorXMLFileRepository(valTeme,"D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\repository\\teme.xml");
        NotaRepository repoNote=new NotaXMLFileRepository(valNota,"D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\repository\\catalog.xml");
        Repository<Intarziere,Integer> repoIntarzieri=new IntarziereXMLFileRepository(valIntarziere,"D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\repository\\intarzieri.xml");


        service=new Service(repoStudenti,repoTeme,repoNote,repoIntarzieri,saptamana);
        labelSaptamana.setText("Saptamana curenta: "+saptamana.toString());

        initTemePane(service,anchorPaneTeme);
        initNotaPane(service,anchorPaneNote);
        initStatisticiPane(service,anchorPaneStatistici);
    }


    public RootController() {
    }

    @FXML
    public void handleClose(){
        rootStage.close();
    }

    @FXML
    public void initialize(){
    }

    protected void initNotaPane(Service service,AnchorPane anchorPaneNote) {
        try{
            FXMLLoader loaderNote=new FXMLLoader();
            loaderNote.setLocation(StartApplication.class.getResource("/view_FXML/nota.fxml"));
            AnchorPane root=(AnchorPane) loaderNote.load();
            ControllerNote controllerNote=loaderNote.getController();
            try {
                Optional<User> userOptional=serviceUsers.getUser(currentUser.getUsername());
                if (userOptional.isPresent())
                    controllerNote.setCurrentUser(userOptional.get());
            } catch (EntityNotFoundException e) {
                handleIesireCont();
            }
            controllerNote.setService(service);
            service.addObserver(controllerNote);

            anchorPaneNote.getChildren().setAll(root);
            AnchorPane.setRightAnchor(root,5d);
            AnchorPane.setLeftAnchor(root,5d);
            AnchorPane.setTopAnchor(root,5d);
            AnchorPane.setBottomAnchor(root,5d);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initTemePane(Service service, AnchorPane anchorPaneTeme){
        try{
            FXMLLoader loaderTeme=new FXMLLoader();
            loaderTeme.setLocation(StartApplication.class.getResource("/view_FXML/tema.fxml"));
            AnchorPane root=(AnchorPane) loaderTeme.load();
            ControllerTemeLaborator controllerTemeLaborator=loaderTeme.getController();
            controllerTemeLaborator.setService(service);
            service.addObserver(controllerTemeLaborator);
            anchorPaneTeme.getChildren().add(root);
            AnchorPane.setRightAnchor(root,5d);
            AnchorPane.setLeftAnchor(root,5d);
            AnchorPane.setTopAnchor(root,5d);
            AnchorPane.setBottomAnchor(root,5d);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initStatisticiPane(Service service,AnchorPane anchorPaneStatistici){
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(StartApplication.class.getResource("/view_FXML/statistici.fxml"));
            AnchorPane root=(AnchorPane) loader.load();

            ControllerStatistici ctrlStatistici=loader.getController();
            ctrlStatistici.setHostServices(hostServices);
            ctrlStatistici.setUser(currentUser);
            service.addObserver(ctrlStatistici);
            ctrlStatistici.setStage(rootStage);

            ctrlStatistici.setService(service);


            anchorPaneStatistici.getChildren().removeAll();
            anchorPaneStatistici.getChildren().addAll(root);
            AnchorPane.setRightAnchor(root,5d);
            AnchorPane.setLeftAnchor(root,5d);
            AnchorPane.setTopAnchor(root,5d);
            AnchorPane.setBottomAnchor(root,5d);

        }catch (IOException e){}

    }

    public void handleSetariCont(){
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view_FXML/setariCont.fxml"));
            BorderPane rootPane=loader.load();

            Stage stageCont=new Stage();
            Scene scene=new Scene(rootPane);
            stageCont.setScene(scene);
            stageCont.setTitle("Setari cont");

            ControllerSetariCont controllerSetariCont=loader.getController();
            try {
                Optional<User> userOptional=serviceUsers.getUser(currentUser.getUsername());
                if (userOptional.isPresent())
                    controllerSetariCont.setCurrentUser(userOptional.get());
                else{
                    handleIesireCont();
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
            controllerSetariCont.setServiceUsers(serviceUsers);
            controllerSetariCont.setRootStage(rootStage);
            controllerSetariCont.setStage(stageCont);
            controllerSetariCont.setHostServices(hostServices);

            stageCont.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleIesireCont(){
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/log.fxml"));
        try{
            BorderPane root=loader.load();
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setTitle("Sistem de gestiune");

            ControllerLog controllerLog=loader.getController();
            controllerLog.setHostServices(hostServices);
            controllerLog.setServiceUsers(serviceUsers);
            controllerLog.setStage(stage);
            controllerLog.initLogin();
            rootStage.close();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSetareSaptamana(ActionEvent event){
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/saptamana.fxml"));
        try{
            BorderPane root=(BorderPane) loader.load();

            Stage stage=new Stage();
            Scene scene=new Scene(root);
            stage.setTitle("Selectati saptamana");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(scene);
            ControllerSapt controllerSapt=loader.getController();
            controllerSapt.setStage(stage);
            controllerSapt.setSceneSapt(scene);
            controllerSapt.setServiceUsers(serviceUsers);
            controllerSapt.setHostServices(hostServices);
            controllerSapt.setUser(serviceUsers.getUser(currentUser.getUsername()).get());

            rootStage.close();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleAbout(ActionEvent event){
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(StartApplication.class.getResource("/view_FXML/about.fxml"));
        try {
            BorderPane root=(BorderPane) loader.load();
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setTitle("Despre");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleOpenTabTeme(ActionEvent event){
        Tab temeTab=new Tab();
        tabPane.getTabs().add(temeTab);
        temeTab.setText("TemeLaborator");
        AnchorPane anchorPane=new AnchorPane();
        temeTab.setContent(anchorPane);
        initTemePane(service,anchorPane);
    }

    @FXML
    public void handleOpenTabNote(ActionEvent event){
        Tab noteTab=new Tab();
        tabPane.getTabs().add(noteTab);
        noteTab.setText("Note");
        AnchorPane anchorPane=new AnchorPane();
        noteTab.setContent(anchorPane);
        initNotaPane(service,anchorPane);
    }

    public void handleCloseCurrentTab(ActionEvent event){
        Tab tab=tabPane.getSelectionModel().getSelectedItem();
        tabPane.getTabs().remove(tab);
    }

    public void handleCloseAllTabs(ActionEvent event){
        tabPane.getTabs().removeAll(tabPane.getTabs());
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void update() {
        anchorPaneNote.getChildren().removeAll();
        initNotaPane(service,anchorPaneNote);
        anchorPaneStatistici.getChildren().removeAll();
        initStatisticiPane(service,anchorPaneStatistici);
    }

    @FXML
    public void handleOpenTabStatistica(ActionEvent event){
        Tab statisticiTab=new Tab();
        tabPane.getTabs().add(statisticiTab);
        statisticiTab.setText("Statistici");
        AnchorPane anchorPane=new AnchorPane();
        statisticiTab.setContent(anchorPane);
        initStatisticiPane(service,anchorPane);
    }

    @FXML
    public void handleOpenAllTabs(ActionEvent event){
        handleOpenTabTeme(new ActionEvent());
        handleOpenTabNote(new ActionEvent());
        handleOpenTabStatistica(new ActionEvent());
    }
}
