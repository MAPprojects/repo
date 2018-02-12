package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Domain.Student;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NewsCtrl {
    public Label newsTitle;
    public Button addNews;
    public TextArea text;
    public Label date;

    private String fileName;
    private int page;
    private String a1;
    private String a2;
    private String a3;
    private String d1;
    private String d2;
    private String d3;
    private String t1;
    private String t2;
    private String t3;

    public void initialize()
    {
        fileName = "News.txt";
        page = 0;
        date.setText("Date:");
        a1="";
        a2="";
        a3="";

        BufferedReader read = null;
        try {
            read = new BufferedReader(new FileReader(this.fileName));
            String line = read.readLine();
            while(line != null)
            {
                if(line.equals("-----")) {
                    page++;
                    line = read.readLine();
                    if(page==1)
                        d1 = line;
                    if(page==2)
                        d2 = line;
                    if(page==3)
                        d3 = line;
                    //date.setText("Date:" + line);
                }
                else
                {
                    if(page==1)
                        a1=a1+line;
                    if(page==2)
                        a2=a2+line;
                    if(page==3)
                        a3=a3+line;
                    //text.appendText(line);
                }
                line = read.readLine();
            }
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        text.appendText(a1);
    }

    public void change1()
    {
        page=1;
        text.clear();
        text.appendText(a1);
    }

    public void change2()
    {
        page=2;
        text.clear();
        text.appendText(a2);
    }

    public void change3()
    {
        page=3;
        text.clear();
        text.appendText(a3);
    }

    public void addNews()
    {
        Stage stage = new Stage();
        TextArea newText = new TextArea();
        BorderPane bp = new BorderPane();
        bp.setCenter(newText);

        bp.setBottom(new Button("Save"));
        Scene scene = new Scene(bp);
        scene.getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        stage.setScene(scene);

    }

    public void connectNews()
    {
        try {
            URL url = new URL("http://www.cs.ubbcluj.ro/");
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));


            while (!(bf.readLine().contains("id=\"recent-posts-2\""))) {
                bf.readLine();
            }

            for (int i = 0; i <= 7; i++) {
                //System.out.println(bf.readLine());
                String findUrl = bf.readLine();
                if(findUrl.contains("http://"))
                {
                    String[] forUrl = findUrl.split("\"");
                    URL urlNews = new URL(forUrl[1]);
                    URLConnection conNews = urlNews.openConnection();
                    InputStream isNews = conNews.getInputStream();
                    BufferedReader bfNews = new BufferedReader(new InputStreamReader(isNews));

                    boolean gotTitle = false;
                    boolean notOn2ndTitle = true;
                    boolean notStop = true;

                    String line = bfNews.readLine();
                    while(line!=null && notStop)
                    {
                        //System.out.println(line);
                        //System.out.println(line.equals("</p>"));

                        if(line.contains("<a href=\"http://www.cs.ubbcluj.ro/anunturi/\" rel=\"category tag\">"))
                        {
                            notStop=false;
                        }

                        if(line.contains("<p>")) {
                            //System.out.println(line);
                            //System.out.println(line.contains("<a href=\"http://www.cs.ubbcluj.ro/anunturi/\" rel=\"category tag\">"));


                            if(line.contains("title="))
                            {
                                if(gotTitle)
                                    notOn2ndTitle = false;
                                else {
                                    t1 = line.substring(line.indexOf("title="));
                                    //System.out.println(line);
                                    //line = line.substring(line.indexOf("\""),line.substring(1).indexOf("\"") +1);
                                    t1 = t1.substring(t1.indexOf("\""));
                                    t1 = t1.substring(1);
                                    t1 = t1.substring(0, t1.indexOf("\""));
                                    //line = line.substring(1,line.indexOf("\""));
                                    gotTitle = true;
                                    System.out.println(t1);

                                }
                            }
                            if(notOn2ndTitle)
                            {
                                //String buildingLine = line.substring(line.indexOf("<p>")+3,line.indexOf("</p>"));
                                //System.out.println(buildingLine);
                                System.out.println(line);

                                String buildingLine = line.substring(line.indexOf("<p>")+3);
                                System.out.println(buildingLine);

                            }


                        }
                        line = bfNews.readLine();
                    }

                    //System.out.print(a1);



                }
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
