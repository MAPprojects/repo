package viewController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import entities.Candidat;
import entities.CheieOptiune;
import entities.Optiune;
import entities.Sectie;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.NodeList;
import repository.*;
import service.AbstractService;
import service.CandidatService;
import service.OptiuneService;
import service.SectieService;
import validator.CandidatValidator;
import validator.OptiuneValidator;
import validator.SectieValidator;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class MainWindowController {

    @FXML
    Tab candidatiTab;
    @FXML
    Tab sectiiTab;
    @FXML
    Tab optiuniTab;
    @FXML
    JFXTextField quickSearchTextField;
    @FXML
    ImageView magnifierPng;
    @FXML
    AnchorPane candidatiAnchor;
    @FXML
    JFXButton toolsButton,addButton,deleteButton,modifyButton,mailButton,logoutButton,reportsButton,filterButton,resetButton;
    @FXML
    JFXTabPane tabPane;

    JFXNodesList nodesList1;
    private AbstractService<Candidat,Integer> candidatService;
    private AbstractService<Sectie,Integer> sectieService;
    private AbstractService<Optiune,CheieOptiune> optiuneService;
    Object currentController;
    private Stage currentFilterStage;
    private DataBaseConnection dataBaseConnection;
    TableTipes currentType=TableTipes.CANDIDAT;
    Stage stage;

    public void initialize() {

        //dataBaseConnection = new DataBaseConnection();

        nodesList1 = new JFXNodesList();
        quickSearchTextField.toFront();
        //quickSearchTextField.setPromptText("Cautare");
        magnifierPng.toFront();

        nodesList1.addAnimatedNode(toolsButton);
        nodesList1.addAnimatedNode(addButton);
        nodesList1.addAnimatedNode(deleteButton);
        nodesList1.addAnimatedNode(modifyButton);
        nodesList1.setSpacing(10);
        //nodesList1.setRotate(180);

        //candidatiTab.setContent(nodesList1);

        initCandidatTable();

       // candidatiTab.setContent(nodesList1);

    }

    public void setStage(Stage stage){
        this.stage=stage;
    }

    private void loadRandomCandidatData(int rows){
        ArrayList<String> firstName = new ArrayList<>();
        ArrayList<String> secondName = new ArrayList<>();
        ArrayList<String> thirdName = new ArrayList<>();
        ArrayList<String> mailDomains = new ArrayList<>();

        firstName.add("Adelin");
        firstName.add("Achim");
        firstName.add("Adrian");
        firstName.add("Anastasiu");
        firstName.add("Anton");
        firstName.add("Augustin");
        firstName.add("Barbu");
        firstName.add("Calin");
        firstName.add("Dinu");
        firstName.add("Eric");
        firstName.add("Eusebiu");
        firstName.add("Felix");
        firstName.add("Flavian");
        firstName.add("Florentin");
        firstName.add("Horatiu");
        firstName.add("Iancu");
        firstName.add("Florentina");
        firstName.add("Floriana");
        firstName.add("Iarina");
        firstName.add("Ines");
        firstName.add("Ionela");
        firstName.add("Karla");
        firstName.add("Lacrima");
        firstName.add("Larisa");
        secondName.add("Albu");
        secondName.add("Andronic");
        secondName.add("Benga");
        secondName.add("Bentoiu");
        secondName.add("Bercea");
        secondName.add("Burcea");
        secondName.add("Cernescu");
        secondName.add("Cozma");
        secondName.add("Croitoru");
        secondName.add("Cuza");
        secondName.add("Danciu");
        secondName.add("Buzatu");
        secondName.add("Butnaru");
        thirdName.add("Anania");
        thirdName.add("Alda");
        thirdName.add("Barnabas");
        thirdName.add("Dada");
        thirdName.add("Evgeniya");
        thirdName.add("Valerie");
        thirdName.add("Zino");
        thirdName.add("Areta");
        thirdName.add("Clement");
        mailDomains.add("mail");
        mailDomains.add("yahoo");
        mailDomains.add("gmail");
        mailDomains.add("hotmail");

        int n=0;
        int nr;
        int firstId=0;
        Random random = new Random();
        try {
            Statement st = dataBaseConnection.getConnection().createStatement();
            String sql = "SELECT MAX(IdC) FROM Candidat";
            ResultSet rs = st.executeQuery(sql);
            if(rs.next())
                firstId=Integer.parseInt(rs.getString(1))+1;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        int minId;
        while(rows!=0) {
            n=9;
            String selectedFirstName = "";
            String selectedSecondName = "";
            String selectedThirdName = "";
            String selectedEmail = "";
            String phoneNumber = "0";
            nr = random.nextInt(firstName.size());
            selectedFirstName = firstName.get(nr);
            nr = random.nextInt(secondName.size());
            selectedSecondName = secondName.get(nr);
            nr = random.nextInt(thirdName.size());
            selectedThirdName = thirdName.get(nr);
            nr = random.nextInt(mailDomains.size());
            selectedEmail = mailDomains.get(nr);
            String name = selectedFirstName + " " + selectedSecondName + " " + selectedThirdName;
            String email = selectedFirstName.toLowerCase() + "." + selectedSecondName.toLowerCase() + "@" + selectedEmail + ".com";
            while(n!=0)
            {
                phoneNumber = phoneNumber + String.valueOf(random.nextInt(10));
                n = n - 1;
            }
            candidatService.addEntity(new Candidat(firstId,name,phoneNumber,email));
            firstId = firstId + 1;
            rows = rows - 1;
        }
    }

    private VBox buildButtonBar(){
        VBox buttonBar = new VBox();
        buttonBar.setSpacing(10);
        buttonBar.getChildren().add(nodesList1);
        buttonBar.getChildren().add(mailButton);
        buttonBar.getChildren().add(reportsButton);
        buttonBar.getChildren().add(filterButton);
        buttonBar.getChildren().add(resetButton);
        buttonBar.getChildren().add(logoutButton);
        buttonBar.setTranslateX(547);
        buttonBar.setTranslateY(20);
        mailButton.setTranslateX(-8);
        mailButton.setDisable(true);
        reportsButton.setTranslateX(-12);
        reportsButton.setTranslateY(-7);
        filterButton.setTranslateX(-6);
        filterButton.setTranslateY(-12);
        resetButton.setTranslateX(-6);
        resetButton.setTranslateY(-20);
        logoutButton.setTranslateX(-7);
        logoutButton.setTranslateY(-25);
        return buttonBar;
    }

    @FXML
    private void initCandidatTable() {
        try {
            CandidatValidator validator = new CandidatValidator();
            AbstractRepository<Integer, Candidat> repository = new CandidatRepositoryDB(validator,dataBaseConnection);
            candidatService = new CandidatService(repository);
            //loadRandomCandidatData(200);
            // Load student viewController.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(appStart.Main.class.getResource("/candidatTable.fxml"));
            AnchorPane centerLayout = (AnchorPane) loader.load();

            //set the service and the model for controller class
            CandidatTableController candidatTableController = loader.getController();
            candidatService.addObserver(candidatTableController);
            candidatTableController.setService(candidatService,this);
            currentController=candidatTableController;

            AnchorPane anchorPane =new AnchorPane();
            anchorPane.getChildren().add(centerLayout);
            anchorPane.getChildren().add(buildButtonBar());


            //mainBorderPane.setCenter(centerLayout);
            //candidatiAnchor.getChildren().add(centerLayout);

            candidatiTab.setContent(anchorPane);
            createCandidatFilterController();
            modifyButton.setDisable(true);
            deleteButton.setDisable(true);
            mailButton.setDisable(true);
            currentType=TableTipes.CANDIDAT;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initSectieTable() {
        try {
            SectieValidator validator = new SectieValidator();
            AbstractRepository<Integer, Sectie> repository = new SectieRepositoryDB(validator,dataBaseConnection);
            sectieService = new SectieService(repository);
            // Load student viewController.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(appStart.Main.class.getResource("/sectieTable.fxml"));
            AnchorPane centerLayout = (AnchorPane) loader.load();

            //set the service and the model for controller class
            SectieTableController sectieTableController = loader.getController();
            sectieService.addObserver(sectieTableController);

            sectieTableController.setService(sectieService,this);

            currentController=sectieTableController;

            AnchorPane anchorPane =new AnchorPane();
            anchorPane.getChildren().add(centerLayout);
            anchorPane.getChildren().add(buildButtonBar());

            sectiiTab.setContent(anchorPane);
            //mainBorderPane.setCenter(centerLayout);

            createSectieFilterController();
            modifyButton.setDisable(true);
            deleteButton.setDisable(true);
            currentType=TableTipes.SECTIE;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initOptiuneTable() {
        try {
            optiuneService = loadOptiuneService();
            // Load student viewController.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(appStart.Main.class.getResource("/optiuneTable.fxml"));
            AnchorPane centerLayout = (AnchorPane) loader.load();

            //set the service and the model for controller class
            OptiuneTableController optiuneTableController = loader.getController();
            optiuneService.addObserver(optiuneTableController);
            optiuneTableController.setService(optiuneService,this);
            currentController=optiuneTableController;

            AnchorPane anchorPane =new AnchorPane();
            anchorPane.getChildren().add(centerLayout);
            anchorPane.getChildren().add(buildButtonBar());

            optiuniTab.setContent(anchorPane);
            //mainBorderPane.setCenter(centerLayout);

            createOptiuneFilterController();
            modifyButton.setDisable(true);
            deleteButton.setDisable(true);
            currentType=TableTipes.OPTIUNE;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OptiuneService loadOptiuneService(){
        OptiuneValidator validator = new OptiuneValidator();
        AbstractRepository<CheieOptiune, Optiune> repository = new OptiuneRepositoryDB(validator,dataBaseConnection);
        if(candidatService==null)
        {
            CandidatValidator candidatValidator = new CandidatValidator();
            AbstractRepository<Integer, Candidat> candidatRepositoryFileepository = new CandidatRepositoryDB(candidatValidator,dataBaseConnection);
            candidatService = new CandidatService(candidatRepositoryFileepository);
        }
        if(sectieService==null)
        {
            SectieValidator sectieValidator = new SectieValidator();
            AbstractRepository<Integer, Sectie> sectieRepositoryFile = new SectieRepositoryDB(sectieValidator,dataBaseConnection);
            sectieService = new SectieService(sectieRepositoryFile);
        }
        return new OptiuneService(repository,candidatService,sectieService);
    }



    public void showCandidatDialog(Candidat candidat) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DetailedCandidatController.class.getResource("/detailedCandidat.fxml"));
            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            DetailedCandidatController detailedCandidatController = loader.getController();
            detailedCandidatController.setService((CandidatService) candidatService, dialogStage,candidat);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSectieDialog(Sectie sectie) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DetailedCandidatController.class.getResource("/detailedSectie.fxml"));
            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            DetailedSectieController detailedSectieController = loader.getController();
            detailedSectieController.setService((SectieService) sectieService, dialogStage,sectie);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOptiuneDialog(Optiune optiune) {
        try {

            if(optiuneService==null)
                optiuneService=loadOptiuneService();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DetailedCandidatController.class.getResource("/detailedOptiuneRemastered.fxml"));
            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            DetailedOptiuneController detailedOptiuneController = loader.getController();
            detailedOptiuneController.setService((OptiuneService) optiuneService, dialogStage,optiune);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createCandidatFilterController(){
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DetailedCandidatController.class.getResource("/candidatFilter.fxml"));
            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            CandidatFilterController candidatFilterController = loader.getController();
            candidatFilterController.setService((CandidatService) candidatService, dialogStage,(CandidatTableController) currentController);

            currentFilterStage=dialogStage;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSectieFilterController(){
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DetailedCandidatController.class.getResource("/sectieFilter.fxml"));
            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SectieFilterController sectieFilterController = loader.getController();
            sectieFilterController .setService((SectieService) sectieService, dialogStage,(SectieTableController) currentController);

            sectieService.addObserver(sectieFilterController);
            currentFilterStage=dialogStage;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createOptiuneFilterController(){
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DetailedCandidatController.class.getResource("/optiuneFilter.fxml"));
            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            OptiuneFilterController optiuneFilterController = loader.getController();
            optiuneFilterController.setService((OptiuneService) optiuneService, dialogStage,(OptiuneTableController) currentController);

            sectieService.addObserver(optiuneFilterController);
            currentFilterStage=dialogStage;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    public void handleAdd(){
        if(currentType.equals(TableTipes.CANDIDAT))
            showCandidatDialog(null);
        else if(currentType.equals(TableTipes.SECTIE))
                showSectieDialog(null);
        else if(currentType.equals(TableTipes.OPTIUNE))
                showOptiuneDialog(null);
    }

    @FXML
    public void handleDelete(){
        if(currentType.equals(TableTipes.CANDIDAT)) {
            CandidatTableController controller = (CandidatTableController) currentController;
            controller.handleDelete();
        }
        else if(currentType.equals(TableTipes.SECTIE)){
            SectieTableController controller = (SectieTableController) currentController;
            controller.handleDelete();
        }
        else if(currentType.equals(TableTipes.OPTIUNE)){
            OptiuneTableController controller = (OptiuneTableController) currentController;
            controller.handleDelete();
        }
    }

    @FXML
    private void handleModify() {
        if(currentType.equals(TableTipes.CANDIDAT)) {
            CandidatTableController controller = (CandidatTableController) currentController;
            showCandidatDialog(controller.getSelectedEntity());
        }
        else if(currentType.equals(TableTipes.SECTIE)){
            SectieTableController controller = (SectieTableController) currentController;
            showSectieDialog(controller.getSelectedEntity());
        }
        else if(currentType.equals(TableTipes.OPTIUNE)){
            OptiuneTableController controller = (OptiuneTableController) currentController;
            showOptiuneDialog(controller.getSelectedEntity());
        }

    }

    @FXML
    private void handleHelp(){
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DetailedCandidatController.class.getResource("/help.fxml"));
            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            HelpController helpController = loader.getController();
            helpController.setStage(dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFilter(){
        currentFilterStage.show();
    }

    @FXML
    private void handleReports(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(appStart.Main.class.getResource("/reportsWindow.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();

            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("defaultSheet.css");
            dialogStage.setScene(scene);

            //set the service and the model for controller class
            ReportsWindowController reportsWindowController = loader.getController();
            optiuneService=loadOptiuneService();
            reportsWindowController.setService(optiuneService,dialogStage);
            dialogStage.show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handleTabChange(Event event) {
        if(dataBaseConnection==null) {
            dataBaseConnection = new DataBaseConnection();
        }
        else {
            if (candidatiTab.isSelected())
                initCandidatTable();
            else if (sectiiTab.isSelected())
                initSectieTable();
            else if (optiuniTab.isSelected())
                initOptiuneTable();
        }
    }

    @FXML
    public void handleSearch(){
        if(currentType.equals(TableTipes.CANDIDAT)) {
            CandidatTableController controller = (CandidatTableController) currentController;
            Predicate<Candidat> filterByNameContains = x -> x.getNume().toLowerCase().contains(quickSearchTextField.getText().toLowerCase());
            List<Candidat> filteredList = candidatService.filterAndSorter(candidatService.getAllEntities(), filterByNameContains, Candidat.compCandidatById);
            controller.setTableValues(filteredList);
        }
        else if(currentType.equals(TableTipes.SECTIE)){
            SectieTableController controller = (SectieTableController) currentController;
            Predicate<Sectie> filterByNameContains = x -> x.getNume().toLowerCase().contains(quickSearchTextField.getText().toLowerCase());
            List<Sectie> filteredList = sectieService.filterAndSorter(sectieService.getAllEntities(), filterByNameContains, Sectie.compSectieById);
            controller.setTableValues(filteredList);
        }
        else if(currentType.equals(TableTipes.OPTIUNE)){
            OptiuneTableController controller = (OptiuneTableController) currentController;
            Predicate<Optiune> filterByNameContains = x -> {OptiuneService auxService=(OptiuneService) optiuneService;
                return (auxService.candidatService.getEntity(x.getIdCandidat()).getNume().toLowerCase().contains(quickSearchTextField.getText()) ||
                        auxService.sectieService.getEntity(x.getIdSectie()).getNume().toLowerCase().contains(quickSearchTextField.getText()));
            };
            List<Optiune> filteredList = optiuneService.filterAndSorter(optiuneService.getAllEntities(), filterByNameContains, Optiune.compOptiuneById);
            controller.setTableValues(filteredList);
        }
}

    public void handleMail(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DetailedCandidatController.class.getResource("/mailComposer.fxml"));
            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            CandidatTableController candidatTableController = (CandidatTableController) currentController;
            Candidat c = ((CandidatTableController) currentController).getSelectedEntity();

            MailController mailController = loader.getController();
            mailController.setDefaults(c.getEmail(),null,null,dialogStage,null);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean clicks=true;

    public void handleTools(ActionEvent actionEvent) {
        if(clicks==true) {
            mailButton.setVisible(false);
            reportsButton.setVisible(false);
            filterButton.setVisible(false);
            clicks=false;
        }
        else
        {
            mailButton.setVisible(true);
            reportsButton.setVisible(true);
            filterButton.setVisible(true);
            clicks=true;
        }
    }

    public void handleLogout(ActionEvent actionEvent) {
        stage.close();
    }

    @FXML
    public void handleToolsButtonGlow(){
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        toolsButton.setEffect(blur);
    }


    @FXML
    public void handleToolsButtonUnglow(){
        toolsButton.setEffect(null);
    }

    public void handleReportsButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        reportsButton.setEffect(blur);
    }

    public void handleReportsButtonUnglow(MouseEvent mouseEvent) {
        reportsButton.setEffect(null);
    }

    public void handleLogoutButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        logoutButton.setEffect(blur);
    }

    public void handleLogoutButtonUnglow(MouseEvent mouseEvent) {
        logoutButton.setEffect(null);
    }

    public void handleAddButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        addButton.setEffect(blur);
    }

    public void handleAddButtonUnglow(MouseEvent mouseEvent) {
        addButton.setEffect(null);
    }

    public void handleDeleteButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        deleteButton.setEffect(blur);
    }

    public void handleDeleteButtonUnglow(MouseEvent mouseEvent) {
        deleteButton.setEffect(null);
    }

    public void handleModifyButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        modifyButton.setEffect(blur);
    }

    public void handleModifyButtonUnglow(MouseEvent mouseEvent) {
        modifyButton.setEffect(null);
    }

    public void handleMailButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        mailButton.setEffect(blur);
    }

    public void handleMailButtonUnglow(MouseEvent mouseEvent) {
        mailButton.setEffect(null);
    }

    public void handleFIlterButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        filterButton.setEffect(blur);
    }

    public void handleFilterButtonUnglow(MouseEvent mouseEvent) {
        filterButton.setEffect(null);
    }

    public void handleResetButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        resetButton.setEffect(blur);
    }

    public void handleResetButtonUnglow(MouseEvent mouseEvent) {
        resetButton.setEffect(null);
    }

    public void handleOptiuniForCandidat(Candidat c){
        if(optiuneService==null)
            optiuneService=loadOptiuneService();
        ObservableList<Optiune> aux = ((OptiuneService) optiuneService).getOptiuniForCandidat(c);
        if(aux.size() != 0) {
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(optiuniTab);
            ((OptiuneTableController) currentController).setTableValues(aux);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lipsa optiuni");
            alert.setHeaderText(null);
            alert.setContentText("Candidatul "+c.getNume()+" nu are nici o optiune inregistrata!");
            alert.showAndWait();
        }
    }


}
