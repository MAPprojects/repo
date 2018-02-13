package Service;

import Domain.Nota;
import Domain.NotaID;
import Domain.Student;
import Domain.Tema;
import Repository.IRepository;
import Repository.RepositoryException;
import Util.ListEvent;
import Util.ListEventType;
import Util.Observable;
import Util.Observer;
import Validator.ValidationException;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static Util.Util.toList;

public class Service implements Observable<Student>{
    private IRepository<Integer, Student> repoStudent;
    private IRepository<Integer, Tema> repoTema;
    private IRepository<NotaID,Nota> repoNota;
    ArrayList<Util.Observer<Student>> studentObservers=new ArrayList<>();
    public Service(IRepository<Integer,Student> repoStudent,IRepository<Integer, Tema> repoTema,IRepository<NotaID,Nota> repoNota){
        this.repoStudent=repoStudent;
        this.repoTema=repoTema;
        this.repoNota=repoNota;
    }

    @Override
    public void addObserver(Util.Observer<Student> o) {
        studentObservers.add(o);
    }

    @Override
    public void removeObserver(Observer<Student> o) {
        studentObservers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Student> event) {
        studentObservers.forEach(x->x.notifyEvent(event));
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

    public List<Student> getAllS(){
        return StreamSupport.stream(repoStudent.getAll().spliterator(),false).collect(Collectors.toList());
    }

    public List<Tema> getAllT(){
        return StreamSupport.stream(repoTema.getAll().spliterator(),false).collect(Collectors.toList());
    }

    public List<Nota> getAllN(){
        return StreamSupport.stream(repoNota.getAll().spliterator(),false).collect(Collectors.toList());
    }


    public void addStudent(int id,String nume,int grupa, String email,String profesor){
        Student stud = new Student(id, nume, grupa, email, profesor);
        repoStudent.save(stud);
        try{
            ListEvent<Student> ev = createEvent(ListEventType.REMOVE, stud, repoStudent.getAll());
            notifyObservers(ev);
        }
        catch(RepositoryException e){
            throw e;
        }
    }

    public void addTema(int nr,String descriere,int deadline){
        Tema tema=new Tema(nr,descriere,deadline);
        repoTema.save(tema);
    }

    public Student deleteStudent(int id){
        for(Nota nota:repoNota.getAll())
            if(id==nota.getID().getIdStudent()) {
                NotaID idNota = new NotaID(id, nota.getID().getNrTema());
                repoNota.delete(idNota);
            }
        Student stud = repoStudent.delete(id);
        if(stud!=null){
            ListEvent<Student> ev = createEvent(ListEventType.REMOVE,stud,repoStudent.getAll());
            notifyObservers(ev);
        }
        return stud;
    }

    public Tema deleteTema(int nr){ return repoTema.delete(nr); }

    public void updateStudent(Student stud){
        try{
            repoStudent.update(stud);
            ListEvent<Student> ev = createEvent(ListEventType.UPDATE,stud,repoStudent.getAll());
            notifyObservers(ev);
        }
        catch(ValidationException e){
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
                //}
            }

        }
        catch(ParseException e){
            System.err.println("Nu se poate gasi saptamana");
        }
    }

    public void addNota(int idStudent,int nrTema,double valoare,int saptamanaPredare,String obs){
        NotaID idNota=new NotaID(idStudent,nrTema);
        if(saptamanaPredare>repoTema.findOne(nrTema).getDeadline())
            if(saptamanaPredare-repoTema.findOne(nrTema).getDeadline()<=0)
                valoare=0;
            else
                valoare=valoare-(saptamanaPredare-repoTema.findOne(nrTema).getDeadline());
        Nota nota=new Nota(idStudent,nrTema,idNota,valoare);
        repoNota.save(nota);
        if(obs=="")
            obs="NONE";
        writeToFileStudent("Adaugare nota",idStudent,nrTema,valoare,repoTema.findOne(nrTema).getDeadline(),saptamanaPredare,obs);
    }

    public void updateNota(int idStudent,int nrTema,double valoare,int saptamanaPredare,String obs){
        NotaID idNota=new NotaID(idStudent,nrTema);
        if(saptamanaPredare>repoTema.findOne(nrTema).getDeadline())
            if(saptamanaPredare-repoTema.findOne(nrTema).getDeadline()<=0)
                valoare=0;
            else
                valoare=valoare-(saptamanaPredare-repoTema.findOne(nrTema).getDeadline());
        if(valoare<repoNota.findOne(idNota).getValoare())
            valoare=repoNota.findOne(idNota).getValoare();
        Nota nota=new Nota(idStudent,nrTema,idNota,valoare);
        repoNota.update(nota);
        writeToFileStudent("Modificare nota",idStudent,nrTema,valoare,repoTema.findOne(nrTema).getDeadline(),saptamanaPredare,obs);
    }

    public void writeToFileStudent(String inceput,int idStudent,int nrTema, double nota,int deadline,int saptamanaPredare,String obs){
        String fileName=""+idStudent+".txt";
        try (FileWriter fw = new FileWriter(fileName,true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out= new PrintWriter(bw))
        {
                String l = inceput+" Tema: "+nrTema+" Nota: "+nota+" Deadline: "+deadline+" Saptamana predarii: "+saptamanaPredare;
                String obser="Observatii Tema "+nrTema+" : "+obs;
                out.println(l);
                out.println(obser);
        } catch (IOException e) {
            System.err.println("ERR" + e);
        }
    }



    public <E>List<E> filterAndSorter(List<E> lista, Predicate<E> pred, Comparator<E> comp){
        return lista.stream().filter(pred).sorted(comp).collect(Collectors.toList());
    }

    Comparator<Student> compStudentIDCresc = (x,y) -> x.compare(x,y);


    public List<Student> filterStudentNameCtr(String name,String comp){
        Predicate<Student> pred=x->x.getNume().equals(name);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc.reversed());
        else
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc);
    }

    public List<Student> filterStudentGroupCtr(int group,String comp){
        Predicate<Student> pred=(x->x.getGrupa()==group);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc.reversed());
        else
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc);
    }

    public List<Student> filterStudentTeacherCtr(String teacher,String comp){
        Predicate<Student> pred=x->x.getProfesor().equals(teacher);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc.reversed());
        else if(comp.contentEquals("Cresc"))
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc);
        else
            throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
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

    Comparator<Nota> compNotaIDCresc=(x,y)->x.compare(x,y);

    public List<Nota> filterNotaSpecificValue(int val,String comp){
        Predicate<Nota> pred=x->x.getValoare()==val;
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc.reversed());
        else if(comp.contentEquals("Cresc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc);
        else
            throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
    }

    public List<Nota> filterNotaTwoValues(int val1,int val2,String comp){
        Predicate<Nota> pred=x->(x.getValoare()>val1 && x.getValoare()<val2);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc.reversed());
        else if(comp.contentEquals("Cresc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc);
        else {
            throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
        }
    }

    public List<Nota> filterNotaIDStudent(int id,String comp){
        Predicate<Nota> pred=x->(x.getID().getIdStudent()>id);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc.reversed());
        else if(comp.contentEquals("Cresc")) {
            return filterAndSorter(toList(getAllN()), pred, compNotaIDCresc);
        } else throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
    }
}
