package Service;

import Domain.Assignment;
import Domain.Grade;
import Domain.Student;
import Repository.RepositoryException;
import Utils.CurrentWeek;
import Utils.FilterAndSort;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralService {
    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeService gradeService;

    public GeneralService(StudentService studentService, AssignmentService assignmentService, GradeService gradeService) {
        this.studentService = studentService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
    }

    public List<Student> getAllContains(String string) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                studentService.getAllStudents(),
                student -> student.getName().toLowerCase().contains(string.toLowerCase()),
                Comparator.comparing(Student::getName)
        );
    }

    public List<Student> getAllStdGroup(int group) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                studentService.getAllStudents(),
                student -> student.getGroup()==group,
                Comparator.comparing(Student::getName));
    }

    public Map<Integer,Integer> getFinalGrades() throws Exception {
        Map<Integer,Integer> gradeAvg = new HashMap<>();
        int k=0;
        int sum=0;
        List<Student> students = studentService.getAllStudents();
        List<Grade> grades = gradeService.getAll();
        for( Student student : students) {
            k=0;
            sum=0;
            for(Grade grade : grades) {
                if (grade.getStd().getIdStudent() == student.getIdStudent()) {
                    sum+=grade.getValue();
                    k++;
                }
            }
            if(k!=0)
                gradeAvg.put(student.getIdStudent(),sum/k);
            else
                gradeAvg.put(student.getIdStudent(),0);
        }
        return gradeAvg;
    }


    public List<Student> getAllStdAvg() throws Exception {
        Map<Integer,Integer> gradeAvg = getFinalGrades();
        return FilterAndSort.filterAndSorter(
                studentService.getAllStudents(),
                student -> gradeAvg.get(student.getIdStudent())>5,
                (std1,std2)-> gradeAvg.get(std1.getIdStudent()).compareTo(gradeAvg.get(std2.getIdStudent()))

        );
    }

    public List<Student> getAllStdProf(String professor) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                studentService.getAllStudents(),
                student -> student.getProfessor().equals(professor),
                Comparator.comparing(Student::getName)
        );
    }

    public List<Student> getAllStdName(String name) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                studentService.getAllStudents(),
                student -> student.getName().toLowerCase().contains(name.toLowerCase()),
                Comparator.comparing(Student::getName)
        );
    }


    public  List<Assignment> getAllAsgDesc(String desc) throws RepositoryException {
        return  FilterAndSort.filterAndSorter(
                assignmentService.getAllAssignments(),
                assignment -> assignment.getDescription().toLowerCase().contains(desc.toLowerCase()),
                Comparator.comparing(Assignment::getDeadline)
        );
    }

    public List<Assignment> getAllAsgToDoDesc(String desc) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                assignmentService.getAllAssignments(),
                assignment -> assignment.getDeadline()> CurrentWeek.getCurrentWeek() && assignment.getDescription().equals(desc),
                Comparator.comparing(Assignment::getDeadline)
        );
    }

    public List<Assignment> getAllAsgNextWeek() throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                assignmentService.getAllAssignments(),
                assignment -> assignment.getDeadline()== CurrentWeek.getCurrentWeek()+1,
                Comparator.comparing(Assignment::getDeadline)
        );
    }

    public List<Assignment> getAllAsgPast() throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                assignmentService.getAllAssignments(),
                assignment -> assignment.getDeadline()< CurrentWeek.getCurrentWeek(),
                Comparator.comparing(Assignment::getDeadline)
        );
    }

    public List<Assignment> getAllAsgTODO() throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                assignmentService.getAllAssignments(),
                assignment -> assignment.getDeadline()>= CurrentWeek.getCurrentWeek(),
                Comparator.comparing(Assignment::getDeadline)
        );
    }

    public List<Grade> getAllGrdStd(int IDstudent) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                gradeService.getAll(),
                grade -> grade.getStd().getIdStudent() == IDstudent,
                Comparator.comparing(Grade::getValue)
        );
    }
    public List<Grade> getAllGrdStd(String name) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                gradeService.getAll(),
                grade -> grade.getStd().getName().toLowerCase().contains(name.toLowerCase()),
                Comparator.comparing(Grade::getValue)
        );
    }

    public List<Grade> getAllGrdAsgDescLastWeek(String desc) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                gradeService.getAll(),
                grade -> grade.getAsg().getDescription().equals(desc) && grade.getAsg().getDeadline()== CurrentWeek.getCurrentWeek()-1,
                Comparator.comparing(Grade::getValue)
        );
    }


    public List<Grade> getAllGrdAsgDesc(String desc) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                gradeService.getAll(),
                grade -> grade.getAsg().getDescription().toLowerCase().contains(desc.toLowerCase()),
                Comparator.comparing(Grade::getValue)
        );
    }

    public List<Grade> getAllGrdGroup(int group) throws RepositoryException {
        return FilterAndSort.filterAndSorter(
                gradeService.getAll(),
                grade -> grade.getStd().getGroup()==group,
                Comparator.comparing(Grade::getValue)
        );
    }

}
