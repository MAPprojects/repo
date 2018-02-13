package Service;

import Domain.Student;
import Domain.Tema;
import Repository.IRepository;
import Repository.RepositoryException;
import Util.ListEvent;
import Util.ListEventType;
import Util.Observable;
import Util.Observer;
import Validator.ValidationException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static Util.Util.toList;

public class TemaService implements Observable<Tema>{
    private IRepository<Integer, Tema> repoTema;
    ArrayList<Observer<Tema>> temaObservers=new ArrayList<>();
    public TemaService(IRepository<Integer,Tema> repoTema){
        this.repoTema=repoTema;
    }

    @Override
    public void addObserver(Util.Observer<Tema> o) {
        temaObservers.add(o);
    }

    @Override
    public void removeObserver(Observer<Tema> o) {
        temaObservers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Tema> event) {
        temaObservers.forEach(x->x.notifyEvent(event));
    }

    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l){
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return l;
            }
            @Override
            public E getElement() {
                return elem;
            }
        };
    }

    public List<Tema> getAllT(){
        return StreamSupport.stream(repoTema.getAll().spliterator(),false).collect(Collectors.toList());
    }

    public void addTema(int nr,String descriere,int deadline){
        Tema tema=new Tema(nr,descriere,deadline);
        repoTema.save(tema);
        try{
            ListEvent<Tema> ev = createEvent(ListEventType.REMOVE, tema, repoTema.getAll());
            notifyObservers(ev);
        }
        catch(RepositoryException e){
            throw e;
        }
    }

    public void updateTermen(int id, int termNou){
        try {
            String input = "20171002";
            String format = "yyyyMMdd";
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            Date date = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat(format);
            Date date2 = df.parse(input);
            calendar.setTime(date);
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.setTime(date2);
            int week2=calendar.get(Calendar.WEEK_OF_YEAR);
            int saptCurenta=week-week2+1;
            System.out.println(saptCurenta);
            if (saptCurenta >= termNou)
                throw new ValidationException("Nu se mai poate prelungi");
            else if (saptCurenta < termNou) {
                //if (repoTema.findOne(id).getDeadline() < termNou) {
                Tema noua = new Tema(id, repoTema.findOne(id).getDescriere(), termNou);
                repoTema.update(noua);
                ListEvent<Tema> ev = createEvent(ListEventType.UPDATE,noua,repoTema.getAll());
                notifyObservers(ev);
                //}
            }

        }
        catch(ParseException e){
            System.err.println("Nu se poate gasi saptamana");
        }
        catch (ValidationException e){
            throw e;
        }
    }

    public <E>List<E> filterAndSorter(List<E> lista, Predicate<E> pred, Comparator<E> comp){
        return lista.stream().filter(pred).sorted(comp).collect(Collectors.toList());
    }

    Comparator<Tema> compTemaIDCresc=(x,y)->x.compare(x,y);

    public List<Tema> filterTemaDescriptionCtr(String description,String comp){
        Predicate<Tema> pred=x->x.getDescriere().equals(description);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllT()),pred,compTemaIDCresc.reversed());
        else if(comp.contentEquals("Cresc")) {
            return filterAndSorter(toList(getAllT()), pred, compTemaIDCresc);
        } else
            throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
    }

    public List<Tema> filterTemaDeadlineCtr(int deadline,String comp){
        Predicate<Tema> pred=x->x.getDeadline()==deadline;
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllT()),pred,compTemaIDCresc.reversed());
        else if(comp.contentEquals("Cresc"))
            return filterAndSorter(toList(getAllT()),pred,compTemaIDCresc);
        else
            throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
    }


    Comparator<Tema> compTemaDeadlineCresc=(x,y)->{
        if(x.getDeadline()>y.getDeadline())
            return 1;
        else if(x.getDeadline()<y.getDeadline())
            return -1;
        else
            return 0;
    };
    public List<Tema> filterTemaValues(int val1,int val2,String comp){
        if(val1>val2){
            throw new ValidationException("The value from the left must be greater!");
        }
        Predicate<Tema> pred=x->(x.getDeadline()>val1 && x.getDeadline()<val2);
        if(comp.contentEquals("Desc"))
            //return filterAndSorter(toList(getAllT()),pred,compTemaIDCresc.reversed());
            return filterAndSorter(toList(getAllT()),pred,compTemaDeadlineCresc.reversed());
        else if(comp.contentEquals("Cresc"))
            //return filterAndSorter(toList(getAllT()),pred,compTemaIDCresc);
            return filterAndSorter(toList(getAllT()),pred,compTemaDeadlineCresc);
        else {
            throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
        }
    }
}
