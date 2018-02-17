package main;

import Domain.*;
import Graphics.Login.LoginController;
import Graphics.MainGui.MainController;
import Repository.*;
import Services.StudentService;
import Services.UsersService;
import Utilities.MD5Encrypter;
import Utilities.PdfExporter;
import Validators.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Main extends Application {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Graphics/MainGui/MainView.fxml"));
    @Override
    public void start(Stage stage) {
        //testStudents();
        try {
            //generateSecretCodes();
            Pane mPane = loader.load();
            MainController ctrl = loader.getController();

            Scene scene = new Scene(mPane);
            stage.setScene(scene);
            stage.setTitle("Best application");
            Image icon = new Image(getClass().getResourceAsStream("/resources/icon.png"));
            stage.getIcons().add(icon);
            stage.setResizable(false);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        //fillTestData();
        launch(args);
    }


    public static void generateSecretCodes() {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        try {
            XMLStreamWriter streamWriter = factory.createXMLStreamWriter(new FileOutputStream("secretcodes.xml"));
            streamWriter.writeStartDocument("UTF-8","1.0");
            streamWriter.writeDTD("\n");
            streamWriter.writeStartElement("codes");
            streamWriter.writeDTD("\n");

                try {
                    streamWriter.writeDTD("\t");
                    streamWriter.writeStartElement("code");
                    streamWriter.writeDTD(MD5Encrypter.getHash("babes123"));
                    streamWriter.writeEndElement();
                    streamWriter.writeDTD("\n");

                    streamWriter.writeDTD("\t");
                    streamWriter.writeStartElement("code");
                    streamWriter.writeDTD(MD5Encrypter.getHash("map"));
                    streamWriter.writeEndElement();
                    streamWriter.writeDTD("\n");

                    streamWriter.writeDTD("\t");
                    streamWriter.writeStartElement("code");
                    streamWriter.writeDTD(MD5Encrypter.getHash("123456"));
                    streamWriter.writeEndElement();
                    streamWriter.writeDTD("\n");

                    streamWriter.writeDTD("\t");
                    streamWriter.writeStartElement("code");
                    streamWriter.writeDTD(MD5Encrypter.getHash("info"));
                    streamWriter.writeEndElement();
                    streamWriter.writeDTD("\n");

                    streamWriter.writeDTD("\t");
                    streamWriter.writeStartElement("code");
                    streamWriter.writeDTD(MD5Encrypter.getHash("facebook"));
                    streamWriter.writeEndElement();
                    streamWriter.writeDTD("\n");
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            streamWriter.writeEndElement();
            streamWriter.writeEndDocument();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
