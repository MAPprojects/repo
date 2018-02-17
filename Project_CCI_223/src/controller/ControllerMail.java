package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import domain.DetaliiLog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import repository.RepositoryFisierLog;
import utils.MailSender1000;
import view_FXML.DropShadowForImage;

import java.util.List;
import java.io.FileNotFoundException;

public class ControllerMail {

    private Stage stage;
    private String emailTo;
    private Integer idStudent;
    @FXML
    private JFXTextField textFieldEmail;
    @FXML
    private JFXTextField textFieldSubject;
    @FXML
    private JFXTextArea textAreaMessage;
    @FXML
    private JFXCheckBox jfxCheckBox;
    @FXML
    private Label labelMesaje;
    @FXML
    private ImageView image;

    @FXML
    public void handleImageEntered(MouseEvent event){
        image.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }

    @FXML
    public void handleImageExit(MouseEvent event){
        image.setEffect(null);
    }


    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
        textFieldEmail.setText(emailTo);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().setAll(new Image("/view_FXML/login/people.png"));
    }

    public ControllerMail() {
    }

    @FXML
    public void handleTextField(MouseEvent event){
        labelMesaje.setText("");
    }

    @FXML
    public void handleSendMail(MouseEvent event){
        if (checkFields()){
            MailSender1000 mailSender1000 = new MailSender1000();
            mailSender1000.send(textFieldSubject.getText(), textAreaMessage.getText()+getIfCheckedLog(), emailTo);
            stage.close();
        }
    }

    private String getIfCheckedLog(){
        if (jfxCheckBox.isSelected()){
            String text="\n\n\n"+
                    "________________________________________________________________________________________________\n" +
                    "| Operatiune         |  NrTema  | Nota  | Deadline  | Sapt. predarii  | Intarzieri  | Greseli   |\n" +
                    "________________________________________________________________________________________________\n" ;
            RepositoryFisierLog rep=new RepositoryFisierLog();
            try {
                List<DetaliiLog> lista=rep.incarcaFisierLog(idStudent);
                Integer intOp=20;
                Integer nrTema=11;
                Integer nota=7;
                Integer deadline=11;
                Integer sapt=17;
                Integer intarz=13;
                Integer gres=11;
                for (DetaliiLog det:lista){
                    text=text+"| "+det.getOperatiune();
                    Integer sizeOp=det.getOperatiune().length();
                    for (int i=sizeOp+1;i<=intOp;i++)
                        text=text+" ";

                    text=text+"| "+det.getNrTema();
                    Integer sizeT=det.getNrTema().toString().length();
                    for (int i=sizeT+1;i<=nrTema;i++)
                        text=text+" ";

                    text=text+"| "+det.getValoareNota();
                    Integer sizeN=det.getValoareNota().toString().length();
                    for (int i=sizeN+1;i<=nota;i++)
                        text=text+" ";

                    text=text+"| "+det.getDeadline();
                    Integer sizeD=det.getDeadline().toString().length();
                    for (int i=sizeD+1;i<=deadline;i++)
                        text=text+" ";

                    text=text+"| "+det.getSaptamana_predarii();
                    Integer sizeSa=det.getSaptamana_predarii().toString().length();
                    for (int i=sizeSa+1;i<=sapt;i++)
                        text=text+" ";

                    text=text+"| "+det.getIntarzieri();
                    Integer sizeI=det.getIntarzieri().length();
                    for (int i=sizeI+1;i<=intarz;i++)
                        text=text+" ";

                    text=text+"| "+det.getGreseli();
                    Integer sizeG=det.getGreseli().length();
                    for (int i=sizeG+1;i<gres;i++)
                        text=text+" ";
                    text=text+"|\n";
                }
                text=text+"________________________________________________________________________________________________\n" ;
                return text;
            } catch (FileNotFoundException e) {
                return text+ "| No content in table                                                                             |\n";
            }

        }
        return "";
    }

    private boolean checkFields() {
        if (textFieldSubject.getText().isEmpty()){
            labelMesaje.setText("Introduceti subiectul!");
            labelMesaje.setStyle("-fx-text-fill: red");
            return false;
        }
        else
        if ((textAreaMessage.getText()+getIfCheckedLog()).isEmpty()){
            labelMesaje.setText("Introduceti mesajul!");
            labelMesaje.setStyle("-fx-text-fill: red");
            return false;
        }
        return true;
    }

    @FXML
    public void initialize(){

    }
}
