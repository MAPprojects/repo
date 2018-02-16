package FXML;


import Domain.Profesor;
import Repository.ProfesorRepoSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;

public class ProfesorPartController {

    String user;
    String pass;
    ProfesorRepoSQL repoSQL=new ProfesorRepoSQL();

    public void setUser(String user,String pass){
        this.user = user;
        this.pass=pass;
    }
    @FXML
    private AnchorPane anchorView;
    @FXML
    private AnchorPane TF_PANE;


    @FXML
    private Button b_changePass;

    @FXML
    private Button b_logout;



    @FXML
    void handleChangePass(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ChangePasswordProfesor.fxml"));

        Parent root =  loader.load();

        ChangePasswordProfessor controller =loader.getController();
        controller.setuser(user,pass);

        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        controller.setStage(stage);
        stage.show();
        stage.setResizable(false);
    }

    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Scene loginScene=new Scene(root);
        Stage window=(Stage)((Node) event.getSource()).getScene().getWindow();
        window.close();
        window.setScene(loginScene);
        window.show();
        window.setResizable(false);

    }
    public void handleShowStudent(Event event) throws IOException {
        AnchorPane profesorTable = FXMLLoader.load(getClass().getResource("Students.fxml"));
        Scene profesorTable_scene=new Scene(profesorTable);
        profesorTable.autosize();
        profesorTable.setPrefSize(946,796);
        profesorTable.setMinHeight(796);
        profesorTable.setMinWidth(946);
        TF_PANE.getChildren().setAll(profesorTable);




//        Stage window=(Stage)((Node) event.getSource()).getScene().getWindow();
//
//        window.setScene(profesorTable_scene);
//        window.show();
//        window.setResizable(false);
    }


    @FXML
    void handleNoteGUI(ActionEvent  event) throws IOException {
//        AnchorPane noteTable = FXMLLoader.load(getClass().getResource("/FXML/Nota.fxml"));
////        Scene temeTable_scene=new Scene(temeTable);
//        //get tha Stage information
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/Nota.fxml"));

        AnchorPane noteTable =  loader.load();
        NotaControllerFXML controllerFXML= loader.getController();

        Stage window=(Stage)((Node) event.getSource()).getScene().getWindow();
        controllerFXML.setStage(window);
        controllerFXML.setPane(TF_PANE);
        noteTable.autosize();
        noteTable.setPrefSize(946,796);
        noteTable.setMinHeight(796);
        noteTable.setMinWidth(946);
        TF_PANE.getChildren().setAll(noteTable);
//        Stage window=(Stage)((Node) event.getSource()).getScene().getWindow();
//        window.setScene(temeTable_scene);
//        window.show();
//        window.setResizable(false);
    }



    @FXML
    void handleTemeGUI(ActionEvent event) throws IOException {
        AnchorPane temeTable = FXMLLoader.load(getClass().getResource("/FXML/Tema.fxml"));
        Scene temeTable_scene=new Scene(temeTable);


        temeTable.autosize();
        temeTable.setPrefSize(946,796);
        temeTable.setMinHeight(796);
        temeTable.setMinWidth(946);
        TF_PANE.getChildren().setAll(temeTable);
        //get tha Stage information
//        Stage window=(Stage)((Node) event.getSource()).getScene().getWindow();
//        window.setScene(temeTable_scene);
//        window.show();
//        window.setResizable(false);
    }
    @FXML
    private PieChart pieChart;

    @FXML
    void handleHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProfesorPart.fxml"));
        Parent profesorPart =  loader.load();

        Scene profesorPartScene = new Scene(profesorPart);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(profesorPartScene);

        window.show();
    }
    @FXML
    void handlePieChart(MouseEvent event) {
        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial white;");
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(data.getPieValue()) + "%");
                        }
                    });
        }
    }


    @FXML
    void initialize() throws SQLException {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();



        ObservableList<String> list=FXCollections.observableArrayList(repoSQL.numeProfesori());
        for (String str : list)
            pieChartData.add(new PieChart.Data(str, repoSQL.numeProfesoriElevi(str)));

        pieChart.setData(pieChartData);




    }
}
