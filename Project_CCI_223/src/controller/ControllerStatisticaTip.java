package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import domain.Student;
import domain.StudentMedia;
import domain.TemaPenalizari;
import domain.User;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import sablonObserver.Observer;
import service.Service;
import view_FXML.DropShadowForImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ControllerStatisticaTip<E> implements Observer {

    protected Service service;
    protected HostServices hostServices;
    protected Integer itemsPerPage;
    protected Stage stage;
    protected ObservableList<E> model= FXCollections.observableArrayList();
    protected ObservableList<E> data=FXCollections.observableArrayList();
    protected List<Student> listaStudenti=new ArrayList<>();

    @FXML
    protected Pagination pagination;
    @FXML
    protected TableView tableView;
    @FXML
    protected CheckComboBox checkComboBoxProfesor;
    @FXML
    protected CheckComboBox checkComboBoxGrupa;
    @FXML
    protected ComboBox comboBoxItemsPerPage;

    @FXML
    protected ImageView imagePdf;

    protected Integer contor;

    protected User user;

    public void setUser(User user) {
        this.user = user;
    }

    private void initContor() {
        Random r=new Random();
        contor=r.nextInt();
    }

    @FXML
    public void handleImageEnterPdf(MouseEvent event){
        imagePdf.setEffect(DropShadowForImage.dropShadow(10d,10d,1d,1d,2d));
    }
    @FXML
    public void handleImageExitPdf(MouseEvent ev){
        imagePdf.setEffect(null);
    }

    @FXML
    public void initialize(){
        setTableViewColumns();
        
        setItemsPerPage(10);
        setComboBoxItemsPerPageList();
        comboBoxItemsPerPage.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                setItemsPerPage((Integer) comboBoxItemsPerPage.getValue());
                updatePage();
                initGrafic();
            }
        });

        pagination.setPageFactory((Integer page)->{
            return createPage(page);
        });

        checkComboBoxGrupa.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change c) {
                setListaStudenti(generareListaStudenti());
                updatePage();
                initGrafic();
            }
        });
        checkComboBoxProfesor.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change c) {
                setListaStudenti(generareListaStudenti());
                updatePage();
                initGrafic();
            }
        });
    }

    protected abstract void initGrafic();

    protected abstract void setTableViewColumns();

    @FXML
    public void handleSavaAsPdf(MouseEvent event){
        FileChooser fileChooser=new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF","*.pdf")
        );
        fileChooser.setInitialFileName("statistica.pdf");
