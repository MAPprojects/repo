package FxmlFiles;

import Service.Service;
import Utils.Event;
import Utils.Observer;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FirstPage implements Observer<Event> {
    private Service service;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private AnchorPane anchorPane;
    public boolean isAdmin;

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setService(Service service) {
        this.service = service;
        this.service.addObserver(this);
    }

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(getClass().getResource("Welcome.fxml"));
            AnchorPane anchorPaneW = loader2.load();

            HamburgerBackArrowBasicTransition burgerTask2 = new HamburgerBackArrowBasicTransition(hamburger);
            burgerTask2.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                burgerTask2.setRate(burgerTask2.getRate() * -1);
                burgerTask2.play();
                if (drawer.isShown()) {
                    drawer.close();
                    anchorPane.getChildren().add(anchorPaneW);
                } else {
                    drawer.open();
                    anchorPane.getChildren().remove(anchorPaneW);

                }
            });

            VBox box = FXMLLoader.load(getClass().getResource("DrawerContent.fxml"));
            drawer.setSidePane(box);
            for (Node node : box.getChildren()) {
                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        switch (node.getAccessibleText()) {
                            case "material4":
                                System.exit(0);
                              //  material4Handler();
                                break;
                            case "material3":
                                material3Handler();
                                break;
                            case "material2":
                                material2Handler();
                                break;
                            case "material1": {
                                material1Handler();
                                break;
                            }
                        }
                    });
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void material4Handler() {

    }

    private void material3Handler() {
        try {
            anchorPane.getChildren().remove(0);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("OptionsView.fxml"));
            AnchorPane anchorPane2 = loader.load();
            anchorPane.getChildren().add(anchorPane2);
            OptionsView ctrl = loader.getController();
            ctrl.setAdmin(isAdmin);
            ctrl.setService(this.service);


        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private void material2Handler() {
        try {
            anchorPane.getChildren().remove(0);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("SectionsView.fxml"));
            AnchorPane anchorPane2 = loader.load();
            anchorPane.getChildren().add(anchorPane2);
            SectionsView ctrl = loader.getController();
            ctrl.setAdmin(isAdmin);
            ctrl.setService(this.service);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void material1Handler() {
        try {
            anchorPane.getChildren().remove(0);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CandidatesView.fxml"));
            AnchorPane anchorPane2 = loader.load();
            anchorPane.getChildren().add(anchorPane2);
            CandidatesView ctrl = loader.getController();
            ctrl.setAdmin(isAdmin);
            ctrl.setService(this.service);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void notifyOnEvent(Event event) {

    }
}
