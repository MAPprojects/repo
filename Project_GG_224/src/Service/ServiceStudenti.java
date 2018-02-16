package Service;

import Domain.Studenti;
import Repository.StudentRepoSQL;
import Repository.ValidationException;
import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observabel;
import Utils.Observer;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceStudenti implements Observabel<Studenti> {
    private StudentRepoSQL studentRepo;

    ArrayList<Observer<Studenti>> StudentiObserverList=new ArrayList<>();

    public ServiceStudenti(StudentRepoSQL studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Studenti saveStudeti(int idSudent,String nume, int grupa,String email, String cadru_didactic) throws ValidationException, SQLException {
        Studenti entity=new Studenti(idSudent,nume,grupa,email,cadru_didactic);
        if (entity.getNume().isEmpty())
            return null;
        if (studentRepo.getById(idSudent)!=null) {
            return null;
        } else
            studentRepo.save(entity);

        if (entity!=null) {
            ListEvent<Studenti> ev = createEvent(ListEventType.ADD, entity, studentRepo.getAll());
            notifyObservers(ev);
        }
        return entity;
    }
        public Studenti stergeStudent(int id) throws Exception{
        if(studentRepo.getById(id)!=null) {
            Studenti entity=studentRepo.getById(id);
            studentRepo.delete(studentRepo.getById(id));


            if (entity!=null) {
                ListEvent<Studenti> ev = createEvent(ListEventType.REMOVE, entity, studentRepo.getAll());
                notifyObservers(ev);
            }
            File file = new File(String.valueOf(studentRepo.getById(id)) + ".txt");
            if (file.exists()) {
                file.delete();
                System.out.println(file.getName() + " is deleted!");

            }
            return entity;
        }
        else
            return  null;
    }

        public Studenti getById(int id){
            return studentRepo.getById(id);
        }
        public Studenti updateStudent(int idVechi, int idSudent, String nume, int grupa, String email, String cadru_didactic) throws ValidationException, SQLException {
        Studenti entity= studentRepo.getById(idSudent);



        if(entity!=null && !nume.isEmpty()){
            studentRepo.update(studentRepo.getById(idVechi),new Studenti(idSudent,nume,grupa,email,cadru_didactic));
            ListEvent<Studenti> ev = createEvent(ListEventType.UPDATE, entity, studentRepo.getAll());
            notifyObservers(ev);
            return entity;}
        else
            return null;
    }




    @Override
    public void addObserver(Observer<Studenti> o) {
        StudentiObserverList.add(o);
    }

    @Override
    public void removeObserver(Observer<Studenti> o) {
        StudentiObserverList.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Studenti> event) {
        StudentiObserverList.forEach(x -> {
            try {
                x.notifyEvent(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }



    public  <E> ListEvent<E> createEvent(ListEventType type, final E elem, final List<E> l) {
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
    public List<Studenti> getAllStudents() {
        return studentRepo.getAll();
    }
}
