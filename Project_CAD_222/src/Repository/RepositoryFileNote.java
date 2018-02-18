package Repository;

import Domain.*;

import javax.jnlp.IntegrationService;
import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class RepositoryFileNote extends AbstractRepositoryFile<String, Nota> {
    private IRepository<Integer, Student> repoStudenti;
    private IRepository<Integer, Tema> repoTeme;
    public RepositoryFileNote(String numeFisier,
                              Validator<Nota> validator,
                              IRepository<Integer,Student> rs,
                              IRepository<Integer,Tema> rt) throws ValidationException {
        super(numeFisier, validator);
        repoStudenti = rs;
        repoTeme = rt;
        incarcareDate();
    }

    public void incarcareDate() throws ValidationException {
        Path path = Paths.get("Catalog.txt");
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(s -> {
                String[] linie = s.split("[|]");
                if (linie.length != 3)
                    System.out.println("Linie invalida: " + s);
                try {
                    int idStudent=Integer.parseInt(linie[0]);
                    Optional<Student> student=repoStudenti.findOne(idStudent);
                    int nrTema= Integer.parseInt(linie[1]);
                    Optional<Tema> tema=repoTeme.findOne(nrTema);
                    int valoare=Integer.parseInt(linie[2]);
                    if (student.isPresent() && tema.isPresent()) {
                        String id=""+idStudent+'-'+nrTema;
                        Nota nota = new Nota(id, student.get(), tema.get(), valoare);
                        super.save(nota);
                    }
                } catch (NumberFormatException | ValidationException e) {
                    System.err.println(e);
                }
            });
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    public void scrieFisier() {
        //Catalog
        try (PrintWriter pw = new PrintWriter(numeFisier)) {
            for (Nota n : getAll()) {
                String l = "" + n.getStudent().getId() + "|" + n.getTema().getId() + "|" + n.getValoare();
                pw.println(l);
            }
        } catch (IOException ioe) {
            System.err.println("Eroare: " + ioe);
        }
    }
}
