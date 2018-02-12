package com.company.GUI;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class WelcomeScreenCtrl {
    public Button crudB;
    public Button logsB;
    public Button filterB;
    public Button raportsB;
    public Button weekB;
    public Button themeB;
    public Button passB;
    public Button exitB;

    public ImageView crudI;
    public ImageView filtrariI;
    public ImageView logI;
    public ImageView raportI;
    public ImageView weekI;
    public ImageView themeI;
    public ImageView passI;
    public ImageView exitI;

    public void initialize()
    {
        Tooltip crudT = new Tooltip("CRUD Operations");
        crudT.setFont(Font.font("",17));
        Tooltip.install(crudI,crudT);

        Tooltip filterT = new Tooltip("Filters");
        filterT.setFont(Font.font("",17));
        Tooltip.install(filtrariI,filterT);

        Tooltip logsT = new Tooltip("Logs");
        logsT.setFont(Font.font("",17));
        Tooltip.install(logI,logsT);

        Tooltip raportsT = new Tooltip("Raports");
        raportsT.setFont(Font.font("",17));
        Tooltip.install(raportI,raportsT);

        Tooltip weekT = new Tooltip("Change Week");
        weekT.setFont(Font.font("",17));
        Tooltip.install(weekI,weekT);

        Tooltip colorT = new Tooltip("Select Theme Color");
        colorT.setFont(Font.font("",17));
        Tooltip.install(themeI,colorT);

        Tooltip passT = new Tooltip("Change password");
        passT.setFont(Font.font("",17));
        Tooltip.install(passI,passT);

        Tooltip exitT = new Tooltip("EXIT");
        exitT.setFont(Font.font("",17));
        Tooltip.install(exitI,exitT);

    }
}
