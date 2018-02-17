package Service;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Export {
    protected String fileName;

    public Export(String file) {
        this.fileName = file;
    }

    public void export(String Operatie, String section, Integer priority) {

        try (PrintWriter outputFile = new PrintWriter(new FileWriter(fileName, true))) {
            String s = "Operatie: " + Operatie + "      Section: " + section + "         Priority: " + priority + "\n";
            outputFile.printf(s);
        } catch (IOException e) {
            System.err.println("ERR" + e);
        }
    }
}
