package mainpackage.repository;

import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;
import mainpackage.validator.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StudentFileRepository extends StudentMemoryRepository {
    private String file_name;

    public StudentFileRepository(Validator<Student> validator, String file_name) {
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
                String group = fields[1];
                String name = fields[2];
                String email = fields[3];
                String teacher = fields[4];

                try {
                    super.save(new Student(name, id, email, teacher, group));
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
            super.get_all().forEach(student -> {
                try {
                    out.write(student.getId() + "|"
                            + student.getGroup() + "|"
                            + student.getName()
                            + "|" + student.getEmail()
                            + "|" + student.getTeacher()+ "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println("I/O Error");
        }
    }

    @Override
    public void save(Student entity) throws MyException {
        super.save(entity);
        save_to_file();
    }

    @Override
    public Student delete(Integer integer) {
        Student old_student = super.delete(integer);
        save_to_file();
        return old_student;
    }

    @Override
    public Student update(Student entity) throws MyException {
        Student updated_student = super.update(entity);
        save_to_file();
        return updated_student;
    }
}
