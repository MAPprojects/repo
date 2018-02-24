package Repository;

import Domain.HasID;
import Domain.Student;
import Domain.Tema;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentRepositoryInFile extends AbstractRepositoryInFile<Integer, Student> {

    private String filename;

    public StudentRepositoryInFile(Validator<Student> validator, String fileName) {
        super(validator, fileName);
        this.filename = fileName;
        super.initialStore();
    }


    @Override
    public void save(Student el) {
        super.save(el);
        String data = el.getId() + ";" + el.getNume() + ";" + el.getGrupa() + ";" + el.getEmail() + ";" + el.getCadruDidactic()+ "\n";
        super.writeToFile(data, filename);
    }

    @Override
    public Student StrToObject(String string) {
        String [] s = string.split("[;]");
        int idStudnet = Integer.parseInt(s[0]);
        String nume = s[1];
        int grupa = Integer.parseInt(s[2]);
        String email = s[3];
        String cadruDidacic = s[4];

        return new Student(idStudnet, nume, grupa, email, cadruDidacic);

    }

}
