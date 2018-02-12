package services;

import entities.Filter;
import entities.FilterImpl;
import entities.HasId;
import entities.Student;
import exceptions.StudentServiceException;
import observer_utils.AbstractObservable;
import observer_utils.StudentEvent;
import observer_utils.StudentEventType;
import repository.AbstractCrudRepo;
import validator.StudentValidator;
import exceptions.AbstractValidatorException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class StudentService extends AbstractObservable<StudentEvent> {
    private AbstractCrudRepo<Student, String> repository;
    private StudentValidator validator;
    private UserSerivce userSerivce;

    public StudentService() {

    }

    /**
     * Consturctor al clasei StudentService
     *
     * @param repository StudentRepository
     * @param validator  StudentValidator
     */
    public StudentService(AbstractCrudRepo<Student, String> repository, StudentValidator validator) {
        super();
        this.repository = repository;
        this.validator = validator;
    }

    public void setUserSerivce(UserSerivce userSerivce) {
        this.userSerivce = userSerivce;
    }

    /**
     * Salveaza un student in repository
     *
     * @param nume String -> Numele studentului pe care dorim sa il salvam in repository
     * @return Student -> entitatea ce tocmai a fost salvata in repository
     * @throws AbstractValidatorException Arunca aexceptie daca studentul nu indeplineste conditiile de validare
     */
    public Student addStudent(String nume, String grupa, String email, String prof) throws AbstractValidatorException, StudentServiceException, IOException {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString().replace("-", "");
        } while (repository.findOne(uuid).isPresent());
        Student student = new Student(uuid, nume, grupa, email, prof);
        //prima data validam studentul
        validator.validate(student);
        //daca totul este ok il salvam in repo
        repository.save(student);
        File studentFile = new File("src\\main\\resources\\" + student.getId() + ".txt");
        studentFile.createNewFile();
        notifyAll(new StudentEvent(StudentEventType.ADD, student));
        return student;
    }

    /**
     * Sterge un student din repository
     *
     * @param student Student
     * @return Student -> Returneaza studentul ce tocmai a fost sters
     * @throws AbstractValidatorException Arunca aexceptie daca studentul nu indeplineste conditiile de validare
     */
    public Student deleteStudent(Student student) throws AbstractValidatorException, StudentServiceException, IOException {
        validator.validate(student);
        if (findOneStudentById(student.getId()) == null) {
            throw new StudentServiceException("Studentul nu exista in repository");
        }
        repository.delete(student.getId());
        File studentFile = new File("src\\main\\resources\\" + student.getId() + ".txt");
        studentFile.delete();
        notifyAll(new StudentEvent(StudentEventType.REMOVE, student));
        return student;
    }

    /**
     * Modifica un student deja existent in repository
     *
     * @param actualStudentId - id-ul studentului deja existent
     * @param nume            - noul student pe care il salvam in locul vechiului student
     * @return Student -> noul student
     */
    public Student updateStudent(String actualStudentId, String nume, String grupa, String email, String prof) throws StudentServiceException, IOException, AbstractValidatorException {
        if (!repository.findOne(actualStudentId).isPresent()) {
            throw new StudentServiceException("Nu exista un astfel de student");
        }
        Student student = new Student(actualStudentId, nume, grupa, email, prof);
        validator.validate(student);
        repository.update(actualStudentId, student);
        notifyAll(new StudentEvent(StudentEventType.UPDATE, student));
        return student;
    }

    /**
     * Cauta un student in repository pe baza id-ului
     *
     * @return Student -> studentul ce are id-ul introdus
     */
    public Student findOneStudentById(String id) throws StudentServiceException {
        Optional<Student> studentOptional = repository.findOne(id);
        if (!studentOptional.isPresent()) {
            throw new StudentServiceException("Nu exista niciun student cu acest id");
        }
        Student student = studentOptional.get();
        return student;
    }

    /**
     * Returneaza o lista cu toti studentii din repository
     *
     * @return ArrayList<Student> -> toti studentii din repository
     */
    public ArrayList<Student> findAllStudents() {
        return new ArrayList<>((Collection<Student>) repository.findAll());
    }

    /**
     * Returneaza numarul total de studenti din repository
     *
     * @return int -> numarul de studenti din repository
     */
    public int numarStudenti() {
        return (int) repository.size();
    }

    /**
     * Filtreaza studentii de la un profesor si ii ordoneaza crescator
     *
     * @param numeCadruDidactic - String ->numele profesorului de la laborator
     * @return Returneaza o lista cu toti studentii pe care i-a gasit
     */
    public Optional<ArrayList<Student>> filtreazaStudentiiDeLaUnProfesor(String numeCadruDidactic) {
        ArrayList<Student> result;
        List<Student> students = new ArrayList<Student>((Collection<? extends Student>) repository.findAll());
        Predicate<Student> predicate = (s) -> {
            return s.getCadruDidacticIndrumator().equals(numeCadruDidactic);
        };
        Filter filter = new FilterImpl();
        Comparator<Student> comparator = (s1, s2) -> {
            return s1.getNume().compareTo(s2.getNume());
        };
        result = new ArrayList<>(filter.defaultFilterAndSort(students, predicate, comparator));
        return Optional.ofNullable(result);
    }

    /**
     * Filtreaza studentii dintr-o grupa si ii afiseaza descrescator in functie de nume
     *
     * @param grupa - String ->grupa profesorului
     * @return Returneaza o lista cu toti studentii pe care i-a gasit
     */
    public Optional<ArrayList<Student>> filtreazaStudentiiDinGrupa(String grupa) {
        ArrayList<Student> result;
        List<Student> students = new ArrayList<Student>((Collection<? extends Student>) repository.findAll());
        Filter filter = new FilterImpl();
        result = new ArrayList<>(filter.defaultFilter(students, new Predicate<Student>() {
            @Override
            public boolean test(Student student) {
                return student.getGrupa().equals(grupa);
            }
        }));
        Collections.sort(result, (s1, s2) -> {
            return s2.getNume().compareTo(s1.getNume());
        });
        return Optional.ofNullable(result);
    }

    /**
     * Filtreaza studentii cu acelasi nume si ii sorteaza crescator dupa numele cadrului didactic
     *
     * @param nume - String ->numele pe baza caruia filtram
     * @return Returneaza o lista cu toti studentii pe care i-a gasit
     */
    public Optional<ArrayList<Student>> filtreazaStudentiiCuNumele(String nume) {
        ArrayList<Student> result;
        List<Student> students = new ArrayList<Student>((Collection<? extends Student>) repository.findAll());
        Filter filter = new FilterImpl();
        Predicate<Student> predicate = (s) -> {
            return s.getNume().contains(nume);
        };
        result = new ArrayList<>(filter.defaultFilterAndSort(students, predicate, Student::comparaNumeCadruDidactic));
        return Optional.ofNullable(result);
    }

    public Optional<ArrayList<Student>> filtreazaStudentiiCuNumeleSiProf(String prof, String nume) {
        ArrayList<Student> result;
        List<Student> students = new ArrayList<Student>((Collection<? extends Student>) repository.findAll());
        Filter filter = new FilterImpl();
        Predicate<Student> predicate = (s) -> {
            return s.getNume().contains(nume) && s.getCadruDidacticIndrumator().equals(prof);
        };
        result = new ArrayList<>(filter.defaultFilterAndSort(students, predicate, Student::comparaNumeCadruDidactic));
        return Optional.ofNullable(result);
    }

    public Optional<ArrayList<Student>> filtreazaStudentiiCuNumeleSiGrupa(String grupa, String nume) {
        ArrayList<Student> result;
        List<Student> students = new ArrayList<Student>((Collection<? extends Student>) repository.findAll());
        Filter filter = new FilterImpl();
        Predicate<Student> predicate = (s) -> {
            return s.getNume().contains(nume) && s.getGrupa().equals(grupa);
        };
        result = new ArrayList<>(filter.defaultFilterAndSort(students, predicate, Student::comparaNumeCadruDidactic));
        return Optional.ofNullable(result);
    }

    public Optional<ArrayList<Student>> filtreazaStudentiiCuGrupaSiProf(String grupa, String prof) {
        ArrayList<Student> result;
        List<Student> students = new ArrayList<Student>((Collection<? extends Student>) repository.findAll());
        Filter filter = new FilterImpl();
        Predicate<Student> predicate = (s) -> {
            return s.getGrupa().equals(grupa) && s.getCadruDidacticIndrumator().equals(prof);
        };
        result = new ArrayList<>(filter.defaultFilterAndSort(students, predicate, Student::comparaNumeCadruDidactic));
        return Optional.ofNullable(result);
    }

    public Optional<ArrayList<Student>> filtreazaStudentiiCuToateFiltrele(String grupa, String prof, String nume) {
        ArrayList<Student> result;
        List<Student> students = new ArrayList<Student>((Collection<? extends Student>) repository.findAll());
        Filter filter = new FilterImpl();
        Predicate<Student> predicate = (s) -> {
            return s.getGrupa().equals(grupa) && s.getCadruDidacticIndrumator().equals(prof) && s.getNume().contains(nume);
        };
        result = new ArrayList<>(filter.defaultFilterAndSort(students, predicate, Student::comparaNumeCadruDidactic));
        return Optional.ofNullable(result);
    }

    public AbstractCrudRepo<Student, String> getRepository() {
        return repository;
    }

    public void setRepository(AbstractCrudRepo<Student, String> repository) {
        this.repository = repository;
    }

    public StudentValidator getValidator() {
        return validator;
    }

    public void setValidator(StudentValidator validator) {
        this.validator = validator;
    }

}
