package Repository;

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

public class TemaRepositoryInFile extends AbstractRepositoryInFile<Integer, Tema> {

    private String filename;

    public TemaRepositoryInFile(Validator<Tema> validator, String fileName) {
        super(validator, fileName);
        this.filename = fileName;
        super.initialStore();
    }

    @Override
    public void save(Tema el) {
        super.save(el);
        String data = el.getId() + ";" + el.getDescriere() + ";" + el.getDeadline() + ";" + el.getTitlu() + "\n";
        writeToFile(data, filename);
    }

    @Override
    public Tema StrToObject(String string) {
        String [] s = string.split("[;]");
        int idTema = Integer.parseInt(s[0]);
        String descriere = s[1];
        int deadline = Integer.parseInt(s[2]);
        String titlu = s[3];

        return new Tema(idTema, descriere, deadline, titlu);
    }


}
