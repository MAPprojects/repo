package Constrollers;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import Service.Service;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GradeController implements Observer
{
    Service service;
    ObservableList<Nota> modelNota;
    @FXML
    AnchorPane extendableSearchPane;
    @FXML
    Rectangle clipRect;
    @FXML
    TableView<Nota> tableView;

    @FXML TableColumn<Nota,String> colNume;
    @FXML TableColumn<Nota,String>  colIdStudent;
    @FXML TableColumn<Nota,String>  colIdTema;
    @FXML TableColumn<Nota,String> colGrupa;
    @FXML TableColumn<Nota,String>  colProfesor;
    @FXML TableColumn<Nota,String>  colNota;

    @FXML ListView<Tema> listView;

    @FXML Button addButton;
    @FXML Button updateButton;
    @FXML Button export;

    @FXML
    TextField txtStudent;
    @FXML
    TextField txtTema;
    @FXML TextField txtNota;
    @FXML TextField txtData;
    @FXML TextArea txtObservatii;

    @FXML TextField txtStudentFiltr;
    @FXML TextField txtNotaFiltr;
    @FXML CheckBox chkNotaSub5;

//    @FXML ComboBox cmbStudentFiltr;

    @FXML
    SplitPane splitPane;

    @FXML Pagination pagination;

    @FXML ComboBox<String> comboPerPage;
    @FXML ComboBox<String> cmbStudentFiltr;

    String cmbData;

    public GradeController() {
    }

    public void setService(Service service)
    {
        this.service=service;
        this.modelNota = FXCollections.observableArrayList(service.iterableToArrayList(service.getNote()));
        tableView.setItems(modelNota);
        listView.setItems(FXCollections.observableArrayList(service.iterableToArrayList(service.getTeme())));

        setPagination();
    }

    @FXML
    public void initialize() {
        cmbData="";
        comboPerPage.getItems().addAll("5","10","20");
        comboPerPage.getSelectionModel().selectFirst();
        comboPerPage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                setPagination();
            }
        });


        colIdStudent.setCellValueFactory(new PropertyValueFactory<Nota,String>("idStudent"));
        colIdTema.setCellValueFactory(new PropertyValueFactory<Nota,String>("idTema"));
        colNume.setCellValueFactory(new PropertyValueFactory<Nota, String>("numeStudent"));
        colGrupa.setCellValueFactory(new PropertyValueFactory<Nota,String>("grupaStudent"));
        colProfesor.setCellValueFactory(new PropertyValueFactory<Nota,String>("profesor"));
        colNota.setCellValueFactory(new PropertyValueFactory<Nota,String>("nota"));

