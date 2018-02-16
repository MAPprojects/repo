package Service;

import Domain.Teme;
import Repository.TemeRepoSQL;
import Repository.ValidationException;
import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observabel;
import Utils.Observer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceTeme implements Observabel<Teme> {

    private TemeRepoSQL temeRepo;
    ArrayList<Observer<Teme>> TemeObserverList=new ArrayList<>();
    public ServiceTeme(TemeRepoSQL temeRepo) {
        this.temeRepo = temeRepo;
    }
    public Teme updateTema(int idVechi, int nrTema,int deadline,String observatii) throws ValidationException, SQLException {
        Teme tema=temeRepo.getById(nrTema);
        if (tema!=null) {
            temeRepo.update(temeRepo.getById(idVechi), new Teme(nrTema, deadline, observatii));
            ListEvent<Teme> ev = createEvent(ListEventType.UPDATE, tema, temeRepo.getAll());
            notifyObservers(ev);
            return tema;
        }
        else
            return null;
    }

    public Teme stergeTema(int nrTema) throws Exception {
        Teme tema=temeRepo.getById(nrTema);
        if (tema != null) {

            temeRepo.delete(temeRepo.getById(nrTema));
            ListEvent<Teme> ev = createEvent(ListEventType.REMOVE, tema, temeRepo.getAll());
            notifyObservers(ev);
            return  tema;
        } else

            return null;
    }

    public Teme saveTema(int nrTema,int deadline,String descriere) throws ValidationException, SQLException {
        Teme entity=new Teme(nrTema,deadline,descriere);
        if(temeRepo.getById(entity.getNrTema())!=null){
            return null;
        }
        else {
            temeRepo.save(entity);

            ListEvent<Teme> ev = createEvent(ListEventType.ADD, entity, temeRepo.getAll());
            notifyObservers(ev);
        }
        return entity;
    }


    @Override
    public void addObserver(Observer<Teme> o) {
        TemeObserverList.add(o);
    }

    @Override
    public void removeObserver(Observer<Teme> o) {
            TemeObserverList.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Teme> event) {
        TemeObserverList.forEach(x -> {
            try {
                x.notifyEvent(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final List<E> l){
        return new ListEvent<E>(type) {
            @Override
            public List<E> getList() {
                return l;
            }
            @Override
            public E getElement() {
                return elem;
            }
        };
    }


    public List<Teme> getAllTeme() {
        return temeRepo.getAll();
    }
}
