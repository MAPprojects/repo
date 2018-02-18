package View;

import Controller.StudentController;
import Domain.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class StudentView
{
    StudentController controller;
    BorderPane borderPane;
    TableView<Student> tableView;

    GridPane gridPaneFields;
    Label labelID;
    Label labelNume;
    Label labelGrupa;
    Label labelEmail;
    Label labelCadruDidactic;

    public TextField textFieldID;
    public TextField textFieldNume;
    public TextField textFieldGrupa;
    public TextField textFieldEmail;
    public TextField textFieldProf;

    Button buttonAdd;
    Button buttonDelete;
    Button buttonUpdate;
    Button buttonClear;
    Button buttonFilter;

    public ComboBox<String> comboBoxFilter;
    public TextField textFieldFilter;

    public StudentView(StudentController controller)
    {
        this.controller=controller;
        initView();
    }

    private void initView() {
        borderPane = new BorderPane();
        borderPane.setLeft(initLeft());
        borderPane.setCenter(initCenter());

    }

    private AnchorPane initCenter() {
        AnchorPane anchorCenterPane=new AnchorPane();

        gridPaneFields=new GridPane();
        gridPaneFields=createGrid();
        gridPaneFields.setHgap(20d);
        gridPaneFields.setVgap(10d);

        createButtons();
        VBox vbox=new VBox();
        AnchorPane.setTopAnchor(vbox,20d);
        AnchorPane.setLeftAnchor(vbox,20d);

        HBox hbox1=new HBox();
        hbox1.setSpacing(10);
        hbox1.getChildren().addAll(buttonAdd,buttonUpdate,buttonDelete,buttonClear);
        hbox1.setSpacing(10);
        vbox.setSpacing(50);
        vbox.getChildren().addAll(gridPaneFields,hbox1);

        HBox hBox2=new HBox();
        comboBoxFilter=new ComboBox<>();
        comboBoxFilter.getItems().setAll("Fara filtru","Filtreaza dupa nume student","Filtreaza dupa profesor","Filtreaza dupa grupa");
        comboBoxFilter.getSelectionModel().selectFirst();
        textFieldFilter=new TextField();
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(comboBoxFilter,textFieldFilter,buttonFilter);
        vbox.getChildren().add(hBox2);
        anchorCenterPane.getChildren().add(vbox);
        return anchorCenterPane;
    }

    private GridPane createGrid() {
        createLabels();
        createTextFields();
        gridPaneFields.add(labelID,0,0);
        gridPaneFields.add(labelNume,0,1);
        gridPaneFields.add(labelGrupa,0,2);
        gridPaneFields.add(labelEmail,0,3);
        gridPaneFields.add(labelCadruDidactic,0,4);

        gridPaneFields.add(textFieldID,1,0);
        gridPaneFields.add(textFieldNume,1,1);
        gridPaneFields.add(textFieldGrupa,1,2);
        gridPaneFields.add(textFieldEmail,1,3);
        gridPaneFields.add(textFieldProf,1,4);

        return gridPaneFields;
    }

    private AnchorPane initLeft()
    {
        AnchorPane anchorPaneLeft=new AnchorPane();
        tableView=new TableView<>();
        tableView=createTableView();
        anchorPaneLeft.getChildren().add(tableView);
        AnchorPane.setTopAnchor(tableView,20d);
        AnchorPane.setLeftAnchor(tableView,20d);
        return anchorPaneLeft;
    }

    private TableView<Student> createTableView() {
        TableColumn<Student,String> columnNume=new TableColumn<>("Nume");
        TableColumn<Student,Integer> columnGrupa=new TableColumn<>("Grupa");
        TableColumn<Student,String> columnEmail=new TableColumn<>("Email");
        TableColumn<Student,String> columnCadruDidactic=new TableColumn<>("Profesor");
        tableView.getColumns().addAll(columnNume,columnGrupa,columnEmail,columnCadruDidactic);

        columnNume.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
        columnGrupa.setCellValueFactory(new PropertyValueFactory<Student,Integer>("grupa"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<Student,String >("email"));
        columnCadruDidactic.setCellValueFactory(new PropertyValueFactory<Student,String>("cadruDidactic"));

        //set items cu lista de studenti
        tableView.setItems(controller.getModelStudent());
        tableView.setPrefSize(377,500);

        //element selectat ->fields
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                controller.fillFiledsFromSelectedItem(newValue);
            }
        });

        return tableView;


    }
    private void createLabels()
    {
        labelID=new Label("ID: ");
        labelNume=new Label("Nume: ");
        labelGrupa=new Label("Grupa: ");
        labelEmail=new Label("Email: ");
        labelCadruDidactic=new Label("Profesor: ");
    }
    private void createTextFields()
    {
        textFieldID=new TextField();
        textFieldNume=new TextField();
        textFieldGrupa=new TextField();
        textFieldEmail=new TextField();
        textFieldProf=new TextField();
    }
    private void createButtons()
    {
        buttonAdd=new Button("Adauga");
        buttonAdd.setOnAction(controller::handleAddStudentFromFilds);
        buttonDelete=new Button("Sterge");
        buttonDelete.setOnAction(controller::handleDeleteStudentSelected);
        buttonUpdate=new Button("Modifica");
        buttonUpdate.setOnAction(controller::handleUpdateStudentFromFields);
        buttonClear=new Button("Refresh");
        buttonClear.setOnAction(controller::handleClearFields);
        buttonFilter=new Button("Go");
        buttonFilter.setOnAction(controller::handleFiler);
    }
    public BorderPane getView()
    {
        return borderPane;
    }
}
