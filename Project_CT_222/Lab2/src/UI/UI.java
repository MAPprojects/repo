package UI;

import Domain.Nota;
import Domain.Studenti;
import Domain.Teme;
import Repositories.RepositoryException;
import Service.Service;
import Validators.ValidatorException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UI {
   /* private Service service;
    public UI(Service serv){
        service=serv;
    }

    private void printMenu(){
        System.out.println("~~~~~~COMENZI~~~~~~");
        System.out.println(" 0. EXIT");
        System.out.println(" 1. Adauga student");
        System.out.println(" 2. Afisare studenti");
        System.out.println(" 3. Sterge student");
        System.out.println(" 4. Modifica student");
        System.out.println(" 5. Cauta student");
        System.out.println(" 6. Adauga tema");
        System.out.println(" 7. Afisare teme");
        System.out.println(" 8. Sterge tema");
        System.out.println(" 9. Modifica tema");
        System.out.println("10. Cauta tema");
        System.out.println("11. Adauga nota");
        System.out.println("12. Modifica nota");
        System.out.println("13. Afiseaza notele");
        System.out.println("14. Filtrare de studenti");
        System.out.println("15. Filtrare de teme");
        System.out.println("16. Filtrare note");
    }
    private void printStudenti(){
        System.out.println("~~~~~~~~~~~~~Domain.Studenti~~~~~~~~~~~~~");
        Iterable<Studenti> all=service.getAllStudenti();
        for(Studenti stud:all){
            System.out.println("ID: "+stud.getId()+"    Nume: "+stud.getNume()+"    Grupa: "+stud.getGrupa()+"    E-mail: "+stud.getEmail()+"    Cadru didactic: "+stud.getCadruDidactic());
        }
        System.out.println("\n\n");
    }
    private void FiltrareStudenti(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti dupa ce doriti sa filtrati studentul (profesor,grupa,email)");
        String cmd=scanner.nextLine();
        if(cmd.equals("profesor")){
            System.out.print("Introduceti numele cadrului didactic: ");
            String nume=scanner.nextLine();
            List<Studenti> l=service.filterCadruDidactic(nume);
            l.forEach(x -> System.out.println("    " + x));
        }
        if(cmd.equals("email")){
            System.out.print("Introduceti email providerul: ");
            String email=scanner.nextLine();
            List<Studenti> l=service.filterEmail(email);
            l.forEach(x -> System.out.println("    " + x));
        }
        if(cmd.equals("grupa")){
            System.out.print("Introduceti grupa: ");
            int grupa=scanner.nextInt();
            List<Studenti> l=service.filterGrupa(grupa);
            l.forEach(x -> System.out.println("    " + x));
        }
    }
    private void FiltrareTeme(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti dupa ce doriti sa filtrati tema (deadline,cerinta,deadlineIntre)");
        String cmd=scanner.nextLine();
        if(cmd.equals("deadline")){
            System.out.print("Introduceti deadlineul: ");
            int deadline=scanner.nextInt();
            List<Teme> l=service.filterDeadline(deadline);
            l.forEach(x -> System.out.println("    " + x));
        }
        if(cmd.equals("cerinta")){
            System.out.print("Introduceti cerinta: ");
            String cerinta=scanner.nextLine();
            List<Teme> l=service.filterCerinta(cerinta);
            l.forEach(x -> System.out.println("    " + x));
        }
        if(cmd.equals("deadlineIntre")) {
            System.out.print("Introduceti limita inferioara a deadlineului: ");
            int d1 = scanner.nextInt();
            System.out.print("Introduceti limita superioara a deadlineului: ");
            int d2 = scanner.nextInt();
            List<Teme> l=service.filterDeadlineBetween(d1, d2);
            l.forEach(x -> System.out.println("    " + x));
        }
    }
    private void FiltrareNote(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti dupa ce doriti sa filtrati nota (IdStudent,valoareSub,IdTema)");
        String cmd=scanner.nextLine();
        if(cmd.equals("IdStudent")){
            System.out.print("Introduceti id-ul studentului: ");
            int id=scanner.nextInt();
            List<Nota> l=service.filterIdStudent(id);
            l.forEach(x -> System.out.println("    " + x));
        }
        if(cmd.equals("IdTema")){
            System.out.print("Introduceti id-ul temei: ");
            int id=scanner.nextInt();
            List<Nota> l=service.filterIdTema(id);
            l.forEach(x -> System.out.println("    " + x));
        }
        if(cmd.equals("valoareSub")) {
            System.out.print("Introduceti valoarea fata de care doriti sa fie mai mici notele: ");
            int d1 = scanner.nextInt();
            List<Nota> l=service.filterSubNota(d1);
            l.forEach(x -> System.out.println("    " + x));
        }
    }
    private void printTeme(){
        System.out.println("~~~~~~~~~~~~~Domain.Teme~~~~~~~~~~~~~");
        Iterable<Teme> all=service.getAllTeme();
        for(Teme tema:all){
            System.out.println("Numar Tema: "+tema.getId()+"    Cerinta: "+tema.getCerinta()+"    Deadline: "+tema.getDeadline());
        }
        System.out.println("\n\n");
    }
    private void printNote(){
        System.out.println("~~~~~~~~~~~~~Note~~~~~~~~~~~~~");
        Iterable<Nota> all=service.getAllNote();
        for(Nota nota:all){
            System.out.println("ID nota: "+nota.getId()+"    StudentID: "+nota.getIdStudent()+"    TemaID: "+nota.getIdTema()+"    Valoare: "+nota.getValoare());
        }
        System.out.println("\n\n");
    }
    private void addStudent(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti id-ul studentului: ");
        int id=scanner.nextInt();
        System.out.print("Introduceti numele studentului: ");
        scanner.nextLine();
        String nume=scanner.nextLine();
        System.out.print("Introduceti grupa studentului: ");
        int grupa=scanner.nextInt();
        System.out.println("Introduceti e-mailul studentului: ");
        scanner.nextLine();
        String email=scanner.nextLine();
        System.out.println("Introduceti cadrul didactic al studentului: ");
        //scanner.nextLine();
        String cadruDidactic=scanner.nextLine();
        service.addStudent(id,nume,grupa,email,cadruDidactic);
        System.out.println("Studentul cu id-ul "+id+" a fost adaugat");
    }
    private void addTema(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti numarul temei: ");
        int NumarTema=scanner.nextInt();
        System.out.print("Introduceti cerinta temei: ");
        scanner.nextLine();
        String Cerinta=scanner.nextLine();
        System.out.print("Introduceti deadlineul temei: ");
        int Deadline=scanner.nextInt();
        service.addTema(NumarTema,Cerinta,Deadline);
        System.out.println("Tema cu numarul "+NumarTema+" a fost adaugata");
    }

    private void addNota(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti id-ul notei: ");
        int id=scanner.nextInt();
        System.out.print("Introduceti id-ul studentului: ");
        int studID=scanner.nextInt();
        System.out.print("Introduceti numarul temei: ");
        int TemaID=scanner.nextInt();
        System.out.print("Introduceti valoarea notei: ");
        int valoare=scanner.nextInt();
        System.out.print("Introduceti saptamana predarii temei: ");
        int saptamanaPredare=scanner.nextInt();
        System.out.print("Introduceti observatii: ");
        scanner.nextLine();
        String observatii=scanner.nextLine();

        service.adaugareNota(id,studID,TemaID,valoare,saptamanaPredare,observatii);
        System.out.println("Domain.Nota cu id-ul "+id+" a fost adaugata");
    }
    private void stergeStudent(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti id-ul studentului: ");
        int id=scanner.nextInt();
        Optional<Studenti> st=service.removeStudent(id);
        if(st.isPresent())
            System.out.println("Student-ul "+st.toString()+" a fost sters.");
        else
            System.out.println("Studentul nu exista");
    }
    private void findStudent(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti id-ul studentului: ");
        int id=scanner.nextInt();
        Studenti st=service.cautaStudent(id);
        System.out.println("Studentul este: "+st.toString());
    }
    private void findTema(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti numarul temei: ");
        int id=scanner.nextInt();
        Teme t=service.cautaTema(id);
        System.out.println("Tema este: "+t.toString());
    }
    private void stergeTema(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti numarul: ");
        int id=scanner.nextInt();
        Optional<Teme> st=service.removeTema(id);
        if(st.isPresent())
            System.out.println("Tema "+st.toString()+" a fost stearsa.");
        else
            System.out.println("Tema nu exista");

    }
    private void modificaStudent(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti id-ul studentului: ");
        int id = scanner.nextInt();
        System.out.print("Introduceti campul pe care doriti sa il modificati: ");
        scanner.nextLine();
        String filtru=scanner.nextLine();
        System.out.print("Introduceti noua valoare a campului: ");
        //scanner.nextLine();
        String valoare=scanner.nextLine();
        service.updateStudent(id,filtru,valoare);
        System.out.println("Studentul "+id+" a fost modificat");
    }
    private void modificaTema(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti numarul temei: ");
        int id = scanner.nextInt();
        System.out.print("Introduceti campul pe care doriti sa il modificati: ");
        scanner.nextLine();
        String filtru=scanner.nextLine();
        System.out.print("Introduceti noua valoare a campului: ");
        scanner.nextLine();
        String valoare=scanner.nextLine();
        service.updateTeme(id,filtru,valoare);
        System.out.println("Tema "+id+" a fost modificata");
    }
    private void modificaNota(){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Introduceti id-ul notei: ");
        int id=scanner.nextInt();
        System.out.print("Introduceti noua valoare a notei: ");
        int valoare=scanner.nextInt();
        System.out.print("Introduceti saptamana predarii noii iteratii: ");
        int saptamanaPredare=scanner.nextInt();
        System.out.print("Introduceti observatii: ");
        scanner.nextLine();
        String observatii=scanner.nextLine();
        service.updateNota(id,valoare,saptamanaPredare,observatii);
        System.out.println("Domain.Nota "+id+" a fost modificata");
    }
    public void testApp(){
        service.getCurrentWeek();
        service.addStudent(1,"Alexandru",2,"alexandru@yahoo.com","profesor1");
        service.addStudent(2,"Mihai",3,"mihai@yahoo.com","profesor2");
        service.addStudent(3,"Laurentiu",4,"laurentiu@yahoo.com","profesor3");
        service.addStudent(4,"Popescu",5,"popescu@yahoo.com","profesor4");
        service.addStudent(5,"Octavian",6,"octavian@yahoo.com","profesor5");
        service.removeStudent(4);
        try
        {
            service.removeStudent(12);
        }
        catch (RepositoryException ex){
            System.out.println(ex.getMessage());
        }

        service.addTema(1,"desc1",4);
        service.addTema(2,"desc2",8);
        service.addTema(3,"desc3",6);
        service.addTema(4,"1",12);
        Iterable<Studenti> all=service.getAllStudenti();
        for (Studenti st: all){
            System.out.println(st);
        }
        System.out.println("teme");
        Iterable<Teme> teme=service.getAllTeme();
        for (Teme t:teme){
            System.out.println(t);
        }
        System.out.println("teme modificate");
        try{
        service.updateTeme(1,"deadline","10");
        }
        catch (RepositoryException ex) {
         System.out.println(ex.getMessage());
        }
        service.updateTeme(2,"deadline","10");
        try{
            service.updateTeme(3,"deadline","29");
        }catch (ValidatorException ex){
            System.out.println(ex.getMessage());
        }
        teme=service.getAllTeme();
        for(Teme t: teme){
            System.out.println(t);
        }
        try{
            service.adaugareNota(7,8,9,13,14,"Frumos lucrat");
        }
        catch (RepositoryException ex) {
            System.out.println(ex.getMessage());
        }
        try{
            service.adaugareNota(7,1,9,13,14,"Urat lucrat");
        }
        catch (RepositoryException ex) {
            System.out.println(ex.getMessage());
        }
        try{
            service.adaugareNota(7,1,2,13,14,"Mediocru lucrat");
        }
        catch (RepositoryException ex) {
            System.out.println(ex.getMessage());
        }
        try{
            service.adaugareNota(7,1,2,5,15,"Multe greseli de sintaxa + Nu ruleaza");
        }
        catch (RepositoryException ex) {
            System.out.println(ex.getMessage());
        }
        service.adaugareNota(1,1,1,8,1,"Bunicel, lipsesc anumite validari");
        service.adaugareNota(2,2,2,5,10,"Functionalitati lipsa");

        for(Nota n: service.getAllNote()){
            System.out.println(n);
        }
        service.updateNota(2,10,11,"Prezinta toate functionalitatile");
        for(Nota n: service.getAllNote()){
            System.out.println(n);
        }
    }
    public void run(){
        System.out.println("Aplicatia a pornit");
        Scanner scanner=new Scanner(System.in);
        while(true){
            System.out.println("Introduceti 30 pentru afisarea comenzilor");
            System.out.println("Introduceti comanda: ");
            try{
                int cmd=scanner.nextInt();
                switch(cmd){
                    case(1):
                        addStudent();
                        break;
                    case(2):
                        printStudenti();
                        break;
                    case(3):
                        stergeStudent();
                        break;
                    case(4):
                        modificaStudent();
                        break;
                    case(5):
                        findStudent();
                        break;
                    case(6):
                        addTema();
                        break;
                    case(7):
                        printTeme();
                        break;
                    case(8):
                        stergeTema();
                        break;
                    case(9):
                        modificaTema();
                        break;
                    case(10):
                        findTema();
                        break;
                    case(11):
                        addNota();
                        break;
                    case(12):
                        modificaNota();
                        break;
                    case(13):
                        printNote();
                        break;
                    case(14):
                        FiltrareStudenti();
                        break;
                    case(15):
                        FiltrareTeme();
                        break;
                    case(16):
                        FiltrareNote();
                        break;
                    case(30):
                        printMenu();
                        break;
                    case(0):
                        return;
                    default:
                        System.out.println("Comanda invalida!");
                }
            }catch (InputMismatchException ex) {
                System.err.println(ex);
                scanner.nextLine();
            } catch (RepositoryException re) {
                System.err.println(re);
            } catch (ValidatorException ve) {
                System.err.println(ve);
            }catch (NullPointerException e){
                System.err.println("eroare");
            }
        }
        //testApp();
    }*/
}
