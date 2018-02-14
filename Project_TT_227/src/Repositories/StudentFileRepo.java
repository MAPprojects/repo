package Repositories;

import Domain.Student;
import Exceptions.RepositoryException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class StudentFileRepo extends StudentRepository {

    private String fileName;
    private Map<Integer, Student> students;

    public StudentFileRepo(String fileName) {
        super();
        this.fileName = fileName;
        students = readFromFile(fileName);
        setStorage(students);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public Optional<Student> save(Student entitate) throws RepositoryException {
        Student savedStudent = super.save(entitate).get();
        saveToFile(fileName);
        return Optional.ofNullable(savedStudent);
    }

    @Override
    public Optional<Student> delete(Integer id_entitate) throws RepositoryException {
        Student deletedStudent = super.delete(id_entitate).get();
        saveToFile(fileName);
        return Optional.ofNullable(deletedStudent);
    }

    @Override
    public Optional<Student> update(Integer id_entitate, Student entitate) throws RepositoryException {
        Student updatedStudent = super.update(id_entitate,entitate).get();
        saveToFile(fileName);
        return Optional.ofNullable(updatedStudent);
    }

    @Override
    public Optional<Student> findEntity(Integer id_entitate) throws RepositoryException {
        return Optional.ofNullable(super.findEntity(id_entitate).get());
    }

    @Override
    public Iterable<Student> findAll() {
        return super.findAll();
    }

    @Override
    public void setStorage(Map<Integer, Student> newStorage) {
        super.setStorage(newStorage);
    }

    protected Map<Integer, Student> readFromFile(String fileName) {
        Map allStudents = new HashMap<Integer, Student>();
        Stream<String> lines;
        Path filePath = Paths.get(fileName);
        try {
            lines = Files.lines(filePath);
            lines.forEach(line-> {
                String[] info = line.split(",");
                int studentID = Integer.valueOf(info[0]);
                String nume = info[1];
                int grupa = Integer.valueOf(info[2]);
                String email = info[3];
                String profLab = info[4];

                Student student = new Student(studentID, nume, grupa, email, profLab);

                allStudents.put(student.getID(), student);
            });
            /*BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String linie;
            linie = reader.readLine();

            while (linie != null) {
                String[] info = linie.split(",");
                int studentID = Integer.valueOf(info[0]);
                String nume = info[1];
                int grupa = Integer.valueOf(info[2]);
                String email = info[3];
                String profLab = info[4];

                Student student = new Student(studentID, nume, grupa, email, profLab);

                allStudents.put(student.getID(), student);

                linie = reader.readLine();
            }*/
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return allStudents;
    }

    protected void saveToFile(String fileName) {
        try {
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file,false);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Student student : findAll()) {
                bw.write(student.getID() + "," + student.getNume() + "," + student.getGrupa() + "," + student.getEmail() + "," + student.getProfLab());
                bw.newLine();
            }
            bw.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
