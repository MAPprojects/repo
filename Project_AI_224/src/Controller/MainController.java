package Controller;

import Service.ApplicationService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;

public class MainController extends StackPane {
    private HashMap<String, Node> screens = new HashMap<>();
    ApplicationService service;

    public MainController(ApplicationService applicationService){
        super();
        this.service=applicationService;
    }

    public void setService(ApplicationService service){
        this.service = service;
    }

    public ApplicationService getService(){
        return service;
    }

    /**
     * adds a new screen to the collection of screens
     * @param name screen name
     * @param screen screen
     */
    public void addScreen(String name, Node screen){
        screens.put(name, screen);
    }

    /**
     * return the screen with the given name
     * @param name screen name
     * @return screen with given name
     */
    public Node getScreen(String name){
        return screens.get(name);
    }

    public boolean loadScreen(String name, String path){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource(path));
            AnchorPane loadScreen = (AnchorPane) loader.load();
            ScreenController screenController = loader.getController();
            screenController.setScreenParent(this);
            this.addScreen(name, loadScreen);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setScreen(String name){
        final DoubleProperty opacity = opacityProperty();
        if(screens.get(name)!=null){
            if(!getChildren().isEmpty()){
                Timeline fade = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)), new KeyFrame(new Duration(300), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        getChildren().remove(0);
                        getChildren().add(0, screens.get(name));
                        Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)), new KeyFrame(new Duration(300), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();
            }
            else{
                setOpacity(0.0);
                getChildren().add(screens.get(name));
                Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)), new KeyFrame(new Duration(1000), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
        }
        return false;
    }

    public boolean unloadScreen(String name){
        return screens.remove(name) != null;
    }

}
