package repositories;

import entities.Candidate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CandidateFileRepository extends AbstractRepository<Candidate, Integer> {
    private String filepath;

    public CandidateFileRepository(String filepath) {
        super();
        this.filepath = filepath;
        loadData();
    }

    /**
     * Save data in file
     */
    @Override
    public void saveData() {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(filepath));
            for (Candidate c : findAll()) {

                String information = c.getId() + ";" + c.getName() + ";" + c.getPhone() + ";" + c.getEmail() + "\n";
                br.write(information);
            }
            br.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * Load data from file
     */
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
                save(new Candidate(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3]));
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    /**
     * Save a candidate and create a log
     * @param entity
     * @return
     */
    @Override
    public Candidate save(Candidate entity) {
        Candidate c = super.save(entity);
        File f = new File("logs\\" + entity.getId() + ".txt");
        if (!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return c;
    }

    /**
     * Get values from repo at a given offset
     * @param pageNumber
     * @param limit
     * @return
     */
    @Override
    public Iterable<Candidate> nextValues(int pageNumber , int limit) {
        return null;
    }
}
