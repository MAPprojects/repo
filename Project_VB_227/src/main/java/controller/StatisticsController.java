package controller;


import entities.Student;
import entities.Tema;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.chart.AxisFormatBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.exception.DRException;
import services.StatisticsService;
import thread_utils.NotifyingThread;
import thread_utils.ThreadCompleteListener;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StatisticsController implements ThreadCompleteListener {

    private static final String TITLU_BAR_CHART_MEDII = "Mediile actuale";
    private Stage primaryStage;
    private Scene mainMenuScene;
    private StatisticsService statisticsService;
    private AnchorPane statisticsRootLayout;
    private GaussianBlur gaussianBlur;
    private ImageView loadingImageView;
    private boolean isMediiStudentiButtonSelected = false;
    private boolean isMediiTemeButtonSelected = false;
    private boolean isPromovatiButtonSelected = false;

    @FXML
    private Pane paneEroareRaport;
    @FXML
    private Pane paneOptiuniRapoarte;
    @FXML
    private Button raportMediiButton;
    @FXML
    private Button raportMediiTemeButton;
    @FXML
    private Button raportPromovatiMedii;
    @FXML
    private Pane centerPaneStatistics;
    @FXML
    private Pane paneChartsCenter;
    @FXML
    private BorderPane borderPaneStatistici;
    @FXML
    private Pane paneOptiuniMeniu;

    @FXML
    public void initialize() {
        gaussianBlur = new GaussianBlur();
        centerPaneStatistics.widthProperty().addListener(((observable, oldValue, newValue) -> {
            paneChartsCenter.setPrefWidth(newValue.doubleValue() - 100);
            paneChartsCenter.setLayoutX(50);
        }));
        paneChartsCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.doubleValue() != 0) {
                paneOptiuniRapoarte.setLayoutX(newValue.doubleValue() / 2 - paneOptiuniRapoarte.getPrefWidth() / 2);
            }
        });
        paneEroareRaport.setVisible(false);
        raportMediiButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                raportMediiButton.setStyle("-fx-background-color: darkcyan;-fx-font-size: 18;-fx-border-color: #00aaaa;");
            }
        });
        raportMediiTemeButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                raportMediiTemeButton.setStyle("-fx-background-color: darkcyan;-fx-font-size: 18;-fx-border-color: #00aaaa;");
            }
        });
        raportPromovatiMedii.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                raportPromovatiMedii.setStyle("-fx-background-color: darkcyan;-fx-font-size: 18;-fx-border-color: #00aaaa;");
            }
        });
        raportMediiButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!isMediiStudentiButtonSelected) {
                    raportMediiButton.setStyle("-fx-background-color: white;-fx-border-color: darkcyan;-fx-font-size: 18;");
                }
            }
        });
        raportMediiTemeButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!isMediiTemeButtonSelected) {
                    raportMediiTemeButton.setStyle("-fx-background-color: white;-fx-border-color: darkcyan;-fx-font-size: 18;");
                }
            }
        });
        raportPromovatiMedii.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!isPromovatiButtonSelected) {
                    raportPromovatiMedii.setStyle("-fx-background-color: white;-fx-border-color: darkcyan;-fx-font-size: 18;");
                }
            }
        });
        raportMediiButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isMediiStudentiButtonSelected = true;
                isPromovatiButtonSelected = false;
                isMediiTemeButtonSelected = false;
                raportPromovatiMedii.setStyle("-fx-background-color: white;-fx-border-color: darkcyan;-fx-font-size: 18;");
                raportMediiTemeButton.setStyle("-fx-background-color: white;-fx-border-color: darkcyan;-fx-font-size: 18;");
                paneEroareRaport.setVisible(false);
            }
        });
        raportMediiTemeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isMediiTemeButtonSelected = true;
                isPromovatiButtonSelected = false;
                isMediiStudentiButtonSelected = false;
                raportPromovatiMedii.setStyle("-fx-background-color: white;-fx-border-color: darkcyan;-fx-font-size: 18;");
                raportMediiButton.setStyle("-fx-background-color: white;-fx-border-color: darkcyan;-fx-font-size: 18;");
                paneEroareRaport.setVisible(false);
            }
        });
        raportPromovatiMedii.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isPromovatiButtonSelected = true;
                isMediiTemeButtonSelected = false;
                isMediiStudentiButtonSelected = false;
                raportMediiButton.setStyle("-fx-background-color: white;-fx-border-color: darkcyan;-fx-font-size: 18;");
                raportMediiTemeButton.setStyle("-fx-background-color: white;-fx-border-color: darkcyan;-fx-font-size: 18;");
                paneEroareRaport.setVisible(false);
            }
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public void setMainMenuScene(Scene mainMenuScene) {
        this.mainMenuScene = mainMenuScene;
    }

    public void setStatisticsRootLayout(AnchorPane statisticsRootLayout) {
        this.statisticsRootLayout = statisticsRootLayout;
    }

    @FXML
    private void backToMenuHandler(MouseEvent mouseEvent) {
        primaryStage.setScene(mainMenuScene);
    }

    private BarChart<String, Number> createBarChartMedii(Map<Student, Double> studentiiCuMediile) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(TITLU_BAR_CHART_MEDII);
        xAxis.setLabel("Student");
        yAxis.setLabel("Medie");
        XYChart.Series set1 = new XYChart.Series();
        studentiiCuMediile.forEach((key, value) -> {
            set1.getData().add(new XYChart.Data<>(key.getNume(), value.floatValue()));
        });
        barChart.getData().add(set1);
        return barChart;
    }

    @FXML
    private void generareRaport(MouseEvent mouseEvent) {
        Thread thread = new Thread() {
            public void run() {
                JasperReportBuilder jasperReportBuilder = null;
                if (isMediiStudentiButtonSelected) {
                    jasperReportBuilder = genereazaRaportMedii();
                } else if (isPromovatiButtonSelected) {
                    jasperReportBuilder = genereazaRaportIntrareExamen();
                } else if (isMediiTemeButtonSelected) {
                    jasperReportBuilder = genereazaRaportMediiNoteTeme();

                }
                try {
                    if (jasperReportBuilder != null) {
                        jasperReportBuilder.show(false);
                    } else {
                        paneEroareRaport.setVisible(true);
                    }
                } catch (DRException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @FXML
    private void initiateReportDownloadAsPng(MouseEvent mouseEvent) {
        if (isMediiStudentiButtonSelected) {
            initiazaExportRaportMedii();
        } else if (isPromovatiButtonSelected) {
            initiazaExportRaportIntrareExamen();
        } else if (isMediiTemeButtonSelected) {
            initiazaExportRaportMediileTemelor();
        } else {
            paneEroareRaport.setVisible(true);
        }
    }

    @FXML
    private void changeChart(MouseEvent mouseEvent) {

    }

    private void initiazaExportRaportMediileTemelor() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            FileInputStream loadingGifInputStream = null;
            try {
                loadingGifInputStream = new FileInputStream("src\\main\\resources\\views\\loadingScene\\loader.gif");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            javafx.scene.image.Image loadingImage = new Image(loadingGifInputStream);
            loadingImageView = new ImageView(loadingImage);
            statisticsRootLayout.getChildren().add(loadingImageView);
            loadingImageView.setX(statisticsRootLayout.getWidth() / 2 - 80);
            loadingImageView.setY(statisticsRootLayout.getHeight() / 2 - 100);
            statisticsRootLayout.getChildren().forEach(c -> {
                if (c != loadingImageView) {
                    c.setDisable(true);
                    c.setEffect(gaussianBlur);
                }
            });
            NotifyingThread exportThread = new NotifyingThread() {
                public void doRun() {
                    JasperReportBuilder jasperReportBuilder = genereazaRaportMediiNoteTeme();
                    try {
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                        jasperReportBuilder.toPdf(bufferedOutputStream);
                        bufferedOutputStream.close();
                    } catch (DRException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            exportThread.addListener(this);
            exportThread.start();
        }
    }

    private void initiazaExportRaportIntrareExamen() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            FileInputStream loadingGifInputStream = null;
            try {
                loadingGifInputStream = new FileInputStream("src\\main\\resources\\views\\loadingScene\\loader.gif");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            javafx.scene.image.Image loadingImage = new Image(loadingGifInputStream);
            loadingImageView = new ImageView(loadingImage);
            statisticsRootLayout.getChildren().add(loadingImageView);
            loadingImageView.setX(statisticsRootLayout.getWidth() / 2 - 80);
            loadingImageView.setY(statisticsRootLayout.getHeight() / 2 - 100);
            statisticsRootLayout.getChildren().forEach(c -> {
                if (c != loadingImageView) {
                    c.setDisable(true);
                    c.setEffect(gaussianBlur);
                }
            });
            NotifyingThread exportThread = new NotifyingThread() {
                public void doRun() {
                    JasperReportBuilder jasperReportBuilder = genereazaRaportIntrareExamen();
                    try {
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                        jasperReportBuilder.toPdf(bufferedOutputStream);
                        bufferedOutputStream.close();
                    } catch (DRException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            exportThread.addListener(this);
            exportThread.start();
        }
    }

    private void initiazaExportRaportMedii() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            FileInputStream loadingGifInputStream = null;
            try {
                loadingGifInputStream = new FileInputStream("src\\main\\resources\\views\\loadingScene\\loader.gif");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            javafx.scene.image.Image loadingImage = new Image(loadingGifInputStream);
            loadingImageView = new ImageView(loadingImage);
            statisticsRootLayout.getChildren().add(loadingImageView);
            loadingImageView.setX(statisticsRootLayout.getWidth() / 2 - 80);
            loadingImageView.setY(statisticsRootLayout.getHeight() / 2 - 100);
            statisticsRootLayout.getChildren().forEach(c -> {
                if (c != loadingImageView) {
                    c.setDisable(true);
                    c.setEffect(gaussianBlur);
                }
            });
            NotifyingThread exportThread = new NotifyingThread() {
                public void doRun() {
                    JasperReportBuilder jasperReportBuilder = genereazaRaportMedii();
                    try {
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                        jasperReportBuilder.toPdf(bufferedOutputStream);
                        bufferedOutputStream.close();
                    } catch (DRException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            exportThread.addListener(this);
            exportThread.start();
        }
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {
        Platform.runLater(() -> {
            statisticsRootLayout.getChildren().remove(loadingImageView);
            statisticsRootLayout.getChildren().forEach(c -> {
                c.setDisable(false);
                c.setEffect(null);
            });
        });
    }

    private JasperReportBuilder genereazaRaportMediiNoteTeme() {
        HashMap<Tema, Double> temeSiMedii = statisticsService.getMediileNotelorLaFiecareTema();
        JasperReportBuilder report = DynamicReports.report();
        //styleForTheReport
        StyleBuilder boldStyle = DynamicReports.stl.style().bold();
        StyleBuilder boldCenteredStyle = DynamicReports.stl.style(boldStyle)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        StyleBuilder columnTitleStyle = DynamicReports.stl.style(boldCenteredStyle)
                .setBorder(DynamicReports.stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
        StyleBuilder titleStyle = DynamicReports.stl.style(boldCenteredStyle)
                .setVerticalImageAlignment(VerticalImageAlignment.MIDDLE)
                .setFontSize(15);

        //adding styles
        report.setColumnTitleStyle(columnTitleStyle)
                .setSubtotalStyle(boldStyle)
                .highlightDetailEvenRows();

        //rows
        TextColumnBuilder<String> cerintaColumn = DynamicReports.col.column("Cerinta", "cerinta", DataTypes.stringType());
        TextColumnBuilder<Double> mediaColumn = DynamicReports.col.column("Media", "media", DataTypes.doubleType());
        TextColumnBuilder<Integer> termenLimitaColumn = DynamicReports.col.column("Termen Limita", "termenLimita", DataTypes.integerType());
        ConditionalStyleBuilder condition = DynamicReports.stl.conditionalStyle(DynamicReports.cnd.smaller(mediaColumn, 5))
                .setBackgroundColor(new Color(255, 210, 210));
        //row number column
        TextColumnBuilder<Integer> rowNumberColumn = DynamicReports.col.reportRowNumberColumn("No.")
                .setFixedColumns(2)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        //charts
        Bar3DChartBuilder noteChart = DynamicReports.cht.bar3DChart().setShowValues(true)
                .setTitle("Diagarama mediilor notelor pentru fiecare tema")
                .setCategory(cerintaColumn)
                .addSerie(
                        DynamicReports.cht.serie(mediaColumn)
                );


        report.columns(
                rowNumberColumn,
                cerintaColumn,
                termenLimitaColumn,
                mediaColumn)
                .subtotalsAtSummary(DynamicReports.sbt.avg(mediaColumn));
        report.pageFooter(Components.pageXofY().setStyle(boldCenteredStyle));
        DRDataSource dataSource = new DRDataSource("cerinta", "termenLimita", "media");
        temeSiMedii.forEach((tema, medie) -> {
            if (medie.isNaN()) {
                dataSource.add(tema.getCerinta(), tema.getTermenPredare(), 0.0);
            } else {
                dataSource.add(tema.getCerinta(), tema.getTermenPredare(), medie);
            }
        });
        report.detailRowHighlighters(condition);
        report.summary(noteChart);
        report.title(
                DynamicReports.cmp.horizontalList()
                        .add(DynamicReports.cmp.image(getClass().getResourceAsStream("/views/statisticsView/report.png"))
                                        .setFixedDimension(80, 80),
                                DynamicReports.cmp.text("Raport cu mediile tuturor notelor pentru fiecare tema").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                        .newRow()
                        .add(DynamicReports.cmp.filler().setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.pen2Point())).setFixedHeight(10)));
        report.setDataSource(dataSource);
        return report;
    }

    private JasperReportBuilder genereazaRaportIntrareExamen() {
        HashMap<Student, Double> studentiiSiMedii = statisticsService.getStudentiiCareIntraExamen();
        JasperReportBuilder report = DynamicReports.report();
        //styleForTheReport
        StyleBuilder boldStyle = DynamicReports.stl.style().bold();
        StyleBuilder boldCenteredStyle = DynamicReports.stl.style(boldStyle)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        StyleBuilder columnTitleStyle = DynamicReports.stl.style(boldCenteredStyle)
                .setBorder(DynamicReports.stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
        StyleBuilder titleStyle = DynamicReports.stl.style(boldCenteredStyle)
                .setVerticalImageAlignment(VerticalImageAlignment.MIDDLE)
                .setFontSize(15);

        //adding styles
        report.setColumnTitleStyle(columnTitleStyle)
                .setSubtotalStyle(boldStyle)
                .highlightDetailEvenRows();

        if (studentiiSiMedii.size() == 0) {
            report.title(
                    DynamicReports.cmp.horizontalList()
                            .add(DynamicReports.cmp.image(getClass().getResourceAsStream("/views/statisticsView/report.png"))
                                            .setFixedDimension(80, 80),
                                    DynamicReports.cmp.text("Niciun student nu poate intra in examen").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                            .newRow()
                            .add(DynamicReports.cmp.filler().setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.pen2Point())).setFixedHeight(10)));
        } else {

            //rows
            TextColumnBuilder<String> numeColumn = DynamicReports.col.column("Nume", "nume", DataTypes.stringType());
            TextColumnBuilder<Double> mediaColumn = DynamicReports.col.column("Media", "media", DataTypes.doubleType());
            TextColumnBuilder<String> grupaColumn = DynamicReports.col.column("Grupa", "grupa", DataTypes.stringType());
            TextColumnBuilder<String> profColumn = DynamicReports.col.column("Profesor", "prof", DataTypes.stringType());
            ConditionalStyleBuilder condition = DynamicReports.stl.conditionalStyle(DynamicReports.cnd.smaller(mediaColumn, 5))
                    .setBackgroundColor(new Color(255, 210, 210));
            //row number column
            TextColumnBuilder<Integer> rowNumberColumn = DynamicReports.col.reportRowNumberColumn("No.")
                    .setFixedColumns(2)
                    .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

            //charts
            Bar3DChartBuilder noteChart = DynamicReports.cht.bar3DChart()
                    .setTitle("Diagarama mediilor celor care pot intra in examen")
                    .setCategory(numeColumn)
                    .addSerie(
                            DynamicReports.cht.serie(mediaColumn)
                    );


            report.columns(
                    rowNumberColumn,
                    numeColumn,
                    grupaColumn,
                    profColumn,
                    mediaColumn)
                    .subtotalsAtSummary(DynamicReports.sbt.avg(mediaColumn));
            report.pageFooter(Components.pageXofY().setStyle(boldCenteredStyle));
            DRDataSource dataSource = new DRDataSource("nume", "grupa", "prof", "media");
            studentiiSiMedii.forEach((student, medie) -> {
                if (medie.isNaN()) {
                    dataSource.add(student.getNume(), student.getGrupa(), student.getCadruDidacticIndrumator(), 0.0);
                } else {
                    dataSource.add(student.getNume(), student.getGrupa(), student.getCadruDidacticIndrumator(), medie);
                }
            });
            report.detailRowHighlighters(condition);
            report.summary(noteChart);
            report.title(
                    DynamicReports.cmp.horizontalList()
                            .add(DynamicReports.cmp.image(getClass().getResourceAsStream("/views/statisticsView/report.png"))
                                            .setFixedDimension(80, 80),
                                    DynamicReports.cmp.text("Raport cu studentii care pot intra in examen").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                            .newRow()
                            .add(DynamicReports.cmp.filler().setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.pen2Point())).setFixedHeight(10)));
            report.setDataSource(dataSource);
        }
        return report;
    }

    private JasperReportBuilder genereazaRaportMedii() {
        HashMap<Student, Double> studentiiSiMedii = statisticsService.getMediileStudentilor();
        JasperReportBuilder report = DynamicReports.report();
        //styleForTheReport
        StyleBuilder boldStyle = DynamicReports.stl.style().bold();
        StyleBuilder boldCenteredStyle = DynamicReports.stl.style(boldStyle)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        StyleBuilder columnTitleStyle = DynamicReports.stl.style(boldCenteredStyle)
                .setBorder(DynamicReports.stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
        StyleBuilder titleStyle = DynamicReports.stl.style(boldCenteredStyle)
                .setVerticalImageAlignment(VerticalImageAlignment.MIDDLE)
                .setFontSize(15);

        //adding styles
        report.setColumnTitleStyle(columnTitleStyle)
                .setSubtotalStyle(boldStyle)
                .highlightDetailEvenRows();

        //rows
        TextColumnBuilder<String> numeColumn = DynamicReports.col.column("Nume", "nume", DataTypes.stringType());
        TextColumnBuilder<Double> mediaColumn = DynamicReports.col.column("Media", "media", DataTypes.doubleType());
        TextColumnBuilder<String> grupaColumn = DynamicReports.col.column("Grupa", "grupa", DataTypes.stringType());
        TextColumnBuilder<String> profColumn = DynamicReports.col.column("Profesor", "prof", DataTypes.stringType());
        ConditionalStyleBuilder condition = DynamicReports.stl.conditionalStyle(DynamicReports.cnd.smaller(mediaColumn, 5))
                .setBackgroundColor(new Color(255, 210, 210));
        //row number column
        TextColumnBuilder<Integer> rowNumberColumn = DynamicReports.col.reportRowNumberColumn("No.")
                .setFixedColumns(2)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        //charts
        Bar3DChartBuilder noteChart = DynamicReports.cht.bar3DChart()
                .setTitle("Diagarama mediilor")
                .setCategory(numeColumn)
                .addSerie(
                        DynamicReports.cht.serie(mediaColumn)
                );


        report.columns(
                rowNumberColumn,
                numeColumn,
                grupaColumn,
                profColumn,
                mediaColumn)
                .subtotalsAtSummary(DynamicReports.sbt.avg(mediaColumn));
        report.pageFooter(Components.pageXofY().setStyle(boldCenteredStyle));
        DRDataSource dataSource = new DRDataSource("nume", "grupa", "prof", "media");
        studentiiSiMedii.forEach((student, medie) -> {
            if (medie.isNaN()) {
                dataSource.add(student.getNume(), student.getGrupa(), student.getCadruDidacticIndrumator(), 0.0);
            } else {
                dataSource.add(student.getNume(), student.getGrupa(), student.getCadruDidacticIndrumator(), medie);
            }
        });
        report.detailRowHighlighters(condition);
        report.summary(noteChart);
        report.title(
                DynamicReports.cmp.horizontalList()
                        .add(DynamicReports.cmp.image(getClass().getResourceAsStream("/views/statisticsView/report.png"))
                                        .setFixedDimension(80, 80),
                                DynamicReports.cmp.text("Raport cu mediile curente ale studentilor").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                        .newRow()
                        .add(DynamicReports.cmp.filler().setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.pen2Point())).setFixedHeight(10)));
        report.setDataSource(dataSource);
        return report;
    }
}
