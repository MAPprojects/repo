package viewController;

import entities.Candidat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import repository.RepositoryException;
import service.AbstractService;
import service.CandidatService;
import validator.ValidationException;

public class DetailedCandidatController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;



    private AbstractService<Candidat,Integer> service;
    private Stage stage;
    private Candidat candidat;

    public void setService(CandidatService service, Stage stage, Candidat candidat) {
        this.service = service;
        this.stage=stage;
        this.candidat=candidat;
        if (candidat!=null) {
            titleLabel.setText("Editare candidat");
            setFields(candidat);
            //textFieldId.setEditable(false);
        }
        else titleLabel.setText("Adaugare candidat");
    }

    @FXML
    private void handleOk() {
        errorLabel.setTextFill(Color.RED);
        if(candidat==null) {
            handleAdd();
        }
        else {
            handleModify();
        }

    }

    private void handleModify() {
        try {
            Candidat c = getCandidatFromFields();
            if(c.getNume().equals(candidat.getNume()) && c.getEmail().equals(candidat.getEmail()) && c.getTelefon().equals(candidat.getTelefon()))
                throw new ValidationException("Nu s-a efectuat nicio modificare!");
            service.updateEntity(c);
            errorLabel.setText("Candidatul " + c.getNume() + " a fost updatat cu succes!");
            errorLabel.setTextFill(Color.GREEN);
        } catch (ValidationException | RepositoryException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void handleAdd(){
        try {
            Candidat c = getCandidatFromFields();
            if (c != null) {
                service.addEntity(c);
                errorLabel.setText("Candidatul " + c.getNume() + " a fost adaugat cu succes!");
                errorLabel.setTextFill(Color.GREEN);
                clearFields();
            }
        } catch (ValidationException | RepositoryException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private Candidat getCandidatFromFields() {
        try{
            int id=Integer.parseInt(idTextField.getText());
            String nume=nameTextField.getText();
            String telefon=phoneTextField.getText();
            String email=emailTextField.getText();
            return new Candidat(id,nume,telefon,email);
        }
        catch(NumberFormatException e){
            errorLabel.setText("ID-ul trebuie sa fie un numar natural nevid!");
            return null;
        }
    }

    @FXML
    private void handleCancel(){
        stage.close();
    }

    private void clearFields(){
        idTextField.setText("");
        nameTextField.setText("");
        phoneTextField.setText("");
        emailTextField.setText("");
    }

    public void setFields(Candidat candidat) {
        idTextField.setText(""+candidat.getID());
        idTextField.setDisable(true);
        nameTextField.setText(candidat.getNume());
        phoneTextField.setText(candidat.getTelefon());
        emailTextField.setText(candidat.getEmail());
    }
}
