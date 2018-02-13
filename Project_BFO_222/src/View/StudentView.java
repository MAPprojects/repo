package View;

import Domain.Student;

import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import View.StudentViewController;
import sun.java2d.loops.FillPath;
import sun.plugin.javascript.navig.Anchor;

import javax.swing.*;
import java.io.File;


public class StudentView {
    private StudentViewController sctr;
    private BorderPane view;
    TextField idField = createField();
    TextField numeField = createField();
    TextField grupaField = createField();
    TextField emailField = createField();
    TextField profesorField=createField();
    TableView<Student> tableView;

    Button buttonAdd=createButton("Adauga");
    Button buttonDelete=createButton("Sterge");
    Button buttonUpdate=createButton("Modifica");

    public StudentView(StudentViewController sctr){
        this.sctr=sctr;
        initView();
        setIcons();
    }

    private TableView<Student> createTableView(){
        TableView<Student> tableView=new TableView<>();
        TableColumn<Student,String> id=new TableColumn<>("ID");
        TableColumn<Student,String> nume=new TableColumn<>("Nume");
        TableColumn<Student,String> grupa=new TableColumn<>("Grupa");
        TableColumn<Student,String> email=new TableColumn<>("Email");
        TableColumn<Student,String> cadru=new TableColumn<>("Cadru didactic");


        tableView.getColumns().addAll(id,nume,grupa,email,cadru);
        id.setCellValueFactory(new PropertyValueFactory<Student,String >("id"));
        nume.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
        grupa.setCellValueFactory(new PropertyValueFactory<Student,String>("grupa"));
        email.setCellValueFactory(new PropertyValueFactory<Student,String>("email"));
        cadru.setCellValueFactory(new PropertyValueFactory<Student,String>("cadru"));
        tableView.setItems(sctr.getModel());

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                sctr.showDetails(newValue);
            }
        });
        return tableView;
    }

    private void initView(){
        view=new BorderPane();
        view.setTop(initTop());
        view.setBottom(initBottom());
    }
    private AnchorPane initBottom(){
        AnchorPane bottomAnchorPane=new AnchorPane();
        Label topLabel=createLabel("");
        topLabel.setText("Student");
        topLabel.setFont(Font.font("Verdana",30));
        topLabel.setTextFill(Color.BLUE);
        bottomAnchorPane.getChildren().add(topLabel);
        AnchorPane.setLeftAnchor(topLabel,338d);
        AnchorPane.setBottomAnchor(topLabel,30d);
        return bottomAnchorPane;
    }

    private AnchorPane initTop(){
        AnchorPane topAnchorPane=new AnchorPane();
        tableView =createTableView();
        topAnchorPane.getChildren().add(tableView);
        AnchorPane.setTopAnchor(tableView,0d);
        AnchorPane.setLeftAnchor(tableView,10d);
        GridPane grdP=createGridPain();
        topAnchorPane.getChildren().add(grdP);

        HBox hBox=createHBox();
        topAnchorPane.getChildren().add(hBox);
        AnchorPane.setTopAnchor(grdP,0d);
        AnchorPane.setLeftAnchor(grdP,410d);
        AnchorPane.setLeftAnchor(hBox,427d);
        AnchorPane.setTopAnchor(hBox,180d);

        File gifFile = new File("D:\\Map2017\\Lab3\\giphy2.gif");
        Image imageGif = new Image(gifFile.toURI().toString());
        ImageView gif=new ImageView(imageGif);

        gif.setFitHeight(183);
        gif.setFitWidth(330);

        topAnchorPane.getChildren().add(gif);
        AnchorPane.setTopAnchor(gif,230d);
        AnchorPane.setLeftAnchor(gif,427d);

        return topAnchorPane;
    }

    private HBox createHBox() {
        HBox hBox = new HBox();

        buttonAdd.setPrefSize(100,35);
        buttonDelete.setPrefSize(100,35);
        buttonUpdate.setPrefSize(100,35);

        hBox.getChildren().addAll(buttonAdd,buttonDelete,buttonUpdate);

        buttonAdd.setOnAction(sctr::addStudent);
        buttonDelete.setOnAction(sctr::deleteStudent);
        buttonUpdate.setOnAction(sctr::updateStudent);
        return hBox;
    }

    private GridPane createGridPain(){
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.add(createLabel("ID:"), 0, 0);
        gridPane.add(createLabel("NUME:"), 0, 1);
        gridPane.add(createLabel("GRUPA:"), 0, 2);
        gridPane.add(createLabel("EMAIL:"), 0, 3);
        gridPane.add(createLabel("PROF.:"), 0, 4);

        gridPane.add(idField, 1, 0);
        gridPane.add(numeField, 1, 1);
        gridPane.add(grupaField, 1, 2);
        gridPane.add(emailField, 1, 3);
        gridPane.add(profesorField, 1, 4);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPrefWidth(100d);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPrefWidth(200d);

        gridPane.getColumnConstraints().addAll(c1,c2);


        return gridPane;
    }

    private Label createLabel(String denumire){
        Label lbl=new Label();
        lbl.setText(denumire);
        lbl.setFont(Font.font("Arial",20));
        return lbl;
    }

    public BorderPane getView(){
        return view;
    }

    private TextField createField(){
        TextField txt=new TextField();
        return txt;
    }

    private void setIcons(){
        //add
        File imageFile = new File("D:\\Map2017\\Lab3\\add3.png");
        Image imageAdd = new Image(imageFile.toURI().toString());
        ImageView add=new ImageView(imageAdd);
        add.setFitHeight(40d);
        add.setFitWidth(30d);
        buttonAdd.setGraphic(add);

        //delete
        File imageFile2 = new File("D:\\Map2017\\Lab3\\delete3.png");
        Image imageDelete = new Image(imageFile2.toURI().toString());
        ImageView delete=new ImageView(imageDelete);
        delete.setFitHeight(40d);
        delete.setFitWidth(30d);
        buttonDelete.setGraphic(delete);

        //update
        File imageFile3 = new File("D:\\Map2017\\Lab3\\updateF.png");
        Image imageUpdate = new Image(imageFile3.toURI().toString());
        ImageView update=new ImageView(imageUpdate);
        update.setFitHeight(40d);
        update.setFitWidth(30d);
        buttonUpdate.setGraphic(update);


    }
    private Button createButton(String text){
        Button button=new Button(text);
        return button;
    }
}
