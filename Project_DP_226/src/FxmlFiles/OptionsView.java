package FxmlFiles;

import Entites.Option;
import Entites.Section;
import Service.Service;
import Utils.DBConnection;
import Utils.Event;
import Utils.Observer;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

public class OptionsView implements Observer<Event> {
    private Service service;
    @FXML
    public TableView<Option> optionsView;
    public TableColumn<Option, Integer> idColumn;
    public TableColumn<Option, Integer> idCandColumn;
    public TableColumn<Option, Integer> idSecColumn;
    public TableColumn<Option, Integer> priorityColumn;
    private ObservableList<Option> model;
    private Stage editStage;
    public TextField filter;
    public ComboBox<String> comboSorter;
    private Connection connection = null;
    private PreparedStatement pst = null;
    public FontAwesomeIconView addButton;
    public FontAwesomeIconView updateButton;
    public FontAwesomeIconView deleteButton;
    private LinkedHashSet<TableRow<Option>> rows = new LinkedHashSet<>();
    private int firstIndex;
    private int lastIndex = 0;
    public boolean isAdmin;

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
//        idCandColumn.setCellValueFactory(new PropertyValueFactory<>("idCandidate"));
//        String sql="select * \n" +
//                "from Candidates\n" +
//                "inner join Options on candidateId=Candidates.id";
//        try {
//            pst=connection.prepareStatement(sql);
//            ResultSet rs=pst.executeQuery();
//            //setez o intreaga coloana
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        idSecColumn.setCellValueFactory(new PropertyValueFactory<>("idSection"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        optionsView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterButtonHandler();
        });

