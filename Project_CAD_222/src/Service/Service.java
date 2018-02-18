package Service;

import Domain.*;
import MVC.MainController;
import Repository.IRepository;
import Utils.Observable;
import Utils.Observer;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Service implements Observable {
    private IRepository<Integer,Student> repositoryStudenti;
    private IRepository<Integer,Tema> repositoryTeme;
    private IRepository<String,Nota> repositoryNote;
    private IRepository<String, Intarziere> repositoryIntarzieri;
    private static int saptamanaCurenta=Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)-11;
    private List<Observer> observers=new ArrayList<>();

    public Service(IRepository<Integer,Student> repositoryStudenti,
                   IRepository<Integer,Tema> repositoryTeme,
                   IRepository<String,Nota> repositoryNote,
                   IRepository<String,Intarziere> repositoryIntarzieri){
        this.repositoryStudenti = repositoryStudenti;
        this.repositoryTeme = repositoryTeme;
        this.repositoryNote = repositoryNote;
        this.repositoryIntarzieri=repositoryIntarzieri;
    }

    @Override
    public void addObserver(Observer observer){
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    public Integer getNrTeme(){
        return repositoryTeme.size();
    }

    public Integer getNrTeme(Predicate<Tema> filtru) {
        return Filtrare.filtrare(StreamSupport.stream(getTeme().spliterator(), false).collect(Collectors.toList()), filtru).size();
    }

    public ArrayList<Tema> sublistaTeme(Integer indexInceput, Integer indexFinal, Predicate<Tema> filtru) {
        ArrayList<Tema> l = new ArrayList<>();
        int i = 0;
        List<Tema> listaTemeFiltrate =
                Filtrare.filtrare(StreamSupport.stream(getTeme().spliterator(), false).collect(Collectors.toList()), filtru);
        for (Tema t : listaTemeFiltrate) {
            if (i >= indexInceput && i < indexFinal)
                l.add(t);
            i++;
            if (i == indexFinal)
                return l;
        }
        return l;
    }

    public Integer getNrStudenti(){
        return repositoryStudenti.size();
    }

    public Integer getNrStudenti(Predicate<Student> filtru) {
        return Filtrare.filtrare(StreamSupport.stream(getStudenti().spliterator(), false).collect(Collectors.toList()), filtru).size();
    }

    public ArrayList<Student> sublistaStudenti(Integer indexInceput, Integer indexFinal, Predicate<Student> filtru) {
        ArrayList<Student> l = new ArrayList<>();

        List<Student> listaInitiala = new ArrayList<>();
        getStudenti().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filtru);
        int i = 0;
        for (Student s : listaInitiala) {
            if (i >= indexInceput && i < indexFinal)
                l.add(s);
            i++;
            if (i == indexFinal)
                break;
        }
        return l;
    }

    public Integer getNrNote() {
        return repositoryNote.size();
    }

    public Integer getNrNote(Predicate<Nota> filtru) {
        return Filtrare.filtrare(StreamSupport.stream(getNote().spliterator(), false).collect(Collectors.toList()), filtru).size();
    }

    public ArrayList<Nota> sublistaNote(Integer indexInceput, Integer indexFinal, Predicate<Nota> filtru) {
        ArrayList<Nota> l = new ArrayList<>();
        List<Nota> listaInitiala = new ArrayList<>();
        getNote().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filtru);
        int i = 0;
        for (Nota n : listaInitiala) {
            if (i >= indexInceput && i < indexFinal)
                l.add(n);
            i++;
            if (i == indexFinal)
                return l;
        }
        return l;
    }

    /*
    Descr: Modifica deadline-ul
    IN: id-ul temei si deadline-ul nou
    OUT: repo teme modificat
     */
    public void modifyingDeadline(Integer id,int deadline) throws ValidationException {
        Optional<Tema> temaLab = repositoryTeme.findOne(id);
        if (temaLab.isPresent()) {
            if ((saptamanaCurenta < temaLab.get().getDeadline()) && (deadline > temaLab.get().getDeadline()) && (deadline < 14))
                repositoryTeme.update(new Tema(id, temaLab.get().getDescriere(), deadline));
            else throw new ValidationException(("Deadline-ul nu mai poate fi modificat!"));
            String mesaj = "A fost modificat deadline-ul pentru tema " + temaLab.get().getDescriere() + " cu noul deadline in saptamana " + deadline;
            for (Student s : repositoryStudenti.getAll()) {
                RunnableSendEmail runnableSendEmail = new RunnableSendEmail(s.getEmail(),mesaj);
                Thread t = new Thread(runnableSendEmail);
                t.start();
            }
            notifyObservers();
        }
    }

    /*
    Descr: adauga un nou student in Repository de studenti
    IN: un student
    OUT: repo de studenti modificat
     */
    public void adaugaStudent(Student student)throws ValidationException{
        repositoryStudenti.save(student);
        notifyObservers();
    }

    public void modificaStudent(Student student)throws ValidationException{
        repositoryStudenti.update(student);
        notifyObservers();
    }

    public void stergeStudent(Integer id)throws ValidationException{
        repositoryStudenti.delete(id);
        notifyObservers();
    }

    /*
    Descr: adauga o noua tema in Repository de teme
    IN: o tema
    OUT: repo de teme modificat
     */
    public void adaugaTema(Tema tema)throws ValidationException{
        repositoryTeme.save(tema);
        String mesaj="A fost adaugata tema "+tema.getDescriere()+" cu deadline in saptamana "+tema.getDeadline();
        for (Student s : repositoryStudenti.getAll()) {
            RunnableSendEmail runnableSendEmail = new RunnableSendEmail(s.getEmail(),mesaj);
            Thread t = new Thread(runnableSendEmail);
            t.start();
        }
        notifyObservers();
    }

    public void modificaTema(Tema tema) throws ValidationException{
        repositoryTeme.update(tema);
        String mesaj="A fost modificata tema "+tema.getDescriere()+" cu deadline in saptamana "+tema.getDeadline();
        for (Student s : repositoryStudenti.getAll()) {
            RunnableSendEmail runnableSendEmail = new RunnableSendEmail(s.getEmail(),mesaj);
            Thread t = new Thread(runnableSendEmail);
            t.start();
        }
        notifyObservers();
    }

    public void stergeTema(Integer id) throws ValidationException{
        repositoryTeme.delete(id);
        notifyObservers();
    }

    /*
    Descr: adauga o nota unui student la o tema de laborator
    IN: nota
    OUT: -
     */
    public void adaugaNota(Nota nota,int saptamana, String observatii) throws ValidationException {
        //daca s-a intarziat mai mult de 2 saptamani, nota e automat 1
        if (saptamana - findTema(nota.getTema().getId()).get().getDeadline() >= 3) {
            nota.setValoare(1);
            Intarziere intarziere=new Intarziere(nota.getIdStudent(),nota.getIdTema(),
                    saptamana - findTema(nota.getTema().getId()).get().getDeadline());
            repositoryIntarzieri.save(intarziere);
        }
        //daca s-a trecut de deadline se scad cate 2 puncte pt fiecare saptamana de intarziere
        else if (saptamana > findTema(nota.getTema().getId()).get().getDeadline()) {
            nota.setValoare(nota.getValoare() - (saptamana - findTema(nota.getTema().getId()).get().getDeadline()) * 2);
            repositoryIntarzieri.save(new Intarziere(nota.getIdStudent(),nota.getIdTema(),
                    saptamana-findTema(nota.getTema().getId()).get().getDeadline()));
        }
        //salvare in catalog
        Boolean gasit = false;
        for (Nota notaVeche : repositoryNote.getAll()) {
            if (notaVeche.getStudent().getId() == nota.getStudent().getId() && notaVeche.getTema().getId() == nota.getTema().getId()) {
                if (notaVeche.getValoare() >= nota.getValoare()) {
                    throw new ValidationException("Se va pastra nota anterioara!");
                }
                nota.setId(notaVeche.getId());
                repositoryNote.update(nota);
                gasit = true;
            }
        }
        String str;
        if (gasit)
            str = "Modificare nota ";
        else {
            str = "Adaugare nota ";
            repositoryNote.save(nota);
        }

        String nume = nota.getStudent().getId().toString() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nume, true))) {
            String mesaj=str+nota.getValoare().toString()+" la tema "+ nota.getTema().getDescriere().toString() +
                    " cu deadline in saptamana " + findTema(nota.getTema().getId()).get().getDeadline() + " predata in saptamana " +
                    Integer.toString(saptamana) + ", avand urmatoarele observatii: "+observatii;
            RunnableSendEmail runnableSendEmail = new RunnableSendEmail(nota.getStudent().getEmail(),mesaj);
            Thread t = new Thread(runnableSendEmail);
            t.start();
            bw.write(mesaj);
            bw.newLine();
        } catch (IOException ioe) {
            MainController.showErrorMessage(ioe.getMessage());
        }
        notifyObservers();
    }

    public void stergeNota(String id) throws ValidationException {
        repositoryNote.delete(id);
        notifyObservers();
    }

    public Optional<Student> findStudent(Integer id){
        return repositoryStudenti.findOne(id);
    }

    public Student findStudentByName(String name){
        for (Student student : getStudenti()) {
            if (student.getNume().equals(name))
                return student;
        }
        return null;
    }

    public ArrayList<Nota> findTemeFromStudentMail(String mail){
        ArrayList<Nota> l=new ArrayList<>();
        for (Nota n : getNote())
            if (n.getStudent().getEmail().equals(mail))
                l.add(n);
        return l;
    }

    public Tema findHomeworkByDescription(String description){
        for (Tema tema : getTeme())
            if (tema.getDescriere().equals(description))
                return tema;
        return null;
    }

    public List<String> getNumeStudenti(){
        List<String> l=new ArrayList<>();
        for (Student student : getStudenti())
            l.add(student.getNume());
        return l;
    }

    public List<String> getGrupaStudenti(){
        List<String> l=new ArrayList<>();
        for (Student student : getStudenti())
            l.add(student.getGrupa().toString());
        return l;
    }

    public List<String> getProfIndrumator(){
        List<String> l=new ArrayList<>();
        for (Student student : getStudenti())
            l.add(student.getProfIndrumator());
        return l;
    }

    public List<String> getDescriereTeme(){
        List<String> l=new ArrayList<>();
        for (Tema tema : getTeme())
            l.add(tema.getDescriere());
        return l;
    }

    //RAPOARTE
    public float raportMediaNotelor(Student student){
        float suma=0, nr=0;
        for (Nota nota :getNote())
            if (student.getId()==nota.getIdStudent()) {
                suma = suma + nota.getValoare();
                nr++;
            }
        return suma/nr;
    }

    public List<Pair<Tema,Integer>> raportCeleMaiGreleTeme(){
        ArrayList<Pair<Tema,Integer>> frecvente=new ArrayList<>();
        for (Tema tema : getTeme()){
            frecvente.add(new Pair<Tema,Integer>(tema,0));
        }

        for (Intarziere intarziere : repositoryIntarzieri.getAll()) {
            int j=0;
            for (Pair<Tema, Integer> pereche : frecvente) {
                if (intarziere.getNrSaptamani()!=0 && intarziere.getNrTemaLaborator().equals(pereche.getKey().getId())) {
                    Pair<Tema, Integer> aux = new Pair<>(pereche.getKey(),pereche.getValue() + intarziere.getNrSaptamani());
                    frecvente.remove(j);
                    frecvente.add(aux);
                    break;
                }
                j++;
            }
        }
        return frecvente;
    }

    public List<Pair<Student,Float>> raportStudentiCarePotIntraInExamen(){
        //(nota mai mare sau egala decat 4)
        ArrayList<Pair<Student,Float>> listaStudentiExamen=new ArrayList<>();
        for (Student student : getStudenti()) {
            float medie=0;
            int nrNote=getNrTeme();
            for (Tema tema : getTeme()) {
                String id = "" + student.getId() + "-" + tema.getId();
                Optional<Nota> notaOptional = findNota(id);
                if (notaOptional.isPresent())
                    medie += notaOptional.get().getValoare();
                else
                    medie += 1;
            }
            if (nrNote > 0)
                medie = medie / nrNote;
            if (medie >= 4) {
                listaStudentiExamen.add(new Pair<>(student, medie));
            }
        }
        return listaStudentiExamen;
    }

    public List<Student> raportStudentiTemePredateLaTimp(){
        List<Student> studentiConstiinciosi=new ArrayList<>();
        boolean intarziat=false;
        for (Student student : getStudenti())
            for (Intarziere intarziere : repositoryIntarzieri.getAll())
                if (intarziere.getIdStudent()==student.getId())
                    intarziat=true;
        for (Nota nota : getNote())
            for (Student student : getStudenti())
                if (Objects.equals(nota.getIdStudent(), student.getId()) && !intarziat &&
                        !studentiConstiinciosi.contains(student))
                    studentiConstiinciosi.add(student);
        return studentiConstiinciosi;
    }

    public Optional<Tema> findTema(Integer id){
        return repositoryTeme.findOne(id);
    }

    public Optional<Nota> findNota(String id){
        return repositoryNote.findOne(id);
    }

    /*
    Descr: Acceseaza toti studentii
    IN: entitatea de tip repoStudenti
    OUT: Elementele din repo studenti
     */
    public Iterable<Student> getStudenti(){
        return repositoryStudenti.getAll();
    }

    /*
    Descr: Acceseaza toate temele
    IN: entitatea de tip repoTeme
    OUT: Elementele din repo teme
     */
    public Iterable<Tema> getTeme(){
        return repositoryTeme.getAll();
    }

    /*
    Descr: Acceseaza toate notele
    IN: entitatea de tip repoNote
    OUT: Elementele din repo note
    */
    public Iterable<Nota> getNote(){
        return repositoryNote.getAll();
    }
}
