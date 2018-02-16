package service;

import com.itextpdf.text.DocumentException;
import domain.Laborator;
import domain.Nota;
import domain.Student;
import observer.ListEvent;
import observer.ListEventType;
import observer.Observable;
import observer.Observer;
import repository.*;
import util.CurrentWeek;
import util.SendEmail;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;


public class ServiceNota implements Observable<Nota> {
    private GeneratePDF generator = new GeneratePDF();

    public RepoNota getRepoNota() {
        return repoNota;
    }

    private ArrayList<Observer<Nota>> notaObservers;


    private RepoNota repoNota;
    private RepoTema repoTema;
    private RepoStudent repoStudent;
    private RepoIdStudent repoIdStudent = new RepoIdStudent();
    private SendEmail sendEmail = new SendEmail();
    private Filtre filtre = new Filtre();

    List<Integer> penalizari = new ArrayList<Integer>();
    List<Student> studentifarapenalizare = new ArrayList<Student>();

    public ServiceNota(RepoStudent repoStudent, RepoNota rNota, RepoTema repoTema) throws Exception {
        this.repoNota = rNota;
        this.repoTema = repoTema;
        this.repoStudent = repoStudent;
        for (Integer i = 0; i <= 14; i++) {
            penalizari.add(0);
        }
        this.notaObservers = new ArrayList<>();

    }

    public List<Nota> returnall() throws Exception {
        return repoNota.getLista();

    }

    public void add(Integer idStudent, Integer valoare, Integer nrTema, String observatii) throws Exception {
        Nota nota = new Nota(idStudent, valoare, nrTema);
        Laborator temaCautat = repoTema.findobject(nrTema, "getNrTema");
        Student s = repoStudent.findobject(idStudent, "getId");
        if (!studentifarapenalizare.contains(s)) {
            studentifarapenalizare.add(s);
        }
        if (temaCautat.getDeadline() + 1 == CurrentWeek.CURRENT_WEEK) {
            nota.setValoare(valoare - 2);
            penalizari.set(nrTema, penalizari.get(nrTema) + 1);
            studentifarapenalizare.remove(s);
        } else {
            if (temaCautat.getDeadline() + 1 < CurrentWeek.CURRENT_WEEK) {
                nota.setValoare(1);
                studentifarapenalizare.remove(s);

            }
        }
        repoNota.add(nota);
        repoIdStudent.addInFile(idStudent, "adaugare nota", nota.getNrTema(), nota.getValoare(), temaCautat.getDeadline(), CurrentWeek.CURRENT_WEEK, observatii);
        sendEmail.sendEmail(repoStudent.findobject(idStudent, "getId").getNume(), repoStudent.findobject(idStudent, "getId").getEmail(), repoIdStudent.getFromFile(idStudent), "aduagare nota!");
        ListEvent<Nota> event = createEvent(ListEventType.ADD, null, this.returnall());
        notifyAllObservers(event);
    }

    public void update(Integer idStudent, Integer valoare, Integer nrTema, String observatii) throws Exception {
        Nota nota = new Nota(idStudent, valoare, nrTema);
        Laborator temaCautat = repoTema.findobject(nrTema, "getNrTema");
        repoNota.update(nota.getIdStudent(), nota);
        repoIdStudent.addInFile(idStudent, "modificare nota", nota.getNrTema(), nota.getValoare(), temaCautat.getDeadline(), CurrentWeek.CURRENT_WEEK, observatii);
        sendEmail.sendEmail(repoStudent.findobject(idStudent, "getId").getNume(), repoStudent.findobject(idStudent, "getId").getEmail(), repoIdStudent.getFromFile(idStudent), "modificare nota!");
        ListEvent<Nota> event = createEvent(ListEventType.UPDATE, null, this.returnall());
        notifyAllObservers(event);
    }