//        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PDF","*.pdf"));

        fileChooser.setTitle("Save As PDF");
        File file=fileChooser.showSaveDialog(stage);
        if (file!=null){

            String path=file.getAbsolutePath();
            createPdf(path);

        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().setAll(new javafx.scene.image.Image("/view_FXML/login/people.png"));    }

    public void createPdf(String path){
        Document document=new Document();
        try{
            PdfWriter writer=PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();

            document.addTitle("Student Managerment System");
            document.addAuthor("Coste Claudia-Ioana");
            document.addCreationDate();
            document.addSubject("Raport: Statistica");
            document.resetPageCount();
            document.setPageCount(2);

            Paragraph title=new Paragraph("Student Managerment System");

            Font font=new Font();
            font.setColor(BaseColor.RED);
            font.setSize(30f);
            font.setFamily("Times New Roman");
            title.setFont(font);

            document.add(title);

            PdfPTable table = getPdfPTable();

            document.add(table);

            String pathImage=getGraficImage();
            Image image=Image.getInstance(pathImage);
            document.add(image);

            document.close();
            writer.close();

            File file=new File(path);
            hostServices.showDocument(file.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getGraficImage();

    public abstract PdfPTable getPdfPTable() throws DocumentException;

    public void updateTableView(){
        listaStudenti=generareListaStudenti();
        List<E> listaStatistica=getStatistica();
        setData(listaStatistica);
        updatePage();
    }

    private void updatePage() {
        Integer index=pagination.getCurrentPageIndex();

        if (data.size()==0){
            pagination.setPageCount(1);
        }
        if (data.size() % getItemsPerPage() != 0) {
            pagination.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            pagination.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        } else {
            pagination.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            pagination.setPageCount((int) ((data.size() / getItemsPerPage())));
        }


        if (index<(int)data.size()/getItemsPerPage()+1) {
//            createPage(index);

            pagination.getPageFactory().call(index);
            pagination.setPageFactory((Integer page)->{return createPage(page);});

        }
        else {
//            createPage(0);
            pagination.getPageFactory().call(0);
            pagination.setPageFactory((Integer page)->{return createPage(page);});
        }
    }


    private List<String> getListProfesori() {
        List<String> list=new ArrayList<>();
        checkComboBoxProfesor.getCheckModel().getCheckedItems().forEach(prof->{
            list.add((String)prof);
        });
        return list;
    }

    private List<String> getListGrupe() {
        List<String> list=new ArrayList<>();
        checkComboBoxGrupa.getCheckModel().getCheckedItems().forEach(grupa->{
            list.add((String)grupa);
        });
        return list;
    }

    private Node createPage(Integer page) {
        VBox vBox=new VBox();
        vBox.getChildren().add(tableView);

        Integer indexFrom=0;
        if (data.size()>=page*getItemsPerPage()){
            indexFrom=page*getItemsPerPage();
        }
        Integer indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
        model.setAll(data.subList(indexFrom,indexTo));
        tableView.setItems(model);

        return vBox;
    }

    private List<Student> generareListaStudenti() {
        List<String> listGrupe=getListGrupe();
        List<String> listProfesori=getListProfesori();

        List<Student> studentList=new ArrayList<>();
        if (listGrupe.contains("toate grupele") || listProfesori.contains("toti profesorii")||(listGrupe.isEmpty()&&listProfesori.isEmpty())){
            studentList=service.getListStudenti();
        }
        else{
            for (Student student:service.getListStudenti()){
                if (!studentList.contains(student)) {
                    if (listGrupe.contains(String.valueOf(student.getGrupa())))
                        studentList.add(student);
                }
                if (!studentList.contains(student)){
                    if (listProfesori.contains(student.getCadru_didactic_indrumator_de_laborator()))
                        studentList.add(student);
                }

            }
        }
        return studentList;
    }


    private void updateTable(){
        if (data.size()%getItemsPerPage()!=0){
            pagination.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            pagination.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        }
        else{
            pagination.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            pagination.setPageCount((int) ((data.size() / getItemsPerPage()) ));
        }

        for (int i=0;i<(int)(data.size()/getItemsPerPage())+1;i++){
            Integer indexFrom=i*getItemsPerPage();
            Integer indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
            model.setAll(data.subList(indexFrom, indexTo));
            tableView.setItems(FXCollections.observableList(model));
        }
    }

    public ControllerStatisticaTip() {
        initContor();
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setService(Service service) {
        this.service = service;
        setCheckComboBoxGrupaList();
        setCheckComboBoxProfesorList();
        updateTableView();
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    private void setComboBoxItemsPerPageList(){
        ObservableList<Integer> list=FXCollections.observableArrayList();
        list.setAll(5,10,20,50);
        comboBoxItemsPerPage.getItems().setAll(list);
    }

    private void setCheckComboBoxGrupaList(){
        ObservableList<String> listaGrupe=FXCollections.observableArrayList();
        List<Student> listStudents=service.getListStudenti();
        listStudents.forEach(student -> {
            if (!listaGrupe.contains(String.valueOf(student.getGrupa())))
                listaGrupe.add(String.valueOf(student.getGrupa()));
        });
        listaGrupe.add("toate grupele");
        checkComboBoxGrupa.getItems().setAll(listaGrupe);
    }

    private void setCheckComboBoxProfesorList(){
        ObservableList<String> listaProfesori=FXCollections.observableArrayList();
        List<Student> listStudents=service.getListStudenti();
        listStudents.forEach(student -> {
            if (!listaProfesori.contains(student.getCadru_didactic_indrumator_de_laborator()))
                listaProfesori.add(student.getCadru_didactic_indrumator_de_laborator());
        });
        listaProfesori.add("toti profesorii");
        checkComboBoxProfesor.getItems().setAll(listaProfesori);
    }


    @Override
    public void update() {
        setCheckComboBoxGrupaList();
        setCheckComboBoxProfesorList();
        updateTableView();
    }

    public abstract List<E> getStatistica();

    private void setData(List<E> list){
        data.setAll(list);
    }

    public void setListaStudenti(List<Student> listaStudenti) {
        this.listaStudenti = listaStudenti;
        updateTableView();
    }

}
