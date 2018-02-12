package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Service.Service;
import javafx.scene.control.TabPane;

public class LogsCtrl {

    public Service service;
    public TabPane tabs;

    public void stylise()
    {
        tabs.getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
    }
}

