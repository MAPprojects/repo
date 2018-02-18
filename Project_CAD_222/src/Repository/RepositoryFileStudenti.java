package Repository;

import Domain.Student;
import Domain.ValidationException;
import Domain.Validator;

import javax.jnlp.IntegrationService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RepositoryFileStudenti extends AbstractRepositoryFile<Integer, Student> {
    public RepositoryFileStudenti(String numeFisier,Validator<Student> validator)throws ValidationException{
        super(numeFisier,validator);
        incarcareDate();
    }

    public void incarcareDate() throws ValidationException {
        Path path = Paths.get("Studenti.txt");
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(s -> {
                String[] linie = s.split("[|]");
                if (linie.length != 5)
                    System.out.println("Linie invalida: " + s);
                try {
                    int idStudent = Integer.parseInt(linie[0]);
                    int grupa = Integer.parseInt(linie[2]);
                    Student student = new Student(idStudent, linie[1], grupa, linie[3], linie[4]);
                    super.save(student);
                } catch (NumberFormatException | ValidationException e) {
                    System.err.println(e);
                }
            });
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    public void scrieFisier() {
        try (PrintWriter pw = new PrintWriter(numeFisier)) {
            for (Student s : getAll()) {
                String l = "" + s.getId() + "|" + s.getNume() + "|" + s.getGrupa() + "|" + s.getEmail() + "|" + s.getProfIndrumator();
                pw.println(l);
            }
        } catch (IOException ioe) {
            System.err.println("Eroare: " + ioe);
        }
    }
}
