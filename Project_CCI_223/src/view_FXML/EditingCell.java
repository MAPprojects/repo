package view_FXML;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class EditingCell<E,T> extends TableCell<E,T> {
    private TextField textField;

    public EditingCell(){}

    @Override
    public void startEdit(){
        super.startEdit();;
        if (textField==null){
            createTextField();
        }
        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.selectAll();
    }

    @Override
    public void cancelEdit(){
        super.cancelEdit();

        setText(String.valueOf(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(T item,boolean empty){
        super.updateItem(item,empty);

        if (empty){
            setText(null);
            setGraphic(null);
        }
        else{
            if (isEditing())
            {
                if (textField!=null) textField.setText(getString());
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
            else{
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    public void editEvent(KeyEvent event){
        if (event.getCode()== KeyCode.ENTER) {
            try {
                commitEdit(covertToType(textField.getText()));
            }
            catch (NumberFormatException e){
                AlertMessage message=new AlertMessage();
                message.showMessage(null, Alert.AlertType.ERROR,"Eroare!","Ceea ce ati introdus nu este valid!!!");
            }
        }
        else if (event.getCode()==KeyCode.ESCAPE) {
            cancelEdit();
        }
    }

    public abstract T covertToType(String text);

    private void createTextField() {
        textField=new TextField(getString());
        textField.setMinWidth(this.getWidth()-this.getGraphicTextGap()*2);
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                editEvent(event);
            }
        });
    }

    private String getString(){
        return getItem()==null ? "": getItem().toString();
    }
}
