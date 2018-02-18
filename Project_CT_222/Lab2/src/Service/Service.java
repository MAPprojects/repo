package Service;

import Domain.*;
import Repositories.Repository;
import Repositories.RepositoryException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Utils.*;
import Utils.Observable;
import Utils.Observer;

public class Service implements Observable<ListEvent> {
    //OBSERVER
    protected ArrayList<Utils.Observer<ListEvent>> observers=new ArrayList<>();
    @Override
    public void addObserver(Utils.Observer<ListEvent> o){
        observers.add(o);
    }
    @Override
    public void removeObserver(Observer<ListEvent> o){
        observers.remove(o);
    }

//    public <E>ListEvent<E> createEvent(ListEventType type,final E elem,final Iterable<E> l){
//        return new ListEvent<E>(type){
//            @Override
//            public Iterable<Studenti> getList(){
//                return l;
//            }
//            @Override
//            public E getElement(){
//                return elem;
//            }
//        };
//    }

    public void notifyObservers(ListEvent e){
        for(Observer o:observers){
            o.notifyEvent(e);
        }
    }

    //OBSERVER
    Repository<Studenti,Integer> studentiRepository;
    Repository<Teme,Integer> temeRepository;
    Repository<Nota,Integer> notaRepository;
    Repository<LoginObject,String> loginObjectRepository;

    public Service(Repository studentRep,Repository temeRep,Repository notaRep,Repository loginObjectRepo){
        this.studentiRepository=studentRep;
        this.temeRepository=temeRep;
        this.notaRepository=notaRep;
        this.loginObjectRepository=loginObjectRepo;
    }
    public void addStudent(String nume,int grupa,String email,String CadruDidactic){
        Integer ids=0;
        for(Studenti s:getAllStudenti()){
            if(s.getId()>ids)
                ids=s.getId();
        }
        Studenti stud=new Studenti(ids+1,nume,grupa,email,CadruDidactic);
        studentiRepository.save(stud);
     //   ListEvent<Studenti> ev=createEvent(ListEventType.ADD,stud,studentiRepository.findAll());
        notifyObservers( new StudentiEvent(ListEventType.ADD,stud));

    }
    public Optional<Studenti> removeStudent(Integer id){
        int ok=1;
        while(ok==1) {
            ok=0;
            for (Nota nota : notaRepository.findAll()) {
                if (nota.getIdStudent() == id) {
                    notaRepository.delete(nota.getId());
                    ok=1;
                    break;
                }
            }
        }
        Optional<Studenti> deleted=studentiRepository.delete(id);
        if(deleted.isPresent()){
            notifyObservers(new StudentiEvent(ListEventType.REMOVE,deleted.get()));
            return (Optional<Studenti>)studentiRepository.delete(id);
        }
        throw new RepositoryException("studentul nu exista");

    }
    public  Optional<Nota> removeNota(Integer id){
        Optional<Nota> deleted=notaRepository.delete(id);
        if(deleted.isPresent()){
            notifyObservers(new NoteEvent(ListEventType.REMOVE,deleted.get()));
            return (Optional<Nota>)notaRepository.delete(id);
        }
        throw new RepositoryException("nota nu exista");
    }

    public Optional<Teme> removeTema(Integer id){
        int ok=1;
        while(ok==1) {
            ok=0;
            for (Nota nota : notaRepository.findAll()) {
                if (nota.getIdTema() == id) {
                    notaRepository.delete(nota.getId());
                    ok=1;
                    break;
                }
            }
        }
        Optional<Teme> deleted=temeRepository.delete(id);
        if(deleted.isPresent()){
            notifyObservers(new TemeEvent(ListEventType.REMOVE,deleted.get()));
        return (Optional<Teme>)temeRepository.delete(id);}
        throw new RepositoryException("Tema nu exista!");
    }

    public Iterable<Studenti> getAllStudenti(){
        return studentiRepository.findAll();
    }

