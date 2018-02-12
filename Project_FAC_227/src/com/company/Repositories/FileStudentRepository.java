package com.company.Repositories;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import com.company.Domain.Student;
import com.company.Exceptions.RepositoryException;

public class FileStudentRepository extends StudentRepository{

    private String fileName;
    private Path filePath;

    public FileStudentRepository(String fileName) throws RepositoryException {
        super();
        this.fileName = fileName;
        this.filePath = Paths.get(fileName);
        loadDataStream();
    }

    public void loadDataStream() throws RepositoryException {
        Path path = this.filePath;
        Stream<String> lines;
        try{
            lines =  Files.lines(path);
            lines.forEach(s-> {
                String[] student = s.split(",");
                try {
                    super.save(new Student(Integer.parseInt(student[0]),student[1],Integer.parseInt(student[2]),student[3],student[4]));
                } catch (RepositoryException e) {

                }
            });
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void loadData() throws RepositoryException {
        BufferedReader read = null;
        try {
            read = new BufferedReader(new FileReader(this.fileName));
            String line = read.readLine();
            while(line != null)
            {
                String params[] = line.split(",");
                super.save(new Student(Integer.parseInt(params[0]),params[1],Integer.parseInt(params[2]),params[3],params[4]));
                line = read.readLine();
            }
            read.close();
        } catch (FileNotFoundException e) {
            throw new RepositoryException("File not found!");
        } catch (IOException e) {
            throw new RepositoryException("Eroare la citire din fisier!");
        } catch (RepositoryException e) {
            throw e;
        }
    }

    public void saveData() throws RepositoryException {
        try {
            ArrayList<Student> allEntities = new ArrayList<Student> ((Collection<? extends Student>) super.findAll());
            BufferedWriter write = new BufferedWriter(new FileWriter(this.fileName));
            for(Student student: allEntities)
            {
                String line = "";
                line = student.getID() + "," + student.getNume() + "," + student.getGrupa() + "," + student.getEmail() + "," + student.getProfLab() + "\n";
                write.write(line);
            }
            write.close();
        } catch (IOException e) {
            throw new RepositoryException("Eroare la scriere in fisier!");
        }
    }


    @Override
    public Student save(Student entitate) throws RepositoryException {
        Student student = super.save(entitate);
        saveData();

        String studentFileName = "Data\\" + student.getID() + ".txt";
        File studentFile = new File(studentFileName);
        try {
            studentFile.createNewFile();
        } catch (IOException e) {
            throw new RepositoryException("Nu s-a putut crea fisierul studentului!");
        }


        return student;
    }

    @Override
    public Optional<Student> delete(Integer id_entitate) throws RepositoryException {
        Student student = super.delete(id_entitate).get();
        saveData();

        String studentFileName = "Data\\" + student.getID() + ".txt";
        File studentFile = new File(studentFileName);
        studentFile.delete();

        return Optional.ofNullable(student);
    }

    @Override
    public Student update(Student student) throws RepositoryException {
        Student st = super.update(student);
        saveData();
        return st;
    }


    /*
    @Override
    public Student delete(Integer id_entitate) throws RepositoryException {
        Student student = super.delete(id_entitate);
        saveData();

        String studentFileName = "Data\\" + student.getID() + ".txt";
        File studentFile = new File(studentFileName);
        studentFile.delete();

        return student;
    }
    */
}
