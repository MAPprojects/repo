package Service;

import Domain.GradeDTO;
import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Mail.GoogleMail;
import Repository.InterfaceRepository;
import Repository.ValidatorException;
import XMLRepository.HandleWriteToFile;
import javafx.scene.control.Alert;

import javax.mail.MessagingException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service extends Observable {

    private InterfaceRepository<Integer, Tema> temaRepository;
    private InterfaceRepository<Integer, Student> studentRepository;
    private InterfaceRepository<Integer, Nota> notaRepository;
    private int currentTemaId, currentNotaId, currentIdStudent;
    private HandleWriteToFile handleWriteToFile;

    private GoogleMail mail;

    private static int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

    public Service(InterfaceRepository<Integer, Tema> temaRepository, InterfaceRepository<Integer, Student> studentRepository, InterfaceRepository<Integer, Nota> notaRepository) {
        this.temaRepository = temaRepository;
        this.studentRepository = studentRepository;
        this.notaRepository = notaRepository;
        currentTemaId = numberOfTeme()+1;
        currentNotaId = numberOfNote()+1;
        currentIdStudent = numberOfStudenti()+1;

        handleWriteToFile = new HandleWriteToFile();
        mail = new GoogleMail();
    }

    public void sendEmail(Student student){
        try {
            mail.Send("ubblabapp", "ubblabapp2018", student.getEmail(), "Raport laborator", handleWriteToFile.getTextToEmail(student));
        } catch (MessagingException e) {
            infoBox("ERROR WHEN SENDING THE EMAIL", "", "", Alert.AlertType.INFORMATION);
        }
    }

    public void addStudent(String nume, int grupa, String email, String cadruDidactic){
        Student student = new Student(currentIdStudent, nume, grupa, email, cadruDidactic);
        studentRepository.save(student);
        currentIdStudent+=1;

        handleWriteToFile.writeStudent(student, "ADD");

        setChanged();
        notifyObservers();
    }

    public void removeStudent(int id){

        Student student = studentRepository.findOne(id);
        studentRepository.delete(id);
        removeGradesWithRemovedStudent(id);


        handleWriteToFile.deleteFile(student);

        setChanged();
        notifyObservers();
    }


    private void removeGradesWithRemovedHomework(int idHomework){
        for(Nota grade: getAllGrades()){
            if(grade.getIdTema() == idHomework)
                removeNota(grade.getId());
        }
    }

    private void removeGradesWithRemovedStudent(int idStudent){
        for(Nota grade: getAllGrades()){
            if(grade.getIdStudent() == idStudent)
                removeNota(grade.getId());
        }

    }

    public List<Nota> getAllNoteFromAHomework(Tema homework){
        List<Nota> outputNote = new LinkedList<>();
        for(Nota nota: getAllGrades()){
            if(nota.getIdTema() == homework.getId()){
                outputNote.add(nota);
            }
        }
        return outputNote;
    }

    public double averagePerHomework(Tema homework){
        int sum=0;
        List<Nota> listOfAllGradesForAHomework = getAllNoteFromAHomework(homework);

        if(listOfAllGradesForAHomework.size()<=0){
            return 0;
        }

        for(Nota nota: listOfAllGradesForAHomework){
            sum = sum+nota.getValoareNota();
        }

        return sum/listOfAllGradesForAHomework.size();

    }

    public double[] getTheCountedGradesForAllHomeworks(){
        double[] grades = new double[15];
        for(Tema homework: getAllTeme()){
            grades[homework.getId()] = averagePerHomework(homework);
        }

        return grades;
    }


    public List<Nota> getAllNoteFromAStudent(Student student){
        List<Nota> outputNote = new LinkedList<>();
        for(Nota nota: getAllGrades()){
            if(nota.getIdStudent() == student.getId()){
                outputNote.add(nota);
            }
        }
        return outputNote;
    }

    public int averagePerStudent(Student student){
        int sum=0;
        List<Nota> listOfAllGradesForAStudent = getAllNoteFromAStudent(student);

        if(listOfAllGradesForAStudent.size()<=0){
            return 0;
        }

        for(Nota nota: listOfAllGradesForAStudent){
            sum = sum+nota.getValoareNota();
        }

        return Math.round(sum/listOfAllGradesForAStudent.size());

    }

    public int[] getTheCountedGrades(){
        int[] grades = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for(Student student: getAllStudenti()){
            grades[averagePerStudent(student)]++;
        }

        return grades;
    }

    public void infoBox(String infoMessage, String titleBar, String headerMessage, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }

    public int getNotPromotedPercentage(){
        int[] grades = getTheCountedGrades();
        int numberOfStudents = numberOfStudenti();
        int sumOfNotPromotedStudents = grades[1] + grades[2] + grades[3] + grades[4];
        return 100*sumOfNotPromotedStudents/numberOfStudents;
    }

    public void updateStudent(int idStudent, String nume, int grupa, String email, String cadruDidactic){

        Student newStudent = new Student(idStudent, nume, grupa, email, cadruDidactic);
        studentRepository.update(newStudent);

        handleWriteToFile.writeStudent(newStudent, "UPDATE");
        handleWriteToFile.getTextToEmail(newStudent);

        setChanged();
        notifyObservers();

    }

    public void removeTema(int id){
        Tema tema = temaRepository.findOne(id);
        temaRepository.delete(id);
        removeGradesWithRemovedHomework(id);

        setChanged();
        notifyObservers();

    }

    public void addTema(String descriere, int deadline, String titlu){

        Tema tema = new Tema(currentTemaId, descriere, deadline, titlu);
        temaRepository.save(tema);
        currentTemaId+=1;

        setChanged();
        notifyObservers();

    }

    public void updateTema(int id, String descriere, int newDeadline, String titlu){

        Tema tema = temaRepository.findOne(id);
        int currentDeadline = tema.getDeadline();

        if(newDeadline<=currentDeadline){
            throw new ValidatorException("Deadline-ul nu poate sa fie mai mic decat cel actual: saptamana " + currentDeadline);
        }

        if(currentWeek+2>currentDeadline){
            throw new ValidatorException("Nu se poate schimba deadline-ul!Mai sunt doar 2 saptamani");
        }

        Tema newTema = new Tema(tema.getId(), tema.getDescriere(), newDeadline, titlu);

        temaRepository.update(newTema);

        setChanged();
        notifyObservers();


    }

    public void addNota(int valoareNota, int idStudent, int idTema, String observatii){

        Student student = studentRepository.findOne(idStudent);
        Tema tema = temaRepository.findOne(idTema);

        if(currentWeek>tema.getDeadline()) valoareNota-=2;

        if(currentWeek-tema.getDeadline()>=2)valoareNota=1;

        validareNota(student, tema);

        Nota nota = new Nota(currentNotaId, valoareNota, idStudent, idTema, tema.getDeadline(), tema.getTitlu(), currentWeek, observatii);

        notaRepository.save(nota);
        currentNotaId+=1;

        handleWriteToFile.writeStudent(nota, "GRADE");

        setChanged();
        notifyObservers();

    }

    public Nota getOneNota(int id){
        return  notaRepository.findOne(id);
    }

    public void removeNota(int id){

        Nota nota = notaRepository.findOne(id);
        notaRepository.delete(id);

        setChanged();
        notifyObservers();
    }

    public void updateNota(int idNota, int valoareNota, int idStudent, int idTema, int deadline, String titlu, int saptamanaPredarii, String observatii){

        Nota newNota = new Nota(idNota, valoareNota, idStudent, idTema, deadline, titlu, saptamanaPredarii, observatii);
        notaRepository.update(newNota);

        handleWriteToFile.writeStudent(newNota, "GRADE");

        setChanged();
        notifyObservers();


    }

    public Tema getOneTema(int id){
        return temaRepository.findOne(id);
    }

    private void validareNota(Student student, Tema tema){

        for(Nota nota : notaRepository.getAll()){
            if(nota.getIdStudent()==student.getId() && nota.getIdTema()==tema.getId())
                throw new ValidatorException("Deja acest sudent are nota la tema aceasta!");
        }

    }

    public  void modificaNota(int idNota, int valoareNota){

        Nota nota = notaRepository.findOne(idNota);
        Tema tema = temaRepository.findOne(nota.getIdTema());

        if(currentWeek-tema.getDeadline()>=2)valoareNota=1;
        if(currentWeek>tema.getDeadline()) valoareNota-=2;

        if(valoareNota>nota.getValoareNota()){
            Nota newNota = new Nota(nota.getId(), valoareNota, nota.getIdStudent(), nota.getIdTema(), nota.getDeadline(), nota.getTitlu(), currentWeek, nota.getObservatii());
            notaRepository.update(newNota);
        }

    }

    public Student getOneStudent(int id){

        return studentRepository.findOne(id);

    }

    private <E> List<E> fromIterableToList(Iterable<E> iterableList){
        List<E> newList=new ArrayList<>();
        for(E e:iterableList){
            newList.add(e);
        }
        return newList;
    }


    public List<Tema> filterHomeworks(String title, String deadline){
        Predicate<Tema> predicate = x -> {return true;};

        if (title.length()>0){
            Predicate<Tema> predicateTitle = x -> x.getTitlu().contains(title);
            predicate = predicate.and(predicateTitle);
        }

        if (deadline.length()>0){
            int deadlineInteger = Integer.parseInt(deadline);
            Predicate<Tema> predicateDeadline = x -> x.getDeadline()==deadlineInteger;
            predicate = predicate.and(predicateDeadline);
        }

        return genericFilter(fromIterableToList(getAllTeme()), predicate, CompareClass::CompareBetweenHomeworks);
    }

    public List<Nota> filterGrades(String title, String deadline){

        Predicate<Nota> predicate = x -> {return true;};

        if(title.length()>0){
            Predicate<Nota> predicateTitle = x -> x.getTitlu().contains(title);
            predicate = predicate.and(predicateTitle);
        }

        if (deadline.length()>0){
            int currentDeadline = Integer.parseInt(deadline);
            Predicate<Nota> predicateDeadline = x -> x.getDeadline()<= currentDeadline;
            predicate = predicate.and(predicateDeadline);
        }

        return genericFilter(fromIterableToList(getAllGrades()), predicate, CompareClass::CompareBetweenGrades);
    }


    public List<Student> filterStudents(String group, String name, String teacher){

        Predicate<Student> predicate = x -> {return true;};

        if(group.length()>0 && !group.equals("No Group")){
            int groupInt = Integer.parseInt(group);
            Predicate<Student> predicateGroup = x -> x.getGrupa() == groupInt;
            predicate = predicate.and(predicateGroup);
        }

        if (name.length()>0){
            Predicate<Student> predicateName = x -> x.getNume().contains(name);
            predicate = predicate.and(predicateName);
        }

        if (teacher.length()>0){
            Predicate<Student> predicateTeacher = x -> x.getCadruDidactic().contains(teacher);
            predicate = predicate.and(predicateTeacher);
        }

        return genericFilter(fromIterableToList(getAllStudenti()), predicate, CompareClass::CompareBetweenStudents);
    }


    private static class CompareClass {

        public static int CompareBetweenStudents(Student student, Student othStudent) {
            return student.getNume().compareTo(othStudent.getNume());

        }

        public static int CompareBetweenHomeworks(Tema tema, Tema othTema){
            return tema.getTitlu().compareTo(othTema.getTitlu());
        }

        public static int CompareBetweenGrades(Nota nota, Nota othNota){
            return nota.getTitlu().compareTo(othNota.getTitlu());
        }

        public static int CompareBetweenStudentsDesc(Student student, Student othStudent){
            return othStudent.getNume().compareTo(student.getNume());
        }

        public static int CompareTemeById(Tema tema, Tema othTema){
            if(tema.getId()<othTema.getId()){
                return  1;
            }
            else if(tema.getId() > othTema.getId()){
                return  -1;
            }
            return 0;
        }

         public static int CompareTemeByIdDesc(Tema tema, Tema othTema){
             if(tema.getId()<othTema.getId()){
                 return  1;
             }
             else if(tema.getId() > othTema.getId()){
                 return  -1;
             }
             return 0;
         }

         public static int CompareBetweenStudentsByGroup(Student student, Student othStudent){
            if(student.getGrupa()>othStudent.getGrupa()){
                return 1;
            }
            else if(student.getGrupa()<othStudent.getGrupa()){
                return -1;
            }
            return  0;
         }

        public static int CompareBetweenNotaByIdTema(GradeDTO nota, GradeDTO othNota){
             if(nota.getIdTema()>othNota.getIdTema()){
                 return 1;
             }
             else if(nota.getIdTema()<othNota.getIdTema()){
                 return -1;
             }
             return 0;
         }

         public static int CompareBetweenNotaByValoare(GradeDTO nota, GradeDTO othNota){
             if(nota.getValoareaNotei()>othNota.getValoareaNotei()){
                 return -1;
             }
             else if(nota.getValoareaNotei()<othNota.getValoareaNotei()){
                 return 1;
             }
             return 0;
         }


    }

    public List<Tema> filterTemaByDeadline(int deadline){
        return genericFilter(fromIterableToList(getAllTeme()), x->x.getDeadline()==deadline, CompareClass::CompareTemeById);
    }

    public List<Tema> filterTemaByLowerDeadline(int deadline){
        return genericFilter(fromIterableToList(getAllTeme()), x->x.getDeadline()<deadline, CompareClass::CompareTemeById);
    }

    public List<Tema> filterTemaByHigherrDeadline(int deadline){
        return genericFilter(fromIterableToList(getAllTeme()), x->x.getDeadline()>deadline, CompareClass::CompareTemeByIdDesc);
    }

    public List<GradeDTO> filterNotaBySaptamana(int saptamana){
        return genericFilter(fromIterableToList(getAllNote()), x->x.getSaptamanaPredarii() == saptamana, CompareClass::CompareBetweenNotaByIdTema);
    }

    public List<GradeDTO> filterNotaByTema(int idTema){
        Predicate<GradeDTO> predicate = x->x.getIdTema() == idTema;
        return genericFilter(fromIterableToList(getAllNote()), predicate, CompareClass::CompareBetweenNotaByValoare);
    }

    public List<GradeDTO> filterNotaByValoare(int valoare){
        Predicate<GradeDTO> predicate = x->x.getValoareaNotei() == valoare;
        return genericFilter(fromIterableToList(getAllNote()), predicate, CompareClass::CompareBetweenNotaByIdTema);
    }

    public List<Student> filterStudentsByGroup(int grupa){

        Iterable<Student> list = getAllStudenti();
        return genericFilter(fromIterableToList(list), x-> x.getGrupa()==grupa, CompareClass::CompareBetweenStudentsDesc);

    }

    public List<Student> filterStudentsByTeacher(String teacher){

        return genericFilter(fromIterableToList(getAllStudenti()), x-> x.getCadruDidactic().equals(teacher), CompareClass::CompareBetweenStudentsByGroup);

    }

    public List<Student> filterStudentsByLetter(String letter){

        Predicate<Student> predicate = x->x.getNume().indexOf(letter) == 0;
        Iterable<Student> list = getAllStudenti();
        return genericFilter(fromIterableToList(list), predicate, CompareClass::CompareBetweenStudents);

    }


    public  <E> List<E> genericFilter(List<E> list, Predicate<E> predicate, Comparator<E> comp){
        return list.stream().filter(predicate).sorted(comp).collect(Collectors.toList());
    }





    public int numberOfTeme(){
        return temaRepository.size();
    }

    public int numberOfStudenti(){
        return studentRepository.size();
    }

    public int numberOfNote(){return notaRepository.size();}

    public Iterable<Student> getAllStudenti(){
        return studentRepository.getAll();
    }

    public Iterable<Nota> getAllGrades(){return notaRepository.getAll();}

    public Iterable<GradeDTO> getAllNote(){

        List<GradeDTO> lista = new ArrayList<GradeDTO>();
        for(Nota nota: notaRepository.getAll()){

            Student student = studentRepository.findOne(nota.getIdStudent());

            GradeDTO grade = new GradeDTO(nota.getId(), nota.getValoareNota(), student.getNume(), nota.getIdTema(), nota.getTitlu(), nota.getDeadline(), nota.getSaptamanaPredarii(), nota.getObservatii());
            lista.add(grade);
        }
        return lista;
    }

    public Iterable<Tema> getAllTeme(){
        return temaRepository.getAll();
    }

}
