package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Domain.Nota;
import com.company.Domain.Student;
import com.company.Domain.Tema;
import com.company.Exceptions.RepositoryException;
import com.company.Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Main;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class MainWindowCtrl {

    public Main main;
    public MenuItem filtersMenu;
    public Menu configMenu;
    public MenuItem raportMenu;
    public MenuItem currentWeek;
    public MenuItem passMenu;
    Service service;
    @FXML
    public BorderPane mainPane;
    @FXML
    public TitledPane studentsTab;
    @FXML
    public Accordion tableTabs;

    public FXMLLoader loaderTables;
    public FXMLLoader loaderControls;


    public void initialize()
    {
        Label label = new Label("Welcome! Please select a tool from the top menu :)");
        label.setFont(new Font(15));
        mainPane.setRight(null);
        mainPane.setCenter(label);

        mainPane.getCenter().getStyleClass().clear();

        setFancyMenu();

        if(Globals.getInstance().accessLevel==2)
        {
            filtersMenu.setText("Filters");
            //passMenu.setDisable(true);
        }
        if(Globals.getInstance().accessLevel==1)
        {
            filtersMenu.setText("My Grades");
            currentWeek.setDisable(true);
            currentWeek.setVisible(false);
            raportMenu.setDisable(true);
            raportMenu.setVisible(false);

        }

        stylise();

    }

    public void setFancyMenu()
    {
        FXMLLoader loadInterface = new FXMLLoader(getClass().getResource("WelcomeScreen.fxml"));
        try {
            mainPane.getCenter().getStyleClass().clear();
            Parent parent = loadInterface.load();
            mainPane.setCenter(parent);



            WelcomeScreenCtrl welcCtrl = loadInterface.<WelcomeScreenCtrl>getController();
            if(Globals.getInstance().accessLevel==1)
            {
                welcCtrl.weekI.setDisable(true);
                welcCtrl.weekI.setEffect(new GaussianBlur(15));
                welcCtrl.raportI.setDisable(true);
                welcCtrl.raportI.setEffect(new GaussianBlur(15));
            }

            welcCtrl.crudI.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    setTables();
                }
            });
            welcCtrl.crudI.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.crudI.setScaleX(1.2);
                    welcCtrl.crudI.setScaleY(1.2);
                    Image image = new Image(getClass().getResource("/com/company/GUI/w1.png").toExternalForm());
                    welcCtrl.crudI.setImage(image);
                }
            });
            welcCtrl.crudI.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.crudI.setScaleX(1.0);
                    welcCtrl.crudI.setScaleY(1.0);
                    Image image = new Image(getClass().getResource("/com/company/GUI/q1.png").toExternalForm());
                    welcCtrl.crudI.setImage(image);
                }
            });

            welcCtrl.filtrariI.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    setFilter();
                }
            });
            welcCtrl.filtrariI.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.filtrariI.setScaleX(1.2);
                    welcCtrl.filtrariI.setScaleY(1.2);
                    Image image = new Image(getClass().getResource("/com/company/GUI/w2.png").toExternalForm());
                    welcCtrl.filtrariI.setImage(image);
                }
            });
            welcCtrl.filtrariI.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.filtrariI.setScaleX(1.0);
                    welcCtrl.filtrariI.setScaleY(1.0);
                    Image image = new Image(getClass().getResource("/com/company/GUI/q2.png").toExternalForm());
                    welcCtrl.filtrariI.setImage(image);
                }
            });

            welcCtrl.logI.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    setLogs();
                }
            });
            welcCtrl.logI.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.logI.setScaleX(1.2);
                    welcCtrl.logI.setScaleY(1.2);
                    Image image = new Image(getClass().getResource("/com/company/GUI/w3.png").toExternalForm());
                    welcCtrl.logI.setImage(image);
                }
            });
            welcCtrl.logI.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.logI.setScaleX(1.0);
                    welcCtrl.logI.setScaleY(1.0);
                    Image image = new Image(getClass().getResource("/com/company/GUI/q3.png").toExternalForm());
                    welcCtrl.logI.setImage(image);
                }
            });

            welcCtrl.raportI.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    setRaports();
                }
            });
            welcCtrl.raportI.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.raportI.setScaleX(1.2);
                    welcCtrl.raportI.setScaleY(1.2);
                    Image image = new Image(getClass().getResource("/com/company/GUI/w4.png").toExternalForm());
                    welcCtrl.raportI.setImage(image);
                }
            });
            welcCtrl.raportI.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.raportI.setScaleX(1.0);
                    welcCtrl.raportI.setScaleY(1.0);
                    Image image = new Image(getClass().getResource("/com/company/GUI/q4.png").toExternalForm());
                    welcCtrl.raportI.setImage(image);
                }
            });

            welcCtrl.weekI.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    openConfigWindow();
                }
            });
            welcCtrl.weekI.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.weekI.setScaleX(1.2);
                    welcCtrl.weekI.setScaleY(1.2);
                    Image image = new Image(getClass().getResource("/com/company/GUI/w5.png").toExternalForm());
                    welcCtrl.weekI.setImage(image);
                }
            });
            welcCtrl.weekI.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.weekI.setScaleX(1.0);
                    welcCtrl.weekI.setScaleY(1.0);
                    Image image = new Image(getClass().getResource("/com/company/GUI/q5.png").toExternalForm());
                    welcCtrl.weekI.setImage(image);
                }
            });

            welcCtrl.themeI.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    openThemeWindow();
                }
            });
            welcCtrl.themeI.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.themeI.setScaleX(1.2);
                    welcCtrl.themeI.setScaleY(1.2);
                    Image image = new Image(getClass().getResource("/com/company/GUI/w6.png").toExternalForm());
                    welcCtrl.themeI.setImage(image);
                }
            });
            welcCtrl.themeI.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.themeI.setScaleX(1.0);
                    welcCtrl.themeI.setScaleY(1.0);
                    Image image = new Image(getClass().getResource("/com/company/GUI/q6.png").toExternalForm());
                    welcCtrl.themeI.setImage(image);
                }
            });

            welcCtrl.passI.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    passwordChange();
                }
            });
            welcCtrl.passI.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.passI.setScaleX(1.2);
                    welcCtrl.passI.setScaleY(1.2);
                    Image image = new Image(getClass().getResource("/com/company/GUI/w7.png").toExternalForm());
                    welcCtrl.passI.setImage(image);
                }
            });
            welcCtrl.passI.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.passI.setScaleX(1.0);
                    welcCtrl.passI.setScaleY(1.0);
                    Image image = new Image(getClass().getResource("/com/company/GUI/q7.png").toExternalForm());
                    welcCtrl.passI.setImage(image);
                }
            });

            welcCtrl.exitI.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    logOff();
                }
            });
            welcCtrl.exitI.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.exitI.setScaleX(1.2);
                    welcCtrl.exitI.setScaleY(1.2);
                    Image image = new Image(getClass().getResource("/com/company/GUI/w8.png").toExternalForm());
                    welcCtrl.exitI.setImage(image);
                }
            });
            welcCtrl.exitI.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    welcCtrl.exitI.setScaleX(1.0);
                    welcCtrl.exitI.setScaleY(1.0);
                    Image image = new Image(getClass().getResource("/com/company/GUI/q8.png").toExternalForm());
                    welcCtrl.exitI.setImage(image);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stylise()
    {
        mainPane.getStylesheets().clear();
        mainPane.getStyleClass().clear();
        mainPane.getCenter().getStyleClass().clear();
        /*
        String pathS = "E:\\MAP\\LabMAP_GUI_Full - Copy (2)\\src\\com\\company\\GUI\\" + Globals.getInstance().theme;
        Path path = Paths.get(pathS);
        Stream<String> stream;
        try {
            stream = Files.lines(path);
            stream.forEach(s->System.out.println(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

//        Path path = Paths.get("E:\\MAP\\LabMAP_GUI_Full - Copy (2)\\src\\com\\company\\GUI\\" + Globals.getInstance().theme);
//        try {
//            String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
//            mainPane.setStyle(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //if(Globals.getInstance().theme.equals("NewTheme.css"))
        //{
        //    mainPane.setStyle(Globals.getInstance().customTheme);
        //    mainPane.getCenter().setStyle(Globals.getInstance().customTheme);
        //}
        //else {

        try {
            String resource = Paths.get("E:\\MAP\\LabMAP_GUI_Full - Copy (2)\\src\\com\\company\\GUI\\" +Globals.getInstance().theme).toUri().toURL().toExternalForm();
            mainPane.getStylesheets().add(resource);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //mainPane.getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
            mainPane.getStyleClass().add("pane");
            mainPane.getCenter().getStyleClass().add("pane");
        //}
    }


    public void openConfigWindow() {
        FXMLLoader loaderConfig = new FXMLLoader();
        try {
            Parent configWindow = loaderConfig.load(getClass().getResource("Config.fxml"));
            Stage config = new Stage();
            config.setScene(new Scene(configWindow));
            config.setResizable(false);
            config.show();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setService(Service service)
    {
        this.service = service;
    }

    public void setFilter()
    {


        FXMLLoader loaderControlls = new FXMLLoader(getClass().getResource("Filters.fxml"));
        try{
            Parent controlls = loaderControlls.load();
            FiltersCtrl ctrl = loaderControlls.<FiltersCtrl>getController();
            ctrl.service=service;
            ctrl.mainPaneReference = mainPane;
            ctrl.setInterfaceMethod();
            mainPane.setCenter(ctrl.listView);
            if(Globals.getInstance().accessLevel==2)
                mainPane.setRight(controlls);
            if(Globals.getInstance().accessLevel==1) {
                mainPane.setRight(null);
                ctrl.setInterfaceMethod();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setTables() {
        loaderTables = new FXMLLoader(getClass().getResource("Tables.fxml"));
        try {
            Parent tableWindow = loaderTables.load();
            TablesCtrl ctrl = loaderTables.<TablesCtrl>getController();
            ctrl.setService(service);
            ctrl.setParentCtrl(this);
            ctrl.setup();
            ctrl.populate();
            mainPane.setCenter(tableWindow);
            loaderControls = new FXMLLoader(getClass().getResource("StudentControlls.fxml"));
            Parent controllsWindow = loaderControls.load();
            StudentControllsCtrl ctrl2 = loaderControls.<StudentControllsCtrl>getController();
            ctrl2.service = service;
            mainPane.setRight(controllsWindow);

            ctrl.studentTable.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->ctrl2.fillSelected(newValue));
            if(ctrl2.idField.getText().length()>0)
                ctrl2.search();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStudentControlls()
    {
        FXMLLoader loaderControls = new FXMLLoader(getClass().getResource("StudentControlls.fxml"));
        try {
            Parent controllsWindow = loaderControls.load();
            StudentControllsCtrl ctrl2 = loaderControls.<StudentControllsCtrl>getController();
            mainPane.setRight(controllsWindow);
            ctrl2.service = service;

            TablesCtrl ctrl = loaderTables.<TablesCtrl>getController();

            ctrl.studentTable.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->ctrl2.fillSelected(newValue));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTemeControlls()
    {
        FXMLLoader loaderControls = new FXMLLoader(getClass().getResource("TemeControlls.fxml"));
        try {
            Parent controllsWindow = loaderControls.load();
            TemeControllsCtrl ctrl2 = loaderControls.<TemeControllsCtrl>getController();
            mainPane.setRight(controllsWindow);
            ctrl2.service = service;

            TablesCtrl ctrl = loaderTables.<TablesCtrl>getController();
            ctrl.temaTable.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->ctrl2.fillSelected(newValue));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNotaControlls() {
        FXMLLoader loaderControls = new FXMLLoader(getClass().getResource("NotaControlls.fxml"));
        try {
            Parent controllsWindow = loaderControls.load();
            NotaControllsCtrl ctrl2 = loaderControls.<NotaControllsCtrl>getController();
            mainPane.setRight(controllsWindow);
            ctrl2.service = service;

            TablesCtrl ctrl = loaderTables.<TablesCtrl>getController();
            ctrl.notaTable.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->ctrl2.fillSelected(newValue));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLogs() {
        FXMLLoader loaderLogs = new FXMLLoader(getClass().getResource("Logs.fxml"));
        try
        {
            Parent logWindow = loaderLogs.load();
            LogsCtrl ctrl = loaderLogs.<LogsCtrl>getController();
            ctrl.service = service;

            if(Globals.getInstance().accessLevel==2)
            {
                ArrayList<Student> students = new ArrayList<>();
                students.addAll((Collection<? extends Student>) service.getStudents());
                for (Student st:students)
                {
                    if(!Files.exists(Paths.get("Data//"+st.getID()+".txt")))
                    {
                        Files.createFile(Paths.get("Data//"+st.getID()+".txt"));
                    }
                    Tab tab = new Tab("" + st.getID() + ":" + st.getNume());
                    TextArea txta = new TextArea();
                    txta.setEditable(false);
                    tab.setContent(txta);
                    ctrl.tabs.getTabs().add(tab);
                    BufferedReader read = null;
                    read = new BufferedReader(new FileReader("Data//"+st.getID()+".txt"));
                    String line = read.readLine();
                    while(line != null)
                    {
                        txta.appendText(line+"\n");
                        line = read.readLine();
                    }
                    read.close();

                }
            }
            if(Globals.getInstance().accessLevel==1)
            {
                ArrayList<Student> students = new ArrayList<>();
                students.addAll((Collection<? extends Student>) service.getStudents());
                for (Student st: students)
                {
                    if(st.getID()==Globals.getInstance().nonAdminID) {
                        Tab tab = new Tab(st.getID() + ":" + st.getNume());
                        TextArea txta = new TextArea();
                        txta.setEditable(false);
                        tab.setContent(txta);
                        ctrl.tabs.getTabs().add(tab);
                        BufferedReader read = null;
                        read = new BufferedReader(new FileReader("Data//"+st.getID()+".txt"));
                        String line = read.readLine();
                        while(line != null)
                        {
                            txta.appendText(line+"\n");
                            line = read.readLine();
                        }
                        read.close();
                    }

                }
            }
            mainPane.setCenter(logWindow);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logOff() {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.close();
        main.loginStep();
    }

    public void setRaports() {
        FXMLLoader loaderRaports = new FXMLLoader(getClass().getResource("Raports.fxml"));
        try {
            Parent parent = loaderRaports.load();
            RaportsCtrl ctrl = loaderRaports.getController();
            ctrl.service = service;
            mainPane.setRight(null);
            mainPane.setCenter(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openThemeWindow() {
        FXMLLoader loaderTheme = new FXMLLoader(getClass().getResource("ThemeWindow.fxml"));
        try{
            Parent parent = loaderTheme.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            mainPane.setRight(null);
            Label label = new Label("Welcome! Please select a tool from the top menu :)");
            label.setFont(new Font(15));
            mainPane.setCenter(label);
            setFancyMenu();
            stylise();
            //label.getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void passwordChange() {
        FXMLLoader loaderChange = new FXMLLoader(getClass().getResource("PasswordChange.fxml"));
        try{
            Parent parent  = loaderChange.load();

            PasswordChangeCtrl ctrl = loaderChange.getController();
            ctrl.service = service;
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