        ObservableList<String> listCombo2 = FXCollections.observableArrayList("Section", "none");
        comboSorter.setItems(listCombo2);
        connection = DBConnection.connect();
        optionsView.addEventFilter(ScrollEvent.ANY, scrollEvent -> manageData());
        optionsView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if ((event.getTarget() instanceof TableColumnHeader) | event.isDragDetect()) {
                manageData();
            }
        });

    }

    @FXML
    private void manageData() {
        if (lastIndex + 3 < this.getVisibleRows()) {
            lastIndex = this.getVisibleRows();
            service.alertLoadData();
            loadData();
            //   System.out.println("load data"+lastIndex + "  "+ getVisibleRows());
        } else {
            // System.out.println("scrool up,"+lastIndex +" "+ getVisibleRows());


        }

    }

    @FXML
    public int getVisibleRows() {
        TableViewSkin<?> skin = (TableViewSkin<?>) optionsView.getSkin();
        if (skin == null) return 0;
        VirtualFlow<?> flow = (VirtualFlow<?>) skin.getChildren().get(1);
        int idxFirst;
        int idxLast;
        if (flow != null &&
                flow.getFirstVisibleCellWithinViewPort() != null &&
                flow.getLastVisibleCellWithinViewPort() != null) {
            idxFirst = flow.getFirstVisibleCellWithinViewPort().getIndex();
            if (idxFirst > optionsView.getItems().size()) {
                idxFirst = optionsView.getItems().size() - 1;
            }
            idxLast = flow.getLastVisibleCellWithinViewPort().getIndex();
            if (idxLast > optionsView.getItems().size()) {
                idxLast = optionsView.getItems().size() - 1;
            }
        } else {
            idxFirst = 0;
            idxLast = 0;
        }
        return idxLast;
    }

    public void setService(Service service) {

        this.service = service;
        this.service.addObserver(this);
        loadData();

    }


    private void loadData() {
        ArrayList<Option> list = service.findAllOptions();
        model = FXCollections.observableArrayList(list);
        optionsView.setItems(model);

        if (!isAdmin) {
            addButton.setDisable(true);
            addButton.setVisible(false);
            updateButton.setDisable(true);
            updateButton.setVisible(false);
            deleteButton.setDisable(true);
            deleteButton.setVisible(false);
        }
    }

    @FXML
    private void addButtonHandler() {
        showAddStage();
    }

    private void showAddStage() {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Controller.class.getResource("AddOptionView.fxml"));

            AnchorPane editPane = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(editPane));
            Image image = new Image("img2.png");
            stage.getIcons().add(image);
            AddOptionView ctrl = loader.getController();
            ctrl.setService(service, stage);
            ctrl.addObserver(this);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        loadData();
    }

    @FXML
    private void updateButtonHandler() {
        Option option = optionsView.getSelectionModel().getSelectedItem();
        if (option == null) {
            showErrorMessage("Select one item first!");
        } else
            showEditStage(option);
    }

    private void showEditStage(Option option) {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Controller.class.getResource("EditOptionView.fxml"));

            AnchorPane editPane = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(editPane));
            Image image = new Image("img2.png");
            stage.getIcons().add(image);
            EditOption ctrl = loader.getController();
            ctrl.setService(service, stage, option);
            ctrl.addObserver(this);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteButtonHandler() {
        Option option = optionsView.getSelectionModel().getSelectedItem();
        int index = optionsView.getSelectionModel().getFocusedIndex();
        try {

            service.deleteOption(option.getID());
            optionsView.getSelectionModel().select(index == model.size() ? index - 1 : index);
            showMessage(Alert.AlertType.INFORMATION, "You deleted:" + option);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
    }

    @FXML
    public void filterButtonHandler() {
        List<Option> result = null;
        String value = filter.getText();
        if (value.equals(""))
            result = service.findAllOptions();
        else
            try {
                result = service.filterByIdCandidate(Integer.parseInt(value));
            } catch (Exception e) {
                showErrorMessage("Must be integer!Please give a number ;)");
            }
        model.setAll(result);
    }

    @FXML
    public void sortHandler() {
        List<Option> result;
        if (comboSorter.getSelectionModel().getSelectedItem().equals("Section")) {
            result = service.sortByIdSection();
        } else
            result = service.findAllOptions();
        model.setAll(result);

    }

    @FXML
    public void generateHandler() {
        try {
            PDFont font = PDType1Font.HELVETICA;
            float fontSize = 14;
            float fontHeight = fontSize;
            float leading = 20;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            Date date = new Date();

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.setFont(font, fontSize);

            final float[] yCordinate = {page.getCropBox().getUpperRightY() - 30};
            float startX = page.getCropBox().getLowerLeftX() + 30;
            float endX = page.getCropBox().getUpperRightX() - 30;

            contentStream.beginText();
            contentStream.newLineAtOffset(startX, yCordinate[0]);
            contentStream.showText("Top 3 cele mai solicitate sectii");
            yCordinate[0] -= fontHeight;
            contentStream.newLineAtOffset(0, -leading);
            yCordinate[0] -= leading;
            contentStream.showText("Data: " + dateFormat.format(date));
            yCordinate[0] -= fontHeight;
            contentStream.endText(); // End of text mode

            contentStream.moveTo(startX, yCordinate[0]);
            contentStream.lineTo(endX, yCordinate[0]);
            contentStream.stroke();
            yCordinate[0] -= leading;
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, yCordinate[0]);
            contentStream.showText("Nume: Pop Daniela");
            yCordinate[0] -= leading;
            ArrayList<Section> sectionList = new ArrayList<>();
            sectionList = this.top();
            sectionList.forEach(x -> {
                try {
                    contentStream.newLineAtOffset(0, -leading);
                    contentStream.showText(x.getID().toString() + " " + x.getName());
                    yCordinate[0] -= leading;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            contentStream.endText();

            contentStream.close();
            doc.save("raport.pdf");
            showMessage(Alert.AlertType.INFORMATION, "Now you have your most wanted Sections in pdf file.Check 'raport.pdf' from this folder! ");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Section> top() {
        String sql = "SELECT count(sectionId) as m,sectionId FROM Options group by sectionId Order by m desc";

        ArrayList<Section> sectionList = new ArrayList<>();
        try {
            pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int last = 10000;
            int count=0;
            while (rs.next() && count<3) {
                Integer m = rs.getInt("m");
                if (m < last) {
                    Integer sectionId = rs.getInt("sectionId");
                    String sql2="Select * from Sections where id=?";
                    pst = connection.prepareStatement(sql2);
                    pst.setInt(1,sectionId);

                    ResultSet rs2 = pst.executeQuery();
                    while(rs2.next()) {
                        String name = rs2.getString("name");
                        int number = rs2.getInt("number");
                        Section section = new Section(sectionId, name, number);
                        sectionList.add(section);
                        count++;
                    }
                    System.out.println(m + " " + sectionId + "\n");
                    last = m;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sectionList;
    }

    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(editStage);
        message.setContentText(text);
        message.showAndWait();
    }

    private void showMessage(Alert.AlertType type, String text) {
        Alert message = new Alert(type);
        message.setHeaderText("");
        message.setContentText(text);
        message.showAndWait();
    }

    @Override
    public void notifyOnEvent(Event event) {
        loadData();
    }
}
