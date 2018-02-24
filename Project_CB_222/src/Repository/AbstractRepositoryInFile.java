package Repository;

import Domain.HasID;
import Domain.Nota;
import Domain.Student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractRepositoryInFile<Id,E extends HasID<Id>> extends AbstractRepository<Id,E> {

    private String fileName;

    public AbstractRepositoryInFile(Validator validator, String fileName) {
        super(validator);
        this.fileName = fileName;
    }
    public void initialStore(){

        List<E> listOfTeme = readFromFile();

        listOfTeme.forEach(x-> super.save(x));
    }

    public List<E> readFromFile() {


        try (Stream<String> s = Files.lines(Paths.get(fileName))) {
            List<E> note = s.map(x -> StrToObject(x)).collect(Collectors.toList());
            return note;

        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public void writeToFile(String data, String fileName){

        FileWriter fw = null;
        BufferedWriter bw = null;

        try {

            File file = new File(fileName);

            // true = append file
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            bw.write(data);


        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }

    }

    public abstract E StrToObject(String string);

}
