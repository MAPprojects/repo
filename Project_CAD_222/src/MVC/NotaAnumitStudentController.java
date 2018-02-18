package MVC;

import Domain.Nota;
import Service.Service;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import java.util.function.Predicate;

public class NotaAnumitStudentController {
    private Service service;
    private Integer nrRanduriPePagina=16;
    private Predicate<Nota> filtru =  (x -> true);
    private String mailUtilizator;

    @FXML
    private TableView<Nota> notaAnumitStudentTableView;
    @FXML
    private Pagination pagination;

    public void setMailUtilizator(String mailUtilizator){
        this.mailUtilizator=mailUtilizator;
    }

    public void setService(Service service) {
        this.service = service;
        if (service.getNrNote(filtru) % nrRanduriPePagina == 0)
            pagination.setPageCount(service.getNrNote(filtru) / nrRanduriPePagina);
        else
            pagination.setPageCount(service.getNrNote(filtru) / nrRanduriPePagina + 1);
        if (service.getNrNote(filtru) == 0)
            pagination.setPageCount(1);
        createPage(pagination.getCurrentPageIndex());
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        notaAnumitStudentTableView.setItems(FXCollections.observableArrayList(service.findTemeFromStudentMail(mailUtilizator)));
        return new Pane();
    }

}
