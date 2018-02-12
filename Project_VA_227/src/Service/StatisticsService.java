package Service;

import Domain.Homework;
import Domain.Note;
import Domain.Student;
import Utils.Observable;
import Utils.Observer;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public class StatisticsService implements Observable {
    private List<Observer> observers;
    private static StudentService studentService;
    private static HomeworkService homeworkService;
    private static NoteService noteService;

    public StatisticsService(StudentService studentService, HomeworkService homeworkService, NoteService noteService) {
        StatisticsService.studentService = studentService;
        StatisticsService.homeworkService = homeworkService;
        StatisticsService.noteService = noteService;
        observers = new ArrayList<>();
        compute();
    }

    private static void compute() {

    }

    private Map<String, Integer> grades = null;

    private void addToCategory(String category) {
        if (grades.containsKey(category))
            grades.put(category, grades.get(category) + 1);
        else
            grades.put(category, 1);
    }

    public Map<String, Integer> media() {
        grades = new HashMap<>();
        for (Integer id : studentService.getAll()) {
            List<Note> marks = noteService.filterStudent(id);
            if (marks.size() == 0)
                addToCategory("< 5");
            else {
                double avg = marks.stream().mapToDouble(x -> x.getValue() * 1.).sum() / (marks.size() * 1.);
                if (avg < 5.)
                    addToCategory("< 5");
                else if (avg < 6.)
                    addToCategory("5 - 5.99");
                else if (avg < 7.0)
                    addToCategory("6 - 6.99");
                else if (avg < 8.0)
                    addToCategory("7 - 7.99");
                else if (avg < 9.0)
                    addToCategory("8 - 8.99");
                else if (avg < 10.0)
                    addToCategory("9 - 9.99");
                else
                    addToCategory("10");
            }
        }
        return grades;
    }

    private Map<Homework, Integer> homeworks = null;

    private void addToHomework(Homework homework) {
        if (homeworks.containsKey(homework))
            homeworks.put(homework, homeworks.get(homework) + 1);
        else
            homeworks.put(homework, 1);
    }

    public Map<Homework, Integer> homeworks() {
        homeworks = new TreeMap<>(Comparator.comparingInt(Homework::getId));
        StreamSupport.stream(noteService.getAll().spliterator(), false).map(noteService::find).forEach(
                (note) -> {
                    if (note.getSapt_predare() > noteService.getHomeworkService().find(note.getHomeworkID()).getDeadline())
                        addToHomework(homeworkService.find(note.getHomeworkID()));
                }
        );
        return homeworks;
    }

    public static StudentService getStudentService() {
        return studentService;
    }

    public static void setStudentService(StudentService studentService) {
        StatisticsService.studentService = studentService;
        compute();
    }

    public static HomeworkService getHomeworkService() {
        return homeworkService;
    }

    public static void setHomeworkService(HomeworkService homeworkService) {
        StatisticsService.homeworkService = homeworkService;
        compute();
    }

    public static NoteService getNoteService() {
        return noteService;
    }

    public static void setNoteService(NoteService noteService) {
        StatisticsService.noteService = noteService;
        compute();
    }


    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(x -> x.notifyOnEvent(this));
    }

    public List<Student> studentiFaraPenalitati() {
        List<Student> students = new ArrayList<>();
        StreamSupport.stream(studentService.getAll().spliterator(), false).map(studentService::find).forEach(
                student -> {
                    AtomicBoolean ok = new AtomicBoolean(true);
                    noteService.filterStudent(student.getId()).forEach(note -> {
                        if (note.getSapt_predare() > note.getDeadline())
                            ok.set(false);
                    });
                    if (ok.get())
                        students.add(student);
                }
        );
        return students;
    }

    public List<Student> studentiEligibili() {
        List<Student> students = new ArrayList<>();
        StreamSupport.stream(studentService.getAll().spliterator(), false).map(studentService::find).forEach(
                (student) -> {
                    AtomicInteger value = new AtomicInteger();
                    AtomicInteger nr = new AtomicInteger();
                    noteService.filterStudent(student.getId()).forEach((note) -> {
                        value.addAndGet(note.getValue());
                        nr.getAndIncrement();
                    });
                    if (nr.get() > 0 && value.get() * 1. / nr.get() * 1. >= 4.)
                        students.add(student);
                }
        );
        return students;
    }
}
