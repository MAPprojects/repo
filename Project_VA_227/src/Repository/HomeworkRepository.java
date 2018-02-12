package Repository;

import Domain.Homework;
import Domain.Note;
import Domain.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class HomeworkRepository extends AbstractFileRepository<Integer, Homework> {
    public HomeworkRepository(String filename) {
        super(filename);
    }

    @Override
    public void loadData() throws IOException {
        Files.lines(this.filename).forEach(line -> {
            String[] attributes = line.split("#");
            if (attributes.length == 3)
                this.save(new Homework(Integer.parseInt(attributes[0]), attributes[1],
                        Integer.parseInt(attributes[2])));
        });
    }
}
