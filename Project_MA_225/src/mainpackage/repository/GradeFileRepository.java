package mainpackage.repository;

import javafx.util.Pair;
import mainpackage.domain.Grade;
import mainpackage.domain.Homework;
import mainpackage.exceptions.MyException;
import mainpackage.validator.Validator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class GradeFileRepository extends AbstractRepository<Grade, Pair<Integer,Integer> > {
    private String file_name;

    public GradeFileRepository(Validator<Grade> validator, String file_name) {
        super(validator);
        this.file_name = file_name;
        read_from_file();
    }

    private void read_from_file(){
        Path path = Paths.get(file_name);
        Stream<String> lines;

        try {
            lines = Files.lines(path);
            //lines.forEach(System.out::println);
            lines.forEach(line -> {
                String fields[] = line.split("\\|");
                int idStudent = Integer.parseInt(fields[0]);
                int idHomework = Integer.parseInt(fields[1]);
                Float value = Float.parseFloat(fields[2]);

                try {
                    super.save(new Grade(idStudent, idHomework,value));
                } catch (MyException e) {
                    System.out.println("Validation when reading from file! " + e.getMessage());
                }
            } );

        } catch (IOException e) {
            System.out.println("I/O Error.");
        }
    }

    private void save_to_file() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file_name, false))) {
            super.get_all().forEach(grade -> {
                try {
                    out.write(grade.get_idStudent() + "|"
                            + grade.get_idHomework()
                            + "|" + grade.get_value()+ "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println("I/O Error");
        }
    }

    @Override
    public void save(Grade entity) throws MyException {
        super.save(entity);
        save_to_file();
    }

    @Override
    public Grade delete(Pair<Integer,Integer> grade_id) {
        Grade old_grade = super.delete(grade_id);
        save_to_file();
        return old_grade;
    }

    @Override
    public Grade update(Grade entity) throws MyException {
        Grade updated_grade = super.update(entity);
        save_to_file();
        return updated_grade;
    }
}
