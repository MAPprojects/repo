package com.company.ui;

import com.company.domain.Nota;
import com.company.domain.Student;
import com.company.domain.Tema;
import com.company.domain.ValidationException;
import com.company.repository.RepositoryException;
import com.company.service.*;

import java.util.List;
import java.util.Scanner;

public class Ui {

    Service service;


    public Ui(Service service){
        this.service=service;
    }

    private static String getMeniu() {
        return
                        "1. Adauga student\n" +
                        "2. Adauga tema laborator\n" +
                        "3. Afisare studenti\n" +
                        "4. Afisare teme laborator\n" +
                        "5. Afisare catalog\n" +
                        "6. Modificare deadline\n" +
                        "7. Adauga nota\n" +
                        "8. Modifica nota\n"+
                        "9. Filtrari\n"+
                        "10. Sterge student\n"+
                        "0. Ieşire";
    }
    public void run() {
        Scanner cin = new Scanner(System.in);
        while (true) {
            System.out.println("\n" + Ui.getMeniu() + "\n");
            System.out.print("Opţiune: ");
            int optiune = cin.nextInt();
            try {
                switch (optiune) {
                    case 0:
                        return;
                    case 1:
                        this.adaugareStudent();
                        break;
                    case 2:
                        this.adaugareTemaLaborator();
                        break;
                    case 3:
                        this.afisareStudenti();
                        break;
                    case 4:
                        this.afisareTemeLaborator();
                        break;
                    case 5:
                        this.afisareCatalog();
                        break;
                    case 6:
                        this.modificareDeadlineTemaLaborator();
                        break;
                    case 7:
                        this.adaugaNotaStudent();
                        break;
                    case 8:
                        this.modificaNotaStudent();
                        break;
                    case 9:{
                        System.out.print("1.Filtreaza studenti dupa nume\n"+
                                "2.Filtreaza studenti dupa cadru\n"+
                                "3.Filtreaza studenti dupa grupa\n"+
                                "4.Filtreaza teme dupa cerinta\n"+
                                "5.Filtreaza teme dupa deadline\n"+
                                "6.Filtreaza teme dupa cerinta si deadline\n"+
                                "7.Filtreaza note dupa nota\n"+
                                "8.Filtreaza note dupa IdStudent\n"+
                                "9.Filtreaza note dupa NrTema\n");
                        System.out.println("Alegeti un tip de filtrare");
                        int filtru=cin.nextInt();
                        switch(filtru){
                            case 1:
                                this.filtreazaStudentiDupaNume();
                                break;
                            case 2:
                                this.filtreazaStudentiDupaCadru();
                                break;
                            case 3:
                                this.filtreazaStudentiDupaGrupa();
                                break;
                            case 4:
                                this.filtreazaTemaDupaCerinta();
                                break;
                            case 5:
                                this.filtreazaTemaDupaDeadline();
                                break;
                            case 6:
                                this.filtreazaTemaDupaCerintaSiDeadline();;
                                break;
                            case 7:
                                this.filtreazaNoteDupaNota();
                                break;
                            case 8:
                                this.filtreazaNoteDupaStudent();
                                break;
                            case 9:
                                this.filtreazaNoteDupaNrTema();
                                break;
                            default:
                                System.out.println("Optiune invalida!");
                        }
                        break;}
                    case 10:
                        this.stergeStudent();
                        break;
                    default:
                        System.out.println("Optiune invalida!");
                }
            }
            catch (RepositoryException err) {
                System.out.println(err.getMessage());
            } catch (ValidationException err) {
                System.out.println(err.getMessage());
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }

        }
    }



    public void adaugareStudent() throws Exception {
        Scanner cin = new Scanner(System.in);
        System.out.print("Id: ");
        Integer id = Integer.parseInt(cin.nextLine());
        System.out.print("Nume: ");
        String nume = cin.nextLine();
        System.out.print("Grupa: ");
        Integer grupa = Integer.parseInt(cin.nextLine());
        System.out.print("Email: ");
        String email = cin.nextLine();
        System.out.print("Cadru: ");
        String cadru = cin.nextLine();
        service.addStudent(id,nume,grupa,email,cadru);
    }

    public void stergeStudent() {
        Scanner cin = new Scanner(System.in);
        System.out.print(" id ");
        Integer id = Integer.parseInt(cin.nextLine());
        try {
            service.deleteStudent(id);
        } catch (RepositoryException | ValidationException e) {
            System.out.println(e);
        }
    }

    private void adaugareTemaLaborator() throws Exception {
        Scanner cin = new Scanner(System.in);
        System.out.print("ID-ul temei: ");
        Integer numarTema =Integer.parseInt(cin.nextLine());
        System.out.print("Descrierea temei: ");
        String descriere = cin.nextLine();
        System.out.print("Saptamana limita de predare: ");
        Integer termenLimita = cin.nextInt();
        service.addTema(numarTema, descriere, termenLimita);
    }

    public void afisareStudenti() {
        System.out.println("IdStudent  Nume  grupa   email   CadruDidactic");
        for (Student st : service.getStudents())
            System.out.println("" + st.getId() + " " + st.getNume() + " " + st.getGrupa() + " " + st.getEmail() + " " + st.getCadru_didactic());
    }

    public void afisareTemeLaborator(){
        System.out.println("Nr tema    cerinta      deadline");
        for(Tema tl:service.getTeme())
            System.out.println(""+tl.getId()+" "+tl.getCerinta()+" "+tl.getDeadline());
    }

