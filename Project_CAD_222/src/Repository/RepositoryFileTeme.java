package Repository;

import Domain.Tema;
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

public class RepositoryFileTeme extends AbstractRepositoryFile<Integer, Tema> {
    public RepositoryFileTeme(String numeFisier, Validator<Tema> validator) throws ValidationException {
        super(numeFisier, validator);
        incarcareDate();
    }

    public void incarcareDate() throws ValidationException {
        Path path = Paths.get("Teme.txt");
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(s -> {
                String[] linie = s.split("[|]");
                if (linie.length != 3)
                    System.out.println("Linie invalid: " + s);
                try {
                    int nrTema = Integer.parseInt(linie[0]);
                    int deadline = Integer.parseInt(linie[2]);
                    Tema tema = new Tema(nrTema, linie[1], deadline);
                    super.save(tema);
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
            for (Tema t : getAll()) {
                String l = "" + t.getId() + "|" + t.getDescriere() + "|" + t.getDeadline();
                pw.println(l);
            }
        } catch (IOException ioe) {
            System.err.println("Eroare: " + ioe);
        }
    }
}
