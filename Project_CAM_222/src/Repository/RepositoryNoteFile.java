package Repository;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Domain.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class RepositoryNoteFile extends AbstractFileRepository<String, Nota>  {
    private IRepository<Integer, Student> repoStudenti;
    private IRepository<Integer, Tema> repoTeme;

    public RepositoryNoteFile(Validator<Nota> validator,
                              String numeFisier,
                              IRepository<Integer,Student> rs,
                              IRepository<Integer,Tema> rt) {
        super(validator, numeFisier);
        repoStudenti = rs;
        repoTeme = rt;
        incarcareFisier();
    }

    public void incarcareFisier() {
        try {
            Path path = Paths.get("./src/" + numeFisier);
            Stream<String> lines;
            lines = Files.lines(path);
            lines.forEach(s -> {
                String[] l = s.split("[;]");
                int idStudent = Integer.parseInt(l[0]);
                Optional<Student> student = repoStudenti.findOne(idStudent);
                int nrTema = Integer.parseInt(l[1]);
                Optional<Tema> tema = repoTeme.findOne(nrTema);
                int valoare = Integer.parseInt(l[2]);
                String id = "" + l[0] + "_" + l[1];
                if (student.isPresent() && tema.isPresent()) {
                    Nota nota = new Nota(id, student.get(), tema.get(), valoare);
                    super.save(nota);
                }
            });
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void salvareFisier() {
        try (PrintWriter pw = new PrintWriter("./src/" + numeFisier)) {
            for (Nota t : getAll()) {
                String l = "" + t.getStudent().getId() + ";" + t.getTema().getId() + ";" + t.getValoare();
                pw.println(l);
            }
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }
}
