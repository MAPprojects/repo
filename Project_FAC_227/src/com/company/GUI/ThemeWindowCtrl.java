package com.company.GUI;

import com.company.Domain.Globals;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThemeWindowCtrl {

    public RadioButton darkC;
    public RadioButton lightC;
    public RadioButton customC;
    public ColorPicker bgColor;
    public ColorPicker accentColor;
    public ColorPicker textColor;
    public Button apply;
    public RadioButton defC;

    public void initialize()
    {
        ToggleGroup group = new ToggleGroup();
        defC.setToggleGroup(group);
        darkC.setToggleGroup(group);
        lightC.setToggleGroup(group);
        customC.setToggleGroup(group);
        disableColors();
        System.out.println(Globals.getInstance().theme);
        System.out.println("Default.css");

        if(Globals.getInstance().theme.equals("Default.css"))
            defC.setSelected(true);
        if(Globals.getInstance().theme.equals("DarkTheme.css"))
            darkC.setSelected(true);
        if(Globals.getInstance().theme.equals("LightTheme.css"))
            lightC.setSelected(true);
        if(Globals.getInstance().theme.equals("NewTheme.css"))
        {
            customC.setSelected(true);
            enableColors();
        }
    }

    public void enableColors()
    {
        bgColor.setDisable(false);
        accentColor.setDisable(false);
        textColor.setDisable(false);
    }

    public void disableColors()
    {
        bgColor.setDisable(true);
        accentColor.setDisable(true);
        textColor.setDisable(true);

    }

    public void custom()
    {
        Color bgc = bgColor.getValue();
        //Color bg2c = new Color(bgc.getRed(),bgc.getBlue(),bgc.getGreen(),1);
        Color ac = accentColor.getValue();
        //Color a2c = new Color(ac.getRed(),ac.getBlue(),ac.getGreen(),1);
        //a2c.darker();
        Color txtc = textColor.getValue();

        String s1= String.format( "#%02X%02X%02X",
                (int)( bgc.getRed() * 255 ),
                (int)( bgc.getGreen() * 255 ),
                (int)( bgc.getBlue() * 255 ) );
        String s2= String.format( "#%02X%02X%02X",
                (int)( Math.max(bgc.getRed() * 255 -30,0) ),
                (int)( Math.max(bgc.getGreen() * 255 -30,0)),
                (int)( Math.max(bgc.getBlue() * 255 -30,0)) );
        String s3= String.format( "#%02X%02X%02X",
                (int)( ac.getRed() * 255 ),
                (int)( ac.getGreen() * 255 ),
                (int)( ac.getBlue() * 255 ) );;
        String s4= String.format( "#%02X%02X%02X",
                (int)( Math.max(ac.getRed() * 255 -30,0) ),
                (int)( Math.max(ac.getGreen() * 255 -30,0)),
                (int)( Math.max(ac.getBlue() * 255 -30,0)) );
        String s5= String.format( "#%02X%02X%02X",
                (int)( txtc.getRed() * 255 ),
                (int)( txtc.getGreen() * 255 ),
                (int)( txtc.getBlue() * 255 ) );;

        Path path = Paths.get("E:\\MAP\\LabMAP_GUI_Full - Copy (2)\\src\\com\\company\\GUI\\Template.css");
        Charset charset = StandardCharsets.UTF_8;
        Path newPath = Paths.get("E:\\MAP\\LabMAP_GUI_Full - Copy (2)\\src\\com\\company\\GUI\\NewTheme.css");
        try {
            File file = new File("E:\\MAP\\LabMAP_GUI_Full - Copy (2)\\src\\com\\company\\GUI\\NewTheme.css");
            PrintWriter wr = new PrintWriter(file);
            wr.write("");
            //wr.close();

            String content = new String(Files.readAllBytes(path),charset);
            content = content.replaceAll("xaccx",s3);
            content = content.replaceAll("xbgx",s1);
            content = content.replaceAll("xbg2x",s2);
            content = content.replaceAll("xacc2x",s4);
            content = content.replaceAll("xtxtx",s5);
            Globals.getInstance().customTheme = content;
            wr.write(content);
            wr.close();
//            FileWriter fw = new FileWriter("NewTheme.css");
//            fw.write(content);
//            //Files.write(newPath,content.getBytes(charset));
            String save = Globals.getInstance().theme;
            Globals.setTheme("NewTheme.css");
            Globals.getInstance().theme = save;
            //Globals.getInstance().theme= "NewTheme.css";

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("A restart is requiered for this to take efect. We are sorry for the inconvenience.");
            alert.showAndWait();

//            long prevLength = -1;
//            long newLength = file.length();
//            while(prevLength < newLength)
//            {
//                prevLength = newLength;
//                Thread.sleep(250);
//                newLength =file.length();
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void dark()
    {
        Globals.setTheme("DarkTheme.css");
        //Globals.getInstance().theme = "DarkTheme.css";
    }

    public void light()
    {
        Globals.setTheme("LightTheme.css");
        //Globals.getInstance().theme = "LightTheme.css";
    }

    public void def()
    {
        Globals.setTheme("Default.css");
        //Globals.getInstance().theme = "Default.css";
    }

    public void onAccept() {
        if (defC.isSelected())
            def();
        if(darkC.isSelected())
            dark();
        if(lightC.isSelected())
            light();
        if(customC.isSelected())
            custom();
        Stage stage = (Stage) defC.getScene().getWindow();
        stage.close();

//        try {
//            String s = ThemeWindowCtrl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
//            Runtime.getRuntime().exec("java -jar " + s);
//            System.exit(0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

    }
}
