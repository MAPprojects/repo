//package ui;
//
//import domain.Laborator;
//import domain.Nota;
//import domain.Student;
//import service.ServiceLaborator;
//import service.ServiceNota;
//import service.ServiceStudent;
//import util.CurrentWeek;
//import util.SendEmail;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//
//public class UI {
//    private ServiceNota serviceNota = new ServiceNota();
//    private ServiceStudent serviceStudent = new ServiceStudent(serviceNota.getRepoNota());
//    private ServiceLaborator serviceLaborator = new ServiceLaborator();
//    private SendEmail sendEmail = new SendEmail();
//
//    public UI() throws Exception {
//    }
//
//    public void interfata() {
//
//        Integer c = 1;
//        while (c != 0) {
//            System.out.println("MENIU ITERATIA 1:");
//            System.out.println();
//            System.out.println("1)Creaza student;");
//            System.out.println("2)Afiseaza student in functie de id;");
//            System.out.println("3)Afiseaza toti studentii!;");
//            System.out.println("4)Modifica;");
//            System.out.println("5)Sterge;");
//            System.out.println("6)Adauga laborator;");
//            System.out.println("7)Modifica deadline laborator;");
//            System.out.println("0)Iesi.");
//            System.out.println();
//            System.out.println();
//            System.out.println("MENIU ITERATIA 2:");
//            System.out.println();
//            System.out.println("8)Adauga nota;");
//            System.out.println("9)Modifica nota;");
//            System.out.println("10)Media ponderata in parte la fiecare student;");
//            System.out.println("11)Cea mai grea tema!!!;");
//            System.out.println("12)Studenti fara penalizari!;");
//            System.out.println();
//            System.out.println("MENIU ITERATIA 3 FILTRE");
//            System.out.println("    FILTRARI NOTE");
//            System.out.println("        13)Filtrare dupa note de 10 ordonat crescator dupa numarul temei!;");
//            System.out.println("        14)Filtrare dupa note de 1 ordonat crescator dupa id-ul studentului!;");
//            System.out.println("        15)Filtrare dupa toate notele unui student ordonate crescator;");
//            System.out.println();
//            System.out.println("    FILTRARI TEME");
//            System.out.println("        16)Filtrare dupa toate temele care au cerinte prelucrarea unui numar/unor numere;");
//            System.out.println("        17)Filtrare dupa dupa deadlinurile din a doua jumatate de semestru;");
//            System.out.println("        18)Filtrare dupa temele ordonate dupa deadline;");
//            System.out.println();
//            System.out.println("    FILTRARI STUDENTI");
//            System.out.println("        19)Filtrare dupa studentii dintr-un anumit an;");
//            System.out.println("        20)Filtrare dupa studentii care sunt asociati unui indrumator anume");
//            System.out.println("        21)Filtrare dupa studentii care au numele sau prenumele care incepe cu o anumita litera;");
//            System.out.println();
//            System.out.println("UTILS");
//            System.out.println("22)Creste saptamana curenta;");
//            System.out.println("23)Afiseaza saptamana curenta");
//            System.out.println("24)Afiseaza studentii trecuti");
//            System.out.println();
//            System.out.println();
//            System.out.println("0)Paraseste aplicatia;");
//
////            Scanner sc = new Scanner(System.in).useDelimiter("\\n");
//            Scanner keyboard = new Scanner(System.in).useDelimiter("\\n");
//            try {
//                c = keyboard.nextInt();
//            } catch (Exception e) {
//                c = 500;
//                System.out.println("Vezi ca nu ai dat un numar!");
//            }
//            switch (c) {
//                case 1:
//                    System.out.println("Dati id-ul studentului!");
//                    Integer idStudent = keyboard.nextInt();
//                    System.out.println("Dati numele studentului!");
//                    String nume = keyboard.next();
//                    System.out.println("Dati grupa studentului!");
//                    Integer grupa = keyboard.nextInt();
//                    System.out.println("Dati emailul studentului!");
//                    String email = keyboard.next();
//                    System.out.println("Dati numele indrumatorului grupei din care studentul face parte!");
//                    String indrumator = keyboard.next();
//                    try {
//                        serviceStudent.add(idStudent, nume, grupa, email, indrumator);
//                        System.out.println("Studentul a fost adaugat cu succes!");
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//
//                    break;
//                case 2:
//                    System.out.println("Dati id-ul studentului pe care doriti sa il gasiti!");
//                    idStudent = keyboard.nextInt();
//                    try {
//                        Student s = serviceStudent.findobject(idStudent);
//                        System.out.println(s.toString());
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                case 3:
//                    System.out.println("Aceasta este lista cu studentii:");
//                    System.out.println();
//                    try {
//                        List<Student> g = serviceStudent.returnall();
//                        for (Student p : g) {
//                            System.out.println(p.toString());
//                        }
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//
//                    break;
//                case 4:
//                    System.out.println("Dati id-ul studentului pe care doriti sa il modificati!");
//                    idStudent = keyboard.nextInt();
//                    System.out.println("Dati numele studentului!");
//                    nume = keyboard.next();
//                    System.out.println("Dati grupa studentului!");
//                    grupa = keyboard.nextInt();
//                    System.out.println("Dati emailul studentului!");
//                    email = keyboard.next();
//                    System.out.println("Dati numele indrumatorului grupei din care studentul face parte!");
//                    indrumator = keyboard.next();
//                    try {
//                        serviceStudent.update(idStudent, nume, grupa, email, indrumator);
//                        System.out.println("Studentul a fost modificat cu succes!");
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                case 5:
//                    System.out.println("Dati id-ul studentului pe care doriti sa il stergeti!");
//                    idStudent = keyboard.nextInt();
//                    try {
//                        serviceStudent.delete(idStudent);
//                        System.out.println("Studentul a fost sters cu succes!");
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                case 6:
//                    System.out.println("Dati numarul temei!");
//                    Integer nrTema = keyboard.nextInt();
//                    System.out.println("Dati enuntul temei!");
//                    String cerinta = keyboard.next();
//                    System.out.println("Dati deadline-ul temei!");
//                    Integer deadline = keyboard.nextInt();
//                    try {
//                        serviceLaborator.add(nrTema, cerinta, deadline);
//                        System.out.println("Laboratorul a fost adaugat cu succes!");
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//
//                    break;
//                case 7:
//                    System.out.println("Dati numarul temei care doriti sa o modificati!");
//                    nrTema = keyboard.nextInt();
//                    System.out.println("Dati deadline-ul temei!");
//                    deadline = keyboard.nextInt();
//                    try {
//                        serviceLaborator.updateDeadline(nrTema, deadline);
//                        System.out.println("Laboratorul a fost modificat cu succes!");
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                case 8:
//                    System.out.println("Dati id-ul studentului!");
//                    idStudent = keyboard.nextInt();
//                    System.out.println("Dati nota!");
//                    Integer valoare = keyboard.nextInt();
//                    System.out.println("Dati tema la care asignati nota!");
//                    nrTema = keyboard.nextInt();
//                    System.out.println("Specificati daca aveti ceva observatii!");
//                    String observatii = keyboard.next();
//
//                    try {
//                        serviceNota.add(idStudent, valoare, nrTema, observatii);
//                        System.out.println("Operatia a fost efectuata cu succes!");
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                case 9:
//                    System.out.println("Dati id-ul studentului!");
//                    idStudent = keyboard.nextInt();
//                    System.out.println("Dati nota!");
//                    valoare = keyboard.nextInt();
//                    System.out.println("Dati numarul temmei la care asignati nota!");
//                    nrTema = keyboard.nextInt();
//                    System.out.println("Specificati daca aveti ceva observatii!");
//                    observatii = keyboard.next();
//
//                    try {
//                        serviceNota.update(idStudent, valoare, nrTema, observatii);
//                        System.out.println("Operatia a fost efectuata cu succes!");
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                case 10:
//                    Map<String, Double> pondere = serviceNota.mediaPonderata();
//                    for (String key : pondere.keySet()) {
//                        System.out.println("***********************");
//                        System.out.println("*          |           *");
//                        System.out.println("**" + "Nume: " + key + "| Nota: " + pondere.get(key) + "**");
//                        System.out.println("*                     *");
//                        System.out.println("***********************");
//
//                    }
//                    break;
//                case 11:
//                    Laborator l = null;
//                    try {
//                        l = serviceNota.ceaMaiGreaTema();
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                    System.out.println(l);
//                    break;
//                case 12:
//                    for (Student s : serviceNota.studentiEminenti()) {
//                        System.out.println(s.getNume());
//                    }
//                    break;
//                case 13:
//                    for (Nota s : serviceNota.noteDe10()) {
////                        System.out.println(s.getValoare()+"----------------"+s.getNrTema());
//                        System.out.println(s);
//                    }
//                    break;
//                case 14:
//                    for (Nota s : serviceNota.noteDe1()) {
//                        System.out.println("Nota: " + s.getValoare() + " Student: " + s.getIdStudent());
//                    }
//                    break;
//                case 15:
//                    System.out.println("Dati id:");
//                    Integer id = keyboard.nextInt();
//                    for (Nota n : serviceNota.noteleUnuiStudentInOrdine(id)) {
//                        System.out.println("Id-ul studentului: " + n.getIdStudent() + " Nota: " + n.getValoare());
//                    }
//                    break;
//                case 16:
//                    for (Laborator s : serviceLaborator.laboratoareCarePrelucreazaNumere()) {
//                        System.out.println("Cerinta: " + s.getCerinta() + " Numar tema: " + s.getNrTema());
//                    }
//                    break;
//                case 17:
//                    for (Laborator s : serviceLaborator.secondHalfOfSemester()) {
//                        System.out.println("Deadline: " + s.getDeadline() + " Numar tema: " + s.getNrTema());
//                    }
//                    break;
//                case 18:
//                    for (Laborator s : serviceLaborator.temeOrdonateDupaDeadline()) {
//                        System.out.println("Deadline: " + s.getDeadline());
//                    }
//                    break;
//                case 19:
//                    System.out.println("Dati an:");
//                    Integer an = keyboard.nextInt();
//                    for (Student s : serviceStudent.studentiPeAni(an)) {
//                        System.out.println("Studenti: " + s.getNume() + " Grupa: " + s.getGrupa());
//                    }
//                    break;
//                case 20:
//                    System.out.println("Dati nume:");
//                    String indrumatorul = keyboard.next();
//                    for (Student s : serviceStudent.tataLor(indrumatorul)) {
//                        System.out.println("Indrumator: " + s.getIndrumator() + " Student: " + s.getNume());
//                    }
//                    break;
//                case 21:
//                    System.out.println("Cautati dupa litera:");
//                    String litera = keyboard.next();
//                    for (Student s : serviceStudent.studentiNumeSauPrenumeCuA(litera)) {
//                        System.out.println("Nume: " + s.getNume());
//                    }
//                    break;
//                case 22:
//                    CurrentWeek.incWeek();
//                    System.out.println("Current week is:" + CurrentWeek.CURRENT_WEEK);
//                    break;
//                case 23:
//                    System.out.println(CurrentWeek.CURRENT_WEEK);
//                    break;
//                case 24:
//                    Map<String,Double> rezultat=serviceNota.studentiCareIntraInExamen();
//                    System.out.println(rezultat+"\n");
//                    break;
//                case 0:
//                    System.out.println("O zi faina!");
//                    break;
//                default:
//                    System.out.println("Optiunile sunt intre 1 si 19. Ca sa iesiti selectati 0!");
//                    break;
//            }
//        }
//    }
//}
