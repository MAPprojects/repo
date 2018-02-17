package Service;

import Domain.Grade;
import Domain.Project;
import Domain.Student;
import Repository.DBAbstractRepository;
import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observable;
import Utils.Observer;
import Validate.ValidationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static Utils.Utils.createEvent;

public class GradeService extends AbstractService<Grade, UUID> implements Observable<Grade> {

    private DBAbstractRepository<UUID, Student> studentRepository;
    private DBAbstractRepository<UUID, Project> projectRepository;

    private ArrayList<Observer<Grade>> observers = new ArrayList<>();

    public GradeService(DBAbstractRepository<UUID, Student> studentRepository, DBAbstractRepository<UUID, Project> projectRepository, DBAbstractRepository<UUID, Grade> gradeRepository) throws IOException {
        super(gradeRepository);
        this.studentRepository = studentRepository;
        this.projectRepository = projectRepository;
        //this.createStudentsFiles();
    }

    public List<Grade> getAllGradesForTable(){
        List<Grade> grades = new ArrayList<>();
        for (Grade grade: repository.getAll()) {
            grades.add(new Grade(grade.getID(), grade.getStudent(), grade.getProject(), grade.getGrade(), grade.getWeek()));
        }
        return StreamSupport.stream(grades.spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * creates and save a Grade for specific Student and Project
     * @param student student
     * @param project project
     * @throws ValidationException if student or project are not valid entities
     */
    public Optional<Grade> saveGrade(Student student, Project project) throws IOException, ValidationException {
        Grade newGrade = new Grade(UUID.randomUUID(), student, project, 0, 0, false);
        Optional<Grade> toSave = repository.save(newGrade);
        ListEvent<Grade> listEvent = createEvent(ListEventType.ADD, newGrade, this.repository.getAll());
        notifyObservers(listEvent);
        writeUpdatedGrade(newGrade, "Grade added ");
        return toSave;
    }

    /**
     * updates an existing grade
     * @param grade grade
     * @param week project week
     * @return updated grade
     */
    public Optional<Grade> updateGrade(UUID gradeID, String week, String grade, String action) throws IOException, ValidationException {
        Optional<Grade> toUpdate = repository.getEntity(gradeID);
        if(!toUpdate.isPresent()){
            throw new ValidationException("grade does not exist!");
        }
        if(week.isEmpty()){
            toUpdate.get().setGrade(Integer.parseInt(grade), toUpdate.get().getWeek());
        }
        else{
            toUpdate.get().setGrade(Integer.parseInt(grade), Integer.parseInt(week));
        }
        repository.update(toUpdate.get());
        writeUpdatedGrade(toUpdate.get(), action);
        ListEvent<Grade> listEvent = createEvent(ListEventType.UPDATE, toUpdate.get(), this.repository.getAll());
        notifyObservers(listEvent);
        return repository.update(toUpdate.get());
    }

    public Optional<Grade> getGrade(UUID id){
        return repository.getEntity(id);
    }

    /**
     * writes updated grade to student file
     * @param grade updated grade
     * @param action update or add
     * @throws IOException if file could not be written
     */
    private void writeUpdatedGrade(Grade grade, String action) throws IOException {
        FileWriter fileWriter = new FileWriter(grade.getStudent().getID()+".txt",true);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(action + grade.getProject().getID() + " " + grade.getGrade() + " " + grade.getProject().getDeadline() + " " + grade.getWeek());
        bw.newLine();
        bw.close();
    }

    private List<Grade> loadData(String fileName, String idStudent) {
        List<Grade> grades = new ArrayList<>();
        Path path = Paths.get(fileName);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(line -> {
                if (line.compareTo("")!=0) {
                    String[] fields = line.split(" ");
                    if(fields.length==6){
                        grades.add(new Grade(this.studentRepository.getEntity(UUID.fromString(idStudent)).get(), this.projectRepository.getEntity(UUID.fromString(fields[2])).get(), Integer.parseInt(fields[3]), Integer.parseInt(fields[5])));
                    }
                }
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return grades;
    }

    private List<UUID> getAllStudentsIds(){
        List<UUID> ids = new ArrayList<>();
        List<Student> students = StreamSupport.stream(this.studentRepository.getAll().spliterator(), false)
                .collect(Collectors.toList());
        for(Student st : students){
            ids.add(st.getID());
        }
        return ids;
    }

    public List<Grade> getStudentsGrades(UUID idStudent){
        List<Grade> studentGrades = new ArrayList<>();
        List<Grade> allGrades =this.getAll();
        if(allGrades.size()==0){
            return studentGrades;
        }
        for(Grade grade:allGrades){
            if(grade.getStudent().getID().compareTo(idStudent)==0){
                studentGrades.add(grade);
            }
        }
        return StreamSupport.stream(studentGrades.spliterator(), false)
                .collect(Collectors.toList());
    }

    public HashMap<UUID, List<Grade>> createGradesHashMap(){
        HashMap<UUID, List<Grade>> grades = new HashMap<>();
        List<UUID> ids=  this.getAllStudentsIds();
        for(UUID id:ids){
            grades.put(id, this.getStudentsGrades(id));
        }
        return grades;
    }


    public void deleteGradesByID(UUID ID) {
        List<Grade> grades = this.getAll();
        for (Grade grade:grades) {
            if(grade.getStudent().getID().equals(ID)){
                try {
                    this.repository.delete(grade.getID());
                    Files.delete(Paths.get(ID + ".txt"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                if(grade.getProject().getID().equals(ID)){
                    try {
                        //this.deleteGradeFromFile(grade.getID(), grade.getStudent().getID());
                        this.repository.delete(grade.getID());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void deleteGradeFromFile(UUID gradeID, UUID studentID) throws IOException {
        File inputFile = new File(studentID + ".txt");
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = gradeID.toString();
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            if(currentLine.contains(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
    }



    private void createStudentsFiles() throws IOException {
        HashMap<UUID, List<Grade>> studentsGrades = this.createGradesHashMap();
        List<UUID> studentsIds = this.getAllStudentsIds();
        for(UUID id:studentsIds){
            List<Grade> memoryGrades = studentsGrades.get(id);
            if(memoryGrades.size()>0) {
                if (studentsGrades.containsKey(id)) {
                    File f = new File(id + ".txt");
                    if (f.exists()) {
                        ArrayList<Grade> grades = (ArrayList<Grade>) this.loadData(id + ".txt", id.toString());
                        FileWriter fileWriter = new FileWriter(id + ".txt", true);
                        BufferedWriter bw = new BufferedWriter(fileWriter);
                        for (Grade grade : memoryGrades) {
                            if (!grades.contains(grade)) {
                                bw.write("Grade added " + grade.getProject().getID() + " " + grade.getGrade() + " " + grade.getProject().getDeadline() + " " + grade.getWeek());
                                bw.newLine();
                            }
                        }
                        bw.close();
                    }
                    else{
                        FileWriter fileWritter = new FileWriter(id + ".txt", true);
                        BufferedWriter bw = new BufferedWriter(fileWritter);
                        for (Grade grade : memoryGrades) {
                            bw.write("Grade added " + grade.getProject().getID() + " " + grade.getGrade() + " " + grade.getProject().getDeadline() + " " + grade.getWeek());
                            bw.newLine();
                        }
                        bw.close();
                    }

                }
            }
        }
    }

    public List<Grade> getGradesPage(int pageNumber){
        return StreamSupport.stream(repository.getPage(pageNumber).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(Observer<Grade> observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Grade> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<Grade> event) {
        observers.forEach(o->o.update(event));
    }
}
