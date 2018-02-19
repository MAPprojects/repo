package viewController;


import javafx.fxml.FXML;
import javafx.stage.Stage;

public class HelpController {

    Stage stage;

    void setStage(Stage stage){
        this.stage=stage;
    }

    @FXML
    private void handleOk(){
        stage.close();
    }
}
