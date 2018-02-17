//package com.company;
//
//import Domain.Grade;
//import Domain.Project;
//import Domain.Student;
//import Repository.FileRepository;
//import Repository.GradeFileRepository;
//import Repository.ProjectFileRepository;
//import Repository.StudentFileRepository;
//import Service.ApplicationService;
//import Service.GradeService;
//import Service.ProjectService;
//import Service.StudentService;
//import Validate.ValidationException;
//
//import java.io.IOException;
//
//public class Main {
//    public static void main(String[] args) {
//        try {
//            //Repositories initialisation
//            FileRepository<String, Student> studentsRepo = new StudentFileRepository("students.txt");
//            FileRepository<String, Project> projectsRepo = new ProjectFileRepository("projects.txt");
//            FileRepository<String, Grade> gradesRepo = new GradeFileRepository("grades.txt", studentsRepo, projectsRepo);
//
//            //Services initialisation
//            StudentService studentService = new StudentService(studentsRepo);
//            ProjectService projectService = new ProjectService(projectsRepo);
//            GradeService gradeService = new GradeService(studentsRepo, projectsRepo,gradesRepo);
//            ApplicationService applicationService = new ApplicationService(studentService, projectService, gradeService);
//
//            //UI initialisation
//            UI.UI ui = new UI.UI(applicationService);
//            ui.runApplication();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ValidationException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}