    public Studenti cautaStudent(Integer id){
        return (Studenti)studentiRepository.findOne(id);
    }
    public Teme cautaTema(Integer id){
        return (Teme)temeRepository.findOne(id);
    }
    public Nota cautaNota(Integer id){
        return (Nota)notaRepository.findOne(id);
    }
    public int getCurrentWeek(){
        Calendar calendar= Calendar.getInstance();
        return calendar.get(calendar.WEEK_OF_YEAR)-40;
    }
    public void addTema(int NumarLaborator,String cerinta,int deadline){
        Teme tema=new Teme(NumarLaborator,cerinta,deadline);
        temeRepository.save(tema);
        notifyObservers( new TemeEvent(ListEventType.ADD,tema));
    }
    public List<Studenti> getStudInLista(){
        Iterable<Studenti> map=getAllStudenti();
        List<Studenti> ret=new ArrayList<>();
        for(Studenti stud: map){
            ret.add(stud);

        }
        return ret;
    }
    public List<Nota> getNotaInLista(){
        Iterable<Nota> l=getAllNote();
        List<Nota> ret=new ArrayList<>();
        for(Nota nota: l){
            ret.add(nota);
        }
        return ret;
    }
    public List<Teme> getTemeInLista(){
        Iterable<Teme> l=getAllTeme();
        List<Teme> ret=new ArrayList<>();
        for(Teme teme: l){
            ret.add(teme);

        }
        return ret;
    }
    public static <E>List<E> filtrareSortare(List<E> list, Predicate<E> pred, Comparator<E> comp){
        return list.stream().filter(pred).sorted(comp).collect(Collectors.toList());
    }
    /*public void updateDeadline(Integer NumarLaborator,Integer newDeadline){
        Domain.Teme tema=new Domain.Teme((Domain.Teme)(temeRepository.findOne(NumarLaborator)));
        if(tema.getDeadline()<=getCurrentWeek())
            throw new Repositories.RepositoryException("S-a depasit termenul admis: "+getCurrentWeek());
        else if(tema.getDeadline()>newDeadline)
            throw new Repositories.RepositoryException("Nu se poate modifica deadlineul");
        else{
        tema.setDeadline(newDeadline);
        temeRepository.update(NumarLaborator,tema);
        }
    }*/
    public Integer VerificaUser(String username,String password){
        for(LoginObject u:loginObjectRepository.findAll())
        {
            if(u.getUser().equals(username) && u.getPassword().equals(password))
                return u.getPriority();
        }
        return null;
    }

    public boolean VerificaEmail(String email){
        for(LoginObject u:loginObjectRepository.findAll()){
            if(u.getId().equals(email))
                return false;
        }
        return true;
    }

    public boolean VerificaUser(String user){
        for(LoginObject u:loginObjectRepository.findAll()){
            if(u.getUser().equals(user))
                return false;
        }
        return true;
    }
    public float calculateAverage(NoteDTO nota){
        float sum=0;
        float counter=0;
        for(Teme t: getAllTeme()){
            if(nota.getGrades().get(t.getId())!=null){
                sum=sum+nota.getGrades().get(t.getId());

            }
            counter=counter+1;
        }

        return sum/counter;
    }

    public List<GrupeTop> getTopGrupe(){
        Integer ok=0;
        List<NoteDTO> list=getAllNoteDTO();
        List<GrupeTop> listgrupe=new ArrayList<>();
        for(NoteDTO n:list){
            ok=0;
            for(GrupeTop gt: listgrupe){

                if(n.getGrupa().compareTo(gt.getGrupa())==0) {
                    gt.setMedie(gt.getMedie() + calculateAverage(n));
                    gt.setNumarStudenti(gt.getNumarStudenti()+1);
                    ok=1;
                    System.out.println("Intra pentru: "+n.getNume());
                }
            }
            if(ok==0)
                listgrupe.add(new GrupeTop(calculateAverage(n),n.getGrupa(),1));
        }
        for(GrupeTop gt:listgrupe){
            gt.setMedie(gt.getMedie()/gt.getNumarStudenti());
        }
        GrupeTop grupa;
        for (int x=0; x<listgrupe.size(); x++)
        {
            for (int i=0; i < listgrupe.size() - x - 1; i++) {
                if (listgrupe.get(i).getMedie()<listgrupe.get(i+1).getMedie())
                {

                    grupa = listgrupe.get(i);
                    listgrupe.set(i,listgrupe.get(i+1) );
                    listgrupe.set(i+1, grupa);
                }
            }
        }
        return listgrupe;
    }


    public List<TemeTop> getTopTeme() {
        List<TemeTop> teme = new ArrayList<>();
        float counter = 0;
        float medie = 0;
        for (Teme t : getAllTeme()){
            medie=0;
            counter=0;
            for (NoteDTO n : getAllNoteDTO()) {
                counter = counter + 1;
                if(n.getGrades().get(t.getId())==null)
                    medie = medie + 0;
                else
                    medie = medie + n.getGrades().get(t.getId());
            }
            teme.add(new TemeTop(t.getId(),medie/counter));
    }
    TemeTop tema;
        for (int x=0; x<teme.size(); x++)
        {
            for (int i=0; i < teme.size() - x - 1; i++) {
                if (teme.get(i).getMedie()<teme.get(i+1).getMedie())
                {

                    tema = teme.get(i);
                    teme.set(i,teme.get(i+1) );
                    teme.set(i+1, tema);
                }
            }
        }
        return teme;
    }


