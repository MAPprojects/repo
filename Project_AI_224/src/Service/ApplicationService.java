package Service;

import Domain.Grade;
import Domain.Project;
import Domain.Student;
import Domain.TableProject;
import Statistics.Filter;

import java.util.*;
import java.util.function.Predicate;

public class ApplicationService{
    public StudentService studentService;
    public ProjectService projectService;
    public GradeService gradeService;
    public LoginService loginService;

    public boolean isStudent;
    public Student student;
    public String email;

    public ApplicationService(StudentService studentService, ProjectService projectService,
                              GradeService gradeService, LoginService loginService) {
        this.studentService = studentService;
        this.projectService = projectService;
        this.gradeService = gradeService;
        this.loginService = loginService;
    }

//    private static HashMap<String, String> getEntitiesLoggedIn(String filename){
//        HashMap<String, String> students = new HashMap<>();
//        Path path = Paths.get(filename);
//        Stream<String> lines;
//
//        try {
//            lines = Files.lines(path);
//            lines.forEach(line -> {
//                if (line.compareTo("")!=0) {
//                    String[] fields = line.split(";");
//                    if(fields.length==2){
//                        students.put(fields[0], fields[1]);
//                    }
//                }
//            });
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//        return students;
//    }
//
    public boolean isLoggedIn(String email, String password){
        return this.loginService.get(email, "StudentsLogin").compareTo(password) == 0 || this.loginService.get(email, "TeachersLogin").compareTo(password)==0;
    }

    public boolean isStudent(String email){
        return this.loginService.get(email, "StudentsLogin").compareTo("") != 0;
    }

    public boolean isTeacher(String email){
        return this.loginService.get(email, "TeachersLogin").compareTo("") != 0;
    }

    public boolean createStudentAccount(String email, String password) {
        return studentService.getStudentByEmail(email) != null && loginService.get(email, "StudentsLogin").compareTo("")==0 && loginService.save(email, password, "StudentsLogin");
    }

    public void deleteStudent(UUID id, String email){
        this.loginService.delete(email, "StudentsLogin");
        this.gradeService.deleteGradesByID(id);
        this.studentService.deleteStudent(id);
    }

    public void deleteProject(UUID id){
        this.gradeService.deleteGradesByID(id);
        this.projectService.deleteProject(id);
    }

    public List<Student> getInTimeStudents(){
        List<Student> inTimeStudents = new ArrayList<>();
        HashMap<UUID, List<Grade>> allStudentsGrades = gradeService.createGradesHashMap();
        List<Student> allStudents = this.studentService.getAllStudents();
        for(Student st:allStudents){
            List<Grade> crtSudentGrades = allStudentsGrades.get(st.getID());
            if(crtSudentGrades.size()!=0) {
                Predicate<Grade> p = x -> !x.isDelay();
                List<Grade> delayedGrades = Filter.filter(crtSudentGrades, p);
                if (delayedGrades.size() == crtSudentGrades.size())
                    inTimeStudents.add(st);
            }
        }
        return inTimeStudents;
    }

    public HashMap<Student, Integer> getPassedStudents(){
        List<Grade> allGrades = this.gradeService.getAll();
        List<Student> allStudents  = this.studentService.getAllStudents();

        HashMap<Student, Integer> passedStudents = new HashMap<>();

        HashMap<UUID, List<Grade>> allStudentsGrades = gradeService.createGradesHashMap();

        for(Student student : allStudents){
            int sum = 0;
            List<Grade> crtStudentGrades = allStudentsGrades.get(student.getID());
            for(Grade grade : crtStudentGrades){
                sum+=grade.getGrade();
            }
            if(crtStudentGrades.size()!=0){
                Integer avg = Math.round(sum / crtStudentGrades.size());
                if(avg>=5){
                    student.setFinalGrade(avg);
                    passedStudents.put(student, avg);
                }
            }
        }
        return passedStudents;
    }

    private HashMap<Project, Integer> getDelayedProjects(){
        HashMap<Project, Integer> projects = new HashMap<>();
        List<Project> allProjects = this.projectService.getAllProjects();
        List<Grade> allGrades =this.gradeService.getAll();
        for(Project pr : allProjects){
            projects.put(pr, 0);
        }
        for(Grade gr : allGrades){
            if(gr.isDelay()){
                projects.put(gr.getProject(), projects.get(gr.getProject())+1);
            }
        }
        return projects;
    }

    public HashMap<TableProject, Integer> getHardestProjects(){
        HashMap<Project, Integer> delayed = getDelayedProjects();
        int maxDelayed = Collections.max(delayed.values());
        HashMap<TableProject, Integer> hardestProjects = new HashMap<>();
        if(delayed.size()==0 || maxDelayed == 0){
            return hardestProjects;
        }
        for(Project pr : this.projectService.getAllProjects()){
            if(delayed.get(pr)==maxDelayed){
                hardestProjects.put(new TableProject(pr.getDescription(), pr.getDeadline(), delayed.get(pr)), maxDelayed);
            }
        }
        return hardestProjects;
    }
}
