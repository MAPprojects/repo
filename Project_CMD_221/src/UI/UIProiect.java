package UI;

import Controllers.ControllerException;
import Controllers.Service;
import Entities.Nota;
import Entities.Teme;
import Entities.User;
import Repository.IRepository;
import Entities.Student;
import Repository.IUserRepository;
import Repository.RepositoryException;
import Validators.Validator;
import Validators.ValidatorException;
import javafx.util.Pair;

import javax.xml.bind.SchemaOutputResolver;
import java.util.List;
import java.util.Scanner;

public class UIProiect {
    private IRepository<Integer, Student> repo_st;
    private IRepository<Integer, Teme> repo_t;
    private IRepository<Pair<Integer,Integer>, Nota> repo_n;
    private IUserRepository<Pair<String, String>, User> repo_u;
    Service service = new Service(repo_st, repo_t, repo_n, repo_u);

    public UIProiect(Service service) {
        this.service = service;
    }

    public void saveStudent() throws ValidatorException {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID student: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            System.out.println("Nume student: ");
            String nume = scanner.nextLine();
            System.out.println("Email student: ");
            String email = scanner.nextLine();
            System.out.println("Grupa student: ");
            String grupa = scanner.nextLine();
            System.out.println("Indrumator: ");
            String prof = scanner.nextLine();
            service.save_student(id, nume, email, grupa, prof, 0, 0, 0.0, true, true);
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
    }