//
        listView.setCellFactory(param -> new ListCell<Tema>() {
            final Accordion acc = new Accordion();
            @Override
            public void updateItem(Tema name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    acc.setAccessibleText(name.getDeadline().toString());
                    acc.setMaxWidth(200);
                    TitledPane pane = new TitledPane();
//                    pane.idProperty().setValue("titledPane");
//                    pane.styleProperty().setValue("@Style.css");
                    pane.setText("Tema: "+name.getID());
                    pane.setMaxWidth(50);
//                    acc.setExpandedPane(pane);
                    pane.setStyle("-fx-color: #FEE096");
                    BorderPane content = new BorderPane();

                    Label label = new Label("Termen: Saptamana "+name.getDeadline().toString());

                    TextArea txt = new TextArea();

                    txt.setWrapText(true);

                    txt.prefHeight(100);
                    txt.maxWidth(50);
                    txt.setText(name.getDescriere());
                    content.setCenter(txt);
                    content.setTop(label);

                    pane.setContent(content);
                    acc.getPanes().add(pane);
                    //set content to pane
                    pane.setOnMouseClicked(x->{ modelNota.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.filterNotaTema(service.iterableToArrayList(service.getNote()),new Integer(pane.getText().split(" ")[1])))));
//                                                clearTxtFields();
                                                setPagination();
                                                txtTema.setText(pane.getText().split(" ")[1]);});
                    setText(null);
                    setGraphic(acc);
                }
            }


        });

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Nota>() {
            @Override
            public void changed(ObservableValue observable, Nota oldValue, Nota newValue)
            {
                if(newValue!= null)
                {
                    fillTxtFields(newValue);
                    if(clipRect.heightProperty().get()==0)
                        toggleExtendableSearch();
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

        addButton.setOnAction(this::addNota);
        updateButton.setOnAction(this::updateNota);
        export.setOnMouseClicked(this::exportToExcel);

        cmbStudentFiltr.getEditor().textProperty().addListener((ov,oldValue,  newValue)-> {
                Platform.runLater(() -> {
                    if (newValue != null) {


                        List<Student> studenti = service.filterNume(service.iterableToArrayList(service.getStudenti()), newValue);
                        cmbStudentFiltr.getItems().clear();
                        for (Student st : studenti
                                ) {
                            cmbStudentFiltr.getItems().add(st.getNume() + ',' + st.getID());
                        }
                        cmbStudentFiltr.show();
                        if(cmbData!="")
                        {
                            cmbStudentFiltr.getEditor().setText(cmbData);
                            cmbData="";
                        }
                    }
                });

        });

//        cmbStudentFiltr.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
//            {
//                if(newValue!=null)
//                {
//                    Student st = service.findStudentById(new Integer(newValue.split(",")[1])).get();
//                    txtStudent.setText(st.getID().toString());
//                    filterManager(newValue.split(",")[0],txtNotaFiltr.getText(),chkNotaSub5.isSelected());
//                }
//                else
//                {
//
//                }
//            }
//        });




        cmbStudentFiltr.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                if(newValue!=null)
                {
                    try {
                        filterManager(newValue, txtNotaFiltr.getText(), chkNotaSub5.isSelected());
                        Student st = service.findStudentById(new Integer(newValue.split(",")[1])).get();
                        txtStudent.setText(st.getID().toString());
                        filterManager(newValue.split(",")[0], txtNotaFiltr.getText(), chkNotaSub5.isSelected());
                        cmbStudentFiltr.getEditor().setText(newValue.split(",")[0]);
                        cmbData = newValue.split(",")[0];
                    }
                    catch (Exception ex)
                    {

                    }
                }
            }
        });


        txtNotaFiltr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterManager(cmbStudentFiltr.getEditor().getText(), txtNotaFiltr.getText(), chkNotaSub5.isSelected());
            }
        });

        chkNotaSub5.setOnAction(this::chkEventHandle);
    }

    private void chkEventHandle(ActionEvent actionEvent) {
        filterManager(cmbStudentFiltr.getEditor().getText(), txtNotaFiltr.getText(), chkNotaSub5.isSelected());
    }

    public void exportToExcel(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose location");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("EXCEL files (.xls)", ".xls");
        fileChooser.getExtensionFilters().add(extFilter);
        //Stage stage = (Stage) buttonExcel.getScene().getWindow();
        File file = fileChooser.showSaveDialog(new Stage());

        if(file!=null)
        {
            Workbook workbook = new HSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("sample");

            Row row = spreadsheet.createRow(0);

            for (int j = 0; j < tableView.getColumns().size()-1; j++) {
                row.createCell(0).setCellValue("Id");
                row.createCell(1).setCellValue("Nume");
                row.createCell(2).setCellValue("Profesor");
                row.createCell(3).setCellValue("Grupa");
                row.createCell(4).setCellValue("Termen de predare");
            }


            for (int i = 0; i < modelNota.size(); i++) {
                row = spreadsheet.createRow(i + 1);
                Nota nota= (Nota) modelNota.get(i);
                row.createCell(0).setCellValue(nota.getIdStudent());
                row.createCell(1).setCellValue(nota.getStudent().getNume());
                row.createCell(2).setCellValue(nota.getStudent().getCadruDidactic());
                row.createCell(3).setCellValue(nota.getStudent().getGrupa());
                row.createCell(4).setCellValue(nota.getTema().getDeadline());
            }

            FileOutputStream fileOut = null;

            try {
                fileOut = new FileOutputStream(file.toString());
                workbook.write(fileOut);
                fileOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Platform.exit();
        }

    }

    private void fillTxtFields(Nota nota)
    {
        txtStudent.setText(nota.getStudent().getID().toString());
        txtTema.setText(nota.getTema().getID().toString());
        txtNota.setText(nota.getNota().toString());

        txtStudent.setDisable(true);
        txtTema.setDisable(true);
    }

    private void filterManager(String student, String nota, boolean noteSub5)
    {
        List<Nota> temp = service.iterableToArrayList(service.getNote());
        if(!student.equals(""))
            temp = service.filterNotaStudent(temp, student);
        if(!nota.equals(""))
            temp = service.filterNota(temp,Integer.parseInt(nota));
        if(noteSub5)
            temp = service.filterNotaSubTrecere(temp);

        modelNota.setAll(FXCollections.observableArrayList(temp));
        setPagination();
    }

    private void addNota(ActionEvent actionEvent) {

        try {
            Integer idSt = Integer.parseInt(txtStudent.getText());
            Integer idTm = Integer.parseInt(txtTema.getText());
            String obs = txtObservatii.getText();
            Integer nota = Integer.parseInt(txtNota.getText());
            Integer sapt = Integer.parseInt(txtData.getText());

//
            service.addNota(idSt,idTm,sapt,nota,obs);

            clearTxtFields();
        }
        catch(RuntimeException ex)
        {
            ex.printStackTrace();
        }

    }

    private void updateNota(ActionEvent actionEvent)
    {
        try {
            Integer idSt = Integer.parseInt(txtStudent.getText());
            Integer idTm = Integer.parseInt(txtTema.getText());
            Integer saptPred = Integer.parseInt(txtData.getText());
            Integer notaNoua = Integer.parseInt(txtNota.getText());

            String obs = txtObservatii.getText();

            if (!obs.equals(""))
                service.modifyNota(idSt,idTm,saptPred,notaNoua,obs);

            clearTxtFields();
        }
        catch (RuntimeException ex)
        {
            ex.printStackTrace();
        }
    }

    private void setPagination()
    {
        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int cat=modelNota.size()/perPage;
        if(cat*perPage!=modelNota.size())
            cat=cat+1;
        pagination.setPageCount(cat);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {

        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int fromIndex=pageIndex*perPage;
        int toIndex = Math.min(fromIndex+perPage, modelNota.size());
        tableView.setItems(FXCollections.observableArrayList(modelNota.subList(fromIndex, toIndex)));

        return new BorderPane(tableView);
    }

    private void clearTxtFields()
    {
        txtTema.setText("");
        txtStudent.setText("");
        txtNota.setText("");
        txtObservatii.setText("");
        txtData.setText(service.getCurrentWeek().toString());

        txtTema.setDisable(false);
        txtStudent.setDisable(false);
        addButton.setDisable(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        modelNota.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getNote())));
        setPagination();
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
            txtData.setText(service.getCurrentWeek().toString());
        }
    }
}
