package repositories;


import entities.Department;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DepartmentFileRepository extends AbstractRepository<Department, Integer> {
    private String filepath;

    public DepartmentFileRepository(String filepath) {
        super();
        this.filepath = filepath;
        loadData();
    }

    @Override
    public void saveData() {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(filepath));
            for (Department d : findAll()) {

                String information = d.getId() + ";" + d.getName() + ";" + d.getNrLoc() + "\n";
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
                save(new Department(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2])));
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

                    save(new Department(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2])));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }


    @Override
    public Iterable<Department> nextValues(int pageNumber , int limit) {
        return null;
    }
}
