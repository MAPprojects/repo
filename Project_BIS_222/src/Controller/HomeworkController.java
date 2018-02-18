package Controller;

import Domain.Student;
import Domain.Tema;
import Domain.User;
import Service.Service;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class HomeworkController implements Observer
{
     Service service;
     ObservableList<Tema> modelHomework;
    User activeUser;
    @FXML private AnchorPane extendableSearchPane;
    @FXML private AnchorPane anchorPaneLeft;
    @FXML private AnchorPane anchorPaneRight;
    @FXML TableView tableHomework;
    @FXML TableColumn columnDescription;
    @FXML TableColumn columnDeadline;
    @FXML TableColumn columnAction;
    @FXML TableColumn columnNumber;
    @FXML TextField txtSearchDescription;
    @FXML TextField txtSearchDeadline;
    @FXML TextField txtNumber;
    @FXML TextArea txtDescription;
    @FXML TextField txtDeadline;
    @FXML RadioButton radioAvailable;
    @FXML RadioButton radioExpired;
    @FXML Pagination pagHomework;
    @FXML ComboBox<String> comboPerPage;
    @FXML Button buttonNew;

    private Rectangle clipRectLeft;
    private Rectangle clipRectRight;
    private int coborari=0;


     public HomeworkController()
     {

     }
    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
        if(activeUser.getRol().equals("vizitator"))
        {
            buttonNew.setVisible(false);
        }

    }

    public void setService(Service service)
     {
         this.service=service;
         modelHomework=FXCollections.observableArrayList(service.iterableToArrayList(service.getAllHomeworks()));
         tableHomework.setItems(modelHomework);
         setPagination();
     }

    private void setPagination()
    {
        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int cat=modelHomework.size()/perPage;
        if(cat*perPage!=modelHomework.size())cat=cat+1;
        pagHomework.setPageCount(cat);
        pagHomework.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex)
    {
        int perPage=Integer.parseInt(comboPerPage.getSelectionModel().getSelectedItem());
        int fromIndex=pageIndex*perPage;
        int toIndex=Math.min(fromIndex+perPage,modelHomework.size());
        tableHomework.setItems(FXCollections.observableArrayList(modelHomework.subList(fromIndex,toIndex)));
        return new BorderPane(tableHomework);
    }

     @FXML
    public void initialize()
     {
         comboPerPage.getItems().addAll("10","20","30");
         comboPerPage.getSelectionModel().selectFirst();
         comboPerPage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
             @Override
             public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
             {
                 setPagination();
             }
         });

        // paneTransparent.setVisible(false);
         columnNumber.setCellValueFactory(new PropertyValueFactory<Tema,String>("iD"));
         columnDescription.setCellValueFactory(new PropertyValueFactory<Tema,String>("descriere"));
         columnDeadline.setCellValueFactory(new PropertyValueFactory<Tema,Integer>("deadline"));
         columnAction.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

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

         txtSearchDescription.textProperty().addListener(new ChangeListener<String>() {
             @Override
             public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                 int deadline;
                 if(txtSearchDeadline.getText().equals(""))deadline=-1;
                 else deadline=Integer.parseInt(txtSearchDeadline.getText());
                 modelHomework = FXCollections.observableArrayList(service.filterAllHomework(newValue,deadline));
                 tableHomework.setItems(modelHomework);
                 setPagination();

             }
         });
         txtSearchDeadline.textProperty().addListener(new ChangeListener<String>() {
             @Override
             public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                 int deadline;
                 if(newValue.equals(""))deadline=-1;
                 else deadline=Integer.parseInt(txtSearchDeadline.getText());
                 modelHomework = FXCollections.observableArrayList(service.filterAllHomework(txtSearchDescription.getText(),deadline));
                 tableHomework.setItems(modelHomework);
                 setPagination();
             }
         });

        radioExpired.setOnAction(this::handleFilterRadios);
        radioAvailable.setOnAction(this::handleFilterRadios);


         Callback<TableColumn<Tema, String>, TableCell<Tema, String>> cellFactory=
                 new Callback<TableColumn<Tema, String>, TableCell<Tema, String>>() {
                     @Override
                     public TableCell call(final TableColumn<Tema, String> param) {
                         final TableCell<Tema, String> cell = new TableCell<Tema, String>() {

                             final Button buttonDelete = new Button();
                             final Button buttonUpdate=new Button();

                             @Override
                             public void updateItem(String item, boolean empty) {
                                 super.updateItem(item, empty);
                                 if (empty)
                                 {
                                     setGraphic(null);
                                     setText(null);
                                 }
                                 else {
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
                                             ctrl.labelAreUSure.setText("Are you sure you want to delete this homework?");
                                             ctrl.buttonYesDelete.setOnAction(ctrl::handleYesDeleteHomework);
                                             ctrl.buttonYesDelete.setText("Yes, delete homework");
                                             ctrl.buttonCancelDelete.setText("No, cancel");
                                             Tema tema = getTableView().getItems().get(getIndex());
                                             ctrl.setHomeworkToDelete(tema);
                                             ctrl.setService(service);
                                             //ctrl.setPane(paneTransparent);
                                             Scene dialogScene = new Scene(root, 350, 204);
                                             dialog.setScene(dialogScene);
                                             dialog.show();
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                         modelHomework=FXCollections.observableArrayList(service.iterableToArrayList(service.getAllHomeworks()));
                                         tableHomework.setItems(modelHomework);
                                         setPagination();
                                     });
                                     Image imageDel = new Image("FXML/delete.png", 20, 20, false, false);
                                     buttonDelete.setGraphic(new ImageView(imageDel));
                                     buttonDelete.getStylesheets().add("FXML/buttonTable.css");

                                     Image imageUpdate = new Image("FXML/update.png", 20, 20, false, false);
                                     buttonUpdate.setGraphic(new ImageView(imageUpdate));
                                     buttonUpdate.setId("buttonUpdate");
                                     buttonUpdate.getStylesheets().add("FXML/buttonTable.css");
                                     buttonUpdate.setOnAction(event->{
                                         //paneTransparent.setVisible(true);
                                         final Stage dialog = new Stage();
                                         dialog.setTitle("Update student");
                                         dialog.initStyle(StageStyle.UNDECORATED);
                                         dialog.setWidth(470);
                                         dialog.setHeight(350);
                                         dialog.setResizable(false);
                                         dialog.initModality(Modality.APPLICATION_MODAL);
                                         FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/updateWindowHomework.fxml")));
                                         try {
                                             AnchorPane root=(AnchorPane)loader.load();
                                             UpdateHomeworkController ctrl=loader.getController();
                                             ctrl.setService(service);
                                             //ctrl.setPane(paneTransparent);
                                             Tema tema = getTableView().getItems().get(getIndex());
                                             ctrl.setFieldsForUpdate(tema.getID(),tema.getDescriere(),tema.getDeadline());
                                             Scene dialogScene = new Scene(root, 470, 350);
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

     }

    @Override
    public void update(Observable o, Object arg) {
            modelHomework.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getAllHomeworks())));
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

    public void handleFilterRadios(ActionEvent actionEvent)
    {
        String desc=txtSearchDescription.getText();
        if(radioExpired.isSelected())
        {
            modelHomework.setAll(FXCollections.observableArrayList(service.filterByDeadlineAll(true,desc)));
        }
        if(radioAvailable.isSelected())
        {
            modelHomework.setAll(FXCollections.observableArrayList(service.filterByDeadlineAll(false,desc)));
        }
        setPagination();
    }

    public void handleNoFilers(MouseEvent mouseEvent)
    {
        radioAvailable.setSelected(false);
        radioExpired.setSelected(false);
        txtSearchDeadline.setText("");
        txtSearchDescription.setText("");
        modelHomework.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getAllHomeworks())));
        setPagination();
    }

    public void  handleAddHomework(ActionEvent actionEvent)
    {
        try
        {
            Integer id=Integer.parseInt(txtNumber.getText());
            String descrption=txtDescription.getText();
            int deadline=Integer.parseInt(txtDeadline.getText());
            service.addTema(id,descrption,deadline);
            setPagination();
        }
        catch (NumberFormatException ne)
        {
            showError("Atributele temei nu sunt valide\n ID, deadline: numere intregi pozitive\n descriere: text");
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


    public void exportToExcel(MouseEvent event) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose location");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("EXCEL files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(new Stage());

        if(file!=null)
        {
            Workbook workbook = new HSSFWorkbook();
            CellStyle cs = workbook.createCellStyle();
            cs.setWrapText(true);
//            cell.setCellStyle(cs);
            Sheet spreadsheet = workbook.createSheet("sample");

            Row row = spreadsheet.createRow(0);
            row.setHeightInPoints((4*spreadsheet.getDefaultRowHeightInPoints()));
            for (int j = 0; j < tableHomework.getColumns().size()-1; j++) {
                row.createCell(0).setCellValue("Nr");
                row.createCell(1).setCellValue("Descriere");
                row.createCell(2).setCellValue("Deadline");
            }

            for (int i = 0; i < modelHomework.size(); i++) {
                row = spreadsheet.createRow(i + 1);
                Tema tema= (Tema) modelHomework.get(i);
                row.createCell(0).setCellValue(tema.getID());
                Cell cell= row.createCell(1);
                cell.setCellStyle(cs);
                cell.setCellValue(tema.getDescriere());

                row.createCell(2).setCellValue(tema.getDeadline());

            }

            FileOutputStream fileOut = new FileOutputStream(file.toString());
            workbook.write(fileOut);
            fileOut.close();
        }

    }


}
