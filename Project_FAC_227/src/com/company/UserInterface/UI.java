package com.company.UserInterface;

import com.company.Domain.Globals;
import com.company.Domain.Nota;
import com.company.Domain.Student;
import com.company.Domain.Tema;
import com.company.Exceptions.ServiceException;
import com.company.Service.Service;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;

public class UI {

    private Service service;

    Scanner scan;

    public UI(Service service) {
        this.service = service;
        this.scan = new Scanner(System.in);
    }


    private void printMenu() {
        System.out.println("Lista optiuni:");
        System.out.println("    1)Adauga student;");
        System.out.println("    2)Adauga tema;");
        System.out.println("    3)Modificare termen tema;");
        System.out.println("    4)Adauga nota;");
        System.out.println("    5)Modifica nota;");
        System.out.println("    6)Filtrari-Sortari;");
        System.out.println("    7)Sterge student;");

        System.out.println("Alege optiunea:");
    }

    private void executeOption(int optiune) {
        int a;
        if (optiune == 1) {
            int id, grupa;
            String nume, email, prof;

            System.out.println("Introduceti id-ul studentului:");
            id = scan.nextInt();
            System.out.println("Introduceti numele:");
            scan.nextLine();
            nume = scan.nextLine();
            System.out.println("Introduceti grupa:");
            grupa = scan.nextInt();
            System.out.println("Introduceti emailul");
            scan.nextLine();
            email = scan.nextLine();
            //scan.nextLine();
            System.out.println("Introduceti profesorul");
            prof = scan.nextLine();
            //scan.nextLine();

            try {
                service.addStudent(id, nume, grupa, email, prof);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
        if (optiune == 2) {
            int nrTema, deadline;
            String descriere;

            System.out.println("Introduceti id-ul temei:");
            nrTema = scan.nextInt();
            System.out.println("Introduceti descrierea:");
            descriere =  scan.nextLine();
            scan.nextLine();
            System.out.println("Introduceti deadlineul:");
            deadline = scan.nextInt();

            try {
                service.addTema(nrTema,descriere,deadline);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
        if (optiune == 3)
        {
            int nrTema, newdeadline;
            System.out.println("Introduceti id-ul temei dorite:");
            nrTema = scan.nextInt();
            System.out.println("Introduceti noul deadline:");
            newdeadline = scan.nextInt();

            try {
                service.modificareTermen(nrTema,newdeadline);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
        if (optiune == 4)
        {
            int idStudent, nrTema, vnota;
            System.out.println("Introduceti id-ul studentului notat:");
            idStudent = scan.nextInt();
            System.out.println("Introduceti id-ul temei notate:");
            nrTema = scan.nextInt();
            System.out.println("Introduceti nota:");
            vnota = scan.nextInt();

            try {
                service.adaugaNota(idStudent,nrTema,vnota);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
        if(optiune == 5)
        {
            int idStudent, nrTema, vnota;
            System.out.println("Introduceti id-ul studentului notat:");
            idStudent = scan.nextInt();
            System.out.println("Introduceti id-ul temei notate:");
            nrTema = scan.nextInt();
            System.out.println("Introduceti nota:");
            vnota = scan.nextInt();

            try {
                service.modificareNota(idStudent,nrTema,vnota);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
        if(optiune == 6)
        {
            openFSsubmenu();
        }
        if(optiune==7)
        {
            int idStudent;
            idStudent = scan.nextInt();
            try {
                service.deleteStudent(idStudent);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void openFSsubmenu() {
        int optiune;
        System.out.println("Lista filtrari/sortari:");
        System.out.println("    1)Toti studentii profesorului dat sortati dupa grupa;");
        System.out.println("    2)Toti studentii care au email cu un anumit domeniu sortati dupa nume;");
        System.out.println("    3)Toti studentii care au cel putin o nota mai mare decat nota data sortati dupa nume;");
        System.out.println("    4)Toate temele cu deadlineul mai mic decat cel dat sortate dupa deadline;");
        System.out.println("    5)Toate temele care contin in descriere sirul introdus sortate dupa deadline;");
        System.out.println("    6)Toate temele la care nu exista nota sortate dupa descriere;");
        System.out.println("    7)Toate notele unui student sortate crescator;");
        System.out.println("    8)Toate notele de la o tema anume sortate descrescator;");
        System.out.println("    9)Toate notele de sub 5 sortate crescator;");

        optiune = scan.nextInt();
        scan.nextLine();
        if(optiune==1){
            System.out.println("Introduceti numele profesurului:");
            String prof = scan.nextLine();
            //scan.nextLine();

            for (Student student:service.studenti_Fprof_Sgrupa(prof)) {
                System.out.println(student);
            }
        }
        if(optiune==2)
            System.out.println("Introduceti domeniul emailului:");
            String domeniu = scan.nextLine();

            for (Student student: service.studenti_Femail_Snume(domeniu))
                System.out.println(student);
        if(optiune==3)
        {
            System.out.println("Introduceti nota:");
            int vnota = scan.nextInt();

            for (Student student: service.studenti_Fnota_Snume(vnota))
                System.out.println(student);
        }
        if(optiune==4)
        {
            System.out.println("Introduceti deadlineul:");
            int deadline = scan.nextInt();

            for(Tema tema:service.teme_Fdeadline_Sdeadline(deadline))
                System.out.println(tema);
        }
        if(optiune==5){
            System.out.println("Introduceti sirul de caractere:");
            String sir = scan.nextLine();

            for(Tema tema: service.teme_Fdescriere_Sdeadline(sir))
                System.out.println(tema);
        }
        if(optiune==6)
        {
            for(Tema tema: service.teme_Fnote_Sdescriere())
                System.out.println(tema);
        }
        if(optiune==7)
        {
            System.out.println("Introduceti idul studentului:");
            int id = scan.nextInt();

            for(Nota nota:service.nota_Fstudent_Screscator(id))
                System.out.println(nota);

        }
        if(optiune==8)
        {
            System.out.println("Introduceti numarul temei:");
            int id = scan.nextInt();

            for(Nota nota: service.nota_Ftema_Sdescrescator(id))
                System.out.println(nota);
        }
        if(optiune==9)
        {
            for(Nota nota: service.nota_Fsub5_Stema())
                System.out.println(nota);
        }
    }

    public void executeApp() {
        int optiune;
        System.out.println("Introduceti saptamana curenta");
        Globals.setSaptCurenta(scan.nextInt());
        while (true) {
            printMenu();
            try {
                optiune = scan.nextInt();
                System.out.println(optiune);
                if (optiune == 0)
                    break;
                else
                    executeOption(optiune);
            } catch (java.util.InputMismatchException e) {
                scan.nextLine();
            }


            //System.out.println(optiune);
            //}
        }
    }
}