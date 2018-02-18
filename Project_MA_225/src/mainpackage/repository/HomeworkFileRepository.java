package mainpackage.repository;

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

public class HomeworkFileRepository extends  HomeworkMemoryRepository{
    private String file_name;

    public HomeworkFileRepository(Validator<Homework> validator, String file_name) {
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

                int id = Integer.parseInt(fields[0]);
                String description = fields[1];
                int deadline = Integer.parseInt(fields[2]);

                try {
                    super.save(new Homework(id, description, deadline));
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
            super.get_all().forEach(homework -> {
                try {
                    out.write(homework.getId() + "|"
                            + homework.getDescription()
                            + "|" + homework.getDeadline()+ "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println("I/O Error");
        }
    }

    @Override
    public void save(Homework entity) throws MyException {
        super.save(entity);
        save_to_file();
    }

    @Override
    public Homework delete(Integer integer) {
        Homework old_Homework = super.delete(integer);
        save_to_file();
        return old_Homework;
    }

    @Override
    public Homework update(Homework entity) throws MyException {
        Homework updated_homework = super.update(entity);
        save_to_file();
        return updated_homework;
    }
}
