package controller;

import entities.Tema;
import exceptions.TemaServiceException;
import exceptions.TemaValidatorException;
import javafx.fxml.FXML;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.NotaService;
import services.TemaService;

import java.io.IOException;

public class DeleteTemaController {
    private Tema temaToBeDeleted;
    private TemaService temaService;
    private NotaService notaService;
    private AnchorPane temaRootLayout;
    private Stage thisStage;

    public void setTemaToBeDeleted(Tema temaToBeDeleted) {
        this.temaToBeDeleted = temaToBeDeleted;
    }

    public void setTemaService(TemaService temaService) {
        this.temaService = temaService;
    }

    public void setNotaService(NotaService notaService) {
        this.notaService = notaService;
    }

    public void setTemaRootLayout(AnchorPane temaRootLayout) {
        this.temaRootLayout = temaRootLayout;
    }

    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    private void backToTemaScene(MouseEvent mouseEvent) {
        temaRootLayout.getChildren().forEach(child -> {
            child.setDisable(false);
            child.setEffect(null);
        });
        thisStage.close();
    }

    @FXML
    private void deleteTemaHandler(MouseEvent mouseEvent) {
        notaService.stergeToateNotelePentruOTema(temaToBeDeleted.getId());
        try {
            temaService.deleteTema(temaToBeDeleted);
            temaRootLayout.getChildren().forEach(child -> {
                child.setEffect(null);
                child.setDisable(false);
            });
            thisStage.close();
        } catch (TemaValidatorException e) {
            e.printStackTrace();
        } catch (TemaServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