    public void afisareCatalog(){
        System.out.println("Id   IdStudent  NrTema  Nota");
        for(Nota c:service.getNote())
            System.out.println(""+c.getId()+" "+c.getIdStudent()+" "+c.getIdTema()+" "+c.getValoare()+" "+c.getObservatie());
    }

    private void modificareDeadlineTemaLaborator() throws Exception{
        Scanner cin = new Scanner(System.in);
        System.out.print("ID tema: ");
        Integer id = Integer.parseInt(cin.nextLine());
        System.out.println("Deadline nou: ");
        Integer newDeadLine = Integer.parseInt(cin.nextLine());
        service.updateDeadline(id,newDeadLine);
    }

    public void adaugaNotaStudent() throws Exception{
        Scanner cin = new Scanner(System.in);
        System.out.print("id Student ");
        Integer idSt = Integer.parseInt(cin.nextLine());
        System.out.print("id tema");
        Integer idNrTema = Integer.parseInt(cin.nextLine());
        System.out.print("nota ");
        Integer nota = Integer.parseInt(cin.nextLine());
        System.out.print("observatii ");
        String obs = cin.nextLine();
        service.addNota(idSt,idNrTema,nota,obs);
    }
    public void modificaNotaStudent() throws Exception{
        Scanner cin = new Scanner(System.in);
        System.out.print("id Student ");
        Integer idSt = Integer.parseInt(cin.nextLine());
        System.out.print("id tema");
        Integer idNrTema = Integer.parseInt(cin.nextLine());
        System.out.print("nota ");
        Integer nota = Integer.parseInt(cin.nextLine());
        System.out.print("observatii ");
        String obs = cin.nextLine();
        service.changeNota(idSt,idNrTema,nota,obs);
    }


    public void filtreazaStudentiDupaNume(){
        Scanner cin = new Scanner(System.in);
        System.out.print("nume student ");
        String nume = cin.nextLine();
        List<Student> l=service.filtreazaStudentiDupaNume(nume);
        l.stream().forEach(x->System.out.println(""+x.getId()+" "+x.getNume()+" "+x.getGrupa()+" "+x.getEmail()+" "+x.getCadru_didactic()));

    }
    public void filtreazaStudentiDupaCadru(){
        Scanner cin = new Scanner(System.in);
        System.out.print("nume cadru ");
        String nume = cin.nextLine();
        List<Student> l=service.filtreazaStudentiDupaCadru(nume);
        l.stream().forEach(x->System.out.println(""+x.getId()+" "+x.getNume()+" "+x.getGrupa()+" "+x.getEmail()+" "+x.getCadru_didactic()));

    }
    public void filtreazaStudentiDupaGrupa(){
        Scanner cin = new Scanner(System.in);
        System.out.print("grupa ");
        int grupa = Integer.parseInt(cin.nextLine());
        List<Student> l=service.filtreazaStudentiDupaGrupa(grupa);
        l.stream().forEach(x->System.out.println(""+x.getId()+" "+x.getNume()+" "+x.getGrupa()+" "+x.getEmail()+" "+x.getCadru_didactic()));

    }
    public void filtreazaTemaDupaCerinta(){
        Scanner cin = new Scanner(System.in);
        System.out.print("nume cerinta ");
        String nume = cin.nextLine();
        List<Tema> l=service.filtreazaTemaDupaCerinta(nume);
        l.stream().forEach(x->System.out.println(""+x.getId()+" "+x.getCerinta()+" "+x.getDeadline()));
    }
    public void filtreazaTemaDupaDeadline(){
        Scanner cin = new Scanner(System.in);
        System.out.print("deadline");
        Integer deadline = Integer.parseInt(cin.nextLine());
        List<Tema> l=service.filtreazaTemaDupaDeadline(deadline);
        l.stream().forEach(x->System.out.println(""+x.getId()+" "+x.getCerinta()+" "+x.getDeadline()));
    }
    public void filtreazaTemaDupaCerintaSiDeadline(){
        Scanner cin = new Scanner(System.in);
        System.out.print("nume cerinta ");
        String nume = cin.nextLine();
        System.out.print("deadline");
        Integer deadline = Integer.parseInt(cin.nextLine());
        List<Tema> l=service.filtreazaTemaDupaCerintaSiDeadline(nume,deadline);
        l.stream().forEach(x->System.out.println(""+x.getId()+" "+x.getCerinta()+" "+x.getDeadline()));
    }

    public void filtreazaNoteDupaNota(){
        Scanner cin = new Scanner(System.in);
        System.out.print("nota");
        Integer nota = Integer.parseInt(cin.nextLine());
        List<Nota> l=service.filtreazaNoteDupaNota(nota);
        l.stream().forEach(x->System.out.println(""+x.getId()+" "+x.getIdStudent()+" "+x.getIdTema()+" "+x.getValoare()));
    }

    public void filtreazaNoteDupaStudent(){
        Scanner cin = new Scanner(System.in);
        System.out.print("id Student");
        Integer id= Integer.parseInt(cin.nextLine());
        List<Nota> l=service.filtreazaNoteDupaStudent(id);
        l.stream().forEach(x->System.out.println(""+x.getId()+" "+x.getIdStudent()+" "+x.getIdTema()+" "+x.getValoare()));
    }

    public void filtreazaNoteDupaNrTema(){
        Scanner cin = new Scanner(System.in);
        System.out.print("Nr tema");
        Integer id= Integer.parseInt(cin.nextLine());
        List<Nota> l=service.filtreazaNoteDupaNrTema(id);
        l.stream().forEach(x->System.out.println(""+x.getId()+" "+x.getIdStudent()+" "+x.getIdTema()+" "+x.getValoare()));
    }



}