    public Map<String, Double> mediaPonderata() {
        Map<String, Double> toateMediile = new HashMap<String, Double>();
        for (Student s : repoStudent.getLista()) {
            Map<Integer, Integer> aparitiinote = new HashMap<Integer, Integer>();
            for (Nota n : repoNota.getLista()) {
                if (s.getId() == n.getIdStudent()) {
                    if (aparitiinote.get(n.getValoare()) == null) {
                        aparitiinote.put(n.getValoare(), 1);
                    } else {
                        aparitiinote.put(n.getValoare(), aparitiinote.get(n.getValoare()) + 1);
                    }
                }
            }

            Integer suma = 0;
            Integer nrTeme = 0;
            for (Integer key : aparitiinote.keySet()) {
                suma += key * aparitiinote.get(key);
                nrTeme += aparitiinote.get(key);
            }
            if (nrTeme != 0 && suma != 0) {
                Double medie = Double.valueOf(suma / nrTeme);
                toateMediile.put(s.getNume(), medie);
            } else if (suma == 0) {
                toateMediile.put(s.getNume(), 0.0);
            }
        }
        return toateMediile;
    }

    public String procentStudentiCuMediiPeste7() throws FileNotFoundException, IllegalAccessException, DocumentException, NoSuchFieldException {
        Map<String, Double> toateMediile = mediaPonderata();
        Integer nrStudentiTrecuti = 0;
        Integer intregNrStudenti = toateMediile.size();
        for (String key : toateMediile.keySet()) {
            if (toateMediile.get(key) >= 7.0) {
                nrStudentiTrecuti++;
            }
        }
        Map<String, String> rezultat = new HashMap<>();
        Double x = Double.valueOf(100 * nrStudentiTrecuti / intregNrStudenti);
        rezultat.put(intregNrStudenti.toString(), nrStudentiTrecuti.toString());
        rezultat.put("100%", x.toString() + "%");

        generator.genereatePDf(rezultat, "Initial", "Din care", "Procent studenti cu medii peste 7");
        return "./src/main/resources/Procent studenti cu medii peste 7.pdf";



    }

    public String generarePdfPentruMediePonderata() throws FileNotFoundException, IllegalAccessException, DocumentException, NoSuchFieldException {
        Map<String, Double> toateMediile = mediaPonderata();
        Map<String, String> finish = new HashMap<>();
        for (String key : toateMediile.keySet()) {
            finish.put(key, toateMediile.get(key).toString());

        }
        generator.genereatePDf(finish, "Nume", "Media ponderata", "Media ponderata a fiecarui student");
        return "./src/main/resources/Media ponderata a fiecarui student.pdf";

    }

    public String studentiCareIntraInExamen() throws FileNotFoundException, DocumentException, NoSuchFieldException, IllegalAccessException {
        Map<String, Double> mediidefiltrat = mediaPonderata();
        Map<String, String> finish = new HashMap<>();
        for (String key : mediidefiltrat.keySet()) {
            if (mediidefiltrat.get(key) >= 4) {
                finish.put(key, mediidefiltrat.get(key).toString());
            }
        }
        generator.genereatePDf(finish, "Nume student", "Medie", "Studenti care intra in examen");
        return "./src/main/resources/Studenti care intra in examen.pdf";
    }

    public List<Nota> noteDe10() {
        Predicate<Nota> p = nota -> nota.getValoare() == 10;
        Comparator<Nota> c = Comparator.comparing(Nota::getNrTema);
        return filtre.filterAndSorter(repoNota.getLista(), p, c);
    }

    public List<Nota> noteDe1() {
        Predicate<Nota> p = nota -> nota.getValoare() == 1;
        Comparator<Nota> c = Comparator.comparing(Nota::getIdStudent);
        return filtre.filterAndSorter(repoNota.getLista(), p, c);
    }

    public List<Nota> noteleUnuiStudentInOrdine(Integer nr) {
        Predicate<Nota> p = nota -> nota.getIdStudent() == nr;
        Comparator<Nota> c = Comparator.comparing(Nota::getValoare);
        return filtre.filterAndSorter(repoNota.getLista(), p, c);
    }

    private <E> ListEvent<E> createEvent(ListEventType type, final E element, final List<Nota> list) {
        return new ListEvent<E>(type) {
            @Override
            public ArrayList<E> getList() {
                return (ArrayList<E>) list;
            }

            @Override
            public E getElement() {
                return element;
            }
        };
    }

    @Override
    public void addObserver(Observer<Nota> obs) {
        this.notaObservers.add(obs);
    }

    @Override
    public void removeObserver(Observer<Nota> obs) {
        this.notaObservers.remove(obs);
    }

    @Override
    public void notifyAllObservers(ListEvent<Nota> event) {
        this.notaObservers.forEach(x -> x.notifyEvent(event));
    }
}
