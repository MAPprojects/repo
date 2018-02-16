//import Domain.Note;
//import Domain.Studenti;
//import Domain.Teme;
//import Repository.*;
//import Service.Service;
//import UI.UI;
//public class Main {
//
//    public static void main(String[] args) throws ValidationException {
//        System.out.println("Hello World!");
//        String nume="   ";
//        System.out.println(nume.length());
//        System.out.println(nume);
//        System.out.println(nume.trim().isEmpty());
//        System.out.println(nume.length());
//
//
//    IRepository<Studenti> repoStudenti = new StudentRepoFIle(new StudentValidator(), "./src/Studenti.txt");
//
//    IRepository<Teme> repoTeme = new TemeFileRepo(new TemeValidator(), "./src/Teme.txt");
//    IRepository<Note> noteRepo =new NoteFileRepo(new NoteValidate(),"./src/Catalog.txt");
//        Service ctr=new Service(noteRepo,repoTeme,repoStudenti);
//
//        UI ui=new UI(ctr);
//        ui.run();
//
//
////        Studenti stud1 = new Studenti(1, "gigi", 224, "dsadas@dsada.com", "leda");
////
////        Studenti stud2 = new Studenti(2, "gigi1", 224, "dsadas@dsada.com", "leda");
////        Studenti stud3 = new Studenti(3, "gigi2", 224, "dsadas@dsadsssssa.com", "leda1");
////        IRepository<Studenti> repo=new Repository(new StudentValidator()) ;
////
////        try {
////            repo.save(stud1);
////            repo.save(stud2);
////            repo.save(stud3);
////        } catch (ValidationException e) {
////            System.out.println(e);
////        }
////
////        Iterable<Studenti> it = repo.getAll();
////        for (Studenti mt : it) {
////            System.out.println(mt);
////        }
////        repo.delete(stud1);
////
////        Iterable<Studenti> it1 = repo.getAll();
////        for (Studenti mt : it1) {
////            System.out.println(mt);
////        }
////        repo.update(stud2, stud1);
////        Iterable<Studenti> it2 = repo.getAll();
////        for (Studenti mt : it2) {
////            System.out.println(mt);
////        }
////        Teme t1 = new Teme(1, 4, "lab2");
////
////        IRepository<Teme> repoTeme = new Repository<Teme>(new TemeValidator());
////        try {
////            repoTeme.save(t1);
////
////        } catch (ValidationException e) {
////            System.out.println(e);
////        }
////
////        Iterable<Teme> itt = repoTeme.getAll();
////        for (Teme mt : itt) {
////            System.out.println(mt);
////        }
////
////        IRepository<Studenti> repo2 = new Repository<Studenti>(new StudentValidator());
////        StudService studS = new StudService(repo2);
////        System.out.println(studS.size());
////        try {
////
////            studS.save(stud2);
////            studS.save(stud2);
////        } catch (ValidationException e) {
////            System.out.println(e);
////        }
////        System.out.println(studS.size());
////
//////    }
////        Teme t1 = new Teme(1, 4, "lab2");
////
////        IRepository<Teme> repoTeme = new Repository<Teme>(new TemeValidator());
////        try {
////            repoTeme.save(t1);
////
////        } catch (ValidationException e) {
////            System.out.println(e);
////        }
////
////        Iterable<Teme> itt = repoTeme.getAll();
////        for (Teme mt : itt) {
////            System.out.println(mt);
////        }
//    }
//    }