    public void updateStudent() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID student: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            System.out.println("Nume student: ");
            String nume = scanner.nextLine();
            System.out.println("Email student: ");
            String email = scanner.nextLine();
            System.out.println("Grupa student: ");
            String grupa = scanner.nextLine();
            System.out.println("Indrumator: ");
            String prof = scanner.nextLine();
            service.update_student(id, nume, email, grupa, prof, service.findStudent(id).getNote(), service.findStudent(id).getNr(), service.findStudent(id).getMedie(), service.findStudent(id).isVal(), service.findStudent(id).isFint());
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
    }

    public void stergeStudent() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID student: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            service.delete_student(id);
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
    }

    public void findStudent() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID student: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            System.out.println(service.findStudent(id));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
    }

    public void filterStudentByGroup() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Dati grupa: ");
            String grupa = scanner.nextLine();
            //showList(service.filterStudentByGroup(grupa));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
    }

    public void filterStudentByTeacher() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Dati proful: ");
            String prof = scanner.nextLine();
            //showList(service.filterStudentByTeacher(prof));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
    }

    public void filterStudentByFirstLetter() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Dati litera: ");
            String litera = scanner.nextLine();
            //showList(service.filterByFirstLetter(litera));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
    }

    public void saveTema() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID tema: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            System.out.println("Deadline: ");
            Integer deadline = Integer.parseInt(scanner.nextLine());
            System.out.println("Descriere: ");
            String descriere = scanner.nextLine();
            service.save_tema(id, deadline, descriere, 0);
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
    }

    public void updateTema() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID tema: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            System.out.println("Deadline: ");
            Integer deadline = Integer.parseInt(scanner.nextLine());
            System.out.println("Descriere: ");
            String descriere = scanner.nextLine();
            service.update_tema(id, deadline, descriere, 0);
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
        catch(ControllerException c) {
            System.out.println(c.getMessage());
        }
    }

    private void updateDeadline() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Tema: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            System.out.println("Deadline: ");
            Integer deadline = Integer.parseInt(scanner.nextLine());
            service.update_Deadline(id, deadline);
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
        catch(ControllerException c) {
            System.out.println(c.getMessage());
        }
    }

    public void stergeTema() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID tema: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            service.delete_tema(id);
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
    }

    public void findTema() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID tema: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            System.out.println(service.findTema(id));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
    }

    public void filterByDeadline() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Dati deadline-ul: ");
            Integer deadline = Integer.parseInt(scanner.nextLine());
            //showList(service.filterByDeadline(deadline));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
    }

    public void filterByLowerDeadline() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Dati deadline-ul: ");
            Integer deadline = Integer.parseInt(scanner.nextLine());
            //showList(service.filterByLowerDeadline(deadline));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
    }

    public void filterByHigherDeadline() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Dati deadline-ul: ");
            Integer deadline = Integer.parseInt(scanner.nextLine());
            //showList(service.filterByHigherDeadline(deadline));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
    }

    public void saveNota() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student: ");
            Integer ids = Integer.parseInt(scanner.nextLine());
            System.out.println("Nr tema: ");
            Integer nrt = Integer.parseInt(scanner.nextLine());
            System.out.println("Nota: ");
            Integer nota = Integer.parseInt(scanner.nextLine());
            System.out.println("Saptamana predarii: ");
            Integer week = Integer.parseInt(scanner.nextLine());
            System.out.println("Observatii: ");
            String obs = scanner.nextLine();
            Pair<Integer,Integer> p = new Pair<>(ids,nrt);
            //service.saveNota(p, nota, week);
            service.export(service.findNota(p), week, obs, "Adaugare nota");
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
        catch(ControllerException c) {
            System.out.println(c.getMessage());
        }
    }

    public void updateNota() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student: ");
            Integer ids = Integer.parseInt(scanner.nextLine());
            System.out.println("Nr tema: ");
            Integer nrt = Integer.parseInt(scanner.nextLine());
            System.out.println("Nota: ");
            Integer nota = Integer.parseInt(scanner.nextLine());
            System.out.println("Saptamana predarii: ");
            Integer week = Integer.parseInt(scanner.nextLine());
            System.out.println("Observatii: ");
            String obs = scanner.nextLine();
            Pair<Integer,Integer> p = new Pair<>(ids,nrt);
            //service.updateNota(p, nota, week);
            service.export(service.findNota(p), week, obs, "Modificare nota");
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
        catch(ControllerException c) {
            System.out.println(c.getMessage());
        }
    }

    public void stergeNota() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student: ");
            Integer ids = Integer.parseInt(scanner.nextLine());
            System.out.println("Nr tema: ");
            Integer nrt = Integer.parseInt(scanner.nextLine());
            Pair<Integer,Integer> id = new Pair<>(ids,nrt);
            service.deleteNota(id);
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
    }

    public void findNota() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student: ");
            Integer ids = Integer.parseInt(scanner.nextLine());
            System.out.println("Nr tema: ");
            Integer nrt = Integer.parseInt(scanner.nextLine());
            Pair<Integer,Integer> id = new Pair<>(ids,nrt);
            System.out.println(service.findNota(id));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
        catch(RepositoryException r) {
            System.out.println(r.getMessage());
        }
    }

    public void filterByValoare() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Dati nota: ");
            Integer nota = Integer.parseInt(scanner.nextLine());
            showList(service.filterByValoare(nota));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
    }

    public void filterByStudent() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Dati id-ul studentului: ");
            Integer idStudent = Integer.parseInt(scanner.nextLine());
            showList(service.filterByStudent(idStudent));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
    }

    public void filterByNrHomework() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Dati numarul temei: ");
            Integer nrTema = Integer.parseInt(scanner.nextLine());
            showList(service.filterByNrHomework(nrTema));
        }
        catch(ValidatorException v) {
            System.out.println(v.getMessage());
        }
    }

    public void afisStudenti() {
        for(Student student : service.getAllStudents())
            System.out.println("- IDStudent: " + student.getID() + " Nume: " + student.getNume() + " Email: " + student.getEmail() + " Grupa: " + student.getGrupa() + " Indrumator: " + student.getProf());
    }

    public void afisTeme() {
        for(Teme tema : service.getAllTeme())
            System.out.println("- IDTema: " + tema.getID() + " Deadline: " + tema.getDeadline() + " Descriere: " + tema.getDescriere());
    }

    public void afisNota() {
        for(Nota nota : service.getAllNote())
            System.out.println(" IDStudent: " + nota.getIDStudent() + " NrTema: " + nota.getNrTema() + " Nota: " + nota.getValoare());
    }

    private void showList(List list){
        if(list.size()>0){
            list.forEach(x -> System.out.println(x));
        }
        else {
            System.out.println("Nu s-a gasit nici o entitate conform filtratii dorite!");
        }
    }

    public void meniuStudenti() {
        while(true) {
            System.out.println("Meniu\n");
            System.out.println("1.Adauga student");
            System.out.println("2.Update student");
            System.out.println("3.Sterge student");
            System.out.println("4.Cauta student");
            System.out.println("5.Toti studentii");
            System.out.println("6.Filtreaza studenti dupa grupa");
            System.out.println("7.Filtreaza studenti dupa prof");
            System.out.println("8.Filtreaza studenti dupa prima litera");
            System.out.println("0.Iesire");
            System.out.println("Dati comanda: ");
            Scanner scanner = new Scanner(System.in);
            Integer cmd = Integer.parseInt(scanner.nextLine());
            if(cmd == 1)
                saveStudent();
            else if(cmd == 2)
                updateStudent();
            else if(cmd == 3)
                stergeStudent();
            else if(cmd == 4)
                findStudent();
            else if(cmd == 5)
                afisStudenti();
            else if(cmd == 6)
                filterStudentByGroup();
            else if(cmd == 7)
                filterStudentByTeacher();
            else if(cmd == 8)
                filterStudentByFirstLetter();
            else if(cmd == 0)
                return;
            else
                System.out.println("Comanda invalida!");
        }
    }

    public void meniuTeme() {
        while(true) {
            System.out.println("Meniu\n");
            System.out.println("1.Adauga tema");
            System.out.println("2.Update tema");
            System.out.println("3.Update deadline");
            System.out.println("4.Sterge tema");
            System.out.println("5.Cauta tema");
            System.out.println("6.Toate temele");
            System.out.println("7.Filtreaza teme dupa deadline");
            System.out.println("8.Filtreaza teme dupa deadline mai mic");
            System.out.println("9.Filtreaza teme dupa deadline mai mare");
            System.out.println("0.Iesire");
            System.out.println("Dati comanda: ");
            Scanner scanner = new Scanner(System.in);
            Integer cmd = Integer.parseInt(scanner.nextLine());
            if(cmd == 1)
                saveTema();
            else if(cmd == 2)
                updateTema();
            else if(cmd == 3)
                updateDeadline();
            else if(cmd == 4)
                stergeTema();
            else if(cmd == 5)
                findTema();
            else if(cmd == 6)
                afisTeme();
            else if(cmd == 7)
                filterByDeadline();
            else if(cmd == 8)
                filterByLowerDeadline();
            else if(cmd == 9)
                filterByHigherDeadline();
            else if(cmd == 0)
                return;
            else
                System.out.println("Comanda invalida!");
        }
    }

    public void meniuNote() {
        while(true) {
            System.out.println("Meniu\n");
            System.out.println("1.Adauga nota");
            System.out.println("2.Update nota");
            System.out.println("3.Sterge nota");
            System.out.println("4.Cauta nota");
            System.out.println("5.Toate notele");
            System.out.println("6.Filtreaza note dupa valoare");
            System.out.println("7.Filtreaza note dupa id-ul studentului");
            System.out.println("8.Filtreaza note dupa numarul temei");
            System.out.println("0.Iesire");
            System.out.println("Dati comanda: ");
            Scanner scanner = new Scanner(System.in);
            Integer cmd = Integer.parseInt(scanner.nextLine());
            if(cmd == 1)
                saveNota();
            else if(cmd == 2)
                updateNota();
            else if(cmd == 3)
                stergeNota();
            else if(cmd == 4)
                findNota();
            else if(cmd == 5)
                afisNota();
            else if(cmd == 6)
                filterByValoare();
            else if(cmd == 7)
                filterByStudent();
            else if(cmd == 8)
                filterByNrHomework();
            else if(cmd == 0)
                return;
            else
                System.out.println("Comanda invalida!");
        }
    }
}
