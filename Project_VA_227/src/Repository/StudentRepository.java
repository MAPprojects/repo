package Repository;

import Domain.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class StudentRepository extends AbstractFileRepository<Integer, Student> {

    public StudentRepository(String filename) {
        super(filename);
    }

    @Override
    public void loadData() throws IOException {
       Files.lines(this.filename).forEach(line -> {
           String[] attributes = line.split("#");
           if (attributes.length == 5)
               this.save(new Student(Integer.parseInt(attributes[0]), attributes[1],
                       Integer.parseInt(attributes[2]), attributes[3], attributes[4]));
       });
    }

    @Override
    public Optional<Student> delete(Integer integer) {
        try {
            Files.delete(Paths.get(this.filename + "/" + integer.toString() + ".txt"));
        } catch (IOException ignored) {

        }
        return super.delete(integer);
    }
}
