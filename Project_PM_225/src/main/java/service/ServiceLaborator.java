package service;

import domain.Laborator;
import domain.Student;
import observer.ListEvent;
import observer.ListEventType;
import observer.Observable;
import observer.Observer;
import repository.Filtre;
import repository.RepoStudent;
import repository.RepoTema;
import util.SendEmail;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ServiceLaborator implements Observable<Laborator> {
    private ArrayList<Observer<Laborator>> laboratorObserver;

    private RepoTema repoTema;
    private RepoStudent repoStudent;
    private SendEmail sendEmail = new SendEmail();
    private Filtre filtre= new Filtre();

    public ServiceLaborator(RepoTema repoTema, RepoStudent repoStudent) throws Exception {
        this.repoTema=repoTema;
        this.repoStudent = repoStudent;
        this.laboratorObserver = new ArrayList<>();
    }
    public List<Laborator> returnall() throws Exception {
        return repoTema.getLista();

    }
    public void add(Integer nrTema, String cerinta, Integer deadline) throws Exception {
        Laborator laborator = new Laborator(nrTema, cerinta, deadline);
        repoTema.add(laborator);

        String mesaj = "S-a adaugata tema numarul " + nrTema + " cu cerinta  \"" + cerinta + "\" si termenul limita " + deadline;
        for (Student s : repoStudent.getLista()) {
            sendEmail.sendEmail(s.getNume(), s.getEmail(), mesaj, "A aparut o noua tema!!! ^_^ ");

        }
        ListEvent<Laborator> event = createEvent(ListEventType.UPDATE, null, this.returnall());
        notifyAllObservers(event);
    }

    public Laborator findobject(Integer nrTema) throws Exception {
        return repoTema.findobject(nrTema,"getNrTema");
    }

    public void update(Integer nrTema, String cerinta, Integer deadline) throws Exception {
        Laborator laborator = new Laborator(nrTema, cerinta, deadline);
        repoTema.update(nrTema, laborator);
        String mesaj = "S-a modificat termenul limita la tema numarul " + nrTema + " cu cerinta  \"" + cerinta + "\" noul termen limita fiind " + deadline;
        for (Student s : repoStudent.getLista()) {
            sendEmail.sendEmail(s.getNume(), s.getEmail(), mesaj, "S-a modificat termenul limita la cerinta numarul "+nrTema+" !!! ^_^");
        }
        ListEvent<Laborator> event = createEvent(ListEventType.UPDATE, null, this.returnall());
        notifyAllObservers(event);
    }

    public void updateDeadline(Integer nrTema, Integer newDeadline) throws Exception {
        Laborator temanecesara = repoTema.findobject(nrTema,"getNrTema");
        if (temanecesara != null && temanecesara.getDeadline() < newDeadline) {
            temanecesara.setDeadline(newDeadline);
            repoTema.update(nrTema, temanecesara);
            ListEvent<Laborator> event = createEvent(ListEventType.UPDATE, null, this.returnall());
            notifyAllObservers(event);
        } else {
            throw new Exception("Deadline can't be little than/equal with current week or your homework isn't exist!!!");
        }
        String mesaj = "S-a modificat termenul limita la tema numarul " + nrTema + " cu cerinta  \"" + temanecesara.getCerinta() + "\" noul termen limita fiind " + newDeadline;
        for (Student s : repoStudent.getLista()) {
            sendEmail.sendEmail(s.getNume(), s.getEmail(), mesaj, "S-a modificat termenul limita la cerinta numarul "+temanecesara.getNrTema()+" !!! ^_^");
        }
    }

    public void delete(Integer nrTema) throws Exception {
        repoTema.delete(nrTema,"getNrTema");
        ListEvent<Laborator> event = createEvent(ListEventType.REMOVE, null, this.returnall());
        notifyAllObservers(event);
    }
    public List<Laborator> laboratoareCarePrelucreazaNumere () {
        Predicate<Laborator> p = laborator->laborator.getCerinta().contains("numere")|| laborator.getCerinta().contains("numar");
        Comparator<Laborator> c = Comparator.comparing(Laborator::getNrTema);
        return filtre.filterAndSorter(repoTema.getLista(),p,c);
    }
    public List<Laborator> secondHalfOfSemester () {
        Predicate<Laborator> p = laborator->laborator.getDeadline()>=7;
        Comparator<Laborator> c = Comparator.comparing(Laborator::getNrTema).reversed();
        return filtre.filterAndSorter(repoTema.getLista(),p,c);
    }
    public List<Laborator> temeOrdonateDupaDeadline () {
        Predicate<Laborator> p = laborator->laborator.getDeadline()==laborator.getDeadline();
        Comparator<Laborator> c = Comparator.comparing(Laborator::getDeadline);
        return filtre.filterAndSorter(repoTema.getLista(),p,c);
    }
    private <E> ListEvent<E> createEvent(ListEventType type, final E element, final List<Laborator> list) {
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
    public void addObserver(Observer<Laborator> obs) {
        this.laboratorObserver.add(obs);
    }

    @Override
    public void removeObserver(Observer<Laborator> obs) {
        this.laboratorObserver.remove(obs);
    }


    @Override
    public void notifyAllObservers(ListEvent<Laborator> event) {
        this.laboratorObserver.forEach(x -> x.notifyEvent(event));
    }


}
