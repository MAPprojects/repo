package Controller;

import Domain.DTOgrades;
import Domain.Student;
import Domain.Tema;
import Service.Service;
import Service.MailSender;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.text.IconView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

public class GradesController implements Observer
{
    @FXML TableView tableGrades;
    @FXML TableColumn columnName;
    @FXML TableColumn columnGroup;
    @FXML TableColumn columnCheck;
    @FXML TableColumn columnAction;
    @FXML TextField txtSearchStudent;
    @FXML ComboBox<String> comboBoxGroup;
    @FXML private AnchorPane extendableSearchPane;
    @FXML private AnchorPane anchorPaneLeft;
    private Rectangle clipRectLeft;
    private int coborari=0;
    @FXML Pagination pagination;
    @FXML ComboBox<String> comboPerPage;

    Service service;
    ObservableList<DTOgrades> model;
    ArrayList<Integer> sendEmailTo;
    public ArrayList<CheckBox> checkBoxes;
    public GradesController()
    {
        sendEmailTo=new ArrayList<>();
        checkBoxes=new ArrayList<>();
    }

    public void setService(Service service)
    {
        this.service=service;

        for(Tema tema : service.getAllHomeworks())
        {
            int nr=tema.getID();
            TableColumn<DTOgrades,String> columnHomework=new TableColumn("Homework "+nr);

            Label labelColumn = new Label();
            Image imageDel = new Image("FXML/info2.png", 20, 20, false, true);
            labelColumn.setGraphic(new ImageView(imageDel));
            labelColumn.setPrefWidth(20);
            labelColumn.setPrefHeight(20);
            labelColumn.setTooltip(new Tooltip(tema.getDescriere()));
            columnHomework.setGraphic(labelColumn);

            columnHomework.setCellFactory(TextFieldTableCell.forTableColumn());
            columnHomework.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<DTOgrades, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<DTOgrades, String> t) {
                            DTOgrades newGrade=((DTOgrades) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                            String oldGrade=t.getOldValue();
                            String grade=t.getNewValue();
                            if(oldGrade=="")
                            {
                                service.addNota(newGrade.getStudent().getID(),nr,service.current_week(),Integer.parseInt(grade),"");
                            }
                            else
                            {
                                try
                                {
                                    service.modifyNota(newGrade.getStudent().getID(),nr,service.current_week(),Integer.parseInt(grade),"" );

                                }catch (Exception e)
                                { }
                            }
                            model= FXCollections.observableArrayList(service.getAllDtoGrades());
                            setPagination();
                        }
                    }
            );


