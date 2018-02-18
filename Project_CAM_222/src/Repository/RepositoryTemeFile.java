package Repository;

import Domain.Tema;
import Domain.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RepositoryTemeFile extends AbstractFileRepository<Integer, Tema>  {

    public RepositoryTemeFile(Validator<Tema> validator, String numeFisier) {
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
                super.save(new Tema(Integer.parseInt(l[0]), l[1], Integer.parseInt(l[2])));
            });
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void salvareFisier() {
        try (PrintWriter pw = new PrintWriter("./src/" + numeFisier)) {
            for (Tema t : getAll()) {
                String l = "" + t.getId() + ";" +
                        t.getDescriere() + ";" +
                        t.getDeadline();
                pw.println(l);
            }
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }
}
