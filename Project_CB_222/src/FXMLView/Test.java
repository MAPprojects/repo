package FXMLView;

import Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Test {

    private final Image IMAGE_USER = new Image(getClass().getClassLoader().getResource("Resources/genericUser.png").toString(), 64, 64, false, false);

    private Service service;

    @FXML
    private ListView<String> listViewOfUsers;

    @FXML
    private void initialize(){
    }

    public void setService(Service service){
        this.service = service;
        initilizeListOfUsers();
    }

    private class UserCell extends ListCell<String> {
        private ImageView imageView = new ImageView();

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                imageView.setImage(null);

                setGraphic(null);
                setText(null);
            } else {
                imageView.setImage(IMAGE_USER);

                setText(item);
                setGraphic(imageView);
            }
        }
    }

    private void initilizeListOfUsers(){

        ObservableList<String> noroc = FXCollections.observableArrayList (
                "RUBY", "APPLE", "VISTA", "TWITTER");
        listViewOfUsers.setItems(noroc);

        listViewOfUsers.setCellFactory(param -> new UserCell());


    }
}
