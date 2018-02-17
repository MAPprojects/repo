package Service;

import Domain.Student;
import Repository.DBAbstractRepository;
import Statistics.Filter;
import Statistics.FilterAndSorter;
import Statistics.Sorter;
import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observable;
import Utils.Observer;
import Validate.ValidationException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static Utils.Utils.createEvent;

public class StudentService extends AbstractService<Student, UUID> implements Observable<Student> {
    private ArrayList<Observer<Student>> studentsObservers=new ArrayList<>();
    //private DBAbstractRepository<UUID, Student> repository;
    /**
     * StudentService constructor
     */
    public StudentService(DBAbstractRepository<UUID, Student> repository) {
        super(repository);
    }

    public Optional<Student> saveStudent(Student student) throws FileNotFoundException, UnsupportedEncodingException, ValidationException {
        Optional<Student> result = repository.save(student);
        if(!result.isPresent()){
            ListEvent<Student> listEvent = createEvent(ListEventType.ADD, student, this.repository.getAll());
            notifyObservers(listEvent);
        }
        return result;
    }

    /**
     * saves new student
     * @param ID
     * @param name
     * @param group
     * @param email
     * @param teacher
     * @return
     * @throws ValidationException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public Optional<Student> saveStudent(String ID, String name, String group, String email, String teacher) throws ValidationException, FileNotFoundException, UnsupportedEncodingException {
        if(group == null || group.isEmpty() || ID.isEmpty() || name.isEmpty() || email.isEmpty() || teacher.isEmpty()){
            throw new ValidationException("students attributes must not be empty");
        }
        Student entity = new Student(ID, name, Integer.parseInt(group), email, teacher);
        Optional<Student> result = repository.save(entity);
        if(!result.isPresent()){
            ListEvent<Student> listEvent = createEvent(ListEventType.ADD, entity, this.repository.getAll());
            notifyObservers(listEvent);
            //createNewStudentFile(entity.getID().toString());
        }
        return result;
    }

    /**
     * updates existing student
     * @param ID
     * @param name
     * @param group
     * @param email
     * @param teacher
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public Optional<Student> updateStudent(UUID uuid, String ID, String name, String group, String email, String teacher) throws FileNotFoundException, UnsupportedEncodingException, ValidationException {
        Optional<Student> oldStudent = repository.getEntity(uuid);
        if(oldStudent.isPresent()){
            if(!ID.isEmpty()){
                oldStudent.get().setCodMatricol(ID);
            }
            if(!name.isEmpty()){
                oldStudent.get().setName(name);
            }
            if(group!=null && !group.isEmpty()){
                oldStudent.get().setGroup(Integer.parseInt(group));
            }
            if(!email.isEmpty()){
                oldStudent.get().setEmail(email);
            }
            if(!teacher.isEmpty()){
                oldStudent.get().setTeacher(teacher);
            }
            Optional<Student> result = repository.update(oldStudent.get());
            if(result.isPresent()){
                ListEvent<Student> listEvent = createEvent(ListEventType.UPDATE, result.get(), this.repository.getAll());
                notifyObservers(listEvent);
            }
        }
        return oldStudent;
    }

    public Optional<Student> updateStudent(Student oldStudent){
        try {
            Optional<Student> result = repository.update(oldStudent);
            if(result.isPresent()){
                ListEvent<Student> listEvent = createEvent(ListEventType.UPDATE, result.get(), this.repository.getAll());
                notifyObservers(listEvent);
            }
            return result;
        } catch (FileNotFoundException | ValidationException | UnsupportedEncodingException e) {
            return Optional.empty();
        }
    }

    /**
     * gets student by id
     * @param id student's id
     * @return student with given id
     */
    public Optional<Student> getStudent(UUID id) {
        return repository.getEntity(id);
    }

    /**
     * deletes an existing student
     * @param id student id
     * @return Student - deleted student
     */
    public Optional<Student> deleteStudent(UUID id) {
        try {
            Optional<Student> result = repository.delete(id);
            if(result.isPresent()){
                ListEvent<Student> listEvent = createEvent(ListEventType.REMOVE, result.get(), this.repository.getAll());
                notifyObservers(listEvent);
            }
            return result;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * gets all students
     * @return Iterable<Student> all students values
     */
    public List<Student> getAllStudents(){
        return StreamSupport.stream(this.repository.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * gets number of students
     * @return long number of students
     */
    public long studentsNumber(){
        return repository.size();
    }

    /**
     * sorts students by name
     * @return list of students sorted by name
     */
    public List<Student> sortStudentsByName(){
        Comparator<Student> studentComparator = (s1, s2)->s1.getName().compareTo(s2.getName());
        return StreamSupport.stream(Sorter.sort(this.getAll(), studentComparator).spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * sorts students by group
     * @return list of students sorted by group
     */
    public List<Student> sortStudentsByGroup(){
        Comparator<Student> studentComparator = (s1, s2)->s1.getGroup()-s2.getGroup();
        return StreamSupport.stream(Sorter.sort(this.getAll(), studentComparator).spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Student> sortStudentsByNameAndGroup(){
        Comparator<Student> studentComparator = (s1, s2)->s1.getGroup()-s2.getGroup();
        return StreamSupport.stream(Sorter.sort(this.sortStudentsByName(), studentComparator).spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * filters students by given group alphabetically
     * @param group
     * @return
     */
    public List<Student> filterByGroup(int group){
        Predicate<Student> p = x->x.getGroup()==group;
        Comparator<Student> c = (x, y)->x.getName().compareTo(y.getName());
        return FilterAndSorter.filterAndSort(this.getAll(), p, c);
    }

    public List<Student> filterByName(String name){
        Predicate<Student> p = x->x.getName().compareTo(name)==0;
        return Filter.filter(this.getAll(), p);
    }

    public List<Student> filterByCod(String cod){
        Predicate<Student> p = x->x.getCodMatricol().compareTo(cod)==0;
        return Filter.filter(this.getAll(), p);
    }


    public List<Student> filterByEmail(String email){
        Predicate<Student> p = x->x.getEmail().compareTo(email)==0;
        return Filter.filter(this.getAll(), p);
    }

    public List<Student> filterByTeacher(String teacher){
        Predicate<Student> p = x->x.getTeacher().compareTo(teacher)==0;
        return Filter.filter(this.getAll(), p);
    }

    public Student getStudentByEmail(String email){
        List<Student> students = this.getAllStudents();
        for (Student s: students
             ) {
            if(s.getEmail().compareTo(email)==0){
                return s;
            }
        }
        return null;
    }

    public List<Student> getStudentsPage(int pageNumber){
        return StreamSupport.stream(repository.getPage(pageNumber).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(Observer<Student> observer) {
        studentsObservers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Student> observer) {
        studentsObservers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<Student> event) {
        studentsObservers.forEach(o->o.update(event));
    }


}
