package Constrollers;

import Domain.Student;
import Service.Service;
import Views.StudentView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Service.EmailSender;

public class StudentControllerV2 implements Observer
{
    Service service;
    ObservableList<Student> modelStudent;

    @FXML AnchorPane extendableSearchPane;
    @FXML Rectangle clipRect;
    @FXML TableView<Student> tableView;
    @FXML TableColumn<Student,String> colNume;
    @FXML TableColumn<Student,String>  colGrupa;
    @FXML TableColumn<Student,String>  colEmail;
    @FXML TableColumn<Student,String>  colCadruDidactic;
    @FXML TableColumn<Student,String>  colDelete;

    @FXML Button addButton;
    @FXML Button updateButton;
    @FXML Button emailButton;
    @FXML TextField txtId;
    @FXML TextField txtNume;
    @FXML TextField txtGrupa;
    @FXML TextField txtEmail;
    @FXML TextField txtProfesor;

    @FXML TextField txtNumeFiltr;
    @FXML TextField txtGrupaFiltr;
    @FXML TextField txtProfesorFiltr;
    @FXML Pagination pagination;

    @FXML SplitPane splitPane;

    @FXML ComboBox<String> comboPerPage;

    public StudentControllerV2()
    {
    }

    public void setService(Service service)
    {
        this.service=service;
        this.modelStudent = FXCollections.observableArrayList(service.iterableToArrayList(service.getStudenti()));
        //modelStudent.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getStudenti())));
        tableView.setItems(modelStudent);
        //tableView.getItems().setAll(modelStudent);

        //pagination = new Pagination((modelStudent.size() / tableView.getItems().size() + 1), 0);
        //pagination.setPageCount((modelStudent.size() / ((int)tableView.getHeight()/40) + 1));
//        pagination.setPageCount((modelStudent.size()/tableView.getItems().size() + 1));
//        pagination.setPageFactory(this::createPage);
        setPagination();
    }

    @FXML
    public void initialize() {
        comboPerPage.getItems().addAll("5","10","20");
        comboPerPage.getSelectionModel().selectFirst();
        comboPerPage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                setPagination();
            }
        });

        colNume.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        colCadruDidactic.setCellValueFactory(new PropertyValueFactory<Student,String>("cadruDidactic"));
        colGrupa.setCellValueFactory(new PropertyValueFactory<Student,String>("grupa"));

        colDelete.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));



        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFactory =
                new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Student, String> param) {
                        final TableCell<Student, String> cell = new TableCell<Student, String>() {

                            final Button buttonDelete = new Button();

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    buttonDelete.setOnAction(event -> {
                                        Student student = getTableView().getItems().get(getIndex());
                                        service.delete(student.getID());

                                        setPagination();
                                    });
                                    //buttonDelete.setMaxSize(50,70);
                                    Image imageDel = new Image("FXML/deleteIcon3.png", 20, 20, false, false);
                                    buttonDelete.setGraphic(new ImageView(imageDel));
                                    buttonDelete.getStylesheets().add("FXML/Style.css");
                                    buttonDelete.setId("buttonDelete");
                                    setGraphic(buttonDelete);
                                    setText(null);

                                }

                            }
                        };

                        return cell;
                    }
                };

        colDelete.setCellFactory(cellFactory);

