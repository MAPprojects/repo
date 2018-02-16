package Service;

import Domain.Note;

import Domain.Penalizare;

import Domain.Studenti;
import Repository.*;

import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observabel;
import Utils.Observer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ServiceNote  implements Observabel<Note> {
    private NoteRepoSQL noteRepo;
    private TemeRepoSQL repoTeme=new TemeRepoSQL(new TemeValidator());
    private StudentRepoSQL repoStudent=new StudentRepoSQL(new StudentValidator());
    private PenalizareRepoSQL repoPenalizari=new PenalizareRepoSQL();
    public ServiceNote(NoteRepoSQL noteRepo) {
        this.noteRepo =  noteRepo;
    }
    ServiceStudenti studentService=new ServiceStudenti(repoStudent);

    public Note getByIdSIdT(int idS, int idT) {

        Iterable<Note> it = noteRepo.getAll();
        for (Note te : it) {
            if (te.getIdStudent() == idS && te.getNrTema() == idT)
                return te;
        }
        return null;
    }

    public Note save(int idStudent,int nrTema,int valoareNota,String observatii, int saptamanPredare) throws ValidationException, SQLException {
        Note entity = new Note(idStudent, nrTema, valoareNota);
        if (repoTeme.getById(nrTema) != null) {
            int termen = repoTeme.getById(entity.getNrTema()).getDeadline();
            System.out.println(termen);
            if (termen < saptamanPredare) {

                if (saptamanPredare - termen > 2) {
                    entity.setValoare(1);

                } else {
                    entity.setValoare(entity.getValoare() - (saptamanPredare - termen));

                }
                repoPenalizari.save(new Penalizare(nrTema, idStudent));
            }
            if (getByIdSIdT(entity.getIdStudent(), entity.getNrTema()) != null) {
                return null;
            } else {


                noteRepo.save(entity, observatii, saptamanPredare);
                ListEvent<Note> ev = createEvent(ListEventType.ADD, entity, noteRepo.getAll());
                notifyObservers(ev);

                return entity;

            }

        }
       return null;
    }


    public Note modificare(int idStudent, int nrTema,int valoareNoua, int saptamanPredare,String observatii) throws ValidationException, SQLException {

        Note entity = getByIdSIdT(idStudent, nrTema);
        Note entityInitial = getByIdSIdT(idStudent, nrTema);
        Iterable<Note> it2 = noteRepo.getAll();
        int valoareIntiala = entity.getValoare();
        entity.setValoare(valoareNoua);
        int termen = repoTeme.getById(entity.getNrTema()).getDeadline();
        if (entity!=null){
            if (termen < saptamanPredare) {
                if (saptamanPredare - termen > 2) {
                    entity.setValoare(1);

                } else {
                    entity.setValoare(entity.getValoare() - (saptamanPredare - termen));

                }


            }
            if (entity.getValoare() < valoareIntiala)
                entity.setValoare(valoareIntiala);

            noteRepo.update(entityInitial, entity, observatii, saptamanPredare);
            ListEvent<Note> ev = createEvent(ListEventType.UPDATE, entity, noteRepo.getAll());
            notifyObservers(ev);
            return entity;
        }
        else
            return null;

    }
    public Note stergeNota(int idStudent, int nrNota) throws SQLException {
        Note entity=getByIdSIdT(idStudent,nrNota);
        if (entity!=null){
            noteRepo.delete(entity);
            ListEvent<Note> ev = createEvent(ListEventType.REMOVE, entity, noteRepo.getAll());
            notifyObservers(ev);
            return entity;
        }
        else return null;


    }

    public List<Note> getAllNote() {
        return noteRepo.getAll();
    }

    ArrayList<Observer<Note>> NoteObserverList=new ArrayList<>();

    @Override
    public void addObserver(Observer<Note> o) {
        NoteObserverList.add(o);
    }

    @Override
    public void removeObserver(Observer<Note> o) {
        NoteObserverList.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Note> event) {
        NoteObserverList.forEach(x -> {
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
}