    public List<NoteDTO> getTopStudents(){
        List<NoteDTO> list=getAllNoteDTO();
        NoteDTO nota;

        for (int x=0; x<list.size(); x++)
        {
            for (int i=0; i < list.size() - x - 1; i++) {
                if (calculateAverage(list.get(i))<(calculateAverage(list.get(i+1))))
                {

                    nota = list.get(i);
                    list.set(i,list.get(i+1) );
                    list.set(i+1, nota);
                }
            }
        }
        return list;
    }
    public void addLoginObject(String email,String user,String password,Integer priority){
        LoginObject stud=new LoginObject(email,user,password,priority);
        loginObjectRepository.save(stud);
    }

    public void updateStudent(Integer id,String filtru,String valoare){
        //System.out.println("Intru haha");
        studentiRepository.update(id,filtru,valoare);
       // ListEvent<Studenti> ev = createEvent(ListEventType.UPDATE, studentiRepository.findOne(id), studentiRepository.findAll());
        notifyObservers(new StudentiEvent(ListEventType.UPDATE,studentiRepository.findOne(id)));
    }
    public void updateTeme(Integer id,String filtru,String valoare){

        temeRepository.update(id,filtru,valoare);
        notifyObservers(new TemeEvent(ListEventType.UPDATE,temeRepository.findOne(id)));
    }

    public void updateNota(Integer id,Integer valoare,Integer saptamanaPredare,String Observatii){
        if(valoare<=0 || valoare>10){
            throw new RepositoryException("Domain.Nota trebuie sa fie cuprinsa intre 1 si 10");
        }
        if(saptamanaPredare>14){
            throw new RepositoryException("S-a terminat semestrul!!!!!!!");
        }
        Nota nota=notaRepository.findOne(id);
        Teme tema=temeRepository.findOne(nota.getIdTema());
        Studenti stud=studentiRepository.findOne(nota.getIdStudent());

        if(tema.getDeadline()<saptamanaPredare)
            if(tema.getDeadline()==saptamanaPredare-1)
                valoare=valoare-2;
            else if (tema.getDeadline()==saptamanaPredare-2)
                valoare=valoare-4;
            else
                valoare=1;
        if(valoare<=nota.getValoare())
            throw new RepositoryException("Noua nota este mai mica decat nota initiala");
        else{
        String valoare1=Integer.toString(valoare);

        notaRepository.update(id,valoare1,"nimic");
        notifyObservers(new NoteEvent(ListEventType.UPDATE,notaRepository.findOne(id)));
        new Export("Student"+stud.getId()).export("Modificare",tema.getId(),valoare,tema.getDeadline(),saptamanaPredare,Observatii);
        }
    }

    public void adaugareNota(Integer StudentID,Integer TemaID,Integer valoare,Integer saptamanaPredare,String Observatii){
        Integer ids=0;
        for(Nota s:getAllNote()){
            if(s.getId()>ids)
                ids=s.getId();
        }
        int ok=0;
        for( Studenti stud : studentiRepository.findAll()){
            if(stud.getId()==StudentID)
            {ok=1;
            break;}
        }
        if(ok==0) {
            throw new RepositoryException("Studentul cu id-ul acesta nu exista");
        }
        ok=0;
        for( Teme stud : temeRepository.findAll()){
            if(stud.getId()==TemaID)
            {
                ok=1;
                break;}
        }
        if(ok==0) {
            throw new RepositoryException("Tema cu id-ul acesta nu exista");
        }
        for (Nota nota : notaRepository.findAll()){
            if(nota.getIdTema()==TemaID && nota.getIdStudent()==StudentID){
                throw new RepositoryException("Studentul a primit deja nota pe aceasta tema");
            }
        }
        if(valoare<=0 || valoare>10){
            throw new RepositoryException("Domain.Nota trebuie sa fie cuprinsa intre 1 si 10");
        }
        if(saptamanaPredare>14){
            throw new RepositoryException("S-a terminat semestrul!!!!!!!");
        }
        if(temeRepository.findOne(TemaID).getDeadline()<saptamanaPredare)
            if(temeRepository.findOne(TemaID).getDeadline()==saptamanaPredare-1)
                valoare=valoare-2;
            else if (temeRepository.findOne(TemaID).getDeadline()==saptamanaPredare-2)
                valoare=valoare-4;
            else
                valoare=1;
        Nota nota=new Nota(ids+1,StudentID,TemaID,valoare);

        notaRepository.save(nota);
        notifyObservers( new NoteEvent(ListEventType.ADD,nota));
        new Export("Student"+StudentID).export("Adaugare",TemaID,valoare,temeRepository.findOne(TemaID).getDeadline(),saptamanaPredare,Observatii);
    }

