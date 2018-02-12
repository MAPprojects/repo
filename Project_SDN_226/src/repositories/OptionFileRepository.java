package repositories;

import entities.Option;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class OptionFileRepository extends AbstractRepository<Option, String> {
    private String filepath;

    public OptionFileRepository(String filepath) {
        super();
        this.filepath = filepath;
        loadData();
    }

    @Override
    public void saveData() {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(filepath));
            for (Option o : findAll()) {

                String information = o.getIdCandidate() + ";" + o.getIdDepartment() + ";" + o.getLanguage() + ";" + o.getPriority() + "\n";
                br.write(information);
            }
            br.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void loadData() {
        Path path = Paths.get(filepath);
        Stream<String> lines;
        try
        {
            lines = Files.lines(path);
            lines.forEach(s -> {
                String fields[];
                fields = s.split(";");
                save(new Option(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), fields[2], Integer.parseInt(fields[3])));
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        /*
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String input;
            String fields[];
            while ((input = br.readLine()) != null) {
                if (!input.equals("")) {
                    fields = input.split(";");
                    //     System.out.println(fields[0] + fields[1]+fields[2]+fields[3]);
                    save(new Option(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), fields[2], Integer.parseInt(fields[3])));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }


    @Override
    public Iterable<Option> nextValues(int pageNumber , int limit) {
        return null;
    }
}

