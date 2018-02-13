package com.company;


import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repository.AbstractRepo;
import Repository.FileRepo;
import Repository.Repository;
import Services.NotaService;
import Services.StatisticsService;
import Services.StudentService;
import Services.TemeService;
import Utils.Notifier;
import Utils.RepoLoader;
import Utils.StudentFileMover;
import Validators.StudentValidator;
import Validators.TemeValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Main  {

    private static  void start() throws  Exception{


        AbstractRepo<Student> studentAbstractRepo = new FileRepo<Student>("Students.txt");
        AbstractRepo <Tema> temaAbstractRepo = new FileRepo<Tema>("Teme.txt");
        AbstractRepo <Nota>  notaAbstractRepo = new FileRepo<Nota>("Catalog.txt");

        StatisticsService statisticsService = new StatisticsService(studentAbstractRepo,temaAbstractRepo,notaAbstractRepo);

        StudentService studentService = new StudentService(studentAbstractRepo,new StudentValidator());

        Notifier notifier = new Notifier(studentAbstractRepo);

        TemeService temeService = new TemeService(temaAbstractRepo,new TemeValidator(),notifier);
        NotaService notaService = new NotaService(notaAbstractRepo,studentService,temeService,notifier);

    }

    private void createDocument() throws  Exception{
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        Element rootElement = document.createElement("Repositories");

        rootElement.setAttribute("type","SQL");
        document.appendChild(rootElement);


        Element repositoryStudent = document.createElement("Student_Repository");

        repositoryStudent.setAttribute("repoName","MSSQLStudentRepository");
        repositoryStudent.setAttribute("fileName","null");
        repositoryStudent.setAttribute("package","Repository.");

        rootElement.appendChild(repositoryStudent);


        repositoryStudent = document.createElement("Homework_Repository");

        repositoryStudent.setAttribute("repoName","MSSQLTemaRepository");
        repositoryStudent.setAttribute("fileName","null");
        repositoryStudent.setAttribute("package","Repository.");

        rootElement.appendChild(repositoryStudent);


        repositoryStudent = document.createElement("Marks_Repository");

        repositoryStudent.setAttribute("repoName","MSSQLNotaRepository");
        repositoryStudent.setAttribute("fileName","null");
        repositoryStudent.setAttribute("package","Repository.");

        rootElement.appendChild(repositoryStudent);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");


        DOMSource source = new DOMSource(document);

        StreamResult result = new StreamResult(new File("C:\\Users\\Onu Edy\\Desktop\\225_Onu_Eduard_Gabriel\\src\\Config\\config.xml") );


        transformer.transform(source,result);
    }

    public static void main(String[] args)  throws  Exception{

         RepoLoader loader = new RepoLoader();

         loader.setRepoLocation("Repository.");

        Repository <Student> studentRepository = loader.getRepoStudent("MSSQLStudentRepository");

        StudentFileMover studentFileMover = new StudentFileMover();


        studentFileMover.setCurrentLocation("C:\\Users\\Onu Edy\\Desktop\\225_Onu_Eduard_Gabriel");

        studentFileMover.setRepository(studentRepository);


        studentFileMover.setDestination("C:\\Users\\Onu Edy\\Desktop\\225_Onu_Eduard_Gabriel\\src\\Config\\DDD");

        studentFileMover.moveAll();

    }


}
