package sample.ui.userUI;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import sample.domain.Candidat;
import sample.domain.Secție;
import sample.service.GeneralService;
import sample.utils.Observer;
import sample.utils.OpțiuneEvent;

import java.util.Vector;

public class StatisticaAlegerilorController implements Observer<OpțiuneEvent> {
    private GeneralService generalService;
    private Candidat candidat;

    @FXML public BarChart<String,Integer> chartSelecțieSecții;
    @FXML public CheckBox checkBoxDoarSecțiileMele;


    public StatisticaAlegerilorController() {
        generalService = new GeneralService();
    }

    public StatisticaAlegerilorController(GeneralService generalService) {
        this.generalService = generalService;
        generalService.getOpțiuneService().addObserver(this);
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        generalService.getOpțiuneService().addObserver(this);
        reloadChartSecții();
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
        if (checkBoxDoarSecțiileMele.isSelected())
            reloadChartSecții();
    }

    @FXML
    private void initialize() {
        checkBoxDoarSecțiileMele.setSelected(true);
        reloadChartSecții();
    }

    /** Raport: Numărul de optări pentru fiecare secție, în funcție de prioritate */
    private void reloadChartSecții() {
        chartSelecțieSecții.getData().clear();
        chartSelecțieSecții.setTitle("Raportul de selecție a secțiilor");
        XYChart.Series optăriTotale = new XYChart.Series();
        XYChart.Series primaOpțiune = new XYChart.Series();
        XYChart.Series douaOpțiune = new XYChart.Series();
        XYChart.Series treiaOpțiune = new XYChart.Series();
        optăriTotale.setName("Optări totale");
        primaOpțiune.setName("Prima opțiune");
        douaOpțiune.setName("A doua opțiune");
        treiaOpțiune.setName("A treia opțiune");

        Vector<Secție> secții = new Vector<>();
        if (checkBoxDoarSecțiileMele.isSelected() && candidat != null)
            secții = generalService.getSecțiiAlese(candidat.getID());
        else
            secții.addAll(generalService.getSecții());

        for (Secție secție : secții) {
            Integer nrOptăriTotale = generalService.getNrOptări(secție.getID());
            Integer nrOptări_1 = generalService.getNrOptăriPrioritate(secție.getID(), 1);
            Integer nrOptări_2 = generalService.getNrOptăriPrioritate(secție.getID(), 2);
            Integer nrOptări_3 = generalService.getNrOptăriPrioritate(secție.getID(), 3);
            optăriTotale.getData().add(new XYChart.Data(secție.toString(), nrOptăriTotale));
            primaOpțiune.getData().add(new XYChart.Data(secție.toString(), nrOptări_1));
            douaOpțiune.getData().add(new XYChart.Data(secție.toString(), nrOptări_2));
            treiaOpțiune.getData().add(new XYChart.Data(secție.toString(), nrOptări_3));
        }
        chartSelecțieSecții.getData().addAll(optăriTotale, primaOpțiune, douaOpțiune, treiaOpțiune);
    }

    public void handlerCheckBoxDoarSecțiilemele(MouseEvent mouseEvent) {
        reloadChartSecții();
    }


    @Override
    public void notifyOnEvent(OpțiuneEvent event) {
        reloadChartSecții();
    }

}
