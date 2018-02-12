package com.company.Domain;

public class ColorHandler {
    private String bgColor;
    private String bg2Color;
    private String accColor;
    private String acc2Color;
    private String txtColor;

    public ColorHandler(String bg, String bg2, String ac, String ac2, String txt)
    {
        bgColor = bg;
        bg2Color = bg2;
        accColor = ac;
        acc2Color = ac2;
        txtColor = txt;
    }

    public String getTableView()
    {
        return "-fx-table-cell-border-color:" +accColor+"; -fx-control-inner-background:" +bgColor+"; -fx-control-inner-background-alt:" +bg2Color+"; -fx-table-cell-border-size: 120%;";
    }

    public String getTableViewTableCell()
    {
        return "-fx-text-fill:" +txtColor+ "; -fx-font-weight: normal; -fx-font-family: \"Arial\";";
    }

    public String getTableRowSelected()
    {
        return "";
    }

}