    public Iterable<Teme> getAllTeme(){
        return temeRepository.findAll();
    }
    public Iterable<Nota> getAllNote(){
        return notaRepository.findAll();
    }

//De adaugat builder pentru creare obiect pentru exportare

public List<Studenti> filterEmail(String email){
    Predicate<Studenti> s = x->x.getEmail().startsWith(email);
    Comparator<Studenti> comp = (x,y) ->x.getNume().compareTo(y.getNume());
    return filtrareSortare(getStudInLista(),s,comp);
}

public List<Studenti> filterGrupa(int grupa){
    Predicate<Studenti> s =x->Integer.toString(x.getGrupa()).startsWith(Integer.toString(grupa));
    Comparator<Studenti> comp=(x,y)->x.getNume().compareTo(y.getNume());
    return filtrareSortare(getStudInLista(),s,comp);
}

public List<Studenti> filterCadruDidactic(String profesor){
    Predicate<Studenti> s =x->x.getCadruDidactic().startsWith(profesor);
    Comparator<Studenti> comp=(x,y)->x.getNume().compareTo(y.getNume());
    return filtrareSortare(getStudInLista(),s,comp);
}

    public List<Studenti> filterNume(String nume){
        Predicate<Studenti> s =x->x.getNume().startsWith(nume);
        Comparator<Studenti> comp=(x,y)->x.getNume().compareTo(y.getNume());
        return filtrareSortare(getStudInLista(),s,comp);
    }


public List<Teme> filterCerinta(String cerinta){
    Predicate<Teme> t =x->x.getCerinta().startsWith(cerinta);
    Comparator<Teme> comp=(x,y)->x.getId().compareTo(y.getId());
    return filtrareSortare(getTemeInLista(),t,comp);
}

public List<Teme> filterDeadline(int deadline){
    Predicate<Teme> t =x->Integer.toString(x.getDeadline()).startsWith(Integer.toString(deadline));
    Comparator<Teme> comp=(x,y)->x.getId().compareTo(y.getId());
    return filtrareSortare(getTemeInLista(),t,comp);
}
public List<Teme> filterNrTema(int id){
    Predicate<Teme> t =x->Integer.toString(x.getId()).startsWith(Integer.toString(id));
    Comparator<Teme> comp=(x,y)->x.getId().compareTo(y.getId());
    return filtrareSortare(getTemeInLista(),t,comp);
}

public List<Teme> filterDeadlineBetween(int d,int d2){
    Predicate<Teme> t =x->(x.getDeadline()>=d && x.getDeadline()<=d2);
    Comparator<Teme> comp=(x,y)->x.getDeadline().compareTo(y.getDeadline());
    return filtrareSortare(getTemeInLista(),t,comp);
}

public List<NoteDTO> filterDTONume(String nume){
    Predicate<NoteDTO> t =x->x.getNume().startsWith(nume);
    Comparator<NoteDTO> comp=(x,y)->x.getNume().compareTo(y.getNume());
    return filtrareSortare(getAllNoteDTO(),t,comp);
}
    public List<NoteDTO> filterDTOGrupa(int grupa){
        Predicate<NoteDTO> t =x->Integer.toString(x.getGrupa()).startsWith(Integer.toString(grupa));
        Comparator<NoteDTO> comp=(x,y)->x.getNume().compareTo(y.getNume());
        return filtrareSortare(getAllNoteDTO(),t,comp);
    }



public List<Nota> filterIdStudent(int is){
    Predicate<Nota> n =x->x.getIdStudent()==is;
    Comparator<Nota> comp=(x,y)->x.getValoare().compareTo(y.getValoare());
    return filtrareSortare(getNotaInLista(),n,comp);
}

public List<Nota> filterIdTema(int is){
    Predicate<Nota> n =x->x.getIdTema()==is;
    Comparator<Nota> comp=(x,y)->x.getValoare().compareTo(y.getValoare());
    return filtrareSortare(getNotaInLista(),n,comp);
}

public List<Nota> filterSubNota(int is){
    Predicate<Nota> n =x->x.getValoare()<is;
    Comparator<Nota> comp=(x,y)->x.getValoare().compareTo(y.getValoare());
    return filtrareSortare(getNotaInLista(),n,comp);
}


public List<NoteDTO> getAllNoteDTO(){
    List<NoteDTO> dtogrades=new ArrayList<>();
    for(Studenti st:studentiRepository.findAll()){
        HashMap<Integer,Integer> grades=new HashMap<Integer,Integer>();
        for(Nota nota:notaRepository.findAll()){
            if(nota.getIdStudent()==st.getId()){
                grades.put(nota.getIdTema(),nota.getValoare());
            }
        }
        NoteDTO dtograde=new NoteDTO(st,grades);
        dtogrades.add(dtograde);
    }
    return dtogrades;

    }

}