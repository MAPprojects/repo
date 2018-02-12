package repository.fileRepo;

import entities.Student;
import org.hibernate.Session;
import repository.AbstractCrudRepo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class StudentFileRepository extends AbstractCrudRepo<Student, String> {

    private String filePath;

    /**
     * Constructor al clasei
     *
     * @param filePath String -> path-ul catre fisierul care va folosi repo-ul
     * @throws IOException Arunca exceptie daca nu exista fisierul introdus
     */
    public StudentFileRepository(String filePath) throws IOException {
        super(Student.class);
        this.filePath = filePath;
        loadData();
    }

    /**
     * Salveaza in repository si in fisier un student
     *
     * @param entity E -> Studentul pe care dorim sa il salvam
     * @return Student -> Studentul pe care l-am salvat
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public Optional<Student> save(Student entity) throws IOException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        super.save(entity);
        saveData();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(entity);
    }

    /**
     * Sterge din repo si din fisier un student
     *
     * @param id -> Id-ul studentului pe care dorim sa il stergem
     * @return Student -> Studentul pe care l-am sters
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public Optional<Student> delete(String id) throws IOException {
        Student s = super.delete(id).get();
        saveData();
        return Optional.ofNullable(s);
    }

    /**
     * Face update la un student deja existent
     *
     * @param id Id-ul studentului pe care dorim sa il modificam
     * @param entity  E -> the new entity to be added in the repository
     * @return Returneaza studentul nou pe care l-am modificat
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public Optional<Student> update(String id, Student entity) throws IOException {
        Student s = super.update(id, entity).get();
        saveData();
        return Optional.ofNullable(s);
    }

    /**
     * Populeaza repo=ul cu o colectie de Studenti
     *
     * @param elements Collection<E> -> o colectie de elemente de tipul generic E
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public void populate(Collection<Student> elements) throws IOException {
        for (Student s : elements) {
            this.save(s);
        }
    }

    /**
     * Incarca din fisier Studentii
     *
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public void loadData() throws IOException {
        Path path = Paths.get(filePath);
        Stream<String> lines;
        lines = Files.lines(path);
        lines.forEach((line) -> {
            String[] formatLine = line.split(",");
            try {
                super.save(new Student(formatLine[0], formatLine[1], formatLine[2], formatLine[3], formatLine[4]));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
//        BufferedReader bf = new BufferedReader(new FileReader(this.filePath));
//        String line = bf.readLine();
//        while (line != null) {
//            String[] formatLine = line.split(",");
//            super.save(new Student(Integer.parseInt(formatLine[0]), formatLine[1], formatLine[2], formatLine[3], formatLine[4]));
//            line = bf.readLine();
//        }
//        bf.close();
    }

    /**
     * Scrie in fisier Studentii
     *
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public void saveData() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(this.filePath));
        bw.flush();
        ArrayList<Student> students = new ArrayList<>((Collection<Student>) super.findAll());
        for (Student s : students) {
            String line = s.getId() + "," + s.getNume() + "," + s.getGrupa()
                    + "," + s.getEmail() + "," + s.getCadruDidacticIndrumator() + "\n";
            bw.write(line);
        }
        bw.close();
    }
}