            columnHomework.setCellValueFactory(c-> {
                if (c.getValue().getGrades().get(nr)!=null)
                {
                    return new SimpleStringProperty(String.valueOf(c.getValue().getGrades().get(nr)));
                }
                else {return new SimpleStringProperty("");}
            });
            tableGrades.getColumns().add(columnHomework);
        }

        model= FXCollections.observableArrayList(service.getAllDtoGrades());
        tableGrades.setItems(model);
        setPagination();
    }

    private void setPagination()
    {
        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int cat=model.size()/perPage;
        if(cat*perPage!=model.size())cat=cat+1;
        pagination.setPageCount(cat);
        pagination.setPageFactory(this::createPage);

    }

    private Node createPage(int pageIndex)
    {
        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int fromIndex=pageIndex*perPage;
        int toIndex=Math.min(fromIndex+perPage,model.size());
        tableGrades.setItems(FXCollections.observableArrayList(model.subList(fromIndex,toIndex)));
        return new BorderPane(tableGrades);
    }

    private void handleSendFeedbackOne(DTOgrades dtoSelected)
    {
        String email=dtoSelected.getStudent().getEmail();
        int id=dtoSelected.getStudent().getID();
        String fileName="src\\Resources\\"+id+"Student.txt";
        try
        {
            MailSender.send(email,fileName);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Email send successfully");
            alert.showAndWait();

        }catch(Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("The mail couldn't be sent");
            alert.showAndWait();
        }
    }
    @FXML
    public void initialize()
    {
        tableGrades.setEditable(true);

        comboPerPage.getItems().addAll("10","20","30");
        comboPerPage.getSelectionModel().selectFirst();
        comboPerPage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                setPagination();
            }
        });


        columnName.setCellValueFactory(new PropertyValueFactory<DTOgrades,String>("name"));
        columnGroup.setCellValueFactory(new PropertyValueFactory<DTOgrades,Integer>("group"));
        columnAction.setCellValueFactory(new PropertyValueFactory<>("nothing"));
        columnCheck.setCellValueFactory(new PropertyValueFactory<>("nothing"));
        comboBoxGroup.getItems().addAll("No group","221","222","223","224","225","226");
        comboBoxGroup.getSelectionModel().selectFirst();

        txtSearchStudent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                int gr;
                if(comboBoxGroup.getSelectionModel().getSelectedItem()=="No group")gr=-1;
                else gr=Integer.parseInt(comboBoxGroup.getSelectionModel().getSelectedItem());
                model = FXCollections.observableArrayList(service.filterAllDTO(newValue,gr));
                tableGrades.setItems(model);
                setPagination();

            }
        });

        comboBoxGroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {

                int gr;
                if(newValue=="No group")gr=-1;
                else gr=Integer.parseInt(newValue);
                model = FXCollections.observableArrayList(service.filterAllDTO(txtSearchStudent.getText(),gr));
                tableGrades.setItems(model);
                setPagination();
            }
        });

        Callback<TableColumn<DTOgrades, String>, TableCell<DTOgrades, String>> cellFactory=
                new Callback<TableColumn<DTOgrades, String>, TableCell<DTOgrades, String>>() {
                    @Override
                    public TableCell call(final TableColumn<DTOgrades, String> param) {
                        final TableCell<DTOgrades, String> cell = new TableCell<DTOgrades, String>() {

                            final Button buttonAddGrade=new Button();
                            final Button buttonUpdate=new Button();
                            final Button buttonSendFeedback=new Button();
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty)
                                {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    buttonAddGrade.setId("buttonAddGrade");
                                    buttonAddGrade.setOnAction(event -> {
                                        final Stage dialog = new Stage();
                                        dialog.setTitle("Add grade");
                                        dialog.initStyle(StageStyle.UNDECORATED);
                                        dialog.setWidth(500);
                                        dialog.setHeight(400);
                                        dialog.setResizable(false);
                                        dialog.initModality(Modality.APPLICATION_MODAL);
                                        //paneTransparent.setVisible(true);
                                        FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/addGradeWindow.fxml")));
                                        try {
                                            AnchorPane root=(AnchorPane)loader.load();
                                            AddGradeController ctrl=loader.getController();
                                            ctrl.buttonAddGrade.setOnAction(ctrl::handleAddGrade);
                                            DTOgrades dtoSelected = getTableView().getItems().get(getIndex());
                                            ctrl.setDTO(dtoSelected);
                                            ctrl.setService(service);
                                           // ctrl.setPane(paneTransparent);

                                            Scene dialogScene = new Scene(root, 500, 400);
                                            dialog.setScene(dialogScene);
                                            dialog.show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        model=FXCollections.observableArrayList(service.iterableToArrayList(service.getAllDtoGrades()));
                                        tableGrades.setItems(model);
                                        setPagination();
                                    });

                                    Image imageUpdate = new Image("FXML/update.png", 20, 20, false, false);
                                    buttonUpdate.setGraphic(new ImageView(imageUpdate));
                                    buttonUpdate.setId("buttonUpdate2");
                                    buttonUpdate.getStylesheets().add("FXML/buttonTable.css");

                                    Image imageAdd = new Image("FXML/addTema.png", 20, 20, false, false);
                                    buttonAddGrade.setGraphic(new ImageView(imageAdd));
                                    buttonAddGrade.getStylesheets().add("FXML/buttonTable.css");

                                    buttonUpdate.setOnAction(event -> {
                                        final Stage dialog = new Stage();
                                        dialog.setTitle("Update grade");
                                        dialog.initStyle(StageStyle.UNDECORATED);
                                        dialog.setWidth(500);
                                        dialog.setHeight(400);
                                        dialog.setResizable(false);
                                        dialog.initModality(Modality.APPLICATION_MODAL);
                                        //paneTransparent.setVisible(true);
                                        FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/addGradeWindow.fxml")));
                                        try {
                                            AnchorPane root=(AnchorPane)loader.load();
                                            AddGradeController ctrl=loader.getController();
                                            ctrl.buttonAddGrade.setText("Update grade");
                                            ctrl.buttonAddGrade.setOnAction(ctrl::handleUpdateGrade);
                                            DTOgrades dtoSelected = getTableView().getItems().get(getIndex());
                                            ctrl.setDTO(dtoSelected);
                                            ctrl.setService(service);
                                            // ctrl.setPane(paneTransparent);

                                            Scene dialogScene = new Scene(root, 500, 400);
                                            dialog.setScene(dialogScene);
                                            dialog.show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        model=FXCollections.observableArrayList(service.iterableToArrayList(service.getAllDtoGrades()));
                                        tableGrades.setItems(model);
                                        setPagination();
                                    });
                                    buttonSendFeedback.setId("buttonSendFeedback");
                                    buttonSendFeedback.setOnAction(event->{
                                        DTOgrades dtoSelected = getTableView().getItems().get(getIndex());
                                        handleSendFeedbackOne(dtoSelected);
                                    });


                                    Image imageFeedback = new Image("FXML/mail.png", 20, 20, false, false);
                                    buttonSendFeedback.setGraphic(new ImageView(imageFeedback));
                                    buttonSendFeedback.getStylesheets().add("FXML/buttonTable.css");

                                    HBox hbox=new HBox();
                                    hbox.setSpacing(10);
                                    hbox.getChildren().addAll(buttonSendFeedback,buttonAddGrade,buttonUpdate);
                                    setGraphic(hbox);
                                    setText(null);
                                }
                            }


                        };
                        return cell;
                    }
                };

        columnAction.setCellFactory(cellFactory);


        tableGrades.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        List list = new ArrayList();
        columnCheck.setCellFactory(
                new Callback<TableColumn, TableCell>()
                {
                    public TableCell call( TableColumn p )
                    {
                        return new TableCell()
                        {
                            private final CheckBox checkBox = new CheckBox();


                            @Override
                            public void updateItem( Object item, boolean empty )
                            {
                                super.updateItem( item, empty );
                                setText( null );

                                if ( empty )
                                {
                                    setGraphic( null );
                                }
                                else
                                {
                                    // select/unselect the item when checkbox is selected/unselected
                                    checkBox.selectedProperty().addListener( new ChangeListener<Boolean>()
                                    {
                                        @Override
                                        public void changed( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue )
                                        {
                                            getTableView().requestFocus();
                                            if ( newValue )
                                            {
                                                getTableView().getSelectionModel().select( getIndex() );
                                                DTOgrades dtoSelected = (DTOgrades)getTableView().getItems().get(getIndex());
                                                if(!sendEmailTo.contains(dtoSelected.getStudent().getID()))
                                                    sendEmailTo.add(dtoSelected.getStudent().getID());
                                                list.add( item );

                                            }
                                            else
                                            {
                                                getTableView().getSelectionModel().clearSelection( getIndex() );
                                                DTOgrades dtoSelected = (DTOgrades)getTableView().getItems().get(getIndex());
                                                sendEmailTo.remove(dtoSelected.getStudent().getID());
                                                list.remove( item );
                                            }
                                        }
                                    } );

                                    // select/unselect the checkbox when the tablerow is selected/unselected
                                    if ( getTableRow() != null )
                                    {
                                        getTableRow().selectedProperty().addListener( new ChangeListener<Boolean>()
                                        {
                                            @Override
                                            public void changed( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue )
                                            {
                                                checkBox.setSelected( newValue );
                                                if ( newValue )
                                                {
                                                    list.add( item );
                                                    DTOgrades dtoSelected = (DTOgrades)getTableView().getItems().get(getIndex());
                                                    if(!sendEmailTo.contains(dtoSelected.getStudent().getID()))
                                                             sendEmailTo.add(dtoSelected.getStudent().getID());
                                                }
                                                else
                                                {
                                                    DTOgrades dtoSelected = (DTOgrades)getTableView().getItems().get(getIndex());
                                                    sendEmailTo.remove(dtoSelected.getStudent().getID());
                                                    list.remove( item );

                                                }
                                            }
                                        } );
                                    }
                                    setGraphic( checkBox );
                                }
                            }
                        };
                    }
                } );


