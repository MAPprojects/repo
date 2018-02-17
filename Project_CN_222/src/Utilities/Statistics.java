package Utilities;

import Domain.*;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Services.NoteService;
import Services.StudentService;
import Services.TaskService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Statistics {

    private StudentService studentService;
    private NoteService gradeService;
    private TaskService taskService;

    public Statistics(StudentService studentService, NoteService gradeService, TaskService taskService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.taskService = taskService;
    }

    public List<StudentAverage> getAverageGrade() {
        List<StudentAverage> res = new ArrayList<>();
        for (Student student : studentService.getAllAsList()) {
            int sum = 0, count = 0;
            for (Task t : taskService.getAllAsList()) {
                try {
                    Nota grade = gradeService.findByStudentAndTask(student.getId(), t.getId());
                    sum += grade.getValoare();
                    count++;
                } catch (RepositoryException | ValidationException e) {

                }
            }
            if (count != 0) {
                res.add(new StudentAverage(student.getNume(),student.getEmail(), sum/count));
            } else {
                res.add(new StudentAverage(student.getNume(),student.getEmail(), 0));
            }

        }
        return res;
    }

    public List<Student> getStudentsThatCanTakeExam() {
        List<Student> res = new ArrayList<>();
        for (StudentAverage studentAverage : getAverageGrade()) {
            if (studentAverage.getAverage() >= 5.0) {
                Student student = studentService.findByEmail(studentAverage.getEmail());
                res.add(student);
            }
        }
        return res;
    }

    public List<TaskCountDTO> getBiggest3Tasks() {
        List<TaskCountDTO> res = new ArrayList<>();
        for (Task task : taskService.getAllAsList()) {
            int count = 0;
            for (Student student : studentService.getAllAsList()) {
                try {
                    gradeService.findByStudentAndTask(student.getId(), task.getId());
                    count++;
                } catch (RepositoryException e) {

                }
            }
            res.add(new TaskCountDTO(task.getId(), task.getDescriere(), count));
        }
        res.sort(new Comparator<TaskCountDTO>() {
            @Override
            public int compare(TaskCountDTO o1, TaskCountDTO o2) {
                return o2.getNrOfStudents() - o1.getNrOfStudents();
            }
        });
        return res.subList(0,Math.min(3,res.size()));
    }

}
