package viewController;

import entities.Sectie;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import repository.RepositoryException;
import service.AbstractService;
import service.SectieService;
import validator.ValidationException;


public class DetailedSectieController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField numberOfPlacesTextField;



    private AbstractService<Sectie,Integer> service;
    private Stage stage;
    private Sectie sectie;

    public void setService(SectieService service, Stage stage, Sectie sectie) {
        this.service = service;
        this.stage=stage;
        this.sectie=sectie;
        if (sectie!=null) {
            titleLabel.setText("Editare sectie");
            setFields(sectie);
        }
        else titleLabel.setText("Adaugare sectie");
    }

    @FXML
    private void handleOk() {
        errorLabel.setTextFill(Color.RED);
        if(sectie==null) {
            handleAdd();
        }
        else {
            handleModify();
        }

    }

    private void handleModify() {
        try {
            Sectie s = getSectieFromFields();
            if(s.getNume().equals(sectie.getNume()) && s.getNrLoc()==sectie.getNrLoc() )
                throw new ValidationException("Nu s-a efectuat nicio modificare!");
            service.updateEntity(s);
            errorLabel.setText("Sectia " + s.getNume() + " a fost updatata cu succes!");
            errorLabel.setTextFill(Color.GREEN);
        } catch (ValidationException | RepositoryException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void handleAdd(){
        try {
            Sectie s = getSectieFromFields();
            if (s != null) {
                service.addEntity(s);
                errorLabel.setText("Sectia " + s.getNume() + " a fost adaugata cu succes!");
                errorLabel.setTextFill(Color.GREEN);
                clearFields();
            }
        } catch (ValidationException | RepositoryException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private int parseNrLocuri(){
        try{
            return Integer.parseInt(numberOfPlacesTextField.getText());
        }
        catch (NumberFormatException e){
            errorLabel.setText("Numarul de locuri trebuie sa fie un numar natural nevid!");
        }
        return Integer.MAX_VALUE;
    }

    private Sectie getSectieFromFields() {
        try{
            int id=Integer.parseInt(idTextField.getText());
            String nume=nameTextField.getText();
            int nrLoc=parseNrLocuri();
            if(nrLoc==Integer.MAX_VALUE)
                return null;
            return new Sectie(id,nume,nrLoc);
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
        numberOfPlacesTextField.setText("");
    }

    public void setFields(Sectie sectie) {
        idTextField.setText(""+sectie.getID());
        idTextField.setDisable(true);
        nameTextField.setText(sectie.getNume());
        numberOfPlacesTextField.setText(""+sectie.getNrLoc());
    }
}
