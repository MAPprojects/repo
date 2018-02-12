package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.Department;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import services.StaffManager;
import utils.ListEvent;
import utils.ListEventType;
import utils.Observable;
import utils.Observer;
import validators.RepositoryException;
import validators.ValidationException;
import java.util.ArrayList;

public class addDepController implements Observable<Department> {
    protected ArrayList<Observer<Department>> observersList=new ArrayList<>();
    StaffManager service;

    @FXML
    private JFXButton exitDepartment;

    @FXML
    private JFXTextField idDepartment;

    @FXML
    private JFXTextField nameDepartment;

    @FXML
    private JFXTextField sizeDepartment;


    /**
     * Set up controller for AddCandidates
     * @param staffService
     */
    public void seteaza(StaffManager staffService) {
        this.service = staffService;

    }

    /**
     * Handler for adding a department
     * @param actionEvent
     */
    public void handlerAddDepartment(ActionEvent actionEvent) {
        Integer id,size;
        String name;
        try{
            id = Integer.parseInt(idDepartment.getText());
            name = nameDepartment.getText();
            size = Integer.parseInt((sizeDepartment.getText()));

            this.service.addDepartment(id,name,size);
            showMessage(Alert.AlertType.CONFIRMATION , "Add" , "Department succesfully added !");

            ListEvent<Department> ev = createEvent(ListEventType.ADD, new Department(id,name,size), service.showDepartments());
            notifyObservers(ev);

        }catch(NumberFormatException e){
            showErrorMessage("Error Add Department","ID/SIZE must be integers");
        }
        catch (RepositoryException | ValidationException e) {
            showErrorMessage("Error Add Department", e.getMessage());
        }

        ListEvent<Department> ev = createEvent(ListEventType.ADD, null, this.service.showDepartments());
        notifyObservers(ev);

    }

    /**
     *  Handler for closing the window
     * @param actionEvent
     */
    public void handleExitDepartment(ActionEvent actionEvent) {
        Stage st = (Stage) exitDepartment.getScene().getWindow();
        st.close();

    }

    /**
     *  Show an Error Alert with a given title and content
     *
     * @param title
     * @param content
     */
    static void showErrorMessage(String title,String content){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle(title);
        message.setContentText(content);
        message.showAndWait();
    }


    /**
     * Show a Message Alert with a given type , header and text.
     *
     * @param type
     * @param header
     * @param text
     */
    static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }


    /**
     * Add an observer to list
     * @param o the observer
     */
    @Override
    public void addObserver(Observer<Department> o) {
        observersList.add(o);
    }

    /**
     * Remove an observer.
     * @param o the observer
     */
    @Override
    public void removeObserver(Observer<Department> o) {
        observersList.remove(o);
    }

    /**
     * Notify all observers
     * @param event
     */
    @Override
    public void notifyObservers(ListEvent<Department> event) {
        observersList.forEach(x->x.notifyEvent(event));
    }

    /**
     * Create an Event
     * @param type
     * @param elem
     * @param l
     * @param <E>
     * @return
     */
    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l){
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return l;
            }
            @Override
            public E getElement() {
                return elem;
            }
        };
    }


}
