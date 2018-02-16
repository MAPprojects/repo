package repository;

import java.io.*;

public class RepoIdStudent {

    public String getFromFile(Integer idStudent) {
        BufferedReader reader;
        String s = "";
        try {
            reader = new BufferedReader(new FileReader("./src/main/resources/" + idStudent + ".txt"));
            String line = reader.readLine();
            while (line != null) {
                s += line + "\n";
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public void addInFile(Integer idStudent, String tipactiune, Integer nrTema, Integer nota, Integer deadline, Integer saptPredare, String observatii) throws IOException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("./src/main/resources/" + idStudent + ".txt", true));
            String s = "";
            s += tipactiune + "," + nrTema + "," + nota + "," + deadline + "," + saptPredare + "," + observatii + "\n";
            bw.write(s);
            bw.close();


        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
