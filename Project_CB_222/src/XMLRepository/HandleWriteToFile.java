package XMLRepository;

import Domain.Nota;
import Domain.Student;
import sun.misc.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HandleWriteToFile {

    private void writeToFile(String data, String fileName){

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

    private String createDataStudent(Student student){
        String studentData = "Id: " + student.getId() + "   " + "Nume: " + student.getNume() + "   " + "Grupa: "
                + student.getGrupa() + "   " + "Email: " + student.getEmail() + "   " + "Cadru didactic: " + student.getCadruDidactic() + "\n";

        String newline = "--------------------------------------------------------------------------------------------------------------------------" + "\n";

        return studentData + newline;
    }

    private String createDataStudent(Nota nota){
        String notaData = "Tema: " + nota.getTitlu() + "   " + "Nota: " + nota.getValoareNota() + "   " + "Deadline: " + nota.getDeadline()
                + "   " + "Saptamana predarii: " + nota.getSaptamanaPredarii() + "   " + "Observatii: " + nota.getObservatii()  + "\n";
        String newline = "--------------------------------------------------------------------------------------------------------------------------" + "\n";
        return  notaData + newline;

    }

    public void writeStudent(Student student, String way){
        String createStudent = createDataStudent(student);
        String finalData = way + ":    " + createStudent;

        String fileName = ""+ student.getId() + "Student.txt";
        writeToFile(finalData, fileName);
    }

    public void writeStudent(Nota nota, String way){
        String createStudent = createDataStudent(nota);
        String finalData = way + ":    " + createStudent;

        String fileName = ""+ nota.getIdStudent() + "Student.txt";

        writeToFile(finalData, fileName);
    }

    private String createString(List<String> listOfString){

        StringBuilder sb = new StringBuilder();
        for (String s : listOfString)
        {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void deleteFile(Student student){
        try
        {
            String filename = ""+student.getId() + "Student.txt";
            Files.deleteIfExists(Paths.get(filename));
        }
        catch(NoSuchFileException e)
        {
            System.out.println("No such file/directory exists");
        }
        catch(DirectoryNotEmptyException e)
        {
            System.out.println("Directory is not empty.");
        }
        catch(IOException e)
        {
            System.out.println("Invalid permissions.");
        }

        System.out.println("Deletion successful.");
    }

    private String readFromFile(Student student) {

        String fileName = student.getId()+ "Student.txt";

        try(Stream<String> s = Files.lines(Paths.get(fileName))) {
            List<String> streamToString = new ArrayList<>();
            s.collect(Collectors.toCollection(() -> streamToString));
            String finalString = createString(streamToString);

            return finalString;

        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public String getTextToEmail(Student student){
        return readFromFile(student);
    }
}
