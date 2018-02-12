//package ui;
//
//import entities.Nota;
//import entities.Student;
//import entities.Tema;
//import exceptions.*;
//import services.NotaService;
//import services.StudentService;
//import services.TemaService;
//import vos.NotaVO;
//
//import javax.xml.bind.ValidationException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class Console {
//    private TemaService temaService;
//    private StudentService studentService;
//    private NotaService notaService;
//
//    public Console(TemaService temaService, StudentService studentService, NotaService notaService) {
//        this.temaService = temaService;
//        this.studentService = studentService;
//        this.notaService = notaService;
//    }
//
//    public void showMenu() {
//        boolean finished = false;
//        Scanner scanner = new Scanner(System.in);
//        while (!finished) {
//            printSelectionMenu();
//            String optiune = scanner.nextLine();
//            if (optiune.equals("1")) {
//                afisareTotiStudentii();
//            } else if (optiune.equals("2")) {
//                adaugaUnStudent();
//            } else if (optiune.equals("3")) {
//                stergeUnStudent();
//            } else if (optiune.equals("4")) {
//                cautaStudentPeBazaId();
//            } else if (optiune.equals("5")) {
//                adaugaTema();
//            } else if (optiune.equals("6")) {
//                modificaTermenPredareTema();
//            } else if (optiune.equals("7")) {
//                afisareToateTemle();
//            } else if (optiune.equals("8")) {
//                adaugareNota();
//            } else if (optiune.equals("9")) {
//                modificareNota();
//            } else if (optiune.equals("10")) {
//                printMeniuFiltrari();
//                String optiuneFiltrari = scanner.nextLine();
//                if (optiuneFiltrari.equals("1")) {
//                    printMeniuFiltrariStuddent();
//                    String optiuneFiltrareStudent = scanner.nextLine();
//                    if (optiuneFiltrareStudent.equals("1")) {
//                        filtreazaStudentiiPeProfesor();
//                    } else if (optiuneFiltrareStudent.equals("2")) {
//                        filtreazaStudentiiDinGrupa();
//                    } else if (optiuneFiltrareStudent.equals("3")) {
//                        filtreazaStudentiiCuNumele();
//                    }
//                } else if (optiuneFiltrari.equals("2")) {
//                    printMeniuFiltrariTema();
//                    String optiuneFiltrareTema = scanner.nextLine();
//                    if (optiuneFiltrareTema.equals("1")) {
//                        filtreazaTemelePeBazaCerintei();
//                    } else if (optiuneFiltrareTema.equals("2")) {
//                        filtreazaTemelePeTermenuluiDePredare();
//                    } else if (optiuneFiltrareTema.equals("3")) {
//                        filtreazaTemelePeBazaCerinteiSiATermenului();
//                    }
//                } else if (optiuneFiltrari.equals("3")) {
//                    printMeniuFiltrariNota();
//                    String optiuneFiltrareNota = scanner.nextLine();
//                    if (optiuneFiltrareNota.equals("1")) {
//                        filtreazaNoteleCuAceeasiValoare();
//                    } else if (optiuneFiltrareNota.equals("2")) {
//                        filtreazaNotelePtUnStudent();
//                    } else if (optiuneFiltrareNota.equals("3")) {
//                        filtreazaNotelePtOTema();
//                    }
//                }
//            } else if (optiune.equals("11")) {
//                finished = true;
//            }
//
//        }
//    }
//
//    private void afisareTotiStudentii() {
//        ArrayList<Student> students = studentService.findAllStudents();
//        if (students.size() == 0) {
//            System.out.println("Nu exista niciun student in repository!");
//        }
//        for (Student student : students) {
//            System.out.println(student.toString());
//        }
//    }
//
//    private void adaugaUnStudent() {
//        String id, nume, grupa, email, cadruDidacticIndrumator;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti id-ul studentului:");
//        id = scanner.nextLine();
//        System.out.println("Introduceti numele studentului:");
//        nume = scanner.nextLine();
//        System.out.println("Introduceti grupa studentului:");
//        grupa = scanner.nextLine();
//        System.out.println("Introduceti email-ul studentului:");
//        email = scanner.nextLine();
//        System.out.println("Introduceti numele cadrului didactic indrumator studentului:");
//        cadruDidacticIndrumator = scanner.nextLine();
//        try {
//            studentService.addStudent(nume, grupa, email, cadruDidacticIndrumator);
//            System.out.println("Studentul a fost adaugat cu succes.");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void stergeUnStudent() {
//        String id,nume, grupa, email, cadruDidacticIndrumator;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti id-ul studentului:");
//        id = scanner.nextLine();
//        System.out.println("Introduceti numele studentului:");
//        nume = scanner.nextLine();
//        System.out.println("Introduceti grupa studentului:");
//        grupa = scanner.nextLine();
//        System.out.println("Introduceti email-ul studentului:");
//        email = scanner.nextLine();
//        System.out.println("Introduceti numele cadrului didactic indrumator studentului:");
//        cadruDidacticIndrumator = scanner.nextLine();
//        try {
//            studentService.deleteStudent(new Student(id, nume, grupa, email, cadruDidacticIndrumator));
//            System.out.println("Studentul a fost sters cu succes.");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void cautaStudentPeBazaId() {
//        System.out.println("Introduceti id-ul studentului");
//        Scanner scanner = new Scanner(System.in);
//        String id = scanner.nextLine();
//        try {
//            Student student = studentService.findOneStudentById(id);
//            System.out.println(student.toString());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void adaugaTema() {
//        Integer termenPredare;
//        String cerinta;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti cerinta temei:");
//        cerinta = scanner.nextLine();
//        System.out.println("Introduceti termenul de predare al temei:");
//        termenPredare = scanner.nextInt();
//        scanner.nextLine();
//        try {
//            temaService.addTema(cerinta, termenPredare);
//            System.out.println("Tema a fost adaugata cu succes!");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void afisareToateTemle() {
//        ArrayList<Tema> teme = temaService.findAllTeme();
//        if (teme.size() == 0) {
//            System.out.println("Nu exista nicio tema in repository!");
//        }
//        for (Tema tema : teme) {
//            System.out.println(tema.toString());
//        }
//    }
//
//    private void modificaTermenPredareTema() {
//        String id;
//        Integer termenNou;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti id-ul temei pe care doriti sa o modificati:");
//        id = scanner.nextLine();
//        scanner.nextLine();
//        System.out.println("Introduceti noul termen de predare:");
//        termenNou = scanner.nextInt();
//        scanner.nextLine();
//        try {
//            temaService.actualizeazaTermenulDePredare(id, termenNou);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void adaugareNota() {
//        String idStudent;
//        Tema idTema;
//        Integer valoareNota;
//        int saptamana;
//        Scanner sc = new Scanner(System.in);
//        this.afisareTotiStudentii();
//        System.out.println("Introduceti id-ul unui student:");
//        idStudent = sc.nextLine();
//        sc.nextLine();
//        this.afisareToateTemle();
//        System.out.println("Introduceti id-ul temei dorite:");
//        idTema = sc.nextLine();
//        sc.nextLine();
//        System.out.println("Introduceti nota:");
//        valoareNota = sc.nextInt();
//        sc.nextLine();
//        System.out.println("Introduceti saptamana curenta:");
//        saptamana = sc.nextInt();
//        sc.nextLine();
//        System.out.println("Introduceti eventuale observatii:");
//        String observatii = sc.nextLine();
//        try {
//            notaService.addNota(new Nota(idStudent, idTema, valoareNota), saptamana, observatii);
//        } catch (NotaServiceException e) {
//            System.out.println(e.getMessage());
//        } catch (AbstractValidatorException e) {
//            System.out.println(e.getMessage());
//        } catch (ValidationException e) {
//            System.out.println(e.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void modificareNota() {
//        String idStudent;
//        Tema idTema;
//        Integer valoareNota;
//        int saptamana;
//        Scanner sc = new Scanner(System.in);
//        this.afisareTotiStudentii();
//        System.out.println("Introduceti id-ul studentului:");
//        idStudent = sc.nextLine();
//        sc.nextLine();
//        this.afisareToateTemle();
//        System.out.println("Introduceti id-ul temei dorite:");
//        idTema = sc.nextLine();
//        sc.nextLine();
//        System.out.println("Introduceti nota noua:");
//        valoareNota = sc.nextInt();
//        sc.nextLine();
//        System.out.println("Introduceti saptamana curenta:");
//        saptamana = sc.nextInt();
//        sc.nextLine();
//        try {
//            notaService.updateNota(new Nota(idStudent, idTema, valoareNota), saptamana);
//        } catch (NotaServiceException e) {
//            System.out.println(e.getMessage());
//        } catch (AbstractValidatorException e) {
//            System.out.println(e.getMessage());
//        } catch (ValidationException e) {
//            System.out.println(e.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void filtreazaStudentiiPeProfesor() {
//        String numeProf;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti numele profesorului");
//        numeProf = scanner.nextLine();
//        ArrayList<Student> list = studentService.filtreazaStudentiiDeLaUnProfesor(numeProf).get();
//        if (list.size() == 0) {
//            System.out.println("Niciun rezultat");
//        } else {
//            list.forEach(System.out::println);
//        }
//    }
//
//    private void filtreazaStudentiiDinGrupa() {
//        String grupa;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti grupa");
//        grupa = scanner.nextLine();
//        ArrayList<Student> list = studentService.filtreazaStudentiiDinGrupa(grupa).get();
//        if (list.size() == 0) {
//            System.out.println("Niciun rezultat");
//        } else {
//            list.forEach(System.out::println);
//        }
//    }
//
//    private void filtreazaStudentiiCuNumele() {
//        String nume;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti numele");
//        nume = scanner.nextLine();
//        ArrayList<Student> list = studentService.filtreazaStudentiiCuNumele(nume).get();
//        if (list.size() == 0) {
//            System.out.println("Niciun rezultat");
//        } else {
//            list.forEach(System.out::println);
//        }
//    }
//
//    private void filtreazaTemelePeBazaCerintei() {
//        String cerinta;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti o parte din cerinta");
//        cerinta = scanner.nextLine();
//        ArrayList<Tema> list = (ArrayList<Tema>) temaService.filtreazaCerinta(cerinta).get();
//        if (list.size() == 0) {
//            System.out.println("Niciun rezultat");
//        } else {
//            list.forEach(System.out::println);
//        }
//    }
//
//    private void filtreazaTemelePeTermenuluiDePredare() {
//        Integer termenPredare;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti termenul de predare");
//        termenPredare = scanner.nextInt();
//        ArrayList<Tema> list = (ArrayList<Tema>) temaService.filtreazaTermenPredare(termenPredare).get();
//        if (list.size() == 0) {
//            System.out.println("Niciun rezultat");
//        } else {
//            list.forEach(System.out::println);
//        }
//    }
//
//    private void filtreazaTemelePeBazaCerinteiSiATermenului() {
//        String cerinta;
//        Integer termenPredare;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduceti cerinta");
//        cerinta = scanner.nextLine();
//        System.out.println("Introduceti termenul limita");
//        termenPredare = scanner.nextInt();
//        ArrayList<Tema> list = (ArrayList<Tema>) temaService.filtreazaTermenPredareSiCerinta(cerinta, termenPredare).get();
//        if (list.size() == 0) {
//            System.out.println("Niciun rezultat");
//        } else {
//            list.forEach(System.out::println);
//        }
//    }
//
//    private void filtreazaNoteleCuAceeasiValoare() {
//        Integer valoare;
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Introduceti valoarea notei");
//        valoare = sc.nextInt();
//        ArrayList<NotaVO> note = (ArrayList<NotaVO>) notaService.filtreazaPentruValoare(valoare).get();
//        if (note.size() == 0) {
//            System.out.println("Niciun rezultat");
//        } else {
//            note.forEach(System.out::println);
//        }
//    }
//
//    private void filtreazaNotelePtUnStudent() {
//        String numeleStudentului;
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Introduceti numele studentului");
//        numeleStudentului = sc.nextLine();
//        ArrayList<NotaVO> note = (ArrayList<NotaVO>) notaService.filtreazaNotelePentruUnStudent(numeleStudentului).get();
//        if (note.size() == 0) {
//            System.out.println("Niciun rezultat");
//        } else {
//            note.forEach(System.out::println);
//        }
//    }
//
//    private void filtreazaNotelePtOTema() {
//        Tema idTema;
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Introduceti id-ul");
//        idTema = sc.nextLine();
//        ArrayList<NotaVO> note = (ArrayList<NotaVO>) notaService.filtreazaNotelePentruOTema(idTema).get();
//        if (note.size() == 0) {
//            System.out.println("Niciun rezultat");
//        } else {
//            note.forEach(System.out::println);
//        }
//    }
//
//    private void printMeniuFiltrari() {
//        System.out.println("1.Studenti");
//        System.out.println("2.Teme");
//        System.out.println("3.Note");
//    }
//
//    private void printMeniuFiltrariNota() {
//        System.out.println("1.Filtreaza toate notele cu aceeasi valoare");
//        System.out.println("2.Filtreaza toate notele pentur un sutdent");
//        System.out.println("3.Filtreaza toate notele pentru o tema");
//    }
//
//    private void printMeniuFiltrariTema() {
//        System.out.println("1.Filtreaza temele pe baza cerintei");
//        System.out.println("2.Filtreaza temele pe baza deadline-ului");
//        System.out.println("3.Filtreaza pe baza termenului de predare si a cerintei");
//    }
//
//    private void printMeniuFiltrariStuddent() {
//        System.out.println("1.Filtreaza studentii de la un anumit profesor");
//        System.out.println("2.Filtreaza studentii dintr-o grupa");
//        System.out.println("3.Filtreaza studentii cu numele dat");
//    }
//
//    private void printSelectionMenu() {
//        System.out.println("____________CATALOG____________");
//        System.out.println("1.Afisare toti studentii");
//        System.out.println("2.Adaugare a unui student");
//        System.out.println("3.Stergerea unui student");
//        System.out.println("4.Cautarea unui student pe baza id-ului personal");
//        System.out.println("5.Adaugarea unei teme noi");
//        System.out.println("6.Modificarea termenului de predare pentru o tema existenta");
//        System.out.println("7.Afisare toate temele");
//        System.out.println("8.Adaugare nota");
//        System.out.println("9.Modificare nota");
//        System.out.println("10.Filtrari");
//        System.out.println("11.Exit");
//    }
//
//
//}
