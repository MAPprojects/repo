package Service;

import Domain.*;
import Repository.*;

import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Service {

    ArrayList<Observer<Studenti>> StudentiObserverList=new ArrayList<>();

//    private TemeRepoSQL repoTeme;
//    private NoteRepoSQL noteRepo;
//    private StudentRepoSQL repoStudenti;
    private PenalizareRepoSQL repoPenalizari=new PenalizareRepoSQL();

    private ServiceNote noteService;
    private ServiceTeme temeService;
    private ServiceStudenti studentiService;
    public Service(ServiceNote noteService, ServiceTeme temeService, ServiceStudenti studentiService) {
        this.noteService =  noteService;
        this.temeService=temeService;
        this.studentiService=studentiService;


    }


    public List<Studenti> createStudentiMedie(){
        List<Studenti> lista= new ArrayList<>();

        for (Studenti studenti : studentiService.getAllStudents()) {
            float medie=0;
            int contor=0;
            for (Note note : noteService.getAllNote())
                if (studenti.getIdStudent() == note.getIdStudent()) {
                    medie += note.getValoare();
                    contor += 1;
                }
            medie=medie/contor;
            lista.add(new Studenti(studenti.getIdStudent(),studenti.getNume(),studenti.getGrupa(),studenti.getEmail(),studenti.getIndrumator(),medie));
        }
        return  lista;

    }
    public List<Studenti> studentiCarePotIntraInExamen(){
        ObservableList<Studenti> listaExamen=FXCollections.observableArrayList();
        List<Studenti>listaStudenti =createStudentiMedie();
        for (Studenti student: listaStudenti)
            if (student.getMedie()>5.0)
                if(areToateTemelePredate(student))
                     listaExamen.add(student);

        return listaExamen;
    }
    public List<Studenti> predateLaTimp(){

        List studentiPenalizati=new ArrayList();
        List<Studenti> allStudents=new ArrayList<Studenti>();
        List<Studenti> listaPenalizari=new ArrayList<Studenti>();
        for (Penalizare pena : repoPenalizari.getAll())
        {
            listaPenalizari.add(studentiService.getById(pena.getIdStudent()));

        }
        for (Studenti stud :studentiService.getAllStudents())
            if (!listaPenalizari.contains(stud))
                allStudents.add(stud);
//        for(Studenti stud:studentiService.getAllStudents())
//            for (Penalizare pen:repoPenalizari.getAll())
//                if(stud.getIdStudent()!=pen.getIdStudent())
//
//                        allStudents.add(stud.getIdStudent());
//
        return allStudents;
    }
    public float getByIdSt(int id){
        for (Studenti student : createStudentiMedie())
            if(student.getIdStudent()==id)
                return student.getMedie();
        return 0;
    }
    public boolean areToateTemelePredate(Studenti stud){
        int nrNote=0;
        for(Note nota :noteService.getAllNote())
            if(nota.getIdStudent()==stud.getIdStudent())
                nrNote+=1;
        if (nrNote==temeService.getAllTeme().size())
            return true;
        else
            return false;
    }

    public List<Studenti> primii10Studenti(){
        ObservableList<Studenti> listaExamen=FXCollections.observableArrayList();
        List<Studenti>listaStudenti =createStudentiMedie();
        int contor=10;

        while (contor>0) {
            for (Studenti student : listaStudenti)
                if (student.getMedie() > 5.0)
                    if (areToateTemelePredate(student)) {
                        listaExamen.add(student);
                        contor-=1;
                    }
        }
        listaExamen.sort(Comparator.comparing(Studenti::getMedie).reversed());
        return listaExamen;

    }

//    @Override
//    public void addObserver(Observer<Studenti> o) {
//        StudentiObserverList.add(o);
//
//    }
//
//    @Override
//    public void removeObserver(Observer<Studenti> o) {
//        StudentiObserverList.remove(o);
//    }
//
//    @Override
//    public void notifyObservers(ListEvent<Studenti> event) {
//        StudentiObserverList.forEach(x->x.notifyEvent(event));
//    }
//    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final List<E> l){
//        return new ListEvent<E>(type) {
//            @Override
//            public List<E> getList() {
//                return l;
//            }
//            @Override
//            public E getElement() {
//                return elem;
//            }
//        };
//    }
//
//
//
//    public List<Studenti> getAllStundetiv2()
//    {
//        return StreamSupport.stream(repoStudenti.getAll().spliterator(), false)
//                .collect(Collectors.toList());
//    }
//    public List<Teme> getAllTemeV2()
//    {
//        return StreamSupport.stream(repoTeme.getAll().spliterator(), false)
//                .collect(Collectors.toList());
//    }
//    public List<Note> getALlNoteV2()
//    {
//        return StreamSupport.stream(noteRepo.getAll().spliterator(), false)
//                .collect(Collectors.toList());
//    }





}
