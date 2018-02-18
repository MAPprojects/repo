package Service;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repository.IRepository;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Service extends Observable
{
    private static final int nrSapt=14;
    private static int saptCurenta;
    private IRepository<Student,Integer> studenti;
    private IRepository<Tema,Integer> teme;
    private IRepository<Nota, Pair<Integer,Integer>> note;

    private <E> List<E> filtrare(List<E> lista, Predicate<E> p, Comparator<E> c)
    {
        return lista.stream().filter(p).sorted(c).collect(Collectors.toList());
    }



    public Service(IRepository<Student,Integer> studenti,IRepository<Tema,Integer> teme, IRepository<Nota, Pair<Integer,Integer>> note)
    {
        this.studenti = studenti;
        this.teme = teme;
        this.note = note;
        saptCurenta = getCurrentWeek();
        seed();
    }
    int i,id=0;
    private void seed()
    {
        if(studenti.size()==0) {

            ArrayList<String> profi = new ArrayList<>();
            profi.add("Camelia Serban");
            profi.add("Cojocaru Grigoreta");
            profi.add("Istvan Czibula");

            for (int j = 222; j <= 224; j++) {
                Path path = Paths.get("src\\Resources\\nume" + j + ".txt");
                ArrayList<String> nume222 = new ArrayList<>();
                ArrayList<String> prenume222 = new ArrayList<>();
                Stream<String> lines;
                try {
                    lines = Files.lines(path);
                    lines.forEach(instance -> nume222.add(instance));
                } catch (IOException e) {
                    System.err.println("Can't load data from file\n");
                }

                path = Paths.get("src\\Resources\\prenume" + j + ".txt");
                try {
                    lines = Files.lines(path);
                    lines.forEach(instance -> prenume222.add(instance));
                } catch (IOException e) {
                    System.err.println("Can't load data from file\n");
                }


                for (i = 0; i < nume222.size(); i++) {
                    id++;
                    String nume = nume222.get(i);
                    String prenume = prenume222.get(i);
                    int grupa = j;
                    String email = nume.toLowerCase() + "@gmail.com";
                    String cadruD = profi.get(j-222);
                    Student student = new Student(id, nume + ' ' + prenume, grupa, email, cadruD);
                    studenti.add(student);
                }
            }

            teme.add(new Tema(1," Implementati funcționalitățile definite de Iteratia 1 din fisierul TemeLaborator.pdf ",4));
            teme.add(new Tema(2," 1. Implementati funcționalitățile definite de Iteratia 2 din fisierul TemeLaborator.pdf -\n" +
                    "2. Asigurați persistența datelor folosind clase din pachetele Java IO sau Java NIO2\n" +
                    "3. Completați layerul UI cu un meniu care să permită utilizatorului:\n" +
                    "a) Efectuarea operațiilor definite de Iterația 1 și Iterația 2.\n" +
                    "b) Validarea datelor introduse (Definiti excepții checked).\n ",6));
            teme.add(new Tema(3," Extindeți proiectul Lab2 cu umătoarele cerințe funcționale sau de proiectare:\n" +
                    "1. Implementati funcționalitățile definite de Iteratia 3 din fisierul TemeLaborator.pdf -\n" +
                    "2. In clasa Service sau EntityService (daca aveti cate unul pentru fiecare entitate), utilizati Stream.filter\n" +
                    "pentru a realiza cerintele iteratiei 3. Mai precis:\n" +
                    "a. Scrieti o metoda generica care filtreaza o lista de entitati de tipul E dupa un anumit criteriu,\n" +
                    "specificat ca si un Predicate in lista de parametri ai metodei. Rezultatul filtrarii va fi apoi sortat\n" +
                    "folosind un comparator care este de asemenea furnizat ca si parametru al metodei.\n" +
                    "public <E> List<E> filterAndSorter(List<E> lista, Predicate<E> p,\n" +
                    "Comparator<T> c)\n" +
                    "Observatie: Puteti defini clase filtre!\n" +
                    "b. Scrieti metode de filtrare concrete care apeleaza metoda generica de la punctul a. (3 metode\n" +
                    "concrete (3 filtre) pentru fiecare entitate)). Folositi lamba si referinte la metode.\n" +
                    "3. Folositi Optional<T> , oriunde este necesar, pentru a preveni NullPointerException (vezi cursul 4 slide 30)\n" +
                    "4. In clasa FileRepository folositi Stream pt citirea(prelucrarea) datelor din fisier.\n ",7));
            teme.add(new Tema(4," Extindeți proiectul Lab4 cu umătoarele cerințe funcționale sau de proiectare:\n" +
                    "- Proiectati o interfata grafica, folosind JavaFX, care sa permita efectuarea de operatii CRUD pentru o\n" +
                    "entitate definita la Iteratia 1.\n" +
                    "- Separati partea de logica de partea de vizualizare si manipulare (use MVC); Atentie Controllerul din\n" +
                    "MVC nu este acelasi cu Controllerul GRASP pe care l-ati avut anul trecut.\n" +
                    "- Fara FXML (Seminar 7) ",8));
            teme.add(new Tema(5," Extindeți proiectul Lab5 cu umătoarele cerințe funcționale sau de proiectare:\n" +
                    "- Proiectati o interfata grafica, folosind JavaFX, care sa permita efectuarea operatiilor ce definesc\n" +
                    "functionalitatile din Iteratia1, Iteratia2 si Iteratia3.\n" +
                    "- Separati partea de logica de partea de vizualizare si manipulare (use MVC);\n" +
                    "View-l sa fie un fisier FXML (Seminar 8).\n ",11));
            teme.add(new Tema(6," Conversie Lab2+Lab3 din Java IN C#",13));

            for (int j = 1; j < 7 ; j++) {
                for (Student st: studenti.getAll()
                     ) {
//                    Random rnd = new Random();
                    int nota = ThreadLocalRandom.current().nextInt(10)+1;
                    note.add(new Nota(st,teme.find(j).get(),nota));
                }

            }
        }
    }

    public Integer getCurrentWeek()
    {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR) - 39;
        if(week<=0)
            week = 11+ calendar.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    public int sizeStudent()
    {
        return studenti.size();
    }

    public Iterable<Student> getStudenti()
    {
        return studenti.getAll();
    }

    public Iterable<Tema> getTeme() {
        return teme.getAll();
    }

    public Iterable<Nota> getNote() {
        return note.getAll();
    }

    public void addStudent(Integer idStudent, String nume, int grupa, String email, String cadruDidactic)
    {
        Student student = new Student(idStudent, nume, grupa, email, cadruDidactic);
        studenti.add(student);
        setChanged();
        notifyObservers();
    }

    public Optional<Student >delete(Integer idStudent)
    {
        //int poz = findStudentById(idStudent);
        Optional<Student> temp = studenti.delete(idStudent);
        if(temp.isPresent())
            deleteNoteWithStudentId(idStudent);
        setChanged();
        notifyObservers();
        return temp;
    }

    private void deleteNoteWithStudentId(Integer idStudent)
    {
        List<Nota> temp = iterableToArrayList(note.getAll());
        for(Nota nota : temp)
        {
            if(nota.getStudent().getID()==idStudent)
                note.delete(nota.getID());
        }
    }

    public void updateStudent(int idStudent, String nume, int grupa, String email, String cadruDidactic)
    {
        Student nou = new Student(idStudent, nume, grupa, email, cadruDidactic);
        //int poz = findStudentById(idStudent);
        studenti.update(nou);
        setChanged();
        notifyObservers();
    }

    public Optional<Student >findStudentById(Integer idStudent)
    {
        return studenti.find(idStudent);
    }

    public Optional<Tema> findTemaById(Integer nrTema)
    {
        //Optional<Tema> o= Optional<Tema>.of(teme.find(nrTema));
        return teme.find(nrTema);

    }

    public void addTema(Integer nrTema, String descriere, int deadline)
    {
        Tema tema = new Tema(nrTema, descriere, deadline);
        teme.add(tema);
        setChanged();
        notifyObservers();
    }

    public void modifyTema(Integer nrTema, int deadline)
    {
        Optional<Tema> op = findTemaById(nrTema);

        if(op.isPresent())
        {
            if(deadline>op.get().getDeadline() && op.get().getDeadline()> saptCurenta && deadline <= nrSapt)
            {
                Tema temaNoua = new Tema(op.get().getID(),op.get().getDescriere(),deadline);
                teme.update(temaNoua);
                setChanged();
                notifyObservers();
            }
            else
            {
                throw new RuntimeException("Deadline-ul nu a fost introdus corect");
            }
        }

        //Tema tema =  findTemaById(nrTema);

    }

    public void modifyTemaDescr(Integer nrTema, String descr)
    {
        Optional<Tema> op = findTemaById(nrTema);

        if(op.isPresent())
        {

            Tema temaNoua = new Tema(op.get().getID(),descr,op.get().getDeadline());
            teme.update(temaNoua);
            setChanged();
            notifyObservers();

        }

        //Tema tema =  findTemaById(nrTema);

    }

    public void addNota(Integer idStudent, Integer idTema, int saptPredarii,int valoare, String obs)
    {

        Optional<Student> ops = studenti.find(idStudent);
        Optional<Tema> opt = teme.find(idTema);

        if( ops.isPresent() && opt.isPresent())
        {
            Student student = ops.get();
            Tema tema = opt.get();

            valoare = checkDeadline(tema, saptPredarii, valoare);

            Nota nota = new Nota(student, tema, valoare);
            note.add(nota);
            writeObservations(nota, obs, "S-a adaugat nota: ", saptPredarii);
            setChanged();
            notifyObservers();
        }
    }

    private void writeObservations(Nota nota, String obs, String action,int saptPredarii)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("src\\Resources\\Student"+nota.getStudent().getID()+".txt",true)))
        {
            Tema tema = nota.getTema();
            Student student = nota.getStudent();
            Calendar calendar = Calendar.getInstance();
            int week = calendar.get(Calendar.DATE);
            writer.write(String.format("In saptamana %d %s a fost predata in saptamana: %d la tema: %d cu termenul de predare: %d nota: %d; observatii: %s\n", week, action, saptPredarii, tema.getID(), tema.getDeadline(), nota.getNota(), obs));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modifyNota(Integer idStudent, Integer idTema, int saptPredarii, int newNota, String obs)
    {
        Optional<Nota> opn= note.find(new Pair<Integer,Integer>(idStudent,idTema));

        if(opn.isPresent())
        {

            Nota nota = opn.get();
            Student student = nota.getStudent();
            Tema tema = nota.getTema();
            newNota = checkDeadline(tema,saptPredarii,newNota);

            if(nota.getNota()<newNota)
            {
                Nota notaNoua = new Nota(student,tema,newNota);
                note.update(notaNoua);
                writeObservations(notaNoua,obs,"S-a modificat nota: ",saptPredarii);
            }
        }
    }

    private int checkDeadline(Tema tema,int saptPredarii, int nota)
    {
        if(saptPredarii-tema.getDeadline()>2)
            nota = 1;
        else if(saptPredarii>tema.getDeadline())
            nota = nota - 2 *(saptPredarii-tema.getDeadline());
        return nota;
    }

    public List<Student> filterGrupa(List<Student> lst, int grupa)
    {
        Predicate<Student> pr = x -> x.getGrupa()==grupa;
        Comparator<Student> cmp = (x,y)->x.getNume().compareTo(y.getNume());
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public List<Student> filterNume(List<Student> lst,String nume)
    {
        Predicate<Student> pr = (x -> x.getNume().contains(nume));
        Comparator<Student> cmp = (x,y)->{if(x.getGrupa()>y.getGrupa())return 1;else return 0;};
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public List<Student> filterCadruDidactic(List<Student> lst,String cadruDidactic)
    {
        Predicate<Student> pr = x -> x.getCadruDidactic().contains(cadruDidactic);
        Comparator<Student> cmp = (x,y)->x.getNume().compareTo(y.getNume());
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public List<Tema> filterDeadline(List<Tema> lst,int deadline)
    {
        Predicate<Tema> pr = x -> x.getDeadline()==deadline;
        Comparator<Tema> cmp = (x,y)->x.getID().compareTo(y.getID());
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public List<Tema> filterDescription(List<Tema> lst,String descr)
    {
        Predicate<Tema> pr = x -> x.getDescriere().contains(descr);
        Comparator<Tema> cmp = (x,y)->x.getID().compareTo(y.getID());
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public List<Tema> filterFinishedDeadline(List<Tema> lst)
    {
        Predicate<Tema> pr = x -> x.getDeadline()<saptCurenta;
        Comparator<Tema> cmp = (x,y)->x.getID().compareTo(y.getID());
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public List<Nota> filterNotaStudent(List<Nota> lst,String student)
    {
        Predicate<Nota> pr = x -> x.getStudent().getNume().contains(student);
        Comparator<Nota> cmp = (x,y)->x.getNota()-y.getNota();
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public List<Nota> filterNotaTema(List<Nota> lst,int temaid)
    {
        Predicate<Nota> pr = x -> x.getTema().getID()==temaid;
        Comparator<Nota> cmp = (x,y)->x.getNota()-y.getNota();
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public List<Nota> filterNota(List<Nota> lst,int nota)
    {
        Predicate<Nota> pr = x -> x.getNota()==nota;
        Comparator<Nota> cmp = (x,y)->x.getStudent().getID()-y.getStudent().getID();
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public List<Nota> filterNotaSubTrecere(List<Nota> lst)
    {
        Predicate<Nota> pr = x -> x.getNota()<5;
        Comparator<Nota> cmp = (x,y)->x.getStudent().getID()-y.getStudent().getID();
        return filtrare(iterableToArrayList(lst),pr,cmp);
    }

    public <E> List<E> iterableToArrayList(Iterable<E> iterable)
    {
        ArrayList<E> list = new ArrayList<E>();
        if(iterable!=null)
        {
            for(E e:iterable)
            {
                list.add(e);
            }
        }
        return list;
    }

    public HashMap<String, Integer> studPerHomework()
    {
        HashMap<String,Integer> raport = new HashMap<>();
        for (Nota nota :
                note.getAll()) {
            String idtema = nota.getTema().getID().toString();
            if(nota.getNota()<5)
               if(raport.containsKey(idtema))
                   raport.replace(idtema,raport.get(idtema)+1);
                else
                    raport.put(idtema,1);
        }

        return raport;
    }

    public HashMap<String, Integer> notePerc()
    {
        HashMap<String,Integer> raport = new HashMap<>();
        for (Nota nota :
                note.getAll()) {
            String idnota = nota.getNota().toString();
            if(nota.getNota()<5)
                if(raport.containsKey(nota.getNota()))
                    raport.replace(idnota,raport.get(idnota)+1);
                else
                    raport.put(idnota,1);
        }

        return raport;
    }

    public HashMap<Integer, Float> getMedie() {
        HashMap<Integer,Float> hashMap = new HashMap<>();

        for (Tema tema: teme.getAll()
             ) {
            Float nr=new Float(0);
            Float suma = new Float(0);
            for (Nota nota: note.getAll()) {
                if(nota.getIdTema()==tema.getID()) {
                    suma = suma + nota.getNota();
                    nr++;
                }
            }

            hashMap.put(tema.getID(),suma/nr);
        }
        return hashMap;

    }
}


