//package UI;
//
//import Domain.Note;
//import Domain.Studenti;
//import Domain.Teme;
//import Repository.ValidationException;
//import Service.Service;
//
//import java.util.List;
//import java.util.Scanner;
//
//public class UI {
//    private Service ctr;
//
//    public UI(Service ctr){
//        this.ctr=ctr;
//    }
//
//    public void run(){
//        while (meniu());
//        System.out.println("End...");
//    }
//
//    private boolean meniu(){
//        System.out.println("-----Meniu------");
//        System.out.println("1. Adauga student");
//        System.out.println("2. Sterge student");
//        System.out.println("3. Modifica student");
//        System.out.println("4. Adauga tema");
//        System.out.println("5. Modifica tema");
//        System.out.println("6. Sterge o tema");
//        System.out.println("7. Da o nota");
//        System.out.println("8. Modifica nota");
//        System.out.println("9.Filtru pentru studenti: studentii profesorului:");
//        System.out.println("10.Filtru pentru stundetii sectiei info romana:");
//        System.out.println("11.Filtru pentru emil-ul studentului:");
//        System.out.println("12.Filtru pentru deadline-ul temei mai mic decat o valoare:");
//        System.out.println("13.Filtru pentru deadline-ul temei cuprins intre 2 valori");
//        System.out.println("14.Filtru pentru descrierea temei");
//        System.out.println("15.Filtru pentru afisare tututor notelor unui student");
//        System.out.println("16.Filtru pentru afisarea studentilor restanti:");
//        System.out.println("17.Filtru pentru afisare tuturoru stundetilor care au nota peste o valoare");
//        System.out.println("18. Afiseza toate datele");
//        System.out.println("0. Exit");
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Introduceti o comanda: ");
//        String comanda=scanner.nextLine();
//        //Integer comanda=scanner.nextInt();
//        Integer nrComanda=-1;
//        try{
//            nrComanda=Integer.parseInt(comanda);
//            if (nrComanda==1)
//                addStudent();
//            if (nrComanda==2)
//                deleteStudent();
//            if (nrComanda==3)
//                updateStudnet();
//            if (nrComanda==4)
//                addTema();
//            if (nrComanda==5)
//                updateTema();
//            if (nrComanda==6)
//                delteTema();
//            if (nrComanda==7)
//                addNota();
//            if (nrComanda==8)
//                updateNota();
//            if (nrComanda==9)
//                filtruStudentIndrumator();
//            if(nrComanda==10)
//                filtruStudentSectie();
//            if(nrComanda == 11)
//                filtruStudentEmail();
//            if (nrComanda == 12)
//                filtruTemaDeadlineMaiMic();
//            if(nrComanda==13)
//                filtruTemaDeadlineCuprins();
//            if(nrComanda==14)
//                filtruTemaDescriere();
//            if(nrComanda==15)
//                filtruNoteIdStudent();
//            if(nrComanda==16)
//                filtruNoteStudentiRestanti();
//            if (nrComanda==17)
//                fittruNoteStundetiMaiMare();
//            if (nrComanda==18)
//                printAll();
//        }catch (Exception e){
//            System.out.println("Comanda ivalida!");
//        }
//        return (nrComanda!=0);
//
//    }
//
//    private void addStudent(){
//
//        try {
//            Scanner scanner = new Scanner(System.in);
//
//            System.out.println("Introduceti datele pentru noul student");
//            System.out.print("Introduceti id-ul studentului: ");
//            int idSudent=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti numele studentului ");
//            String nume=scanner.nextLine();
//            System.out.print("Introduce grupa studnetului: ");
//            int grupa=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti emailul stundetlui: ");
//            String email=scanner.nextLine();
//            System.out.print("Introduceti numele cadrului didactic indrumator: ");
//            String cadru_didactic=scanner.nextLine();
//            ctr.saveStudeti(idSudent,nume,grupa,email,cadru_didactic);
//        }catch (ValidationException e){
//            System.out.println(e);
//        }catch (Exception e){
//            System.out.println("Ceva nu a fost bine!");
//        }
//    }
//
//    private void updateStudnet(){
//        try {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Introduceti datele pentru studentul ce doriti sa il modificati");
//            System.out.print("Introduceti id-ul studentului pe care vreti sa il schimbati: ");
//            int idVechi=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti id-ul studentului: ");
//            int idSudent=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti numele studentului ");
//            String nume=scanner.nextLine();
//            System.out.print("Introduce grupa studnetului: ");
//            int grupa=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti emailul stundetlui: ");
//            String email=scanner.nextLine();
//            System.out.print("Introduceti numele cadrului didactic indrumator: ");
//            String cadru_didactic=scanner.nextLine();
//            ctr.updateStudent(idVechi,idSudent,nume,grupa,email,cadru_didactic);
//        } catch (Exception e){
//            System.out.println("Ceva nu a fost bine!");
//        } catch (ValidationException validationException) {
//            validationException.printStackTrace();
//        }
//    }
//
//    private void deleteStudent(){
//        try{
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Introduceti id-ul stundetului ce doriti sa il stergeti: ");
//            int idStudent=Integer.parseInt(scanner.nextLine());
//            ctr.stergeStudent(idStudent);
//        }catch (Exception e){
//            System.out.println("Ceva nu a fost bine!");
//        }
//    }
//
//
//
//    private void addTema(){
//        try {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Introduceti datele pentru noua tema:");
//            System.out.print("Introduceti id-ul temei: ");
//            int nrTema=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti descrierea temei");
//            String descriere=scanner.nextLine();
//            System.out.print("Introduceti deadline: ");
//            int deadline=Integer.parseInt(scanner.nextLine());
//            ctr.saveTema(nrTema,deadline,descriere);
//        }catch (ValidationException e){
//            System.out.println(e);
//        }catch (Exception e){
//            System.out.println("Ceva nu a fost bine!");
//        }
//    }
//
//    private void updateTema(){
//        try {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Introduceti datele pentru tema ce doriti sa o modificati:");
//            System.out.print("Introduceti id-ul  vechi al temei: ");
//            int nrTemaVechi=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti id-ul temei: ");
//            int nrTema=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti deadline: ");
//            int deadline=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti observatii: ");
//            String observatie=scanner.nextLine();
//            ctr.updateTema(nrTemaVechi,nrTema,deadline,observatie);
//        }catch (ValidationException e){
//            System.out.println(e);
//        }catch (Exception e){
//            System.out.println("Ceva nu a fost bine!");
//        }
//    }
//    private void delteTema(){
//        try {
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Introduceti id-ul temei pentru a fi stearsa: ");
//            int nrTema=Integer.parseInt(scanner.nextLine());
//
//
//            ctr.stergeTema(nrTema);
//            System.out.println("Teme a fost stearsa");
//        }catch (Exception e){
//            System.out.println("Ceva nu a fost bine!");
//        }
//    }
//
//
//
//
//    private void addNota(){
//        try {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Introduceti datele mentru noua nota");
//            System.out.print("Introduceti id-ul studentului: ");
//            int idSudent=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti id-ul temei: ");
//            int idTema=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti valoarea notei: ");
//            int nota=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti observatii notei: ");
//            String Obs=scanner.nextLine();
//            System.out.print("Introduceti saptaman predari notei ");
//            int saptamanPredare=Integer.parseInt(scanner.nextLine());
//
//            ctr.save(idSudent,idTema,nota,Obs,saptamanPredare);
//        }catch (Exception e){
//            System.out.println("Ceva nu a fost bine!");
//        }
//    }
//
//    private void updateNota(){
//        try {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Introduceti datele pentru nota ce doriti sa o adaugati ");
//            System.out.print("Introduceti id-ul studentului: ");
//            int idSudent=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti id-ul temei: ");
//            int idTema=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti valoarea notei ");
//            int nota=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti saptaman predarii ");
//            int saptamanPredare=Integer.parseInt(scanner.nextLine());
//            System.out.print("Introduceti observatii");
//            String observatii=scanner.nextLine();
//            ctr.modificare(idSudent,idTema,nota,saptamanPredare,observatii);
//        }catch (Exception e){
//            System.out.println("Ceva nu a fost bine!");
//        }
//    }
//
//    public  void filtruStudentIndrumator(){
//        try{
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Introduceti numele profesorului a carui studenti vreti sa ii vizualizati");
//            String prof=scanner.nextLine();
//            List<Studenti> stud=ctr.filtrareNumeIndrumator(prof);
//            System.out.println(stud);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//
//    public  void filtruStudentSectie(){
//        try{
//            System.out.println("Toti studentii de la sectia info romana:");
//            List<Studenti> stud=ctr.filtrareSectie();
//            System.out.println(stud);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//
//    public  void filtruStudentEmail(){
//        try{
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Introduceti tipul de email al studentilor ce doriti a fi afisati");
//            String email=scanner.nextLine();
//            List<Studenti> stud=ctr.filtrareStudentEmail(email);
//            System.out.println(stud);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//
//    public  void filtruTemaDeadlineMaiMic(){
//        try{
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Introduceti deadlinul pana cand trebuie predate temele pe care doriti sa le vizualizati");
//            int deadline=Integer.parseInt(scanner.nextLine());
//            List<Teme> teme=ctr.filtrareTemeDeadline(deadline);
//            System.out.println(teme);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//    public  void filtruTemaDeadlineCuprins(){
//        try{
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Introduceti perioada din care vreti sa afisati temele:");
//
//            System.out.print("Saptamana de inceput");
//            int deadlineInceput=Integer.parseInt(scanner.nextLine());
//
//            System.out.print("Saptamana de final");
//            int deadlineFinal=Integer.parseInt(scanner.nextLine());
//
//            List<Teme> teme=ctr.filtrareTemeDeadlineCuprins(deadlineInceput,deadlineFinal);
//            System.out.println(teme);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//    public  void filtruTemaDescriere(){
//        try{
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("introduceti ce doriti sa se gaseasca in descrierea temei");
//            String descriere=scanner.nextLine();
//            List<Teme> teme=ctr.filtrareTemeDescriere(descriere);
//            System.out.println(teme);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//    public  void filtruNoteIdStudent(){
//        try{
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Introduceti id-ul studentului a carui note vreti sa le vedeti: ");
//            int id=Integer.parseInt(scanner.nextLine());
//            List<Note> note=ctr.filtrareNoteIdStudent(id);
//            System.out.println(note);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//
//
//    public  void filtruNoteStudentiRestanti(){
//        try{
//            System.out.println("Toti studentii care au picat");
//            List<Note> note=ctr.filtrareNoteStundetiPicati();
//            System.out.println(note);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//    public  void fittruNoteStundetiMaiMare(){
//        try{
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Introduceti nota pana la care sa se afiseze stundetii: ");
//            int valoare=Integer.parseInt(scanner.nextLine());
//            List<Note> note=ctr.filtrareNoteMaiMare(valoare);
//            System.out.println(note);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//
//    public void printAllStudenti(){
//        System.out.println("Studenti:");
//        System.out.println("==========================");
//        Iterable<Studenti> it = ctr.getAllStudents();
//        for (Studenti st : it) {
//            System.out.println(st);
//        }
//        System.out.println("==========================");
//    }
//
//    public void printAllTeme(){
//        System.out.println("Teme:");
//        System.out.println("==========================");
//        Iterable<Teme> it = ctr.getAllTeme();
//        for (Teme te : it) {
//            System.out.println(te);
//        }
//        System.out.println("==========================");
//    }
//
//
//    public void printALlNote(){
//        System.out.println("Note:");
//        System.out.println("==========================");
//        Iterable<Note> it = ctr.getAllNote();
//        for (Note  nt : it) {
//            System.out.println(nt);
//        }
//        System.out.println("==========================");
//    }
//    public void printAll(){
//        printAllStudenti();
//        printAllTeme();
//        printALlNote();
//    }
//}
