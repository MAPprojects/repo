package Repository;

import Domain.HasID;
import Domain.Nota;
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

public class NotaRepositoryInFile extends AbstractRepositoryInFile<Integer, Nota> {

    private String fileName;

    public NotaRepositoryInFile(Validator<Nota> validator, String fileName) {
        super(validator, fileName);
        super.initialStore();
        this.fileName = fileName;
    }


    private String getDataForANotaByStudentId(Nota el){
        return "Student adaugat" + "; " + "Titlu tema:  " +  el.getTitlu() + "; " + "Valoarea notei: " + el.getValoareNota() + "; " + "Deadline: "
                + el.getDeadline() + "; " + "Saptamana predarii: " + el.getSaptamanaPredarii() + "; "
                + "Observatii: " + el.getObservatii() + "\n";
    }

    private String getDataForANota(Nota el){
        return "" + el.getId() + ";" + el.getValoareNota() + ";" + el.getIdStudent() + ";" + el.getIdTema() + ";"
                + el.getIdTema() + ";" + el.getDeadline() + ";" + el.getTitlu() + ";" + el.getSaptamanaPredarii() + ";" + el.getObservatii() + "\n";
    }

    private String getFileNameForStudent(Nota el){
        return ""+ el.getIdStudent() + "Student.txt";
    }

    @Override
    public void save(Nota el) {
        super.save(el);

        String dataForParticularStudent = getDataForANotaByStudentId(el);
        String specialFileName = getFileNameForStudent(el);
        super.writeToFile(dataForParticularStudent, specialFileName);

        String data = getDataForANota(el);
        super.writeToFile(data, fileName);

    }

    @Override
    public void update(Nota el) {
        super.update(el);

        String data = getDataForANotaByStudentId(el);
        String specilaFileName = getFileNameForStudent(el);
        writeToFile(data, specilaFileName);
    }


    @Override
    public Nota StrToObject(String string) {
        String [] s = string.split("[;]");
        int idNota = Integer.parseInt(s[0]);
        int valoareNota = Integer.parseInt(s[1]);
        int idStudent = Integer.parseInt(s[2]);
        int idTema = Integer.parseInt(s[3]);
        int deadline = Integer.parseInt(s[4]);
        String titlu = s[5];
        int saptamanaPredarii = Integer.parseInt(s[6]);
        String observatii = s[7];


        return new Nota(idNota, valoareNota, idStudent, idTema, deadline, titlu, saptamanaPredarii, observatii);
    }
}