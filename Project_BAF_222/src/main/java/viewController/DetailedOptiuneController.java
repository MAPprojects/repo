package viewController;

import com.jfoenix.controls.JFXTextField;
import entities.*;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import repository.RepositoryException;
import service.AbstractService;
import service.OptiuneService;
import validator.ValidationException;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class DetailedOptiuneController {

    @FXML
    private Label titleLabel,idErrorLabel,candidatNameErrorLabel,sectieNameErrorLabel,priorityErrorLabel,resultLabel;
    @FXML
    private JFXTextField idOptiuneTextField;
    @FXML
    private JFXTextField candidatNameTextField;
    @FXML
    private JFXTextField sectieNameTextField;
    @FXML
    private JFXTextField priorityTextField;



    private AbstractService<Optiune, CheieOptiune> service;
    private Stage stage;
    private Optiune optiune;
    int idCandidatFromAutoComplete,idSectieFromAutoComplete;

    public void setService(OptiuneService service, Stage stage, Optiune optiune) {
        this.service = service;
        this.stage=stage;
        this.optiune=optiune;
        TextField textField = new TextField();

        AutoCompletionBinding<String> acb1 =  new AutoCompletionTextFieldBinding(candidatNameTextField, new Callback<AutoCompletionBinding.ISuggestionRequest, Collection>() {
            @Override
            public Collection call(AutoCompletionBinding.ISuggestionRequest param) {
                OptiuneService auxService = (OptiuneService) service;
                Predicate<Candidat> filterByNameContains = x -> x.getNume().toLowerCase().contains(candidatNameTextField.getText().toLowerCase());
                List<Candidat> filteredList = service.candidatService.filterAndSorter(service.candidatService.getAllEntities(), filterByNameContains, Candidat.compCandidatById);
                List<String> autocompleteResults = new ArrayList<>();
                for(Candidat c : filteredList)
                    autocompleteResults.add(c.getNume()+" ID-"+c.getID());
                return autocompleteResults;
            }
        });

        acb1.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>()
        {

            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event)
            {
                String valueFromAutoCompletion = event.getCompletion();
                String[] nameID = valueFromAutoCompletion.split("ID-");
                candidatNameTextField.setText(nameID[0]);
                idCandidatFromAutoComplete = Integer.parseInt(nameID[1]);

            }
        });

        AutoCompletionBinding<String> acb2 = new AutoCompletionTextFieldBinding(sectieNameTextField, new Callback<AutoCompletionBinding.ISuggestionRequest, Collection>() {
            @Override
            public Collection call(AutoCompletionBinding.ISuggestionRequest param) {
                OptiuneService auxService = (OptiuneService) service;
                Predicate<Sectie> filterByNameContains = x -> x.getNume().toLowerCase().contains(sectieNameTextField.getText().toLowerCase());
                List<Sectie> filteredList = service.sectieService.filterAndSorter(service.sectieService.getAllEntities(), filterByNameContains, Sectie.compSectieById);
                List<String> autocompleteResults = new ArrayList<>();
                for(Sectie s : filteredList)
                    autocompleteResults.add(s.getNume()+" ID-"+s.getID());
                return autocompleteResults;
            }
        });

        acb2.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>()
        {

            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String valueFromAutoCompletion = event.getCompletion();
                String[] nameID = valueFromAutoCompletion.split("ID-");
                sectieNameTextField.setText(nameID[0]);
                idSectieFromAutoComplete = Integer.parseInt(nameID[1]);
            }
        });


        setFields();
        if (optiune!=null && optiune.getIdSectie()!=-1) {
            titleLabel.setText("Editare optiune");
        }
        else {priorityTextField.setDisable(true);
            titleLabel.setText("Adaugare optiune");}


    }

    @FXML
    private void handleOk() {
        if(optiune==null || optiune.getIdSectie()==-1) {
            handleAdd();
        }
        else {
            handleModify();
        }

    }

    private void handleModify() {
        try {
            HybridOptiune o = getOptiuneFromFields();
            if(o.getPrioritate()==optiune.getPrioritate())
                throw new ValidationException("Nu s-a efectuat nicio modificare!");
            service.updateEntity(o);
            resultLabel.setVisible(true);
            resultLabel.setText(System.lineSeparator()+"A fost modificata cu succes prioritatea optiunii!");
        } catch (ValidationException | RepositoryException e) {
            resultLabel.setVisible(true);
            resultLabel.setTextFill(Color.RED);
            resultLabel.setText(e.getMessage());
        }
    }

    private void handleAdd(){
        try {
            HybridOptiune o = getOptiuneFromFields();
            if (o != null) {
                service.addEntity(o);
                OptiuneService auxService = (OptiuneService) service;
                resultLabel.setVisible(true);
                resultLabel.setText("A fost adaugata cu succes optiunea!");
                clearFields();
            }
        } catch (ValidationException | RepositoryException e) {
            resultLabel.setTextFill(Color.RED);
            resultLabel.setVisible(true);
            resultLabel.setText("O optiune cu acelasi id exista deja!");
        }
    }

    private HybridOptiune getOptiuneFromFields() {
        try{
            boolean good = true;
            if(candidatNameTextField.getText().equals("")){
                candidatNameErrorLabel.setVisible(true);
                good = false;
            }
            if(sectieNameTextField.getText().equals("")){
                sectieNameErrorLabel.setVisible(true);
                good = false;
            }
            String numeCandidat=candidatNameTextField.getText();
            String numeSectie=sectieNameTextField.getText();
            int idOptiune=Integer.parseInt(idOptiuneTextField.getText());
            if(good == false)
                return null;
            if(optiune!=null && optiune.getIdSectie()==-1)
                    idCandidatFromAutoComplete=optiune.getIdCandidat();
            else
            if(optiune!=null)
                {
                idCandidatFromAutoComplete=optiune.getIdCandidat();
                idSectieFromAutoComplete=optiune.getIdSectie();
            }
            HybridOptiune optiuneFromFields=new HybridOptiune(idOptiune,idCandidatFromAutoComplete,idSectieFromAutoComplete,numeCandidat,numeSectie);
            if(optiune!=null && optiune.getIdSectie()!=-1)
                optiuneFromFields.setPrioritate(Integer.parseInt(priorityTextField.getText()));
            return optiuneFromFields;
        }
        catch(NumberFormatException e){
            idErrorLabel.setText("ID-ul trebuie sa fie un numar pozitiv!");
            idErrorLabel.setVisible(true);
            return null;
        }
    }

    @FXML
    private void handleCancel(){
        stage.close();
    }

    private void clearFields(){
        HybridOptiune hybridOptiune=(HybridOptiune) optiune;
        idOptiuneTextField.setText(""+(((OptiuneService) service).getMaxId()+1));
        sectieNameTextField.setText("");
        if(optiune==null) {
            candidatNameTextField.setText("");
        }
    }

    public void setFields() {
        HybridOptiune hybridOptiune=(HybridOptiune) optiune;
        if(hybridOptiune!=null) {
            idOptiuneTextField.setText(""+(((OptiuneService) service).getMaxId()+1));
            candidatNameTextField.setText(hybridOptiune.getNumeCandidat());
            candidatNameTextField.setDisable(true);
        }
        if(hybridOptiune!=null && hybridOptiune.getIdSectie()!=-1)
        {
            idOptiuneTextField.setText(""+hybridOptiune.getIdOptiune());
            idOptiuneTextField.setDisable(true);
            sectieNameTextField.setText(hybridOptiune.getNumeSectie());
            sectieNameTextField.setDisable(true);
            priorityTextField.setText(""+hybridOptiune.getPrioritate());
        }


    }

}
