package Utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;

public class GradeExporter {
    private String fileName;

    public GradeExporter(String fileName) {
        this.fileName = fileName;
    }

    //exports nota to file
    public void exportGrade(String operation, int nrTema, int valoare, int deadline, int week, String observatie) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName + ".txt", true));

            bw.write(operation +
                        "tema nr: " + nrTema + "; " +
                        "nota: " + valoare + "; " +
                        "deadline: " + deadline + "; " +
                        "tema predata in saptamana: "+ week + "; " +
                        "observatii: " + observatie);
            bw.newLine();
            bw.flush();
                //System.out.println("GRADE EXPORTED");

        } catch (IOException e) {
            System.err.println(e);
        }
    }


}
