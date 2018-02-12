package com.company;

import com.company.Domain.Globals;
import com.company.Exceptions.RepositoryException;
import com.company.Exceptions.ServiceException;
import com.company.Repositories.*;
import com.company.Service.Service;
import com.company.UserInterface.UI;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {



	    //StudentRepository stRepo = new StudentRepository();
        //TemaRepository tmRepo = new TemaRepository();
        //NotaRepository ntRepo = new NotaRepository();

        StudentRepository stRepo = null;
        try {
            stRepo = new FileStudentRepository("data\\Studenti.txt");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }

        TemaRepository tmRepo = null;
        try {
            tmRepo = new FileTemaRepository("data\\Teme.txt");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }

        NotaRepository ntRepo = null;
        try {
            ntRepo = new FileNotaRepository("data\\Note.txt");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }

        Service service = new Service(stRepo,tmRepo,ntRepo);

        UI ui = new UI(service);



//        try {
//            System.out.println(stRepo.findEntity(1));
//        } catch (RepositoryException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            System.out.println(tmRepo.findEntity(2));
//        } catch (RepositoryException e) {
//            e.printStackTrace();
//        }
//
//        service.modificareTermen(2,3);
//        service.modificareTermen(3,4);
//
//        try {
//            System.out.println(tmRepo.findEntity(2));
//        } catch (RepositoryException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            System.out.println(tmRepo.findEntity(3));
//        } catch (RepositoryException e) {
//            e.printStackTrace();
//        }

        /*
        File myfile = new File("Data\\1.txt");
        try {
            myfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        ui.executeApp();

    }
}