//        Node divider = splitPane.lookup(".split-pane-divider");
//        if(divider!=null){
//            divider.setStyle("-fx-background-color: #C94B46;");
//        }

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue observable, Student oldValue, Student newValue)
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

        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        double widthInitial = 627;
        double heightInitial = 134;

        clipRect = new Rectangle();
        clipRect.setWidth(widthInitial);
        clipRect.setHeight(0);
        clipRect.translateYProperty().set(heightInitial);
        extendableSearchPane.setClip(clipRect);
        extendableSearchPane.translateYProperty().set(-heightInitial);
        extendableSearchPane.prefHeightProperty().set(0);

        addButton.setOnAction(this::addStudent);
        updateButton.setOnAction(this::updateStudent);
        emailButton.setOnAction(this::sendEmail);

        txtNumeFiltr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterManager(txtNumeFiltr.getText(),txtGrupaFiltr.getText(),txtProfesorFiltr.getText());
            }
        });

        txtGrupaFiltr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterManager(txtNumeFiltr.getText(),txtGrupaFiltr.getText(),txtProfesorFiltr.getText());
            }
        });

        txtProfesorFiltr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterManager(txtNumeFiltr.getText(),txtGrupaFiltr.getText(),txtProfesorFiltr.getText());
            }
        });
    }

    private void setPagination()
    {
        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int cat=modelStudent.size()/perPage;
        if(cat*perPage!=modelStudent.size())
            cat=cat+1;
        pagination.setPageCount(cat);
        pagination.setPageFactory(this::createPage);

    }

    private Node createPage(int pageIndex) {

        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int fromIndex=pageIndex*perPage;
        int toIndex = Math.min(fromIndex+perPage, modelStudent.size());
        tableView.setItems(FXCollections.observableArrayList(modelStudent.subList(fromIndex, toIndex)));

        return new BorderPane(tableView);
    }

    private void filterManager(String nume, String grupa, String profesor)
    {
        List<Student> temp = service.iterableToArrayList(service.getStudenti());
        if(!nume.equals(""))
            temp = service.filterNume(temp,nume);
        if(!grupa.equals(""))
            temp = service.filterGrupa(temp,Integer.parseInt(grupa));
        if(!profesor.equals(""))
            temp = service.filterCadruDidactic(temp,profesor);
        modelStudent.setAll(FXCollections.observableArrayList(temp));
        setPagination();
    }

    private void addStudent(ActionEvent actionEvent) {

        try {
            Integer id = Integer.parseInt(txtId.getText());
            String name = txtNume.getText();
            Integer grupa = Integer.parseInt(txtGrupa.getText());
            String email = txtEmail.getText();
            String cadruDidactic = txtProfesor.getText();

            service.addStudent(id, name, grupa, email, cadruDidactic);

            clearTxtFields();
            setPagination();
        }
        catch(RuntimeException ex){}
    }

    private void sendEmail(ActionEvent actionEvent)
    {
        EmailSender sender = new EmailSender();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        tableView.getSelectionModel().getSelectedItems().forEach(x->temp.add(x.getID()));
        final Stage dialog = new Stage();
        dialog.setTitle("Sending emails");
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setWidth(312);
        dialog.setHeight(137);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        //paneTransparent.setVisible(true);
        FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/Proggress.fxml")));
        try {
            AnchorPane root=(AnchorPane)loader.load();
            ProggressController ctrl=loader.getController();
            Scene dialogScene = new Scene(root, 312, 137);
            dialog.setScene(dialogScene);
            ctrl.setService(service);
            ctrl.setEmailsAndCheckBoxes(temp);
            dialog.show();

            // ctrl.setPane(paneTransparent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateStudent(ActionEvent actionEvent)
    {
        Integer id = Integer.parseInt(txtId.getText());
        String name = txtNume.getText();
        Integer grupa = Integer.parseInt(txtGrupa.getText());
        String email = txtEmail.getText();
        String cadruDidactic = txtProfesor.getText();
        service.updateStudent(id,name,grupa,email,cadruDidactic);
        clearTxtFields();

        setPagination();
    }

    private void fillTxtFields(Student st)
    {
        txtNume.setText(st.getNume());
        txtId.setText(st.getID().toString());
        txtEmail.setText(st.getEmail());
        txtGrupa.setText(st.getGrupa().toString());
        txtProfesor.setText(st.getCadruDidactic());

        txtId.setDisable(true);
    }

    private void clearTxtFields()
    {
        txtNume.setText("");
        txtId.setText("");
        txtEmail.setText("");
        txtGrupa.setText("");
        txtProfesor.setText("");

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
        modelStudent.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getStudenti())));

        setPagination();
    }
}
