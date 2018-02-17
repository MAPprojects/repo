package controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import domain.StudentMedia;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;
import service.Service;
import view_FXML.AlertMessage;
import view_FXML.DropShadowForImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ControllerStatisticaStudentMedia extends ControllerStatisticaTip<StudentMedia> {

    @FXML
    private TableColumn tableColumnMediaNotelor;
    @FXML
    private TableColumn tableColumnProfesor;
    @FXML
    private TableColumn tableColumnEmail;
    @FXML
    private TableColumn tableColumnGrupa;
    @FXML
    private TableColumn tableColumnNume;
    @FXML
    private TableColumn tableColumnIdStudent;
    @FXML
    protected TextField textFieldPondere;
    @FXML
    protected CheckComboBox checkComboBoxTeme;

    protected Map<Integer,Double> ponderi=new HashMap<>();


    @FXML
    protected ImageView imageAdd;

    @FXML
    public void handleImageEnterAdd(MouseEvent event){
        imageAdd.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }
    @FXML
    public void handleImageExitAdd(MouseEvent ev){
        imageAdd.setEffect(null);
    }

    private void setPonderi(Double pondere, List<Integer> temeCuPondereaData) {
        temeCuPondereaData.forEach(idTema->{
            ponderi.put(idTema,pondere);
        });

        Double newPondere=(100-pondere*temeCuPondereaData.size())/(ponderi.size()-temeCuPondereaData.size());

        for (Map.Entry<Integer,Double> pair:ponderi.entrySet()){
            if (!temeCuPondereaData.contains(pair.getKey())){
                ponderi.put(pair.getKey(),newPondere);
            }
        }
    }

    @Override
    public void setService(Service service){
        super.setService(service);
        setCheckComboBoxTemeList();
        initPonderi();
        updateTableView();
    }

    private void initPonderi(){
        Double pondere=Double.valueOf(100/service.getSizeTemeLab());
        service.getListTemeLaborator().forEach(tema->{
            ponderi.put(tema.getId(),pondere);
        });
    }

    @Override
    public void update(){
        super.update();
        setCheckComboBoxTemeList();
    }

    private void setCheckComboBoxTemeList(){
        ObservableList<String> temeList= FXCollections.observableArrayList();
        service.getListTemeLaborator().forEach(tema->{
            temeList.add(tema.toString());
        });
        checkComboBoxTeme.getItems().setAll(temeList);
    }

    @FXML
    public void handleAddPondere(MouseEvent event){
        String text=textFieldPondere.getText();
        try{
            Double pondere=Double.parseDouble(text);
            if (pondere<0) throw new NumberFormatException();

            List<Integer> listIdTemeCuPondereaData = getTemeCuPondereaData();

            if (pondere*listIdTemeCuPondereaData.size()>100){
                AlertMessage message=new AlertMessage();
                message.showMessage(null, Alert.AlertType.ERROR,"Eroare","Ponderile insumate nu pot avea mai mult de 100%!!!");
                return;
            }
            setPonderi(pondere,listIdTemeCuPondereaData);
            updateTableView();
            initGrafic();

        }catch (NumberFormatException e){
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.ERROR,"Eroare!!!","Trebuie sa introduceti un numar real pozitiv!");
        }
    }

    private List<Integer> getTemeCuPondereaData() {
        List<Integer> listIdTemeCuPondereaData=new ArrayList<>();
        checkComboBoxTeme.getCheckModel().getCheckedItems().forEach((tema)->{
            String temaLab=(String) tema;
            String[] fields=temaLab.split(";");
            listIdTemeCuPondereaData.add(Integer.parseInt(fields[0]));
        });
        return listIdTemeCuPondereaData;
    }

    @Override
    protected void setTableViewColumns() {
        tableColumnIdStudent.setCellValueFactory(new PropertyValueFactory<StudentMedia,Integer>("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<StudentMedia,String>("nume"));
        tableColumnGrupa.setCellValueFactory(new PropertyValueFactory<StudentMedia,Integer>("grupa"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<StudentMedia,String>("email"));
        tableColumnProfesor.setCellValueFactory(new PropertyValueFactory<StudentMedia,String>("profesor"));
        tableColumnMediaNotelor.setCellValueFactory(new PropertyValueFactory<StudentMedia,Double>("media"));
    }

    @Override
    public PdfPTable getPdfPTable() throws DocumentException {
        PdfPTable table=new PdfPTable(6);
        table.setWidthPercentage(105);
        table.setSpacingBefore(11f);
        table.setSpacingAfter(11f);

        float[] colwidth={1f,1.2f,0.8f,2f,2f,2f};
        table.setWidths(colwidth);
        PdfPCell cellIdStudent=new PdfPCell(new Paragraph("Id Student"));
        PdfPCell cellNume=new PdfPCell(new Paragraph("Nume"));
        PdfPCell cellGrupa=new PdfPCell(new Paragraph("Grupa"));
        PdfPCell cellProfesor=new PdfPCell(new Paragraph("Profesor"));
        PdfPCell cellEmail=new PdfPCell(new Paragraph("Email"));
        PdfPCell cellMedia=new PdfPCell(new Paragraph("Media Notelor"));

        table.addCell(cellIdStudent);
        table.addCell(cellNume);
        table.addCell(cellGrupa);
        table.addCell(cellProfesor);
        table.addCell(cellEmail);
        table.addCell(cellMedia);

        List<StudentMedia> listaStudenti=new ArrayList<>();
        for (int i=0;i<data.size();i++){
            listaStudenti.add((StudentMedia) data.get(i));
        }

        cellIdStudent.setRowspan(listaStudenti.size());
        cellNume.setRowspan(listaStudenti.size());
        cellGrupa.setRowspan(listaStudenti.size());
        cellProfesor.setRowspan(listaStudenti.size());
        cellEmail.setRowspan(listaStudenti.size());
        cellMedia.setRowspan(listaStudenti.size());

        listaStudenti.forEach(studentMedia -> {
            table.addCell(studentMedia.getId().toString());
            table.addCell(studentMedia.getNume());
            table.addCell(studentMedia.getGrupa().toString());
            table.addCell(studentMedia.getProfesor());
            table.addCell(studentMedia.getEmail());
            table.addCell(studentMedia.getMedia().toString());
        });
        return table;
    }

    @Override
    public abstract List<StudentMedia> getStatistica();


}
