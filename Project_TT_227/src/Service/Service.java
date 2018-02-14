package Service;

import Domain.Globals;
import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Exceptions.RepositoryException;
import Repositories.*;
import Utils.Observable;
import Utils.Observer;
import Utils.StudentEvent;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service {

    private SQLStudentRepo sqlStudentRepo;
    private SQLTemaRepo sqlTemaRepo;
    private SQLNotaRepo sqlNotaRepo;

    public Service(SQLStudentRepo sqlStudentRepo, SQLTemaRepo sqlTemaRepo, SQLNotaRepo sqlNotaRepo)
    {
        this.sqlStudentRepo = sqlStudentRepo;
        this.sqlTemaRepo = sqlTemaRepo;
        this.sqlNotaRepo = sqlNotaRepo;
    }

    public void addStudent(int idStudent, String nume, int grupa, String email, String profLab) throws RepositoryException {
        Student student = new Student(idStudent,nume,grupa,email,profLab);
        try {
            sqlStudentRepo.save(student);
        } catch (RepositoryException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void addTema( int nrTema, String descriere, int deadline, boolean sendEmail) throws RepositoryException {
        Tema tema = new Tema(nrTema,descriere,deadline);
        try {
            sqlTemaRepo.save(tema);
            if(sendEmail) {
                for (Student st : sqlStudentRepo.findAll()) {
                    EmailSender email = new EmailSender("smtp.gmail.com", "tudortritean@gmail.com", "Iwillbethebest1", st.getEmail(), "tudortritean@gmail.com", "Tema noua", "S-a adaugat o tema noua cu datele urmatoare: Numar: " + tema.getID() + ", Descriere: " + tema.getDescriere() + ", Deadline: " + tema.getDeadline());
                    email.send();
                }
            }
        }
        catch (RepositoryException e)
        {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void modificareTermen(int nrTema, int newDeadline, boolean sendEmail) throws RepositoryException {
        Tema tema = null;
        try {
            tema = sqlTemaRepo.findEntity(nrTema).get();
            if(tema.getDeadline() < newDeadline && tema.getDeadline() > Globals.getInstance().getSaptCurenta() && newDeadline <= 14) {
                sqlTemaRepo.update(nrTema,new Tema(nrTema,tema.getDescriere(),newDeadline));
                if(sendEmail) {
                    for (Student st : sqlStudentRepo.findAll()) {
                        EmailSender email = new EmailSender("smtp.gmail.com", "tudortritean@gmail.com", "Iwillbethebest1", st.getEmail(), "tudortritean@gmail.com", "Modificare termen tema", "S-a modificat termenul unei teme cu datele urmatoare: Numar: " + tema.getID() + ", Descriere: " + tema.getDescriere() + ", Deadline: " + tema.getDeadline());
                        email.send();
                    }
                }
            }
            else
                throw new RepositoryException("Noul deadline nu respecta conditiile!");
        } catch (RepositoryException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void adaugaNota(int idStudent, int nrTema, int vnota) throws RepositoryException {
        try{
            Student student = sqlStudentRepo.findEntity(idStudent).get();
            Tema tema = sqlTemaRepo.findEntity(nrTema).get();
            if(tema.getDeadline()<Globals.getInstance().getSaptCurenta())
                vnota = vnota - (Globals.getInstance().getSaptCurenta() - tema.getDeadline()) * 2;
            if(vnota < 1)
                vnota = 1;
            Nota nota = new Nota(idStudent, nrTema, vnota);
            sqlNotaRepo.save(nota);
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("/src/Output/" + student.getID() + ".txt");
            File file = new File(filePath);
            if(!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            String buffer="Adaugare nota, nrTema: " + nrTema + ", Nota: " + vnota + ", Deadline: " + tema.getDeadline() + ", Saptamana predarii: " + Globals.getInstance().getSaptCurenta() + ". ";
            if(tema.getDeadline() < Globals.getInstance().getSaptCurenta())
                buffer += "Observatii: Nota a fost scazuta cu " + (Globals.getInstance().getSaptCurenta() - tema.getDeadline())*2 + " datorita intarzierii! Nota initiala: " + (vnota+(Globals.getInstance().getSaptCurenta() - tema.getDeadline())*2);
            else buffer += "Observatii: -";
            buffer += "\n";
            bw.write(buffer);
            bw.close();
        } catch (RepositoryException e) {
            throw new RepositoryException(e.getMessage());
        } catch (IOException IOe) {
            System.out.println(IOe.getMessage());
        }
    }

    public void modificareNota(int idNota, int vnota, boolean sendEmail) throws RepositoryException {
        try {
            Nota nota = sqlNotaRepo.findEntity(idNota).get();
            String buffer = "Modificare nota, Tema: " + nota.getNrTema() + ", Nota: " + nota.getNota() + ", Deadline: " + sqlTemaRepo.findEntity(nota.getNrTema()).get().getDeadline() + ", Saptamana predarii: " + Globals.getInstance().getSaptCurenta() + ". Observatii: ";
            if(vnota <= 10) {
                if (sqlTemaRepo.findEntity(nota.getNrTema()).get().getDeadline() < Globals.getInstance().getSaptCurenta()) {
                    vnota = vnota - (Globals.getInstance().getSaptCurenta() - sqlTemaRepo.findEntity(nota.getNrTema()).get().getDeadline()) * 2;
                    buffer += "Nota a fost scazuta cu " + ((Globals.getInstance().getSaptCurenta() - sqlTemaRepo.findEntity(nota.getNrTema()).get().getDeadline()) * 2) + " puncte datorita intarzierii. ";
                }
                if (vnota < 1) {
                    vnota = 1;
                    buffer += "Nota este 1 datorita unei intarzieri prea mari. \n";
                    throw new RepositoryException("Nota: (1-10)");
                }
                if (vnota > nota.getNota()) {
                    sqlNotaRepo.update(idNota, new Nota(nota.getIdStudent(), nota.getNrTema(), vnota));
                    if (sendEmail) {
                        for (Student st : sqlStudentRepo.findAll()) {
                            if(st.getID() == nota.getIdStudent()) {
                                EmailSender email = new EmailSender("smtp.gmail.com", "tudortritean@gmail.com", "Iwillbethebest1", st.getEmail(), "tudortritean@gmail.com", "Modificare nota", "Salut!\nNota ta la tema cu numarul " + nota.getNrTema() + " a fost modificata la " + vnota + "!");
                                email.send();
                            }
                        }
                    }
                }
                else buffer += "Nota nu a fost modificata deoarece este mai mare sau egala cu cea noua. Nota propusa: " + vnota + ". ";
            }
            else{
                throw new RepositoryException("Nota: (1-10)");
            }
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("/src/Output/" + nota.getIdStudent() + ".txt");
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            buffer += "\n";
            bw.write(buffer);
            bw.close();
        } catch (RepositoryException e) {
            throw new RepositoryException(e.getMessage());
        } catch (IOException IOe) {
            System.out.println(IOe.getMessage());
        }
    }

    public void removeStudent(int idStudent) throws RepositoryException {
        Iterable<Nota> allMarks = findAllMarks();
        try {
            //Stergere nota in cazul in care studentul a fost sters.
            for(Nota n : allMarks){
                if(idStudent == n.getIdStudent())
                    sqlNotaRepo.delete(n.getID());
            }
            sqlStudentRepo.delete(idStudent);
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("/src/Output/" + idStudent + ".txt");
            File file = new File(filePath);
            if(file.exists()) file.delete();
        } catch (RepositoryException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void updateStudent(int idStudent, String nume, int grupa, String email, String profLab) throws RepositoryException {
        Student newStudent = new Student(idStudent,nume,grupa,email,profLab);
        try {
            sqlStudentRepo.update(idStudent,newStudent);
        } catch (RepositoryException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void removeHomework(int nrTema) throws RepositoryException {
        Iterable<Nota> allMarks = findAllMarks();
        try {
            sqlTemaRepo.findEntity(nrTema);
            //Stergere nota in cazul in care tema a fost stearsa.
            for(Nota n : allMarks){
                if(nrTema == n.getNrTema())
                    sqlNotaRepo.delete(n.getID());
            }
            sqlTemaRepo.delete(nrTema);
        } catch (RepositoryException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void removeMark(int idMark) throws RepositoryException {
        try {
            sqlNotaRepo.findEntity(idMark);
            sqlNotaRepo.delete(idMark);
        } catch (RepositoryException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public Iterable<Student> findAllStudents(){
        return sqlStudentRepo.findAll();
    }

    public Iterable<Tema> findAllHomeworks(){
        return sqlTemaRepo.findAll();
    }

    public Iterable<Nota> findAllMarks(){
        return sqlNotaRepo.findAll();
    }

    public Integer noOfStudents() { return sqlStudentRepo.size(); }

    public Integer noOfHomeworks() { return sqlTemaRepo.size(); }

    public Integer noOfMarks() { return sqlNotaRepo.size(); }

    public static <E> List<E> filterAndSorter(List<E> lista, Predicate<E> predicat, Comparator<E> comparator) {
        List<E> newList = lista.stream().filter(predicat).collect(Collectors.toList());
        newList.sort(comparator);
        return newList;
    }

    public List<Student> filterByName(String nume){
        ArrayList<Student> all = new ArrayList<>();
        Iterable<Student> it = sqlStudentRepo.findAll();
        for(Student student : it)
            all.add(student);
        List<Student> allStudents = new ArrayList<>(all);
        Predicate<Student> predicate = (Student student) -> student.getNume().toLowerCase().contains(nume.toLowerCase());
        Comparator<Student> comparator = (Student st1, Student st2) -> (st1.getNume().compareTo(st2.getNume()));
        return filterAndSorter(allStudents, predicate, comparator);
    }

    public List<Student> filterByIndrumator(String profLab){
        ArrayList<Student> all = new ArrayList<>();
        Iterable<Student> it = sqlStudentRepo.findAll();
        for(Student student : it)
            all.add(student);
        List<Student> allStudents = new ArrayList<>(all);
        Predicate<Student> predicate = (Student student) -> student.getProfLab().toLowerCase().contains(profLab.toLowerCase());
        Comparator<Student> comparator = (Student st1, Student st2) -> (st1.getNume().compareTo(st2.getNume()));
        return filterAndSorter(allStudents, predicate, comparator);
    }

    public List<Student> filterByEmail(String emailProvider){
        ArrayList<Student> all = new ArrayList<>();
        Iterable<Student> it = sqlStudentRepo.findAll();
        for(Student student : it)
            all.add(student);
        List<Student> allStudents = new ArrayList<>(all);
        Predicate<Student> predicate = (Student student) -> {
            Integer poz = student.getEmail().indexOf("@");
            if(poz < 0)
                return false;
            String provider = student.getEmail().substring(poz);
            return provider.toLowerCase().contains(emailProvider.toLowerCase());
        };
        Comparator<Student> comparator = (Student st1, Student st2) -> (st1.getNume().compareTo(st2.getNume()));
        return filterAndSorter(allStudents, predicate, comparator);
    }

    public List<Tema> filterByDescriere(String descriere){
        ArrayList<Tema> all = new ArrayList<>();
        Iterable<Tema> it = sqlTemaRepo.findAll();
        for(Tema tema : it)
            all.add(tema);
        List<Tema> allHomeworks = new ArrayList<>(all);
        Predicate<Tema> predicate = (Tema tema) -> tema.getDescriere().toLowerCase().contains(descriere.toLowerCase());
        Comparator<Tema> comparator = (Tema t1, Tema t2) -> (t1.getDescriere().compareTo(t2.getDescriere()));
        return filterAndSorter(allHomeworks, predicate, comparator);
    }

    public List<Tema> filterByDeadline(Integer deadline){
        ArrayList<Tema> all = new ArrayList<>();
        Iterable<Tema> it = sqlTemaRepo.findAll();
        for(Tema tema : it)
            all.add(tema);
        List<Tema> allHomeworks = new ArrayList<>(all);
        Predicate<Tema> predicate = (Tema tema) -> (tema.getDeadline() < deadline);
        Comparator<Tema> comparator = (Tema t1, Tema t2) -> (t2.getDeadline() - t1.getDeadline());
        return filterAndSorter(allHomeworks, predicate, comparator);
    }

    public List<Tema> filterByNrTema(Integer nrTema){
        ArrayList<Tema> all = new ArrayList<>();
        Iterable<Tema> it = sqlTemaRepo.findAll();
        for(Tema tema : it)
            all.add(tema);
        List<Tema> allHomeworks = new ArrayList<>(all);
        Predicate<Tema> predicate = (Tema tema) -> (tema.getID() <= nrTema);
        Comparator<Tema> comparator = (Tema t1, Tema t2) -> (t1.getID() - t2.getID());
        return filterAndSorter(allHomeworks, predicate, comparator);
    }

    public List<Nota> filterByNota(Integer vnota){
        ArrayList<Nota> all = new ArrayList<>();
        Iterable<Nota> it = sqlNotaRepo.findAll();
        for(Nota nota : it)
            all.add(nota);
        List<Nota> allMarks = new ArrayList<>(all);
        Predicate<Nota> predicate = (Nota nota) -> (nota.getNota() == vnota);
        Comparator<Nota> comparator = (Nota n1, Nota n2) -> (n2.getNota() - n1.getNota());
        return filterAndSorter(allMarks, predicate, comparator);
    }

    public List<Nota> filterByStudent(Integer idStudent){
        ArrayList<Nota> all = new ArrayList<>();
        Iterable<Nota> it = sqlNotaRepo.findAll();
        for(Nota tema : it)
            all.add(tema);
        List<Nota> allMarks = new ArrayList<>(all);
        Predicate<Nota> predicate = (Nota nota) -> (nota.getIdStudent() == idStudent);
        Comparator<Nota> comparator = (Nota n1, Nota n2) -> (n2.getNota() - n1.getNota());
        return filterAndSorter(allMarks, predicate, comparator);
    }

    public List<Nota> filterByTema(Integer nrTema){
        ArrayList<Nota> all = new ArrayList<>();
        Iterable<Nota> it = sqlNotaRepo.findAll();
        for(Nota tema : it)
            all.add(tema);
        List<Nota> allMarks = new ArrayList<>(all);
        Predicate<Nota> predicate = (Nota nota) -> (nota.getNrTema() == nrTema);
        Comparator<Nota> comparator = (Nota n1, Nota n2) -> (n2.getNota() - n1.getNota());
        return filterAndSorter(allMarks, predicate, comparator);
    }

    public Student getStudentByMark(Integer idStudent) throws RepositoryException {
        Student student;
        try {
            student = sqlStudentRepo.findEntity(idStudent).get();
        } catch (RepositoryException e) {
            throw new RepositoryException(e.getMessage());
        }
        return student;
    }

    public ArrayList<String> finalMarks() {
        Iterable<Nota> allMarks = findAllMarks();
        Iterable<Student> allStudents = findAllStudents();
        ArrayList<String> finalMarks = new ArrayList<>();
        Double medie;
        Integer count;
        for (Student st : allStudents) {
            medie = 0.0;
            count = 0;
            for (Nota n : allMarks) {
                if(n.getIdStudent() == st.getID()) {
                    medie += n.getNota();
                    ++count;
                }
            }
            finalMarks.add(st.getNume());
            finalMarks.add(String.valueOf(medie/count));
        }
        return finalMarks;
    }

    public ArrayList<String> mostDifficultHomeworks() {
        ArrayList<String> mostDifficultHomeworks = new ArrayList<>();
        String filePath = new File("").getAbsolutePath();
        Iterable<Student> it = findAllStudents();
        Integer nr;
        for(Tema t : findAllHomeworks()) {
            nr=0;
            for (Student st : it) {
                filePath = new File("").getAbsolutePath();
                filePath = filePath.concat("/src/Output/" + st.getID() + ".txt");
                File file = new File(filePath);
                if (file.exists()) {
                    try {
                        FileReader fr = new FileReader(file);
                        BufferedReader br = new BufferedReader(fr);
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (Objects.equals(line.split(" ")[0], "Adaugare")) {
                                String nrTema = line.split(" ")[3].split(",")[0];
                                if (line.contains("scazuta") && Objects.equals(t.getID(), Integer.valueOf(nrTema)))
                                    ++nr;
                            }
                        }
                        br.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            mostDifficultHomeworks.add(t.getDescriere() + " " + nr);
        }
        return mostDifficultHomeworks;
    }

    public ArrayList<String> studentiPromovati(ArrayList<String> finalMarks) {
        ArrayList<String> studentsAboveFour = new ArrayList<>();
        for(int i=0; i<finalMarks.size(); ++i) {
            if(i%2!=0) {
                if(Double.valueOf(finalMarks.get(i)) >= 4)
                    studentsAboveFour.add(finalMarks.get(i-1));
            }
        }
        return studentsAboveFour;
    }

    public ArrayList<String> studentiLaZi() {
        ArrayList<String> studentsLaZi = new ArrayList<>();
        String filePath = new File("").getAbsolutePath();
        Iterable<Student> it = findAllStudents();
        boolean laZi;
        for(Student st : it) {
            laZi = true;
            filePath = new File("").getAbsolutePath();;
            filePath = filePath.concat("/src/Output/" + st.getID() + ".txt");
            File file = new File(filePath);
            if(file.exists()) {
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        if(Objects.equals(line.split(" ")[0], "Adaugare")) {
                            if(!line.contains("-"))
                                laZi = false;
                        }
                    }

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            else laZi = false;
            if(laZi)
                studentsLaZi.add(st.getNume());
        }
        return studentsLaZi;
    }

    public SQLStudentRepo getStudentRepository() {
        return sqlStudentRepo;
    }

    public SQLTemaRepo getTemaRepository() {
        return sqlTemaRepo;
    }

    public SQLNotaRepo getNotaRepository() {
        return sqlNotaRepo;
    }
}
