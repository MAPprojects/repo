package Controller;

import Domain.DTOgrades;
import Domain.Student;
import Service.Service;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class StudentController2 implements java.util.Observer
{

    Service service;
    ObservableList<Student> modelStudent;

    @FXML private AnchorPane extendableSearchPane;
    @FXML private AnchorPane anchorPaneLeft;
    @FXML private AnchorPane anchorPaneRight;
    @FXML TableView tableStudent;
    @FXML TableColumn columnName;
    @FXML TableColumn columnGroup;
    @FXML TableColumn columnTeacher;
    @FXML TableColumn columnEmail;
    @FXML TableColumn columnAction;
    @FXML TextField txtSearchName;
    @FXML TextField txtTeacher;
    @FXML ChoiceBox<String> comboGroup;
    @FXML SplitPane splitPane;
    @FXML TextField txtId;
    @FXML TextField txtName;
    @FXML TextField txtGroup;
    @FXML TextField txtEmail;
    @FXML ComboBox comboTeacher;
    @FXML Pane paneTransparent;
    @FXML Pagination pagination;
    @FXML ComboBox<String> comboPerPage;
    @FXML Button buttonExcel;

    private Rectangle clipRectLeft;
    private Rectangle clipRectRight;

    private int coborari=0;
    public StudentController2()
    {

    }

    public void setService(Service service)
    {
        this.service=service;
        modelStudent=FXCollections.observableArrayList(service.iterableToArrayList(service.getAllStudents()));
        tableStudent.setItems(modelStudent);
        setPagination();

    }
    private void setPagination()
    {
        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int cat=modelStudent.size()/perPage;
        if(cat*perPage!=modelStudent.size())cat=cat+1;
        pagination.setPageCount(cat);
        pagination.setPageFactory(this::createPage);

    }

    public void exportToExcel(MouseEvent event) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose location");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("EXCEL files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        //Stage stage = (Stage) buttonExcel.getScene().getWindow();
        File file = fileChooser.showSaveDialog(new Stage());

        if(file!=null)
        {
            Workbook workbook = new HSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("sample");

            Row row = spreadsheet.createRow(0);

            for (int j = 0; j < tableStudent.getColumns().size()-1; j++) {
                row.createCell(0).setCellValue("Id");
                row.createCell(1).setCellValue("Nume");
                row.createCell(2).setCellValue("Grupa");
                row.createCell(3).setCellValue("Email");
                row.createCell(4).setCellValue("Profesor");
            }


            for (int i = 0; i < modelStudent.size(); i++) {
                row = spreadsheet.createRow(i + 1);
                Student student= (Student) modelStudent.get(i);
                row.createCell(0).setCellValue(student.getID());
                row.createCell(1).setCellValue(student.getNume());
                row.createCell(2).setCellValue(student.getGrupa());
                row.createCell(3).setCellValue(student.getEmail());
                row.createCell(4).setCellValue(student.getCadruDidactic());
                }

                FileOutputStream fileOut = new FileOutputStream(file.toString());
                workbook.write(fileOut);
                fileOut.close();
                //Platform.exit();
        }

    }

    private Node createPage(int pageIndex)
    {
        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int fromIndex=pageIndex*perPage;
        int toIndex=Math.min(fromIndex+perPage,modelStudent.size());
        tableStudent.setItems(FXCollections.observableArrayList(modelStudent.subList(fromIndex,toIndex)));
        return new BorderPane(tableStudent);
    }
    @FXML
    public void toggleExtendableSearch(MouseEvent mouseEvent) {
        final KeyValue kvUp5;
        clipRectLeft.setWidth(anchorPaneLeft.getWidth());

        if (clipRectLeft.heightProperty().get() != 0) {
            coborari--;
            if (coborari==0)
            {
                kvUp5 = new KeyValue(extendableSearchPane.prefHeightProperty(), 0);
            }
            else
            {
                kvUp5 = new KeyValue(extendableSearchPane.prefHeightProperty(), extendableSearchPane.getPrefHeight());
            }
            // Animation for scroll up.
            Timeline timelineUp = new Timeline();

            // Animation of sliding the search pane up, implemented via
            // clipping.
            final KeyValue kvUp1 = new KeyValue(clipRectLeft.heightProperty(), 0);
            final KeyValue kvUp2 = new KeyValue(clipRectLeft.translateYProperty(), anchorPaneLeft.getHeight());

            // The actual movement of the search pane. This makes the table
            // grow.

            final KeyValue kvUp4 = new KeyValue(anchorPaneLeft.prefHeightProperty(), 0);
            final KeyValue kvUp3 = new KeyValue(anchorPaneLeft.translateYProperty(), -anchorPaneLeft.getHeight());



            final KeyFrame kfUp = new KeyFrame(Duration.millis(200), kvUp1, kvUp2, kvUp3, kvUp4,kvUp5);
            timelineUp.getKeyFrames().add(kfUp);
            timelineUp.play();
        } else {
            if(coborari==0)
            {
                kvUp5 = new KeyValue(extendableSearchPane.prefHeightProperty(), 200);
            }
            else
            {
                kvUp5 = new KeyValue(extendableSearchPane.prefHeightProperty(), extendableSearchPane.getPrefHeight());
            }
            coborari++;

            // Animation for scroll down.
            Timeline timelineDown = new Timeline();

            // Animation for sliding the search pane down. No change in size,
            // just making the visible part of the pane
            // bigger.
            final KeyValue kvDwn1 = new KeyValue(clipRectLeft.heightProperty(), 200);
            final KeyValue kvDwn2 = new KeyValue(clipRectLeft.translateYProperty(), 0);

            // Growth of the pane.
            final KeyValue kvDwn4 = new KeyValue(anchorPaneLeft.prefHeightProperty(), 200);
            final KeyValue kvDwn3 = new KeyValue(anchorPaneLeft.translateYProperty(), 0);

            final KeyFrame kfDwn = new KeyFrame(Duration.millis(200), kvDwn1, kvDwn2,
                    kvDwn3, kvDwn4,kvUp5);
            timelineDown.getKeyFrames().add(kfDwn);

            timelineDown.play();
        }
    }
    @FXML
    public void toggleExtendableAdd(MouseEvent mouseEvent) {

        final KeyValue kvUp5,kvDwn5;
        clipRectRight.setWidth(anchorPaneRight.getWidth());

        if (clipRectRight.heightProperty().get() != 0) {
            coborari--;
            if (coborari==0)
            {
                kvUp5 = new KeyValue(extendableSearchPane.prefHeightProperty(), 0);
            }
            else
            {
                kvUp5 = new KeyValue(extendableSearchPane.prefHeightProperty(), extendableSearchPane.getPrefHeight());
            }

            // Animation for scroll up.
            Timeline timelineUp = new Timeline();

            // Animation of sliding the search pane up, implemented via
            // clipping.
            final KeyValue kvUp1 = new KeyValue(clipRectRight.heightProperty(), 0);
            final KeyValue kvUp2 = new KeyValue(clipRectRight.translateYProperty(), 200);

            // The actual movement of the search pane. This makes the table
            // grow.

            final KeyValue kvUp4 = new KeyValue(anchorPaneRight.prefHeightProperty(), 0);
            final KeyValue kvUp3 = new KeyValue(anchorPaneRight.translateYProperty(), -200);

            final KeyFrame kfUp = new KeyFrame(Duration.millis(200), kvUp1, kvUp2, kvUp3, kvUp4,kvUp5);
            timelineUp.getKeyFrames().add(kfUp);
            timelineUp.play();
        } else {
            if(coborari==0)
            {
                kvDwn5 = new KeyValue(extendableSearchPane.prefHeightProperty(), 200);
            }
            else
            {
                kvDwn5 = new KeyValue(extendableSearchPane.prefHeightProperty(), extendableSearchPane.getPrefHeight());
            }
            coborari++;
            // Animation for scroll down.
            Timeline timelineDown = new Timeline();

            // Animation for sliding the search pane down. No change in size,
            // just making the visible part of the pane
            // bigger.
            final KeyValue kvDwn1 = new KeyValue(clipRectRight.heightProperty(), 200);
            final KeyValue kvDwn2 = new KeyValue(clipRectRight.translateYProperty(), 0);

            // Growth of the pane.
            final KeyValue kvDwn4 = new KeyValue(anchorPaneRight.prefHeightProperty(), 200);
            final KeyValue kvDwn3 = new KeyValue(anchorPaneRight.translateYProperty(), 0);

            final KeyFrame kfDwn = new KeyFrame(Duration.millis(200), kvDwn1, kvDwn2,
                    kvDwn3, kvDwn4,kvDwn5);
            timelineDown.getKeyFrames().add(kfDwn);

            timelineDown.play();
        }
    }

    @FXML
    public void initialize()
    {
        //paneTransparent.setVisible(false);



        comboPerPage.getItems().addAll("10","20","30");
        comboPerPage.getSelectionModel().selectFirst();
        comboPerPage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                setPagination();
            }
        });


        comboTeacher.getItems().addAll("SERBAN CAMELIA","COJOCARU GRIGORETA");

        columnName.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
        columnGroup.setCellValueFactory(new PropertyValueFactory<Student,Integer>("grupa"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<Student,String >("email"));
        columnTeacher.setCellValueFactory(new PropertyValueFactory<Student,String>("cadruDidactic"));
        columnAction.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));



        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFactory=
                new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Student, String> param) {
                        final TableCell<Student, String> cell = new TableCell<Student, String>() {

                            final Button buttonDelete = new Button();
                            final Button buttonUpdate=new Button();
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty)
                                {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    buttonDelete.setId("buttonDelete");
                                    buttonDelete.setOnAction(event -> {
                                        final Stage dialog = new Stage();
                                        dialog.setTitle("Are you sure?");
                                        dialog.initStyle(StageStyle.UNDECORATED);
                                        dialog.setWidth(350);
                                        dialog.setHeight(204);
                                        dialog.setResizable(false);
                                        dialog.initModality(Modality.APPLICATION_MODAL);
                                        //paneTransparent.setVisible(true);
                                        FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/areYouSure.fxml")));
                                        try {
                                            AnchorPane root=(AnchorPane)loader.load();
                                            DeleteController ctrl=loader.getController();
                                            Student student = getTableView().getItems().get(getIndex());
                                            ctrl.buttonYesDelete.setOnAction(ctrl::handleYesDelete);
                                            ctrl.setStudentToDelete(student);
                                            ctrl.setService(service);
                                            ctrl.setPane(paneTransparent);

                                            Scene dialogScene = new Scene(root, 350, 204);
                                            dialog.setScene(dialogScene);
                                            dialog.show();

                                           // service.deleteStudent(student.getID());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        //Student student = getTableView().getItems().get(getIndex());
                                        //service.deleteStudent(student.getID());
                                        modelStudent=FXCollections.observableArrayList(service.iterableToArrayList(service.getAllStudents()));
                                        tableStudent.setItems(modelStudent);
                                        setPagination();
                                    });
                                    //buttonDelete.setMaxSize(50,70);
                                    Image imageDel = new Image("FXML/delete.png", 20, 20, false, false);
                                    buttonDelete.setGraphic(new ImageView(imageDel));
                                    buttonDelete.getStylesheets().add("FXML/buttonTable.css");

                                    Image imageUpdate = new Image("FXML/update.png", 20, 20, false, false);
                                    buttonUpdate.setGraphic(new ImageView(imageUpdate));
                                    buttonUpdate.getStylesheets().add("FXML/buttonTable.css");
                                    buttonUpdate.setId("buttonUpdate");
                                    buttonUpdate.setOnAction(event->{

                                        //paneTransparent.setVisible(true);
                                        final Stage dialog = new Stage();
                                        dialog.setTitle("Update student");
                                        //dialog.initStyle(StageStyle.UTILITY);
                                        dialog.initStyle(StageStyle.UNDECORATED);
                                        //dialog.setAlwaysOnTop(true);
                                        dialog.setWidth(500);
                                        dialog.setHeight(400);
                                        dialog.setResizable(false);
                                        dialog.initModality(Modality.APPLICATION_MODAL);
                                        FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/updateWindow.fxml")));
                                        try {
                                            AnchorPane root=(AnchorPane)loader.load();
                                            UpdateWindowController ctrl=loader.getController();
                                            ctrl.setService(service);
                                            ctrl.setPane(paneTransparent);
                                            Student student = getTableView().getItems().get(getIndex());
                                            ctrl.setFieldsForUpdate(student.getID(),student.getNume(),student.getGrupa(),student.getEmail(),student.getCadruDidactic());
                                            Scene dialogScene = new Scene(root, 300, 200);
                                            dialog.setScene(dialogScene);
                                            dialog.show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    });

                                    HBox hbox=new HBox();
                                    hbox.setSpacing(10);
                                    hbox.getChildren().addAll(buttonDelete,buttonUpdate);
                                    setGraphic(hbox);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        columnAction.setCellFactory(cellFactory);

        double widthInitial = 200;
        double heightInitial = 200;
        clipRectLeft = new Rectangle();
        clipRectLeft.setWidth(widthInitial);
        clipRectLeft.setHeight(0);
        clipRectLeft.translateYProperty().set(heightInitial);
        anchorPaneLeft.setClip(clipRectLeft);
        anchorPaneLeft.translateYProperty().set(-heightInitial);
        anchorPaneLeft.prefHeightProperty().set(0);

        clipRectRight = new Rectangle();
        clipRectRight.setWidth(widthInitial);
        clipRectRight.setHeight(0);
        clipRectRight.translateYProperty().set(heightInitial);
        anchorPaneRight.setClip(clipRectRight);
        anchorPaneRight.translateYProperty().set(-heightInitial);
        anchorPaneRight.prefHeightProperty().set(0);

        //extendableSearchPane.translateYProperty().set(-heightInitial);
        extendableSearchPane.prefHeightProperty().set(0);


        txtSearchName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                int gr;
                if(comboGroup.getSelectionModel().getSelectedItem()=="No group")gr=-1;
                else gr=Integer.parseInt(comboGroup.getSelectionModel().getSelectedItem());
                modelStudent = FXCollections.observableArrayList(service.filterAllStudent(newValue,gr,txtTeacher.getText()));
                tableStudent.setItems(modelStudent);
                setPagination();

            }
        });
        txtTeacher.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                int gr;
                if(comboGroup.getSelectionModel().getSelectedItem()=="No group")gr=-1;
                else gr=Integer.parseInt(comboGroup.getSelectionModel().getSelectedItem());
                modelStudent = FXCollections.observableArrayList(service.filterAllStudent(txtSearchName.getText(),gr,newValue));
                tableStudent.setItems(modelStudent);
                setPagination();
            }
        });

        comboGroup.getItems().setAll("No group","221","222","223","224","225","226","227");
        comboGroup.getSelectionModel().selectFirst();
        comboGroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {

                int gr;
                if(newValue=="No group")gr=-1;
                else gr=Integer.parseInt(newValue);
                modelStudent = FXCollections.observableArrayList(service.filterAllStudent(txtSearchName.getText(),gr,txtTeacher.getText()));
                tableStudent.setItems(modelStudent);
                setPagination();
            }
        });
    }

    public ObservableList<Student> getModelStudent()
    {
        return modelStudent;
    }

    public void handleAddStudent(ActionEvent actionEvent)
    {

        try
        {
            Integer id=Integer.parseInt(txtId.getText());
            String nume=txtName.getText();
            int grupa=Integer.parseInt(txtGroup.getText());
            String email=txtEmail.getText();
            String prof=comboTeacher.getSelectionModel().getSelectedItem().toString();
            service.addStudent(id,nume,grupa,email,prof);
            setPagination();
        }
        catch (NumberFormatException ne)
        {
            showError("Atributele studentului nu sunt valide\n ID, grupa: numere intregi pozitive\n nume, email,profesor: siruri de caractere ");
        }catch (Exception e)
        {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alerta=new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Mesaj eroare");
        alerta.setContentText(message);
        alerta.showAndWait();
    }


    @Override
    public void update(Observable o, Object arg) {
        modelStudent.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getAllStudents())));
        setPagination();
    }

}