//        Callback<TableColumn<DTOgrades, String>, TableCell<DTOgrades, String>> cellFactoryCheck=
//                new Callback<TableColumn<DTOgrades, String>, TableCell<DTOgrades, String>>() {
//                    @Override
//                    public TableCell call(final TableColumn<DTOgrades, String> param) {
//
//                        final TableCell<DTOgrades, String> cell = new TableCell<DTOgrades, String>() {
//
//                            final CheckBox checkBox=new CheckBox();
//
//                            @Override
//                            public void updateItem(String item, boolean empty) {
//                                super.updateItem(item, empty);
//                                checkBoxes.add(checkBox);
//                                if (empty)
//                                {
//                                    setGraphic(null);
//                                    setText(null);
//                                } else {
//
//
//                                    checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//                                        public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
//                                            DTOgrades dtoSelected = getTableView().getItems().get(getIndex());
//                                            if(checkBox.isSelected())
//                                            {
//                                                sendEmailTo.add(dtoSelected.getStudent().getID());
//                                            }
//                                            else
//                                            {
//                                                sendEmailTo.remove(dtoSelected.getStudent().getID());
//                                            }
//                                        }
//                                    });
//
//                                    setGraphic(checkBox);
//                                    setText(null);
//                                }
//                            }
//
//
//                        };
//                        return cell;
//                    }
//                };
//
//        columnCheck.setCellFactory(cellFactoryCheck);

        double widthInitial = 200;
        double heightInitial = 200;
        clipRectLeft = new Rectangle();
        clipRectLeft.setWidth(widthInitial);
        clipRectLeft.setHeight(0);
        clipRectLeft.translateYProperty().set(heightInitial);
        anchorPaneLeft.setClip(clipRectLeft);
        anchorPaneLeft.translateYProperty().set(-heightInitial);
        anchorPaneLeft.prefHeightProperty().set(0);
        extendableSearchPane.prefHeightProperty().set(0);
    }

    @Override
    public void update(Observable o, Object arg) {
        model.setAll(FXCollections.observableArrayList(service.getAllDtoGrades()));
        setPagination();
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

    public void handleSendEmailAll(ActionEvent actionEvent)
    {
        final Stage dialog = new Stage();
        dialog.setTitle("Sending emails");
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setWidth(500);
        dialog.setHeight(400);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        //paneTransparent.setVisible(true);
        FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/progressSendMails.fxml")));
        try {
            AnchorPane root=(AnchorPane)loader.load();
            ProgressController ctrl=loader.getController();
            Scene dialogScene = new Scene(root, 500, 400);
            dialog.setScene(dialogScene);
            ctrl.setService(service);
            ctrl.setEmailsAndCheckBoxes(sendEmailTo,checkBoxes);
            dialog.show();

            // ctrl.setPane(paneTransparent);

        } catch (IOException e) {
            e.printStackTrace();
        }
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


            row.createCell(0).setCellValue("Nume");
            row.createCell(1).setCellValue("Grupa");

            for (int j = 4; j < tableGrades.getColumns().size(); j++) {
                String col=((TableColumn) tableGrades.getColumns().get(j)).getText();
                row.createCell(j-2).setCellValue(col);
            }


            for (int i = 0; i < model.size(); i++) {
                row = spreadsheet.createRow(i + 1);
                DTOgrades nota= (DTOgrades) model.get(i);
                row.createCell(0).setCellValue(nota.getStudent().getNume());
                row.createCell(1).setCellValue(nota.getStudent().getGrupa());
                for (int j=0;j<nota.getGrades().size();j++)
                {
                    row.createCell(j+2).setCellValue(nota.getGrades().get(nota.getGrades().keySet().toArray()[j]).toString());
                }
            }

            FileOutputStream fileOut = new FileOutputStream(file.toString());
            workbook.write(fileOut);
            fileOut.close();
            //Platform.exit();
        }

    }
}
