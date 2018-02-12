package com.company.Domain;

import com.company.Repositories.SQLStudentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Globals {

    private static Globals globals = new Globals();

    public int saptCurenta;
    public int accessLevel;
    public int nonAdminID;
    public String theme;
    public String customTheme = "";

    private void readConfig()
    {
        Connection connection = SQLStudentRepository.getConnection();
        String querry = "SELECT * FROM Config";
        try{
            PreparedStatement ps = connection.prepareStatement(querry);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            saptCurenta = resultSet.getInt("sapt");
            theme = resultSet.getString("css");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Globals()
    {
        readConfig();
    }

    public static Globals getInstance() {
        return globals;
    }

    public static void setSaptCurenta(int s)
    {
        globals.saptCurenta=s;
        updateSQL();
    }

    public static void setTheme(String theme){
        globals.theme=theme;
        updateSQL();
    }

    public static void updateSQL()
    {
        Connection connection = SQLStudentRepository.getConnection();
        try
        {
            String querry = "DELETE FROM Config";
            PreparedStatement ps = connection.prepareStatement(querry);
            ps.executeUpdate();
            querry = "INSERT INTO Config VALUES (" + globals.saptCurenta + ",\'" + globals.theme + "\')";
            ps = connection.prepareStatement(querry);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSaptCurenta()
    {
        return saptCurenta;
    }
}
