package Repository;

import Domain.Student;
import Domain.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RepositoryStudentiFile extends AbstractFileRepository<Integer, Student> {

    public RepositoryStudentiFile(Validator<Student> validator, String numeFisier) {
        super(validator, numeFisier);
        incarcareFisier();
    }

    public void incarcareFisier() {
        try {
            Path path = Paths.get("./src/" + numeFisier);
            Stream<String> lines;
            lines = Files.lines(path);
            lines.forEach(s -> {
                String[] l = s.split("[;]");
                super.save(new Student(Integer.parseInt(l[0]), l[1], Integer.parseInt(l[2]), l[3], l[4]));
            });
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void salvareFisier() {
        try (PrintWriter pw = new PrintWriter("./src/" + numeFisier)) {
            for (Student s : getAll()) {
                String l = "" + s.getId() + ";" +
                        s.getNume() + ";" +
                        s.getGrupa() + ";" +
                        s.getEmail() + ";" +
                        s.getProfIndrumator();
                pw.println(l);
            }
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }
}
